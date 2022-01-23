package AdvancedScala.Exercise

object DarkSugars {
  class sample {
    private var internalMember: Int = 0
    def member: Int = internalMember
    def member_=(mem: Int): Unit = mem match {
      case x if(x > 100 & x < 200) => internalMember = x
      case _ => internalMember = 150
    }
  }

  def main(args: Array[String]): Unit = {
    val halu = new sample

    halu.member = 50

    println(halu.member)
  }
}
