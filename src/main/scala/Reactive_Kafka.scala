import akka.stream.scaladsl.{Sink, Source}
import com.softwaremill.react.kafka.{ConsumerProperties, ProducerProperties, ReactiveKafka}
import kafka.serializer.{StringDecoder, StringEncoder}
import utils.Utils

object Reactive_Kafka {
  import Utils.{actorSystem, materializer}

  def main(args: Array[String]) {
    val kafka = new ReactiveKafka()

    val publisher = kafka.consume(ConsumerProperties(
      brokerList = "localhost:9092",
      zooKeeperHost = "localhost:2181",
      topic = "test",
      groupId = "groupName",
      decoder = new StringDecoder()
    ))
    val subscriber = kafka.publish(ProducerProperties(
      brokerList = "localhost:9092",
      topic = "uppercaseStrings",
      clientId = "groupName",
      encoder = new StringEncoder()
    ))

    Source(publisher).map(_.toUpperCase).to(Sink(subscriber)).run()
  }
}
