import akka.actor.Actor

class TestActor extends Actor {
  override def receive: Receive = {
    case AskNameMessage => sender ! "Fred"
    case _ => println("got an unknown message")
  }
}
