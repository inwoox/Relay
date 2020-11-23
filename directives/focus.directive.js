
// 지시자 - 표현식의 값이 변경될 때 이에 반응해 DOM에 변경 사항을 적용한다.
Vue.directive('focus', {
  // 바인딩된 요소가 DOM으로 삽입될 때
  inserted: function (el) {
    // 요소에 초점을 둔다.
    el.focus();
  }
});