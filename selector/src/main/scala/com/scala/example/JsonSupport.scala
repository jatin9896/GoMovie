package com.scala.example
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.knoldus.example.models.Ticket
import spray.json.DefaultJsonProtocol

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val ticketFormat = jsonFormat2(Ticket)
}
