/**
  * Created by EdSnow on 7/22/2016.
  */


package p_sequent

import p_formula._

import p_sequent.Cedent.CedentTemplate


sealed trait CTElement
case class CTESFormula(f: P_SFormula) extends CTElement
case class CTECedent(c: Cedent) extends CTElement


case class Cedent (
    fSet: Set[P_SFormula]
) {

  //  A dummy parameter is introduced in the constructor below
  //  in order to distinguish it from the primary constructor
  //  after type erasure.

  def this(template: CedentTemplate, dummy: Any) =
    this(template.foldLeft(Set[P_SFormula]()){
        (v: Set[P_SFormula], u: CTElement) =>
          u match {
            case x: CTESFormula => v +  x.f
            case x: CTECedent   => v ++ x.c.fSet
          }
      }
    )

  def contains[T <: P_SFormula](f: T) : Option[(Cedent,T)] =
    if (fSet contains f)
      None
    else Some(this, f)

  def --(that : P_Formula) : (Cedent, Option[(Cedent,P_Formula)]) =
  {
    val error = contains(that)
    (new Cedent(fSet - that), error)
  }

  def -(that : P_Formula) : (CTECedent, Option[(Cedent,P_Formula)]) =
  {
    val (c, f) = this -- that
    (CTECedent(c), f)
  }


  override
  def toString: String =
    if (fSet.isEmpty)
      ""
    else
      fSet.map(_.formulaString).reduceLeft(
        (x: String, y: String) => x + ", " + y.toString)

}


object Cedent {

  type CedentTemplate = Set[CTElement]

}


// ====================================================================


import error._
import sequent._


case class P_Sequent (
    ante: Cedent,
    succ: Cedent,
    override val error: Option[Error]
) extends Sequent(error) {

  override
  def sequentString: String =
  {
    val anteString = ante.toString
    val succString = succ.toString
    val  leftPad = if (anteString.length == 0) "" else " "
    val rightPad = if (succString.length == 0) "" else " "
    ante.toString + leftPad + "⊢" + rightPad + succ.toString
  }

}
