package diff

import scala.util.control.TailCalls.{TailRec, done, tailcall}

import p_term._
import p_formula._

import sequent._
import derivedInferenceRule._

/**
  * Created by EdSnow on 1/29/2017.
  */


abstract class DiffElement {

  def matches(e: DiffElement): Boolean

  def wrap(s: Sequent): Sequent

}


trait DiffFormula {

  val swap: Boolean

  def rectify[T](
      template: T,
      candidate: T
    ): (T, T) =
    if (swap)
      (candidate, template)
    else
      (template, candidate)

}


case class DiffTermApplication(
    tt: P_TermApplication,          //  template term
    tc: P_TermApplication,          //  candidate term
    termIndex: Int
) extends DiffElement {

  def matches(e: DiffElement): Boolean =
    e match {
      case eta: DiffTermApplication if tt.f == eta.tt.f =>
        termIndex == eta.termIndex
      case _ =>
        false
    }

  def wrap(s: Sequent): Sequent =
    {println("!!null!!: diff, line 39"); null}

}


case class DiffFormulaApplication(
    override val swap: Boolean,
    ft: P_FormulaApplication,       //  template formula
    fc: P_FormulaApplication,       //  candidate formula
    termIndex: Int
) extends DiffElement with DiffFormula {

  def matches(e: DiffElement): Boolean =
    e match {
      case efa: DiffFormulaApplication if ft.p == efa.ft.p =>
        termIndex == efa.termIndex
      case _ =>
        false
    }

  def wrap(s: Sequent): Sequent =
    {println("!!null!!: diff, line 58"); null}

}

case class DiffFormulaDyadicL(
    override val swap: Boolean,
    ft: P_FormulaDyadic,            //  template formula
    fc: P_FormulaDyadic,            //  candidate formula
    dummy: Int                      //  gives this function a
                                    //    distinguished signature
) extends DiffElement with DiffFormula {

  def matches(e: DiffElement): Boolean =
    e match {
      case efdL: DiffFormulaDyadicL if ft.kind == efdL.ft.kind =>
        true
      case _ =>
        false
    }

  def wrap(s: Sequent): Sequent = {
    val rule =
      ft match {
        case _: P_FormulaConjunction =>
          inferenceRuleRool2R
        case _: P_FormulaDisjunction =>
          inferenceRuleRool3R
        case _: P_FormulaImplication =>
          inferenceRuleYRool4
      }
    val (a, b) = rectify[P_Formula](ft.f1, fc.f1)
    rule(s, a, b, ft.f2)
  }

}

object DiffFormulaDyadicL {

  def apply(
      swap: Boolean,
      ft: P_FormulaDyadic,          //  template formula
      fc: P_FormulaDyadic           //  candidate formula
    ) = {
    val newSwap = if (ft.isInstanceOf[P_FormulaImplication]) !swap else swap
    new DiffFormulaDyadicL(newSwap, ft, fc, 0)
  }

}

case class DiffFormulaDyadicR(
    override val swap: Boolean,
    ft: P_FormulaDyadic,            //  template formula
    fc: P_FormulaDyadic             //  candidate formula
) extends DiffElement with DiffFormula {

  def matches(e: DiffElement): Boolean =
    e match {
      case efdR: DiffFormulaDyadicR if ft.kind == efdR.ft.kind =>
        true
      case _ =>
        false
    }

  def wrap(s: Sequent): Sequent = {
    val rule =
      ft match {
        case _: P_FormulaConjunction =>
          inferenceRuleRool2L
        case _: P_FormulaDisjunction =>
          inferenceRuleRool3L
        case _: P_FormulaImplication =>
          inferenceRuleXRool4
      }
    val (a, b) = rectify[P_Formula](ft.f2, fc.f2)
    rule(s, a, b, ft.f1)
  }

}

case class DiffFormulaNegation(
    override val swap: Boolean,
    ft: P_FormulaNegation,          //  template formula
    fc: P_FormulaNegation,          //  candidate formula
    dummy: Int                      //  gives this function a
                                    //    distinguished signature
) extends DiffElement with DiffFormula {

  def matches(e: DiffElement): Boolean = e.isInstanceOf[DiffFormulaNegation]

  def wrap(s: Sequent): Sequent = {
    val (a, b) = rectify[P_Formula](ft.f1, fc.f1)
    inferenceRuleXRool5(s, a, b)
  }

}

object DiffFormulaNegation {

  def apply(
      swap: Boolean,
      ft: P_FormulaNegation,        //  template formula
      fc: P_FormulaNegation         //  candidate formula
    ) = new DiffFormulaNegation(!swap, ft, fc, 0)

}

case class DiffFormulaQuantification(
    override val swap: Boolean,
    ft: P_FormulaQuantification,    //  template formula
    fc: P_FormulaQuantification     //  candidate formula
) extends DiffElement with DiffFormula {

  def matches(e: DiffElement): Boolean =
    e match {
      case efq: DiffFormulaQuantification if ft.kind == efq.ft.kind =>
        ft.v == efq.ft.v
      case _ =>
        false
    }

  def wrap(s: Sequent): Sequent = {
    val varName = ft.v
    val varTerm = P_TermVariable(varName)
    val substVar  = (varName, varName)
    val substTerm = (varTerm, varName)

    def rule(a:  P_Formula, b: P_Formula) =
      ft.kind match {
        case QuantificationKind.Universal =>
          inferenceRuleXRool6(s, a, substTerm, b, substVar)
        case QuantificationKind.Existential =>
          inferenceRuleYRool6(s, a, substVar, b, substTerm)
      }

    val (a, b) = rectify[P_Formula](ft.f1, fc.f1)
    rule(a, b)
  }

}


