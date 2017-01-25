/**
  * Created by EdSnow on 12/11/2016.
  */

package object vetter {


  import error.Error


  abstract class ErrorArgument extends Error

  case class Error_null()  extends ErrorArgument
  case class Error_empty(error: Option[Error]) extends ErrorArgument
  case class Error_list(errorList: List[(Error, Int)]) extends ErrorArgument
  case class Error_atype(error: Option[Error]) extends ErrorArgument


  type AType[T] = (Option[T], Option[Error])


}
