import akka.actor.{ActorSystem, Props}

object Main extends App {
  val system = ActorSystem("PingPongSystem")
  val pong = system.actorOf(Props[Pong], name="Pong")
  val ping = system.actorOf(Props(new Ping(pong)), name= "Ping")

  ping ! Messages.StartMessage

  system.terminate()

}
