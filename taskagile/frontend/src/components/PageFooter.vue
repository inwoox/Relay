
// footer
  // copyright
  // footer-links list-inline float-right
    // list-inline-item * 4

<template>
  <footer class="footer">
    <span class="copyright">&copy; 2020 TaskAgile.com</span>
    <ul class="footer-links list-inline float-right"> <!-- footer-links와 list-inline은 설명 용도 / float-right는 유틸리티 클래스 -->
      <li class="list-inline-item"><a href="#">About</a></li>
      <li class="list-inline-item"><a href="#">서비스 약관</a></li>
      <li class="list-inline-item"><a href="#">개인정보보호정책</a></li>
      <li class="list-inline-item"><a href="https://github.com/taskagile/vuejs.spring-boot.mysql" target="_blank">GitHub</a></li>
    </ul>
  </footer>
</template>

<script>
  export default {
    name: 'PageFooter'
  }
</script>

// CSS도 순서대로 적용
<style lang="scss" scoped>
.footer {
  width: 100%; font-size: 13px;
  margin-top: 10%;                            // 줄 위 공간 띄우기
  border-top: 1px solid #ddd;               // 위에 줄 만들기
  line-height: 40px;                          // 줄에서 내용 띄우기
  .list-inline-item { margin-right: 10px; }   // 오른쪽 공간 띄우기
  a { color: #666; }                        // 항목들 색상 변경
}
</style>
