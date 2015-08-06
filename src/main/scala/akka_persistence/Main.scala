package akka_persistence

import akka.actor.{Props, ActorSystem}
import akka.persistence._
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._

case class Cmd(data: String)
case class Evt(data: String)

case class ExampleState(events: List[String] = Nil) {
  def updated(evt: Evt): ExampleState = copy(evt.data :: events)
  def size: Int = events.length
  override def toString: String = events.reverse.toString()
}

class ExamplePersistentActor extends PersistentActor {
  override def persistenceId = "sample-id-1"

  var state = ExampleState()

  def updateState(event: Evt): Unit =
    state = state.updated(event)

  def numEvents = state.size

  def receiveRecover: Receive = {
    case evt: Evt                                 => updateState(evt)
    case SnapshotOffer(_, snapshot: ExampleState) => state = snapshot
  }

  def receiveCommand: Receive = {
    case Cmd(data) =>
      println(state.size)
      persist(Evt(s"$data-$numEvents"))(updateState)
      persist(Evt(s"$data-${numEvents + 1}")) { event =>
        updateState(event)
        context.system.eventStream.publish(event)
      }
    case "snap"  => saveSnapshot(state)
    case "print" => println(state)
  }
}


object Main {
  def main(args: Array[String]) {
    val actorSystem = ActorSystem()
    val actor1 = actorSystem.actorOf(Props(new ExamplePersistentActor()))
    import actorSystem.dispatcher
    implicit val timeout = Timeout(5 seconds)

    val unit1 = actor1 ? Cmd("pallavi")
    val unit2 = actor1 ? Cmd("pallavi")


    unit1.onComplete(_ => unit2.onComplete(_ => actorSystem.shutdown()))

  }
}