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



def findUsername(userId: UserId): DBReader[Option[UserName]] = ???

def checkPassword(userName: UserName, password: Password): DBReader[Boolean] = ???

def checkLogin(userId: UserId, password: Password): DBReader[Boolean] = ???























































def findUsername1(userId: UserId): DBReader[Option[UserName]] = Reader { db =>
  db.usernames.get(userId)
}











def checkPassword1(userName: UserName, password: Password): DBReader[Boolean] = Reader { db =>
  db.passwords.get(userName).contains(password)
}











import cats.syntax.applicative._


def checkLogin1(userId: UserId, password: Password): DBReader[Boolean] = {
  findUsername1(userId).flatMap { maybeUser =>
    maybeUser
      .map(checkPassword1(_, password))
      .getOrElse(false.pure[DBReader])
  }
}




def checkLogin2(userId: UserId, password: Password): DBReader[Boolean] = {
  for {
    maybeUser <- findUsername(userId)
    isCorrect <- maybeUser.map { userName =>
                    checkPassword1(userName, password)
                  }.getOrElse {
                    false.pure[DBReader]
                  }
  } yield isCorrect
}
































def checkLogin3(userId: UserId, password: Password): DBReader[Boolean] = {
  for {
    maybeUser <- findUsername(userId)
    isCorrect <- (for {
      userName <- maybeUser
    } yield checkPassword1(userName, password)).getOrElse {
      false.pure[DBReader]
    }
  } yield isCorrect
}
