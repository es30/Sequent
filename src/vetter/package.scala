/**
  * Created by EdSnow on 12/11/2016.
  */

package object vetter {


  import error.Error


  abstract class ErrorArgument extends Error

  class   Error_null  extends ErrorArgument
  class   Error_empty(val error: Option[Error]) extends ErrorArgument
  class   Error_list(val errorList: List[(Error, Int)]) extends ErrorArgument
  class   Error_atype(val error: Option[Error]) extends ErrorArgument


  type AType[T] = (Option[T], Option[Error])


}
