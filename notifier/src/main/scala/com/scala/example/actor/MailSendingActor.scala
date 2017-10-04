package com.scala.example.notifier.actor

import java.util.{Date, Properties}
import javax.mail.internet.{InternetAddress, MimeMessage}
import javax.mail.{Address, Message, Session}

import akka.actor.{Actor, ActorLogging, Props}
import com.knoldus.example.services.ConfServiceImpl
import org.slf4j.{Logger, LoggerFactory}

class MailSendingActor extends Actor with ActorLogging {
  val logger: Logger = LoggerFactory.getLogger(this.getClass)

  override def receive: PartialFunction[Any, Unit] = {
    case (mailId: String, movieName: String, seats: List[String]) =>
      sendMail(mailId, movieName, seats)
  }


  def sendMail(reciever: String, movieName: String, seats: List[String]) {
    var message: Message = null
    val properties = new Properties()
    properties.put("mail.smtp.host", "smtp.gmail.com")
    properties.put("mail.smtp.port", "587")
    properties.put("mail.smtp.auth", "true")
    properties.put("mail.smtp.starttls.enable", "true")
    val sender=ConfServiceImpl.readString("senderId")
    val password=ConfServiceImpl.readString("mailPassword")
    val session = Session.getDefaultInstance(properties, null)
    val transport = session.getTransport("smtp")

    message = new MimeMessage(session)
    message.setFrom(new InternetAddress(sender))
    message.setSentDate(new Date())
    message.setSubject(s"$movieName Tickets")
    message.setText(s"Request for $movieName tickets and ${seats.toString().replace("List(", "(")} has been Booked\n Thanks!!")
    val addressArray = InternetAddress.parse(reciever).asInstanceOf[Array[Address]]
    if ((addressArray != null) && (addressArray.length > 0)) {
      message.setRecipients(Message.RecipientType.TO, addressArray)
    }
    transport.connect("smtp.gmail.com", sender, password)
    transport.sendMessage(message, message.getAllRecipients)
    logger.info("Mail send !!")
  }
}

object MailSendingActor {
  val props = Props[MailSendingActor]
}