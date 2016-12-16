/**
  * Created by EdSnow on 12/8/2016.
  */


package object formulaBuilder {


  import shapeless._

  import error._
  import term._
  import p_term._
  import formula._
  import p_formula._

  import vetter._


// ====================================================================


  object vetArgument extends Poly1 {

    implicit val caseFormula = at[Formula](
      x => {
        x match {
          case null => (None, Some(new Error_null))
          case a: P_Formula => (Some(a), a.error)
          case a: Formula => (None, Some(new Error_empty(a.error)))
        }
      }: AType[P_Formula])

    implicit val caseP_Proposition = at[P_Proposition](
      x => {
        x match {
          case null => (None, Some(new Error_null))
          case a: P_Proposition => (Some(a), None)
        }
      }: AType[P_Proposition])

    implicit val caseP_Predicate = at[P_Predicate](
      x => {
        x match {
          case null => (None, Some(new Error_null))
          case a: P_Predicate => (Some(a), None)
        }
      }: AType[P_Predicate])

    implicit val caseQuantificationKind = at[QuantificationKind.Kind](
      x => {
        x match {
          case null => (None, Some(new Error_null))
          case a: QuantificationKind.Kind => (Some(a), None)
        }
      }: AType[QuantificationKind.Kind])

    implicit val caseP_VarName = at[P_VarName](
      x => {
        x match {
          case null => (None, Some(new Error_null))
          case a: P_VarName => (Some(a), None)
        }
      }: AType[P_VarName])

    implicit val caseP_Function = at[P_Function](
      x => {
        x match {
          case null => (None, Some(new Error_null))
          case a: P_Function => (Some(a), None)
        }
      }: AType[P_Function])

    implicit val caseApplicand = at[Array[Term]](
      x => {
        x match {
          case null => (None, Some(new Error_null))
          case a: Array[Term] => {

            val inError: PartialFunction[(Term, Int), (Error, Int)] = {
              case (null, pos) =>
                (new Error_null, pos)
              case (t: P_Term, pos) if t.error.isDefined =>
                (t.error.get, pos)
              case (t: Term, pos) =>
                (new Error_empty(t.error), pos)
            }

            val (p1, p2) = a.span(_.isInstanceOf[P_Term])
            val termArray =
              if (p2.length == 0)
                Some(new P_Applicand(p1.map(_.asInstanceOf[P_Term])))
              else
                None
            val errorList: List[(Error, Int)] = a.toList.zip(Stream from 1).collect(inError)
            (termArray, None)
          }
        }
      }: AType[P_Applicand])

    implicit val caseError = at[Option[Error]](
      x => x: Option[Error])

  }


// ====================================================================


  trait VetApplicand[L <: HList, Lout <: HList] {
    def vetApplicand(x: L, argPos: Int): (Option[Lout], List[(Error, Int)], Option[Error])
  }


  object VetApplicand {

    implicit def caseHNil[H, Hy]
      (implicit
        getHead: vetArgument.Case.Aux[H, Option[Error]]
      ) = new VetApplicand[H :: HNil, HNil] {

        def vetApplicand(x: H :: HNil, argPos: Int) = {
          val error = getHead(x.head)
          (Some(HNil), Nil, error)
        }

      }

    implicit def caseHCons[H, Hy, T1, T2 <: HList, Ty <: HList]
      (implicit
        getHead: vetArgument.Case.Aux[H, (Option[Hy], Option[Error])],
        getTail: VetApplicand[T1 :: T2, Ty]
      ) = new VetApplicand[H :: T1 :: T2, Hy :: Ty] {

        def vetApplicand(x: H :: T1 :: T2, argPos: Int) = {
          val (argArg, error) = getHead(x.head)
          val (hlist, errorList, errorzz) = getTail.vetApplicand(x.tail, argPos + 1)
          val newHList =
            (argArg, hlist) match {
              case (Some(head), Some(tail)) => Some(head :: tail)
              case (_, _) => None
            }
          val newError: List[(Error, Int)] =
            error match {
              case Some(a) => (a, argPos) :: errorList
              case None => errorList
            }
          (newHList, newError, errorzz)
        }

      }

  }


// ====================================================================


  trait Encapsulator[Tout] {
    def encapsulation(x: Option[Error]): Tout
  }


  object Encapsulator {

    implicit val caseFormula = new Encapsulator[Formula] {
      def encapsulation(x: Option[Error]) = new Formula(x)
    }

    implicit val caseTerm = new Encapsulator[Term] {
      def encapsulation(x: Option[Error]) = new Term(x)
    }

  }


// ====================================================================

  import shapeless.ops.function.{FnFromProduct, FnToProduct}

/*
  def formulaBuilder[Ta <: HList, T <: HList, F, Tout](f: F)
    (implicit
      ftp: FnToProduct.Aux[F, T => Option[Error] => Tout],
      vet: VetApplicand[Ta, T],
      enc: Encapsulator[Tout],
      ffp: FnFromProduct[Ta => Tout]
    ): ffp.Out =
    ffp(
      (a: Ta) => {

        //  Scrutinize the arguments in the wrapper's applicand
        //  and convert them to a form consumable by the wrapped
        //  inference rule.

        val (applicand, errorListExt, error) = vet.vetApplicand(a, 1)
        val combinedError = errorListCombined(error, errorListExt)

        //  Apply the builder if there is an applicand
        //  to which to apply it.

        applicand match {
          case Some(x) => ftp(f)(x)(combinedError)
          case none => enc.encapsulation(combinedError)
        }

      }
    )

// ====================================================================

  val formulaBiconditional =
    formulaBuilder(
      (
        f1: P_Formula,
        f2: P_Formula
      ) => (
        error: Option[Error]
      ) => new P_FormulaBiconditional(f1, f2, error): Formula
    )

  val formulaImplication =
    formulaBuilder(
      (
        f1: P_Formula,
        f2: P_Formula
      ) => (
        error: Option[Error]
      ) => new P_FormulaImplication(f1, f2, error): Formula
    )

  val formulaDisjunction =
    formulaBuilder(
      (
        f1: P_Formula,
        f2: P_Formula
      ) => (
        error: Option[Error]
      ) => new P_FormulaDisjunction(f1, f2, error): Formula
    )

  val formulaConjunction =
    formulaBuilder(
      (
        f1: P_Formula,
        f2: P_Formula
      ) => (
        error: Option[Error]
      ) => new P_FormulaConjunction(f1, f2, error): Formula
    )

  val formulaProposition =
    formulaBuilder(
      (
        p: P_Proposition
      ) => (
        error: Option[Error]
      ) => new P_FormulaProposition(p, error): Formula
    )

  val formulaApplication =
    formulaBuilder(
      (
        p: P_Predicate,
        a: P_Applicand
      ) => (
        error: Option[Error]
      ) => new P_FormulaApplication(p, a, error): Formula
    )

  val formulaNegation =
    formulaBuilder(
      (
        f1: P_Formula
      ) => (
        error: Option[Error]
      ) => new P_FormulaNegation(f1, error): Formula
    )

  val formulaQuantification =
    formulaBuilder(
      (
        q: QuantificationKind.Kind,
        v: P_VarName,
        f1: P_Formula
      ) => (
        error: Option[Error]
      ) => new P_FormulaQuantification(q, v, f1 , error): Formula
    )

// ====================================================================

  val termApplication =
    formulaBuilder(
      (
        f: P_Function,
        a: P_Applicand
      ) => (
        error: Option[Error]
      ) => new P_TermApplication(f, a, error): Term
    )

  val termVariable =
    formulaBuilder(
      (
        v: P_VarName
      ) => (
        error: Option[Error]
      ) => new P_TermVariable(v, error): Term
    )
*/

// ====================================================================

  def formulaBuilder[Ta <: HList, T <: HList, F, Tout](f: F)
    (implicit
      ftp: FnToProduct.Aux[F, T => Option[Error] => Formula],
      vet: VetApplicand[Ta, T],
      enc: Encapsulator[Formula],
      ffp: FnFromProduct[Ta => Formula]
    ): ffp.Out =
    ffp(
      (a: Ta) => {

        //  Scrutinize the arguments in the wrapper's applicand
        //  and convert them to a form consumable by the wrapped
        //  inference rule.

        val (applicand, errorListExt, error) = vet.vetApplicand(a, 1)
        val combinedError = errorListCombined(error, errorListExt)

        //  Apply the builder if there is an applicand
        //  to which to apply it.

        applicand match {
          case Some(x) => ftp(f)(x)(combinedError)
          case none => enc.encapsulation(combinedError)
        }

      }
    )

  def termBuilder[Ta <: HList, T <: HList, F, Tout](f: F)
    (implicit
      ftp: FnToProduct.Aux[F, T => Option[Error] => Term],
      vet: VetApplicand[Ta, T],
      enc: Encapsulator[Term],
      ffp: FnFromProduct[Ta => Term]
    ): ffp.Out =
    ffp(
      (a: Ta) => {

        //  Scrutinize the arguments in the wrapper's applicand
        //  and convert them to a form consumable by the wrapped
        //  inference rule.

        val (applicand, errorListExt, error) = vet.vetApplicand(a, 1)
        val combinedError = errorListCombined(error, errorListExt)

        //  Apply the builder if there is an applicand
        //  to which to apply it.

        applicand match {
          case Some(x) => ftp(f)(x)(combinedError)
          case none => enc.encapsulation(combinedError)
        }

      }
    )

// ====================================================================

  val formulaBiconditional =
    formulaBuilder(
      (
        f1: P_Formula,
        f2: P_Formula
      ) => (
        error: Option[Error]
      ) => new P_FormulaBiconditional(f1, f2, error): Formula
    )

  val formulaImplication =
    formulaBuilder(
      (
        f1: P_Formula,
        f2: P_Formula
      ) => (
        error: Option[Error]
      ) => new P_FormulaImplication(f1, f2, error): Formula
    )

  val formulaDisjunction =
    formulaBuilder(
      (
        f1: P_Formula,
        f2: P_Formula
      ) => (
        error: Option[Error]
      ) => new P_FormulaDisjunction(f1, f2, error): Formula
    )

  val formulaConjunction =
    formulaBuilder(
      (
        f1: P_Formula,
        f2: P_Formula
      ) => (
        error: Option[Error]
      ) => new P_FormulaConjunction(f1, f2, error): Formula
    )

  val formulaProposition =
    formulaBuilder(
      (
        p: P_Proposition
      ) => (
        error: Option[Error]
      ) => new P_FormulaProposition(p, error): Formula
    )

  val formulaApplication =
    formulaBuilder(
      (
        p: P_Predicate,
        a: P_Applicand
      ) => (
        error: Option[Error]
      ) => new P_FormulaApplication(p, a, error): Formula
    )

  val formulaNegation =
    formulaBuilder(
      (
        f1: P_Formula
      ) => (
        error: Option[Error]
      ) => new P_FormulaNegation(f1, error): Formula
    )

  val formulaQuantification =
    formulaBuilder(
      (
        q: QuantificationKind.Kind,
        v: P_VarName,
        f1: P_Formula
      ) => (
        error: Option[Error]
      ) => new P_FormulaQuantification(q, v, f1 , error): Formula
    )

// ====================================================================

  val termApplication =
    termBuilder(
      (
        f: P_Function,
        a: P_Applicand
      ) => (
        error: Option[Error]
      ) => new P_TermApplication(f, a, error): Term
    )

  val termVariable =
    termBuilder(
      (
        v: P_VarName
      ) => (
        error: Option[Error]
      ) => new P_TermVariable(v, error): Term
    )


}
