package AdvancedScala.Exercise

object Input {
  def main(args: Array[String]): Unit = {
    scala.io.Source.stdin.getLines().foreach(x => println(x))
  }
}
