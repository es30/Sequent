package axiomatization_ZF

//import p_term.P_Term.{lookupConstant, lookupVarName}
//import p_term.{P_VarName, P_Term, P_TermConstant}
import formula.Formula
//import p_formula._
import p_axiomatization.{AxiomTemplate, AxiomTemplateList, P_Axiomatization}

/**
  * Created by EdSnow on 1/5/2017.
  */


object subsumer extends P_Axiomatization("ZF") {

  import parseStuff._

  private val template_1 = pf("∀X∀Y.(∀u.(u∈X⇔u∈Y) ⇒ X=Y)")
                                                    //  Axiom of Extensionality
  private val template_2 = pf("∀a∀b∃c∀x.(x∈c ⇔ (x=a∨x=b))")
                                                    //  Axiom of the Unordered Pair
  private val template_3 = pf("∀X∀p∃Y∀u.(u∈Y ⇔ (u∈X∧Z))")
                                                    //  Axiom Schema of Subsets!!!!!!
  private val template_4 = pf("∀X∃Y∀u.(u∈Y ⇔ ∃z.(z∈X∧u∈z))")
                                                    //  Axiom of the Sum Set
  private val template_5 = pf("∀X∃Y∀u.(u∈Y ⇔ u⊆X)")
                                                    //  Axiom of the Power Set
  private val template_6 = pf("∃S.(∅∈S ∧ ∀x.(x∈S ⇒ ∃X.(∀u.(u∈X⇔u=x) ∧ x∪X∈S)))")
                                                    //  Axiom of Infinity
  private val template_7 = pf("∃S.(∅∈S ∧ ∀x.(x∈S ⇒ x∪{x}∈S))")
                                                    //  Axiom Schema of Replacement!!!!!!
  private val template_8 = pf("∀S.(S≠∅ ⇒ ∃x.(x∈S∧S∩x=∅))")
                                                    //  Axiom of Foundation

  def subsumes(f: Formula) =
    AxiomTemplateList(Seq(

      AxiomTemplate(template_1),
      AxiomTemplate(template_2),
      AxiomTemplate(template_3),
      AxiomTemplate(template_4),
      AxiomTemplate(template_5),
      AxiomTemplate(template_6),
      AxiomTemplate(template_7),
      AxiomTemplate(template_8)

    )).matchAxiom(f)

}
