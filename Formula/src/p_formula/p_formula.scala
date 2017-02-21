/**
  * Created by EdSnow on 8/20/2016.
  */


package p_formula


trait POrAName


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

  final override
  def equals(obj: Any): Boolean =
    equals(None, obj).result

  def equals(
      continuation: Option[() => TailRec[Boolean]],
      obj: Any
    ): TailRec[Boolean]

  final override
  def hashCode(): Int =
    hashCode(None, 0).result

  def hashCode(
      continuation: Option[Int => TailRec[Int]],
      hash: Int
    ): TailRec[Int]

}


abstract class P_SOnlyFormula(
    error: Option[Error]
) extends SFormula(error) with P_SFormula


abstract class P_Formula(
    error: Option[Error] = None
) extends Formula(error) with P_SFormula


object P_Formula {


  val pOrANameMap =  collection.mutable.Map[String, POrAName]()

  //  This is only called from Java code (The ANTLR-generated parser in particular),
  //  hence the use of null as a return value.
  def lookupProposition(s: String): P_Proposition = {
    val entry = pOrANameMap.getOrElseUpdate(s, new P_Proposition(s))
    entry match {
      case p: P_Proposition => p
      case _ => null
    }
  }

  val predicateMap =  collection.mutable.Map[String, P_Predicate]()
  def lookupPredicate(s: String) = predicateMap.getOrElseUpdate(s, new P_Predicate(s))
  def lookupPredicate(s: String, p: P_Predicate) = predicateMap.getOrElseUpdate(s, p)


  implicit def P_SFormulaWrapper(pf: P_SFormula): SFormula =
    pf match {
      case f: P_Formula => f
      case f: P_SOnlyFormula => f
    }

  val noError: Option[Error] = None

  def equals_continue(
      continuation: Option[() => TailRec[Boolean]],
      retval: Boolean
    ): TailRec[Boolean] =
    if (retval)
      continuation match {
        case Some(cont) =>
          tailcall(cont())
        case None =>
          done(true)
      }
    else
      done(false)

  def hashCode_continue(
      continuation: Option[Int => TailRec[Int]],
      retval: Int
    ): TailRec[Int] =
    continuation match {
      case Some(cont) =>
        tailcall(cont(retval))
      case None =>
        done(retval)
    }

}


import P_Formula._


case class P_FormulaProposition (p: P_Proposition, override val error: Option[Error] = None)
    extends P_Formula {

  override
  def formulaString() = p.toString

  final
  def equals(
      continuation: Option[() => TailRec[Boolean]],
      obj: Any
    ): TailRec[Boolean] = {
    val retval =
      obj match {
        case f: P_FormulaProposition =>
          f.p == p
        case _ =>
          false
      }
    tailcall(equals_continue(continuation, retval))
  }

  final
  def hashCode(
      continuation: Option[Int => TailRec[Int]],
      hash: Int
    ): TailRec[Int] =
    tailcall(hashCode_continue(continuation, 3 * hash + p.hashCode()))

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
      continuation: Option[() => TailRec[Boolean]],
      obj: Any
    ): TailRec[Boolean] = {
    val retval =
      obj match {
        case f: P_FormulaApplication if f.p == p =>
          f.a == a
        case _ =>
          false
      }
    tailcall(equals_continue(continuation, retval))
  }

  final
  def hashCode(
      continuation: Option[Int => TailRec[Int]],
      hash: Int
    ): TailRec[Int] =
    tailcall(hashCode_continue(continuation, 5 * hash + p.hashCode() + a.hashCode()))

}


case class P_FormulaNegation (f1: P_Formula, override val error: Option[Error] = None)
  extends P_Formula {

  override
  def formulaString() = "¬" + f1.formulaString

  final
  def equals(
      continuation: Option[() => TailRec[Boolean]],
      obj: Any
    ): TailRec[Boolean] =
    obj match {
      case f: P_FormulaNegation =>
        tailcall(f1.equals(continuation, f.f1))
      case _ =>
        tailcall(equals_continue(continuation, false))
    }

  final
  def hashCode(
      continuation: Option[Int => TailRec[Int]],
      hash: Int
    ): TailRec[Int] =
    tailcall(f1.hashCode(continuation, 7 * hash))

}


object DyadicKind extends Enumeration {
  type Kind = Value
  val Conjunction, Disjunction, Implication, Biconditional = Value
}


trait P_FormulaDyadic {
  val kind: DyadicKind.Kind
  val f1: P_Formula
  val f2: P_Formula
  val connective: String

  def replace(f1: P_Formula, f2: P_Formula): P_Formula

  lazy val bumper = " " + connective + " "

  def formulaString() =
    "(" + f1.formulaString + bumper + f2.formulaString + ")"

  final
  def equals(
      continuation: Option[() => TailRec[Boolean]],
      obj: Any
    ): TailRec[Boolean] =
    obj match {
      case f: P_FormulaDyadic if kind == f.kind =>
        def cont1: () => TailRec[Boolean] =
          () => tailcall(f2.equals(continuation, f.f2))

        tailcall(f1.equals(Some(cont1), f.f1))
      case _ =>
        tailcall(equals_continue(continuation, false))
    }

  final
  def hashCode(
      continuation: Option[Int => TailRec[Int]],
      hash: Int
    ): TailRec[Int] = {
    def cont1: Int => TailRec[Int] =
      (hash: Int) => tailcall(f2.hashCode(continuation, hash))

    tailcall(f1.hashCode(Some(cont1), hash + kind.hashCode()))
  }

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
  type Kind = Value
  val Universal, Existential = Value
}


trait P_FormulaQuantification {
  val kind: QuantificationKind.Kind
  val v: P_VarName
  val f1: P_Formula
  val quantifier: String

  def replace(f1: P_Formula): P_Formula

  def formulaString() = "(" + quantifier + v.toString + "." + f1.formulaString + ")"

  final
  def equals(
      continuation: Option[() => TailRec[Boolean]],
      obj: Any
    ): TailRec[Boolean] =
    obj match {
      case f: P_FormulaQuantification if f.v == v =>
        tailcall(f1.equals(continuation, f.f1))
      case _ =>
        tailcall(equals_continue(continuation, false))
    }

  final
  def hashCode(
      continuation: Option[Int => TailRec[Int]],
      hash: Int
    ): TailRec[Int] =
    tailcall(f1.hashCode(continuation, 11 * hash + kind.hashCode() + v.hashCode()))

}


object P_FormulaQuantification {

  def construct(
      q: QuantificationKind.Kind,
      v: P_VarName,
      f1: P_Formula,
      error: Option[Error] = None
    ): P_Formula =
    q match {
      case QuantificationKind.Universal =>
        P_FormulaQuantificationUniversal(v, f1 , error)
      case QuantificationKind.Existential =>
        P_FormulaQuantificationExistential(v, f1 , error)
    }

}


case class P_FormulaQuantificationUniversal (
    v: P_VarName,
    f1: P_Formula,
    override val error: Option[Error] = None
  ) extends P_Formula with P_FormulaQuantification {
  override def formulaString() = super[P_FormulaQuantification].formulaString
  val kind = QuantificationKind.Universal
  val quantifier = "∀"
  def replace(f1: P_Formula) = copy(f1 = f1)
}

case class P_FormulaQuantificationExistential (
    v: P_VarName,
    f1: P_Formula,
    override val error: Option[Error] = None
  ) extends P_Formula with P_FormulaQuantification {
  override def formulaString() = super[P_FormulaQuantification].formulaString
  val kind = QuantificationKind.Existential
  val quantifier = "∃"
  def replace(f1: P_Formula) = copy(f1 = f1)
}
