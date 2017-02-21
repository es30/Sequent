/**
  * Created by EdSnow on 7/16/2016.
  */


import p_term._
import p_term.P_Term.{lookupFunction, lookupVarName}
import p_formula._
import p_formula.P_Formula.{lookupPredicate, lookupProposition}

import parseStuff._
import substitution.hasFreeOccurrence._
import substitution.variables.P_Term_variables
import substitution.substitute._
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
      P_FormulaProposition(null)
    //  a misapplication
    //  <None>
    val propA = P_FormulaProposition(p_A)
    //  A
    val propB = P_FormulaProposition(p_B)
    //  B
    val propC = P_FormulaProposition(p_C)
    //  C
    //  println(propNull.toString)                  //  would provoke a null exception
    println(propA.toString)

    val form001 = P_FormulaConjunction(propA, propB)
    //  (A ∧ B)
    println(form001.toString)

    val form002 = P_FormulaConjunction(form001, propC)
    //  ((A ∧ B) ∧ C)
    println(form002.toString)

    val var_x = v("x")
    val var_y = v("y")
    val var_z = v("z")

    val v_x = P_TermVariable(var_x)
    //  x
    println(v_x)

    val v_y = P_TermVariable(var_y)
    //  y
    println(v_y)

    val v_z = P_TermVariable(var_z)
    //  z
    println(v_z)

    println
    println("#### Block 1a tests: Stackless substitution")

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
    val ff101 = pf("∀x∀y.(x=y → f(x)=f(y))")
    val sequent9101 = inferenceRuleAxiom(ff101)
    val sequent9102 = inferenceRuleWeakenL(sequent9101, ffEq)
    val sequent9103 = inferenceRuleContractL(sequent9102, ffEq, ff101)
    sequent9103.d

    println
    val ff102 = pf("a=b → f(a)=f(b)")
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

    //  Γ, A∧B ⊢ C, Δ
    //  ---------------
    //  Γ, A ⊢ B → C, Δ

    val sequent001c1 = inferenceRuleAxiom(pf("C"))
    val sequent001c2 = inferenceRuleWeakenL(sequent001c1, pf("A∧B"))
    val thm01Xa = inferenceRuleRool1Xa(sequent001c2, pf("A"), pf("B"), pf("C"))
    thm01Xa.d

    //  Γ, A ⊢ B → C, Δ
    //  ---------------
    //  Γ, A∧B ⊢ C, Δ

    val thm01Ya = inferenceRuleRool1Ya(thm01Xa, pf("A"), pf("B"), pf("C"))
    thm01Ya.d

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

    val sz01 = inferenceRuleAxiom(pf("A → B"))
    val sz02 = inferenceRuleRool1b(sz01, (pf("A"), pf("B")))
    sz02.d

    //  Γ, A ⊢ B, Δ
    //  ----------------
    //  Γ, C∧A ⊢ C∧B, Δ

    val thmXA02L = inferenceRuleRool2L(sz02, pf("A"), pf("B"), pf("C"))
    thmXA02L.d

    //  Γ, A ⊢ B, Δ
    //  ----------------
    //  Γ, A∧C ⊢ B∧C, Δ

    val thmXA02R = inferenceRuleRool2R(sz02, pf("A"), pf("B"), pf("C"))
    thmXA02R.d

    //  Γ, A ⊢ B, Δ
    //  ----------------
    //  Γ, C∨A ⊢ C∨B, Δ

    val thmXA03L = inferenceRuleRool3L(sz02, pf("A"), pf("B"), pf("C"))
    thmXA03L.d

    //  Γ, A ⊢ B, Δ
    //  ----------------
    //  Γ, A∨C ⊢ B∨C, Δ

    val thmXA03R = inferenceRuleRool3R(sz02, pf("A"), pf("B"), pf("C"))
    thmXA03R.d

    //  Γ, A ⊢ B, Δ
    //  ------------------
    //  Γ, C → A ⊢ C → B, Δ

    val thmXA04 = inferenceRuleXRool4(sz02, pf("A"), pf("B"), pf("C"))
    thmXA04.d

    //  Γ, A ⊢ B, Δ
    //  ------------------
    //  Γ, B → C ⊢ A → C, Δ

    val thmYA04 = inferenceRuleYRool4(sz02, pf("A"), pf("B"), pf("C"))
    thmYA04.d

    //  Γ, A ⊢ B, Δ
    //  --------------
    //  Γ, ¬B ⊢ ¬A, Δ

    val thmXA05 = inferenceRuleXRool5(sz02, pf("A"), pf("B"))
    thmXA05.d

    val thmZ0601 = inferenceRuleAxiom(pf("A(f(x)) → B(g(x))"))
    val thmZ0602 = inferenceRuleRool1b(thmZ0601, (pf("A(f(x))"), pf("B(g(x))")))
    val thmZ0603 = inferenceRuleUnivL(thmZ0602, pf("A(f(z)) → B(g(z))"),
      (pt("x"), var_z))

    //  Γ, A[t/x] ⊢ B[z/y], Δ
    //  ----------------------
    //  Γ, ∀xA ⊢ ∀yB, Δ

    println()
    thmZ0603.d
    val thmX0601 = inferenceRuleXRool6(thmZ0603, pf("A(f(x))"), (pt("x"), var_x),
      pf("B(g(x))"), (var_x, var_x))
    thmX0601.d
    val thmX0602 = inferenceRuleXRool6(thmZ0603, pf("A(f(y))"), (pt("x"), var_y),
      pf("B(g(y))"), (var_x, var_y))
    thmX0602.d

    //  Γ, A[z/x] ⊢ B[t/y], Δ
    //  ----------------------
    //  Γ, ∃xA ⊢ ∃yB, Δ

    println()
    thmZ0603.d
    val thmY0601 = inferenceRuleYRool6(thmZ0603, pf("A(f(x))"), (var_x, var_x),
      pf("B(g(x))"), (pt("x"), var_x))
    thmY0601.d
    val thmY0602 = inferenceRuleYRool6(thmZ0603, pf("A(f(y))"), (var_x, var_y),
      pf("B(g(y))"), (pt("x"), var_y))
    thmY0602.d

    //  Γ, A[t/x] ⊢ B[z/y], Δ
    //  ----------------------
    //  Γ, ∀xA ⊢ ¬∃y¬B, Δ

    println()
    val thmZ0701 = inferenceRuleAxiom(pf("P(x)"))
    thmZ0701.d
    val thmX0702 = inferenceRuleXRool7(thmZ0701, pf("P(x)"), (pt("x"), var_x),
      (pf("P(x)"), pf("¬P(x)")), (var_x, var_x))
    thmX0702.d

    //  Γ, A[z/x] ⊢ B[t/y], Δ
    //  ----------------------
    //  Γ, ∃xA ⊢ ¬∀y¬B, Δ

    println()
    thmZ0701.d
    val thmY0702 = inferenceRuleYRool7(thmZ0701, pf("P(x)"), (var_x, var_x),
      (pf("P(x)"), pf("¬P(x)")), (pt("x"), var_x))
    thmY0702.d

    //  InferenceRuleReplace

    import tactics._

    println()
    val thmTTX001 = inferenceRuleRool1e(pf("P"), pf("Q"), pf("R"))
    thmTTX001.d
    val thmTTX002 = tacticReplaceFormula(thmTTX001,
      (pf("(P∧Q)∧R"), pf("P∧(Q∧R)")),
      (pf("Q ∨ (P∧(Q∧R) → S(x))"), pf("Q ∨ ((P∧Q)∧R → S(x))")))
    thmTTX002.d

    println()
    val thmTTX101 = inferenceRuleRool1c(pf("P(x)"), pf("Q(y)"))
    thmTTX101.d
    val thmTTX102 = tacticReplaceFormula(thmTTX101,
      (pf("P(x)∧Q(y)"), pf("Q(y)∧P(x)")),
      (pf("∀x.(R(z) ∨ (Q(y)∧P(x) → S(x)))"), pf("∀x.(R(z) ∨ (P(x)∧Q(y) → S(x)))")))
    thmTTX102.d
    val thmTTX103 = tacticReplaceFormula(thmTTX101,
      (pf("P(x)∧Q(y)"), pf("Q(y)∧P(x)")),
      (pf("∃x.(R(z) ∨ (Q(y)∧P(x) → S(x)))"), pf("∃x.(R(z) ∨ (P(x)∧Q(y) → S(x)))")))
    thmTTX103.d

    println()
    val thmTTX501 = inferenceRuleAxiom(pf("f(0) = g(0)"))
    thmTTX501.d
    val thmTTX502 = tacticReplaceTerm(thmTTX501, pf("f(0) = g(0)"),
      (pf("P(h(0,0,f(0)))"), pf("P(h(0,0,g(0)))")))
    //thmTTX502.d

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
    println("#### Block 7.5 tests: diff")

    import diff.diff._

    println()
    val fd001 = pf("A ∨ B").asInstanceOf[P_Formula]
    println(fd001.diff(fd001))
    val fd002 = pf("C ∨ B").asInstanceOf[P_Formula]
    println(fd001.diff(fd002))
    val fd003 = pf("A ∨ C").asInstanceOf[P_Formula]
    println(fd001.diff(fd003))
    val fd004 = pf("A ∧ B → C ∨ D").asInstanceOf[P_Formula]
    println(fd004.diff(fd004))
    val fd005 = pf("A ∧ B → X ∨ D").asInstanceOf[P_Formula]
    println(fd004.diff(fd005))
    val fd006 = pf("A ∧ B → P(x0, x1, x2, x3, x4, x5, x6, x7) ∨ D").asInstanceOf[P_Formula]
    println(fd006.diff(fd006))
    val fd007 = pf("A ∧ B → P(x0, x1, x2, x3, x4, y, x6, x7) ∨ D").asInstanceOf[P_Formula]
    println(fd006.diff(fd007))
    val fd008 = pf("y = f(x0, x1, x2, x3, x4, x5, x6, x7)").asInstanceOf[P_Formula]
    println(fd008.diff(fd008))
    val fd009 = pf("y = f(x0, x1, x2, x3, g(x40, x41, x42, x43), x5, x6, x7)").asInstanceOf[P_Formula]
    println(fd008.diff(fd009))
    val fd010 = pf("y = f(x0, x1, x2, x3, g(x40, x41, x42, x43), x5, x6, x7)").asInstanceOf[P_Formula]
    println(fd010.diff(fd010))
    val fd011 = pf("y = f(x0, x1, x2, x3, g(x40, h(x410, x411), x42, x43), x5, x6, x7)").asInstanceOf[P_Formula]
    println(fd010.diff(fd011))
    val fd012 = pf("y = f(x0, x1, x2, x3, g(x40, h(x410, 0), x42, x43), x5, x6, x7)").asInstanceOf[P_Formula]
    println(fd011.diff(fd012))

    println
    println("#### Block 8 test: Eq and PA axioms")
    println

    import axiomatization_Eq.theorems._

    println("####      Equality is symmetric.")
    println

    val thEq001 = inferenceRuleAxiom(pf("x=y → (x=z → y=z)"))
    thEq001.d
    val thEq002 = inferenceRuleUnivL(thEq001, pf("x=y → (x=z → y=z)"),
      (pt("y"), var_y))
    thEq002.d
    val thEq003 = inferenceRuleUnivL(thEq002, pf("∀y.(x=y → (x=z → y=z))"),
      (pt("z"), var_z))
    thEq003.d
    val thEq004 = inferenceRuleUnivL(thEq003, pf("∀z∀y.(x=y → (x=z → y=z))"),
      (pt("x"), var_x))
    thEq004.d
    val thEq005 = inferenceRuleWeakenL(thEq004, thEq)
    thEq005.d
    val thEq006 = inferenceRuleContractL(thEq005, thEq, pf("∀x∀z∀y.(x=y → (x=z → y=z))"))
    thEq006.d
    val thEq007 = inferenceRuleRool1b(thEq006, (pf("x=y"), pf("x=z → y=z")))
    thEq007.d
    val thEq008 = inferenceRuleUnivR(thEq007, pf("x=z → y=z"),
      (var_z, var_z))
    thEq008.d

    println()
    val thEq009 = inferenceRuleAxiom(pf("x=x → y=x"))
    thEq009.d
    val thEq010 = inferenceRuleUnivL(thEq009, pf("x=z → y=z"),
      (pt("x"), var_z))
    thEq010.d
    val thEq011 = inferenceRuleCut((thEq008, thEq010), pf("∀z.(x=z → y=z)"))
    thEq011.d
    val thEq012 = inferenceRuleRool1b(thEq011, (pf("x=x"), pf("y=x")))
    thEq012.d
    val thEq013 = inferenceRuleImplR(thEq012, (pf("x=y"), pf("y=x")))
    thEq013.d
    val thEq014 = inferenceRuleUnivL(thEq013, pf("x=x"),
      (pt("x"), var_x))
    thEq014.d
    val thEq015 = inferenceRuleCut((thEq1, thEq014), pf("∀x.(x=x)"))
    thEq015.d
    val thEq016 = inferenceRuleUnivR(thEq015, pf("x=y → y=x"),
      (var_y, var_y))
    thEq016.d
    val thEq017 = inferenceRuleUnivR(thEq016, pf("∀y.(x=y → y=x)"),
      (var_x, var_x))
    thEq017.d
