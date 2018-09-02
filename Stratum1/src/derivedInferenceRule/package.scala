/**
  * Created by EdSnow on 8/27/2016.
  */


package object derivedInferenceRule {


  import shapeless.HList
  import shapeless.ops.function.{FnFromProduct, FnToProduct}

  import p_formula._
  import p_formula.P_Formula.P_SFormulaWrapper

  import sequent._
  import p_sequent._
  import inferenceRuleWrapper._
  import foundationalInferenceRule._


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
        case Some(x) =>
          ftp(f)(x)
        case none =>
          new Sequent(None)
      }

    }
  )


// ====================================================================


  //  -----------
  //  Theory ⊢ A 

  val inferenceRuleAxiomFromTheory =
    derivedInferenceRule("Axiom",
      (
        theory: P_SFormula,             //  Theory
        f: P_Formula                    //  A
      ) => {
        val a = f                                   //  A

        val s001 = inferenceRuleAxiom(f)            //  A ⊢ A
        val s002 = inferenceRuleWeakenL(s001, theory)
                                                    //  Theory, A ⊢ A
        val s003 = inferenceRuleContractL(s002, theory, f)
                                                    //  Theory ⊢ A
        s003
      }
    )


  //  Γ, A ⊢ B, Δ
  //  ----------- (→R)
  //  Γ ⊢ A → B, Δ


  //  Γ ⊢ A → B, Δ
  //  -----------
  //  Γ, A ⊢ B, Δ

  val inferenceRuleRool1b =
    derivedInferenceRule("Rool1b",
      (
        s: P_Sequent,                   //  Γ ⊢ A → B, Δ
        f: P_FormPair                   //  (A, B)
      ) => {
        val s1 = s                  //  must conform to Γ ⊢ A → B, Δ
        val (a, b) = f                              //  (A, B)

        val f001 = P_FormulaImplication(a, b)       //  A → B
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleAxiom(b)            //  B ⊢ B
        val s003 = inferenceRuleImplL((s001, s002), (a, b))
                                                    //  A, A → B ⊢ B
        val s004 = inferenceRuleCut((s1, s003), f001)
                                                    //  Γ, A ⊢ B, Δ
        s004
      }
    )


  //  -----------
  //  A → B, A ⊢ B

  val inferenceRuleRool1cc1 =
    derivedInferenceRule("Rool1cc1",
      (
        a: P_Formula,                   //  A
        b: P_Formula                    //  B
      ) => {
        val f001 = P_FormulaImplication(a, b)       //  A → B
        val s001 = inferenceRuleAxiom(f001)         //  A → B ⊢ A → B
        val s002 = inferenceRuleRool1b(s001, (a, b))
                                                    //  A → B, A ⊢ B
        s002
      }
    )


  //  ------------
  //  A → B ⊢ ¬A∨B

  val inferenceRuleRool1cc =
    derivedInferenceRule("Rool1cc",
      (
        a: P_Formula,                   //  A
        b: P_Formula                    //  B
      ) => {
        val f001 = P_FormulaNegation(a)             //  ¬A
        val s001 = inferenceRuleRool1cc1(a, b)      //  A → B, A ⊢ B
        val s002 = inferenceRuleNotR(s001, a)       //  A → B ⊢ ¬A, B
        val s003 = inferenceRuleOrR1(s002, (f001, b))
                                                    //  A → B ⊢ ¬A∨B, B
        val s004 = inferenceRuleOrR2(s003, (f001, b))
                                                    //  A → B ⊢ ¬A∨B, ¬A∨B
                                                    //  =>  A → B ⊢ ¬A∨B
        s004
      }
    )


  //  ------------
  //  ¬A∨B ⊢ A → B

  val inferenceRuleRool1dd =
    derivedInferenceRule("Rool1dd",
      (
        a: P_Formula,                   //  A
        b: P_Formula                    //  B
      ) => {
        val f001 = P_FormulaNegation(a)             //  ¬A
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleAxiom(b)            //  B ⊢ B
        val s003 = inferenceRuleNotL(s001, a)       //  A, ¬A ⊢
        val s004 = inferenceRuleOrL((s003, s002), (f001, b))
                                                    //  A, ¬A∨B ⊢ B
        val s005 = inferenceRuleImplR(s004, (a, b))
                                                    //  ¬A∨B ⊢ A → B
        s005
      }
    )


  //  ---------------------
  //  (A → B)∧(B → C) ⊢ A → C

  val inferenceRuleRool1eeYYY =
    derivedInferenceRule("Rool1ee",
      (
        a: P_Formula,                   //  A
        b: P_Formula,                   //  B
        c: P_Formula                    //  C
      ) => {
        val f001 = P_FormulaImplication(a, b)       //  A → B
        val f002 = P_FormulaImplication(b, c)       //  B → C
        val s001 = inferenceRuleRool1cc1(a, b)      //  A → B, A ⊢ B
        val s002 = inferenceRuleRool1cc1(b, c)      //  B → C, B ⊢ C
        val s003 = inferenceRuleCut((s001, s002), b)
                                                    //  A → B, A, B → C ⊢ C
        val s004 = inferenceRuleImplR(s003, (a, c)) //  A → B, B → C ⊢ A → C
        val s005 = inferenceRuleAndL1(s004, (f001, f002))
                                                    //  B → C, (A → B)∧(B → C) ⊢ A → C
        val s006 = inferenceRuleAndL2(s005, (f001, f002))
                                                    //  (A → B)∧(B → C), (A → B)∧(B → C) ⊢ A → C
                                                    //  =>  (A → B)∧(B → C) ⊢ A → C
        s006
      }
    )


  //  ------------
  //  A ↔ B ⊢ A → B

  val inferenceRuleRool1ddx =
    derivedInferenceRule("Rool1ddx",
      (
        a: P_Formula,                   //  A
        b: P_Formula                    //  B
      ) => {
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleAxiom(b)            //  B ⊢ B
        val s003 = inferenceRuleBicondL1((s001, s002), (a, b))
                                                    //  A, A ↔ B ⊢ B
        val s004 = inferenceRuleImplR(s003, (a, b))
                                                    //  A ↔ B ⊢ A → B
        s004
      }
    )


  //  ------------
  //  A ↔ B ⊢ B → A

  val inferenceRuleRool1ddy =
    derivedInferenceRule("Rool1ddy",
      (
        a: P_Formula,                   //  A
        b: P_Formula                    //  B
      ) => {
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleAxiom(b)            //  B ⊢ B
        val s003 = inferenceRuleBicondL2((s001, s002), (a, b))
                                                    //  B, A ↔ B ⊢ A
        val s004 = inferenceRuleImplR(s003, (b, a))
                                                    //  A ↔ B ⊢ B → A
        s004
      }
    )


  //  ---------------------
  //  A ↔ B ⊢ (A → B)∧(B → A)

  val inferenceRuleRool1ddxy =
    derivedInferenceRule("Rool1ddxy",
      (
        a: P_Formula,                   //  A
        b: P_Formula                    //  B
      ) => {
        val f001 = P_FormulaImplication(a, b)       //  A → B
        val f002 = P_FormulaImplication(b, a)       //  B → A
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleAxiom(b)            //  B ⊢ B
        val s003 = inferenceRuleBicondL1((s001, s002), (a, b))
                                                    //  A, A ↔ B ⊢ B
        val s004 = inferenceRuleBicondL2((s001, s002), (a, b))
                                                    //  B, A ↔ B ⊢ A
        val s005 = inferenceRuleImplR(s003, (a, b))
                                                    //  A ↔ B ⊢ A → B
        val s006 = inferenceRuleImplR(s004, (b, a))
                                                    //  A ↔ B ⊢ B → A
        val s007 = inferenceRuleAndR((s005, s006), (f001, f002))
                                                    //  A ↔ B, A ↔ B ⊢ (A → B)∧(B → A)
                                                    //  =>  A ↔ B ⊢ (A → B)∧(B → A)
        s007
      }
    )


  //  ------------
  //  A ↔ B ⊢ B ↔ A

  val inferenceRuleRool1ddz =
    derivedInferenceRule("Rool1ddz",
      (
        a: P_Formula,                   //  A
        b: P_Formula                    //  B
      ) => {
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleAxiom(b)            //  B ⊢ B
        val s003 = inferenceRuleBicondL1((s001, s002), (a, b))
                                                    //  A, A ↔ B ⊢ B
        val s004 = inferenceRuleBicondL2((s001, s002), (a, b))
                                                    //  B, A ↔ B ⊢ A
        val s005 = inferenceRuleBicondR((s004, s003), (b, a))
                                                    //  A ↔ B, A ↔ B ⊢ B ↔ A
                                                    //  =>  A ↔ B ⊢ B ↔ A
        s005
      }
    )


  //  ---------------------
  //  (A → B)∧(B → A) ⊢ A ↔ B

  val inferenceRuleRool1ee =
    derivedInferenceRule("Rool1ee",
      (
        a: P_Formula,                   //  A
        b: P_Formula                    //  B
      ) => {
        val f001 = P_FormulaImplication(a, b)       //  A → B
        val f002 = P_FormulaImplication(b, a)       //  B → A
        val s001 = inferenceRuleAxiom(f001)         //  A → B ⊢ A → B
        val s002 = inferenceRuleAxiom(f002)         //  B → A ⊢ B → A
        val s003 = inferenceRuleRool1b(s001, (a, b))
                                                    //  A → B, A ⊢ B
        val s004 = inferenceRuleRool1b(s002, (b, a))
                                                    //  B → A, B ⊢ A
        val s005 = inferenceRuleBicondR((s003, s004), (a, b))
                                                    //  A → B, B → A ⊢ A ↔ B
        val s006 = inferenceRuleAndL1(s005, (f001, f002))
                                                    //  B → A, (A → B)∧(B → A) ⊢ A ↔ B
        val s007 = inferenceRuleAndL2(s006, (f001, f002))
                                                    //  (A → B)∧(B → A), (A → B)∧(B → A) ⊢ A ↔ B
                                                    //  =>  (A → B)∧(B → A) ⊢ A ↔ B
        s007
      }
    )


  //  ---------------------
  //  (A ↔ B)∧(B ↔ C) ⊢ A ↔ C

  val inferenceRuleRool1eeZZZ =
    derivedInferenceRule("Rool1ee",
      (
        a: P_Formula,                   //  A
        b: P_Formula,                   //  B
        c: P_Formula                    //  C
      ) => {
        val f001 = P_FormulaImplication(a, b)       //  A → B
        val f002 = P_FormulaImplication(b, c)       //  B → C
        val f003 = P_FormulaConjunction(f001, f002) //  (A → B)∧(B → C)
        val f004 = P_FormulaBiconditional(a, b)     //  A ↔ B
        val f005 = P_FormulaBiconditional(b, c)     //  B ↔ C
        val f006 = P_FormulaImplication(c, b)       //  C → B
        val f007 = P_FormulaImplication(b, a)       //  B → A
        val f008 = P_FormulaConjunction(f006, f007) //  (C → B)∧(B → A)
        val f009 = P_FormulaImplication(a, c)       //  A → C
        val f010 = P_FormulaImplication(c, a)       //  C → A
        val f011 = P_FormulaConjunction(f009, f010) //  (A → C)∧(C → A)
        val s001 = inferenceRuleRool1ddx(a, b)      //  A ↔ B ⊢ A → B
        val s002 = inferenceRuleRool1ddx(b, c)      //  B ↔ C ⊢ B → C
        val s003 = inferenceRuleAndR((s001, s002), (f001, f002))
                                                    //  A ↔ B, B ↔ C ⊢ (A → B)∧(B → C)
        val s004 = inferenceRuleRool1eeYYY(a, b, c) //  (A → B)∧(B → C) ⊢ A → C
        val s005 = inferenceRuleCut((s003, s004), f003)
                                                    //  A ↔ B, B ↔ C ⊢ A → C
        val s006 = inferenceRuleAndL1(s005, (f004, f005))
                                                    //  B ↔ C, (A ↔ B)∧(B ↔ C) ⊢ A → C
        val s007 = inferenceRuleAndL2(s006, (f004, f005))
                                                    //  (A ↔ B)∧(B ↔ C), (A ↔ B)∧(B ↔ C) ⊢ A → C
                                                    //  =>  (A ↔ B)∧(B ↔ C) ⊢ A → C
        val s008 = inferenceRuleRool1ddy(b, c)      //  B ↔ C ⊢ C → B
        val s009 = inferenceRuleRool1ddy(a, b)      //  A ↔ B ⊢ B → A
        val s010 = inferenceRuleAndR((s008, s009), (f006, f007))
                                                    //  B ↔ C, A ↔ B ⊢ (C → B)∧(B → A)
        val s011 = inferenceRuleRool1eeYYY(c, b, a) //  (C → B)∧(B → A) ⊢ C → A
        val s012 = inferenceRuleCut((s010, s011), f008)
                                                    //  B ↔ C, A ↔ B ⊢ C → A
        val s013 = inferenceRuleAndL1(s012, (f004, f005))
                                                    //  B ↔ C, (A ↔ B)∧(B ↔ C) ⊢ C → A
        val s014 = inferenceRuleAndL2(s013, (f004, f005))
                                                    //  (A ↔ B)∧(B ↔ C), (A ↔ B)∧(B ↔ C) ⊢ C → A
                                                    //  =>  (A ↔ B)∧(B ↔ C) ⊢ C → A
        val s015 = inferenceRuleAndR((s007, s014), (f009, f010))
                                                    //  (A ↔ B)∧(B ↔ C), (A ↔ B)∧(B ↔ C) ⊢ (A → C)∧(C → A)
                                                    //  =>  (A ↔ B)∧(B ↔ C) ⊢ (A → C)∧(C → A)
        val s016 = inferenceRuleRool1ee(a, c)       //  (A → C)∧(C → A) ⊢ A ↔ C
        val s017 = inferenceRuleCut((s015, s016), f011)
                                                    //  (A ↔ B)∧(B ↔ C) ⊢ A ↔ C
        s017
      }
    )


  //  Γ, A∧B ⊢ C, Δ
  //  --------------
  //  Γ, A ⊢ B → C, Δ

  val inferenceRuleRool1Xa =
    derivedInferenceRule("Rool1Xa",
      (
        s: P_Sequent,                   //  Γ, A∧B ⊢ C, Δ
        a: P_Formula,                   //  A
        b: P_Formula,                   //  B
        c: P_Formula                    //  C
      ) => {
        val s1 = s                  //  must conform to Γ, A∧B ⊢ C, Δ

        val f001 = P_FormulaConjunction(a, b)       //  A∧B
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleAxiom(b)            //  B ⊢ B
        val s003 = inferenceRuleAndR((s001, s002), (a, b))
                                                    //  A, B ⊢ A∧B
        val s004 = inferenceRuleCut((s003, s), f001)
                                                    //  Γ, A, B ⊢ C, Δ
        val s005 = inferenceRuleImplR(s004, (b, c)) //  Γ, A ⊢ B → C, Δ
        s005
      }
    )


  //  Γ, A ⊢ B → C, Δ
  //  --------------
  //  Γ, A∧B ⊢ C, Δ

  val inferenceRuleRool1Ya =
    derivedInferenceRule("Rool1Ya",
      (
        s: P_Sequent,                   //  Γ, A ⊢ B → C, Δ
        a: P_Formula,                   //  A
        b: P_Formula,                   //  B
        c: P_Formula                    //  C
      ) => {
        val s1 = s                  //  must conform to Γ, A ⊢ B → C, Δ
        val s001 = inferenceRuleRool1b(s1, (b, c))  //  Γ, A, B ⊢ C, Δ
        val s002 = inferenceRuleAndL1(s001, (a, b)) //  Γ, A∧B, B ⊢ C, Δ
        val s003 = inferenceRuleAndL2(s002, (a, b)) //  Γ, A∧B, A∧B ⊢ C, Δ
                                                    //  =>  Γ, A∧B ⊢ C, Δ
        s003
      }
    )


  //  ----------
  //  A∧B ⊢ B∧A

  val inferenceRuleRool1c =
    derivedInferenceRule("Rool1c",
      (
        a: P_Formula,                   //  A
        b: P_Formula                    //  B
      ) => {
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleAxiom(b)            //  B ⊢ B
        val s003 = inferenceRuleAndR((s002, s001), (b, a))
                                                    //  A, B ⊢ B∧A
        val s004 = inferenceRuleAndL1(s003, (a, b)) //  A∧B, B ⊢ B∧A
        val s005 = inferenceRuleAndL2(s004, (a, b)) //  A∧B ⊢ B∧A
        s005
      }
    )


  //  ----------
  //  A∨B ⊢ B∨A

  val inferenceRuleRool1d =
    derivedInferenceRule("Rool1d",
      (
        a: P_Formula,                   //  A
        b: P_Formula                    //  B
      ) => {
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleAxiom(b)            //  B ⊢ B
        val s003 = inferenceRuleOrL((s001, s002), (a, b))
                                                    //  A∨B ⊢ A, B
        val s004 = inferenceRuleOrR2(s003, (b, a))  //  A∨B ⊢ B∨A, B
        val s005 = inferenceRuleOrR1(s004, (b, a))  //  A∨B ⊢ B∨A
        s005
      }
    )


  //  -------------------
  //  (A∧B)∧C ⊢ A∧(B∧C)

  val inferenceRuleRool1e =
    derivedInferenceRule("Rool1e",
      (
        a: P_Formula,                   //  A
        b: P_Formula,                   //  B
        c: P_Formula                    //  C
      ) => {
        val f001 = P_FormulaConjunction(a, b)       //  A∧B
        val f002 = P_FormulaConjunction(b, c)       //  B∧C
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleAxiom(b)            //  B ⊢ B
        val s003 = inferenceRuleAxiom(c)            //  C ⊢ C
        val s004 = inferenceRuleAndR((s002, s003), (b, c))
                                                    //  B, C ⊢ B∧C
        val s005 = inferenceRuleAndR((s001, s004), (a, f002))
                                                    //  A, B, C ⊢ A∧(B∧C)
        val s006 = inferenceRuleAndL1(s005, (a, b)) //  A∧B, B, C ⊢ A∧(B∧C)
        val s007 = inferenceRuleAndL2(s006, (a, b)) //  A∧B, C ⊢ A∧(B∧C)
        val s008 = inferenceRuleAndL1(s007, (f001, c))
                                                    //  (A∧B)∧C, C ⊢ A∧(B∧C)
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
        a: P_Formula,                   //  A
        b: P_Formula,                   //  B
        c: P_Formula                    //  C
      ) => {
        val f001 = P_FormulaDisjunction(a, b)       //  A∨B
        val f002 = P_FormulaDisjunction(b, c)       //  B∨C
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleAxiom(b)            //  B ⊢ B
        val s003 = inferenceRuleAxiom(c)            //  C ⊢ C
        val s004 = inferenceRuleOrL((s001, s002), (a, b))
                                                    //  A∨B ⊢ A, B
        val s005 = inferenceRuleOrL((s004, s003), (f001, c))
                                                    //  (A∨B)∨C ⊢ A, B, C
        val s006 = inferenceRuleOrR1(s005, (b, c))  //  (A∨B)∨C ⊢ A, B∨C, C
        val s007 = inferenceRuleOrR2(s006, (b, c))  //  (A∨B)∨C ⊢ A, B∨C
        val s008 = inferenceRuleOrR1(s007, (a, f002))
                                                    //  (A∨B)∨C ⊢ A∨(B∨C), B∨C
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
        a: P_Formula,                   //  A
        b: P_Formula,                   //  B
        c: P_Formula                    //  C
      ) => {
        val f001 = P_FormulaConjunction(a, b)       //  A∧B
        val f002 = P_FormulaConjunction(b, c)       //  B∧C
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleAxiom(b)            //  B ⊢ B
        val s003 = inferenceRuleAxiom(c)            //  C ⊢ C
        val s004 = inferenceRuleAndR((s001, s002), (a, b))
                                                    //  A, B ⊢ A∧B
        val s005 = inferenceRuleAndR((s004, s003), (f001, c))
                                                    //  A, B, C ⊢ (A∧B)∧C
        val s006 = inferenceRuleAndL1(s005, (b, c)) //  A, B∧C, C ⊢ (A∧B)∧C
        val s007 = inferenceRuleAndL2(s006, (b, c)) //  A, B∧C ⊢ (A∧B)∧C
        val s008 = inferenceRuleAndL1(s007, (a, f002))
                                                    //  A∧(B∧C), B∧C ⊢ (A∧B)∧C
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
        a: P_Formula,                   //  A
        b: P_Formula,                   //  B
        c: P_Formula                    //  C
      ) => {
        val f001 = P_FormulaDisjunction(a, b)       //  A∨B
        val f002 = P_FormulaDisjunction(b, c)       //  B∨C
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleAxiom(b)            //  B ⊢ B
        val s003 = inferenceRuleAxiom(c)            //  C ⊢ C
        val s004 = inferenceRuleOrL((s002, s003), (b, c))
                                                    //  B∨C ⊢ B, C
        val s005 = inferenceRuleOrL((s001, s004), (a, f002))
                                                    //  A∨(B∨C) ⊢ A, B, C
        val s006 = inferenceRuleOrR1(s005, (a, b))  //  A∨(B∨C) ⊢ A∨B, B, C
        val s007 = inferenceRuleOrR2(s006, (a, b))  //  A∨(B∨C) ⊢ A∨B, C
        val s008 = inferenceRuleOrR1(s007, (f001, c))
                                                    //  A∨(B∨C) ⊢ (A∨B)∨C, C
        val s009 = inferenceRuleOrR2(s008, (f001, c))
                                                    //  A∨(B∨C) ⊢ (A∨B)∨C
        s009
      }
    )


  //  ---------------
  //  ¬(A∧B) ⊢ ¬A∨¬B

  val inferenceRuleDeMorgan1A =
    derivedInferenceRule("DeMorgan1A",
      (
        a: P_Formula,                   //  A
        b: P_Formula                    //  B
      ) => {
        val f001 = P_FormulaNegation(a)             //  ¬A
        val f002 = P_FormulaNegation(b)             //  ¬B
        val f003 = P_FormulaConjunction(a, b)       //  A∧B
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleNotR(s001, a)       //  ⊢ ¬A, A
        val s003 = inferenceRuleOrR1(s002, (f001, f002))
                                                    //  ⊢ ¬A∨¬B, A
        val s004 = inferenceRuleAxiom(b)            //  B ⊢ B
        val s005 = inferenceRuleNotR(s004, b)       //  ⊢ ¬B, B
        val s006 = inferenceRuleOrR2(s005, (f001, f002))
                                                    //  ⊢ ¬A∨¬B, B
        val s007 = inferenceRuleAndR((s003, s006), (a, b))
                                                    //  ⊢ A∧B, ¬A∨¬B, ¬A∨¬B
                                                    //  =>  ⊢ A∧B, ¬A∨¬B
        val s008 = inferenceRuleNotL(s007, f003)    //  ⊢ ¬(A∧B) ⊢ ¬A∨¬B
        s008
      }
    )


  //  ---------------
  //  ¬(A∨B) ⊢ ¬A∧¬B

  val inferenceRuleDeMorgan2A =
    derivedInferenceRule("DeMorgan2A",
      (
        a: P_Formula,                   //  A
        b: P_Formula                    //  B
      ) => {
        val f001 = P_FormulaNegation(a)             //  ¬A
        val f002 = P_FormulaNegation(b)             //  ¬B
        val f003 = P_FormulaDisjunction(a, b)       //  A∨B
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleNotR(s001, a)       //  ⊢ ¬A, A
        val s003 = inferenceRuleOrR1(s002, (a, b))  //  ⊢ A∨B, ¬A
        val s004 = inferenceRuleAxiom(b)            //  B ⊢ B
        val s005 = inferenceRuleNotR(s004, b)       //  ⊢ ¬B, B
        val s006 = inferenceRuleOrR2(s005, (a, b))  //  ⊢ A∨B, ¬B
        val s007 = inferenceRuleAndR((s003, s006), (f001, f002))
                                                    //  ⊢ ¬A∧¬B, A∨B, A∨B
                                                    //  =>  ⊢ ¬A∧¬B, A∨B
        val s008 = inferenceRuleNotL(s007, f003)    //  ¬(A∨B) ⊢ ¬A∧¬B
        s008
      }
    )


  //  ---------------
  //  ¬A∧¬B ⊢ ¬(A∨B)

  val inferenceRuleDeMorgan2B =
    derivedInferenceRule("DeMorgan2B",
      (
        a: P_Formula,                   //  A
        b: P_Formula                    //  B
      ) => {
        val f001 = P_FormulaNegation(a)             //  ¬A
        val f002 = P_FormulaNegation(b)             //  ¬B
        val f003 = P_FormulaDisjunction(a, b)       //  A∨B
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleNotL(s001, a)       //  A, ¬A ⊢
        val s003 = inferenceRuleAndL1(s002, (f001, f002))
                                                    //  A, ¬A∧¬B ⊢
        val s004 = inferenceRuleAxiom(b)            //  B ⊢ B
        val s005 = inferenceRuleNotL(s004, b)       //  B, ¬B ⊢
        val s006 = inferenceRuleAndL2(s005, (f001, f002))
                                                    //  B, ¬A∧¬B ⊢
        val s007 = inferenceRuleOrL((s003, s006), (a, b))
                                                    //  ¬A∧¬B, ¬A∧¬B, A∨B ⊢
                                                    //  =>  ¬A∧¬B, A∨B ⊢
        val s008 = inferenceRuleNotR(s007, f003)    //  ¬A∧¬B ⊢ ¬(A∨B)
        s008
      }
    )


  //  ---------------
  //  ¬A∨¬B ⊢ ¬(A∧B)

  val inferenceRuleDeMorgan1B =
    derivedInferenceRule("DeMorgan1B",
      (
        a: P_Formula,                   //  A
        b: P_Formula                    //  B
      ) => {
        val f001 = P_FormulaNegation(a)             //  ¬A
        val f002 = P_FormulaNegation(b)             //  ¬B
        val f003 = P_FormulaConjunction(a, b)       //  A∧B
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleNotL(s001, a)       //  A, ¬A ⊢
        val s003 = inferenceRuleAndL1(s002, (a, b)) //  ¬A, A∧B ⊢
        val s004 = inferenceRuleAxiom(b)            //  B ⊢ B
        val s005 = inferenceRuleNotL(s004, b)       //  B, ¬B ⊢
        val s006 = inferenceRuleAndL2(s005, (a, b)) //  ¬B, A∧B ⊢
        val s007 = inferenceRuleOrL((s003, s006), (f001, f002))
                                                    //  A∧B, A∧B, ¬A∨¬B ⊢
                                                    //  =>  A∧B, ¬A∨¬B ⊢
        val s008 = inferenceRuleNotR(s007, f003)    //  ¬A∨¬B ⊢ ¬(A∧B)
        s008
      }
    )


  //  Γ, A ⊢ B, Δ
  //  ---------------
  //  Γ, C∧A ⊢ C∧B, Δ

  val inferenceRuleRool2L =
    derivedInferenceRule("Rool2L",
      (
        s: P_Sequent,                   //  Γ, A ⊢ B, Δ
        a: P_Formula,                   //  A
        b: P_Formula,                   //  B
        c: P_Formula                    //  C
      ) => {
        val s001 = inferenceRuleAxiom(c)            //  C ⊢ C
        val s002 = inferenceRuleAndL1(s001, (c, a)) //  C∧A ⊢ C
        val s003 = inferenceRuleAndR((s002, s), (c, b))
                                                    //  Γ, C∧A, A ⊢ C∧B, Δ
        val s004 = inferenceRuleAndL2(s003, (c, a)) //  Γ, C∧A, C∧A ⊢ C∧B, Δ
                                                    //  =>  Γ, C∧A ⊢ C∧B, Δ
        s004
      }
    )


  //  Γ, A ⊢ B, Δ
  //  ---------------
  //  Γ, A∧C ⊢ B∧C, Δ

  val inferenceRuleRool2R =
    derivedInferenceRule("Rool2R",
      (
        s: P_Sequent,                   //  Γ, A ⊢ B, Δ
        a: P_Formula,                   //  A
        b: P_Formula,                   //  B
        c: P_Formula                    //  C
      ) => {
        val s001 = inferenceRuleAxiom(c)            //  C ⊢ C
        val s002 = inferenceRuleAndL1(s, (a, c))    //  Γ, A∧C ⊢ B, Δ
        val s003 = inferenceRuleAndR((s002, s001), (b, c))
                                                    //  Γ, A∧C, C ⊢ B∧C, Δ
        val s004 = inferenceRuleAndL2(s003, (a, c)) //  Γ, A∧C, A∧C ⊢ B∧C, Δ
                                                    //  =>  Γ, A∧C ⊢ B∧C, Δ
        s004
      }
    )


  //  Γ, A ⊢ B, Δ
  //  ---------------
  //  Γ, C∨A ⊢ C∨B, Δ

  val inferenceRuleRool3L =
    derivedInferenceRule("Rool3L",
      (
        s: P_Sequent,                   //  Γ, A ⊢ B, Δ
        a: P_Formula,                   //  A
        b: P_Formula,                   //  B
        c: P_Formula                    //  C
      ) => {
        val s001 = inferenceRuleAxiom(c)            //  C ⊢ C
        val s002 = inferenceRuleOrR1(s001, (c, b))  //  C ⊢ C∨B
        val s003 = inferenceRuleOrL((s002, s), (c, a))
                                                    //  Γ, C∨A ⊢ C∨B, B, Δ
        val s004 = inferenceRuleOrR2(s003, (c, b))  //  Γ, C∨A ⊢ C∨B, C∨B, Δ
                                                    //  =>  Γ, C∨A ⊢ C∨B, Δ
        s004
      }
    )


  //  Γ, A ⊢ B, Δ
  //  ---------------
  //  Γ, A∨C ⊢ B∨C, Δ

  val inferenceRuleRool3R =
    derivedInferenceRule("Rool3R",
      (
        s: P_Sequent,                   //  Γ, A ⊢ B, Δ
        a: P_Formula,                   //  A
        b: P_Formula,                   //  B
        c: P_Formula                    //  C
      ) => {
        val s001 = inferenceRuleAxiom(c)            //  C ⊢ C
        val s002 = inferenceRuleOrR1(s, (b, c))     //  Γ, A ⊢ B∨C, Δ
        val s003 = inferenceRuleOrL((s002, s001), (a, c))
                                                    //  Γ, A∨C ⊢ B∨C, C, Δ
        val s004 = inferenceRuleOrR2(s003, (b, c))  //  Γ, A∨C ⊢ B∨C, B∨C, Δ
                                                    //  =>  Γ, A∨C ⊢ B∨C, Δ
        s004
      }
    )


  //  Γ, A ⊢ B, Δ
  //  ----------------
  //  Γ, C → A ⊢ C → B, Δ

  val inferenceRuleXRool4 =
    derivedInferenceRule("XRool4",
      (
        s: P_Sequent,                   //  Γ, A ⊢ B, Δ
        a: P_Formula,                   //  A
        b: P_Formula,                   //  B
        c: P_Formula                    //  C
      ) => {

        val s001 = inferenceRuleAxiom(c)            //  C ⊢ C
        val s002 = inferenceRuleImplL((s001, s), (c, a))
                                                    //  Γ, C, C → A ⊢ B, Δ
        val s003 = inferenceRuleImplR(s002, (c, b)) //  Γ, C → A ⊢ C → B, Δ
        s003
      }
    )


  //  Γ, A ⊢ B, Δ
  //  ----------------
  //  Γ, B → C ⊢ A → C, Δ

  val inferenceRuleYRool4 =
    derivedInferenceRule("YRool4",
      (
        s: P_Sequent,                   //  Γ, A ⊢ B, Δ
        a: P_Formula,                   //  A
        b: P_Formula,                   //  B
        c: P_Formula                    //  C
      ) => {

        val s001 = inferenceRuleAxiom(c)            //  C ⊢ C
        val s002 = inferenceRuleImplL((s, s001), (b, c))
                                                    //  Γ, A, B → C ⊢ C, Δ
        val s003 = inferenceRuleImplR(s002, (a, c)) //  Γ, B → C ⊢ A → C, Δ
        s003
      }
    )


  //  Γ, A ⊢ B, Δ
  //  -------------
  //  Γ, ¬B ⊢ ¬A, Δ

  val inferenceRuleXRool5 =
    derivedInferenceRule("XRool5",
      (
        s: P_Sequent,                   //  Γ, A ⊢ B, Δ
        a: P_Formula,                   //  A
        b: P_Formula                    //  B
      ) => {
        val s001 = inferenceRuleNotR(s, a)          //  Γ ⊢ ¬A, B, Δ
        val s002 = inferenceRuleNotL(s001, b)       //  Γ, ¬B ⊢ ¬A, Δ
        s002
      }
    )


  import p_term._


  //  Γ, A[t/x] ⊢ B[z/y], Δ
  //  ---------------------
  //  Γ, ∀xA ⊢ ∀yB, Δ

  val inferenceRuleXRool6 =
    derivedInferenceRule("XRool6",
      (
        s: P_Sequent,                   //  Γ, A[t/x] ⊢ B[z/y], Δ
        a: P_Formula,                   //  A
        substA: (P_Term, P_VarName),    //  (t, x)
        b: P_Formula,                   //  B
        substB: (P_VarName, P_VarName)  //  (z, y)
      ) => {
        val s001 = inferenceRuleUnivL(s, a, substA) //  Γ, ∀xA ⊢ B[z/y], Δ
        val s002 = inferenceRuleUnivR(s001, b, substB)
                                                    //  Γ, ∀xA ⊢ ∀yB, Δ
        s002
      }
    )


  //  Γ, A[z/x] ⊢ B[t/y], Δ
  //  ---------------------
  //  Γ, ∃xA ⊢ ∃yB, Δ

  val inferenceRuleYRool6 =
    derivedInferenceRule("YRool6",
      (
        s: P_Sequent,                   //  Γ, A[z/x] ⊢ B[t/y], Δ
        a: P_Formula,                   //  A
        substA: (P_VarName, P_VarName), //  (z, x)
        b: P_Formula,                   //  B
        substB: (P_Term, P_VarName)     //  (t, y)
      ) => {
        val s001 = inferenceRuleExistR(s, b, substB)
                                                    //  Γ, A[z/x] ⊢ ∃yB, Δ
        val s002 = inferenceRuleExistL(s001, a, substA)
                                                    //  Γ, ∃xA ⊢ ∃yB, Δ
        s002
      }
    )


  //  Γ, A[t/x] ⊢ B[z/y], Δ
  //  ---------------------
  //  Γ, ∀xA ⊢ ¬∃y¬B, Δ

  val inferenceRuleXRool7 =
    derivedInferenceRule("XRool7",
      (
        s: P_Sequent,                   //  Γ, A[t/x] ⊢ B[z/y], Δ
        a: P_Formula,                   //  A
        substA: (P_Term, P_VarName),    //  (t, x)
        b: (P_Formula, P_Formula),      //  (B[z/y], ¬B)
        substNB: (P_VarName, P_VarName) //  (z, y)
      ) => {
        val (bPresub, nB) = b                       //  (B[z/y], ¬B)
        val (z, y) = substNB                        //  (z, y)
        val f001 = P_FormulaNegation(bPresub)       //  ¬B[z/y]
        val f002 = P_FormulaQuantificationExistential(y, nB)
                                                    //  ∃y¬B
        val s001 = inferenceRuleUnivL(s, a, substA) //  Γ, ∀xA ⊢ B[z/y], Δ
        val s002 = inferenceRuleNotL(s001, bPresub) //  Γ, ∀xA, ¬B[z/y] ⊢ Δ
        val s003 = inferenceRuleExistL(s002, nB, substNB)
                                                    //  Γ, ∀xA, ∃y¬B ⊢ Δ
        val s004 = inferenceRuleNotR(s003, f002)    //  Γ, ∀xA ⊢ ¬∃y¬B, Δ
        s004
      }
    )


  //  Γ, A[z/x] ⊢ B[t/y], Δ
  //  ---------------------
  //  Γ, ∃xA ⊢ ¬∀y¬B, Δ

  val inferenceRuleYRool7 =
    derivedInferenceRule("YRool7",
      (
        s: P_Sequent,                   //  Γ, A[z/x] ⊢ B[t/y], Δ
        a: P_Formula,                   //  A
        substA: (P_VarName, P_VarName), //  (z, x)
        b: (P_Formula, P_Formula),      //  (B[t/y], ¬B)
        substNB: (P_Term, P_VarName)    //  (t, y)
      ) => {
        val (bPresub, nB) = b                       //  (B[t/y], ¬B)
        val (t, y) = substNB                        //  (t, y)
        val f001 = P_FormulaNegation(bPresub)       //  ¬B[t/y]
        val f002 = P_FormulaQuantificationUniversal(y, nB)
                                                    //  ∀y¬B
        val s001 = inferenceRuleNotL(s, bPresub)    //  Γ, A[z/x], ¬B[t/y] ⊢ Δ
        val s002 = inferenceRuleUnivL(s001, nB, substNB)
                                                    //  Γ, A[z/x], ∀y¬B ⊢ Δ
        val s003 = inferenceRuleNotR(s002, f002)    //  Γ, A[z/x] ⊢ ¬∀y¬B, Δ
        val s004 = inferenceRuleExistL(s003, a, substA)
                                                    //  Γ, ∃yA ⊢ ¬∀y¬B, Δ
        s004
      }
    )


  //  Γ, A ⊢ C, Δ    Σ, B ⊢ D, Π
  //  -------------------------
  //  Γ, Σ, A∨B ⊢ C∨D, Δ, Π

  val inferenceRuleRool8conj =
    derivedInferenceRule("Rool8conj",
      (
        s: P_SeqPair,                   //  (Γ, A ⊢ C, Δ; Σ, B ⊢ D, Π)
        f: P_FormQuadruple              //  (A, B, C, D)
      ) => {
        val (s1, s2) = s            //  must conform to Γ, A ⊢ C, Δ
                                    //              and Σ, B ⊢ D, Π
        val (a, b, c, d) = f                        //  (A, B, C, D)
        val s001 = inferenceRuleAndL1(s1, (a, b))   //  Γ, A∧B ⊢ C, Δ
        val s002 = inferenceRuleAndL2(s2, (a, b))   //  Σ, A∧B ⊢ D, Π
        val s003 = inferenceRuleAndR((s001, s002), (c, d))
                                                    //  Γ, Σ, A∧B, A∧B ⊢ C∧D, Δ, Π
                                                    //  =>  Γ, Σ, A∧B ⊢ C∧D, Δ, Π
        s003
      }
    )


  //  Γ, A ⊢ C, Δ    Σ, B ⊢ D, Π
  //  -------------------------
  //  Γ, Σ, A∧B ⊢ C∧D, Δ, Π

  val inferenceRuleRool8disj =
    derivedInferenceRule("Rool8disj",
      (
        s: P_SeqPair,                   //  (Γ, A ⊢ C, Δ; Σ, B ⊢ D, Π)
        f: P_FormQuadruple              //  (A, B, C, D)
      ) => {
        val (s1, s2) = s            //  must conform to Γ, A ⊢ C, Δ
                                    //              and Σ, B ⊢ D, Π
        val (a, b, c, d) = f                        //  (A, B, C, D)
        val s001 = inferenceRuleOrR1(s1, (c, d))    //  Γ, A ⊢ C∨D, Δ
        val s002 = inferenceRuleOrR2(s2, (c, d))    //  Σ, B ⊢ C∨D, Π
        val s003 = inferenceRuleOrL((s001, s002), (a, b))
                                                    //  Γ, Σ, A∨B ⊢ C∨D, C∨D, Δ, Π
                                                    //  =>  Γ, Σ, A∨B ⊢ C∨D, Δ, Π
        s003
      }
    )


}
