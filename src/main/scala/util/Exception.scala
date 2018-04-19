package util

class AnalysesException(_msg: String, _cause: Exception = null) extends Exception(_msg, _cause)

case class UserCreationException(_msg: String) extends AnalysesException(_msg)