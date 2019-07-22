package sonac.github.io.germanwords.common

sealed trait Error

case class ExternalServiceFailure(message: String) extends Error
