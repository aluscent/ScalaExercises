package MyPilotProjects.TestChatApp

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

object ChatUser {
  case class CreateGroup(name: String, id: String)
  case class JoinGroup(groupId: String)

  def userProps(name: String): Props = Props(new ChatUser(name))
}

class ChatUser(val name: String) extends Actor with ActorLogging {
  import ChatGroup.Acknowledge

  override def receive: Receive = {
    case Acknowledge(gName, group) =>
      log.info(s"[USER] User $name joined group.")
      context.become(withGroup(Map(gName -> group)))

    case _ => log.info("[USER] Invalid Option.")
  }

  def withGroup(groups: Map[String, ActorRef]): Receive = {
    case Acknowledge(gName, group) =>
      log.info(s"[USER] User $name joined group.")
      context.become(withGroup(groups + (gName -> group)))

    case _ => log.info("[USER] Invalid Option.")
  }
}