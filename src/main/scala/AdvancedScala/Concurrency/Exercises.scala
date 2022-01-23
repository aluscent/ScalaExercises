package AdvancedScala.Concurrency

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Promise
import scala.util.{Failure, Random, Success}

class Ex1 {
  val promise = Promise[Int]()
  val future = promise.future

  future.onComplete {
    case Success(value) => println("[consumer] consumed " + value)
  }

  val random = new Random()
  val producer = new Thread(() => {
    promise.success(random.between(100, 200))
    println("[producer] done.")
  })

  producer.start()
}

class Ex2 {
  val promise1 = Promise[Int]()
  val future1 = promise1.future
  val promise2 = Promise[Int]()
  val future2 = promise2.future

  val random = new Random()
  val producer1 = new Thread(() => {
    promise1.success(random.between(100, 200))
    println("[producer 1] done.")
  })

  future2.onComplete {
    case Success(value) => println(s"[consumer 2] consumed $value.")
  }

  future1.onComplete {
    case Success(value) =>
      println(s"[consumer 1] consumed $value.")
      new Thread(() => {
        promise2.success(value)
        println("[producer 2] new value produced.")
      }).start()
  }

  producer1.start()
}

class Ex4 {
  val promise = Promise[Int]()
  val future1 = promise.future
  val future2 = promise.future

  val producer = new Thread(() => {
    println("[producer] waiting...")
    Thread.sleep(50)
    promise.success(867)
    println("[producer] produced.")
  })

  var x = ""

  future2.onComplete {
    case Success(value) => x = "[consumer 2] " + value
    case Failure(exception) => println(exception)
  }
  future1.onComplete {
    case Success(value) => x = "[consumer 1] " + value
    case Failure(exception) => println(exception)
  }

  producer.start()
  Thread.sleep(200)
  println(x)
}

object Exercises {
  def main(args: Array[String]): Unit = {
//    val exercise1 = new Ex1
//    val exercise2 = new Ex2
    val exercise4 = new Ex4

    Thread.sleep(500)
  }
}
