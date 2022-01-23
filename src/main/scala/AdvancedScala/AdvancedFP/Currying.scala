package AdvancedScala.AdvancedFP

object Currying extends App {
  val simpleAddFunction = (x: Int, y: Int) => x + y

  def simpleAddMethod(x: Int, y: Int) = x + y

  def curryingAddMethod(x: Int)(y: Int) = x + y

  val add7_1 = (x: Int) => simpleAddFunction(x, 7)
  val add7_2 = simpleAddFunction(_: Int, 7)
  val add7_3 = simpleAddFunction.curried(7)

  def add7_4(x: Int) = simpleAddMethod(x, 7)

  val add7_5 = simpleAddMethod(7, _: Int)
  val add7_6 = curryingAddMethod(7) _
  val add7_7 = curryingAddMethod(7)(_)


}
