package AdvancedScala.Exercise

object PathDependentTypes {
  abstract class Item[Key] {
    def get(index: Key): Key
  }
  class IntItem extends Item[Int] {
    type intToGet = Int
    override def get(index: intToGet): Int = ???
  }
  class StringItem extends Item[String] {
    type stringToGet = String
    override def get(index: stringToGet): String = ???
  }

  def main(args: Array[String]): Unit = {
    val intItem = new IntItem
    intItem.get(4)
  }
}

/*
object PathDependentTypes {
  trait ItemBase {
    type Key
  }
  trait Item[T] extends ItemBase {
    type Key = T
  }
  trait IntItem extends Item[Int]
  trait StringItem extends Item[String]

  def get[ItemType <: ItemBase](index: ItemType#Key): ItemType = ???

  def main(args: Array[String]): Unit = {
    get[Item[Int]](5)
  }
}
*/
