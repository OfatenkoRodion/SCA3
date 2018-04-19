package rest.entities

case class HttpEntitiesRequests()

case class RequestCreateUser(login: String, password1: String, password2: String, email: String)

case class ServerResponse(serverResponse: String)

case class RequestCreateOrder(login: String, link: String, metricsList: Seq[Long])