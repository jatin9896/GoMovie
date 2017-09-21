package com.scala.example.actor

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer
import com.knoldus.example.models.{MailInfo, Ticket}
import com.knoldus.example.services.ConfServiceImpl
import com.scala.example.JsonSupport
import org.slf4j.{Logger, LoggerFactory}
import spray.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class InvokeNotifierActor extends Actor with ActorLogging with Directives with JsonSupport with SprayJsonSupport {
  implicit val system = ActorSystem("GoMovieSelectorActorSystem")
  implicit val materializer = ActorMaterializer()
  val logger: Logger = LoggerFactory.getLogger(this.getClass)

  override def receive: PartialFunction[Any, Unit] = {
    case (ticket: Ticket, mailId: String) =>
      val uriString = s"http://${ConfServiceImpl.readString("notifierServiceIp")}/notify"
      implicit val seatFormat = jsonFormat2(MailInfo)
      val ticketJson: String = MailInfo(mailId, ticket).toJson.toString()
      val http = Http(system)
      val request = HttpRequest(
        method = HttpMethods.POST,
        uri = uriString,
        entity = HttpEntity(ContentTypes.`application/json`, ticketJson)
      )
      val response: Future[HttpResponse] = http.singleRequest(request)
      response.map(res => logger.info(res.toString())).recover {
        case exception: Exception => println(exception.getMessage)
      }
  }
}

object InvokeNotifierActor {
  val props = Props[InvokeNotifierActor]
}