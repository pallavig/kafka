package utils

import java.nio.file.Files
import akka.stream.scaladsl.Sink
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.ByteArraySerializer

object Producer {

  val broker1 = "localhost:9092"
  val broker2 = "localhost:9093"
  val topic = "image4"
  val zooKeeper = "localhost:2181"

  def main(args: Array[String]) {

    import Utils.materializer

    val files = Utils.filesIn("/usr/local/data/tmt/frames/input/")
    val serializer = new ByteArraySerializer()
    val producer = Utils.createProducer(List(broker1, broker2), serializer, serializer)

    files.runWith(Sink.foreach(file => {
      println(s"sending ${file.getName}")
      val content = Files.readAllBytes(file.toPath)
      val record = new ProducerRecord[Array[Byte], Array[Byte]](topic, content)
      producer.send(record)
    }))
  }
}
