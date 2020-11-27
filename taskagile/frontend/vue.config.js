module.exports = {
	devServer: {
		port: 3000,
		proxy: {
			'/api/*': { // localhost:3000/api/login으로 요청하면, HTTP 프록시가 localhost:8080/api/login으로 요청을 전달한다.
				target: 'http://localhost:8080'
			}
		}
	}
}
