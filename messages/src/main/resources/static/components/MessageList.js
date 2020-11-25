
// ES6 모듈 (컴포넌트)
// 컴포넌트를 정의하는 options 객체를 기본 내보내기로 내보낸다.
import MessageListItem from './MessageListItem.js';
export default {
  name: 'MessageList',
  // :item은 이 컴포넌트에서, MessageListItem 컴포넌트로 props을 전달하는 것
  // :key는 리스트에서 어떤 아이템이 변경되었는지 추적해, DOM에서 해당 부분만 업데이트하도록, 리스트에서 아이템의 고유 아이디가 될 수 있는 키를 지정
  template: `<div><ul><message-list-item v-for="item in items"
    :item="item" :key="item.id"@delete="deleteMessage(item)">
    </message-list-item></ul></div>`,
  props: {
    items: {
      type: Array,
      required: true
    }
  },
  components: {
    MessageListItem
  },
  methods: {
    deleteMessage(message) {
      this.$emit('delete', message);
    }
  }
}
