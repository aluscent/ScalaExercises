package MyPilotProjects.ZIO

import zio.{App, ExitCode, Promise, Queue, Ref, Task, UIO, URIO, ZIO}

object ZIOActors extends App {

  import zio.console._
  import zio.stm._

  sealed trait Command
  case object ReadTemperature extends Command
  final case class AdjustTemperature(value: Double) extends Command

  type TemperatureActor = Command => Task[Double]

  def makeActor(initialTemp: Double): UIO[TemperatureActor] = {
    type Bundle = (Command, Promise[Nothing, Double])

    for {
      ref <- Ref.make(initialTemp)
      queue <- Queue.bounded[Bundle](100)
      _ <- queue.take.flatMap{
        case (ReadTemperature, promise) =>
          ref.get.flatMap(promise.succeed)
        case (AdjustTemperature(temperature), promise) =>
          ref.update(old => (old + temperature) / 2.0).flatMap(_ => promise.succeed(temperature))
      }.forever.fork
    } yield (c: Command) => Promise.make[Nothing, Double].flatMap(p => queue.offer(c -> p) *> p.await)
  }

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    val temperatures = (100 to 200).map(_.toDouble)

    (for {
      actor <- makeActor(135.5)
      _ <- ZIO.foreachPar(temperatures) { temp => actor(AdjustTemperature(temp)) }
      temp <- actor(ReadTemperature)
      _ <- putStrLn(s"Final temperature is $temp.")
    } yield ())
      .fold(_ => ExitCode.success, _ => ExitCode.failure)
  }
}
