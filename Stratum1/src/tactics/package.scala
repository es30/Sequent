/**
  * Created by EdSnow on 2/10/2017.
  */


package object tactics {


  import scala.annotation.tailrec

  import shapeless.HList
  import shapeless.ops.function.{FnFromProduct, FnToProduct}

  import p_term._
  import p_formula._
  import p_formula.P_Formula.P_SFormulaWrapper

  import sequent._
  import p_sequent._
  import foundationalInferenceRule._
  import derivedInferenceRule._

  import parseStuff._
  import theorems_Eq.theorems._
  import tactics.walker.SynVarIndex


  //
  //  Term/Product Replacement Experiment
  //

  import parseStuff.predicateIsEqualTo
  import diff.diff._
  import diff._

  val var_x = v("x")
  val var_y = v("y")

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
      t01: P_Term,                      //  t
      u01: P_Term,                      //  u
      at01: P_Applicand,                //  (…t…}
      au01: P_Applicand,                //  (…u…}
      termIndex: Int,
      xformer: Transformer
    ) = {
    val length = at01.terms.length
    val f001 = formulaEquation(t01, u01)            //  t=u
    val f002 = xformer(at01, au01)                  //  f(…t…}=f(…u…}
                                                    //  or
                                                    //  P(…t…} → P(…u…}
    val f003 = P_FormulaImplication(f001, f002)     //  t=u → f(…t…}=f(…u…}
                                                    //  or
                                                    //  t=u → (P(…t…} → P(…u…})
    val s001 = inferenceRuleAxiom(f003)             //  t=u → f(…t…}=f(…u…} ⊢
                                                    //    t=u → f(…t…}=f(…u…}
                                                    //  or
                                                    //  t=u → (P(…t…} → P(…u…}) ⊢
                                                    //    t=u → (P(…t…} → P(…u…})

    val termsT = at01.terms
    val termsU = au01.terms
    val isVulnX = termsT(termIndex) != v_x
    val isVulnY = termsU(termIndex) != v_y

    @tailrec def extractVars(
        loopIndex: Int,
        vars: Set[SynVarIndex],
        firstGap: SynVarIndex,
        varList1: List[(P_VarName, Int)],
        varList2: List[P_VarName],
        mustReplaceX: Boolean,
        mustReplaceY: Boolean
      ): (List[(P_VarName, Int)], List[P_VarName]) = {
      lazy val allocVar = v("x" + firstGap.toString)
      if (loopIndex >= length) {
        val newVarList1 =
          if (mustReplaceY)
            (allocVar, loopIndex) :: varList1
          else
            varList1
        (newVarList1, var_y :: varList2)
      }
      else {
        val useAllocVar: Boolean =
          if (loopIndex == termIndex)
            mustReplaceX
          else
            true
        val adjFirstGap =
          if (useAllocVar)
            firstGap + 1
          else
            firstGap
        val newVarList2Head: P_VarName =
          if (loopIndex == termIndex)
            var_x
          else
            allocVar
        val newVarList2 = newVarList2Head :: varList2
        import tactics.walker.P_Term_walker
        val (newVars, newFirstGap, occursX, occursY) =
          termsT(loopIndex).walk(vars, adjFirstGap)
        val blocksX = occursX && isVulnX && loopIndex < termIndex
        val newMustReplaceX = mustReplaceX || blocksX
        val blocksY = occursY && isVulnY
        val newMustReplaceY = mustReplaceY || blocksY
        val replace =
          if (loopIndex == termIndex)
            mustReplaceX
          else
            blocksX || blocksY
        val newVarList1 =
          if (replace)
            (allocVar, loopIndex) :: varList1
          else
            varList1
        extractVars(loopIndex + 1, newVars, newFirstGap,
          newVarList1, newVarList2, newMustReplaceX, newMustReplaceY)
      }
    }

    val (varList1, varList2) =
      extractVars(0, Set(), 0, List(), List(), false, false)

    @tailrec def looper1(
        zList: List[(P_VarName, Int)],
        s: Sequent,                     //  ...
        t: P_Term,                      //  t
        u: P_Term,                      //  u
        at: P_Applicand,                //  (…t…}
        au: P_Applicand                 //  (…u…}
      ): (Sequent, P_Term, P_Term, P_Applicand, P_Applicand) =
      zList match {
        case Nil =>
          (s, t, u, at, au)
        case (zz, loopIndex) :: xs =>
          val zzv = P_TermVariable(zz)
          val (replacee, newT, newU, newAt, newAu) =
            loopIndex match {
              case i if i == termIndex =>
                val replacee = at.terms(termIndex)
                val newAt = updated(at, zzv, termIndex)
                (replacee, zzv, u, newAt, au)
              case i if i >= length =>
                val replacee = au.terms(termIndex)
                val newAu = updated(au, zzv, termIndex)
                (replacee, t, zzv, at, newAu)
              case _ =>
                val replacee = at.terms(loopIndex)
                val newAt = updated(at, zzv, loopIndex)
                val newAu = updated(au, zzv, loopIndex)
                (replacee, t, u, newAt, newAu)
            }
          val f001 = formulaEquation(newT, newU)    //  t'=u'
          val f002 = xformer(newAt, newAu)          //  f(…t'…}=f(…u'…}
                                                    //  or
                                                    //  P(…t'…} → P(…u'…}
          val f003 = P_FormulaImplication(f001, f002)
                                                    //  t'=u' → f(…t'…}=f(…u'…}
                                                    //  or
                                                    //  t'=u' → (P(…t'…} → P(…u'…})

          @tailrec def prefix(
              zList: List[(P_VarName, Int)],
              f: P_Formula
            ): P_Formula =
            zList match {
              case (varName, idx) :: xs if idx > loopIndex =>
                val newF = P_FormulaQuantificationUniversal(varName, f)
                prefix(xs, newF)
              case _ =>
                f
            }


          val f004 = prefix(varList1, f003)
          val newS = inferenceRuleUnivL(s, f004, (replacee, zz))
                                                    //  ∀... ⊢ ...
          looper1(xs, newS, newT, newU, newAt, newAu)
      }

    val secondStageUnneeded = varList1.isEmpty
    val (s002, t02, u02, at02, au02) =
      if (secondStageUnneeded)
        (s001, t01, u01, at01, au01)
      else
        looper1(varList1, s001, t01, u01, at01, au01)

    val f201 = formulaEquation(t02, u02)            //  t=u
    val f202 = xformer(at02, au02)                  //  f(…t…}=f(…u…}
                                                    //  or
                                                    //  P(…t…} → P(…u…}
    val f203 = P_FormulaImplication(f201, f202)     //  t=u → f(…t…}=f(…u…}
                                                    //  or
                                                    //  t=u → (P(…t…} → P(…u…})
    val s201 = inferenceRuleAxiom(f203)             //  t=u → f(…t…}=f(…u…} ⊢
                                                    //    t=u → f(…t…}=f(…u…}
                                                    //  or
                                                    //  t=u → (P(…t…} → P(…u…}) ⊢
                                                    //    t=u → (P(…t…} → P(…u…})

    @tailrec def looper2(
        zList: List[P_VarName],
        loopIndex: Int,
        s: Sequent,                     //  ...
        t: P_Term,                      //  t
        u: P_Term,                      //  u
        at: P_Applicand,                //  (…t…}
        au: P_Applicand,                //  (…u…}
        lastF: P_Formula,
        firstVar: P_VarName
      ): (Sequent, P_Term, P_Term, P_Applicand, P_Applicand,
          P_Formula, P_VarName) =
      zList match {
        case Nil =>
          (s, t, u, at, au, lastF, firstVar)
        case zz :: xs =>
          val zzv = P_TermVariable(zz)
          val (replacee, newT, newU, newAt, newAu) =
            loopIndex match {
              case i if i == termIndex =>
                val replacee = at.terms(termIndex)
                val newAt = updated(at, zzv, termIndex)
                (replacee, zzv, u, newAt, au)
              case i if i >= length =>
                val replacee = au.terms(termIndex)
                val newAu = updated(au, zzv, termIndex)
                (replacee, t, zzv, at, newAu)
              case _ =>
                val replacee = at.terms(loopIndex)
                val newAt = updated(at, zzv, loopIndex)
                val newAu = updated(au, zzv, loopIndex)
                (replacee, t, u, newAt, newAu)
            }
          val f001 = formulaEquation(newT, newU)    //  t'=u'
          val f002 = xformer(newAt, newAu)          //  f(…t'…}=f(…u'…}
                                                    //  or
                                                    //  P(…t'…} → P(…u'…}
          val f003 = P_FormulaImplication(f001, f002)
                                                    //  t'=u' → f(…t'…}=f(…u'…}
                                                    //  or
                                                    //  t'=u' → (P(…t'…} → P(…u'…})

          @tailrec def prefix(
              zList: List[P_VarName],
              idx: Int,
              f: P_Formula
            ): P_Formula =
            zList match {
              case varName :: xs if idx > loopIndex =>
                val newF = P_FormulaQuantificationUniversal(varName, f)
                prefix(xs, idx - 1, newF)
              case _ =>
                f
            }

          val f004 = prefix(varList2, length, f003)
          val newS = inferenceRuleUnivL(s, f004, (replacee, zz))
                                                    //  ∀... ⊢ ...
          looper2(xs, loopIndex - 1, newS, newT, newU, newAt, newAu, f004, zz)
      }

    val (s401, t03, u03, at03, au03, lastF, firstVar) =
      looper2(varList2, length, s201, t02, u02, at02, au02, f203, var_y)
    val f401 = P_FormulaQuantificationUniversal(firstVar, lastF)
    val s402 = inferenceRuleWeakenL(s401, thEq)
    val s403 = inferenceRuleContractL(s402, thEq, f401)

    @tailrec def looper3(
        zList: List[(P_VarName, Int)],
        s: Sequent,                     //  ...
        f: P_Formula
      ): (Sequent, P_Formula) =
      zList match {
        case Nil =>
          (s, f)
        case (zz, _) :: xs =>
          val newS = inferenceRuleUnivR(s, f, (zz, zz))
                                                    //  ∀... ⊢ ...
          val newF = P_FormulaQuantificationUniversal(zz, f)
          looper3(xs, newS, newF)
      }

    val s660 =
      if (secondStageUnneeded)
        s403
      else {
        val f601 = formulaEquation(t02, u02)        //  t=u
        val f602 = xformer(at02, au02)              //  f(…t…}=f(…u…}
                                                    //  or
                                                    //  P(…t…} → P(…u…}
        val f603 = P_FormulaImplication(f601, f602) //  t=u → f(…t…}=f(…u…}
                                                    //  or
                                                    //  t=u → (P(…t…} → P(…u…})
        val (s501, fxxx) = looper3(varList1, s403, f603)
        val s555 = inferenceRuleCut((s501, s002), fxxx)
        s555
      }
    val s661 = inferenceRuleRool1b(s660, (f001, f002))
    val s666 = inferenceRuleCut((s, s661), f001)
    s666
  }

  private def swapSides(
      s: Sequent,                       //  Γ, u=t ⊢ Δ
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
    val s003 = inferenceRuleUnivL(s002, pf("∀y.(x=y → y=x)"), (t, var_x))
                                                    //  ∀x∀y(x=y → y=x) ⊢ t=u → u=t
    val s004 = inferenceRuleCut((thEq2, s003), pf("∀x∀y.(x=y → y=x)"))
                                                    //  Eq ⊢ t=u → u=t
    val s005 = inferenceRuleRool1b(s004, (f001, f002))
                                                    //  Eq, t=u ⊢ u=t
    val s006 = inferenceRuleCut((s, s005), f001)    //  Eq, Γ ⊢ Δ
    s006
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
        val (next_t, next_u) =
          if (swap)
            (ta.tc, ta.tt)
          else
            (ta.tt, ta.tc)
        val sxxx = xform004(s, t, u, next_t.a, next_u.a, ta.termIndex, xform002T(ta.tt.f))
        wrapx(sxxx, next_t, next_u, cdDiff.tail, swap)
      case fa: DiffFormulaApplication =>
        val (rect_s, rect_t, rect_u) =
          if (swap == fa.swap)
            (s, t, u)
          else
            (swapSides(s, t, u), u, t)
        val sxx1 = xform004(rect_s, rect_t, rect_u,
            fa.ft.a, fa.fc.a, fa.termIndex, xform002F(fa.ft.p))
        val sxx2 = inferenceRuleRool1b(sxx1, (fa.ft, fa.fc))
        wrap(sxx2, cdDiff.tail)
    }
  }

  val tacticReplaceTerm =
    derivedInferenceRule("Replace Term",
      (
        s: P_Sequent,                               //  Γ ⊢ t=u, Δ
        tu: (P_Term, P_Term),                       //  (t, u)
        cd: (P_Formula, P_Formula)                  //  (C, D)
      ) => {

        val (t, u) = tu                             //  (t, u)
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
