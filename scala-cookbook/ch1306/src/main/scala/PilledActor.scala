import akka.actor.Actor

class PilledActor extends Actor {
  override def receive: Receive = {
    case s: String => println(s"pilled get message: $s")
    case _ => println("get an unknown message")
  }

  override def postStop(): Unit = println("pilled::postStop called")
}
