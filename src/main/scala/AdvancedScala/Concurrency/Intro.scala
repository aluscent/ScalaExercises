package AdvancedScala.Concurrency

import java.util.concurrent.Executors

object Intro {
  def runInParallel = {
    var x = 0

    val thread1 = new Thread(new Runnable {
      override def run(): Unit = x += 1
    })

    val thread2 = new Thread(new Runnable {
      override def run(): Unit = x += 1
    })

    thread1.start()
    thread2.start()

//    Thread.sleep(0,1)
    println(x)
  }

  class Bank(var amount: Int) {
    override def toString: String = "current amount is " + amount
    def buy(price: Int) = amount -= price
  }

  def main(args: Array[String]): Unit = {

    /*
    (1 to 8).foreach(x => (new Thread(new Runnable {
      override def run(): Unit = {
        println(s"running thread $x ...")
      }
    })).start())

    val runnable = new Runnable {
      override def run(): Unit = println(s"running thread ...")
    }
    val pool = Executors.newFixedThreadPool(8)

    pool.execute(runnable)
    pool.execute(() => {
      println("hi")
      Thread.sleep(1500)
      println("bye")
    })

    pool.shutdown()

     */

    for(_ <- 1 to 0) {
      val account = new Bank(5000)
      val t1 = new Thread(() => account.buy(450))
      val t2 = new Thread(() => account.buy(250))
      val t3 = new Thread(() => account.buy(300))
      t1.start()
      t2.start()
      t3.start()
      Thread.sleep(10)
      if(account.amount != 4000)
        println(account)
    }

    var xs = 0
    var threads = List[Thread]()
    for(_ <- 1 to 10000) threads = new Thread(() => {
      xs += 1
//      println(s"thread $xs")
    }) :: threads
    threads.foreach(x => {
      x.start()
    })
    println("overall " + xs)
  }
}
