package MyPilotProjects.ZIO

import akka.stream.scaladsl.OrElse
import zio._

object ZIOTour {

  object ZIOTypes {
    type SampleTask[+A] = ZIO[Any, Throwable, A]
    implicitly[SampleTask[Int] =:= Task[Int]]

    type SampleUIO[+A] = ZIO[Any, Nothing, A] // the NOTHING represents that this effect can't fail
    implicitly[SampleUIO[Int] =:= UIO[Int]]

    type SampleRIO[-R, +A] = ZIO[R, Throwable, A]
    implicitly[SampleRIO[Int, String] =:= RIO[Int, String]]

    type SampleIO[+E, +A] = ZIO[Any, E, A]
    implicitly[SampleIO[Int, String] =:= IO[Int, String]]

    type SampleURIO[-R, +A] = ZIO[R, Nothing, A]
    implicitly[SampleURIO[Int, String] =:= URIO[Int, String]]
  }

  def main(args: Array[String]): Unit = {

  }
}

object HelloWorld extends App {
  import zio.console._

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = // the output of the system is int so we have to map output into an int
    putStrLn("hello world!").fold(_ => ExitCode.success, _ => ExitCode.failure)
}

object SequencePrint extends App {
  import zio.console._

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    putStrLn("hello").zipLeft(putStrLn("world"))
      .fold(_ => ExitCode.success, _ => ExitCode.failure)
}

object ErrorRecovery extends App {
  import zio.console._

  lazy val failed = putStrLn("Is failing...") *> ZIO.fail("Failed.")

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    (failed as ExitCode.failure) orElse ZIO.succeed(ExitCode.failure)
}

object PromptName extends App {
  import zio.console._

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    putStrLn("enter your name:").*>(getStrLn flatMap { name => putStrLn(s"your name is $name")})
        .fold(_ => ExitCode.success, _ => ExitCode.failure)
}

object PromptNameFor extends App {
  import zio.console._

  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] = (for {
    _ <- putStrLn("enter your name:")
    name <- getStrLn
    _ <- putStrLn(s"your name is $name")
  } yield ())
    .fold(_ => ExitCode.success, _ => ExitCode.failure)
}

/**
 * Playground
 */
object TestZIO1 extends App {
  import zio.console._

  def run(args: List[String]): URIO[Console, ExitCode] =
    appLogic.fold(_ => ExitCode.success, _ => ExitCode.failure)

  val appLogic = for {
    _ <- putStrLn("hello")
    _ <- putStrLn("world")
    _ <- putStrLn("hi hi")
  } yield ()
}

object TestZIO2 {
  import zio.console._

  def main(args: Array[String]) = {
    val task = Task(println("hi"))
    val runtime = Runtime.default
    runtime.unsafeRun(task)
  }
}