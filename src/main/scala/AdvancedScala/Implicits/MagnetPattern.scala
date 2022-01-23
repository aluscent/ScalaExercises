package AdvancedScala.Implicits

import scala.concurrent.Future

object MagnetPattern {
  trait P2P

  class P2PRequest extends P2P

  class P2PResponse extends P2P

  class Serializer[T]

  sealed trait Codes

  case class SuccessCode() extends Codes

  case class FailureCode() extends Codes


  // this is an old model
  trait Actor {
    def receive(statusCode: Codes): Int

    def receive(request: P2PRequest): Int

    def receive(request: P2PResponse): Int

    def receive[T: Serializer](message: T): Int

    def receive[T: Serializer](message: T, statusCode: Codes): Int

    def receive(future: Future[P2P]): Int
  }


  // this is the magnet part
  trait MessageMagnet[Result] {
    def apply(): Result
  }
  // advantages:
  //  1 - no type erasure problem
  //  2 - lifting works (if magnet trait does not have type argument, which isn't the case for MessageMagnet[Result])
  // disadvantages:
  //  1 - super-verbose
  //  2 - harder to read
  //  3 - can't name/place default arguments
  //  4 - call by name doesn't work correctly


  def receive[R](magnet: MessageMagnet[R]): R = magnet()

  implicit class FromP2PRequest(value: P2PRequest) extends MessageMagnet[Int] {
    override def apply(): Int = ???
  }

  implicit class FromP2PResponse(value: P2PResponse) extends MessageMagnet[Int] {
    override def apply(): Int = ???
  }

  implicit class FromFutureP2PRequest(value: Future[P2PRequest]) extends MessageMagnet[Int] {
    override def apply(): Int = ???
  }

  implicit class FromFutureP2PResponse(value: Future[P2PResponse]) extends MessageMagnet[Int] {
    override def apply(): Int = ???
  }

  def main(args: Array[String]): Unit = {
    receive(new P2PRequest)
    receive(new P2PResponse)
  }
}
