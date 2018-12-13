import cats.data.Reader


case class Env(vars: Map[String, String])

case class Settings(enabled: Boolean, featureName: String)


def settingsReader(featureName: String): Reader[Env, Settings] =  Reader { env =>
  val vars = env.vars
  val enabled = vars.get(s"${featureName.toUpperCase}_ENABLED").exists(_.toBoolean)
  Settings(enabled, featureName)
}

val settingsListReader = for {
  f1 <- settingsReader("f1")
  f2 <- settingsReader("f2")
} yield Vector(f1, f2)


settingsListReader.run(Env(Map(
  "F1_ENABLED" -> "true",
  "F2_ENABLED" -> "false")))