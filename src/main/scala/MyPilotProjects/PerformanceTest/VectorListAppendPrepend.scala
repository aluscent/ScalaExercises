package MyPilotProjects.PerformanceTest

object VectorListAppendPrepend {

  def appendPrependSeq(seq:Seq[Int], f: (Seq[Int], Int) => Seq[Int], it:Int): (Seq[Int], Double) ={
    val begin = System.currentTimeMillis
    var modifiedSeq = seq
    for (j <- 0 until it) {
      modifiedSeq = f(modifiedSeq, j)
    }
    val elapsedTime = System.currentTimeMillis - begin
    return (modifiedSeq, elapsedTime)
  }

  def main(args: Array[String]): Unit = {

    val vec: Vector[Int] = Vector()
    val lst: List[Int] = List()

    val numElements = 100000
    val appendTimeRatio = appendPrependSeq(lst, (a, b) => a :+ b, numElements)._2 / appendPrependSeq(vec, (a, b) => a :+ b, numElements)._2
    val prependTimeRatio = appendPrependSeq(vec, (a, b) => b +: a, numElements)._2 / appendPrependSeq(lst, (a, b) => b +: a, numElements)._2

    println("Append test with %s elements, Vector is ~ %s times faster than List".format(numElements, appendTimeRatio))
    println("Prepend test with %s elements, List is ~ %s times faster than Vector".format(numElements, prependTimeRatio))
  }
}
