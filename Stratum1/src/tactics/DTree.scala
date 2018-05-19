package wooble

import scala.annotation.tailrec

/**
  * Created by EdSnow on 4/15/2017.
  */


class Element {
  var vertex: Vertex = new Vertex

  def find(): Vertex = {
    val root = Vertex.findRoot(vertex)

    @tailrec def reparent(v: Vertex): Unit =
    v.parent match {
      case None =>
        ()
      case Some(parent) =>
        v.parent = Some(root)
        reparent(parent)
    }

    reparent(vertex)
    root
  }

  def unite(e: Element): Unit = {
    val thisRoot = find()
    val eRoot = e.find()
    if (thisRoot != eRoot) {
      //  thisRoot and eRoot do not yet belong to the same set.
      if (thisRoot.rank < eRoot.rank)
        thisRoot.parent = Some(eRoot)
      else {
        eRoot.parent = Some(thisRoot)
        if (thisRoot.rank == eRoot.rank)
          thisRoot.rank += 1
      }
    }

  }

}


object Vertex {

  @tailrec final def findRoot(v: Vertex): Vertex =
    v.parent match {
      case None =>
        v
      case Some(parent) =>
        findRoot(parent)
    }

}

class Vertex {
  var parent: Option[Vertex] = None
  var rank: Int = 0
  var name: Option[Int] = None
}
