/**
  * Created by EdSnow on 8/27/2016.
  */


package object derivedInferenceRule {


  import formula._
  import p_formula._
  import p_sequent._
  import sequent._
  import foundationalInferenceRule._

  import shapeless.HList
  import shapeless.ops.function.{FnFromProduct, FnToProduct}

  import inferenceRuleWrapper._


  def derivedInferenceRule[Ta <: HList, T <: HList, F](name: String, f: F)
   (implicit
     ftp: FnToProduct.Aux[F, T => Sequent],
     vet: VetApplicand[Ta, T],
     ffp: FnFromProduct[Ta => Sequent]
   ): ffp.Out =
  ffp(
    (a: Ta) => {

      //  Scrutinize the arguments in the wrapper's applicand
      //  and convert them to a form consumable by the wrapped
      //  inference rule.

      val (applicand, errorList) = vet.vetApplicand(a, 1)

      //  Apply the inference rule if there is an applicand
      //  to which to apply it.

      applicand match {
        case Some(x) => ftp(f)(x)
        case none => new Sequent(None)
      }

    }
  )


// ====================================================================


  //  Γ, A ⊢ B, Δ
  //  ------------
  //  Γ ⊢ A → B, Δ

  val inferenceRuleRool1a =
    derivedInferenceRule("Rool1a",
      (
        s: P_Sequent,                               //  Γ, A ⊢ B, Δ
        f: P_FormPair                               //  (A, B)
      ) => {
        val s1 = s                  //  must conform to Γ, A ⊢ B, Δ
        val a = f._1                                //  A
        val b = f._2                                //  B

        val s001 = inferenceRuleImplR(s1, (a, b))   //  A, A → B ⊢ B, Δ
        s001
      }
    )


  //  Γ ⊢ A → B, Δ
  //  ------------
  //  Γ, A ⊢ B, Δ

  val inferenceRuleRool1b =
    derivedInferenceRule("Rool1b",
      (
        s: P_Sequent,                               //  Γ ⊢ A → B, Δ
        f: P_FormPair                               //  (A, B)
      ) => {
        val s1 = s                  //  must conform to Γ ⊢ A → B, Δ
        val a = f._1                                //  A
        val b = f._2                                //  B

        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleAxiom(b)            //  B ⊢ B
        val s003 = inferenceRuleImplL((s001, s002), (a, b))
                                                    //  A, A → B ⊢ B
        val s004 = inferenceRuleCut((s1, s003), new P_FormulaImplication(a, b))
                                                    //  Γ, A ⊢ B, Δ
        s004
      }
    )


  //  ----------
  //  A∧B ⊢ B∧A

  val inferenceRuleRool1c =
    derivedInferenceRule("Rool1c",
      (
        a: P_Formula,                               //  A
        b: P_Formula                                //  B
      ) => {
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleAxiom(b)            //  B ⊢ B
        val s003 = inferenceRuleAndR((s002, s001), (b, a))
                                                    //  A, B ⊢ B∧A
        val s004 = inferenceRuleAndL1(s003, (a, b)) //  A∧B, B ⊢ B∧A
        val s005 = inferenceRuleAndL2(s004, (a, b)) //  A∧B ⊢ B∧A
        s005
      }
    )


  //  ----------
  //  A∨B ⊢ B∨A

  val inferenceRuleRool1d =
    derivedInferenceRule("Rool1d",
      (
        a: P_Formula,                               //  A
        b: P_Formula                                //  B
      ) => {
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleAxiom(b)            //  B ⊢ B
        val s003 = inferenceRuleOrL((s001, s002), (a, b))
                                                    //  A∨B ⊢ A, B
        val s004 = inferenceRuleOrR2(s003, (b, a))  //  A∨B ⊢ B∨A, B
        val s005 = inferenceRuleOrR1(s004, (b, a))  //  A∨B ⊢ B∨A
        s005
      }
    )


  //  -------------------
  //  (A∧B)∧C ⊢ A∧(B∧C)

  val inferenceRuleRool1e =
    derivedInferenceRule("Rool1e",
      (
        a: P_Formula,                               //  A
        b: P_Formula,                               //  B
        c: P_Formula                                //  C
      ) => {
        val f001 = new P_FormulaConjunction(a, b)   //  A∧B
        val f002 = new P_FormulaConjunction(b, c)   //  B∧C
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleAxiom(b)            //  B ⊢ B
        val s003 = inferenceRuleAxiom(c)            //  C ⊢ C
        val s004 = inferenceRuleAndR((s002, s003), (b, c))
                                                    //  B, C ⊢ B∧C
        val s005 = inferenceRuleAndR((s001, s004), (a, f002))
                                                    //  A, B, C ⊢ A∧(B∧C)
        val s006 = inferenceRuleAndL1(s005, (a, b)) //  A∧B, B, C ⊢ A∧(B∧C)
        val s007 = inferenceRuleAndL2(s006, (a, b)) //  A∧B, C ⊢ A∧(B∧C)
        val s008 = inferenceRuleAndL1(s007, (f001, c))
                                                    //  (A∧B)∧C, C ⊢ A∧(B∧C)
        val s009 = inferenceRuleAndL2(s008, (f001, c))
                                                    //  (A∧B)∧C ⊢ A∧(B∧C)
        s009
      }
    )


  //  -------------------
  //  (A∨B)∨C ⊢ A∨(B∨C)

  val inferenceRuleRool1f =
    derivedInferenceRule("Rool1f",
      (
        a: P_Formula,                               //  A
        b: P_Formula,                               //  B
        c: P_Formula                                //  C
      ) => {
        val f001 = new P_FormulaDisjunction(a, b)   //  A∨B
        val f002 = new P_FormulaDisjunction(b, c)   //  B∨C
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleAxiom(b)            //  B ⊢ B
        val s003 = inferenceRuleAxiom(c)            //  C ⊢ C
        val s004 = inferenceRuleOrL((s001, s002), (a, b))
                                                    //  A∨B ⊢ A, B
        val s005 = inferenceRuleOrL((s004, s003), (f001, c))
                                                    //  (A∨B)∨C ⊢ A, B, C
        val s006 = inferenceRuleOrR1(s005, (b, c))  //  (A∨B)∨C ⊢ A, B∨C, C
        val s007 = inferenceRuleOrR2(s006, (b, c))  //  (A∨B)∨C ⊢ A, B∨C
        val s008 = inferenceRuleOrR1(s007, (a, f002))
                                                    //  (A∨B)∨C ⊢ A∨(B∨C), B∨C
        val s009 = inferenceRuleOrR2(s008, (a, f002))
                                                    //  (A∨B)∨C ⊢ A∨(B∨C)
        s009
      }
    )


  //  -------------------
  //  A∧(B∧C) ⊢ (A∧B)∧C

  val inferenceRuleRool1g =
    derivedInferenceRule("Rool1g",
      (
        a: P_Formula,                               //  A
        b: P_Formula,                               //  B
        c: P_Formula                                //  C
      ) => {
        val f001 = new P_FormulaConjunction(a, b)   //  A∧B
        val f002 = new P_FormulaConjunction(b, c)   //  B∧C
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleAxiom(b)            //  B ⊢ B
        val s003 = inferenceRuleAxiom(c)            //  C ⊢ C
        val s004 = inferenceRuleAndR((s001, s002), (a, b))
                                                    //  A, B ⊢ A∧B
        val s005 = inferenceRuleAndR((s004, s003), (f001, c))
                                                    //  A, B, C ⊢ (A∧B)∧C
        val s006 = inferenceRuleAndL1(s005, (b, c)) //  A, B∧C, C ⊢ (A∧B)∧C
        val s007 = inferenceRuleAndL2(s006, (b, c)) //  A, B∧C ⊢ (A∧B)∧C
        val s008 = inferenceRuleAndL1(s007, (a, f002))
                                                    //  A∧(B∧C), B∧C ⊢ (A∧B)∧C
        val s009 = inferenceRuleAndL2(s008, (a, f002))
                                                    //  A∧(B∧C) ⊢ (A∧B)∧C
        s009
      }
    )


  //  -------------------
  //  A∨(B∨C) ⊢ (A∨B)∨C

  val inferenceRuleRool1h =
    derivedInferenceRule("Rool1h",
      (
        a: P_Formula,                               //  A
        b: P_Formula,                               //  B
        c: P_Formula                                //  C
      ) => {
        val f001 = new P_FormulaDisjunction(a, b)   //  A∨B
        val f002 = new P_FormulaDisjunction(b, c)   //  B∨C
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleAxiom(b)            //  B ⊢ B
        val s003 = inferenceRuleAxiom(c)            //  C ⊢ C
        val s004 = inferenceRuleOrL((s002, s003), (b, c))
                                                    //  B∨C ⊢ B, C
        val s005 = inferenceRuleOrL((s001, s004), (a, f002))
                                                    //  A∨(B∨C) ⊢ A, B, C
        val s006 = inferenceRuleOrR1(s005, (a, b))  //  A∨(B∨C) ⊢ A∨B, B, C
        val s007 = inferenceRuleOrR2(s006, (a, b))  //  A∨(B∨C) ⊢ A∨B, C
        val s008 = inferenceRuleOrR1(s007, (f001, c))
                                                    //  A∨(B∨C) ⊢ (A∨B)∨C, C
        val s009 = inferenceRuleOrR2(s008, (f001, c))
                                                    //  A∨(B∨C) ⊢ (A∨B)∨C
        s009
      }
    )


  //  -------------
  //  ∀xAx ⊢ ∀yAy

  val inferenceRuleRool1RUniv =
    derivedInferenceRule("Rool1RUniv",
      (
        a: P_Formula,                               //  A
        b: P_Formula,                               //  B
        c: P_Formula                                //  C
      ) => {
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleUnivL(s001, (a, b)) //  ∀xAx ⊢ A
        val s003 = inferenceRuleUnivR(s002, (a, c)) //  ∀xAx ⊢ ∀yAy
        s003
      }
    )


  //  ------------
  //  ∃xAx ⊢ ∃yAy

  val inferenceRuleRool1RExist =
    derivedInferenceRule("Rool1RExist",
      (
        a: P_Formula,                               //  A
        b: P_Formula,                               //  B
        c: P_Formula                                //  C
      ) => {
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleExistL(s001, (a, b))
                                                    //  ∃xAx ⊢ A
        val s003 = inferenceRuleExistR(s002, (a, c))
                                                    //  ∃xAx ⊢ ∃yAy
        s003
      }
    )


  //  ----------------
  //  A → B ⊢ A∧C → B∧C

  val inferenceRuleRool2 =
    derivedInferenceRule("Rool2",
      (
        a: P_Formula,                               //  A
        b: P_Formula,                               //  B
        c: P_Formula                                //  C
      ) => {
        val f001 = new P_FormulaConjunction(a, c)   //  A∧C
        val f002 = new P_FormulaConjunction(b, c)   //  B∧C
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleAxiom(b)            //  B ⊢ B
        val s003 = inferenceRuleAxiom(c)            //  C ⊢ C
        val s004 = inferenceRuleWeakenL(s002, c)    //  B, C ⊢ B
        val s005 = inferenceRuleWeakenL(s003, b)    //  B, C ⊢ C
        val s006 = inferenceRuleAndR((s004, s005), (b, c))
                                                    //  B, C ⊢ B∧C
        val s007 = inferenceRuleImplL((s001, s006), (a, b))
                                                    //  A, C, A → B ⊢ B∧C
        val s008 = inferenceRuleAndL1(s007, (a, c)) //  A∧C, C, A → B ⊢ B∧C
        val s009 = inferenceRuleAndL2(s008, (a, c)) //  A∧C, A → B ⊢ B∧C
        val s010 = inferenceRuleImplR(s009, (f001, f002))
                                                    //  A → B ⊢ A∧C → B∧C
        s010
      }
    )


  //  ----------------
  //  A → B ⊢ A∨C → B∨C

  val inferenceRuleRool3 =
    derivedInferenceRule("Rool3",
      (
        a: P_Formula,                               //  A
        b: P_Formula,                               //  B
        c: P_Formula                                //  C
      ) => {
        val f001 = new P_FormulaDisjunction(a, c)   //  A∨C
        val f002 = new P_FormulaDisjunction(b, c)   //  B∨C
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleAxiom(b)            //  B ⊢ B
        val s003 = inferenceRuleAxiom(c)            //  C ⊢ C
        val s004 = inferenceRuleImplL((s001, s002), (a, b))
                                                    //  A, A → B ⊢ B
        val s005 = inferenceRuleOrR1(s004, (b, c))  //  A, A → B ⊢ B∨C
        val s006 = inferenceRuleWeakenL(s003, new P_FormulaImplication(a, b))
                                                    //  C, A → B ⊢ C
        val s007 = inferenceRuleOrR2(s006, (b, c))  //  C, A → B ⊢ B∨C
        val s008 = inferenceRuleOrL((s005, s007), (a, c))
                                                    //  A∨C, A → B ⊢ B∨C
        val s009 = inferenceRuleImplR(s008, (f001, f002))
                                                    //  A → B ⊢ A∨C → B∨C
        s009
      }
    )


  //  ---------------------
  //  A → B ⊢ (C → A) → (C → B)

  val inferenceRuleRool4 =
    derivedInferenceRule("Rool4",
      (
        a: P_Formula,                               //  A
        b: P_Formula,                               //  B
        c: P_Formula                                //  C
      ) => {
        val f001 = new P_FormulaImplication(c, a)   //  C → A
        val f002 = new P_FormulaImplication(c, b)   //  C → B
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleAxiom(b)            //  B ⊢ B
        val s003 = inferenceRuleAxiom(c)            //  C ⊢ C
        val s004 = inferenceRuleImplL((s003, s001), (c, a))
                                                    //  C → A, C ⊢ A
        val s005 = inferenceRuleImplL((s001, s002), (a, b))
                                                    //  A → B, A ⊢ B
        val s006 = inferenceRuleCut((s004, s005), a)
                                                    //  C → A, A → B, C ⊢  B
        val s007 = inferenceRuleImplR(s006, (c, b)) //  C → A, A → B ⊢ C → B
        val s008 = inferenceRuleImplR(s007, (f001, f002))
                                                    //  A → B ⊢ (C → A) → (C → B)
        s008
      }
    )


  //  ---------------------
  //  A → B ⊢ ¬B → ¬A

  val inferenceRuleRool5 =
    derivedInferenceRule("Rool5",
      (
        a: P_Formula,                               //  A
        b: P_Formula                                //  B
      ) => {
        val f001 = new P_FormulaNegation(a)         //  ¬ A
        val f002 = new P_FormulaNegation(b)         //  ¬ B
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleAxiom(b)            //  B ⊢ B
        val s003 = inferenceRuleImplL((s001, s002), (a, b))
                                                    //  A → B, A ⊢ B
        val s004 = inferenceRuleNotR(s003, a)       //  A → B ⊢ ¬A, B
        val s005 = inferenceRuleNotL(s004, b)       //  A → B, ¬B ⊢ ¬A
        val s006 = inferenceRuleImplR(s005, (f002, f001))
                                                    //  A → B ⊢ ¬B → ¬A
        s006
      }
    )


  //  ----------------
  //  A → B ⊢ ∀xA → ∀yB

  val inferenceRuleRool6 =
    derivedInferenceRule("Rool6",
      (
        a: P_Formula,                               //  A
        b: P_Formula,                               //  B
        c: P_Formula,                               //  ∀xA
        d: P_Formula                                //  ∀yB
      ) => {
//      val f001 = new P_FormulaNegation(a)         //  ¬ A
//      val f002 = new P_FormulaNegation(b)         //  ¬ B
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleAxiom(b)            //  B ⊢ B
        val s003 = inferenceRuleImplL((s001, s002), (a, b))
                                                    //  A → B, A ⊢ B
        val s004 = inferenceRuleUnivL(s003, (a, c)) //  A → B, ∀xA ⊢ B
        s004.d
        val s005 = inferenceRuleUnivR(s004, (b, d)) //  A → B, ∀xA ⊢ ∀yB
        s005.d
        val s006 = inferenceRuleImplR(s005, (c, d)) //  A → B ⊢ ∀xA → ∀yB
        s006
      }
    )


  //  ----------------
  //  A → B ⊢ ∃xA → ∃yB

  val inferenceRuleRool7 =
    derivedInferenceRule("Rool7",
      (
        a: P_Formula,                               //  A
        b: P_Formula,                               //  B
        c: P_Formula,                               //  ∃xA
        d: P_Formula                                //  ∃yB
      ) => {
//      val f001 = new P_FormulaNegation(a)         //  ¬ A
//      val f002 = new P_FormulaNegation(b)         //  ¬ B
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleAxiom(b)            //  B ⊢ B
        val s003 = inferenceRuleImplL((s001, s002), (a, b))
                                                    //  A → B, A ⊢ B
        val s004 = inferenceRuleExistL(s003, (a, c))
                                                    //  A → B, ∃xA ⊢ B
        s004.d
        val s005 = inferenceRuleExistR(s004, (b, d))
                                                    //  A → B, ∃xA ⊢ ∃yB
        s005.d
        val s006 = inferenceRuleImplR(s005, (c, d)) //  A → B ⊢ ∃xA → ∃yB
        s006
      }
    )


}
