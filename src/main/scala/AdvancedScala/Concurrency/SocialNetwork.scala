package AdvancedScala.Concurrency

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Random, Success, Failure}

case class Profile(id: Int, name: String) {
  def poke(other: Profile) = {
    println(s"${this.name} poking ${other.name}")
  }

  override def toString: String = s"person $name"
}

class SocialNetwork {
  private val random = new Random()

  private val names = (1 to 10).map(x => random.between(10000,20000) -> s"person$x").toMap
//  val friends = (for (_ <- 1 to 8; x = random.between(1,10); y = random.between(1,10); if (x != y)) yield x -> y).toMap

  private val profiles = names.map(x => x._1 -> Profile(x._1, x._2))
  private var relations = List[(Profile, Profile)]()

  def fetchProfile(id: Int): Future[Profile] = Future {
    Thread.sleep(random.between(500, 1000))
    profiles(id)
  }

  def fetchProfileByIndex(index: Int): Future[Profile] = Future {
    Thread.sleep(random.between(500, 1000))
    profiles.toIndexedSeq(index)._2
  }

  def fetchBestFriends(person: Profile, friend: Profile): Future[Unit] = Future {
    Thread.sleep(random.between(500, 1000))
    relations = (person, friend) :: relations
    person.poke(friend)
  }
}

object test extends App {
  val socialNetwork = new SocialNetwork

  val random = new Random()
  val firstCandidate = socialNetwork.fetchProfileByIndex(random.between(0,5))
  val secondCandidate = socialNetwork.fetchProfileByIndex(random.between(5,10))

  /*
  val first = firstCandidate.onComplete {
    case Success(name) => name
    case Failure(e) => throw new Exception(e)
  }
  val second = secondCandidate.onComplete {
    case Success(name) => name
    case Failure(e) => throw new Exception(e)
  }
   */

  for {
    first <- socialNetwork.fetchProfileByIndex(random.between(0,5))
    second <- socialNetwork.fetchProfileByIndex(random.between(5,10))
  } first.poke(second)

  val failedProfile = socialNetwork.fetchProfileByIndex(20).recover {
    case _: Exception => Profile(0, "Dummy")
  }

  val anotherFailedProfile = socialNetwork.fetchProfileByIndex(20).fallbackTo(socialNetwork.fetchProfileByIndex(1))

  Thread.sleep(2000)
}

