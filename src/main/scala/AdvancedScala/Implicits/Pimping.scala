package AdvancedScala.Implicits

object Pimping {
  implicit class RichInt(value: Int) {
    def isEven: Boolean = value % 2 == 0

    def times(function: () => Unit): Unit = (1 to value).foreach(x => function())

    def *[T](nums: List[T]): List[T] = (1 to value).toList.map(_ => nums).flatMap(list => list.iterator)
  }

  implicit class RichString(value: String) {
    def asInt: Int = try {
      value.toInt
    } catch {
      case _: Exception => 0
    }

    def encrypt: String = value.map(x => (x.toInt + 2).toChar)
  }

  def main(args: Array[String]): Unit = {
    println(4 times (() => println("times")))
    println(4 * List(1, 4, 6))

    println("hello".encrypt)
  }
}