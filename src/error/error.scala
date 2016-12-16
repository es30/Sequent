package error

/**
  * Created by EdSnow on 12/14/2016.
  */


abstract class Error()

//  class ErrorSingleton extends Error()

class ErrorList(val errors: Seq[Error]) extends Error{}

// ..............

class IntrinsicError(val inherited: Option[InheritedError]) extends Error()

class InheritedError() extends Error()

class Error_composite(
    val intrinsic: Seq[Error],              //  Error => ErrorCedent?
    val extrinsic: List[(Error, Int)]       //  Error => ErrorArgument?
) extends Error
