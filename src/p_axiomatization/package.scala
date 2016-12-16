/**
  * Created by EdSnow on 12/3/2016.
  */


package object p_axiomatization {


  import formula._
  import p_formula._

  import parseStuff._


  //  This is only called from Java code (The ANTLR-generated parser in particular),
  //  hence the use of null as a return value.
  def lookupAxiomatization(s: String): P_Axiomatization = {
    val entry = pOrANameMap.get(s)
    entry match {
      case Some(a: P_Axiomatization) => a
      case _ => null
    }
  }


  type AxiomTemplate = P_Formula => Boolean

  def matchAxiom(f: P_Formula, axiomList: Seq[AxiomTemplate]): Boolean =
    axiomList match {
      case Nil => false
      case head :: tail => head(f) || matchAxiom(f, tail)
    }

  def axiomMatcher(axiom: Formula): AxiomTemplate =
    axiom match {
      case x: P_Formula =>
        f => f == axiom
      case _ =>
        f => false
    }


  def equalityPredicateSchemaMatcher: AxiomTemplate = {
    val schema = pf("x=y → (Z → Z)")
        //  schema:  x=y → (ϕ(x) → ϕ(y))
    f => false
  }

  def equalityFunctionSchemaMatcher: AxiomTemplate = {
    val schema = pf("x=y → Z=Z")
        //  schema:  x=y → t(x)=t(y)
    f => false
  }

  object axiomatization_Eq extends P_Axiomatization("Eq") {

    def subsumes(f: P_Formula) =
      matchAxiom(f, Seq(

        axiomMatcher(pf("∀x.(x=x)")),
        axiomMatcher(pf("∀x∀y∀z.(x=y ∧ y=z → x=z)")),

        equalityPredicateSchemaMatcher,
        equalityFunctionSchemaMatcher

      ))

  }


  def inductionSchemaMatcher: AxiomTemplate = {
    val schema = pf("Z ∧ ∀x.(Z → Z) → ∀x.Z")
        //  schema:  ϕ(0) ∧ ∀x(ϕ(x) → ϕ(S(x))) → ∀xϕ(x)
    f => false
  }

  object axiomatization_PA extends P_Axiomatization("PA") {

    def subsumes(f: P_Formula) =
      matchAxiom(f, Seq(

        axiomMatcher(pf("∀x.(0≠S(x))")),
        axiomMatcher(pf("∀x∀y.(S(x)=S(y) → x=y)")),

        axiomMatcher(pf("∀x.(x+0 = x)")),
        axiomMatcher(pf("∀x∀y.(x+S(y) = S(x+y))")),

        axiomMatcher(pf("∀x.(x×0 = 0)")),
        axiomMatcher(pf("∀x∀y.(x×S(y) = x×y+x)")),

        inductionSchemaMatcher

      ))

  }


}
