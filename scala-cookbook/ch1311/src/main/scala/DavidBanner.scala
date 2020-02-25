import Messages.{ActNormalMessage, BadGuysMakeMeAngry, TryToFindSolution}
import akka.actor.Actor

class DavidBanner extends Actor {
  import context._

  def normalState: Receive = {
    case TryToFindSolution =>
      println("Looking for solution to the problem...")
    case BadGuysMakeMeAngry =>
      println("I am getting angry...")
      become(angryState)
  }

  def angryState: Receive = {
    case ActNormalMessage =>
      println("Phew, I am back to being David.")
      become(normalState)
  }

  override def receive: Receive = {
    case BadGuysMakeMeAngry => become(angryState)
    case ActNormalMessage => become(normalState)
  }
}
