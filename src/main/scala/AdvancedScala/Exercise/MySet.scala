package AdvancedScala.Exercise

import scala.annotation.tailrec
import scala.collection.View.Empty

trait MySet[A] extends (A => Boolean) {
  def apply(element: A): Boolean = contains(element)
  def contains(element: A): Boolean
  def +(element: A): MySet[A]
  def ++(other: MySet[A]): MySet[A]

  def map[B](function: A => B): MySet[B]
  def flatMap[B](function: A => MySet[B]): MySet[B]
  def filter(function: A => Boolean): MySet[A]
  def foreach(function: A => Unit): Unit

  def -(element: A): MySet[A]
  def &(other: MySet[A]): MySet[A]
  def --(other: MySet[A]): MySet[A]

  def mkString(delimiter: String): String = {
    var tempStr = ""
    this.foreach(x => tempStr += delimiter + x)
    tempStr
  }
  override def toString(): String = "MySet(" + (this mkString(", ")) + ")"

  def unary_! : MySet[A]
}

class PropertyBasedSet[A](property: A => Boolean) extends MySet[A] {
  def contains(element: A): Boolean = property(element)
  def +(element: A): MySet[A] =
    if(!property(element)) new PropertyBasedSet[A](x => property(x) || x == element)
    else this
  def ++(other: MySet[A]): MySet[A] = new PropertyBasedSet[A](x => property(x) || other(x))

  def map[B](function: A => B): MySet[B] = fail
  def flatMap[B](function: A => MySet[B]): MySet[B] = fail
  def filter(function: A => Boolean): MySet[A] = new PropertyBasedSet[A](x => property(x) && function(x))
  def foreach(function: A => Unit): Unit = fail

  def -(element: A): MySet[A] = new PropertyBasedSet[A](x => property(x) && x != element)
  def &(other: MySet[A]): MySet[A] = new PropertyBasedSet[A](x => property(x) & other(x))
  def --(other: MySet[A]): MySet[A] = new PropertyBasedSet[A](x => property(x) && !other(x))

  def unary_! : MySet[A] = new PropertyBasedSet[A](x => !property(x))

  def fail = throw new IllegalArgumentException
}

class EmptySet[A] extends MySet[A] {
  override def contains(element: A): Boolean = false
  override def +(element:  A): MySet[A] = new NonEmptySet[A](element, this)
  override def ++(other: MySet[A]): MySet[A] = other

  override def map[B](function: A => B): MySet[B] = new EmptySet[B]
  override def flatMap[B](function: A => MySet[B]): MySet[B] = new EmptySet[B]
  override def filter(function: A => Boolean): MySet[A] = this
  override def foreach(function: A => Unit): Unit = ()

  override def -(element: A): MySet[A] = this
  override def &(other: MySet[A]): MySet[A] = this
  override def --(other: MySet[A]): MySet[A] = other

  override def toString(): String = super.toString()

  def unary_! : MySet[A] = new PropertyBasedSet[A](_ => true)
}

class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A] {
  override def contains(element: A): Boolean =
    head == element || tail.contains(element)
  override def +(element: A): MySet[A] =
    if(this.contains(element)) this
    else new NonEmptySet[A](element, this)
  override def ++(other: MySet[A]): MySet[A] =
    tail ++ other + head

  override def map[B](function: A => B): MySet[B] = tail.map(function) + function(head)
  override def flatMap[B](function: A => MySet[B]): MySet[B] = tail.flatMap(function) ++ function(head)
  override def filter(function: A => Boolean): MySet[A] =
    if(function(head)) tail.filter(function) + head
    else tail.filter(function)
  override def foreach(function: A => Unit): Unit = {
    function(head)
    tail.foreach(function)
  }

  override def -(element: A): MySet[A] = this.filter(x => x != element)
  override def &(other: MySet[A]): MySet[A] = this.filter(x => other.contains(x))
  override def --(other: MySet[A]): MySet[A] = (this ++ other).filter(x => !(this(x) && other(x)))

  override def toString(): String = super.toString()

  def unary_! : MySet[A] = new PropertyBasedSet[A](x => !this(x))
}

object MySet {
  def apply[A](values: A*): MySet[A] = {
    @tailrec
    def buildSet(valueSet: Seq[A], acc: MySet[A]): MySet[A] =
      if(valueSet.isEmpty) acc
      else buildSet(valueSet.tail, acc + valueSet.head)

    buildSet(values.toSeq, new EmptySet[A])
  }

}

object test {
  def main(args: Array[String]): Unit = {
    val newSet = MySet[Int](1, 4, 3)

    println(newSet.map(x => x * 2))
  }
}
