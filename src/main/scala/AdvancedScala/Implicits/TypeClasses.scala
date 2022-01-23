package AdvancedScala.Implicits

object TypeClasses {
  // this is a type-class
  trait ObjectSerializer[T] {
    def serializer(value: T): String
  }

  case class Person(name: String, age: Int)

  // serializers for every type
  implicit object PersonSerializer extends ObjectSerializer[Person] {
    def serializer(person: Person): String = s"<div>${person.name} is just ${person.age} yo.</div>"
  }

  import java.util.Date

  object DateSerializer extends ObjectSerializer[Date] {
    override def serializer(value: Date): String = ???
  }

  // defining multiple serializers for a specific type
  object UserSerializer extends ObjectSerializer[Person] {
    override def serializer(value: Person): String = s"<div>${value.name}</div>"
  }

  // we can make implicit actions for type-classes
  object HTMLSerializer {
    def serialize[T](value: T)(implicit serialize: ObjectSerializer[T]): String = serialize.serializer(value)
  }

  implicit object IntSerializer extends ObjectSerializer[Int] {
    override def serializer(value: Int): String = s"<div>$value</div>"
  }


  // equality type-class exercise //
  trait Equality[T] {
    def isEqual(one: T, other: T): Boolean
  }

  object Equality {
    def apply[T](one: T, another: T)(implicit equalizer: Equality[T]): Boolean =
      equalizer.isEqual(one, another)
  }

  implicit object PersonEquality extends Equality[Person] {
    override def isEqual(one: Person, other: Person): Boolean = one.name == other.name
  }

  implicit class Enricher[T](value: T) {
    def ===(other: T)(implicit comparer: Equality[T]): Boolean = comparer.isEqual(value, other)

    def !==(other: T)(implicit comparer: Equality[T]): Boolean = !comparer.isEqual(value, other)
  }

  def main(args: Array[String]): Unit = {
    val john = Person("john", 24)

    println(HTMLSerializer.serialize(42))
    println(HTMLSerializer.serialize(john))
  }
}