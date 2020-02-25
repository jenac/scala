import akka.actor.Actor

class HelloActor(name: String) extends Actor{
  override def receive: Receive = {
    case "hello" => println(s"hello, my name is $name")
    case _ => println(s"what? from $name")
  }
}
