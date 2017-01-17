/**
  * Created by EdSnow on 8/27/2016.
  */


package object derivedInferenceRule {


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
        case Some(x) =>
          ftp(f)(x)
        case none =>
          new Sequent(None)
      }

    }
  )


// ====================================================================


  //  ------------
  //  Theory ⊢ A 

  val inferenceRuleAxiomFromTheory =
    derivedInferenceRule("Axiom",
      (
        theory: P_SFormula,                         //  Theory
        f: P_Formula                                //  A
      ) => {
        val a = f                                   //  A

        val s001 = inferenceRuleAxiom(f)            //  A ⊢ A
        val s002 = inferenceRuleWeakenL(s001, theory)
                                                    //  Theory, A ⊢ A
        val s003 = inferenceRuleContractL(s002, theory, f)
                                                    //  Theory ⊢ A
        s003
      }
    )


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


  //  ---------------
  //  ∀xA ⊢ ∀yA[y/x]

  import p_term._

  val inferenceRuleRool1RUniv =
    derivedInferenceRule("Rool1RUniv",
      (
        a: P_Formula,                               //  A
        b: P_Formula,                               //  A[y/x]
        vars: (P_VarName, P_VarName)                //  (x, y)
      ) => {
        val (x, y) = vars
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleUnivL(s001, a, (new P_TermVariable(x), x))      // !!!
                                                    //  ∀xA ⊢ A
        val s003 = inferenceRuleUnivR(s002, b, (x, y))
                                                    //  ∀xA ⊢ ∀yA[y/x]
        s003
      }
    )


  //  --------------
  //  ∃xA ⊢ ∃yA[y/x]

  val inferenceRuleRool1RExist =
    derivedInferenceRule("Rool1RExist",
      (
        a: P_Formula,                               //  A
        b: P_Formula,                               //  A[y/x]
        vars: (P_VarName, P_VarName)                //  (x, y)
      ) => {
        val (x, y) = vars
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleExistR(s001, b, (new P_TermVariable(x), y))      // !!!
                                                    //  A ⊢ ∃yA[y/x]
        val s003 = inferenceRuleExistL(s002, a, (x, x))
                                                    //  ∃xA ⊢ ∃yA[y/x]
        s003
      }
    )


  //  Γ, A ⊢ B, Δ
  //  ----------------
  //  Γ, C∧A ⊢ C∧B, Δ

  val inferenceRuleRool2L =
    derivedInferenceRule("Rool2L",
      (
        s: P_Sequent,                               //  Γ, A ⊢ B, Δ
        a: P_Formula,                               //  A
        b: P_Formula,                               //  B
        c: P_Formula                                //  C
      ) => {
        val s001 = inferenceRuleAxiom(c)            //  C ⊢ C
        val s002 = inferenceRuleAndL1(s001, (c, a)) //  C∧A ⊢ C
        val s003 = inferenceRuleAndR((s002, s), (c, b))
                                                    //  Γ, C∧A, A ⊢ C∧B, Δ
        val s004 = inferenceRuleAndL2(s003, (c, a)) //  Γ, C∧A, C∧A ⊢ C∧B, Δ
                                                    //  =>  Γ, C∧A ⊢ C∧B, Δ
        s004
      }
    )


  //  Γ, A ⊢ B, Δ
  //  ----------------
  //  Γ, A∧C ⊢ B∧C, Δ

  val inferenceRuleRool2R =
    derivedInferenceRule("Rool2R",
      (
        s: P_Sequent,                               //  Γ, A ⊢ B, Δ
        a: P_Formula,                               //  A
        b: P_Formula,                               //  B
        c: P_Formula                                //  C
      ) => {
        val s001 = inferenceRuleAxiom(c)            //  C ⊢ C
        val s002 = inferenceRuleAndL1(s, (a, c))    //  Γ, A∧C ⊢ B, Δ
        val s003 = inferenceRuleAndR((s002, s001), (b, c))
                                                    //  Γ, A∧C, C ⊢ B∧C, Δ
        val s004 = inferenceRuleAndL2(s003, (a, c)) //  Γ, A∧C, A∧C ⊢ B∧C, Δ
                                                    //  =>  Γ, A∧C ⊢ B∧C, Δ
        s004
      }
    )


  //  Γ, A ⊢ B, Δ
  //  ----------------
  //  Γ, C∨A ⊢ C∨B, Δ

  val inferenceRuleRool3L =
    derivedInferenceRule("Rool3L",
      (
        s: P_Sequent,                               //  Γ, A ⊢ B, Δ
        a: P_Formula,                               //  A
        b: P_Formula,                               //  B
        c: P_Formula                                //  C
      ) => {
        val s001 = inferenceRuleAxiom(c)            //  C ⊢ C
        val s002 = inferenceRuleOrR1(s001, (c, b))  //  C ⊢ C∨B
        val s003 = inferenceRuleOrL((s002, s), (c, a))
                                                    //  Γ, C∨A ⊢ C∨B, B, Δ
        val s004 = inferenceRuleOrR2(s003, (c, b))  //  Γ, C∨A ⊢ C∨B, C∨B, Δ
                                                    //  =>  Γ, C∨A ⊢ C∨B, Δ
        s004
      }
    )


  //  Γ, A ⊢ B, Δ
  //  ----------------
  //  Γ, A∨C ⊢ B∨C, Δ

  val inferenceRuleRool3R =
    derivedInferenceRule("Rool3R",
      (
        s: P_Sequent,                               //  Γ, A ⊢ B, Δ
        a: P_Formula,                               //  A
        b: P_Formula,                               //  B
        c: P_Formula                                //  C
      ) => {
        val s001 = inferenceRuleAxiom(c)            //  C ⊢ C
        val s002 = inferenceRuleOrR1(s, (b, c))     //  Γ, A ⊢ B∨C, Δ
        val s003 = inferenceRuleOrL((s002, s001), (a, c))
                                                    //  Γ, A∨C ⊢ B∨C, C, Δ
        val s004 = inferenceRuleOrR2(s003, (b, c))  //  Γ, A∨C ⊢ B∨C, B∨C, Δ
                                                    //  =>  Γ, A∨C ⊢ B∨C, Δ
        s004
      }
    )


  //  Γ, A ⊢ B, Δ
  //  ------------------
  //  Γ, C → A ⊢ C → B, Δ

  val inferenceRuleXRool4 =
    derivedInferenceRule("Rool4",
      (
        s: P_Sequent,                               //  Γ, A ⊢ B, Δ
        a: P_Formula,                               //  A
        b: P_Formula,                               //  B
        c: P_Formula                                //  C
      ) => {

        val s001 = inferenceRuleAxiom(c)            //  C ⊢ C
        val s002 = inferenceRuleImplL((s001, s), (c, a))
                                                    //  Γ, C, C → A ⊢ B, Δ
        val s003 = inferenceRuleImplR(s002, (c, b)) //  Γ, C → A ⊢ C → B, Δ
        s003
      }
    )


  //  Γ, A ⊢ B, Δ
  //  ------------------
  //  Γ, B → C ⊢ A → C, Δ

  val inferenceRuleYRool4 =
    derivedInferenceRule("Rool4",
      (
        s: P_Sequent,                               //  Γ, A ⊢ B, Δ
        a: P_Formula,                               //  A
        b: P_Formula,                               //  B
        c: P_Formula                                //  C
      ) => {

        val s001 = inferenceRuleAxiom(c)            //  C ⊢ C
        val s002 = inferenceRuleImplL((s, s001), (b, c))
                                                    //  Γ, A, B → C ⊢ C, Δ
        val s003 = inferenceRuleImplR(s002, (a, c)) //  Γ, B → C ⊢ A → C, Δ
        s003
      }
    )


  //  Γ, A ⊢ B, Δ
  //  --------------
  //  Γ, ¬B ⊢ ¬A, Δ

  val inferenceRuleXRool5 =
    derivedInferenceRule("Rool5",
      (
        s: P_Sequent,                               //  Γ, A ⊢ B, Δ
        a: P_Formula,                               //  A
        b: P_Formula                                //  B
      ) => {
        val s001 = inferenceRuleNotR(s, a)          //  Γ ⊢ ¬A, B, Δ
        val s002 = inferenceRuleNotL(s001, b)       //  Γ, ¬B ⊢ ¬A, Δ
        s002
      }
    )


  //  Γ, A ⊢ B, Δ
  //  ----------------
  //  Γ, ∀xA ⊢ ∀yB, Δ

  val inferenceRuleXRool6 =
    derivedInferenceRule("Rool6",
      (
        s: P_Sequent,                               //  Γ, A ⊢ B, Δ
        a: P_Formula,                               //  A
        b: P_Formula,                               //  B
        vars: (P_VarName, P_VarName)                //  (x, y)
      ) => {
        val (x, y) = vars
        val s001 = inferenceRuleUnivL(s, a, (new P_TermVariable(x), x))    // !!!
                                                    //  A → B, ∀xA ⊢ B
        s001.d
        val s002 = inferenceRuleUnivR(s001, b, (y, y))
                                                    //  A → B, ∀xA ⊢ ∀yB
        s002.d
        s002
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
        d: P_Formula,                               //  ∀yB
        vars: (P_VarName, P_VarName)                //  (x, y)
      ) => {
        val (x, y) = vars
//      val f001 = new P_FormulaNegation(a)         //  ¬ A
//      val f002 = new P_FormulaNegation(b)         //  ¬ B
        val s001 = inferenceRuleAxiom(a)            //  A ⊢ A
        val s002 = inferenceRuleAxiom(b)            //  B ⊢ B
        val s003 = inferenceRuleImplL((s001, s002), (a, b))
                                                    //  A → B, A ⊢ B
        val s004 = inferenceRuleUnivL(s003, a, (new P_TermVariable(x), x))    // !!!
                                                    //  A → B, ∀xA ⊢ B
        s004.d
        val s005 = inferenceRuleUnivR(s004, b, (y, y))
                                                    //  A → B, ∀xA ⊢ ∀yB
        s005.d
        val s006 = inferenceRuleImplR(s005, (c, d)) //  A → B ⊢ ∀xA → ∀yB
        s006
      }
    )

/*
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
*/


}
