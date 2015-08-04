package multi_partition_topic

import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import utils.Utils

object MultiPartition_1 {

  val broker = "localhost:9092"

  def main(args: Array[String]) {
    val serializer = new StringSerializer

    val producer = Utils.createProducer[String, String](List(broker), serializer, serializer)

    val record1 = new ProducerRecord[String, String]("multiPartition", 0, "hey","hey.. there I am using laptop!!")
    producer.send(record1)
  }
}
