package substitution

import p_term.P_VarName
import p_formula._
import p_formula.walker._

/**
  * Created by EdSnow on 12/29/2016.
  */


object hasFreeOccurrence {

  implicit class P_Formula_hasFreeOccurrence(val formula: P_Formula) {

    def hasFreeOccurrence(target: P_VarName): Boolean = {

      class Checker extends Visitor {

        def variable(vars: Set[P_VarName], v: P_VarName): Boolean =
          v == target

        def binding(v: P_VarName): Boolean =
          v == target

      }

      val visitor = new Checker
      formula.walk(visitor)
    }

  }

}


object checkOccurs {

  import p_formula._
  import hasFreeOccurrence._

  import p_sequent._

  implicit class Cedent_checkOccurs(val cedent: Cedent) {

    def checkOccurs(v: P_VarName): Option[(Set[P_Formula], P_VarName)] = {
      val violators =
        cedent.fSet.collect {
          case a: P_Formula if a.hasFreeOccurrence(v) => a
        }
      if (violators.isEmpty)
        None
      else
        Some((violators, v))
    }

  }

}
