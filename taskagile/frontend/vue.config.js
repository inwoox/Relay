module.exports = {
	devServer: {
		port: 3000,
		proxy: {
			'/api/*': { // localhost:3000/api/login으로 요청하면, HTTP 프록시가 localhost:8080/api/login으로 요청을 전달한다.
				target: 'http://localhost:8080'
			}
		}
  },
  // 부트스트랩을 사용하려면, 부트스트랩의 컴파일된 CSS를 애플리케이션으로 가져와야한다. 여기서는 웹팩을 이용해 부트스트랩의 CSS를 가져온다.
  // 웹팩에 style이라는 새로운 진입점을 만들어, 모든 서드 파티 스타일을 하나의 .css 파일로 묶는다.
  configureWebpack: {
    entry: {
      app: './src/main.js',
      style: [
        'bootstrap/dist/css/bootstrap.min.css' // 필요할 때마다 서드 파티 라이브러리의 스타일을 이곳에 추가한다.
      ]
    }
  }
}
