import akka.actor.{Actor, Props, Terminated}

class Parent extends Actor {
  val kenny = context.actorOf(Props[Kenny], name="Kenny")
  context.watch(kenny)
  override def receive: Receive = {
    case Terminated(kenny) => println("OMG, they killed kenny")
    case _ => println("Parent got a message")
  }
}
