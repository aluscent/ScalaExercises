package AdvancedScala.Concurrency

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object Futures {
  def firstCalculate: Int = {
    Thread.sleep(1000)
    432
  }
  def secondCalculate: Int = {
    Thread.sleep(1200)
    376
  }
  def thirdCalculate: Int = {
    Thread.sleep(800)
    654
  }

  def main(args: Array[String]): Unit = {
    val aFuture = Future {
      firstCalculate
      secondCalculate
      thirdCalculate
    }

    println(aFuture.value)

    aFuture.onComplete {
      case Success(value) => println(s"values is $value")
      case Failure(exception) => println(s"exception $exception")
    }

    Thread.sleep(1500)
  }
}
