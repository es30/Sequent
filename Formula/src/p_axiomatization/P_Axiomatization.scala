/**
  * Created by EdSnow on 12/3/2016.
  */


package p_axiomatization


import error.Error
import formula._
import p_formula._
import p_formula.P_Formula._

import scala.util.control.TailCalls.{TailRec, tailcall}


abstract class P_Axiomatization(val name: String) extends POrAName {

  def register(s: String) = pOrANameMap.update(s, this)

  def subsumes(f: Formula): Boolean

  override
  def toString = name

}


object P_Axiomatization {

  //  This is only called from Java code (The ANTLR-generated parser in particular),
  //  hence the use of null as a return value.
  def lookupAxiomatization(s: String): P_Axiomatization = {
    val entry = pOrANameMap.get(s)
    entry match {
      case Some(a: P_Axiomatization) =>
        a
      case _ =>
        null
    }
  }

}


// ====================================================================


import scala.annotation.tailrec


case class P_SFormulaAxiomatization(a: P_Axiomatization, override val error: Option[Error] = None)
      extends P_SOnlyFormula(error) {

  def checkSubsumption(f: P_Formula): Option[(P_SFormulaAxiomatization, P_Formula)] =
    if (a.subsumes(f))
      None
    else
      Some((this, f))

  override
  def formulaString: String = a.toString

  override
  def toString = "     : " + formulaString

  final
  def equals(
      continuation: Option[() => TailRec[Boolean]],
      obj: Any
    ): TailRec[Boolean] = {
    val retval =
      obj match {
        case f: P_SFormulaAxiomatization =>
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
    tailcall(hashCode_continue(continuation, 17 * hash + + a.hashCode()))

}


case class AxiomTemplate(matcher: Formula => Boolean)


object AxiomTemplate {

  def apply(template: Formula): AxiomTemplate =
    AxiomTemplate(
      (candidate: Formula) =>
        template match {
          case t: P_Formula =>
            t == candidate
          case _ =>
            false
        }
    )

}


case class AxiomTemplateList(list: Seq[AxiomTemplate]) {

  def matchAxiom(f: Formula): Boolean =
    matchAxiom(f, list)

  @tailrec private def matchAxiom(f: Formula, axiomList: Seq[AxiomTemplate]): Boolean =
    axiomList match {
      case Nil =>
        false
      case head :: tail =>
        head.matcher(f) || matchAxiom(f, tail)
    }

}
