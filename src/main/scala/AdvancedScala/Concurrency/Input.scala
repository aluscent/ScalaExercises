package AdvancedScala.Concurrency

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Random, Success, Try}

object Input extends App {
  val promise = Promise[String]()
  val future = promise.future

  new Thread(() => {
    while (true) {
      Thread.sleep(1000)
      println("1 second passed.")
    }
  }).start()

  promise.success(scala.Console.in.readLine())

  future.onComplete {
    case Success(value) => println(value)
  }

  Thread.sleep(5000)
}
