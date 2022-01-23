package AdvancedScala.Concurrency

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future, Promise}
import scala.util.Random

object Promises {
  sealed trait Status {val value = ""}
  case class Success() extends Status {override val value = "Success"}
  case class Failure() extends Status {override val value = "Failure"}

  case class User(name: String)
  case class Transaction(sender: User, receiver: User, amount: Int, status: Status)

  object BankingApp {
    val name = "first app"
    def fetchUser(name: String): Future[User] = Future {
      Thread.sleep(1000)
      User(name)
    }
    def createTransaction(user: User, merchant: User, amount: Int): Future[Transaction] = Future {
      Thread.sleep(1200)
      Transaction(user, merchant, amount, Success())
    }
    def purchase(user: User, item: String, merchant: User, cost: Int): Status = {
      val transactionStatus = for {
        transaction <- createTransaction(user, merchant, cost)
      } yield transaction.status

      Await.result(transactionStatus, 1.seconds)
    }
  }

  def main(args: Array[String]): Unit = {
    println("started app.")

    val bankingApp = BankingApp
    val user = User("john")
    val merchant = User("jim")

//    println(bankingApp.purchase(user, "cake", merchant, 30).value)

    val promise = Promise[Int]()
    val future = promise.future

    future.onComplete {
      case scala.util.Success(r) => println("[consumer] received: " + r)
    }

    val random = new Random()
    val producer = new Thread(() => {
      println("[producer] computing...")
      Thread.sleep(1000)
      promise.success(random.between(1000, 5000))
      println("[producer] produced.")
    })

    Thread.sleep(100)
    producer.start()
  }
}
