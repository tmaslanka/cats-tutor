package monads.writerreader

object Reader4 extends App {
  //Ex

  import cats.data.Reader

  type UserId = Int
  type UserName = String
  type Password = String

  case class DB(
                 usernames: Map[UserId, UserName],
                 passwords: Map[UserName, Password]
               )


  type DBReader[A] = Reader[DB, A]



  def findUsername(userId: UserId): DBReader[Option[UserName]] = Reader { db =>
    db.usernames.get(userId)
  }

  def checkPassword(userName: UserName, password: Password): DBReader[Boolean] = Reader { db =>
    db.passwords.get(userName).contains(password)
  }













  import cats.syntax.applicative._ //for false.pure[DBReader]

  def checkLogin(userId: UserId, password: Password): DBReader[Boolean] = {
    findUsername(userId).flatMap { maybeUser =>
      maybeUser
        .map(checkPassword(_, password))
        .getOrElse(false.pure[DBReader])
    }
  }













  def checkLogin2(userId: UserId, password: Password): DBReader[Boolean] = {
    for {
      maybeUser <- findUsername(userId)
      isCorrect <- maybeUser.map { userName =>
                    checkPassword(userName, password)
                  }.getOrElse {
                    false.pure[DBReader]
                  }
    } yield isCorrect
  }







  val users = Map(
    1 -> "dade",
    2 -> "kate",
    3 -> "margo"
  )

  val passwords = Map(
    "dade"  -> "zerocool",
    "kate"  -> "acidburn",
    "margo" -> "secret"
  )

  val db = DB(users, passwords)
  private val reader: DBReader[Boolean] = checkLogin(1, "zerocool")
  println(reader)
  println(s"checkLogin(1, zerocool).run(db): ${reader.run(db)}")
  println(s"checkLogin(4, davinci).run(db) ${checkLogin(4, "davinci").run(db)}")
}
