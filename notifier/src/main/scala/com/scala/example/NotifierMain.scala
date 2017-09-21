import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.{Directives, HttpApp, Route}
import akka.stream.ActorMaterializer
import com.knoldus.example.models.{MailInfo, Ticket}
import com.knoldus.example.services.ConfServiceImpl
import com.scala.example.notifier.actor.MailSendingActor
import spray.json.DefaultJsonProtocol


object NotifierMain extends HttpApp with App with Directives with SprayJsonSupport with DefaultJsonProtocol {

  implicit val system = ActorSystem("GoMovieNotifier")
  val sendMails = system.actorOf(MailSendingActor.props, "send-mail")
  implicit val materializer = ActorMaterializer()

  implicit val executionContext = system.dispatcher
  val ipInfo: Array[String] = ConfServiceImpl.readString("notifierServiceIp").split(":")

  def routes: Route =
    pathEndOrSingleSlash {
      complete("notification service server fully up and running")
    } ~
      path("notify") {
        post {
          implicit val seatFormat = jsonFormat2(Ticket)
          implicit val mailFormat = jsonFormat2(MailInfo)
          entity(as[MailInfo]) { response =>
            complete(processNotification(response.mailId, response.ticket))
          }
        }
      }


  def processNotification(mailId: String, ticket: Ticket): String = {
    sendMails ! (mailId, ticket.movieName, ticket.seatList)
    "mail sending is in process"
  }

  startServer(ipInfo.apply(0), ipInfo.apply(1).toInt)
}


