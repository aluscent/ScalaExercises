package AdvancedScala.Concurrency

import java.util.concurrent.atomic.AtomicReference
import scala.collection.parallel.immutable.ParVector
import scala.collection.parallel.CollectionConverters._
import scala.collection.parallel.ForkJoinTaskSupport
//import scala.concurrent.forkjoin.ForkJoinPool

object ParallelUtils extends App {
  // parallel collections
  val aParList = List[Int](1,2,3,4,5).par

  val aParVector = ParVector[Int]()

  def measureTime[T](action: => T): (Long, T) = {
    val startTime = System.currentTimeMillis()
    val result = action
    (System.currentTimeMillis() - startTime, result)
  }

  /*
  val testList: List[Long] = (2 to 10000000).toList.map(x => x.toLong)
  val parTestList = testList.par

  println("serial list: " + measureTime(testList.reduce(_ + _)))
  println("parallel list: " + measureTime(parTestList.reduce(_ + _)))

  aParVector.tasksupport = new ForkJoinTaskSupport(ForkJoinPool(2))
  */

  val anAtomic = new AtomicReference[Int](2)
  val atomicValue = anAtomic.get() // thread-safe read
  anAtomic.set(5) // thread-safe write
  anAtomic.compareAndSet(35, 67) // reference equality

  println(anAtomic.updateAndGet(_ + 1))
  println(anAtomic.getAndUpdate(_ + 1))
  println(anAtomic.get())
}
