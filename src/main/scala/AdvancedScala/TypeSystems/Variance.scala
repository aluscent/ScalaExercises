package AdvancedScala.TypeSystems

object Variance extends App {

  class Animal
  class Dog extends Animal { def dogSound = println("Dog") }
  class Cat extends Animal { def catSound = println("Cat") }
  class Crocodile extends Animal { def crocodileSound = println("Crocodile")}
  class Bulldog extends Dog { def bulldogSound = println("Bulldog") }

  // co-variance
  class CoCage[+T]
  val coDog: CoCage[Animal] = new CoCage[Dog]

  // in-variance
  class InCage[T]
  val inDog: InCage[Dog] = new InCage[Dog]

  // contra-variance
  class ContraCage[-T]
  val contraDog: ContraCage[Bulldog] = new ContraCage[Dog]

  // blah blah blah
  class CovariantCageVal[+T](val animal: T)
  //class CovariantCageVar[+T](var animal: T)
  val sample: CovariantCageVal[Animal] = new CovariantCageVal[Dog](new Dog)

  //class ContravariantCageVal[-T](val animal: T)
  //class ContravariantCageVar[-T](var animal: T)
}
