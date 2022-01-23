package AdvancedScala.TypeSystems

object HigherKindedTypes {
  trait AHigherKindedType[F[_]] // this is a higher kinded type!

  trait MyMonad[F[_], A] {
    def flatMap[B](function: A => F[B]): F[B]
  }

  implicit class MyOption[A](option: Option[A]) extends MyMonad[Option, A] {
    override def flatMap[B](function: A => Option[B]): Option[B] = option.flatMap(function)
  }

//  implicit def multiplier[F[_], A, C](implicit monadA: MyMonad[F, A], monadB: MyMonad[F, C]): F[(A,C)] = 
//    for {
//      x <- monadA
//      y <- monadB
//    } yield (xs, ys)

  def main(args: Array[String]): Unit = {
    val myOption = new MyOption[Int](Option(1))
  }
}
