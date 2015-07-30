package utils

import java.io.File
import java.util.Properties

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import kafka.consumer.{ConsumerConfig, Consumer}
import kafka.utils.VerifiableProperties
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig}
import org.apache.kafka.common.serialization.Serializer

object Utils {

  implicit val actorSystem = ActorSystem("kafka")
  implicit val materializer = ActorMaterializer()

  def createProducer[K, V](brokersSocketAddress: String, keySerializer: Serializer[K], valueSerializer: Serializer[V]) = {
    val producerProps = new Properties()
    producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokersSocketAddress)

    new KafkaProducer[K, V](producerProps, keySerializer, valueSerializer)
  }


  def createConsumerFor(topic: String, groupId: String, zooKeeper: String) = {
    val consumerProps = new Properties()
    consumerProps.put("zookeeper.connect", zooKeeper)
    consumerProps.put("group.id", groupId)

    Consumer.create(new ConsumerConfig(consumerProps))
  }


  def filesIn(directoryName: String) = {
    Source(new File(directoryName).listFiles.toList)
  }
}
