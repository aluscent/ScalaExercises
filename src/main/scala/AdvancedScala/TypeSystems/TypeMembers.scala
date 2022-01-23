package AdvancedScala.TypeSystems

object TypeMembers {
  class Animal
  class Dog extends Animal
  class Cat extends Animal

  class AnimalCollection {
    type AnimalType
    type BoundedAnimal <: Animal
    type SuperBoundedAnimal >: Dog <: Animal
  }

  def main(args: Array[String]): Unit = {
    val animalCollection = new AnimalCollection
    val collectionDog: animalCollection.SuperBoundedAnimal = new Dog
    // val collectionCat: animalCollection.BoundedAnimal = new Cat
  }
}
