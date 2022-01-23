package AdvancedScala.AdvancedFP

object LazyEvaluation extends App {

  println("test 1:")
  lazy val test1 = {
    println("hello")
    42
  }
  println("test 2:")
  val test2 = {
    println("hello")
    42
  }

  println(test1)

  println(test1)
}
