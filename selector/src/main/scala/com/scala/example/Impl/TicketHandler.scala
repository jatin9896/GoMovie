package com.scala.example.selector.Impl

import com.datastax.driver.core.Cluster
import com.knoldus.example.services.ConfServiceImpl
import org.slf4j.LoggerFactory

class TicketHandler {
  val cluster = Cluster.builder().addContactPoint(ConfServiceImpl.readString("cassandrahost")).build()
  val logger = LoggerFactory.getLogger(this.getClass)
  val session = cluster.connect("cinemadb")

  def selectTicket(movieName: String, ticketCount: Int): List[String] = {
    val res = session.execute(s"select seatid from cinema where movieName = '${movieName}' and Status ='nb' limit ${ticketCount} ALLOW FILTERING;")
    import scala.collection.JavaConverters._
    val seatList: List[String] =res.all().asScala.map(seat=>seat.getString("seatid")).toList
    seatList
  }
}
