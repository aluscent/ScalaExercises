package AdvancedScala.Implicits

object Organizing extends App {
  val tempList = List(4,6,3,5,8,3,6)
  println(tempList.sorted)

  implicit val reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  println(tempList.sorted)
  // the sorted uses an implicit ordering

  /*
  AdvancedScala.Implicits (used as implicit parameters)
    -val/var
    -accessor methods
    -object
  */
  case class Person(name: String, age: Int)

  object AgeOrdering {
    implicit val ageOrder: Ordering[Person] = Ordering.fromLessThan(_.age < _.age)
  }
  object nameOrdering {
    implicit val ageOrder: Ordering[Person] = Ordering.fromLessThan((a,b) => a.name.compareTo(b.name) < 0)
  }

  import AgeOrdering._
  println(List(Person("ali",24),Person("mhmd",30),Person("hamid",14)).sorted)

  /*
  search for AdvancedScala.Implicits:
    -normal scope
    -import scope
    -companion objects of all types in method signature
  */

  case class Purchase(units: Int, price: Int) {
    val totalPrice = units * price
  }

  object PurchaseOrdering {
    implicit val totalPriceOrder: Ordering[Purchase] = Ordering.fromLessThan(_.totalPrice < _.totalPrice)
    implicit val unitsOrder: Ordering[Purchase] = Ordering.fromLessThan(_.units < _.units)
    implicit val priceOrder: Ordering[Purchase] = Ordering.fromLessThan(_.price < _.price)
  }

  import PurchaseOrdering.totalPriceOrder
  val purchases = List(Purchase(5,150),Purchase(10,100),Purchase(6,250),Purchase(1,500))
  println(purchases.sorted)
}
