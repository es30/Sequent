/**
  * Created by EdSnow on 7/28/2016.
  */


package object error {

  type OError = Option[Error]

  def collect(list: Seq[OError]): OError = {
    val errorList: Seq[Error] = list.flatten
    errorList.length match {
      case 0 => None
      case 1 => Some(errorList.head)
      case _ => Some(new ErrorList(errorList))
    }
  }

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


object distinctionFactory {

  type Distinction = Long

  class DistinctionExhaustionException extends Exception

  private var count: Distinction = 0

  def apply() = this.synchronized {
    val current = count
    if (count < Int.MaxValue) {
      count += 1
      current
    }
    else
      throw new DistinctionExhaustionException
  }

}
