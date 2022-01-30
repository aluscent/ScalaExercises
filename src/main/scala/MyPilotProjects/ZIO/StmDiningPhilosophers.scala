package MyPilotProjects.ZIO

import zio._
import scala.collection.Iterable

object StmDiningPhilosophers extends App {

  import zio.console._
  import zio.stm._

  final case class Forks(number: Int)

  final case class Placement(left: TRef[Option[Forks]], right: TRef[Option[Forks]])

  final case class Seats(seats: Vector[Placement])

  def takeForks(left: TRef[Option[Forks]], right: TRef[Option[Forks]]): STM[Nothing, (Forks, Forks)] =
    (left.get collect { case Some(forks) => forks }) zip (right.get collect { case Some(forks) => forks })

  def putForks(left: TRef[Option[Forks]], right: TRef[Option[Forks]])(tuple: (Forks, Forks)): STM[Nothing, Unit] = {
    val (leftFork, rightFork) = tuple

    for {
      _ <- right.set(Some(rightFork))
      _ <- left.set(Some(leftFork))
    } yield ()
  }

  def setupTable(size: Int): ZIO[Any, Nothing, Seats] = {
    def makeFork(i: Int): STM[Nothing, TRef[Option[Forks]]] = TRef.make[Option[Forks]](Some(Forks(i)))

    (for {
      allForks0 <- STM.foreach((0 to size).toList) { i => makeFork(i) }
      allForks = allForks0 ++ List(allForks0.head)
      placements = (allForks zip allForks.drop(1)).map { case (l, r) => Placement(l, r) }
    } yield Seats(placements.toVector)).commit
  }

  def eat(philosophers: Int, seats: Seats): ZIO[Console, Nothing, Unit] = {
    val placement = seats.seats(philosophers)

    val left = placement.left
    val right = placement.right

    (for {
      forks <- takeForks(left, right).commit
      _ <- putStrLn(s"Philosopher $philosophers eating...")
      _ <- putForks(left, right)(forks).commit
      _ <- putStrLn(s"Philosopher $philosophers done eating.")
    } yield ())
      .fold(_ => ExitCode.success, _ => ExitCode.failure)
  }

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    val count = 10

    def eaters(table: Seats): Iterable[ZIO[Console, Nothing, Unit]] =
      (0 to count).map { i => eat(i, table) }

    (for {
      table <- setupTable(count)
      fiber <- ZIO.forkAll(eaters(table))
      _ <- fiber.join
      _ <- putStrLn("All philosophers have eaten.")
    } yield ())
      .fold(_ => ExitCode.success, _ => ExitCode.failure)
  }
}
