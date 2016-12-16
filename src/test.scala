/**
  * Created by EdSnow on 7/16/2016.
  */


import p_term._
import p_formula._

import foundationalInferenceRule._


object test {

  def main(args: Array[String]) {

    //  Set the stage.
    //  Primitive functions are invoked which are "under the
    //  floor" and vulnerable to misapplication if directly
    //  user invoked.

    println("#### Block 1 tests")
    println

    //  TODO: Would "Proposition" be more appropriate than "P_ Proposition"?
    val p_A = new P_Proposition("A")
    val p_B = new P_Proposition("B")
    val p_C = new P_Proposition("C")

    val propNull =
                new P_FormulaProposition(null)      //  a misapplication
    //  <None>
    val propA = new P_FormulaProposition(p_A)
    //  A
    val propB = new P_FormulaProposition(p_B)
    //  B
    val propC = new P_FormulaProposition(p_C)
    //  C
//  println(propNull.toString)                      //  would provoke a null exception
    println(propA.toString)

    val form001 = new P_FormulaConjunction(propA, propB)
    //  (A ∧ B)
    println(form001.toString)

    val form002 = new P_FormulaConjunction(form001, propC)
    //  ((A ∧ B) ∧ C)
    println(form002.toString)

    val var_x = new P_VarName("x")
    val var_y = new P_VarName("y")
    val var_z = new P_VarName("z")

    val v_x = new P_TermVariable(var_x)
    //  x
    println(v_x)

    val v_y = new P_TermVariable(var_y)
    //  y
    println(v_y)

    val v_z = new P_TermVariable(var_z)
    //  z
    println(v_z)

    import untangle._
    val ut = new Untangler[Int]
    ut.goforit

    //  Finger exercises

    println
    println("#### Block 2 tests")
    println

    val s000 = inferenceRuleAxiom(null)
    //  <Nothing>
    println(s000.toString)
    val s001 = inferenceRuleAxiom(propA)
    //  A ⊢ A
    println(s001.toString)
    val s002 = inferenceRuleAxiom(propB)
    //  B ⊢ B
    println(s002.toString)

    println
    val s003 = inferenceRuleAndL1(s001, (propA, propB))
    //  (A ∧ B) ⊢ A
    println(s003.toString)
    val s004 = inferenceRuleOrR1(s001, (propA, propB))
    //  A ⊢ (A ∨ B)
    println(s004.toString)
    val s005 = inferenceRuleAndL2(s002, (propA, propB))
    //  (A ∧ B) ⊢ B
    println(s005.toString)
    val s006 = inferenceRuleOrR2(s002, (propA, propB))
    //  B ⊢ (A ∨ B)
    println(s006.toString)

    println
    val sx02 = inferenceRuleWeakenL(s001, propB)
    //  A, B ⊢ A
    println(sx02.toString)
    val sx03 = inferenceRuleWeakenR(s001, propB)
    //  A ⊢ B, A
    println(sx03.toString)
    val sx04 = inferenceRuleNotL(sx03, propB)
    //  A, ¬B ⊢ A
    println(sx04.toString)
    val sx05 = inferenceRuleNotR(sx02, propB)
    //  A ⊢ ¬B, A
    println(sx05.toString)
    val sx06 = inferenceRuleNotR(s001, propA)
    //  ⊢ ¬A, A
    println(sx06.toString)

    println
    s001.d
    s002.d
    s003.d
    s004.d
    s005.d
    s006.d

    println
    sx02.d
    sx03.d
    sx04.d
    sx05.d
    sx06.d

    import parseStuff._

    println
    println("#### Block 3 tests: Check out the parser.")
    println

    psf("( A \\/ B ))").d
    //  Parse error

    psf("( A # B ))").d
    //  Parse error

    psf("( A \\/ B )").d
    //  (A ∨ B)

    psf("( A \\/ PA )").d    //  TODO: Make this an error!
    //  Parse error

    psf("(A \\/ B /\\ C -> D \\/ ~E \\/ F)").d
    //  ((A ∨ (B ∧ C)) → ((D ∨ ¬E) ∨ F))

    psf("∀x.(x+0=x)").d
    //  ∀x(x+0=x)

    import p_axiomatization._

    val ffx1 = pf("P")
    ffx1.d
    val ffx2 = psf("P")
    ffx2.d
    println("------")
    val ffx3 = pf("PA")
    ffx3.d
    val ff01 = psf("PA")
    ff01.d
    val ff02 = psf("∀x∀y.(S(x)=S(y) → x=y)")
    ff02.d
    val ff03 = psf("∀x∀y.(S(x)=S(y) → x=z)")
    ff03.d

    println
    println("#### Block 4 tests: Axiomatizations")
    println

    val sequent9001a1 = inferenceRuleAxiom(ff01)
    sequent9001a1.d
    println("------")
    val sequent9011 = inferenceRuleAxiom(ff02)
    sequent9011.d
    val sequent9012 = inferenceRuleWeakenL(sequent9011, ff01)
    sequent9012.d
    val sequent9013 = inferenceRuleContractL(sequent9012, ff01, ff02)
    sequent9013.d
    println("------")
    val sequent9021 = inferenceRuleAxiom(ff03)
    sequent9021.d
    val sequent9022 = inferenceRuleWeakenL(sequent9021, ff01)
    sequent9022.d
    val sequent9023 = inferenceRuleContractL(sequent9022, ff01, ff03)
    sequent9023.d

    println
    println("#### Block 5 tests: Derived inference rules")
    println

    import derivedInferenceRule._

    //  Γ, A ⊢ B, Δ
    //  ------------
    //  Γ ⊢ A → B, Δ

    val sequent001a1 = inferenceRuleAxiom(pf("C"))
    val sequent001a2 = inferenceRuleWeakenL(sequent001a1, pf("A"))
    val sequent001a3 = inferenceRuleWeakenR(sequent001a2, pf("B"))
    val thm01a = inferenceRuleRool1a(sequent001a3, (pf("A"), pf("B")))
    thm01a.d

    //  Γ ⊢ A → B, Δ
    //  ------------
    //  Γ, A ⊢ B, Δ

    val sequent001b = inferenceRuleAxiom(pf("A -> B"))
    val thm01b = inferenceRuleRool1b(sequent001b, (pf("A"), pf("B")))
    thm01b.d

    //  ----------
    //  A∧B ⊢ B∧A

    val thm01c = inferenceRuleRool1c(pf("A"), pf("B"))
    thm01c.d

    //  ----------
    //  A∨B ⊢ B∨A

    val thm01d = inferenceRuleRool1d(pf("A"), pf("B"))
    thm01d.d

    //  -------------------
    //  (A∧B)∧C ⊢ A∧(B∧C)

    val thm01e = inferenceRuleRool1e(pf("A"), pf("B"), pf("C"))
    thm01e.d

    //  -------------------
    //  (A∨B)∨C ⊢ A∨(B∨C)

    val thm01f = inferenceRuleRool1f(pf("A"), pf("B"), pf("C"))
    thm01f.d

    //  -------------------
    //  A∧(B∧C) ⊢ (A∧B)∧C

    val thm01g = inferenceRuleRool1g(pf("A"), pf("B"), pf("C"))
    thm01g.d

    //  -------------------
    //  A∨(B∨C) ⊢ (A∨B)∨C

    val thm01h = inferenceRuleRool1h(pf("A"), pf("B"), pf("C"))
    thm01h.d

    //  -------------
    //  ∀xAx ⊢ ∀yAy

    val thm1RUniv = inferenceRuleRool1RUniv(pf("A(z)"), pf("A(x)"), pf("A(y)"))
    thm1RUniv.d

    //  ------------
    //  ∃xAx ⊢ ∃yAy

    val thm1RExist = inferenceRuleRool1RExist(pf("A(z)"), pf("A(x)"), pf("A(y)"))
    thm1RExist.d

    //  ----------------
    //  A → B ⊢ A∧C → B∧C

    val thm02 = inferenceRuleRool2(pf("A"), pf("B"), pf("C"))
    thm02.d

    //  ----------------
    //  A → B ⊢ A∨C → B∨C

    val thm03 = inferenceRuleRool3(pf("A"), pf("B"), pf("C"))
    thm03.d

    //  ---------------------
    //  A → B ⊢ (C → A) → (C → B)

    val thm04 = inferenceRuleRool4(pf("A"), pf("B"), pf("C"))
    thm04.d

    //  ---------------------
    //  A → B ⊢ ¬B → ¬A

    val thm05 = inferenceRuleRool5(pf("A"), pf("B"))
    thm05.d

    //  ----------------
    //  A → B ⊢ ∀xA → ∀yB

    val thm06 = inferenceRuleRool6(pf("A(z)"), pf("B(z)"), pf("A(x)"), pf("B(y)"))
    thm06.d

    //  ----------------
    //  A → B ⊢ ∃xA → ∃yB

    val thm07 = inferenceRuleRool7(pf("A(z)"), pf("B(z)"), pf("A(x)"), pf("B(y)"))
    thm07.d

    println(pf("P(f(x),g(h(y),z))").toString)

    println()
    val z003 = inferenceRuleAxiom(pf("P(g(h(u,v),y))"))
    z003.d
    val z004 = inferenceRuleUnivL(z003, (pf("P(g(h(u,v),y))"), pf("P(g(x,y))")))
    z004.d

    println()
    val z005 = inferenceRuleAxiom(pf("P(0)"))
    z005.d
    val z006 = inferenceRuleUnivL(z005, (pf("P(0)"), pf("P(x)")))
    z006.d

    println()
    val z007 = inferenceRuleAxiom(pf("P(x)"))
    z007.d
    val z008 = inferenceRuleUnivR(z007, (pf("P(x)"), pf("P(y)")))
    z008.d

    println()
    val z001 = inferenceRuleAxiom(pf("P(x)"))
    z001.d
    val z002 = inferenceRuleExistL(z001, (pf("P(x)"), pf("P(y)")))
    z002.d

    println()
    val z011 = inferenceRuleAxiom(pf("P(g(h(u,v),y))"))
    z011.d
    val z012 = inferenceRuleExistR(z011, (pf("P(g(h(u,v),y))"), pf("P(g(x,y))")))
    z012.d

    println()
    val z013 = inferenceRuleAxiom(pf("P(0)"))
    z013.d
    val z014 = inferenceRuleExistR(z013, (pf("P(0)"), pf("P(y)")))
    z014.d

    println()
    val z021 = inferenceRuleAxiom(pf("P(f(x))"))
    z021.d
    val z022 = inferenceRuleExistR(z021, (pf("P(f(x))"), pf("P(y)")))
    z022.d

    println()
    val z023 = inferenceRuleAxiom(pf("P(f(x))"))
    z023.d
    val z024 = inferenceRuleExistR(z023, (pf("P(f(x))"), pf("P(f(y))")))
    z024.d

    println()
    val z025 = inferenceRuleAxiom(pf("P(f(x+z))"))
    z025.d
    val z026 = inferenceRuleExistR(z025, (pf("P(f(x+z))"), pf("P(f(y+z))")))
    z026.d

    println()
    val z015 = inferenceRuleAxiom(pf("P(x)"))
    z015.d
    val z016 = inferenceRuleExistR(z015, (pf("P(x)"), pf("P(y)")))
    z016.d

    //============

    println()
    val f100x = pf("x1 + /S 0 = /S x1")
    println(f100x.toString)


    val z2000 = inferenceRuleAxiom(f100x)
    z2000.d

    val fm011 = pf("(∃x)P")
    println(fm011.toString)

    val fm001 = pf("∃x.P(x)")
    println(fm001.toString)

  }

}
