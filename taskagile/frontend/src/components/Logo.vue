// logo-wrapper
  // logo
  // tagline

<template>
  <div class="logo-wrapper">
    <img class="logo" src="/static/images/logo.png">
    <div class="tagline">Open source task management tool</div>
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