package AdvancedScala.Concurrency

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Random, Success, Try}

object Answers {
  def fullfillImmediate[A](value: A): Future[A] = Future(value)

  def inSequence[A, B](x: Future[A], y: Future[B]): Future[B] =
    x.flatMap(_ => y)

  def first[A](x: Future[A], y: Future[A]): Future[A] = {
    val promise = Promise[A]

    x.onComplete (promise.tryComplete)
    y.onComplete (promise.tryComplete)

    promise.future
  }

  def last[A](x: Future[A], y: Future[A]): Future[A] = {
    val commonPromise = Promise[A]
    val finalPromise = Promise[A]
    def checkComplete(result: Try[A]) = if(!commonPromise.tryComplete(result)) finalPromise.complete(result)

    x.onComplete(checkComplete)
    y.onComplete(checkComplete)

    finalPromise.future
  }

  def retryUntil[A](action: () => Future[A], predicate: A => Boolean): Future[A] = {
    action().filter(predicate).recoverWith {case _ => retryUntil(action, predicate)}
  }

  def main(args: Array[String]): Unit = {
//    fullfillImmediate(65)

    val fast = Future {
      Thread.sleep(1000)
      "fast future"
    }
    val slow = Future {
      Thread.sleep(2000)
      "slow future"
    }

    first(fast,slow).onComplete {
      case Success(value) => println(value)
      case Failure(exception) =>
    }

    last(fast,slow).onComplete {
      case Success(value) => println(value)
      case Failure(exception) =>
    }

    val random = new Random
    val action = () => Future {
      Thread.sleep(500)
      val num = random.nextInt(100)
      println("generated number: " + num)
      num
    }
    val predicate = (x: Int) => x < 20

    retryUntil(action, predicate).foreach(x => println("settled at " + x))

    Thread.sleep(5000)
  }
}
