import ChecksumAccumulator.calculate

object Summer {
    def main(args: Array[String]) = {
        for (arg <- args)
        println(arg + ": " + calculate(arg))
    }
}
// scalac ChecksumAccumulator.scala Summer.scala
// fsc ChecksumAccumulator.scala Summer.scala
// scala Summer of love