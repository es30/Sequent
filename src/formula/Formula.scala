/**
  * Created by EdSnow on 11/17/2016.
  */


package formula

import error._


trait SFormula {

  def formulaString: String

  def d() = println(this.toString)

}


class Formula (
    val error: Option[Error]
) extends SFormula {

  def formulaString = "<Nothing>"

  override
  def toString = {
    val errorString =
      error match {
        case Some(_)  => "Bad! : "
        case None     => "     : "
      }
    errorString + formulaString
  }

}
