
// 필터를 이용해 이중 중괄호 보간법 (콧수염 표기법)이나 v-bind 표현법을 이용할 때 텍스트 형식을 지정할 수 있다.
// Vue.filter를 사용하여 전역으로 등록하거나, 컴포넌트의 options 객체의 filters 프로퍼티를 통해 로컬로 등록할 수 있다.
const formatter = new Intl.DateTimeFormat('en-US', {
  year: 'numeric', month: 'long', week: 'long', day: 'numeric', hour: 'numeric', minute: 'numeric', second: 'numeric'
});

Vue.filter('datetime', function (value, pattern) {
  if (!value) return '';
  return formatter.format(value);
});