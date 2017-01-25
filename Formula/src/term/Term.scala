package term

/**
  * Created by EdSnow on 12/8/2016.
  */

import error._


class Term (
    val error: Option[Error]
) {

  def termString = "<Nothing>"

  def d() = println(this.toString)

  override
  def toString = {
    val errorString =
      error match {
        case Some(_)  => "Bad! : "
        case None     => "     : "
      }
    errorString + termString
  }

}
