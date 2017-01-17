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

    def v(x: String) = lookupVarName(x)

    def prop(x: String) = lookupProposition(x)

    println("#### Block 1 tests")
    println

    //  TODO: Would "Proposition" be more appropriate than "P_ Proposition"?
    val p_A = prop("A")
    val p_B = prop("B")
    val p_C = prop("C")

    val propNull =
      new P_FormulaProposition(null)
    //  a misapplication
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

    val var_x = v("x")
    val var_y = v("y")
    val var_z = v("z")

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

    println
    println("#### Block 1a tests: Stackless substitution")

    import substitution.substitute._
    import substitution.hasFreeOccurrence._
    import substitution.variables._

    val func_f = lookupFunction("f")
    val pred_P = lookupPredicate("P")
    val app_leaf = P_TermApplication(func_f, P_Applicand(Array(v_x, v_y, v_y, v_x)))

    var cur_app = app_leaf
    for (i <- 1 to 6) {
      cur_app = P_TermApplication(func_f, P_Applicand(Array(cur_app, cur_app, cur_app)))
    }
    val f9999 = P_FormulaApplication(pred_P, P_Applicand(Array(cur_app, cur_app, cur_app)))
    println
    println(f9999.hasFreeOccurrence(var_z))
    println(f9999.hasFreeOccurrence(var_y))
    println
    println(cur_app.variables())

    cur_app = app_leaf
    for (i <- 1 to 1000000) {
      cur_app = P_TermApplication(func_f, P_Applicand(Array(v_x, v_x, v_x, cur_app, v_x)))
    }
    println
    println(f9999.hasFreeOccurrence(var_z))
    println(f9999.hasFreeOccurrence(var_y))
    println
    println(cur_app.variables())
    //    cur_app.substitution(v_x, v_y)

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

    psf("( A \\/ PA )").d
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
    val ff04 = psf("P(0) ∧ ∀x.(P(x) → P(S(x))) → ∀x.P(x)")
    ff04.d

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
    println("------")
    val sequent9031 = inferenceRuleAxiom(ff04)
    sequent9031.d
    val sequent9032 = inferenceRuleWeakenL(sequent9031, ff01)
    sequent9032.d
    val sequent9033 = inferenceRuleContractL(sequent9032, ff01, ff04)
    sequent9033.d

    println
    println("#### Block 4a tests: hasFreeOccurrence")
    println

    import substitution.checkOccurs._

    val fblk4a01 = pf("P(f(g(x))) ∨ Q(h(g(y)))")
    fblk4a01.d
    fblk4a01 match {
      case a: P_Formula =>
        println(a.hasFreeOccurrence(var_x))
        println(a.hasFreeOccurrence(var_y))
        println(a.hasFreeOccurrence(var_z))
      case _ =>
        println("fblk4a01 fails!")
    }

    val sequent4a01 = inferenceRuleAxiom(fblk4a01)
    sequent4a01.d
    val sequent4a02 = inferenceRuleUnivR(sequent4a01, fblk4a01, (var_y, var_x))
    sequent4a02.d
    val sequent4a03 = inferenceRuleUnivR(sequent4a01, fblk4a01, (var_y, var_y))
    sequent4a03.d
    val sequent4a04 = inferenceRuleUnivR(sequent4a01, fblk4a01, (var_y, var_z))
    sequent4a04.d

    println
    println("#### Block 4a tests: Equality axioms")

    val ffEq = psf("Eq")

    println
    val ff101 = pf("∀x∀y.(x=y  → f(x)=f(y))")
    val sequent9101 = inferenceRuleAxiom(ff101)
    val sequent9102 = inferenceRuleWeakenL(sequent9101, ffEq)
    val sequent9103 = inferenceRuleContractL(sequent9102, ffEq, ff101)
    sequent9103.d

    println
    val ff102 = pf("a=b  → f(a)=f(b)")
    val sequent9104 = inferenceRuleAxiom(ff102)
    sequent9104.d
    val sequent9105 = inferenceRuleUnivL(sequent9104, pf("a=y  → f(a)=f(y)"),
      (pt("b"), v("y")))
    sequent9105.d
    val sequent9106 = inferenceRuleUnivL(sequent9105, pf("∀y.(x=y  → f(x)=f(y))"),
      (pt("a"), v("x")))
    sequent9106.d

    println
    val sequent9107 = inferenceRuleCut((sequent9103, sequent9106), ff101)
    sequent9107.d

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

    //  ---------------
    //  ∀xA ⊢ ∀yA[x/y]

    val thm1RUniv = inferenceRuleRool1RUniv(pf("A(x,y)"), pf("A(y,y)"),
        (v("x"), v("y")))
    thm1RUniv.d

    //  ------------
    //  ∃xAx ⊢ ∃yAy

    val thm1RExist = inferenceRuleRool1RExist(pf("A(x)"), pf("A(y)"),
        (v("x"), v("y")))
    thm1RExist.d

    val szz001 = inferenceRuleAxiom(pf("B"))
    val szz002 = inferenceRuleWeakenL(szz001, psf("Zz"))
    val szz003 = inferenceRuleContractL(szz002, psf("Zz"), pf("B"))
    val szz1   = inferenceRuleWeakenL(szz003, pf("A"))
    szz1.d
    val szz004 = inferenceRuleWeakenL(szz1, pf("Gamma"))
    val szz2   = inferenceRuleWeakenR(szz004, pf("Delta"))
    szz2.d

    //  Γ, A ⊢ B, Δ
    //  ----------------
    //  Γ, C∧A ⊢ C∧B, Δ

    val thmXA02L = inferenceRuleRool2L(szz1, pf("A"), pf("B"), pf("C"))
    thmXA02L.d
    val thmXB02L = inferenceRuleRool2L(szz2, pf("A"), pf("B"), pf("C"))
    thmXB02L.d

    //  Γ, A ⊢ B, Δ
    //  ----------------
    //  Γ, A∧C ⊢ B∧C, Δ

    val thmXA02R = inferenceRuleRool2R(szz1, pf("A"), pf("B"), pf("C"))
    thmXA02R.d
    val thmXB02R = inferenceRuleRool2R(szz2, pf("A"), pf("B"), pf("C"))
    thmXB02R.d

    //  Γ, A ⊢ B, Δ
    //  ----------------
    //  Γ, C∨A ⊢ C∨B, Δ

    val thmXA03L = inferenceRuleRool3L(szz1, pf("A"), pf("B"), pf("C"))
    thmXA03L.d
    val thmXB03L = inferenceRuleRool3L(szz2, pf("A"), pf("B"), pf("C"))
    thmXB03L.d

    //  Γ, A ⊢ B, Δ
    //  ----------------
    //  Γ, A∨C ⊢ B∨C, Δ

    val thmXA03R = inferenceRuleRool3R(szz1, pf("A"), pf("B"), pf("C"))
    thmXA03R.d
    val thmXB03R = inferenceRuleRool3R(szz2, pf("A"), pf("B"), pf("C"))
    thmXB03R.d

    //  Γ, A ⊢ B, Δ
    //  ------------------
    //  Γ, C → A ⊢ C → B, Δ

    val thmXA04 = inferenceRuleXRool4(szz1, pf("A"), pf("B"), pf("C"))
    thmXA04.d
    val thmXB04 = inferenceRuleXRool4(szz2, pf("A"), pf("B"), pf("C"))
    thmXB04.d

    //  Γ, A ⊢ B, Δ
    //  ------------------
    //  Γ, B → C ⊢ A → C, Δ

    val thmYA04 = inferenceRuleYRool4(szz1, pf("A"), pf("B"), pf("C"))
    thmYA04.d
    val thmYB04 = inferenceRuleYRool4(szz2, pf("A"), pf("B"), pf("C"))
    thmYB04.d

    //  Γ, A ⊢ B, Δ
    //  --------------
    //  Γ, ¬B ⊢ ¬A, Δ

    val thmXA05 = inferenceRuleXRool5(szz1, pf("A"), pf("B"))
    thmXA05.d
    val thmXB05 = inferenceRuleXRool5(szz2, pf("A"), pf("B"))
    thmXB05.d

    //  Γ, A ⊢ B, Δ
    //  ----------------
    //  Γ, ∀xA ⊢ ∀yB, Δ

    val thmXA06 = inferenceRuleXRool6(szz1, pf("A"), pf("B"),
      (v("x"), v("y")))
    thmXA06.d
    val thmXB06 = inferenceRuleXRool6(szz2, pf("A(x)"), pf("B(y)"),
      (v("x"), v("y")))
    thmXB06.d

    //  ----------------
    //  A → B ⊢ ∀xA → ∀yB

