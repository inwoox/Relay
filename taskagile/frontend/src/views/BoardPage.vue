<template>
  <div class="page">
    <PageHeader />
    <div class="page-body">
      <div class="board-wrapper">
        <div class="board">
          <div class="board-header clearfix">
            <div class="board-name board-header-item">{{ board.name }}</div>
            <div class="board-header-divider"></div>
            <div class="team-name board-header-item">
              <span v-if="!board.personal">{{ team.name }}</span>
              <span v-if="board.personal">Personal</span>
            </div>
            <div class="board-header-divider"></div>
            <div class="board-members board-header-item">
              <div class="member" v-for="member in members" v-bind:key="member.id">
                <span>{{ member.shortName }}</span>
              </div>
              <div class="member add-member-toggle" @click="openAddMember()">
                <span><font-awesome-icon icon="user-plus" /></span>
              </div>
            </div>
          </div>
          <div class="board-body">
            <draggable v-model="cardLists" class="list-container" @end="onCardListDragEnded"
              :options="{handle: '.list-header', animation: 0, scrollSensitivity: 100, touchStartThreshold: 20}">
              <div class="list-wrapper" v-for="cardList in cardLists" v-bind:key="cardList.id">
                <div class="list">
                  <div class="list-header">{{ cardList.name }}</div>
                  <draggable class="cards" v-model="cardList.cards" @end="onCardDragEnded"
                    :options="{draggable: '.card-item', group: 'cards', ghostClass: 'ghost-card',
                    animation: 0, scrollSensitivity: 100, touchStartThreshold: 20}"
                    v-bind:data-list-id="cardList.id">
                    <div class="card-item" v-for="card in cardList.cards" v-bind:key="card.id">
                      <div class="card-title">{{ card.title }}</div>
                    </div>
                    <div class="add-card-form-wrapper" v-if="cardList.cardForm.open">
                      <form @submit.prevent="addCard(cardList)" class="add-card-form">
                        <div class="form-group">
                          <textarea class="form-control" v-model="cardList.cardForm.title" v-bind:id="'cardTitle' + cardList.id"
                            @keydown.enter.prevent="addCard(cardList)" placeholder="Type card title here"></textarea>
                        </div>
                        <button type="submit" class="btn btn-sm btn-primary">Add</button>
                        <button type="button" class="btn btn-sm btn-link btn-cancel" @click="closeAddCardForm(cardList)">Cancel</button>
                      </form>
                    </div>
                  </draggable>
                  <div class="add-card-button" v-show="!cardList.cardForm.open" @click="openAddCardForm(cardList)">+ Add a card</div>
                </div>
              </div>
              <div class="list-wrapper add-list">
                <div class="add-list-button" v-show="!addListForm.open" @click="openAddListForm()">+ Add a list</div>
                <form @submit.prevent="addCardList()" v-show="addListForm.open" class="add-list-form">
                  <div class="form-group">
                    <input type="text" class="form-control" v-model="addListForm.name" id="cardListName" placeholder="Type list name here" />
                  </div>
                  <button type="submit" class="btn btn-sm btn-primary">Add List</button>
                  <button type="button" class="btn btn-sm btn-link btn-cancel" @click="closeAddListForm()">Cancel</button>
                </form>
              </div>
            </draggable>
          </div>
        </div>
      </div>
    </div>
    <AddMemberModal
      :boardId="board.id"
      @added="onMemberAdded"/>
    <CardModal
    :card="openedCard"
    :cardList="focusedCardList"
    :board="board"
    :members="members"
    @coverImageChanged="updateCardCoverImage"/>
  </div>
</template>

<script>
import draggable from 'vuedraggable'
import $ from 'jquery'
import PageHeader from '@/components/PageHeader.vue'
import AddMemberModal from '@/modals/AddMemberModal.vue'
import CardModal from '@/modals/CardModal.vue'
import notify from '@/utils/notify'
import boardService from '@/services/boards'
import cardListService from '@/services/card-lists'
import cardService from '@/services/cards'

