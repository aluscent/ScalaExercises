package AdvancedScala.Implicits

object Intro extends App {
  val pair = "Ali" -> "555" -> 24

  case class Person(name: String) {
    def greet = "new person " + name
  }

  implicit def toPerson(name: String): Person = Person(name)

  println("Ali".greet)


  def increment(x: Int)(implicit pace: Int) = x + pace
  implicit val defaultPace: Int = 5

  println(increment(9))
}
