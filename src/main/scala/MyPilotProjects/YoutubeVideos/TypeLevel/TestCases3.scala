package MyPilotProjects.YoutubeVideos.TypeLevel

import scala.language.higherKinds

object TestCases3 {

  sealed class Nat {
    type Previous <: Nat
    type Plus[That <: Nat] <: Nat
  }

  object Nat {
    type Plus[A <: Nat, B <: Nat] = A#Plus[B]
  }

  class Zero extends Nat {
    override type Previous = Nothing
    override type Plus[That <: Nat] = That
  }

  class N[Prev <: Nat] extends Nat {
    override type Previous = Prev
    override type Plus[That <: Nat] = N[Prev#Plus[That]]
  }

  /**
   * Implementing Vector type with Map
   * @tparam V
   */
  sealed trait HList[V] {
    type Size <: Nat
    type Val <: V
    type Next <: HList[V]
    type AddElement[Element]
  }

  sealed class EmptyHList[V] extends HList[V] {
    override type Size = Zero
    override type Val = Nothing
    override type Next = Nothing
    override type AddElement[Element <: V] = NonEmptyHList[V, Element, EmptyHList[V]]
  }

  sealed class NonEmptyHList[V, Value: V, Tail <: HList[V]] extends HList[V] {
    override type Size = N[Tail#Size]
    override type Val = Value
    override type Next = Tail
    type AddElement[Element <: V] = NonEmptyHList[V, Element, NonEmptyHList[V, Value, Tail]]
  }

  object VecSpec {
    import Nat.Plus

    implicitly[EmptyHList[Int]#AddElement[5]#AddElement[7] =:= NonEmptyHList[Int, 7, NonEmptyHList[Int, 5, EmptyHList[Int]]]]
    implicitly[EmptyHList[Int]#AddElement[5]#AddElement[7]#Size =:= N[N[Zero]]]
    implicitly[EmptyHList[Int]#AddElement[5]#AddElement[7]#Val =:= 7]

    implicitly[Plus[N[N[Zero]],N[Zero]] =:= N[N[N[Zero]]]]
  }
}
