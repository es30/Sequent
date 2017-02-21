package axiomatization_Eq

import scala.annotation.tailrec

import p_term.{P_Applicand, P_TermApplication, P_TermVariable, P_VarName}
import formula.Formula
import p_formula.{P_Formula, P_FormulaApplication}
import p_axiomatization.{AxiomTemplate, AxiomTemplateList, P_Axiomatization, Visitor}
import p_axiomatization.prefixWalker.P_Formula_prefixWalker
import p_axiomatization.deconstruct.FormulaDeconstructor

import parseStuff._
import foundationalInferenceRule._
import derivedInferenceRule.{inferenceRuleAxiomFromTheory, inferenceRuleRool1b}

/**
  * Created by EdSnow on 1/5/2017.
  */

object subsumer extends P_Axiomatization("Eq") {

  private val template_1 = pf("∀x.(x=x)")

  private val template_2 = pf("x=y → (Z → Z)")
  //  schema:  x=y → (ϕ(x) → ϕ(y))
  private val template_3 = pf("x=y → Z=Z")
  //  schema:  x=y → f(x)=f(y)

  private val var_x = v("x")
  private val var_y = v("y")

  private val v_y = P_TermVariable(var_x)

  private class PrefixVisitor extends Visitor[List[P_VarName]] {
    def collect(v: P_VarName, vars: List[P_VarName]): List[P_VarName] =
      v :: vars
  }

  private def checkApplicands(
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
                    checkTails(termIndex + 1, varsTail, substFound)
              }
            }
          case _ =>
            false
        }

    terms1.length == terms2.length &&
      checkTails(0, closureVars, false)
  }

  private val equalityPredicateSchemaMatcher = {
    val deconstructor = template_2.deconstructor
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
                  case (aa1: P_FormulaApplication,
                  aa2: P_FormulaApplication) if aa1.p == aa2.p =>
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

  private val equalityFunctionSchemaMatcher = {
    val deconstructor = template_3.deconstructor
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
                  case (ta1: P_TermApplication,
                  ta2: P_TermApplication) if ta1.f == ta2.f =>
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

      equalityPredicateSchemaMatcher,
      equalityFunctionSchemaMatcher

    )).matchAxiom(f)

}


object theorems {

  private val var_v = v("v")
  private val var_x = v("x")
  private val var_y = v("y")
  private val var_z = v("z")

  private val v_x = P_TermVariable(var_x)
  private val v_y = P_TermVariable(var_y)
  private val v_z = P_TermVariable(var_z)

  val  thEq = psf("Eq")                             //  Eq

  //  Equality is reflexive, thEq1: ∀x(x=x)

  val thEq1 = inferenceRuleAxiomFromTheory(thEq, pf("∀x.(x=x)"))
                                                    //  Eq ⊢ ∀x(x=x)

  //  Equality is symmetric, thEq2: ∀x∀y(x=y → y=x)

  private val s001 = inferenceRuleAxiom(pf("x=y → (x=z → y=z)"))
                                                    //  x=y → (x=z → y=z) ⊢ x=y → (x=z → y=z)
  private val s002 = inferenceRuleUnivL(s001, pf("x=y → (x=z → y=z)"),
    (v_y, var_y))                                   //  ∀y(x=y → (x=z → y=z)) ⊢ x=y → (x=z → y=z)
  private val s003 = inferenceRuleUnivL(s002, pf("∀y.(x=y → (x=z → y=z))"),
    (v_z, var_z))                                   //  ∀z∀y(x=y → (x=z → y=z)) ⊢ x=y → (x=z → y=z)
  private val s004 = inferenceRuleUnivL(s003, pf("∀z∀y.(x=y → (x=z → y=z))"),
    (v_x, var_x))                                   //  ∀x∀z∀y(x=y → (x=z → y=z)) ⊢ x=y → (x=z → y=z)
  private val s005 = inferenceRuleWeakenL(s004, thEq)
                                                    //  Eq, ∀x∀z∀y(x=y → (x=z → y=z)) ⊢ x=y → (x=z → y=z)
  private val s006 = inferenceRuleContractL(s005, thEq,
    pf("∀x∀z∀y.(x=y → (x=z → y=z))"))               //  Eq ⊢ x=y → (x=z → y=z)
  private val s007 = inferenceRuleRool1b(s006, (pf("x=y"), pf("x=z → y=z")))
                                                    //  Eq, x=y ⊢ x=z → y=z
  private val s008 = inferenceRuleUnivR(s007, pf("x=z → y=z"),
    (var_z, var_z))                                 //  Eq, x=y ⊢ ∀z(x=z → y=z)

