package com.scala.example.selector.actor

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import com.knoldus.example.models.Ticket
import com.scala.example.Impl.TicketHandlerSql
import com.scala.example.actor.InvokeNotifierActor


class SelectSeatActor extends Actor with ActorLogging {
  val system = ActorSystem("GoMovieSelectorActorSystem")
  val invokeNotifierSystem = system.actorOf(InvokeNotifierActor.props, "invoke-notifier")
  override def receive: PartialFunction[Any, Unit] = {
    case (mailId: String, movieName: String, count: Int) =>
      val ticketHandler=new TicketHandlerSql
      val ticketList=ticketHandler.selectTicket(movieName,count)
      sender() ! Ticket(movieName, ticketList)
      invokeNotifierSystem ! (Ticket(movieName,ticketList),mailId)
  }
}

object SelectSeatActor {
  val props = Props[SelectSeatActor]
}
