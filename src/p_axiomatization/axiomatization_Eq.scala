package p_axiomatization

import scala.annotation.tailrec

import p_term._
import formula.Formula
import p_formula.{P_Formula, P_FormulaApplication}
import parseStuff._
import deconstructP_Formula.FormulaDeconstructor
import p_axiomatization.prefixWalker.P_Formula_prefixWalker

/**
  * Created by EdSnow on 1/5/2017.
  */

object axiomatization_Eq extends P_Axiomatization("Eq") {

  val template_1 = pf("∀x.(x=x)")
  val template_2 = pf("∀x∀y∀z.(x=y ∧ y=z → x=z)")

  val template_3 = pf("x=y → (Z → Z)")
  //  schema:  x=y → (ϕ(x) → ϕ(y))
  val template_4 = pf("x=y → Z=Z")
  //  schema:  x=y → f(x)=f(y)

  val var_x = lookupVarName("x")
  val var_y = lookupVarName("y")
  val v_y   = P_TermVariable(lookupVarName("x"))

  class PrefixVisitor extends Visitor[List[P_VarName]] {
    def collect(v: P_VarName, vars: List[P_VarName]): List[P_VarName] =
      v :: vars
  }

  def checkApplicands(
      closureVars: List[P_VarName],
      applicand1: P_Applicand,
      applicand2: P_Applicand
    ): Boolean = {
    val terms1 = applicand1.terms
    val terms2 = applicand2.terms

    @tailrec def checkTails(
        termIndex: Int,
        vars: List[P_VarName],
        substFound: Boolean
      ): Boolean =
      if (termIndex >= terms1.length)
        substFound &&
        (vars match {
          case List(`var_y`) => true
          case _ => false
        })
      else
        (terms1(termIndex), terms2(termIndex)) match {
          case (term1: P_TermVariable, term2: P_TermVariable) =>
            val v1 = term1.v
            val v2 = term2.v
            v1 != var_y &&
            v1 == vars.head && {
              val varsTail = vars.tail
              !(varsTail contains v1) && {
                if (v2 == var_y)
                  !substFound &&
                  checkTails(termIndex + 1, varsTail, true)
                else
                  v1 == v2 &&
                  checkTails(termIndex + 1, varsTail, false)
              }
            }
          case _ =>
            false
        }

    terms1.length == terms2.length &&
    checkTails(0, closureVars, false)
  }

  val equalityPredicateSchemaMatcher = {
    val deconstructor = template_3.deconstructor
    AxiomTemplate(
      (f: Formula) =>
        f match {
          case candidate: P_Formula =>
            val (prefixVars, stripped_formula) =
              candidate.walkPrefix[List[P_VarName]](new PrefixVisitor, Nil)
            val closureVars = prefixVars.reverse
            deconstructor(stripped_formula) match {
              case Some((formulas, _)) =>
                (formulas(0), formulas(1)) match {
                  case (aa1: P_FormulaApplication, aa2: P_FormulaApplication)
                      if aa1.p == aa2.p =>
                    checkApplicands(closureVars, aa1.a, aa2.a)
              case _ =>
                    false
                }
              case None =>
                false
            }
          case _ =>
            false
        }
    )
  }

  val equalityFunctionSchemaMatcher = {
    val deconstructor = template_4.deconstructor
    AxiomTemplate(
      (f: Formula) =>
        f match {
          case candidate: P_Formula =>
            val (prefixVars, stripped_formula) =
              candidate.walkPrefix[List[P_VarName]](new PrefixVisitor, Nil)
            val closureVars = prefixVars.reverse
            deconstructor(stripped_formula) match {
              case Some((_, terms)) =>
                (terms(0), terms(1)) match {
                  case (ta1: P_TermApplication, ta2: P_TermApplication)
                      if ta1.f == ta2.f =>
                    checkApplicands(closureVars, ta1.a, ta2.a)
                  case _ =>
                    false
                }
              case None =>
                false
            }
          case _ =>
            false
        }
    )
  }

  def subsumes(f: Formula) =
    AxiomTemplateList(Seq(

      AxiomTemplate(template_1),
      AxiomTemplate(template_2),

      equalityPredicateSchemaMatcher,
      equalityFunctionSchemaMatcher

    )).matchAxiom(f)

}
