
// case class Writer[Log, Value](run: (Log, Value))



import cats.data.Writer

Writer(Vector("log1", "log2"), 2)
























type Log = Vector[String]
type Logged[A] = Writer[Log, A]

{
  import cats.syntax.applicative._
  import cats.instances.vector._
  123.pure[Logged]  // Monoid[Log]
}


import cats.syntax.writer._

val a: Writer[Log, Unit] = Vector("log1", "log2", "log3").tell

val b: Writer[Log, Int] = 123.writer(Vector("log1", "log2", "log3"))

val value: Int = b.value
val log: Log = b.written

val (l: Log, v: Int) = b.run