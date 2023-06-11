// In file built-in-control-structures/Match.scala

val firstArg = if (args.length > 0) args(0) else ""

  firstArg match {
    case "salt" => println("pepper")
    case "chips" => println("salsa")
    case "eggs" => println("bacon")
    case _ => println("huh?")
  }           


// In file built-in-control-structures/Match.scala

val firstArg = if (!args.isEmpty) args(0) else ""

val friend =
    firstArg match {
      case "salt" => "pepper"
      case "chips" => "salsa"
      case "eggs" => "bacon"
      case _ => "huh?"
    }           

  println(friend)