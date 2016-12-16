/**
  * Created by EdSnow on 12/9/2016.
  */


package object substitution {

  import p_term._

  implicit class P_TermExtension(val t: P_Term) {

    def substitution(u: P_Term): Either[Boolean, (P_VarName, P_Term)] = t match {

      case tc: P_TermConstant =>
        Left(tc == u): Either[Boolean, (P_VarName, P_Term)]

      case tv: P_TermVariable =>
        u match {
          case uv: P_TermVariable =>
            if (tv.v == uv.v)
              Left(true)
            else
              Right(tv.v, t)
          case _ => Right((tv.v, u))
        }

      case ta: P_TermApplication =>
        u match {
          case ua: P_TermApplication =>
            if (ta.f == ua.f)
              ta.a.substitution(ua.a)
            else
              Left(false)
          case _ => Left(false)
        }

    }

  }

  implicit class P_ApplicandExtension(val a: P_Applicand) {

    def substitution(b: P_Applicand): Either[Boolean, (P_VarName, P_Term)] =
      if (a.terms.length == b.terms.length) {
        val ifa = a.terms.iterator
        val ifb = b.terms.iterator
        var collector: Either[Boolean, (P_VarName, P_Term)] = Left(true)
        while ((collector match {
          case Left(false) => false;
          case _ => true
        }) && ifa.hasNext) {
          val gunk = ifa.next().substitution(ifb.next())
          collector =
            collector match {
              case Left(true) => gunk
              case Right((va, ta)) =>
                gunk match {
                  case Left(true) => collector
                  case Right((vb, tb)) =>
                    if (va == vb && ta == tb)
                      collector
                    else
                      Left(false)
                  case Left(_) => Left(false)
                }
              case Left(_) => Left(false)
            }
        }
        collector
      }
      else
        Left(false)

  }

  import p_formula._

  implicit class P_FormulaExtension(val f: P_Formula) {

    def substitution(g: P_Formula): Either[Boolean, (P_VarName, P_Term)] = f match {

      case fp: P_FormulaProposition =>
        g match {
          case gp: P_FormulaProposition =>
            Left(fp.p == gp.p)
          case _ =>
            Left(false)
        }

      case fa: P_FormulaApplication =>
        g match {
          case ga: P_FormulaApplication =>
            if (fa.p == ga.p)
              fa.a.substitution(ga.a)
            else
              Left(false)
          case _ => Left(false)
        }

      case fn: P_FormulaNegation =>
        g match {
          case gn: P_FormulaNegation =>
            fn.f1.substitution(gn.f1)
          case _ =>
            Left(false)
        }

      case fd: P_FormulaDyadic =>
        g match {
          case gd: P_FormulaDyadic =>
            if (fd.kind == gd.kind) {
              val f1_subst = fd.f1.substitution(gd.f1)
              f1_subst match {
                case Left(false) => Left(false)
                case _ =>
                  val f2_subst = fd.f2.substitution(gd.f2)
                  f2_subst match {
                    case Left(false) => Left(false)
                    case Left(true) => f1_subst
                    case Right(f2_s) =>
                      f1_subst match {
                        case Left(_) => f2_subst
                        case Right(f1_s) =>
                          if (f1_s._1 == f2_s._1 && f1_s._2 == f2_s._2)
                            f1_subst
                          else
                            Left(false)
                      }
                  }
              }
            }
            else
              Left(false)
          case _ => Left(false)
        }

      case fq: P_FormulaQuantification =>
        g match {
          case gq: P_FormulaQuantification =>
            if (fq.kind._1 == gq.kind._1 && fq.v == gq.v)
              fq.f1.substitution(gq.f1)
            else
              Left(false)
          case _ => Left(false)
        }

      case _ => Left(false)

    }

    def checkSubstTerm(g: P_Formula): (P_VarName, Option[(P_Formula, P_Formula)]) = {
      val subst = substitution(g)
      subst match {
        case Left(_) => (failedSubstitutedVariable, Some(f, g))
        case Right((v, _)) =>
          (v, None)
      }
    }

    def checkSubstVar(g: P_Formula): (P_VarName, Option[(P_Formula, P_Formula)]) = {
      val subst = substitution(g)
      subst match {
        case Left(_) => (failedSubstitutedVariable, Some(f, g))
        case Right((v, t)) =>
          t match {
            case _: P_TermVariable => (v, None)
            case _ => (failedSubstitutedVariable, Some(f, g))
          }
      }
    }

  }

}
