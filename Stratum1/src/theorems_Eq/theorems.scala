package theorems_Eq

import p_term.P_TermVariable
import foundationalInferenceRule._
import derivedInferenceRule.{inferenceRuleAxiomFromTheory, inferenceRuleRool1b}

import parseStuff._

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
