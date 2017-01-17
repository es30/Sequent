package p_axiomatization

import formula.Formula
import parseStuff.pf

/**
  * Created by EdSnow on 1/5/2017.
  */

object axiomatization_Zz extends P_Axiomatization("Zz") {

  val template_1 = pf("B")

  def subsumes(f: Formula) =
    AxiomTemplateList(Seq(

      AxiomTemplate(template_1)

    )).matchAxiom(f)

}
