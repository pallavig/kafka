import java.util.Properties
import kafka.consumer.{Consumer, ConsumerConfig}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

object Hello {
  val broker = "localhost:9092"
  val readTopic: String = "test"
  val group = "1"
  val zookeeper = "localhost:2181"

  def main(args: Array[String]): Unit = {
    println("Hello, world!")
    val consumer = createConsumerFor(readTopic, group, zookeeper)
    val producer = createProducer

    val record1 = new ProducerRecord[String, String]("test", "hey.. there I am using laptop!!")
    producer.send(record1)

    while (true) {
      val message = consumer.next()
      println(message.message().map(_.toChar).mkString(""))
    }
  }
  
  def createConsumerFor(topic: String, groupId: String, zooKeeper: String) = {
    val consumerProps = new Properties()
    consumerProps.put("zookeeper.connect", zooKeeper)
    consumerProps.put("group.id", groupId)

    Consumer.create(new ConsumerConfig(consumerProps))
      .createMessageStreams(Map(topic -> 1))
      .get(topic)
      .get
      .head
      .iterator()
  }


  def createProducer = {
    val serializer = new StringSerializer()

    val producerProps = new Properties()
    producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, broker)

    new KafkaProducer[String, String](producerProps, serializer, serializer)
  }
}
