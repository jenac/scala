import akka.actor.Actor

class Pong extends Actor {
  override def receive: Receive = {
    case Messages.PingMessage =>
      println("pong")
      sender ! Messages.PongMessage
    case Messages.StopMessage =>
      println("pong stopped")
      context.stop(self)
    case _ => println("got something unexpected")
  }
}
