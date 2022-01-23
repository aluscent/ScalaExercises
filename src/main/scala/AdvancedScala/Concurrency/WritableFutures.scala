package AdvancedScala.Concurrency

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Random, Success, Try}

object WritableFutures {
  def main(args: Array[String]): Unit = {
    val aFuture = Future

    def getPreciousValue(arg: Int): Future[String] = ???

    object MyService {
      def preciousValue(arg: Int) = "the precious value is " + arg

      def submitTask[A](arg: A)(function: A => Unit): Boolean = true
    }

    val promise = Promise()
    val future = promise.future


  }
}
