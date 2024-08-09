package services

func LaunchApi() {
	LoadConfiguration()
	dbSingleton := InitialiseDatabase()
	LaunchApiRouter(dbSingleton)
}
