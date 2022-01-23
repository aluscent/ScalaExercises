package AdvancedScala.TypeSystems

import AdvancedScala.Exercise.ScalaReflection.Person
import scala.reflect.runtime.{universe => ru}

// reflection + macros + quasiqoutes => MetaProgramming(allows scala to modify even other programs)
object ScalaReflectionPart1 {
  case class Person(name: String) {
    def sayTheName(): Unit = println(name)
  }

  def main(args: Array[String]): Unit = {
    // 1 - Mirror:
    val mirror = ru.runtimeMirror(getClass.getClassLoader)

    // 2 - Create a class object [by NAME]:
    val theClass = mirror.staticClass("AdvancedScala.TypeSystems.ScalaReflection.Person")

    // 3 - Create a reflected mirror:
    val classMirror = mirror.reflectClass(theClass)

    // 4 - Constructor:
    val constructor = theClass.primaryConstructor.asMethod

    // 5 - Reflect the constructor:
    val constructorMirror = classMirror.reflectConstructor(constructor)

    // 6 - Invoke the constructor:
    val instance = constructorMirror.apply("John")

    println(instance)
  }
}


object ScalaReflectionPart2 extends App {
  val nums = (1 to 5).toList

  // Generic type erasure:
  // 1 - no differentiation between generic types
  nums match {
    case _: List[String] => println("strings") // this one executes every time
    case _: List[Int] => println("integers")
  }

  // 2 - limitations on overloads
  /* def processList(list: List[Int]) = "list of integers"
    def processList(list: List[String]) = "list of strings" // this one overrides
    println(processList(List(1,2))) */


  // Solution: Type tags
  import ru._
  val tTag = typeTag[Person]
  println(tTag.tpe)

  // example:
  class MyMap[K, V]
  def getTypeArgument[T](value: T)(implicit ttag: TypeTag[T]): List[Type] = ttag.tpe match {
    case TypeRef(_, _, typeArguments) => typeArguments
    case _ => List()
  }
  println(getTypeArgument(new MyMap[Int,String]))


  def isSubType[A,B](implicit ttagA: TypeTag[A], ttagB: TypeTag[B]): Boolean =
    ttagA.tpe <:< ttagB.tpe
  class Animal
  class Dog extends Animal
  println(isSubType[Animal,Dog])


  // these type tags can work with reflects:
  val person = Person("John")
  val method = "callName"
  val mirror = ru.runtimeMirror(getClass.getClassLoader)
  val reflected = mirror.reflect(person)
  val methodSymbol = typeTag[Person].tpe.decl(ru.TermName(method)).asMethod
  val reflectedMethod = reflected.reflectMethod(methodSymbol)
  reflectedMethod.apply()
}