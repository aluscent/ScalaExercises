package AdvancedScala.AdvancedFP

object Monads {
  class Lazy[+A](value: => A) {
    lazy val internalValue = value

    def use: A = internalValue

    def flatMap[B](function: (=> A) => Lazy[B]): Lazy[B] = function(internalValue)
    //    def map[B](function: (=> A) => B): Lazy[B] = flatMap(x => Lazy(function(x)))
    //    def flatten: Lazy[A] = flatMap((x: Lazy[A]) => x)
  }

  object Lazy {
    def apply[A](value: => A): Lazy[A] = new Lazy[A](value)
  }

  def main(args: Array[String]): Unit = {
    val sample1 = Lazy {
      println("hello world!")
      543
    }

    val sample2 = Lazy {
      println("hi bitch!")
      956
    }

    sample1.flatMap(x => Lazy {
      76 * x
    })
    sample1.use
    sample1.use
    sample2.use
    sample2.use

  }
}
