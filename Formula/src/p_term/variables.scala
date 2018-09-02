package p_term

/**
  * Created by EdSnow on 12/30/2016.
  */

import p_term.walker.P_Term_walker

object variables {

  implicit class P_Term_variables(val term: P_Term) {

    def variables(): Set[P_VarName] = {

      class VariableNameCollector extends Visitor {

        var collectedVars = Set[P_VarName]()

        def variable(vars: Set[P_VarName], v: P_VarName): Boolean = {
          collectedVars += v
          false
        }

      }

      val visitor = new VariableNameCollector
      term.walk(visitor, Set())
      visitor.collectedVars
    }

  }

}
