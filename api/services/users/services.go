package users

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io"
	"log"
	"net/http"
	"note-it/structs/api"
	"os"
	"strings"
)

func UpdateUser(userId string, user api.User) bool {
	endpoint := strings.Join([]string{os.Getenv("AUTH0_API"), "users", userId}, "/")
	method := "PATCH"

	jsonData, err := json.Marshal(user)
	if err != nil {
		log.Fatal(err)
		return false
	}
	payload := bytes.NewReader(jsonData)

	client := &http.Client{}
	req, err := http.NewRequest(method, endpoint, payload)

	if err != nil {
		log.Fatal(err)
		return false
	}

	token := os.Getenv("AUTH0_TOKEN")

	req.Header.Add("Content-Type", "application/json")
	req.Header.Add("Accept", "application/json")
	req.Header.Add("Authorization", fmt.Sprintf("Bearer %s", token))

	res, err := client.Do(req)
	if err != nil {
		log.Fatal(err)
		return false
	}
	defer func(Body io.ReadCloser) {
		err := Body.Close()
		if err != nil {
			log.Fatal(err)
		}
	}(res.Body)

	if !(res.StatusCode >= 200 && res.StatusCode < 300) {
		log.Println("Request failed with status code:", res.StatusCode)

		var body, _ = io.ReadAll(res.Body)
		log.Println(string(body))

		return false
	}

	return true
}
