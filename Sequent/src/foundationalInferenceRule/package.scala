/**
  * Created by EdSnow on 7/22/2016.
  */


package object foundationalInferenceRule {


  import shapeless._

  import error._
  import error.Error.OError
  import error.Error_composite.errorListCombined
  import p_term._
  import p_formula._
  import p_axiomatization._

  import p_sequent._
  import p_sequent.Cedent
  import p_sequent.Cedent.CedentTemplate
  import sequent._
  import substitution.checkOccurs._
  import substitution.substitute._

  import inferenceRuleWrapper._


// ====================================================================


  private def zz_Error_subst(x: Option[Error]): OError = x


  private def zz_Error_axiom(x: Option[(Cedent, P_SFormulaAxiomatization)]): OError =
    x match {
      case Some((c, f_axiom)) => Some(new Error_axiom(c, f_axiom))
      case None => None
    }


  private def zz_Error_subsm(x: Option[(P_SFormulaAxiomatization, P_Formula)]): OError =
    x match {
      case Some((f, f_subsm)) => Some(new Error_subsm(f, f_subsm))
      case None => None
    }


// ====================================================================


  private type Rxxx = (CedentTemplate, CedentTemplate, Seq[OError])


  import shapeless.ops.function.{FnFromProduct, FnToProduct}

  private def foundationalInferenceRule[Ta <: HList, T <: HList, F](name: String, f: F)
    (implicit
      ftp: FnToProduct.Aux[F, T => Rxxx],
      vet: VetApplicand[Ta, T],
      ffp: FnFromProduct[Ta => Sequent]
    ): ffp.Out =
  ffp(
    (a: Ta) => {

      //  Scrutinize the arguments in the wrapper's applicand
      //  and convert them to a form consumable by the wrapped
      //  inference rule.

      val (applicand, errorListExt) = vet.vetApplicand(a, 1)

      //  Apply the inference rule if there is an applicand
      //  to which to apply it.

      val inference = applicand match {
        case Some(x) => Some(ftp(f)(x))
        case none => None
      }

      //  Turn the results of the inference back into a Sequent.

      inference match {
        case Some(x) =>
          val (ante, succ, errorSeqInt) = x
          P_Sequent(
            new Cedent(ante, 0 /* dummy */), new Cedent(succ, 0 /* dummy */),
            errorListCombined(errorSeqInt.flatten, errorListExt))
        case None =>
          new Sequent(errorListCombined(Seq(), errorListExt))
      }

    }
  )


// ====================================================================


  //  ------ (I)
  //  A ⊢ A

  val inferenceRuleAxiom =
    foundationalInferenceRule("I",
      (
        f: P_Formula                    //  A
      ) => {
        val fa = f
        val  a = CTESFormula(fa)
        (
          Set(a), Set(a),
          Seq()
        )
      }: Rxxx
    )


  //  Γ ⊢ Δ, A    A, Σ ⊢ Π
  //  -------------------- (Cut)
  //  Γ, Σ ⊢ Δ, Π

  val inferenceRuleCut =
    foundationalInferenceRule("Cut",
      (
        s: P_SeqPair,                   //  (Γ ⊢ Δ, A; A, Σ ⊢ Π)
        f: P_Formula                    //  A
      ) => {
        val (sL, sR) = s
        val fa = f
//      val  a = CTESFormula(fa)
        val  gamma      = CTECedent(sL.ante     )
        val (delta, error_L_succ) = sL.succ - fa
        val (sigma, error_R_ante) = sR.ante - fa
        val     pi      = CTECedent(sR.succ     )
        (
          Set(gamma, sigma), Set(delta, pi),
          Seq(
            zz_Error_L_succ(error_L_succ),
            zz_Error_R_ante(error_R_ante)
          )
        )
      }: Rxxx
    )


  //  Γ, A ⊢ Δ
  //  ---------- (∧L₁)
  //  Γ, A∧B ⊢ Δ

  val inferenceRuleAndL1 =
    foundationalInferenceRule("∧L₁",
      (
        s: P_Sequent,                   //  Γ, A ⊢ Δ
        f: P_FormPair                   //  (A, B)
      ) => {
        val (fa, fb) = f
//      val  a = CTESFormula(fa)
//      val  b = CTESFormula(fb)
        val ab = CTESFormula(P_FormulaConjunction(fa, fb))
        val (gamma,   error_ante) = s .ante - fa
        val  delta      = CTECedent(s .succ     )
        (
          Set(gamma, ab), Set(delta),
          Seq(
            zz_Error_ante(error_ante)
          )
        )
      }: Rxxx
    )


  //  Γ ⊢ A, Δ
  //  ---------- (∨R₁)
  //  Γ ⊢ A∨B, Δ

  val inferenceRuleOrR1 =
    foundationalInferenceRule("∨R₁",
      (
        s: P_Sequent,                   //  Γ ⊢ A, Δ
        f: P_FormPair                   //  (A, B)
      ) => {
        val (fa, fb) = f
//      val  a = CTESFormula(fa)
//      val  b = CTESFormula(fb)
        val ab = CTESFormula(P_FormulaDisjunction(fa, fb))
        val  gamma      = CTECedent(s .ante     )
        val (delta,   error_succ) = s .succ - fa
        (
          Set(gamma), Set(ab, delta),
          Seq(
            zz_Error_succ(error_succ)
          )
        )
      }: Rxxx
    )


  //  Γ, B ⊢ Δ
  //  ---------- (∧L₂)
  //  Γ, A∧B ⊢ Δ

  val inferenceRuleAndL2 =
    foundationalInferenceRule("∧L₂",
      (
        s: P_Sequent,                   //  Γ, B ⊢ Δ
        f: P_FormPair                   //  (A, B)
      ) => {
        val (fa, fb) = f
//      val  a = CTESFormula(fa)
//      val  b = CTESFormula(fb)
        val ab = CTESFormula(P_FormulaConjunction(fa, fb))
        val (gamma,   error_ante) = s .ante - fb
        val  delta      = CTECedent(s .succ     )
        (
          Set(gamma, ab), Set(delta),
          Seq(
            zz_Error_ante(error_ante)
          )
        )
      }: Rxxx
    )


  //  Γ ⊢ B, Δ
  //  ---------- (∨R₂)
  //  Γ ⊢ A∨B, Δ

  val inferenceRuleOrR2 =
    foundationalInferenceRule("∨R₂",
      (
        s: P_Sequent,                   //  Γ ⊢ B, Δ
        f: P_FormPair                   //  (A, B)
      ) => {
        val (fa, fb) = f
//      val  a = CTESFormula(fa)
//      val  b = CTESFormula(fb)
        val ab = CTESFormula(P_FormulaDisjunction(fa, fb))
        val  gamma      = CTECedent(s .ante     )
        val (delta,   error_succ) = s .succ - fb
        (
          Set(gamma), Set(ab, delta),
          Seq(
            zz_Error_succ(error_succ)
          )
        )
      }: Rxxx
    )


  //  Γ, A ⊢ Δ    Σ, B ⊢ Π
  //  -------------------- (∨L)
  //  Γ, Σ, A∨B ⊢ Δ, Π

  val inferenceRuleOrL =
    foundationalInferenceRule("∨L",
      (
        s: P_SeqPair,                   //  (Γ, A ⊢ Δ; Σ, B ⊢ Π)
        f: P_FormPair                   //  (A, B)
      ) => {
        val (sL, sR) = s
        val (fa, fb) = f
//      val  a = CTESFormula(fa)
//      val  b = CTESFormula(fb)
        val ab = CTESFormula(P_FormulaDisjunction(fa, fb))
        val (gamma, error_L_ante) = sL.ante - fa
        val  delta      = CTECedent(sL.succ     )
        val (sigma, error_R_ante) = sR.ante - fb
        val     pi      = CTECedent(sR.succ     )
        (
          Set(gamma, sigma, ab), Set(delta, pi),
          Seq(
            zz_Error_L_ante(error_L_ante),
            zz_Error_R_ante(error_R_ante)
          )
        )
      }: Rxxx
    )


  //  Γ ⊢ A, Δ    Σ ⊢ B, Π
  //  -------------------- (∧R)
  //  Γ, Σ ⊢ A∧B, Δ, Π

  val inferenceRuleAndR =
    foundationalInferenceRule("∧R",
      (
        s: P_SeqPair,                   //  (Γ ⊢ A, Δ; Σ ⊢ B, Π)
        f: P_FormPair                   //  (A, B)
      ) => {
        val (sL, sR) = s
        val (fa, fb) = f
//      val  a = CTESFormula(fa)
//      val  b = CTESFormula(fb)
        val ab = CTESFormula(P_FormulaConjunction(fa, fb))
        val  gamma      = CTECedent(sL.ante     )
        val (delta, error_L_succ) = sL.succ - fa
        val  sigma      = CTECedent(sR.ante     )
        val (   pi, error_R_succ) = sR.succ - fb
        (
          Set(gamma, sigma), Set(ab, delta, pi),
          Seq(
            zz_Error_L_succ(error_L_succ),
            zz_Error_R_succ(error_R_succ)
          )
        ): Rxxx
      }
    )


  //  Γ ⊢ A, Δ    Σ, B ⊢ Π
  //  -------------------- (→L)
  //  Γ, Σ, A → B ⊢ Δ, Π

  val inferenceRuleImplL =
    foundationalInferenceRule("→L",
      (
        s: P_SeqPair,                   //  (Γ ⊢ A, Δ; Σ, B ⊢ Π)
        f: P_FormPair                   //  (A, B)
      ) => {
        val (sL, sR) = s
        val (fa, fb) = f
//      val  a = CTESFormula(fa)
//      val  b = CTESFormula(fb)
        val ab = CTESFormula(P_FormulaImplication(fa, fb))
        val  gamma      = CTECedent(sL.ante     )
        val (delta, error_L_succ) = sL.succ - fa
        val (sigma, error_R_ante) = sR.ante - fb
        val     pi      = CTECedent(sR.succ     )
        (
          Set(gamma, sigma, ab), Set(delta, pi),
          Seq(
            zz_Error_L_succ(error_L_succ),
            zz_Error_R_ante(error_R_ante)
          )
        )
      }: Rxxx
    )


  //  Γ, A ⊢ B, Δ
  //  ----------- (→R)
  //  Γ ⊢ A → B, Δ

  val inferenceRuleImplR =
    foundationalInferenceRule("→R",
      (
        s: P_Sequent,                   //  Γ, A ⊢ B, Δ
        f: P_FormPair                   //  (A, B)
      ) => {
        val (fa, fb) = f
//      val  a = CTESFormula(fa)
//      val  b = CTESFormula(fb)
        val ab = CTESFormula(P_FormulaImplication(fa, fb))
        val (gamma,   error_ante) = s .ante - fa
        val (delta,   error_succ) = s .succ - fb
        (
          Set(gamma), Set(ab, delta),
          Seq(
            zz_Error_ante(error_ante),
            zz_Error_succ(error_succ)
          )
        )
      }: Rxxx
    )


  //  Γ ⊢ A, Δ    Σ, B ⊢ Π
  //  -------------------- (↔L₁)
  //  Γ, Σ, A ↔ B ⊢ Δ, Π

  val inferenceRuleBicondL1 =
    foundationalInferenceRule("↔L₁",
      (
        s: P_SeqPair,                   //  (Γ ⊢ A, Δ; Σ, B ⊢ Π)
        f: P_FormPair                   //  (A, B)
      ) => {
        val (sL, sR) = s
        val (fa, fb) = f
//      val  a = CTESFormula(fa)
//      val  b = CTESFormula(fb)
        val ab = CTESFormula(P_FormulaBiconditional(fa, fb))
        val  gamma      = CTECedent(sL.ante     )
        val (delta, error_L_succ) = sL.succ - fa
        val (sigma, error_R_ante) = sR.ante - fb
        val     pi      = CTECedent(sR.succ     )
        (
          Set(gamma, sigma, ab), Set(delta, pi),
          Seq(
            zz_Error_L_succ(error_L_succ),
            zz_Error_R_ante(error_R_ante)
          )
        )
      }: Rxxx
    )


  //  Γ, A ⊢ Δ    Σ ⊢ B, Π
  //  -------------------- (↔L₂)
  //  Γ, Σ, A ↔ B ⊢ Δ, Π

  val inferenceRuleBicondL2 =
    foundationalInferenceRule("↔L₂",
      (
        s: P_SeqPair,                   //  (Γ, A ⊢ Δ; Σ ⊢ B, Π)
        f: P_FormPair                   //  (A, B)
      ) => {
        val (sL, sR) = s
        val (fa, fb) = f
//      val  a = CTESFormula(fa)
//      val  b = CTESFormula(fb)
        val ab = CTESFormula(P_FormulaBiconditional(fa, fb))
        val (gamma, error_L_ante) = sL.ante - fa
        val  delta      = CTECedent(sL.succ     )
        val  sigma      = CTECedent(sR.ante     )
        val (   pi, error_R_succ) = sR.succ - fb
        (
          Set(gamma, sigma, ab), Set(delta, pi),
          Seq(
            zz_Error_L_succ(error_L_ante),
            zz_Error_R_ante(error_R_succ)
          )
        )
      }: Rxxx
    )


  //  Γ, A ⊢ B, Δ    Σ, B ⊢ A, Π
  //  ------------------------- (↔R)
  //  Γ, Σ ⊢ A ↔ B, Δ, Π

  val inferenceRuleBicondR =
    foundationalInferenceRule("↔R",
      (
        s: P_SeqPair,                   //  (Γ, A ⊢ B, Δ; Σ, B ⊢ A, Π)
        f: P_FormPair                   //  (A, B)
      ) => {
        val (sL, sR) = s
        val (fa, fb) = f
//      val  a = CTESFormula(fa)
//      val  b = CTESFormula(fb)
        val ab = CTESFormula(P_FormulaBiconditional(fa, fb))
        val (gamma, error_L_ante) = sL.ante - fa
        val (delta, error_L_succ) = sL.succ - fb
        val (sigma, error_R_ante) = sR.ante - fb
        val (   pi, error_R_succ) = sR.succ - fa
        (
          Set(gamma, sigma), Set(ab, delta, pi),
          Seq(
            zz_Error_ante(error_L_ante),
            zz_Error_ante(error_L_succ),
            zz_Error_ante(error_R_ante),
            zz_Error_succ(error_R_succ)
          )
        )
      }: Rxxx
    )


  //  Γ ⊢ A, Δ
  //  --------- (¬L)
  //  Γ, ¬A ⊢ Δ

  val inferenceRuleNotL =
    foundationalInferenceRule("¬L",
      (
        s: P_Sequent,                   //  Γ ⊢ A, Δ
        f: P_Formula                    //  A
      ) => {
        val fa = f
//      val  a = CTESFormula(fa)
        val na = CTESFormula(P_FormulaNegation(fa))
        val  gamma      = CTECedent(s .ante     )
        val (delta,   error_succ) = s .succ - fa
        (
          Set(gamma, na), Set(delta),
          Seq(
            zz_Error_succ(error_succ)
          )
        )
      }: Rxxx
    )


  //  Γ, A ⊢ Δ
  //  --------- (¬R)
  //  Γ ⊢ ¬A, Δ

  val inferenceRuleNotR =
    foundationalInferenceRule("¬R",
      (
        s: P_Sequent,                   //  Γ, A ⊢ Δ
        f: P_Formula                    //  A
      ) => {
        val fa = f
//      val  a = CTESFormula(fa)
        val na = CTESFormula(P_FormulaNegation(fa))
        val (gamma,   error_ante) = s .ante - fa
        val  delta      = CTECedent(s .succ     )
        (
          Set(gamma), Set(na, delta),
          Seq(
            zz_Error_ante(error_ante)
          )
        )
      }: Rxxx
    )


  //  Γ, A[t/x] ⊢ Δ
  //  ------------- (∀L)
  //  Γ, ∀xA ⊢ Δ

  val inferenceRuleUnivL =
    foundationalInferenceRule("∀L",
      (
        s: P_Sequent,                       //  Γ, A[t/x] ⊢ Δ
        f: P_Formula,                       //  A
        subst: (P_Term, P_VarName)          //  (t, x)
      ) => {
        val fa = f
//      val  a = CTESFormula(fa)
        val (t, x) = subst
        val (faSubst, error_subst) = fa.substitute(t, x)
        val qa = CTESFormula(P_FormulaQuantificationUniversal(x, fa))
        val (gamma,   error_ante) = s .ante - faSubst
        val  delta      = CTECedent(s .succ          )
        (
          Set(gamma, qa), Set(delta),
          Seq(
            zz_Error_ante(error_ante),
            zz_Error_subst(error_subst)
          )
        )
      }: Rxxx
    )


  //  Γ ⊢ A[y/x], Δ
  //  ------------- (∀R)
  //  Γ ⊢ ∀xA, Δ

  val inferenceRuleUnivR =
    foundationalInferenceRule("∀R",
      (
        s: P_Sequent,                   //  Γ ⊢ A[y/x], Δ
        f: P_Formula,                   //  A
        subst: (P_VarName, P_VarName)   //  (y, x)
      ) => {
        val fa = f
//      val  a = CTESFormula(fa)
        val (y, x) = subst
        val (faSubst, error_subst) = fa.substitute(y, x)
        val qa = CTESFormula(P_FormulaQuantificationUniversal(x, fa))
        val  cgamm                = s .ante
        val (cdelt,   error_succ) = s .succ -- faSubst
        val  gamma = CTECedent(cgamm)
        val  delta = CTECedent(cdelt)
        val error_occurs_ante = cgamm.checkOccurs(y)
        val error_occurs_succ = cdelt.checkOccurs(y)
        (
          Set(gamma), Set(qa, delta),
          Seq(
            zz_Error_succ(error_succ),
            zz_Error_subst(error_subst),
            zz_Error_occurs_ante(error_occurs_ante),
            zz_Error_occurs_succ(error_occurs_succ)
          )
        )
      }: Rxxx
    )


  //  Γ, A[y/x] ⊢ Δ
  //  ------------- (∃L)
  //  Γ, ∃xA ⊢ Δ

  val inferenceRuleExistL =
    foundationalInferenceRule("∃L",
      (
        s: P_Sequent,                   //  Γ, A[y/x] ⊢ Δ
        f: P_Formula,                   //  A
        subst: (P_VarName, P_VarName)   //  (y, x)
      ) => {
        val fa = f
//      val  a = CTESFormula(fa)
        val (y, x) = subst
        val (faSubst, error_subst) = fa.substitute(y, x)
        val qa = CTESFormula(P_FormulaQuantificationExistential(x, fa))
        val (cgamm,   error_ante) = s .ante -- faSubst
        val  cdelt                = s .succ
        val  gamma = CTECedent(cgamm)
        val  delta = CTECedent(cdelt)
        val error_occurs_ante = cgamm.checkOccurs(y)
        val error_occurs_succ = cdelt.checkOccurs(y)
        (
          Set(gamma, qa), Set(delta),
          Seq(
            zz_Error_ante(error_ante),
            zz_Error_subst(error_subst),
            zz_Error_occurs_ante(error_occurs_ante),
            zz_Error_occurs_succ(error_occurs_succ)
          )
        )
      }: Rxxx
    )


  //  Γ ⊢ A[t/x], Δ
  //  ------------- (∃R)
  //  Γ ⊢ ∃xA, Δ

  val inferenceRuleExistR =
    foundationalInferenceRule("∃R",
      (
        s: P_Sequent,                   //  Γ ⊢ A[t/x], Δ
        f: P_Formula,                   //  A
        subst: (P_Term, P_VarName)      //  (t, x)
      ) => {
        val fa = f
//      val  a = CTESFormula(fa)
        val (t, x) = subst
        val (faSubst, error_subst) = fa.substitute(t, x)
        val qa = CTESFormula(P_FormulaQuantificationExistential(x, fa))
        val  gamma      = CTECedent(s .ante          )
        val (delta,   error_succ) = s .succ - faSubst
        (
          Set(gamma), Set(qa, delta),
          Seq(
            zz_Error_succ(error_succ),
            zz_Error_subst(error_subst)
          )
        )
      }: Rxxx
    )


  //  Γ ⊢ Δ
  //  -------- (WL)
  //  Γ, A ⊢ Δ
  //
  //  This is the one and only place where
  //  axiomatizations can be introduced.

  val inferenceRuleWeakenL =
    foundationalInferenceRule("WL",
      (
        s: P_Sequent,                   //  Γ ⊢ Δ
        f: P_SFormula                   //  A
      ) => {
        val fa = f
        val  a = CTESFormula(fa)
        val  gamma      = CTECedent(s .ante     )
        val  delta      = CTECedent(s .succ     )
        (
          Set(gamma, a), Set(delta),
          Seq()
        )
      }: Rxxx
    )


  //  Γ ⊢ Δ
  //  -------- (WR)
  //  Γ ⊢ A, Δ

  val inferenceRuleWeakenR =
    foundationalInferenceRule("WR",
      (
        s: P_Sequent,                   //  Γ ⊢ Δ
        f: P_Formula                    //  A
      ) => {
        val fa = f
        val  a = CTESFormula(fa)
        val  gamma      = CTECedent(s .ante     )
        val  delta      = CTECedent(s .succ     )
        (
          Set(gamma), Set(a, delta),
          Seq()
        )
      }: Rxxx
    )


  //  Γ, Theory, A ⊢ Δ
  //  ---------------- (CL)
  //  Γ, Theory ⊢ Δ

  val inferenceRuleContractL =
    foundationalInferenceRule("CL",
      (
        s: P_Sequent,                   //  Γ, Theory, A ⊢ Δ
        a: P_SFormulaAxiomatization,    //  Theory
        f: P_Formula                    //  A
      ) => {
        val     error_axiom = s .ante.contains(a)
        val     error_subsm = a.checkSubsumption(f)
        val (gamma,   error_ante) = s .ante - f
        val  delta      = CTECedent(s .succ    )
        (
          Set(gamma), Set(delta),
          Seq(
            zz_Error_ante(error_ante),
            zz_Error_axiom(error_axiom),
            zz_Error_subsm(error_subsm)
          )
        )
      }: Rxxx
    )


}
