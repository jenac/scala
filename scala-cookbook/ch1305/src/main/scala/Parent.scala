import Messages.{CreateChild, Name}
import akka.actor.{Actor, Props}

class Parent extends Actor {
  override def receive: Receive = {
    case CreateChild(name) =>
      println(s"Parent about to create a child: $name....")
      val child = context.actorOf(Props[Child], name = s"$name")
      child ! Name(name)
    case _ => println(s"Parent got some other message")
  }
}
