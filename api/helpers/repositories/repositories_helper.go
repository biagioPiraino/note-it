package repositories

import "time"

func SimpleOperationsTimeout() time.Duration {
	return 3 * time.Second
}

func ComplexOperationsTimeout() time.Duration {
	return 5 * time.Second
}
