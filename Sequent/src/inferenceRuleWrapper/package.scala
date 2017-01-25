/**
  * Created by EdSnow on 7/22/2016.
  */


package object inferenceRuleWrapper {


  import shapeless._

  import error._
  import term._
  import p_term._
  import formula._
  import p_formula._
  import p_axiomatization._
  import p_sequent._
  import sequent._

  import vetter._


// ====================================================================
// ====================================================================


  //type Seq = Sequent
  type SeqPair = (Sequent, Sequent)

  //type SForm = SFormula
  type SFormPair = (SFormula, SFormula)
  type SFormTriple = (SFormula, SFormula, SFormula)
  type SFormQuadruple = (SFormula, SFormula, SFormula, SFormula)

  //type Term

  //type P_VarName

  type Term_P_VarName = (Term, P_VarName)
  type P_VarNamePair = (P_VarName, P_VarName)


// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -


  //type P_Seq = P_Sequent
  type P_SeqPair = (P_Sequent, P_Sequent)

  //type P_Form = P_Formula
  type P_FormPair = (P_Formula, P_Formula)
  type P_FormTriple = (P_Formula, P_Formula, P_Formula)
  type P_FormQuadruple = (P_Formula, P_Formula, P_Formula, P_Formula)

  //type P_SFAxm = P_SFormulaAxiomatization

  //type P_SForm = P_SFormula

  //type P_Term

  //type P_VarName

  type P_Term_P_VarName = (P_Term, P_VarName)
  //type P_VarNamePair = (P_VarName, P_VarName)


// ====================================================================


  trait VetTuple[L <: HList, Lout <: HList] {
    def vetTuple(x: L, argPos: Int): (Option[Lout], List[(Error, Int)])
  }


  object VetTuple {

    implicit val caseHNil = new VetTuple[HNil, HNil] {
      def vetTuple(x: HNil, argPos: Int) = (Some(HNil), List.empty[(Error, Int)])
    }

    implicit def caseHCons[H, Hy, T <: HList, Ty <: HList]
      (implicit
        getHead: vetArgument.Case.Aux[H, (Option[Hy], Option[Error])],
        getTail: VetTuple[T, Ty]
      ) = new VetTuple[H :: T, Hy :: Ty] {

        def vetTuple(x: H :: T, argPos: Int) = {
          val (argArg, error) = getHead(x.head)
          val (hlist, errorList) = getTail.vetTuple(x.tail, argPos + 1)
          val newHList =
            (hlist, argArg) match {
              case (Some(tail), Some(head)) =>
                Some(head :: tail)
              case (_, _) =>
                None
            }
          val newError: List[(Error, Int)] =
            error match {
              case Some(a) =>
                (a, argPos) :: errorList
              case None =>
                errorList
            }
          (newHList, newError)
        }

    }

  }


  object vetArgument extends Poly1 {


    val fake_P_SeqPair: P_SeqPair = null
    val fake_P_FormPair: P_FormPair = null
    val fake_P_FormTriple: P_FormTriple = null
    val fake_P_FormQuadruple: P_FormQuadruple = null
    val fake_P_Term_P_VarName: P_Term_P_VarName = null
    val fake_P_VarNamePair: P_VarNamePair = null


    def argTuple[P <: Product, L <: HList, R, T <: HList](p: P, obj: R)
      (implicit
        geni: Generic.Aux[P, L],
        geno: Generic.Aux[R, T],
        vet: VetTuple[L, T]
      ): (Option[R], Option[Error]) = {
        val qqq = geni.to(p)
        val (argArg, errorList) = vet.vetTuple(qqq, 1)
        val newArg =
          argArg match {
            case Some(a) =>
              Some(geno.from(a))
            case None =>
              None
          }
        val error =
          if (errorList.isEmpty)
            None
          else
            Some(new Error_list(errorList))
        (newArg, error)
      }


    implicit val caseSequent2P = at[Sequent](
      x => {
        x match {
          case null => (None, Some(new Error_null))
          case s: P_Sequent => (Some(s), s.error)
          case s: Sequent => (None, Some(new Error_empty(s.error)))
        }
      }: AType[P_Sequent])

    implicit val caseSeqPair2P = at[SeqPair](
      x => argTuple(x, fake_P_SeqPair): AType[P_SeqPair])

    implicit val caseSFormula2P = at[SFormula](
      x => {
        x match {
          case null => (None, Some(new Error_null))
          case a: P_Formula => (Some(a), a.error)
          case a: P_SFormulaAxiomatization => (None, Some(new Error_atype(None)))
          case a: Formula => (None, Some(new Error_empty(a.error)))
        }
      }: AType[P_Formula])

    implicit val caseSFormPair2P = at[SFormPair](
      x => argTuple(x, fake_P_FormPair): AType[P_FormPair])
    implicit val caseSFormTriple2P = at[SFormTriple](
      x => argTuple(x, fake_P_FormTriple): AType[P_FormTriple])
    implicit val caseSFormQuadruple2P = at[SFormQuadruple](
      x => argTuple(x, fake_P_FormQuadruple): AType[P_FormQuadruple])

    implicit val caseSFormula2P_A = at[SFormula](
      x => {
        x match {
          case null => (None, Some(new Error_null))
          case a: P_Formula => (None, Some(new Error_atype(a.error)))
          case a: P_SFormulaAxiomatization => (Some(a), None)
          case a: Formula => (None, Some(new Error_empty(a.error)))
        }
      }: AType[P_SFormulaAxiomatization])

    implicit val caseSFormula2P_S = at[SFormula](
      x => {
        x match {
          case null => (None, Some(new Error_null))
          case a: P_Formula => (Some(a), a.error)
          case a: P_SFormulaAxiomatization => (Some(a), None)
          case a: Formula => (None, Some(new Error_empty(a.error)))
        }
      }: AType[P_SFormula])

    implicit val caseTerm2P = at[Term](
      x => {
        x match {
          case null => (None, Some(new Error_null))
          case t: P_Term => (Some(t), t.error)
          case t: Term => (None, Some(new Error_empty(t.error)))
        }
      }: AType[P_Term])

    implicit val caseP_VarName = at[P_VarName](
      x => {
        x match {
          case null => (None, Some(new Error_null))
          case v: P_VarName => (Some(v), None)
        }
      }: AType[P_VarName])

    implicit val caseTerm_P_VarName2P = at[Term_P_VarName](
      x => argTuple(x, fake_P_Term_P_VarName): AType[P_Term_P_VarName])
    implicit val caseP_VarNamePair = at[P_VarNamePair](
      x => argTuple(x, fake_P_VarNamePair): AType[P_VarNamePair])

  }


// ====================================================================


  trait VetApplicand[L <: HList, Lout <: HList] {
    def vetApplicand(x: L, argPos: Int): (Option[Lout], List[(Error, Int)])
  }


  object VetApplicand {

    implicit val caseHNil = new VetApplicand[HNil, HNil] {
      def vetApplicand(x: HNil, argPos: Int) = (Some(HNil), List.empty[(Error, Int)])
    }

    implicit def caseHCons[H, Hy, T <: HList, Ty <: HList]
      (implicit
        getHead: vetArgument.Case.Aux[H, (Option[Hy], Option[Error])],
        getTail: VetApplicand[T, Ty]
      ) = new VetApplicand[H :: T, Hy :: Ty] {

        def vetApplicand(x: H :: T, argPos: Int) = {
          val (argArg, error) = getHead(x.head)
          val (hlist, errorList) = getTail.vetApplicand(x.tail, argPos + 1)
          val newHList =
            (argArg, hlist) match {
              case (Some(head), Some(tail)) =>
                Some(head :: tail)
              case (_, _) =>
                None
            }
          val newError: List[(Error, Int)] =
            error match {
              case Some(a) =>
                (a, argPos) :: errorList
              case None =>
                errorList
            }
          (newHList, newError)
        }

      }

  }


}
