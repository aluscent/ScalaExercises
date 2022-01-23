package AdvancedScala.Implicits

object Exercise {
  trait Multiplier[A, B] {
    def **(base: Int, power: Int): BigInt

    def length(value: A): Int

    def multiply(value: A, times: Int): B
  }

  implicit object IntMultiply extends Multiplier[Int, BigInt] {
    def **(base: Int, power: Int): BigInt = {
      def apply(num: Int, times: Int): BigInt = times match {
        case x if (x < 0) => throw new UnknownError()
        case 0 => 1
        case 1 => num
        case _ => apply(num * base, times - 1)
      }
      apply(1, power)
    }

    def length(value: Int): Int = value.toString.length

    def multiply(value: Int, times: Int): BigInt = if (times > 0) (1 to times).map(x => value * (10 ** ((x - 1) * value.length))).sum else 0
  }

  // type-enrichment here:
  implicit class Inter(value: Int) {
    def **(power: Int)(implicit multiplier: Multiplier[Int, BigInt]): BigInt = multiplier.**(value, power)

    def length(implicit multiplier: Multiplier[Int, BigInt]): Int = multiplier.length(value)

    def multiply(times: Int)(implicit multiplier: Multiplier[Int, BigInt]): BigInt = multiplier.multiply(value, times)
  }


  def main(args: Array[String]): Unit = {
    val num = 958
    println(num.multiply(2))

    val times = 12
    val nums = (1 to times).map(x => num ** ((x - 1) * num.length))
    println(nums)

    case class Permissions(mask: String)
    implicit val defaultPerms: Permissions = new Permissions("0744")
    val implicits = implicitly[Permissions]
  }
}
