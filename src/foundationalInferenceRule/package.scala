/**
  * Created by EdSnow on 7/22/2016.
  */


package object foundationalInferenceRule {


  import shapeless._

  import error._
  import p_formula._
  import p_axiomatization._
  import substitution._

  import p_sequent._
  import sequent._
  import inferenceRuleWrapper._


// ====================================================================


  def zz_Error_subst(x: Option[(P_Formula, P_Formula)]): OError =
    x match {
      case Some((f, f_subst)) => Some(new Error_subst(f, f_subst))
      case None => None
    }


  def zz_Error_axiom(x: Option[(Cedent, P_Axiomatization)]): OError =
    x match {
      case Some((c, f_axiom)) => Some(new Error_axiom(c, f_axiom))
      case None => None
    }


  def zz_Error_subsm(x: Option[(P_Axiomatization, P_Formula)]): OError =
    x match {
      case Some((f, f_subsm)) => Some(new Error_subsm(f, f_subsm))
      case None => None
    }


// ====================================================================


  import shapeless.ops.function.{FnFromProduct, FnToProduct}

  def foundationalInferenceRule[Ta <: HList, T <: HList, F](name: String, f: F)
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
          new P_Sequent(
            new Cedent(ante, 0 /* dummy */), new Cedent(succ, 0 /* dummy */),
            errorListCombined(errorSeqInt.flatten, errorListExt))
        case None =>
          new Sequent(errorListCombined(Seq(), errorListExt))
      }

    }
  )


