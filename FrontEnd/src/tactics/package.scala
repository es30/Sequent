/**
  * Created by EdSnow on 2/10/2017.
  */


package object tactics {


  import scala.annotation.tailrec
  import scala.collection.mutable

  import shapeless.HList
  import shapeless.ops.function.{FnFromProduct, FnToProduct}

  import p_term._
  import p_formula._
  import p_formula.P_Formula.P_SFormulaWrapper

  import sequent._
  import p_sequent._
  import inferenceRuleWrapper._
  import foundationalInferenceRule._
  import derivedInferenceRule._

  import parseStuff._
  import axiomatization_Eq.theorems._


  private def xxx_derivedInferenceRule[Ta <: HList, T <: HList, F](name: String, f: F)
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


  //
  //  Term/Product Replacement Experiment
  //

  import parseStuff.predicateIsEqualTo
  import diff.diff._
  import diff._

  private val var_x = v("x")
  private val var_y = v("y")

  private val v_x = P_TermVariable(var_x)
  private val v_y = P_TermVariable(var_y)

  private def formulaEquation(
      t: P_Term,
      u: P_Term
    ): P_Formula = {
    val terms = Array(t, u)
    P_FormulaApplication(predicateIsEqualTo, P_Applicand(terms))
                                                    //  t=u
  }


  private def updated(
      a: P_Applicand,
      t: P_Term,
      termIndex: Int
    ): P_Applicand = {
    val newA = P_Applicand(a.terms.clone)
    newA.terms(termIndex) = t
    newA
  }

  private def xform001(
      t: P_Term,                        //  t
      u: P_Term,                        //  u
      a: P_Formula                      //  A
    ): P_Formula = {
    val f001 = formulaEquation(t, u)                //  t=u
    val f002 = P_FormulaImplication(f001, a)        //  t=u → A
    f002
  }

  type Transformer = (P_Applicand, P_Applicand) => P_Formula

  private def xform002T(
      f: P_Function                     //  f
    ): Transformer =
    (
      at: P_Applicand,                  //  (…t…}
      au: P_Applicand                   //  (…u…}
    ) => {
      val t001 = P_TermApplication(f, at)           //  f(…t…}
      val t002 = P_TermApplication(f, au)           //  f(…u…}
      val f001 = formulaEquation(t001, t002)        //  f(…t…}=f(…u…}
      f001
    }

  private def xform002F(
      p: P_Predicate                    //  P
    ): Transformer =
    (
      at: P_Applicand,                  //  (…t…}
      au: P_Applicand                   //  (…u…}
    ) => {
      val f001 = P_FormulaApplication(p, at)        //  P(…t…}
      val f002 = P_FormulaApplication(p, au)        //  P(…u…}
      val f003 = P_FormulaImplication(f001, f002)   //  P(…t…} → P(…u…}
      f003
    }

  private def xform004(
      s: Sequent,                       //  Γ ⊢ t=u, Δ
      t: P_Term,                        //  t
      u: P_Term,                        //  u
      at: P_Applicand,                  //  (…t…}
      au: P_Applicand,                  //  (…u…}
      termIndex: Int,
      xformer: Transformer
  ) = {
    val length = at.terms.length
    val aLHS = at                                   //  (…t…}
    val aRHS = updated(au, v_y, termIndex)          //  (…y…}
    val f101 = formulaEquation(t, u)                //  t=u
    val f102 = xformer(at, au)                      //  f(…t…}=f(…u…}
                                                    //  or
                                                    //  P(…t…} → P(…u…}
    val f003 = P_FormulaImplication(f101, f102)     //  t=u → f(…t…}=f(…u…}
                                                    //  or
                                                    //  t=u → (P(…t…} → P(…u…})
    val f004 = xformer(aLHS, aRHS)                  //  f(…t…}=f(…y…}
                                                    //  or
                                                    //  P(…t…} → P(…y…}
    val f005 = xform001(t, v_y, f004)               //  t=y → f(…t…}=f(…y…}
                                                    //  or
                                                    //  t=y → (P(…t…} → P(…y…})
    val f006 = P_FormulaQuantificationUniversal(var_y, f005)
                                                    //  ∀y{t=y → f(…t…}=f(…y…)}
                                                    //  or
                                                    //  ∀y{t=y → (P(…t…} → P(…y…})}
    val s001 = inferenceRuleAxiom(f003)             //  t=u → f(…t…}=f(…u…} ⊢
                                                    //    t=u → f(…t…}=f(…u…}
                                                    //  or
                                                    //  t=u → (P(…t…} → P(…u…}) ⊢
                                                    //    t=u → (P(…t…} → P(…u…})
    val s002 = inferenceRuleUnivL(s001, f005, (u, var_y))
                                                    //  ∀y{t=y → f(…t…}=f(…y…}} ⊢
                                                    //    t=u → f(…t…}=f(…u…}
                                                    //  or
                                                    //  ∀y{t=y → (P(…t…} → P(…y…})} ⊢
                                                    //    t=u → (P(…t…} → P(…u…})

    @tailrec def looper(
        varIndex: Int,
        varsPrefix: mutable.LinearSeq[P_VarName],
        aLHS: P_Applicand,
        aRHS: P_Applicand,
        s: Sequent,
        f: P_Formula,
        lastTI: Int                     //  iterated term index
      ): (Sequent, P_Formula) = {
      if (lastTI <= 0)
        (s, f)
      else {
        val localTI = lastTI - 1
        val tx = if (localTI <= termIndex) v_x else t
        val tReplaced = at.terms(localTI)
        val (newVarIndex, varUniv) =
          if (localTI == termIndex) (
            varIndex,                   //  newVarIndex
            var_x                       //  varUniv
          )
          else (
            varIndex + 1,               //  newVarIndex
            v("x" + varIndex.toString)  //  varUniv
          )
        val (varLHS, varRHS) =
          if (localTI == termIndex)
            (v_x, v_y)
          else {
            val tVar = P_TermVariable(varUniv)
            (tVar, tVar)
          }
        val newALHS = updated(aLHS, varLHS, localTI)
        val newARHS = updated(aRHS, varRHS, localTI)
        val f101 = xformer(newALHS, newARHS)        //  f(…t…}=f(…y…}
                                                    //  or
                                                    //  P(…t…} → P(…y…}
        val f102 = xform001(tx, v_y, f101)          //  t=y → f(…t…}=f(…y…}
                                                    //  or
                                                    //  t=y → (P(…t…} → P(…y…})
        val f103 = varsPrefix.foldLeft(f102)(       //  f
          (f: P_Formula, v: P_VarName) =>
            P_FormulaQuantificationUniversal(v, f))
        val f104 = P_FormulaQuantificationUniversal(varUniv, f103)
                                                    //  ∀varUniv.f
        val s101 = inferenceRuleAxiom(f)            //  f[tReplaced/varUniv] ⊢
                                                    //    f[tReplaced/varUniv]
        val s102 = inferenceRuleUnivL(s101, f103, (tReplaced, varUniv))
                                                    //  ∀varUniv.f ⊢
                                                    //    f[tReplaced/varUniv]
        val s103 = inferenceRuleCut((s102, s), f)   //  ∀varUniv.f ⊢
                                                    //    t=u → f(…t…}=f(…u…}
                                                    //  or
                                                    //  ∀varUniv.f ⊢
                                                    //    t=u → (P(…t…} → P(…u…})
        looper(newVarIndex, varsPrefix :+ varUniv, newALHS, newARHS, s103, f104, localTI)
      }
    }

    val (s003, f007) = looper(0, mutable.LinearSeq(var_y), aLHS, aRHS, s002, f006, length)
    val s004 = inferenceRuleWeakenL(s003, thEq)
    val s005 = inferenceRuleContractL(s004, thEq, f007)
    val s006 = inferenceRuleRool1b(s005, (f101, f102))
    val s007 = inferenceRuleCut((s, s006), f101)
    s007
  }

  private def swapSides(
      t: P_Term,                        //  t
      u: P_Term                         //  u
    ): Sequent = {
    val f001 = formulaEquation(t, u)                //  t=u
    val f002 = formulaEquation(u, t)                //  u=t
    val f003 = P_FormulaImplication(f001, f002)     //  t=u → u=t
    val f004 = formulaEquation(t, v_y)              //  t=y
    val f005 = formulaEquation(v_y, t)              //  y=t
    val f006 = P_FormulaImplication(f004, f005)     //  t=y → y=t
    val s001 = inferenceRuleAxiom(f003)             //  t=u → u=t ⊢ t=u → u=t
    val s002 = inferenceRuleUnivL(s001, f006, (u, var_y))
                                                    //  ∀y(t=y → y=t) ⊢ t=u → u=t
    val s003 = inferenceRuleUnivL(s002, pf("∀y.(x=y → y=x)"), (t, var_y))
                                                    //  ∀x∀y(x=y → y=x) ⊢ t=u → u=t
    val s004 = inferenceRuleCut((thEq2, s003), pf("∀x∀y.(x=y → y=x)"))
                                                    //  Eq ⊢ t=u → u=t
    val s005 = inferenceRuleRool1b(s004, (f001, f002))
                                                    //  Eq, t=u ⊢ u=t
    s005
  }

  @tailrec private def subtract(
      abDiff: List[DiffElement],
      cdDiff: List[DiffElement]
    ): Option[List[DiffElement]] =
    if (abDiff.isEmpty)
      Some(cdDiff)
    else
    if (cdDiff.isEmpty)
      None    //  Bad: abDiff was deeper than cdDiff.
    else {
      val abHead = abDiff.head
      val cdHead = cdDiff.head
      if (abHead.matches(cdHead))
        subtract(abDiff.tail, cdDiff.tail)
      else
        None  //  Bad: List heads don't match.
    }




  @tailrec def wrap(
      s: Sequent,
      cdDiff: List[DiffElement]
    ): Sequent =
    if (cdDiff.isEmpty)
      s
    else {
      val cdHead = cdDiff.head
      val cdTail = cdDiff.tail
      val x = cdHead.wrap(s)
      wrap(x, cdTail)
    }

  @tailrec def wrapx(
      s: Sequent,
      t: P_Term,
      u: P_Term,
      cdDiff: List[DiffElement],
      swap: Boolean
    ): Sequent = {
    val cdDHead = cdDiff.head
    cdDHead match {
      case ta: DiffTermApplication =>
        val next_t = ta.tt
        val next_u = ta.tc
        val sxxx = xform004(s, t, u, next_t.a, next_u.a, ta.termIndex, xform002T(ta.tt.f))
        wrapx(sxxx, next_t, next_u, cdDiff.tail, swap)
      case fa: DiffFormulaApplication =>
        val sxx1 = xform004(s, t, u, fa.ft.a, fa.fc.a, fa.termIndex, xform002F(fa.ft.p))
        val sxx2 = inferenceRuleRool1b(sxx1, (fa.ft, fa.fc))
        wrap(sxx2, cdDiff.tail)
    }
  }

  val tacticReplaceTerm =
    derivedInferenceRule("Replace Term",
      (
        s: P_Sequent,                               //  Γ ⊢ t=u, Δ
        tu: P_Formula,                              //  t=u
        cd: (P_Formula, P_Formula)                  //  (C, D)
      ) => {

        val (t, u) =                                //  (t, u)
          tu match {
            case equation: P_FormulaApplication
                  if equation.p == predicateIsEqualTo &&
                    equation.a.terms.length == 2 =>
              val terms = equation.a.terms
              (terms(0), terms(1))
            case _ =>
              (null, null)
          }

        val (c, d) = cd                             //  (C, D)
        val tuDiff = t.diff(u)
        val cdDiff = c.diff(d)

        (tuDiff, cdDiff) match {
          case (Some(tuD), Some(cdD)) =>
            val x = subtract(tuD, cdD)
            val xx = x.getOrElse(null)

            val (at, ac, termIndex) =
              xx.head match {
                case ta: DiffTermApplication =>
                  (ta.tt.a, ta.tc.a, ta.termIndex)
                case fa: DiffFormulaApplication =>
                  (fa.ft.a, fa.fc.a, fa.termIndex)
                case _ =>
                  null                      //  TODO: Fix!!
              }
            val swap = t == ac.terms(termIndex)
            wrapx(s, t, u, xx, swap)        //  TODO: Fix!!!
          case _ =>
            null
        }

/*
        eq match {
          case equation: P_FormulaApplication
              if equation.p == predicateIsEqualTo && equation.a.terms.length == 2 =>
            val terms = equation.a.terms
            val t = terms(0)
            val u = terms(1)
            val termDiff = t.diff(u)
            val formDiff = a.diff(b)


            null
          case _ =>
            null
        }
*/

      }
    )


  val tacticReplaceFormula =
    derivedInferenceRule("Replace Formula",
      (
        s: P_Sequent,                               //  Γ, A ⊢ B, Δ
        ab: (P_Formula, P_Formula),                 //  (A, B)
        cd: (P_Formula, P_Formula)                  //  (C, D)
      ) => {

        val (a, b) = ab                             //  (A, B)
        val (c, d) = cd                             //  (C, D)
        val abDiff = a.diff(b)
        val cdDiff = c.diff(d)

        (abDiff, cdDiff) match {
          case (Some(abD), Some(cdD)) =>
            val x = subtract(abD, cdD)
            val xx = x.getOrElse(null)
            wrap(s, xx)                     //  TODO: Fix!!!
          case _ =>
            null
        }

      }

    )


}
