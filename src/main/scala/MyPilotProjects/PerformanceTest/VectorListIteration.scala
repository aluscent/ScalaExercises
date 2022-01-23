package MyPilotProjects.PerformanceTest

object VectorListIteration {

  def calculateSumSeq(seq:Seq[Int]): (Int, Double) ={
    val begin = System.currentTimeMillis
    var total = 0
    for (elem <- seq) {
      total = total + elem
    }
    val elapsedTime = System.currentTimeMillis - begin
    return (total, elapsedTime)
  }

  def main(args: Array[String]): Unit = {

    val numElements = 10000000
    val vec:Vector[Int] = (1 to numElements).toVector
    val lst:List[Int] = (1 to numElements).toList

    println(" Vector iteration of %s elements took %s milliseconds".format(numElements, calculateSumSeq(vec)._2))
    println(" List iteration of %s elements took %s milliseconds".format(numElements, calculateSumSeq(lst)._2))
  }
}
