import akka.actor.Actor
import Messages.Name
class Child extends Actor {
  var name = "No name"

  override def postStop(): Unit = println(s"D'oh! They killed me ($name): ${self.path}")

  override def receive: Receive = {
    case Name(name) => this.name = name
    case _ => println(s"Child $name got a message")
  }
}
