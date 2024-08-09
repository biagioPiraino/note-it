# note-it
A note-taking web app built with Go, Vue.js, and MongoDB. Includes a companion native Android app.

## Architecture

The web and Android app clients are supported by a Go API, developed using the [Gin Framework](https://github.com/gin-gonic/gin).

Auth0 is used as the Identity Provider for managing authorization and authentication.

Data is stored in a MongoDB database, with deployment managed through Terraform.

## Functionalities

Both the web and Android apps share the same functionalities. Users can:

* View, create, modify, and delete categories
* View, create, modify, and delete notes
* View, create, modify, and delete tasks
* Update account information
