object Ex5 {
  // easy access to Fruit
  import bobsdelights.Fruit
  
  // easy access to all members of bobsdelights
  import bobsdelights._
  
  // easy access to all members of Fruits
  import bobsdelights.Fruits._
  def showFruit(fruit: Fruit) = {
    import fruit._
    println(name + "s are " + color)
  }

  def main(args: Array[String]): Unit = {
    println("Apple [" + Apple + "]")
    showFruit(Fruits.Apple)
  }
}