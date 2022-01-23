package AdvancedScala.TypeSystems

object PathDependentTypes {
  class Outer {
    class Inner
    object Inner
    type InnerType

    def typeGeneral(argument: Outer#Inner) = ???
  }



  def main(args: Array[String]): Unit = {
    val outer = new Outer
    val inner = new outer.Inner

    val otherOuter = new Outer
    val otherInner = new otherOuter.Inner
    // val otherInner: otherOuter.Inner = outer.Inner
    // these types are path-dependent

    otherOuter.typeGeneral(inner)
  }
}
