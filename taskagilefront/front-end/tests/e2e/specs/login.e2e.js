// For authoring Nightwatch tests, see
// https://nightwatchjs.org/guide

module.exports = {
  'login test': browser => {
    browser
      .url(process.env.VUE_DEV_SERVER_URL + 'login')
      .assert.containsText('h1', 'TaskAgile')      // h1 요소가 TaskAgile을 포함하는가
      .waitForElementVisible('#app', 5000)         // #app 요소가 5초안에 보이는가
      // .assert.elementCount('img', 1)
      // .assert.elementPresent('.hello')
      .end()
  },
}

//   beforeEach: (browser) => browser.init(),
//   'e2e tests using page objects': (browser) => {
//     const homepage = browser.page.homepage()
//     homepage.waitForElementVisible('@appContainer')
//     const app = homepage.section.app
//     app.assert.elementCount('@logo', 1)
//     app.expect.section('@welcome').to.be.visible
//     app.expect.section('@headline').text.to.match(/^Welcome to Your Vue\.js (.*)App$/)
//     browser.end()
//   },
//   'verify if string "e2e-nightwatch" is within the cli plugin links': (browser) => {
//     const homepage = browser.page.homepage()
//     const welcomeSection = homepage.section.app.section.welcome
//     welcomeSection.expect.element('@cliPluginLinks').text.to.contain('e2e-nightwatch')
//   }
// }
