package MyPilotProjects.YoutubeVideos.TypeLevelProgramming

import scala.reflect.runtime.universe._

object Part1 {
  def show[T](value: T)(implicit ttag: TypeTag[T]) = ttag.toString().replace("YoutubeVideos.TypeLevelProgramming.Part1.", "")
  case class Person(name: String)

  // type-level programming: using compiler as a program executor
  trait Natural
  class Zero extends Natural
  class Successor[N <: Natural] extends Natural

  type One = Successor[Zero] // this is "Peano Arithmetic" method
  type Two = Successor[One]
  type Three = Successor[Two]
  type Four = Successor[Three]

  // type relationships: Two < Four ?
  trait < [A <: Natural, B <: Natural]
  object < {
    implicit def lessThanBasic[B <: Natural]: <[Zero, Successor[B]] = new <[Zero, Successor[B]] {}
    implicit def inductive[A <: Natural, B <: Natural](implicit lt: <[A,B]) = new <[Successor[A], Successor[B]] {}
    def apply[A <: Natural, B <: Natural](implicit lt: <[A,B]) = lt
  }

//  trait <= [A <: Natural, B <: Natural]
//  object <= {
//    implicit def lessThanBasic[B <: Natural]: <=[Zero, Successor[B]] = new <=[Zero, Successor[B]] {}
//    implicit def inductive[A <: Natural, B <: Natural](implicit lt: <=[A,B]) = new <[Successor[A], Successor[B]] {}
//    def apply[A <: Natural, B <: Natural](implicit lt: <=[A,B]) = lt
//  }

  trait > [A <: Natural, B <: Natural]
  object > {
    implicit def greaterThanBasic[B <: Natural]: >[Successor[B], Zero] = new >[Successor[B], Zero] {}
    implicit def inductive[A <: Natural, B <: Natural](implicit lt: >[A,B]) = new >[Successor[A], Successor[B]] {}
    def apply[A <: Natural, B <: Natural](implicit lt: >[A,B]) = lt
  }

  def main(args: Array[String]): Unit = {
    val john = Person("John")
    println(show(john))

    val validCompare: One < Two = <[One, Two]
    val invalidCompare: Two > One = >[Two, One]
  }
}
