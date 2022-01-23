package AdvancedScala.TypeSystems

object NaiveSolution {
  trait Animal {
    def breed: List[Animal]
  }

  trait Cat extends Animal {
    override def breed: List[Cat]
  }
  trait Dog extends Animal {
    override def breed: List[Dog]
  }
}


object FBoundedPolymorphism {
  trait Animal[A <: Animal[A]] { // recursive types
    def breed: List[Animal[A]]
  }

  trait Cat extends Animal[Cat] {
    override def breed: List[Animal[Cat]]
  }
  trait Dog extends Animal[Dog] {
    override def breed: List[Animal[Dog]]
  }
}


object Solution3 {
  trait Animal[A <: Animal[A]] { self: A =>
    def breed: List[Animal[A]]
  }

  trait Cat extends Animal[Cat] {
    override def breed: List[Animal[Cat]]
  }
  trait Dog extends Animal[Dog] {
    override def breed: List[Animal[Dog]]
  }
  trait Fish[A] extends Animal[Fish[A]] {
    override def breed: List[Animal[Fish[A]]] = ???
  }
  trait Shark extends Fish[Shark] {
    override def breed: List[Animal[Fish[Shark]]] = ???
  }
}


object Solution4 {
  trait Animal
  trait CanBreed[A] {
    def breed[A]: List[A]
  }

  class Dog extends Animal
  object Dog {
    implicit object CanDogsBreed extends CanBreed[Dog] {
      override def breed[Dog]: List[Dog] = List()
    }
  }

  implicit class CanBreedOps[A](animal: A) {
    def breed(implicit canBreed: CanBreed[A]) = canBreed.breed
  }

  class Cat extends Animal
  object Cat {
    implicit object CanCatsBreed extends CanBreed[Cat] {
      override def breed[Cat]: List[Cat] = List()
    }
  }

  def main(args: Array[String]): Unit = {
    val dog = new Dog
    dog.breed

    val cat = new Cat
    cat.breed
  }
}


object Solution5 {
  trait Animal[A] {
    def breed[A]: List[A]
  }

  class Dog
  object Dog {
    implicit object AnimalDog extends Animal[Dog] {
      override def breed[A]: List[A] = List()
    }
  }

  implicit class AnimalOps[A](animal: A) {
    def breed(implicit animalTypeClassInstance: Animal[A]): List[A] = animal.breed
  }

  def main(args: Array[String]): Unit = {
    val dog = new Dog
    dog.breed
  }
}

