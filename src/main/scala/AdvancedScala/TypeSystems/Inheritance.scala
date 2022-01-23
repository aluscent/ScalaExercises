package AdvancedScala.TypeSystems

object Inheritance extends App {
  trait Writer[T] {
    def write(value: T): Unit
  }

  trait Closable {
    def close(status: Int): Unit
  }

  trait GenericStream[T] {
    def foreach(function: T => Unit): Unit
  }

  def processStream[T](value: GenericStream[T] with Writer[T] with Closable): Unit = {
    value.foreach(println)
    value.close(1)
  }


  // diamond problem
  trait Animal {def name: String}
  trait Mammal extends Animal {override def name = "Mammals"}
  trait Lion extends Mammal {override def name = "Lion"}
  trait Tiger extends Mammal {override def name = "Tiger"}
  class Mutant extends Lion with Tiger //{override def name: String = "Mutant"}

  val mutant = new Mutant
  println(mutant.name)
  // LAST OVERRIDE GETS PICKED (here the last inheritance was Tiger so Tiger's name gets picked)


  // the super problem + type linearization
  trait Cold {
    def print: Unit = println("cold")
  }
  trait Green extends Cold {
    override def print: Unit = {
      println("green")
      super.print
    }
  }
  trait Blue extends Cold {
    override def print: Unit = {
      println("blue")
      super.print
    }
  }
  trait Red {
    def print: Unit = {
      println("red")
    }
  }
  class White extends Red with Green with Blue {
    override def print: Unit = {
      println("white")
      super.print
    }
  }
  //  type linearization for White:
  //    White = Red with Green with Blue = (AnyRef with Red) with (AnyRef with Cold with Green) with (AnyRef with Cold with Blue) with White
  //    = AnyRef with Red with Cold with Green with Blue with White
  //  here ^^^ super looks at the immediate left of itself. For White, it's Blue, for Blue it's Green.

  val white = new White
  white.print
}
