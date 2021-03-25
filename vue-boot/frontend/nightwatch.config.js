module.exports = {
  test_settings: {
    default: {      // test:integration 스크립트에 전달된 시작 URL이 process.env.LAUNCH_URL에 설정된다. (localhost:8080)
                    // npm run test:e2e와 npm run test:integration을 실행할 때, 나이트왓치는 올바른 URL을 고른다. (스크립트에 전달된 URL을 고를 것이다)
      launch_url: (process.env.LAUNCH_URL || process.env.VUE_DEV_SERVER_URL)
    },
    chrome: {
      desiredCapabilities: {
        browserName: "chrome",
        chromeOptions: {
          args:  ["headless", "no-sandbox", "disable-gpu"]
        }
      }
    }
  },
  page_objects_path: 'tests/e2e/page-objects'
}
