package MyPilotProjects.YoutubeVideos.TypeLevel

import scala.language.higherKinds

object TestCases1 {

  sealed trait BoolType {
    type Not <: BoolType
    type Or[That <: BoolType] <: BoolType
    type And[That <: BoolType] <: BoolType
  }

  object BoolType {
    type \/[A <: BoolType, B <: BoolType] = A#Or[B]
    type /\[A <: BoolType, B <: BoolType] = A#And[B]
  }

  sealed trait TrueType extends BoolType {
    override type Not = FalseType
    override type Or[That <: BoolType] = TrueType
    override type And[That <: BoolType] = That
  }

  sealed trait FalseType extends BoolType {
    override type Not = TrueType
    override type Or[That <: BoolType] = That
    override type And[That <: BoolType] = FalseType
  }

  // compile to test
  object BoolTypeSpecs {
    import BoolType._

    implicitly[TrueType =:= TrueType]
    implicitly[FalseType =:= FalseType]
    implicitly[FalseType =:= TrueType#Not]
    implicitly[TrueType =:= FalseType#Or[TrueType]]
    implicitly[\/[TrueType,FalseType] =:= TrueType]
  }
}
