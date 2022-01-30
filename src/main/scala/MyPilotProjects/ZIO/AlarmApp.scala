package MyPilotProjects.ZIO

import zio._

import java.io.IOException
import java.util.concurrent.TimeUnit

object AlarmApp extends App {

  import zio.console._
  import zio.duration._

  def toDouble(string: String): Either[NumberFormatException, Double] =
    try Right(string.toDouble) catch { case e: NumberFormatException => Left(e) }

  lazy val getAlarmDuration: ZIO[Console, IOException, Duration] = {
    def parseDuration(input: String): Either[NumberFormatException, Duration] =
      toDouble(input).map(number => Duration((number * 1000.0).toLong, TimeUnit.MILLISECONDS))

    val fallback = putStrLn("number invalid!") *> getAlarmDuration

    for {
      _ <- putStrLn("enter alarm duration:")
      input <- getStrLn
      duration <- ZIO.fromEither(parseDuration(input)) orElse fallback
    } yield duration
  }

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = (
    for {
      duration <- getAlarmDuration
      fiber <- (putStr(".") *> ZIO.sleep(500.millis)).forever.fork
      _ <- ZIO.sleep(duration) *> putStrLn("Time to wakeup!") *> fiber.interrupt
    } yield ()).fold(_ => ExitCode.success, _ => ExitCode.failure)
}
