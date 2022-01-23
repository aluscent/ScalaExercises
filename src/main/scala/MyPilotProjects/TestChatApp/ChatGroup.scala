package MyPilotProjects.TestChatApp

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

object ChatGroup {
  case class Subscribe(uName: String, user: ActorRef)
  case class Acknowledge(gName: String, group: ActorRef)

  def groupProps(name: String): Props = Props(new ChatGroup(name))
}

class ChatGroup(name: String) extends Actor with ActorLogging {
  import ChatGroup.{Subscribe, Acknowledge}

  override def receive: Receive = {
    case Subscribe(uName, user) =>
      log.info(s"[GROUP] User joined group $name.")
      context.become(withUser(Map(uName -> user)))

    case _ => log.info("[GROUP] Invalid Option.")
  }

  def withUser(users: Map[String, ActorRef]): Receive = {
    case Subscribe(uName, user) =>
      log.info(s"[GROUP] User joined group $name.")
      context.become(withUser(users + (uName -> user)))
      user ! Acknowledge(name, self)

    case _ => log.info("[GROUP] Invalid Option.")
  }
}