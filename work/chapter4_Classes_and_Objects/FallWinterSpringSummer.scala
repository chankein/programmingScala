import ChecksumAccumulator.calculate

object FallWinterSpringSummer extends App {

  for (season <- List("fall", "winter", "spring"))
    println(season + ": " + calculate(season))
}

object EchoArgs {

    def main(args: Array[String]) = {
        for (arg <- args)
            print(arg + " ")
        println()
    }
}