package MyPilotProjects

import akka.NotUsed
import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import akka.stream.{Attributes, FlowShape, Inlet, Outlet}
import akka.stream.stage.{GraphStageLogic, GraphStageWithMaterializedValue, OutHandler}
import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.{Future, Promise}
import scala.language.postfixOps

object MovingAverage2_0 {
  implicit val system = ActorSystem("movingAverage2")

  case class UpdateState(newTick: Any)

  class StateActor extends Actor with ActorLogging {
    override def receive: Receive = Stateful(List[Double]())

    def Stateful(ticks: List[Double]): Receive = {
      case UpdateState(newTick: Double) =>
        log.debug("Updating state.")
        sender() ! UpdateState(ticks)
        if (ticks.length >= 10) context.become(Stateful(ticks.tail :+ newTick))
        else context.become(Stateful(ticks :+ newTick))
      case _ => log.info("Invalid option!")
    }
  }

  def main(args: Array[String]): Unit = {

    val source1 = Source((10 to 30).map(_ / 10.0))
    implicit val timeout: Timeout = Timeout(20 seconds)
    val stateActor = system.actorOf(Props[StateActor],"state")
    val actorBasedFlow = Flow[UpdateState].ask[UpdateState](1)(stateActor)

    val pipeline: Source[UpdateState, NotUsed] = Source((10 to 30).map(_ / 10.0))
      .via(Flow[Double].map(tick => {
        println(s"New tick: $tick")
        UpdateState(tick)
      }))
    val secondPart: Source[UpdateState, NotUsed] = pipeline
      .via(actorBasedFlow)
      .via(Flow[UpdateState].filter(state => state.newTick match {
        case list: List[Double] => list.length == 10
      }))
    val thirdPart = secondPart
      .to(Sink.foreach[UpdateState](tickList => tickList.newTick match {
        case list: List[Double] => println(list.sum / list.length)
      })).run()
  }
}