//  val thEq2 = thEq017

    println
    println("####      Equality is transitive.")

    println
    val var_v = v("v")
    val thEq101 = inferenceRuleAxiom(pf("x=y → y=x"))
    thEq101.d
    val thEq102 = inferenceRuleUnivL(thEq101, pf("x=y → y=x"),
      (pt("y"), var_y))
    thEq102.d
    val thEq103 = inferenceRuleUnivL(thEq102, pf("∀y.(x=y → y=x)"),
      (pt("x"), var_x))
    thEq103.d
    val thEq104 = inferenceRuleCut((thEq2, thEq103), pf("∀x∀y.(x=y → y=x)"))
    thEq104.d
    val thEq105 = inferenceRuleRool1b(thEq104, (pf("x=y"), pf("y=x")))
    thEq105.d

    println()
    val thEq106 = inferenceRuleAxiom(pf("x=y → (x=z → y=z)"))
    thEq106.d
    val thEq107 = inferenceRuleUnivL(thEq106, pf("x=y → (x=z → y=z)"),
      (pt("y"), var_y))
    thEq107.d
    val thEq108 = inferenceRuleUnivL(thEq107, pf("∀y.(x=y → (x=z → y=z))"),
      (pt("z"), var_z))
    thEq108.d
    val thEq109 = inferenceRuleUnivL(thEq108, pf("∀z∀y.(x=y → (x=z → y=z))"),
      (pt("x"), var_x))
    thEq109.d
    val thEq110 = inferenceRuleWeakenL(thEq109, thEq)
    thEq110.d
    val thEq111 = inferenceRuleContractL(thEq110, thEq, pf("∀x∀z∀y.(x=y → (x=z → y=z))"))
    thEq111.d
    val thEq112 = inferenceRuleUnivR(thEq111, pf("v=y → (v=z → y=z)"),
      (var_x, var_v))
    thEq112.d
    val thEq113 = inferenceRuleUnivR(thEq112, pf("∀v.(v=x → (v=z → x=z))"),
      (var_y, var_x))
    thEq113.d

    println()
    val thEq114 = inferenceRuleAxiom(pf("y=x → (y=z → x=z)"))
    thEq114.d
    val thEq115 = inferenceRuleUnivL(thEq114, pf("v=x → (v=z → x=z)"),
      (pt("y"), var_v))
    thEq115.d
    val thEq116 = inferenceRuleUnivL(thEq115, pf("∀v.(v=x → (v=z → x=z))"),
      (pt("x"), var_x))
    thEq116.d
    val thEq117 = inferenceRuleCut((thEq113, thEq116), pf("∀x∀v.(v=x → (v=z → x=z))"))
    thEq117.d
    val thEq118 = inferenceRuleRool1b(thEq117, (pf("y=x"), pf("y=z → x=z")))
    thEq118.d
    val thEq119 = inferenceRuleCut((thEq105, thEq118), pf("y=x"))
    thEq119.d
    val thEq120 = inferenceRuleRool1b(thEq119, (pf("y=z"), pf("x=z")))
    thEq120.d
    val thEq121 = inferenceRuleAndL1(thEq120, (pf("x=y"), pf("y=z")))
    thEq121.d
    val thEq122 = inferenceRuleAndL2(thEq121, (pf("x=y"), pf("y=z")))
    thEq122.d
    val thEq123 = inferenceRuleImplR(thEq122, (pf("x=y ∧ y=z"), pf("x=z")))
    thEq123.d
    val thEq124 = inferenceRuleUnivR(thEq123, pf("x=y ∧ y=z → x=z"),
      (var_z, var_z))
    thEq124.d
    val thEq125 = inferenceRuleUnivR(thEq124, pf("∀z.(x=y ∧ y=z → x=z)"),
      (var_y, var_y))
    thEq125.d
    val thEq126 = inferenceRuleUnivR(thEq125, pf("∀y∀z.(x=y ∧ y=z → x=z)"),
      (var_x, var_x))
    thEq126.d
