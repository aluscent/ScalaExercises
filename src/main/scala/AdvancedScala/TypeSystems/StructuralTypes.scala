package AdvancedScala.TypeSystems

import scala.language.reflectiveCalls

object StructuralTypes {
  type JavaCloseable = java.io.Closeable

  class HipsterCloseable {
    def close(): Unit = ???
  }

  type UnifiedCloseable = {
    def close(): Unit
  } // Structural Type


  type RefinedClosable = JavaCloseable {
    def closeSwiftly(): Unit
  } // Refined Type


  // using structural types as standalone types
  def altCloseable(closeable: {def close(): Unit}): Unit = closeable.close()


  // type-checking: dock-typing
  type soundMaker = {
    def makeSound(): Unit
  }
  class Dog {
    def makeSound(): Unit = println("bark!")
  }
  class Car {
    def makeSound(): Unit = println("vrooom!")
  }


  def main(args: Array[String]): Unit = {
    def closeUnified(unifiedCloseable: UnifiedCloseable): Unit = unifiedCloseable.close()

    closeUnified(new JavaCloseable {
      override def close(): Unit = ???
    })
    closeUnified(new HipsterCloseable)


    // static duck typing
    val dog: soundMaker = new Dog // this works as long as types on the right-hand side, conform to STRUCTURE of the types on left-hand side


  }
}
