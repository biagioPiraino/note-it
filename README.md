# note-it
A note-taking web app built with Go, Vue.js, and MongoDB. Includes a companion native Android app.

## Architecture

The web and Android app clients are served by a Go API developed using the [Gin Framework](https://github.com/gin-gonic/gin).

Authorization and authentication are managed using Auth0 as Identity provider.

The data will be stored in a MongoDB database.

## Functionalities

The apps allow users to:
* View, create, modify and delete categories
* View, create, modify and delete notes
* View, create, modify and delete tasks
* Update the user account information
