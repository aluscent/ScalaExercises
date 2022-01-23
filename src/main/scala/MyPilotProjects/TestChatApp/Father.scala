package MyPilotProjects.TestChatApp

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

object Father {
  case class CreateUser(id: String, name: String)
}

class Father extends Actor with ActorLogging {
  import ChatUser.{CreateGroup, JoinGroup, userProps}
  import ChatGroup.{Subscribe, groupProps}
  import Father.{CreateUser}

  override def receive: Receive = {
    case CreateUser(name, id) =>
      log.info(s"[FATHER] New user $name created.")
      context.become(withUser(Map(id -> context.actorOf(userProps(name), name)), Map()))

    case _ =>  log.info("[FATHER] Invalid Option.")
  }


  def withUser(users: Map[String, ActorRef], groups: Map[String, ActorRef]): Receive = {
    case CreateUser(name, id) =>
      log.info(s"[FATHER] New user $name created.")
      context.become(withUser(users + (id -> context.actorOf(userProps(name), name)), groups))

    case CreateGroup(name, id) =>
      log.info(s"[FATHER] New group $name created.")
      context.become(withUser(users, groups + (id -> context.actorOf(groupProps(name), name))))

    case JoinGroup(groupId) =>
      if (groups.contains(groupId)) {
        log.info(s"[FATHER] User joined group $groupId.")
        groups(groupId) ! Subscribe(groupId, sender())
      }
      else log.info("[FATHER] Group does not exist.")

    case _ =>  log.info("[FATHER] Invalid Option.")
  }
}