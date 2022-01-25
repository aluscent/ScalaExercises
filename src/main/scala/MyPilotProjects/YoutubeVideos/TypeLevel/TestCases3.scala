package MyPilotProjects.YoutubeVideos.TypeLevel

import scala.language.higherKinds

object TestCases3 {

  sealed trait Nat {
    type Plus[That <: Nat] <: Nat
  }

  object Nat {
    type Plus[A <: Nat, B <: Nat] = A#Plus[B]
  }

  sealed trait Zero extends Nat {
    override type Plus[That <: Nat] = That
  }

  sealed trait N[Prev <: Nat] extends Nat {
    override type Plus[That <: Nat] = N[Prev#Plus[That]]
  }

  sealed trait Vec[V] {
    type Size <: Nat
    type Val <: V
    type Next <: Vec[V]
  }

  sealed trait EmptyVec[V] extends Vec[V] {
    override type Size = Zero
    override type Val = Nothing
    override type Next = Nothing
    type AddElement[Element <: V] = NonEmptyVec[V, Element, EmptyVec[V]]
  }

  sealed trait NonEmptyVec[V, Value <: V, Tail <: Vec[V]] extends Vec[V] {
    override type Size = N[Tail#Size]
    override type Val = Value
    override type Next = Tail
    type AddElement[Element <: V] = NonEmptyVec[V, Element, NonEmptyVec[V, Value, Tail]]
  }

  object VecSpec {
    implicitly[EmptyVec[Int]#AddElement[5]#AddElement[7] =:= NonEmptyVec[Int, 7, NonEmptyVec[Int, 5, EmptyVec[Int]]]]
  }
}