  private val s009 = inferenceRuleAxiom(pf("x=x → y=x"))
                                                    //  x=x → y=x ⊢ x=x → y=x
  private val s010 = inferenceRuleUnivL(s009, pf("x=z → y=z"),
    (v_x, var_z))                                   //  ∀z(x=z → y=z) ⊢ x=x → y=x
  private val s011 = inferenceRuleCut((s008, s010), pf("∀z.(x=z → y=z)"))
                                                    //  Eq, x=y ⊢ x=x → y=x
  private val s012 = inferenceRuleRool1b(s011, (pf("x=x"), pf("y=x")))
                                                    //  Eq, x=y, x=x ⊢ y=x
  private val s013 = inferenceRuleImplR(s012, (pf("x=y"), pf("y=x")))
                                                    //  Eq, x=x ⊢ x=y → y=x
  private val s014 = inferenceRuleUnivL(s013, pf("x=x"),
    (v_x, var_x))                                   //  Eq, ∀x(x=x) ⊢ x=y → y=x
  private val s015 = inferenceRuleContractL(s014, thEq, pf("∀x.(x=x)"))
                                                    //  Eq ⊢ x=y → y=x
  private val s016 = inferenceRuleUnivR(s015, pf("x=y → y=x"),
    (var_y, var_y))                                 //  Eq ⊢ ∀y(x=y → y=x)
  private val s017 = inferenceRuleUnivR(s016, pf("∀y.(x=y → y=x)"),
    (var_x, var_x))                                 //  Eq ⊢ ∀x∀y(x=y → y=x)
  val thEq2 = s017

  //  Equality is transitive, thEq3: ∀x∀y∀z(x=y ∧ y=z → x=z)

  private val s101 = inferenceRuleRool1b(s015, (pf("x=y"), pf("y=x")))
                                                    //  Eq, x=y ⊢ y=x

  private val s102 = inferenceRuleUnivR(s006, pf("v=y → (v=z → y=z)"),
    (var_x, var_v))                                 //  Eq ⊢ ∀v(v=y → (v=z → y=z))
  private val s103 = inferenceRuleUnivR(s102, pf("∀v.(v=x → (v=z → x=z))"),
    (var_y, var_x))                                 //  Eq ⊢ ∀x∀v(v=x → (v=z → x=z))

  private val s104 = inferenceRuleAxiom(pf("y=x → (y=z → x=z)"))
                                                    //  y=x → (y=z → x=z) ⊢ y=x → (y=z → x=z)
  private val s105 = inferenceRuleUnivL(s104, pf("v=x → (v=z → x=z)"),
    (v_y, var_v))                                   //  ∀v(v=x → (v=z → x=z)) ⊢ y=x → (y=z → x=z)
  private val s106 = inferenceRuleUnivL(s105, pf("∀v.(v=x → (v=z → x=z))"),
    (v_x, var_x))                                   //  ∀x∀v(v=x → (v=z → x=z)) ⊢ y=x → (y=z → x=z)
  private val s107 = inferenceRuleCut((s103, s106),
    pf("∀x∀v.(v=x → (v=z → x=z))"))               //  Eq ⊢ y=x → (y=z → x=z)
  private val s108 = inferenceRuleRool1b(s107, (pf("y=x"), pf("y=z → x=z")))
                                                    //  Eq, y=x ⊢ y=z → x=z
  private val s109 = inferenceRuleCut((s101, s108), pf("y=x"))
                                                    //  Eq, x=y ⊢ y=z → x=z
  private val s110 = inferenceRuleRool1b(s109, (pf("y=z"), pf("x=z")))
                                                    //  Eq, x=y, y=z ⊢ x=z
  private val s111 = inferenceRuleAndL1(s110, (pf("x=y"), pf("y=z")))
                                                    //  Eq, x=y ∧ y=z, y=z ⊢ x=z
  private val s112 = inferenceRuleAndL2(s111, (pf("x=y"), pf("y=z")))
                                                    //  Eq, x=y ∧ y=z, x=y ∧ y=z ⊢ x=z
                                                    //  =>  Eq, x=y ∧ y=z ⊢ x=z
  private val s113 = inferenceRuleImplR(s112, (pf("x=y ∧ y=z"), pf("x=z")))
                                                    //  Eq ⊢ x=y ∧ y=z → x=z
  private val s114 = inferenceRuleUnivR(s113, pf("x=y ∧ y=z → x=z"),
    (var_z, var_z))                                 //  Eq ⊢ ∀z(x=y ∧ y=z → x=z)
  private val s115 = inferenceRuleUnivR(s114, pf("∀z.(x=y ∧ y=z → x=z)"),
    (var_y, var_y))                                 //  Eq ⊢ ∀y∀z(x=y ∧ y=z → x=z)
  private val s116 = inferenceRuleUnivR(s115, pf("∀y∀z.(x=y ∧ y=z → x=z)"),
    (var_x, var_x))                                 //  Eq ⊢ ∀x∀y∀z(x=y ∧ y=z → x=z)
  val thEq3 = s116

}
