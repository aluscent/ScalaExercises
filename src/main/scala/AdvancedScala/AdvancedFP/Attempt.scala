package AdvancedScala.AdvancedFP

object test {
  trait Attempt[+A] {
    def flatMap[B >: A](function: B => Attempt[B]): Attempt[B]
  }

  object Attempt {
    def apply[A](a: => A): Attempt[A] =
      try {
        Success(a)
      } catch {
        case e: Throwable => Failure(e)
      }
  }

  case class Success[+A](value: A) extends Attempt[A] {
    def flatMap[B >: A](function: B => Attempt[B]): Attempt[B] =
      try {
        function(value)
      } catch {
        case e: Throwable => Failure(e)
      }
  }

  case class Failure(throwable: Throwable) extends Attempt[Nothing] {
    override def flatMap[B >: Nothing](function: B => Attempt[B]): Attempt[B] = this
  }


  def main(args: Array[String]): Unit = {
    val sample1: Attempt[Int] = Attempt(5)

    sample1.flatMap(x => Attempt(x))
  }
}