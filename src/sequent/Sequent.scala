package sequent

/**
  * Created by EdSnow on 11/18/2016.
  */


import error._


class Sequent (
    val error: Option[Error]
) {

  def sequentString = "<Nothing>"

  override
  def toString = {
    val errorString =
      error match {
        case Some(_) => "Bad! : "
        case None    => "     : "
      }
    errorString + sequentString
  }

  def d() = println(this.toString)

}
