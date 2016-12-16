/**
  * Created by EdSnow on 1/24/2016.
  */


package parseStuff


//  import org.antlr.v4.runtime._


class RecognizerError(offendingSymbol: AnyRef,
    line: Int, charPositionInLine: Int,
    msg: String) {

  def print = System.out.println("line " + line + ":" + charPositionInLine +
                    " at " + offendingSymbol + ": " + msg)

/*
  def getTree(adaptor: XTreeAdaptor): CommonTree = {
    val node: CommonTree = adaptor.create("error")
    node.addChild(adaptor.create("line " + line + ":" + charPositionInLine +
      " at " + offendingSymbol + ": " + msg))
    node
  }
*/

}


class ErrorCollector{

  val errorBuf = scala.collection.mutable.ArrayBuffer.empty[RecognizerError]

  def collect(offendingSymbol: AnyRef,
              line: Int, charPositionInLine: Int,
              msg: String) = {
    val recogError =
      new RecognizerError(offendingSymbol, line, charPositionInLine, msg)
    errorBuf += recogError
  }

  def apply(): Array[RecognizerError] = {
    val errorArray = errorBuf.toArray
    errorBuf.clear
    errorArray
  }

}