object diff {

  implicit class P_TermDiffer(val template: P_Term) {

    def diff(
        candidate: P_Term
      ): Option[List[DiffElement]] =
      diff(None, candidate, Nil).result

    def diff(
        continuation: Option[() => TailRec[Option[List[DiffElement]]]],
        candidate: P_Term,
        deconst: List[DiffElement]
      ): TailRec[Option[List[DiffElement]]] = {

      def diff_continue(
          continuation: Option[() => TailRec[Option[List[DiffElement]]]],
          finished: Boolean
        ): TailRec[Option[List[DiffElement]]] =
        if (finished)
          done(Some(deconst))
        else
          continuation match {
            case Some(cont) =>
              tailcall(cont())
            case None =>
              done(None)
          }

      template match {

        case tc: P_TermConstant =>
          val finished = candidate != tc
          tailcall(diff_continue(continuation, finished))

        case tv: P_TermVariable =>
          val finished = candidate != tv
          tailcall(diff_continue(continuation, finished))

        case ta: P_TermApplication =>
          candidate match {
            case t: P_TermApplication if t.f == ta.f =>
              def diffElement(termIndex: Int) =
                DiffTermApplication(ta, t, termIndex)
              tailcall(ta.a.diff(continuation, t.a, diffElement, deconst))
            case _ =>
              tailcall(diff_continue(continuation, true))
          }

      }

    }

  }

  implicit class P_ApplicandDiffer(val template: P_Applicand) {

    def diff(
        continuation: Option[() => TailRec[Option[List[DiffElement]]]],
        candidate: P_Applicand,
        diffElement: Int => DiffElement,
        deconst: List[DiffElement]
      ): TailRec[Option[List[DiffElement]]] = {
      val templateTerms = template.terms
      val candidateTerms = candidate.terms
      val length = templateTerms.length
      if (candidateTerms.length == length) {

        def looper(
            termIndex: Int
          ): TailRec[Option[List[DiffElement]]] =
          if (termIndex >= length)
            //  Processing of the current applicand is complete without
            //  any mismatch between template and candidate being found.
            tailcall(diff_continue(continuation, None))
          else {
            def cont1: () => TailRec[Option[List[DiffElement]]] =
              () => tailcall(looper(termIndex + 1))
            val newDeconst = diffElement(termIndex) :: deconst
            tailcall(templateTerms(termIndex).
              diff(Some(cont1), candidateTerms(termIndex), newDeconst))
          }

        tailcall(looper(0))

      }
      else
        done(Some(deconst))

    }

  }

  implicit class P_FormulaDiffer(val template: P_Formula) {

    def diff(
        candidate: P_Formula
      ): Option[List[DiffElement]] =
      diff(None, false, candidate, Nil).result

    def diff(
        continuation: Option[() => TailRec[Option[List[DiffElement]]]],
        swap: Boolean,
        candidate: P_Formula,
        deconst: List[DiffElement]
      ): TailRec[Option[List[DiffElement]]] = {

      template match {

        case ap: P_FormulaProposition =>
          val retval =
            if (candidate != ap)
              Some(deconst)
            else
              None
          tailcall(diff_continue(continuation, retval))

        case aa: P_FormulaApplication =>
          candidate match {
            case f: P_FormulaApplication if f.p == aa.p =>
              def diffElement(termIndex: Int) =
                DiffFormulaApplication(swap, aa, f, termIndex)
              tailcall(aa.a.diff(continuation, f.a, diffElement, deconst))
            case _ =>
              tailcall(diff_continue(continuation, Some(deconst)))
          }

        case an: P_FormulaNegation =>
          candidate match {
            case f: P_FormulaNegation =>
              tailcall(an.f1.diff(continuation, !swap, f.f1, DiffFormulaNegation(swap, an, f) :: deconst))
            case _ =>
              tailcall(diff_continue(continuation, Some(deconst)))
          }

        case ad: P_FormulaDyadic =>
          candidate match {
            case f: P_FormulaDyadic if f.kind == ad.kind =>
              def cont1: () => TailRec[Option[List[DiffElement]]] =
                () => tailcall(ad.f2.diff(continuation, swap, f.f2, DiffFormulaDyadicR(swap, ad, f) :: deconst))
              val diffEl = DiffFormulaDyadicL(swap, ad, f)
              tailcall(ad.f1.diff(Some(cont1), diffEl.swap, f.f1, diffEl :: deconst))
            case _ =>
              tailcall(diff_continue(continuation, Some(deconst)))
          }

        case aq: P_FormulaQuantification =>
          candidate match {
            case f: P_FormulaQuantification if f.kind == aq.kind && f.v == aq.v =>
              tailcall(aq.f1.diff(continuation, swap, f.f1, DiffFormulaQuantification(swap, aq, f) :: deconst))
            case _ =>
              tailcall(diff_continue(continuation, Some(deconst)))
          }

      }

    }

  }

  private def diff_continue(
      continuation: Option[() => TailRec[Option[List[DiffElement]]]],
      retval: Option[List[DiffElement]]
    ): TailRec[Option[List[DiffElement]]] =
    retval match {
      case Some(_) =>
        done(retval)
      case None =>
        continuation match {
          case Some(cont) =>
            tailcall(cont())
          case None =>
            done(None)
        }
    }

}
