package MyPilotProjects

import akka.Done
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, Uri}
import akka.stream.scaladsl.{Sink, Source}
import spray.json.DefaultJsonProtocol._
import spray.json.{RootJsonFormat, enrichString}

import java.util.UUID
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.{implicitConversions, postfixOps}
import scala.util.{Failure, Success}


case class Symbol(symbol: String, name: String, baseCurrency: String, quoteCurrency: String, feeCurrency: String, market: String,
                  baseMinSize: String, quoteMinSize: String, baseMaxSize: String, quoteMaxSize: String, baseIncrement: String,
                  quoteIncrement: String, priceIncrement: String, priceLimitRate: String, isMarginEnabled: Boolean, enableTrading: Boolean)
case class SymbolList(code: String, data: Seq[Symbol])


trait KucoinJsonLibrary {
  implicit val symbolFormat: RootJsonFormat[Symbol] = jsonFormat16(Symbol)
  implicit val symbolListFormat: RootJsonFormat[SymbolList] = jsonFormat2(SymbolList)
}


object Kucoin extends KucoinJsonLibrary {
  implicit val system: ActorSystem = ActorSystem()
  import system.dispatcher

  object TickerUpdater {
    case object GetTicks
    case object PrintTicks
  }

  class TickerUpdater extends Actor with ActorLogging {
    import TickerUpdater.{GetTicks, PrintTicks}

    override def receive: Receive = {
      case newTicks: Seq[Symbol] => context.become(Stateful(newTicks))
      case _ =>
    }

    def Stateful(ticks: Seq[Symbol]): Receive = {
      case newTicks: Seq[Symbol] => context.become(Stateful(newTicks))
      case GetTicks => sender() ! ticks
      case PrintTicks => println(s"New ticks: " + ticks.mkString(", "))
      case _ =>
    }
  }

  val tickerUpdaterActor: ActorRef = system.actorOf(Props[TickerUpdater], "tickerUpdater")

  def getSymbolsList: Future[Done] =
    callAPI(List(Tuple2(HttpRequest(HttpMethods.GET, Uri("/api/v1/symbols")), UUID.randomUUID().toString)))

  def callAPI(requests: List[(HttpRequest, String)]): Future[Done] =
    Source(requests).via(Http().cachedHostConnectionPoolHttps[String]("api.kucoin.com"))
      .runWith(Sink.foreach {
        case (Success(response), value) =>
          println(s"[ACTOR] Response to request $value returned: $response")
          tickerUpdaterActor ! response.entity.toStrict(15 seconds).map(x =>
            x.data.utf8String.parseJson.convertTo[SymbolList].data)
        case (Failure(exception), value) =>
          println(s"[ACTOR] Response to request $value failed with: $exception")
      })




  def main(args: Array[String]): Unit = {


    getSymbolsList


  }
}
