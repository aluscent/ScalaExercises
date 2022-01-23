package AdvancedScala.MyStream

abstract class MyStream[+A] {
  def isEmpty: Boolean
  def head: A
  def tail: MyStream[A]

  def #::[B >: A](element: B) : MyStream[B]
  def ++[B >: A](other: => MyStream[B]): MyStream[B]

  def foreach(function: A => Unit): Unit
  def map[B](function: A => B): MyStream[B]
  def flatMap[B](function: A => MyStream[B]): MyStream[B]
  def filter(predicate: A => Boolean): MyStream[A]

  def take(index: Int = 1): MyStream[A]
  def toList: List[A]
}

class MyEmptyStream extends MyStream[Nothing] {
  def isEmpty: Boolean = true
  def head: Nothing = throw new NoSuchElementException
  def tail: MyStream[Nothing] = throw new NoSuchElementException

  def #::[B >: Nothing](element: B): MyStream[B] = new MyNonEmptyStream[B](element, this)
  def ++[B >: Nothing](other: => MyStream[B]): MyStream[B] = other

  def foreach(function: Nothing => Unit): Unit = ()
  def map[B](function: Nothing => B): MyStream[B] = this
  def flatMap[B](function: Nothing => MyStream[B]): MyStream[B] = this
  def filter(predicate: Nothing => Boolean): MyStream[Nothing] = this

  def take(index: Int): MyStream[Nothing] = this
  def toList: List[Nothing] = List[Nothing]()
}

class MyNonEmptyStream[+A](_head: A, _tail: => MyStream[A]) extends MyStream[A] {
  def isEmpty: Boolean = false
  val head: A = _head
  lazy val tail: MyStream[A] = _tail

  override def #::[B >: A](element: B): MyStream[B] = new MyNonEmptyStream[B](element, this)
  def ++[B >: A](other: => MyStream[B]): MyStream[B] = new MyNonEmptyStream[B](head, this.tail ++ other)

  def foreach(function: A => Unit): Unit = {
    function(head)
    tail.foreach(function)
  }
  def map[B](function: A => B): MyStream[B] = new MyNonEmptyStream[B](function(head), tail.map(function))
  def flatMap[B](function: A => MyStream[B]): MyStream[B] = tail.flatMap(function) ++ function(head)
  def filter(predicate: A => Boolean): MyStream[A] =
    if(predicate(head)) head #:: (tail.filter(predicate))
    else tail.filter(predicate)

  def take(index: Int): MyStream[A] = {
    index match {
      case n if n <= 0 => new MyEmptyStream
      case 1 => new MyNonEmptyStream[A](head, new MyEmptyStream)
      case _ => new MyNonEmptyStream[A](head, tail.take(index - 1))
    }
  }

  override def toList: List[A] = {
    def iterate(str: MyStream[A], acc: List[A]): List[A] = {
      if(!str.tail.isEmpty)
        iterate(str.tail, str.head :: acc)
      else str.head :: acc
    }

    iterate(this, List[A]())
  }
}

object MyStream {
  def from[A](start: A)(generator: A => A): MyStream[A] = new MyNonEmptyStream[A](start, MyStream.from(generator(start))(generator))
  def fibbonatchi(start1: BigInt, start2: BigInt): MyStream[BigInt] = new MyNonEmptyStream[BigInt](start1 + start2, MyStream.fibbonatchi(start2, start1 + start2))
  def eratosthene(stream: MyStream[Int]): MyStream[Int] =
    if(stream.isEmpty) stream
    else new MyNonEmptyStream[Int](stream.head, eratosthene(stream.tail.filter(_ % stream.head != 0)))
}

object test {
  def main(args: Array[String]): Unit = {
    val fibbo = MyStream.fibbonatchi(1,1)
    println(fibbo.take(100).toList)

    val erato = MyStream.eratosthene(MyStream.from[Int](2)( _ + 1).take(1000))
    println(erato.toList)
  }
}