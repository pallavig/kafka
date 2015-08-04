import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import utils.Utils

object Hello {
  val broker = "localhost:9092"
  val readTopic: String = "test"
  val group = "1"
  val zookeeper = "localhost:2181"


  def main(args: Array[String]): Unit = {
    val serializer = new StringSerializer

    val consumerConnector = Utils.createConsumerFor(readTopic, group, zookeeper)

    val iterator = consumerConnector.createMessageStreams(Map("test" -> 1)).get(readTopic).get.head.iterator()
    val producer = Utils.createProducer[String, String](List(broker), serializer, serializer)

    val record1 = new ProducerRecord[String, String]("test", "hey.. there I am using laptop!!")
    producer.send(record1)

    while (iterator.hasNext()) {
      val message = iterator.next()
      consumerConnector.commitOffsets(true)

      println(message.message().map(_.toChar).mkString(""))
    }
  }
}
