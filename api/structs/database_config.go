package structs

type DatabaseConfig struct {
	Database struct {
		Connection string `yaml:"connection"`
		Name       string
	} `yaml:"database"`
}
