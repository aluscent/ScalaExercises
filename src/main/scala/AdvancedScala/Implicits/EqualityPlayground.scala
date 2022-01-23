package AdvancedScala.Implicits

import AdvancedScala.Implicits.TypeClasses._

// equality type-class exercise
object EqualityPlayground extends App {
  val john = new Person("john", 24)
  val otherJohn = new Person("john", 34)

  println(Equality(john, otherJohn))
  println(john === otherJohn)
  println(john !== otherJohn)
}
