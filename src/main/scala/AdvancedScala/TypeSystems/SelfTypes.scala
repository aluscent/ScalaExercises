package AdvancedScala.TypeSystems

object SelfTypes {
  trait Instrumentalist {
    def play: Unit
  }
  trait Singer {self: Instrumentalist => // who ever implements singer, implements instrumentalist as well
    def sing(): Unit = ???
  }

  trait LeadSinger extends Singer with Instrumentalist { // Singer trait SHOULD be extended with Instrumentalist
    override def play: Unit = ???
    override def sing(): Unit = ???
  }


  class Guitarist extends Instrumentalist {
    override def play: Unit = println("playing guitar!")
  }


  // self-types vs. inheritance
  class A
  class B extends A // B is an A
  // but:
  trait C
  trait D { self: C => } // D requires C


  def main(args: Array[String]): Unit = {
    val chesterBennington = new Singer with Instrumentalist {
      override def play: Unit = ???
      override def sing(): Unit = ???
    }


    val ericClapton = new Guitarist with Singer {
      override def sing(): Unit = ???
    }
  }
}
