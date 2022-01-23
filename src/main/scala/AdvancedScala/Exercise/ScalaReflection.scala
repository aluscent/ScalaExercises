package AdvancedScala.Exercise

import scala.reflect.runtime.{universe => ru}

object ScalaReflection {
  case class Person(name: String) {
    def callName: Unit = println(name)
  }

  def main(args: Array[String]): Unit = {
    val person = Person("John")
    val method = "callName"
    val mirror = ru.runtimeMirror(getClass.getClassLoader)
    val reflected = mirror.reflect(person)
    val methodSymbol = ru.typeOf[Person].decl(ru.TermName(method)).asMethod
    val reflectedMethod = reflected.reflectMethod(methodSymbol)

    reflectedMethod.apply()
  }
}
