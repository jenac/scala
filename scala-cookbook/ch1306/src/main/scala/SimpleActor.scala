import akka.actor.Actor

class SimpleActor extends Actor {
  override def receive: Receive = {
    case _ => println("simple actor get a message")
  }
}
