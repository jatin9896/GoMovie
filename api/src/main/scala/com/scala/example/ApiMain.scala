package com.scala.example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.server.{Directives, HttpApp, Route}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.knoldus.example.models.Ticket
import spray.json.DefaultJsonProtocol

import scala.concurrent.Future
import scala.concurrent.duration._
import com.knoldus.example.services.ConfServiceImpl
import org.slf4j.{Logger, LoggerFactory}
object ApiMain extends HttpApp with App with Directives with SprayJsonSupport with DefaultJsonProtocol {
  implicit val system = ActorSystem("GoMovieApiActorSystem")
  implicit val materializer = ActorMaterializer()

  implicit val executionContext = system.dispatcher
  val logger: Logger = LoggerFactory.getLogger(this.getClass)
  val ipInfo: Array[String] =ConfServiceImpl.readString("apiServiceIp").split(":")
  def routes: Route =
    pathEndOrSingleSlash {
      complete("Booking service server fully up and running")
    } ~
      path("bookSeat") {
        implicit val ticketFormat = jsonFormat2(Ticket)
        get {
          parameter('mailId.?, 'movieName.?, 'count.?) { (mailId, movieName, ticketCount) =>
            if (!List(mailId, movieName, ticketCount).forall(_.isDefined))
              complete(Future.successful("Wrong Parameter"))
            else
              complete(processSeatIssue(mailId, movieName, ticketCount.map(count => count.toInt)))
          }
        }
      }

  private def processSeatIssue(mailId: Option[String], movieName: Option[String], ticketCount: Option[Int]): Future[Ticket] = {
    implicit val timeout = Timeout(1000 seconds)

    implicit val itemFormat = jsonFormat2(Ticket)

    (mailId, movieName, ticketCount) match {
      case (Some(emailId), Some(movieName), Some(ticketCount)) =>
        val url = s"http://${ConfServiceImpl.readString("selectorServiceIp")}/selectSeat"
       logger.info("hitting url "+url)
        val uriString: String = (url + "?mailId=" + emailId + "&movieName=" + movieName + "&count=" + ticketCount).replace(" ", "%20")
        val response: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = uriString))
        response.flatMap(response => Unmarshal(response.entity).to[Ticket]).recover{
          case exception:Exception=>throw new Exception("selector service not found ")
        }
      case (_,_,_)=>Future.successful(Ticket("",List("")))
    }

  }
  startServer(ipInfo.apply(0), ipInfo.apply(1).toInt)
}
