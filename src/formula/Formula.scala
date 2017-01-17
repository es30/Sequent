/**
  * Created by EdSnow on 11/17/2016.
  */


package formula

import error._


class SFormula (val error: Option[Error]) {

  def d() = println(this.toString)

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


class Formula(error: Option[Error]) extends SFormula(error)
