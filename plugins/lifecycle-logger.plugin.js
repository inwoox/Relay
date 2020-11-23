
const switchers = {
  created: true,
  beforeMount: true,
  mounted: true,
  destroyed: true
}

export default {
  install(Vue, options) {
    Object.assign(switchers, options) // 이 메서드로 options 객체 내의 switchers를 미리 정의된 switchers에 병합.
    Vue.mixin({                       // 라이프 사이클 훅 함수를 전역 믹스인으로 적용한다.
      created() {
        if (switchers.created) {
          console.log(`${this.$options.name} created`)
        }
      },
      beforeMount() {
        if (switchers.beforeMount) {
          console.log(`${this.$options.name} about to mount`)
        }
      },
      mounted() {
        if (switchers.mounted) {
          console.log(`${this.$options.name} mounted`)
        }
      },
      destroyed() {
        if (switchers.destroyed) {
          console.log(`${this.$options.name} destroyed`)
        }
      }
    })
  }
}