
// HomePage.vue와 CreateBoardModal.vue의 책임은 무엇인가?

// 보드 생성 창은 홈페이지에서 나타나기 때문에, 자연스럽게 팝업창을 띄우는 것은 HomePage.vue의 책임이다
// 따라서 CreateBoardModal.vue를 HomePage.vue로 가져오고 부트스트랩한 뒤 Create New Board 버튼이 클릭되면 팝업 창을 띄운다.
// HomePage.vue는 보드가 생성 되면 보드 페이지로 사용자를 리다이렉트한다.
// CreateBoardModal.vue는 보드를 저장하기 위해 boardService.create() 메서드를 호출한 다음, 요청이 성공하면
// addBoard 액션을 Vuex 스토어에 디스패치하거나 요청이 실패하면 팝업 창에 에러 메시지를 표시할 책임이 있다.

// saveBoard 메서드에서 데이터 검증을 수행하고, 모든 필드가 유효하면, 보드 서비스의 create 메서드를 통해 API 호출
// 정상적으로 호출 되면, addBoard 액션을 디스패치하여, mutations을 통해 상태를 변경
// created 이벤트를 발생시켜, HomePage에서 받아, board 페이지로 사용자를 리다이렉트하게 하면서,
// 새로 생성된 보드의 id를 전달하고, 모달 창을 닫는다.

<template>
  <form @submit.prevent="saveBoard">
    <div class="modal" tabindex="-1" role="dialog" backdrop="static" id="createBoardModal">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Create Board</h5>
            <button type="button" class="close" @click="close" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <div v-show="errorMessage" class="alert alert-danger failed">{{ errorMessage }}</div>
            <div class="form-group">
              <input type="text" class="form-control" id="boardNameInput" v-model="board.name" placeholder="Board name" maxlength="128">
              <div class="field-error" v-if="$v.board.name.$dirty">
                <div class="error" v-if="!$v.board.name.required">Name is required</div>
              </div>
            </div>
            <div class="form-group">
              <textarea class="form-control" v-model="board.description" placeholder="Add board description here"></textarea>
              <div class="field-error" v-if="$v.board.description.$dirty">
                <div class="error" v-if="!$v.board.description.required">Description is required</div>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button type="submit" class="btn btn-primary">Create</button>
            <button type="button" class="btn btn-default btn-cancel" @click="close">Cancel</button>
          </div>
        </div>
      </div>
    </div>
  </form>
</template>

<script>
import $ from 'jquery'
import { required } from 'vuelidate/lib/validators'
import boardService from '@/services/boards'

export default {
  name: 'CreateBoardModal',
  props: ['teamId'], // HomePage.vue에서 이 팝업 창이 열릴 때, teamId를 속성으로 받는다.
  data () {
    return {
      board: {
        name: '',
        description: ''
      },
      errorMessage: ''
    }
  },
  validations: {
    board: {
      name: {
        required
      },
      description: {
        required
      }
    }
  },
  mounted () { // 모달 창이 열리면 boardNameInput 필드에 포커스가 위치하게 만든다.
    $('#createBoardModal').on('shown.bs.modal', () => {
      $('#boardNameInput').trigger('focus')
    })
  },
  methods: {
    saveBoard () {
      this.$v.$touch()
      if (this.$v.$invalid) {
        return
      }
      const board = {
        teamId: this.teamId,
        name: this.board.name,
        description: this.board.description
      }

      boardService.create(board).then((createdBoard) => {
        this.$store.dispatch('addBoard', createdBoard)
        this.$emit('created', createdBoard.id)
        this.close()
      }).catch(error => {
        this.errorMessage = error.message
      })
    },
    close () {
      this.$v.$reset()
      this.board.name = ''
      this.board.description = ''
      this.errorMessage = ''
      $('#createBoardModal').modal('hide')
    }
  }
}
</script>

<style lang="scss" scoped>
.modal {
  .modal-dialog {
    width: 400px;
  }
}
</style>
