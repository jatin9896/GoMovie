package com.scala.example

import akka.actor.ActorSystem
import akka.http.scaladsl.server.{Directives, HttpApp, Route}
import akka.pattern.ask
import akka.util.Timeout
import com.knoldus.example.models.Ticket
import com.knoldus.example.services.ConfServiceImpl
import com.scala.example.selector.actor.SelectSeatActor

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._


object SelectorMain extends HttpApp with App with Directives with JsonSupport {
  val system = ActorSystem("GoMovieSelectorActorSystem")

  val selectSeatActor = system.actorOf(SelectSeatActor.props, "issue_ticket_actor")
  val ipInfo: Array[String] = ConfServiceImpl.readString("selectorServiceIp").split(":")

  def routes: Route =
    pathEndOrSingleSlash {
      complete("Selector service server fully up and running")
    } ~
      path("selectSeat") {
        get {
          parameter('mailId.?, 'movieName.?, 'count.?) { (mailId, movieName, count) =>
            val output: Ticket = processSeat(mailId, movieName, count.map(ticketCount => ticketCount.toInt))
            complete(output)
          }
        }
      }

  private def processSeat(mailId: Option[String], movieName: Option[String], ticketCount: Option[Int]): Ticket = {
    import scala.concurrent.ExecutionContext.Implicits.global
    implicit val timeout = Timeout(1000 seconds)

    (mailId, movieName, ticketCount) match {
      case (Some(mailId), Some(movieName), Some(count)) =>
        val output: Future[Any] = (selectSeatActor ? (mailId, movieName, count))
        val tickets = output.map(ticket => ticket match {
          case ticket: Ticket => ticket
          case _ => Ticket("", List(""))
        })
        val result = Await.result(tickets, 15 seconds)
        result
      case (_,_,_)=>Ticket("", List(""))
    }
  }

  startServer(ipInfo.apply(0), ipInfo.apply(1).toInt)
}
