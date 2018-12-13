// Composing and Transforming

import cats.data.Writer
type Log = Vector[String]
type Logged[A] = Writer[Log, A]

import cats.syntax.writer._
import cats.syntax.applicative._
import cats.instances.vector._ //Monoid[Log]

val w = for {
  a <- 10.pure[Logged]
  _ <- Vector("a", "b", "c").tell
  b <- 32.writer(Vector("x", "y", "z"))
} yield a + b

w.run
























w.mapWritten(_.map(_.toUpperCase)).run


w.bimap(
  log => log.map(_.toUpperCase),
  v => v * 10
).run


w.mapBoth( (log, v) => (
  log.map(_ + "!"),
  v * 1000
)).run


w.reset.run


w.swap.run