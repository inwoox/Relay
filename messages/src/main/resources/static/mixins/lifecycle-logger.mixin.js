
// 믹스인은 컴포넌트 외에 코드를 재사용하는 또 다른 방법, 컴포넌트의 options 객체에 믹스인을 포함시켜 사용한다. 
// 믹스인과 컴포넌트가 둘 다 같은 라이프 사이클 훅을 포함하면, 두 메서드를 배열에 넣고 믹스인의 메서드를 먼저 호출한다.
// 믹스인을 전역으로 적용하면, 이후에 생성되는 모든 Vue 인스턴스에 영향을 미친다.
export default {
  created() {
    console.log(`${this.$options.name} created`);
  },
  beforeMount() {
    console.log(`${this.$options.name} about to mount`);
  },
  mounted() {
    console.log(`${this.$options.name} mounted`);
  },
  destroyed() {
    console.log(`${this.$options.name} destroyed`);
  }
}