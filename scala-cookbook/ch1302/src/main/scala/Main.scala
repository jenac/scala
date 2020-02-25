import akka.actor.{ActorSystem, Props}

object Main extends App {
  val system = ActorSystem("HelloSystem")
  val helloActor = system.actorOf(Props(new HelloActor("Fred")), name = "HelloActor")

  helloActor ! "how are you?"
  helloActor ! "hello"
  system.terminate()
}
