/**
  * Created by EdSnow on 8/20/2016.
  */


package p_formula


class P_Proposition(val name: String) extends POrAName {

  override
  def toString = name

}


class P_Predicate(val name: String) {

  override
  def toString = name

}


// ====================================================================


import error._
import p_term._
import formula._


trait P_SFormula extends SFormula


abstract class P_Formula (
    error: Option[Error] = None
) extends Formula(error) with P_SFormula


case class P_FormulaProposition (val p: P_Proposition, override val error: Option[Error] = None)
    extends P_Formula {

  final override
  def equals(obj: Any) =
    obj match {
      case f: P_FormulaProposition => p == f.p
      case _ => false
    }

  final override
  def hashCode(): Int = p.hashCode()

  override
  def formulaString = p.toString

}


case class P_FormulaApplication (val p: P_Predicate, val a: P_Applicand, override val error: Option[Error] = None)
    extends P_Formula {

  final override
  def equals(obj: Any) =
    obj match {
      case f: P_FormulaApplication => p == f.p && a == f.a
      case _ => false
    }

  final override
  def hashCode(): Int = p.hashCode() + a.hashCode()

  override
  def formulaString = p.toString + a.toString

}


case class P_FormulaNegation (val f1: P_Formula, override val error: Option[Error] = None)
  extends P_Formula {

  final override
  def equals(obj: Any) =
    obj match {
      case f: P_FormulaNegation => f1 == f.f1
      case _ => false
    }

  final override
  def hashCode(): Int = 41 * f1.hashCode()

  override
  def formulaString = "¬" + f1.formulaString

}


object DyadicKind extends Enumeration {
  type Kind = Value
  val Conjunction, Disjunction, Implication, Biconditional = Value
}

abstract case class P_FormulaDyadic(kind: DyadicKind.Kind,
    f1: P_Formula, f2: P_Formula,
    connective: String,
    override val error: Option[Error] = None
  ) extends P_Formula {

  val bumper = " " + connective + " "

  final override
  def equals(obj: Any) =
    obj match {
      case f: P_FormulaDyadic =>
        (kind == f.kind) && (f1 == f.f1) && (f2 == f.f2)
      case _ => false
    }

  final override
  def hashCode(): Int =
    kind.hashCode() + f1.hashCode() + f2.hashCode()

  override
  def formulaString = "(" + f1.formulaString + bumper + f2.formulaString + ")"

}


class P_FormulaConjunction (f1: P_Formula, f2: P_Formula, error: Option[Error] = None)
    extends P_FormulaDyadic(DyadicKind.Conjunction, f1, f2, "∧", error)

class P_FormulaDisjunction (f1: P_Formula, f2: P_Formula, error: Option[Error] = None)
    extends P_FormulaDyadic(DyadicKind.Disjunction, f1, f2, "∨", error)

class P_FormulaImplication (f1: P_Formula, f2: P_Formula, error: Option[Error] = None)
    extends P_FormulaDyadic(DyadicKind.Implication, f1, f2, "→", error)

class P_FormulaBiconditional (f1: P_Formula, f2: P_Formula, error: Option[Error] = None)
    extends P_FormulaDyadic(DyadicKind.Biconditional, f1, f2, "↔", error)


object QuantificationKind extends Enumeration {
//  type Kind = Value
//  val Existential, Universal = Value
  type Kind = (Value, String)
  val Existential = (Value, "∃")
  val Universal   = (Value, "∀")
}

case class P_FormulaQuantification(val kind: QuantificationKind.Kind,
    val v: P_VarName, val f1: P_Formula,
    override val error: Option[Error] = None
  ) extends P_Formula {

  final override
  def equals(obj: Any) =
    obj match {
      case f: P_FormulaQuantification => v == f.v && f1 == f.f1
      case _ => false
    }

  final override
  def hashCode(): Int = v.hashCode() + f1.hashCode()

  override
  def formulaString = "(" + kind._2 + v.toString + "." + f1.formulaString + ")"

}

/*
class FormulaQuantificationExistential private(error: OError, v: TermVariable, f1: Formula)
  extends FormulaQuantification(error, QuantificationKind.Existential, v, f1, "∃") {

  def this(v: TermVariable, f1: Formula) = this(None, v, f1)

}


class FormulaQuantificationUniversal private(error: OError, v: TermVariable, f1: Formula)
  extends FormulaQuantification(error, QuantificationKind.Universal, v, f1, "∀") {

  def this(v: TermVariable, f1: Formula) = this(None, v, f1)

}
*/
