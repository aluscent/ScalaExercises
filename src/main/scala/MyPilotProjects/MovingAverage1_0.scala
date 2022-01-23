package MyPilotProjects

import akka.NotUsed
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.stream.{FlowShape, Graph}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Sink, Source}

import scala.util.Random

object MovingAverage1_0 {
  implicit val system: ActorSystem = ActorSystem("MA")

  case object GetState
  case class SetState(newTicker: Int)
  case class Tick(ticker: Int)

  class StateActor(interval: Int) extends Actor with ActorLogging {
    override def receive: Receive = {
      case GetState => sender() ! List[Int]()
      case SetState(newTicker) => context.become(Stateful(List[Int](newTicker)))
    }

    def Stateful(tickers: List[Int]): Receive = {
      case GetState => sender() ! tickers
      case SetState(newTicker) =>
        if (tickers.length >= interval) context.become(Stateful(tickers.tail :+ newTicker))
        else context.become(Stateful(tickers :+ newTicker))
    }
  }

  class EventActor(stateActor: ActorRef, graph: Graph[FlowShape[List[Int], Float], NotUsed])
    extends Actor with ActorLogging {
    override def receive: Receive = {
      case Tick(ticker) =>
        stateActor ! SetState(ticker)
        stateActor ! GetState
      case tickers: List[Int] =>
        Source(List(tickers)).via(graph).runWith(Sink.foreach[Float](x => println(s"New MA: $x")))
    }
  }

  def average(window: List[Int]): Float = {
    val len = window.length
    window.sum.toFloat / len.toFloat
  }

  def newSource(name: String, interval: Int): ActorRef = {
    val graph = GraphDSL.create() { implicit builder =>
      val aggregatorShape = builder.add(Flow[List[Int]].map(x => average(x)))
      val filterShape = builder.add(Flow[List[Int]].filter(x => x.length >= interval))
      val broadcast = builder.add(Broadcast[List[Int]](2))
      val printerShape = builder.add(Sink.foreach[List[Int]](x => println(s"Tuple for $name MA$interval: $x")))

      import GraphDSL.Implicits._
      filterShape ~> broadcast ~> aggregatorShape
      broadcast ~> printerShape

      FlowShape(filterShape.in, aggregatorShape.out)
    }

    val stateActor = system.actorOf(Props(new StateActor(interval)), s"state$name")
    val eventActor = system.actorOf(Props(new EventActor(stateActor, graph)), s"event$name")
    eventActor
  }

  def main(args: Array[String]): Unit = {
    val random = new Random()

    val processor1 = newSource("BTCUSDT", 10)
    val source1 = new Thread(() => {
      for (_ <- 1 to 100) {
        Thread.sleep(100)
        val ticker = random.between(55000, 65000)
        processor1 ! Tick(ticker)
      }
    })
    source1.start()

    val processor2 = newSource("ETHUSDT", 14)
    val source2 = new Thread(() => {
      for (_ <- 1 to 100) {
        Thread.sleep(100)
        val ticker = random.between(4300, 4700)
        processor2 ! Tick(ticker)
      }
    })
    source2.start()
  }
}
