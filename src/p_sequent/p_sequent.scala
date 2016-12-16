/**
  * Created by EdSnow on 7/22/2016.
  */


package p_sequent


import p_term._
import p_formula._


sealed trait CTElement
case class CTESFormula(f: P_SFormula) extends CTElement
case class CTECedent(c: Cedent) extends CTElement


class Cedent (
    val fSet: Set[P_SFormula]
) {

  //  A dummy parameter is introduced in the constructor below
  //  in order to distinguish it from the primary constructor
  //  after type erasure.

  def this(template: CedentTemplate, dummy: Any) =
    this(template.foldLeft(Set[P_SFormula]())
      (
        (v: Set[P_SFormula], u: CTElement) => {
          u match {
            case x: CTESFormula => v +  x.f
            case x: CTECedent  => v ++ x.c.fSet
          }
        }
      )
    )

  def contains[T <: P_SFormula](f : T) : Option[(Cedent,T)] =
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

  def checkOccurs(v: P_VarName) : Option[(Cedent,P_TermVariable)] =
    None

  override
  def toString: String =
    if (fSet.isEmpty)
      ""
    else
      fSet.map(_.formulaString).reduceLeft(
        (x: String, y: String) => x + ", " + y.toString)

}


// ====================================================================


import error._
import sequent._


class P_Sequent (
    val ante: Cedent,
    val succ: Cedent,
    error: Option[Error]
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
