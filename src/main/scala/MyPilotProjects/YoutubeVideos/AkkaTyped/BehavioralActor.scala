package MyPilotProjects.YoutubeVideos.AkkaTyped

import akka.actor.typed.ActorRef
import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors

object BehavioralActor {

  // typed messages
  trait ShoppingCart
  case class AddItem(item: String) extends ShoppingCart

  // define actor behavior
  val typedActor = ActorSystem(
    Behaviors.receiveMessage[ShoppingCart] { message =>
      message match
      {
        case AddItem(item) =>
          println(s"New item added to cart: $item")
      }

      Behaviors.same
    },
    "simpleShopping"
  )

  // mutable state
  def mutableActor(items: Seq[String]): Behavior[ShoppingCart] =
    Behaviors.receiveMessage[ShoppingCart] {
      case AddItem(item) =>
        println(s"New item added to cart: $item")
        mutableActor(items :+ item)
    }

  // hierarchy
  val rootShoppingActor = ActorSystem(
    Behaviors.setup[ShoppingCart] { ctx =>
      ctx.spawn(mutableActor(Seq()), "modernShoppingCart")
      Behaviors.empty
    },
    "modern"
  )

  def main(args: Array[String]): Unit = {
    val actor: ActorRef[ShoppingCart] = ActorSystem(mutableActor(Seq()), "mutable")
    actor ! AddItem("bread")

    rootShoppingActor ! AddItem("milk")
  }
}