println("->->->doodles")
    val thm06 = inferenceRuleRool6(pf("A(x)"), pf("B(y)"), pf("A(x)"), pf("B(y)"),
        (v("x"), v("y")))
    thm06.d

    //  ----------------
    //  A → B ⊢ ∃xA → ∃yB
/*
    val thm07 = inferenceRuleRool7(pf("A(z)"), pf("B(z)"), pf("A(x)"), pf("B(y)"))
    thm07.d
*/
    println(pf("P(f(x),g(h(y),z))").toString)

    println
    println("#### Block 6 tests: Substitution")

    println()
//    val z301 = pf("P(x) ∨ ∀x.P(x) ∨ Q(y, x)").asInstanceOf[P_Formula]
//    val z301 = pf("P(x) ∨ Q").asInstanceOf[P_Formula]
//    val z301 = pf("∀x.P(x)").asInstanceOf[P_Formula]
//    val z301 = pf("∀v.¬¬∀w.¬∀y.∀z.P(x)").asInstanceOf[P_Formula]
//    val z301 = pf("∀v.¬¬∀x.¬∀y.∀z.P(x)").asInstanceOf[P_Formula]
    val z301 = pf("P(x) ∨ ∀x.P(x) ∨ ∀z.P(z) ∨ Q(y, x)").asInstanceOf[P_Formula]
