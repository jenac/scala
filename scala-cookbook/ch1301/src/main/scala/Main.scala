import akka.actor.{ActorSystem, Props}

object Main extends App {
  val system = ActorSystem("HelloSystem")
  val helloActor = system.actorOf(Props[HelloActor], name="HelloActor")
  helloActor ! "hello"
  helloActor ! "how are you?"
  system.terminate()
}
