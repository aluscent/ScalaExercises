package MyPilotProjects.YoutubeVideos.TypeLevel

import scala.language.higherKinds

object TestCases2 {

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

  object NatTypeSpec {
    import Nat._

    implicitly[Plus[N[Zero],N[N[Zero]]] =:= N[N[N[Zero]]]]
  }
}