export default {
  name: 'BoardPage',
  data () {
    return {
      board: { id: 0, name: '', personal: false },
      cardLists: [/* {id, name, cards, cardForm} */],
      team: { name: '' },
      members: [/* {id, shortName} */],
      addListForm: {
        open: false,
        name: ''
      }
    }
  },
  components: {
    PageHeader,
    AddMemberModal,
    draggable
  },
  // 보드의 로딩 뿐만 아니라 실시간 업데이트 채널로부터 구독 해지를 발생시키기 위해,
  // vue-router의 다음 네비게이션 가드에 의존했었지만,

  // beforeRouteEnter (to, from, next) {     // 이 컴포넌트를 렌더링하는 라우트 앞에 호출
  //   next(vm => {
  //     vm.loadBoard()
  //   })
  // },
  // beforeRouteUpdate (to, from, next) {    // 이 컴포넌트를 렌더링하는 라우트가 업데이트될 때 호출
  //   next()
  //   this.unsubscribeFromRealTimeUpdate()
  //   this.loadBoard()
  // },
  // beforeRouteLeave (to, from, next) {     // 이 컴포넌트를 렌더링하는 라우트가 이전으로 네비게이션될 때 호출
  //   next()
  //   this.unsubscribeFromRealTimeUpdate()
  // },

  // 지금은 보드 URL로 페이지를 열고나서, 카드를 열고 닫은 다음 다른 카드로 이동하거나, 
  // 카드 URL로 페이지를 열고 맨 위에 있는 Boards 메뉴를 활용해 다른 보드로 이동해도 보드 URL과 카드 URL 두 경로를
  // BoardPage 컴포넌트로 매핑하기 때문에, vue-router 라이브러리가 beforeRouteUpdate 가드를 호출하지 않는다.

  // vue-router 문서에 따르면, beforeRouteUpdate 가드는 /board/1에서 /board/2로 이동할 경우 호출되지만
  // /board/1에서 /card/1/card-title1로 이동할 경우에는 호출되지 않는다.
  // 그러므로 BoardPage 페이지에 머물 때 경로의 변경사항을 감지하기 위해서는 네비게이션 가드에 의존할 수 없으며,
  // 경로의 변경 사항을 감지하기 위해 다음과 같이 this.$route 객체를 지켜봐야한다. 

  // 보다시피 to.name과 from.name을 활용해 보드 간의 전환, 카드 열기, 카드 닫기라는 세 개의 시나리오를 감지
  // 와처를 활용하면 beforeRouteEnter, beforeRouteUpdate 네비게이션 가드가 필요 없다.
  // beforeRouteLeave 가드는 보드 페이지에서 나가는 것을 감지하기 위해 여전히 필요, 이것은 $route로는 감지할 수 없다.
  watch: {
    '$route' (to, from) {
      // Switch from one board to another
      if (to.name === from.name && to.name === 'board') {
        this.unsubscribeFromRealTimeUpdate(from.params.boardId)
        this.loadBoard(to.params.boardId)
      }
      // Open a card
      if (to.name === 'card' && from.name === 'board') {
        this.loadCard(to.params.cardId).then(() => {
          this.openCardWindow()
        })
      }
      // Close a card
      if (to.name === 'board' && from.name === 'card') {
        this.closeCardWindow()
        this.openedCard = {}
      }
    }
  },
  beforeRouteLeave (to, from, next) {
    console.log('[BoardPage] Before route leave')
    next()
    if (to.name !== 'card') {
      this.unsubscribeFromRealTimeUpdate(this.board.id)
    }
  },

  // BoardPage 컴포넌트 인스턴스의 mounted 훅에서 사용자가 
  // 보드 URL 또는 카드 URL을 바로 열 경우 또는 페이지를 새로 고칠 경우, 보드 페이지의 데이터 로딩을 시작해야한다.
  // 이를 위해 아래와 같이 리팩토링한다.
  // mounted () {
  //   this.$el.addEventListener('click', this.dismissActiveForms)
  // },
  mounted () {
    console.log('[BoardPage] Mouted')
    
    // BoardPage가 마운트 됐을 때, 서버로부터 데이터를 로드하기 위해 loadInitial() 메서드를 호출한다.
    this.loadInitial() 
    this.$el.addEventListener('click', this.dismissActiveForms)
    
    // 카드 모달창을 닫으면 보드 URL로 변경하기 위해 이벤트를 바인드한다. 
    $('#cardModal').on('hide.bs.modal', () => {
      this.$router.push({name: 'board', params: {boardId: this.board.id}})
    })
  },
  beforeDestroy () {
    this.$el.removeEventListener('click', this.dismissActiveForms)
  },
  methods: {
    loadInitial () {
      // The board page can be opened through a card URL.
      if (this.$route.params.cardId) {
        console.log('[BoardPage] Opened with card URL')
        this.loadCard(this.$route.params.cardId).then(card => {
          return this.loadBoard(card.boardId)
        }).then(() => {
          this.openCardWindow()
        })
      } else {
        console.log('[BoardPage] Opened with board URL')
        this.loadBoard(this.$route.params.boardId)
      }
    },
    loadCard (cardId) {
      return new Promise(resolve => {
        console.log('[BoardPage] Loading card ' + cardId)
        cardService.getCard(cardId).then(card => {
          this.openedCard = card
          resolve(card)
        }).catch(error => {
          notify.error(error.message)
        })
      })
    },
    loadBoard (boardId) {
      return new Promise(resolve => {
        console.log('[BoardPage] Loading board ' + boardId)
        boardService.getBoard(boardId).then(data => {
          this.team.name = data.team ? data.team.name : ''
          this.board.id = data.board.id
          this.board.personal = data.board.personal
          this.board.name = data.board.name

          this.members.splice(0)

          data.members.forEach(member => {
            this.members.push({
              id: member.userId,
              name: member.name,
              shortName: member.shortName
            })
          })

          this.cardLists.splice(0)

          data.cardLists.sort((list1, list2) => {
            return list1.position - list2.position
          })

          data.cardLists.forEach(cardList => {
            cardList.cards.sort((card1, card2) => {
              return card1.position - card2.position
            })

            this.cardLists.push({
              id: cardList.id,
              name: cardList.name,
              cards: cardList.cards,
              cardForm: {
                open: false,
                title: ''
              }
            })
          })
          this.subscribeToRealTimUpdate(data.board.id)
          resolve()
        }).catch(error => {
          notify.error(error.message)
        })
      })
    },
    dismissActiveForms (event) {
      console.log('[BoardPage] Dismissing forms')
      let dismissAddCardForm = true
      let dismissAddListForm = true
      if (event.target.closest('.add-card-form') || event.target.closest('.add-card-button')) {
        dismissAddCardForm = false
      }
      if (event.target.closest('.add-list-form') || event.target.closest('.add-list-button')) {
        dismissAddListForm = false
      }
      if (dismissAddCardForm) {
        this.cardLists.forEach((cardList) => { cardList.cardForm.open = false })
      }
      if (dismissAddListForm) {
        this.addListForm.open = false
      }
    },
    openAddMember () {
      $('#addMemberModal').modal('show')
    },
    onMemberAdded (member) {
      this.members.push(member)
    },
    addCardList () {
      if (!this.addListForm.name) {
        return
      }
      const cardList = {
        boardId: this.board.id,
        name: this.addListForm.name,
        position: this.cardLists.length + 1
      }
      cardListService.add(cardList).then(savedCardList => {
        this.cardLists.push({
          id: savedCardList.id,
          name: savedCardList.name,
          cards: [],
          cardForm: {
            open: false,
            title: ''
          }
        })
        this.closeAddListForm()
      }).catch(error => {
        notify.error(error.message)
      })
    },
    addCard (cardList) {
      if (!cardList.cardForm.title.trim()) {
        return
      }

      const card = {
        boardId: this.board.id,
        cardListId: cardList.id,
        title: cardList.cardForm.title,
        position: cardList.cards.length + 1
      }

      cardService.add(card).then(savedCard => {
        this.appendCardToList(cardList, savedCard)
        cardList.cardForm.title = ''
        this.focusCardForm(cardList)
      }).catch(error => {
        notify.error(error.message)
      })
    },
    openAddListForm () {
      this.addListForm.open = true
      this.$nextTick(() => {
        $('#cardListName').trigger('focus')
      })
    },
    closeAddListForm () {
      this.addListForm.open = false
      this.addListForm.name = ''
    },
    openAddCardForm (cardList) {
      // Close other add card form
      this.cardLists.forEach((cardList) => { cardList.cardForm.open = false })
      cardList.cardForm.open = true
      this.focusCardForm(cardList)
    },
    focusCardForm (cardList) {
      this.$nextTick(() => { $('#cardTitle' + cardList.id).trigger('focus') })
    },
    closeAddCardForm (cardList) {
      cardList.cardForm.open = false
    },
    onCardListDragEnded (event) {
      console.log('[BoardPage] Card list drag ended', event)

      // Get the latest card list order and send it to the back-end
      const positionChanges = {
        boardId: this.board.id,
        cardListPositions: []
      }

      this.cardLists.forEach((cardList, index) => {
        positionChanges.cardListPositions.push({
          cardListId: cardList.id,
          position: index + 1
        })
      })

      cardListService.changePositions(positionChanges).catch(error => {
        notify.error(error.message)
      })
    },
    onCardDragEnded (event) {
      console.log('[BoardPage] Card drag ended', event)
      // Get the card list that have card orders changed
      const fromListId = event.from.dataset.listId
      const toListId = event.to.dataset.listId
      const changedListIds = [fromListId]
      if (fromListId !== toListId) {
        changedListIds.push(toListId)
      }

      const positionChanges = {
        boardId: this.board.id,
        cardPositions: []
      }

      changedListIds.forEach(cardListId => {
        const cardList = this.cardLists.filter(cardList => { return cardList.id === parseInt(cardListId) })[0]

        cardList.cards.forEach((card, index) => {
          positionChanges.cardPositions.push({
            cardListId: cardListId,
            cardId: card.id,
            position: index + 1
          })
        })
      })

      cardService.changePositions(positionChanges).catch(error => {
        notify.error(error.message)
      })
    },

    // SockJS 객체를 감싸는 RealTimeClient 클래스를 이용해, 특정 보드가 로드될 때, 이 메서드를 호출해 보드를 구독한다.
    subscribeToRealTimUpdate () {
      this.$rt.subscribe('/board/' + this.board.id, this.onRealTimeUpdated)
    },
    unsubscribeFromRealTimeUpdate () {
      this.$rt.unsubscribe('/board/' + this.board.id, this.onRealTimeUpdated)
    },
    onRealTimeUpdated (update) {
      console.log('[BoardPage] Real time update received', update)
      if (update.type === 'cardAdded') {
        this.onCardAdded(update.card)
      }
    },
    onCardAdded (card) {
      const cardList = this.cardLists.filter(cardList => { return cardList.id === card.cardListId })[0]
      if (!cardList) {
        console.warn('No card list found by id ' + card.cardListId)
        return
      }
      this.appendCardToList(cardList, card)
    },
    appendCardToList (cardList, card) {
      const existingIndex = cardList.cards.findIndex(existingCard => { return existingCard.id === card.id })
      if (existingIndex === -1) {
        cardList.cards.push({
          id: card.id,
          title: card.title
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.page-body {
  flex-grow: 1;
  position: relative;
  overflow-y: auto;

  .board-wrapper {
    position: absolute;
    left: 0;
    right: 0;
    top: 0;
    bottom: 0;

    .board {
      height: 100%;
      display: flex;
      flex-direction: column;

      .board-header {
        flex: none;
        height: auto;
        overflow: hidden;
        position: relative;
        padding: 8px 4px 8px 8px;

        .board-header-divider {
          float: left;
          border-left: 1px solid #ddd;
          height: 16px;
          margin: 8px 10px;
        }

        .board-header-item {
          float: left;
          height: 32px;
          line-height: 32px;
          margin: 0 4px 0 0;
        }

        .board-name {
          font-size: 18px;
          line-height: 32px;
          padding-left: 4px;
          text-decoration: none;
        }

        .board-members {
          .member {
            display: block;
            float: left;
            height: 30px;
            width: 30px;
            margin: 0 0 0 -2px;
            border-radius: 50%;
            background-color: #377EF6;
            position: relative;

            span {
              height: 30px;
              line-height: 30px;
              width: 30px;
              text-align: center;
              display: block;
              color: #fff;
            }
          }

          .add-member-toggle {
            margin-left: 5px;
            background-color: #eee;
            cursor: pointer;

            svg {
              font-size: 10px;
              position: absolute;
              top: 9px;
              left: 9px;
              color: #000;
            }
          }

          .add-member-toggle:hover {
            background-color: #666;

            svg {
              color: #fff;
            }
          }
        }
      }

      .board-body {
        position: relative;
        flex-grow: 1;

        .list-container {
          position: absolute;
          top: 0;
          left: 8px;
          right: 0;
          bottom: 0;
          overflow-x: auto;
          overflow-y: hidden;
          white-space: nowrap;
          margin-bottom: 6px;
          padding-bottom: 6px;

          .list-wrapper {
            width: 272px;
            margin: 0 4px;
            height: 100%;
            box-sizing: border-box;
            display: inline-block;
            vertical-align: top;
            white-space: nowrap;

            .list {
              background: #eee;
              border-radius: 3px;
              box-sizing: border-box;
              display: flex;
              flex-direction: column;
              max-height: 100%;
              white-space: normal;
              position: relative;

              .list-header {
                padding: .55rem .75rem;
                font-weight: 600;
                cursor: pointer;
              }

              .add-card-button {
                padding: 8px 10px;
                color: #888;
                cursor: pointer;
                border-bottom-left-radius: 3px;
                border-bottom-right-radius: 3px;
              }

              .add-card-button:hover {
                background: #dfdfdf;
                color: #333;
              }

              .add-card-form-wrapper {
                padding: 0 8px 8px;

                .form-group {
                  margin-bottom: 5px;

                  textarea {
                    resize: none;
                    padding: 0.30rem 0.50rem;
                    box-shadow: none;
                  }
                }
              }

              .cards {
                overflow-y: auto;
                min-height: 1px;

                .card-item {
                  overflow: hidden;
                  background: #fff;
                  padding: 5px 8px;
                  border-radius: 4px;
                  margin: 0 8px 8px;
                  box-shadow: 0 1px 0 #ccc;
                  cursor: pointer;

                  .card-title {
                    margin: 0;
                  }
                }

                .ghost-card {
                  background-color: #ccc !important;
                  color: #ccc !important;
                }
              }
            }

            .ghost-list .list {
              background: #aaa;
            }
          }

          .list-wrapper.add-list {
            background: #f4f4f4;
            border-radius: 3px;
            box-sizing: border-box;
            height: auto;
            color: #888;
            margin-right: 8px;

            .add-list-button {
              padding: 8px 10px;
            }

            .add-list-button:hover {
              background: #ddd;
              cursor: pointer;
              border-radius: 3px;
              color: #333;
            }

            form  {
              padding: 5px;

              .form-group {
                margin-bottom: 5px;

                .form-control {
                  height: calc(1.80rem + 2px);
                  padding: .375rem .3rem;
                }
              }
            }
          }
        }
      }
    }
  }
}
</style>
