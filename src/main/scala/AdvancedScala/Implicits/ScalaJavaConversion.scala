package AdvancedScala.Implicits

import java.{util => ju}
import java.util.Optional

object ScalaJavaConversion {

  class ToScala[T](value: => T) {
    def asScala: T = value
  }

  implicit def converter[T](value: Optional[T]): ToScala[Option[T]] =
    new ToScala[Option[T]](if (value.isPresent) Some(value.get()) else None)

  import scala.jdk.CollectionConverters

  def main(args: Array[String]): Unit = {
    val javaSet: ju.Set[Int] = new ju.HashSet[Int]()
    (1 to 10).foreach(javaSet.add)

    println(javaSet)

    val scalaSet = javaSet

    val javaOptional = Optional.of(435)
    println(javaOptional.asScala)
  }
}