//    val z301 = pf("P(x) ∨ ∀x.P(x) ∨ ∀z.P(z) ∨ ∀z.P(x) ∨ Q(y, x)").asInstanceOf[P_Formula]
    z301.d
//    val (z302, z302Error) = z301.substitute(pt("0").asInstanceOf[P_Term], var_x)
    val (z302, z302Error) = z301.substitute(pt("f(y, z)").asInstanceOf[P_Term], var_x)
    z302.d
    z302Error match {
      case Some(error) =>
         println("Error!")
      case None =>
    }

    println()
    val z201 = inferenceRuleAxiom(pf("P(0)"))
    z201.d
    val z202 = inferenceRuleUnivL(z201, pf("P(x)"),
      (pt("0"), v("x")))
    z202.d

    println()
    val z203 = inferenceRuleAxiom(pf("P(0) ∨ ∀x.P(x)"))
    z203.d
    val z204 = inferenceRuleUnivL(z203, pf("P(x) ∨ ∀x.P(x)"),
      (pt("0"), v("x")))
    z204.d

    println()
    val z205 = inferenceRuleAxiom(pf("P(0) ∨ ∀x.P(0)"))
    z205.d
    val z206 = inferenceRuleUnivL(z205, pf("P(x) ∨ ∀x.P(x)"),
      (pt("0"), v("x")))
    z206.d

    println
    println("#### Block 7 tests: Quantification")

    println()
    val z003 = inferenceRuleAxiom(pf("P(g(h(u,v),y))"))
    z003.d
    val t001 = pt("h(u,v)")
    t001.d
    val z904 = inferenceRuleUnivL(z003, pf("P(g(h(u,v),y))"),
        (pt("h(u,v)"), v("x")))
    z904.d
    val z004 = inferenceRuleUnivL(z003, pf("P(g(x,y))"),
        (pt("h(u,v)"), v("x")))
    z004.d

    println()
    val z005 = inferenceRuleAxiom(pf("P(0)"))
    z005.d
    val z006 = inferenceRuleUnivL(z005, pf("P(x)"),
        (pt("0"), v("x")))
    z006.d

    println()
    val f101 = pf("P(f(a, g(h(b, z), h(d, f(y, e), v), g(0, a))), f(b))")
    val z101 = inferenceRuleAxiom(f101)
    z101.d
    val f102 = pf("P(f(a, g(h(b, z), h(d, f(y, x), v), g(0, a))), f(b))")
    val t102 = pt("e")
    val z102 = inferenceRuleUnivL(z101, f102,
      (t102, v("x")))
    z102.d

    println()
    val z007 = inferenceRuleAxiom(pf("P(x)"))
    z007.d
    val z008 = inferenceRuleUnivR(z007, pf("P(x)"),
        (v("x"), v("y")))
    z008.d

    println()
    z101.d
    val z103 = inferenceRuleUnivR(z101, f101,
      (v("y"), v("x")))
    z103.d

    println()
    val z001 = inferenceRuleAxiom(pf("P(x)"))
    z001.d
    val z002 = inferenceRuleExistL(z001, pf("P(x)"),
        (v("x"), v("y")))
    z002.d

    println()
    val z011 = inferenceRuleAxiom(pf("P(g(h(u,v),y))"))
    z011.d
    val z012 = inferenceRuleExistR(z011, pf("P(g(x,y))"),
        (pt("h(u,v)"), v("x")))
    z012.d

    println()
    val z013 = inferenceRuleAxiom(pf("P(0)"))
    z013.d
    val z014 = inferenceRuleExistR(z013, pf("P(y)"),
        (pt("0"), v("y")))
    z014.d

    println()
    val z021 = inferenceRuleAxiom(pf("P(f(x))"))
    z021.d
    val z022 = inferenceRuleExistR(z021, pf("P(y)"),
        (pt("f(x)"), v("y")))
    z022.d

    println()
    val z023 = inferenceRuleAxiom(pf("P(f(x))"))
    z023.d
    val z024 = inferenceRuleExistR(z023, pf("P(f(y))"),
        (pt("x"), v("y")))
    z024.d

    println()
    val z025 = inferenceRuleAxiom(pf("P(f(x+z))"))
    z025.d
    val z026 = inferenceRuleExistR(z025, pf("P(f(y+z))"),
        (pt("x"), v("y")))
    z026.d

    println()
    val z015 = inferenceRuleAxiom(pf("P(x)"))
    z015.d
    val z016 = inferenceRuleExistR(z015, pf("P(y)"),
        (pt("x"), v("y")))
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

    println
    println("#### Block 8 test: Addition is associative")
    println

    val  thEq = psf("Eq")

    val  fEq1 = pf("∀x.(x=x)")
    val thEq1 = inferenceRuleAxiomFromTheory(thEq, fEq1)
    val  fEq2 = pf("∀x∀y∀z.(x=y ∧ y=z → x=z)")
    val thEq2 = inferenceRuleAxiomFromTheory(thEq, fEq2)

    val  thPA = psf("PA")

    val  fPA1 = pf("∀x.(0≠S(x))")
    val thPA1 = inferenceRuleAxiomFromTheory(thPA, fPA1)
    val  fPA2 = pf("∀x∀y.(S(x)=S(y) → x=y)")
    val thPA2 = inferenceRuleAxiomFromTheory(thPA, fPA2)

    val  fPA3 = pf("∀x.(x+0 = x)")
    val thPA3 = inferenceRuleAxiomFromTheory(thPA, fPA3)
    val  fPA4 = pf("∀x∀y.(x+S(y) = S(x+y))")
    val thPA4 = inferenceRuleAxiomFromTheory(thPA, fPA4)

    val  fPA5 = pf("∀x.(x×0 = 0)")
    val thPA5 = inferenceRuleAxiomFromTheory(thPA, fPA5)
    val  fPA6 = pf("∀x∀y.(x×S(y) = x×y+x)")
    val thPA6 = inferenceRuleAxiomFromTheory(thPA, fPA6)

    thPA1.d
    thPA2.d
    thPA3.d
    thPA4.d
    thPA5.d
    thPA6.d

    println()
    val th0001 = inferenceRuleAxiom(pf("∀x.(x + S(0) = S(x))"))
    th0001.d

    println()
    println("  (3)")
    val th0002 = inferenceRuleAxiom(pf("x + S(0) = S(x + 0)"))
    th0002.d
    val th0003 = inferenceRuleUnivL(th0002, pf("x + S(y) = S(x + y)"),
      (pt("0"), var_y))
    th0003.d
    val th0004 = inferenceRuleUnivL(th0003, pf("∀y.(x + S(y) = S(x + y))"),
      (pt("x"), var_x))
    th0004.d
    val th0005 = inferenceRuleCut((thPA4, th0004), pf("∀x∀y.(x + S(y) = S(x + y))"))
    th0005.d
    val th0006 = inferenceRuleUnivR(th0005, pf("x + S(0) = S(x + 0)"),
      (var_x, var_x))
    th0006.d

    println()
    println("  (4)")
    val ft0101 = pf("∀x∀y.(x=y → S(x)=S(y))")
    val th0101 = inferenceRuleAxiomFromTheory(thEq, ft0101)
    th0101.d
    val th0102 = inferenceRuleUnivL(th0101, pf("x=y → S(x)=S(y)"),
      (pt("x+0"), var_x))
    th0102.d


    println()
    val th1102 = inferenceRuleAxiom(pf("z+0=z → S(z+0)=S(z)"))
    th1102.d
    val th1103 = inferenceRuleUnivL(th1102, pf("z+0=y → S(z+0)=S(y)"),
      (pt("z"), var_y))
    th1103.d
    val th1104 = inferenceRuleUnivL(th1103, pf("∀y.(x=y → S(x)=S(y))"),
      (pt("z+0"), var_x))
    th1104.d
    val th1105 = inferenceRuleCut((th0101, th1104), pf("∀x∀y.(x=y → S(x)=S(y))"))
    th1105.d
    val th1106 = inferenceRuleUnivR(th1105, pf("x+0=x → S(x+0)=S(x)"),
      (var_z, var_x))
    th1106.d

    println()
    val th1107 = inferenceRuleAxiom(pf("x+0=x → S(x+0)=S(x)"))
    th1107.d
    val th1108 = inferenceRuleUnivL(th1107, pf("x+0=x → S(x+0)=S(x)"),
      (pt("x"), var_x))
    th1108.d
    val th1109 = inferenceRuleCut((th1106, th1108), pf("∀x.(x+0=x → S(x+0)=S(x))"))
    th1109.d
    val th1110 = inferenceRuleRool1b(th1109, (pf("x+0 = x"), pf("S(x+0)=S(x)")))
    th1110.d

    println()
    val th0106 = inferenceRuleAxiom(pf("x+0 = x"))
    th0106.d
    val th0107 = inferenceRuleUnivL(th0106, pf("x+0 = x"),
      (pt("x"), var_x))
    th0107.d
    val th0108 = inferenceRuleCut((thPA3, th0107), pf("∀x.(x+0 = x)"))
    th0108.d
    val th0109 = inferenceRuleCut((th0108, th1110), pf("x+0 = x"))
    th0109.d

  }

}
