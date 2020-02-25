import akka.actor.Actor

class HelloActor  extends  Actor {
  override def receive: Receive = {
    case "hello" => println("hi!")
    case _ => println("what?")
  }
}
