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


import scala.util.control.TailCalls._

import error._
import p_term._
import formula._


trait P_SFormula {

  def formulaString(): String

  def equals(
      stack: List[(P_Formula, P_Formula)],
      obj: Any
    ): TailRec[Boolean]

  final override
  def equals(obj: Any): Boolean =
    equals(Nil, obj).result

  def hashCode(
      stack: List[P_Formula],
      hash: Int
    ): TailRec[Int]

  final override
  def hashCode(): Int =
    hashCode(Nil, 0).result

}


abstract class P_SOnlyFormula(
    error: Option[Error]
) extends SFormula(error) with P_SFormula


abstract class P_Formula(
    error: Option[Error] = None
) extends Formula(error) with P_SFormula


case class P_FormulaProposition (p: P_Proposition, override val error: Option[Error] = None)
    extends P_Formula {

  override
  def formulaString() = p.toString

  final
  def equals(
      stack: List[(P_Formula, P_Formula)],
      obj: Any
    ): TailRec[Boolean] = {
    val retval =
      obj match {
        case f: P_FormulaProposition =>
          f.p == p
        case _ =>
          false
      }
    tailcall(equals_continue(stack, retval))
  }

  final
  def hashCode(
      stack: List[P_Formula],
      hash: Int
    ): TailRec[Int] =
    tailcall(hashCode_continue(stack, 3 * hash + p.hashCode()))

}


case class P_FormulaApplication (p: P_Predicate, a: P_Applicand, override val error: Option[Error] = None)
    extends P_Formula {

  override
  def formulaString() =
    p match {
      case predicate: DisplayString =>
        predicate.displayString(a)
      case _ =>
        p.toString + a.toString
    }

  final
  def equals(
      stack: List[(P_Formula, P_Formula)],
      obj: Any
    ): TailRec[Boolean] = {
    val retval =
      obj match {
        case f: P_FormulaApplication if f.p == p =>
          f.a == a
        case _ =>
          false
      }
    tailcall(equals_continue(stack, retval))
  }

  final
  def hashCode(
      stack: List[P_Formula],
      hash: Int
    ): TailRec[Int] =
    tailcall(hashCode_continue(stack, 5 * hash + p.hashCode() + a.hashCode()))

}


case class P_FormulaNegation (f1: P_Formula, override val error: Option[Error] = None)
  extends P_Formula {

  override
  def formulaString() = "¬" + f1.formulaString

  final
  def equals(
      stack: List[(P_Formula, P_Formula)],
      obj: Any
    ): TailRec[Boolean] =
    obj match {
      case f: P_FormulaNegation =>
        tailcall(f1.equals(stack, f.f1))
      case _ =>
        tailcall(equals_continue(stack, false))
    }

  final
  def hashCode(
      stack: List[P_Formula],
      hash: Int
    ): TailRec[Int] =
    tailcall(f1.hashCode(stack, 7 * hash))

}


object DyadicKind extends Enumeration {
  type Kind = Value
  val Conjunction, Disjunction, Implication, Biconditional = Value
}


case class P_FormulaConjunction (f1: P_Formula, f2: P_Formula, override val error: Option[Error] = None)
    extends P_Formula with P_FormulaDyadic {
  override def formulaString() = super[P_FormulaDyadic].formulaString
  val kind = DyadicKind.Conjunction
  val connective = "∧"
  def replace(f1: P_Formula, f2: P_Formula) = copy(f1 = f1, f2 = f2)
}

case class P_FormulaDisjunction (f1: P_Formula, f2: P_Formula, override val error: Option[Error] = None)
    extends P_Formula with P_FormulaDyadic {
  override def formulaString() = super[P_FormulaDyadic].formulaString
  val kind = DyadicKind.Disjunction
  val connective = "∨"
  def replace(f1: P_Formula, f2: P_Formula) = copy(f1 = f1, f2 = f2)
}

case class P_FormulaImplication (f1: P_Formula, f2: P_Formula, override val error: Option[Error] = None)
    extends P_Formula with P_FormulaDyadic {
  override def formulaString() = super[P_FormulaDyadic].formulaString
  val kind = DyadicKind.Implication
  val connective = "→"
  def replace(f1: P_Formula, f2: P_Formula) = copy(f1 = f1, f2 = f2)
}

case class P_FormulaBiconditional (f1: P_Formula, f2: P_Formula, override val error: Option[Error] = None)
    extends P_Formula with P_FormulaDyadic {
  override def formulaString() = super[P_FormulaDyadic].formulaString
  val kind = DyadicKind.Biconditional
  val connective = "→"
  def replace(f1: P_Formula, f2: P_Formula) = copy(f1 = f1, f2 = f2)
}


object QuantificationKind extends Enumeration {
//  type Kind = Value
//  val Existential, Universal = Value
  type Kind = (Value, String)
  val Existential = (Value, "∃")
  val Universal   = (Value, "∀")
}

case class P_FormulaQuantification(kind: QuantificationKind.Kind,
    v: P_VarName, f1: P_Formula,
    override val error: Option[Error] = None
  ) extends P_Formula {

  override
  def formulaString() = "(" + kind._2 + v.toString + "." + f1.formulaString + ")"

  final
  def equals(
      stack: List[(P_Formula, P_Formula)],
      obj: Any
    ): TailRec[Boolean] =
    obj match {
      case f: P_FormulaQuantification if f.v == v =>
        tailcall(f1.equals(stack, f.f1))
      case _ =>
        tailcall(equals_continue(stack, false))
    }

  final
  def hashCode(
      stack: List[P_Formula],
      hash: Int
    ): TailRec[Int] =
    tailcall(f1.hashCode(stack, 11 * hash + kind.hashCode() + v.hashCode()))

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
