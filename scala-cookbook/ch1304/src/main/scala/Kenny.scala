import akka.actor.Actor

class Kenny extends Actor {
  println("enter the Kenny constructor")

  override def preStart(): Unit = println("Kenny: preStart")

  override def postStop(): Unit = println("Kenny: postStop")

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    println("Kenny: preRestart")
    println(s"MESSAGE: ${message.getOrElse("")}")
    println(s"REASON: ${reason.getMessage}")
    super.preRestart(reason, message)
  }

  override def postRestart(reason: Throwable): Unit = {
    println("Kenny: postRestart")
    println(s"REASON: ${reason.getMessage}")
    super.postRestart(reason)
  }

  override def receive: Receive = {
    case ForceRestart => throw new Exception("Boom!")
    case _ => println("Kenny got a message")
  }
}
