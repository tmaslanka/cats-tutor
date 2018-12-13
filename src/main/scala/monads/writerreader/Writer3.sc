
def slowly[A](body: => A) = try body finally Thread.sleep(100)

def factorial(n: Int): Int = {
  val v = slowly {
    if (n == 0) 1 else n * factorial(n - 1)
  }
  println(s"fact $n is $v")
  v
}


import cats.data.Writer

import scala.concurrent.{Await, Future => async}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

val computations = Seq(async(factorial(10)), async(factorial(10)))
Await.result(async.sequence(computations), 5.seconds)













































import cats.syntax.writer._
import cats.syntax.applicative._
import cats.instances.vector._

type Log = Vector[String]
type Logged[A] = Writer[Log, A]

def factorialW(n: Int): Logged[Int] = {
  for {
    v <- slowly {
      if (n == 0) 1.pure[Logged]
      else factorialW(n - 1).map(_ * n)
    }
    _ <- Vector(s"fact $n is $v").tell
  } yield v
}


val fs = Seq(async(factorialW(10).run), async(factorialW(10).run))
val Seq((logA, a), (logB, b)) = Await.result(async.sequence(fs), 5.seconds)