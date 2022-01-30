package MyPilotProjects.ZIO

import zio._

object ComputePi extends App {

  import zio.random._
  import zio.console._
  import zio.clock._
  import zio.duration._
  import zio.stm._

  case class PiState(inside: Long, total: Long)

  def estimatedPi(inside: Long, total: Long): Double = (inside.toDouble / total.toDouble) * 4.0

  def insideCircle(x: Double, y: Double): Int = if (math.sqrt(x * x + y * y) <= 1.0) 1 else 0

  val randomPoint: ZIO[Random, Nothing, (Double, Double)] = nextDouble zip nextDouble

  def updateOne(ref: Ref[PiState]): ZIO[Random, Nothing, Unit] = for {
      tuple <- randomPoint
      (x, y) = tuple
      inside = insideCircle(x, y)
      _ <- ref.update(state => PiState(state.inside + inside, state.total + 1))
    } yield ()

  def printEstimate(ref: Ref[PiState]): ZIO[Console, Nothing, Unit] = for {
      state <- ref.get
      _ <- putStrLn(s"${estimatedPi(state.inside, state.total)}")
        .fold(_ => ExitCode.success, _ => ExitCode.failure)
    } yield ()

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = (for {
    ref <- Ref.make(PiState(0L, 0L))
    worker = updateOne(ref).forever
    workers = List.fill(4)(worker)
    fiber <- ZIO.forkAll(workers)
    printerFiber <- (printEstimate(ref) *> ZIO.sleep(1000.millis)).forever.fork
    _ <- putStrLn("Press any key to terminate...")
    _ <- getStrLn *> (fiber zip printerFiber).interrupt
  } yield ())
    .fold(_ => ExitCode.success, _ => ExitCode.failure)
}
