package com.scala.example.Impl

import java.sql.DriverManager
import java.sql.Connection

import com.knoldus.example.services.ConfServiceImpl
import org.slf4j.LoggerFactory

import scala.collection.mutable.ListBuffer


class TicketHandlerSql {
  def selectTicket(movieName: String, ticketCount: Int): List[String] ={
    // connect to the database named "mysql" on the localhost
    val logger = LoggerFactory.getLogger(this.getClass)
    val driver = "com.mysql.jdbc.Driver"
    val url = ConfServiceImpl.readString("dbUrl")
    val username = ConfServiceImpl.readString("dbUserName")
    val password = ConfServiceImpl.readString("dbpassword")
    var seatLists = new ListBuffer[String]()

    // there's probably a better way to do this
    var connection:Connection = null

    try {
      // make the connection
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      // create the statement, and run the select query
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(s"select seatid from sql12196427.cinema where movieName = '${movieName}' and Status ='nb' limit ${ticketCount} ")
      while ( resultSet.next() ) {
        val seatList = resultSet.getString("seatid")
        seatLists+=seatList
       logger.info(s"seatid : $seatList retrieve")
      }
    } catch {
      case e => e.printStackTrace
    }
    connection.close()
    seatLists.toList
  }
}
