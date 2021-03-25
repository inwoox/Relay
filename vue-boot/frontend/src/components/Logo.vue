// logo-wrapper
  // logo
  // tagline

<template>
  <div class="logo-wrapper">
    <img class="logo" src="/images/logo.png"> <!-- src/main/resources/static 경로부터 시작 -->
    <div class="tagline">{{ $t("logo.tagLine") }}</div>
  </div>
</template>

<script>
export default {
  name: 'Logo'
}
</script>
<style lang="scss" scoped>
  .logo-wrapper {
    text-align: center;
    margin-bottom: 40px;
    .logo {
      max-width: 200px;
      //margin: 0 auto;
      margin-top: 60%;
    }
    .tagline {
      line-height: 180%;
      color: #666;
    }
  }
</style>
