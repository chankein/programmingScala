//1
args.foreach(arg => println(arg))
//2
args.foreach((arg: String) => println(arg))
//3
args.foreach(println)
//4
for (arg <- args)
    println(arg)
//scala pa.scala Concise is nice