// ====================================================================


  def inferenceRuleAxiom =
    foundationalInferenceRule("I",
      (
        f: P_Formula
      ) => {
        val fa = f
        val  a = CTESFormula(fa)
        (
          Set(a), Set(a),
          Seq()
        )
      }: Rxxx
    )


  def inferenceRuleCut =
    foundationalInferenceRule("Cut",
      (
        s: P_SeqPair,
        f: P_Formula
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


  def inferenceRuleAndL1 =
    foundationalInferenceRule("∧L1",
      (
        s: P_Sequent,
        f: P_FormPair
      ) => {
        val fa = f._1
        val fb = f._2
//      val  a = CTESFormula(fa)
//      val  b = CTESFormula(fb)
        val ab = CTESFormula(new P_FormulaConjunction(fa, fb))
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


  val inferenceRuleOrR1 =
    foundationalInferenceRule("∨R1",
      (
        s: P_Sequent,
        f: P_FormPair
      ) => {
        val (fa, fb) = f
//      val  a = CTESFormula(fa)
//      val  b = CTESFormula(fb)
        val ab = CTESFormula(new P_FormulaDisjunction(fa, fb))
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


  val inferenceRuleAndL2 =
    foundationalInferenceRule("∧L2",
      (
        s: P_Sequent,
        f: P_FormPair
      ) => {
        val (fa, fb) = f
//      val  a = CTESFormula(fa)
//      val  b = CTESFormula(fb)
        val ab = CTESFormula(new P_FormulaConjunction(fa, fb))
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


  val inferenceRuleOrR2 =
    foundationalInferenceRule("∨R2",
      (
        s: P_Sequent,
        f: P_FormPair
      ) => {
        val (fa, fb) = f
//      val  a = CTESFormula(fa)
//      val  b = CTESFormula(fb)
        val ab = CTESFormula(new P_FormulaDisjunction(fa, fb))
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


  val inferenceRuleOrL =
    foundationalInferenceRule("∨L",
      (
        s: P_SeqPair,
        f: P_FormPair
      ) => {
        val (sL, sR) = s
        val (fa, fb) = f
//      val  a = CTESFormula(fa)
//      val  b = CTESFormula(fb)
        val ab = CTESFormula(new P_FormulaDisjunction(fa, fb))
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


  val inferenceRuleAndR =
    foundationalInferenceRule("∧R",
      (
        s: P_SeqPair,
        f: P_FormPair
      ) => {
        val (sL, sR) = s
        val (fa, fb) = f
//      val  a = CTESFormula(fa)
//      val  b = CTESFormula(fb)
        val ab = CTESFormula(new P_FormulaConjunction(fa, fb))
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


  val inferenceRuleImplL =
    foundationalInferenceRule("→L",
      (
        s: P_SeqPair,
        f: P_FormPair
      ) => {
        val (sL, sR) = s
        val (fa, fb) = f
//      val  a = CTESFormula(fa)
//      val  b = CTESFormula(fb)
        val ab = CTESFormula(new P_FormulaImplication(fa, fb))
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


  val inferenceRuleImplR =
    foundationalInferenceRule("→R",
      (
        s: P_Sequent,
        f: P_FormPair
      ) => {
        val (fa, fb) = f
//      val  a = CTESFormula(fa)
//      val  b = CTESFormula(fb)
        val ab = CTESFormula(new P_FormulaImplication(fa, fb))
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


  val inferenceRuleNotL =
    foundationalInferenceRule("¬L",
      (
        s: P_Sequent,
        f: P_Formula
      ) => {
        val fa = f
//      val  a = CTESFormula(fa)
        val na = CTESFormula(new P_FormulaNegation(fa))
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


  val inferenceRuleNotR =
    foundationalInferenceRule("¬R",
      (
        s: P_Sequent,
        f: P_Formula
      ) => {
        val fa = f
//      val  a = CTESFormula(fa)
        val na = CTESFormula(new P_FormulaNegation(fa))
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


  val inferenceRuleUnivL =
    foundationalInferenceRule("∀L",
      (
        s: P_Sequent,
        f: P_FormPair
      ) => {
        val (faSubst, fa) = f
        val (v, error_subst) = fa.checkSubstTerm(faSubst)
//      val  a = CTESFormula(fa)
        val va = CTESFormula(new P_FormulaQuantification(QuantificationKind.Universal, v, fa))
        val (gamma,   error_ante) = s .ante - faSubst
        val  delta      = CTECedent(s .succ          )
        (
          Set(gamma, va), Set(delta),
          Seq(
            zz_Error_ante(error_ante),
            zz_Error_subst(error_subst)
          )
        )
      }: Rxxx
    )


  val inferenceRuleUnivR =
    foundationalInferenceRule("∀R",
      (
        s: P_Sequent,
        f: P_FormPair
      ) => {
        val (faSubst, fa) = f
        val (v, error_subst) = fa.checkSubstVar(faSubst)
//      val  a = CTESFormula(fa)
        val va = CTESFormula(new P_FormulaQuantification(QuantificationKind.Universal, v, fa))
        val  cgamm                = s .ante
        val (cdelt,   error_succ) = s .succ -- faSubst
        val  gamma = CTECedent(cgamm)
        val  delta = CTECedent(cdelt)
        val error_occurs_ante = cgamm.checkOccurs(v)
        val error_occurs_succ = cdelt.checkOccurs(v)
        (
          Set(gamma), Set(va, delta),
          Seq(
            zz_Error_succ(error_succ),
            zz_Error_subst(error_subst),
            zz_Error_occurs_ante(error_occurs_ante),
            zz_Error_occurs_succ(error_occurs_succ)
          )
        )
      }: Rxxx
    )


  val inferenceRuleExistL =
    foundationalInferenceRule("∃L",
      (
        s: P_Sequent,
        f: P_FormPair
      ) => {
        val (faSubst, fa) = f
        val (v, error_subst) = fa.checkSubstVar(faSubst)
//      val  a = CTESFormula(fa)
        val va = CTESFormula(new P_FormulaQuantification(QuantificationKind.Existential, v, fa))
        val (cgamm,   error_ante) = s .ante -- faSubst
        val  cdelt                = s .succ
        val  gamma = CTECedent(cgamm)
        val  delta = CTECedent(cdelt)
        val error_occurs_ante = cgamm.checkOccurs(v)
        val error_occurs_succ = cdelt.checkOccurs(v)
        (
          Set(gamma, va), Set(delta),
          Seq(
            zz_Error_ante(error_ante),
            zz_Error_subst(error_subst),
            zz_Error_occurs_ante(error_occurs_ante),
            zz_Error_occurs_succ(error_occurs_succ)
          )
        )
      }: Rxxx
    )


  val inferenceRuleExistR =
    foundationalInferenceRule("∃R",
      (
        s: P_Sequent,
        f: P_FormPair
      ) => {
        val (faSubst, fa) = f
        val (v, error_subst) = fa.checkSubstTerm(faSubst)
//      val  a = CTESFormula(fa)
        val va = CTESFormula(new P_FormulaQuantification(QuantificationKind.Existential, v, fa))
        val  gamma      = CTECedent(s .ante          )
        val (delta,   error_succ) = s .succ - faSubst
        (
          Set(gamma), Set(va, delta),
          Seq(
            zz_Error_succ(error_succ),
            zz_Error_subst(error_subst)
          )
        )
      }: Rxxx
    )


  val inferenceRuleWeakenL =
    foundationalInferenceRule("WL",
      (
        s: P_Sequent,
        f: P_SFormula     //  This is the one and only place where
                          //  axiomatizations can be introduced.
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


  val inferenceRuleWeakenR =
    foundationalInferenceRule("WR",
      (
        s: P_Sequent,
        f: P_Formula
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


  val inferenceRuleContractL =
    foundationalInferenceRule("CL",
      (
        s: P_Sequent,
        a: P_Axiomatization,
        f: P_Formula
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
