import akka.actor.{Actor, ActorRef}

class Ping(pong: ActorRef) extends Actor {
  var count = 0
  def incrementAndPrint = { count +=1; println(s"$count: ping")}

  override def receive: Receive = {
    case Messages.StartMessage =>
      incrementAndPrint
      pong ! Messages.PingMessage
    case Messages.PongMessage =>
      incrementAndPrint
      if (count > 99) {
        sender ! Messages.StopMessage
        println("ping stopped")
        context.stop(self)
      } else {
        sender ! Messages.PingMessage
      }
    case _ => println("got something unexpected")
  }
}
