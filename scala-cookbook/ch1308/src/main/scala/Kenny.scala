import akka.actor.Actor

class Kenny extends Actor {
  override def receive: Receive = {
    case Explode => throw new Exception("Boom!!!")
    case _ => println("Kenny got a message")
  }

  override def preStart(): Unit = println("Kenny::preStart")

  override def postStop(): Unit = println("Kenny::postStop")

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    println("Kenny::preRestart")
    super.preRestart(reason, message)
  }

  override def postRestart(reason: Throwable): Unit = {
    println("Kenny::postRestart")
    super.postRestart(reason)
  }
}
