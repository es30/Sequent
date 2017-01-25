package error

/**
  * Created by EdSnow on 12/14/2016.
  */


abstract class Error()


object Error {

  type OError = Option[Error]

  def collect(list: Seq[OError]): OError = {
    val errorList: Seq[Error] = list.flatten
    errorList.length match {
      case 0 => None
      case 1 => Some(errorList.head)
      case _ => Some(new ErrorList(errorList))
    }
  }

}


//  class ErrorSingleton extends Error()

class ErrorList(val errors: Seq[Error]) extends Error{}

// ..............

class IntrinsicError(val inherited: Option[InheritedError]) extends Error()

class InheritedError() extends Error()


case class Error_composite(
    intrinsic: Seq[Error],                  //  Error => ErrorCedent?
    extrinsic: List[(Error, Int)]           //  Error => ErrorArgument?
) extends Error


object Error_composite {

  def errorListCombined(
      intrinsic: Seq[Error],
      extrinsic: List[(Error, Int)]
    ): Option[Error_composite] = {
    if (intrinsic.isEmpty && extrinsic.isEmpty)
      None
    else
      Some(new Error_composite(intrinsic, extrinsic))
  }

  def errorListCombined(
      intrinsic: Option[Error],
      extrinsic: List[(Error, Int)]
    ): Option[Error_composite] = {
    val errorListInt = intrinsic match {
      case Some(a) => Seq(a)
      case None => Nil
    }
    errorListCombined(errorListInt, extrinsic)
  }

}
