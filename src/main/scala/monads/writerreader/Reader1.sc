import cats.data.Reader

//case class Reader[A, B](run: A => B)






// Used for Dependency Injection


// Example use cases:
// 1) Database Row => entity / domain object
// 2) Env variables => Settings



























trait Row {
  def getAt[A](idx: Int): A
}

case class Book(title: String, authors: Vector[String])



















class TestRow(f: Int => Any) extends Row {
  def getAt[A](idx: Int): A = f(idx).asInstanceOf[A] //sic
}

















val bookReader: Reader[Row, Book] = Reader { row =>
  val title: String = row.getAt(1)
  val authors: Vector[String] = row.getAt(2)
  Book(title, authors)
}














val bookRow = new TestRow({
  case 0 => 10
  case 1 => "Scala with Cats"
  case 2 => Vector("Noel Welsh", "Dave Gurnell")
})

val book: Book = bookReader.run(bookRow)