//  val thEq3 = thEq126

    println
    println("####      Basic equality axiom and theorems")
    println

    thEq1.d
    thEq2.d
    thEq3.d

    println
    println("####      PA axioms")
    println

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

    println
    println("#### Block 9 test: Addition is associative")
    println

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

    println()
    println("  (4)")
    val ft0101 = pf("∀x∀y.(x=y → S(x)=S(y))")
    val th0101 = inferenceRuleAxiomFromTheory(thEq, ft0101)
    th0101.d

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

    println()
    val th0131 = inferenceRuleAndR((th0005, th0109),
      (pf("x+S(0) = S(x+0)"), pf("S(x+0) = S(x)")))
    th0131.d

    println()
    val ff0132 = pf("x+S(0)=S(x+0) ∧ S(x+0)=S(x) → x+S(0)=S(x)")
    val th0132 = inferenceRuleAxiom(ff0132)
    th0132.d
    val ff0133 = pf("x+S(0)=S(x+0) ∧ S(x+0)=z → x+S(0)=z")
    val th0133 = inferenceRuleUnivL(th0132, ff0133,
      (pt("S(x)"), var_z))
    th0133.d
    val ff0134 = pf("∀z.(x+S(0)=y ∧ y=z → x+S(0)=z)")
    val th0134 = inferenceRuleUnivL(th0133, ff0134,
      (pt("S(x+0)"), var_y))
    th0134.d
    val ff0135 = pf("∀y∀z.(x=y ∧ y=z → x=z)")
    val th0135 = inferenceRuleUnivL(th0134, ff0135,
      (pt("x+S(0)"), var_x))
    th0135.d
    val th0136 = inferenceRuleCut((thEq3, th0135),
      pf("∀x∀y∀z.(x=y ∧ y=z → x=z)"))
    th0136.d
    val ff0137 = pf("x+S(0)=S(x+0) ∧ S(x+0)=S(x)")
    val th0137 = inferenceRuleRool1b(th0136, (ff0137, pf("x+S(0)=S(x)")))
    th0137.d

    println()
    val th0138 = inferenceRuleCut((th0131, th0137), ff0137)
    th0138.d
    val th0139 = inferenceRuleUnivR(th0138, pf("x+S(0)=S(x)"),
      (var_x, var_x))
    th0139.d

    println()
    thEq.d
    thEq1.d
    thEq2.d
    thEq3.d

    println()
    val th881 = inferenceRuleAxiomFromTheory(thEq, pf("∀x∀y.(x=y → f(x) = f(y))"))
    th881.d
    val th882 = inferenceRuleAxiomFromTheory(thEq, pf("∀x∀x1∀y.(x=y → f(x, x1) = f(y, x1))"))
    th882.d
    val th883 = inferenceRuleAxiomFromTheory(thEq, pf("∀x0∀x∀y.(x=y → f(x0, x) = f(x0, y))"))
    th883.d
    val th888 = inferenceRuleAxiomFromTheory(thEq, pf("∀x0∀x1∀x2∀x3∀x∀x5∀y.(x=y → f(x0, x1, x2, x3, x, x5) = f(x0, x1, x2, x3, y, x5))"))
    th888.d

    println()
    val th1001 = inferenceRuleAxiom(pf("b=a → a=b"))
                                                    //  b=a → a=b ⊢ b=a → a=b
    val th1002 = inferenceRuleUnivL(th1001, pf("b=y → y=b"),
      (pt("a"), var_y))                             //  ∀y(b=y → y=b) ⊢ b=a → a=b
    val th1003 = inferenceRuleUnivL(th1002, pf("∀y.(x=y → y=x)"),
      (pt("b"), var_x))                             //  ∀x∀y(x=y → y=x) ⊢ b=a → a=b
    val th1004 = inferenceRuleCut((thEq2, th1003), pf("∀x∀y.(x=y → y=x)"))
                                                    //  Eq ⊢ b=a → a=b
    val th1005 = inferenceRuleRool1b(th1004, (pf("b = a"), pf("a = b")))
                                                    //  Eq, b=a ⊢ a=b
    th1005.d

    println()
    val th1011 = tacticReplaceTerm(th1005, pf("a = b"),
      (pf("P(a)"), pf("P(b)")))
    th1011.d
    val th1012 = tacticReplaceTerm(th1005, pf("a = b"),
      (pf("P(0, 0, a, 0)"), pf("P(0, 0, b, 0)")))
    th1012.d
    val th1013 = tacticReplaceTerm(th1005, pf("a = b"),
      (pf("A ∧ P(0, 0, a, 0) ∨ B"), pf("A ∧ P(0, 0, b, 0) ∨ B")))
    th1013.d

    println()
    val th1021 = tacticReplaceTerm(th1005, pf("a = b"),
      (pf("P(0, 0, S(f(0, 0+a)), 0)"), pf("P(0, 0, S(f(0, 0+b)), 0)")))
    th1021.d
    val th1022 = tacticReplaceTerm(th1005, pf("a = b"),
      (pf("A ∧ P(0, 0, S(f(0, 0+a)), 0) ∨ B"), pf("A ∧ P(0, 0, S(f(0, 0+b)), 0) ∨ B")))
    th1022.d

    println()
    val th2001 = inferenceRuleAxiom(pf("(a + b) + 0 = a + b"))
    val th2002 = inferenceRuleUnivL(th2001, pf("x+0 = x"), (pt("a+b"), var_x))
    val th2003 = inferenceRuleWeakenL(th2002, thPA)
    val th2004 = inferenceRuleContractL(th2003, thPA, pf{"∀x.(x+0 = x)"})
    th2004.d
    val th2011 = inferenceRuleAxiom(pf("b + 0 = b"))
    val th2012 = inferenceRuleUnivL(th2011, pf("x+0 = x"), (pt("b"), var_x))
    val th2013 = inferenceRuleWeakenL(th2012, thPA)
    val th2014 = inferenceRuleContractL(th2013, thPA, pf{"∀x.(x+0 = x)"})
    th2014.d
    val th2021 = tacticReplaceTerm(th2014, pf("b = b+0"),
      (pf("(a + b) + 0 = a + b"), pf("(a + b) + 0 = a + (b + 0)")))
    th2021.d



    println()
    val th2201 = tacticReplaceTerm(th1005, pf("a = b"),
      (pf("(c + d) + 0 = c + a"), pf("(c + d) + 0 = c + b")))
    th2201.d
    val th2202 = tacticReplaceTerm(th1005, pf("a = b"),
      (pf("(a + b) + 0 = a + a"), pf("(a + b) + 0 = a + b")))
    th2202.d
    val th2211 = inferenceRuleAxiom(pf("a = b"))
    val th2212 = tacticReplaceTerm(th2211, pf("a = b"),
      (pf("(a + b) + 0 = a + a"), pf("(a + b) + 0 = a + b")))
    th2212.d
    val th2213 = inferenceRuleAxiom(pf("b = a"))
    val th2214 = tacticReplaceTerm(th2213, pf("b = a"),
      (pf("(a + b) + 0 = a + b"), pf("(a + b) + 0 = a + a")))
    th2214.d
    val th2215 = inferenceRuleAxiom(pf("b = b+0"))
    val th2216 = tacticReplaceTerm(th2215, pf("b = b+0"),
      (pf("(a + b) + 0 = a + b"), pf("(a + b) + 0 = a + (b + 0)")))
    th2216.d
  }

}
