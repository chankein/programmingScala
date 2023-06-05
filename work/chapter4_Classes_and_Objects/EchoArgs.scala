object EchoArgs {

    def main(args: Array[String]) = {
        for (arg <- args)
            print(arg + " ")
        println()
    }
}