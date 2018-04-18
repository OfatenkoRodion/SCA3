Sniffle Video REST service
=========================

## Description:
Goal of service  is create Zoom meeting with help of Zoom API
Zoom API - [https://zoom.github.io/api](https://zoom.github.io/api)

## Requirements
* JDK 8 (e.g. [http://www.oracle.com/technetwork/java/javase/downloads/index.html](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* Scala 2.12.4 (e.g. [https://www.scala-lang.org/download/](https://www.scala-lang.org/download/)
* sbt ([http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html](http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html)

## Zoom Api
You can find by link below details about the app you're building and credentials for the API
* [https://developer.zoom.us/me/](https://developer.zoom.us/me/)

## Structure

- `src/main/resources`: configuration files

- `src/main/scala/com/sniffle/video/common`: help classes

- `src/main/scala/com/sniffle/video/config`: config classes

- `src/main/scala/com/sniffle/video/entity`: case classes and objects used for many reasons such as for request validation, response formats and marshalling (converting classes and objects to/from serialized formats, such as json).

- `src/main/scala/com/sniffle/video/rest`: classes that define routes (i.e. what paths and methods trigger which operations) and call whatever resources (actor operations, web services, etc) they need to complete their tasks.

- `src/main/scala/com/sniffle/video/util`: utils directory contains code that is generic enough so as to be used in other projects.

- `src/main/scala/com/sniffle/video/service`: services classes which contain business logic.

- `src/main/scala/com/sniffle/video/Main.scala`: this file can be thought of as a "main" method. Here the actor system is started, others actors are started too and all routes are merged.

- `src/test`: test classes


## Docker
- To package application as docker image, use `docker build -t sniffle-video-service:v1 .`
- To run application
```
docker run -it -e APP_PROFILE='production' \  
 -e APP_HOST='localhost' \
 -e APP_PORT='8082' \
 -e APP_HTTPS_PASSWORD='pBhirmAbTvmyBreVRI3NPg' \
 -e ZOOM_API_KEY='pBhirmAbTvmyBreVRI3NPg' \
 -e ZOOM_API_SECRET='s7b5aEFmViQ1SRJF6bi7j8ZP1BBfqamWoUoJ' \
 -e ZOOM_API_USER_EMAIL='shkurenko.work@gmail.com' \
 --name sniffle-video-service:v1
 ```


### Running locally
If you want to launch application in `"development"` profile u don't need fulfill env variables
application.conf file already contains values for all env variables. Please add own configurations values
See `Deployment on production` how to run in production mode
After that, just run `sbt run` and enjoy hacking. For better expirience you can use `sbt reStart` that will give you ability to
restart application without restarting of sbt.

### Deployment on production
If you want to launch application in `"production"` withour docker profile u need fulfill some env variables or add changes into application.conf:

 - `APP_PROFILE` - get conf values by profiles, example: "production"
 - `APP_HOST` - app host, example: "localhost"
 - `APP_PORT` - app port, example: "8082"
 - `APP_HTTPS_PASSWORD` - app https password, example: "sdfqwesfasf"
 - `ZOOM_API_KEY` - zoom api key, example: "pBhirmAbTvmyB3NPg"
 - `ZOOM_API_SECRET` - zoom api secret, example: "s7b5aEFmViQ1SRJF6BBfqamWoUoJ"
 - `ZOOM_API_USER_EMAIL` - zoom user email, example: "f@gmail.com"
 - `AUTH_CLIENTID` - Client id for Auth service, example "booking"
 - `AUTH_SECRET` - Client secret for Auth service, example "sniffle"
 - `AUTH_DOMAIN` - Host for Auth service, example: "http://localhost:8081"
 - `AUTH_API` - Version Auth service, example: "v1.0"
 - `SWAGGER_SERVER_HOST` - Swagger host, example: "localhost"
 - `SWAGGER_SERVER_HOST` - Swagger port, example: "8083"
