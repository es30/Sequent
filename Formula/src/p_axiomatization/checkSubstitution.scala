package p_axiomatization

import scala.util.control.TailCalls._

import p_term._
import p_formula._

/**
  * Created by EdSnow on 1/7/2017.
  */

object checkSubstitution {

  implicit class P_TermChecker(val template: P_Term) {

    def checkSubstitution(
        candidate: P_Term,
        subst: (P_Term, P_VarName)
      ): Boolean =
      checkSubstitution(None, candidate, subst).result

    def checkSubstitution(
        continuation: Option[() => TailRec[Boolean]],
        candidate: P_Term,
        subst: (P_Term, P_VarName)
      ): TailRec[Boolean] =
      template match {

        case tc: P_TermConstant =>
          val retval = candidate == tc
          tailcall(checkSubstitution_continue(continuation, retval))

        case tv: P_TermVariable =>
          val (substitution, substitutee) = subst
          val retval =
            if (tv.v == substitutee)
              candidate == substitution
            else
              candidate == tv
          tailcall(checkSubstitution_continue(continuation, retval))

        case ta: P_TermApplication =>
          candidate match {
            case t: P_TermApplication if t.f == ta.f =>
              tailcall(ta.a.checkSubstitution(continuation, t.a, subst))
            case _ =>
              done(false)
          }

      }

  }

  implicit class P_ApplicandChecker(val template: P_Applicand) {

    def checkSubstitution(
        candidate: P_Applicand,
        subst: (P_Term, P_VarName)
      ): Boolean =
      tailcall(checkSubstitution(None, candidate, subst)).result

    def checkSubstitution(
        continuation: Option[() => TailRec[Boolean]],
        candidate: P_Applicand,
        subst: (P_Term, P_VarName)
      ): TailRec[Boolean] = {
      val  templateTerms = template.terms
      val candidateTerms = candidate.terms
      val length = templateTerms.length
      if (candidateTerms.length == length) {

        def looper(
            termIndex: Int
          ): TailRec[Boolean] =
          if (termIndex >= length)
            tailcall(checkSubstitution_continue(continuation, true))
          else {
            def cont1: () => TailRec[Boolean] =
              () => tailcall(looper(termIndex + 1))
            tailcall(templateTerms(termIndex).
              checkSubstitution(Some(cont1), candidateTerms(termIndex), subst))
          }

        tailcall(looper(0))

      }
      else
        done(false)
    }

  }

  implicit class P_FormulaChecker(val template: P_Formula) {

    def checkSubstitution(
        candidate: P_Formula,
        subst: (P_Term, P_VarName)
      ): Boolean =
      checkSubstitution(None, candidate, subst).result

    private def checkSubstitution(
        continuation: Option[() => TailRec[Boolean]],
        candidate: P_Formula,
        subst: (P_Term, P_VarName)
      ): TailRec[Boolean] =
      template match {

        case ap: P_FormulaProposition =>
          val retval = candidate == ap
          tailcall(checkSubstitution_continue(continuation, retval))

        case aa: P_FormulaApplication =>
          val retval =
            candidate match {
              case f: P_FormulaApplication if f.p == aa.p =>
                aa.a.checkSubstitution(f.a, subst)
              case _ =>
                false
            }
          tailcall(checkSubstitution_continue(continuation, retval))

        case an: P_FormulaNegation =>
          candidate match {
            case f: P_FormulaNegation =>
              tailcall(an.f1.checkSubstitution(continuation, f.f1, subst))
            case _ =>
              done(false)
          }

        case ad: P_FormulaDyadic =>
          candidate match {
            case f: P_FormulaDyadic if f.kind == ad.kind =>
              def cont1: () => TailRec[Boolean] =
                () => tailcall(ad.f2.checkSubstitution(continuation, f.f2, subst))
              tailcall(ad.f1.checkSubstitution(Some(cont1), f.f1, subst))
            case _ =>
              done(false)
          }

        case aq: P_FormulaQuantification =>
          candidate match {
            case f: P_FormulaQuantification if f.kind == aq.kind && f.v == aq.v =>
              tailcall(aq.f1.checkSubstitution(continuation, f.f1, subst))
            case _ =>
              done(false)
          }

      }

  }

  private def checkSubstitution_continue(
      continuation: Option[() => TailRec[Boolean]],
      retval: Boolean
    ): TailRec[Boolean] =
    if (retval)
      continuation match {
        case Some(cont) =>
          tailcall(cont())
        case None =>
          done(true)
      }
    else
      done(false)

}
