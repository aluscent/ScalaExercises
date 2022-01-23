package AdvancedScala.Concurrency

import scala.collection.mutable
import scala.util.Random

object ThreadCommunication {
  class SimpleContainer {
    private var value = 0
    private var consumers = List[Consumer]()
    private var producers = List[Producer]()
    def isEmpty: Boolean = value == 0
    def get: Int = {
      val result = value
      value = 0
      result
    }
    def set(newValue: Int) = value = newValue
    def addConsumer(consumer: Consumer) = consumers = consumer :: consumers
    def addProducer(producer: Producer) = producers = producer :: producers
  }

  trait Agents {
    def run: Unit
    def consistentRun = while(true) run
  }

  class Consumer(id: Int, container: mutable.Queue[Int]) extends Thread with Agents {
    override def run: Unit = {
      container.synchronized {
        while (container.isEmpty) {
          println(s"[consumer] [$id] container was empty!")
          container.wait()
        }
        println(s"[consumer] [$id] consumed value: " + container.dequeue())
        container.notify()
      }
      Thread.sleep(250)
    }
  }

  class Producer(id: Int, container: mutable.Queue[Int], maxCap: Int) extends Thread with Agents {
    override def run: Unit = {
      val random = new Random()
      container.synchronized {
        while (container.length >= maxCap) {
          println(s"[producer] [$id] buffer is full!")
          container.wait()
        }
        val newValue = random.between(100,500)
        println(s"[producer] [$id] produced value: " + newValue)
        container.enqueue(newValue)
        container.notify()
      }
      Thread.sleep(200)
    }
  }

  def multiAgent(nCons: Int, nProds: Int, container: mutable.Queue[Int], bufferLength: Int): List[Agents] = {
    var agents = List[Agents]()
    for(x <- 1 to nProds) agents = (new Producer(5000 + x, container, bufferLength)) :: agents
    for(x <- 1 to nCons) agents = (new Consumer(1000 + x, container)) :: agents
    agents
  }

  def main(args: Array[String]): Unit = {
    val cap = 10
//    val container = new SimpleContainer
    val container = new mutable.Queue[Int]()

    multiAgent(5, 4, container, cap).foreach(x => new Thread(() => x.consistentRun).start())
  }
}
