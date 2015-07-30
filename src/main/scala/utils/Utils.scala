package utils

import java.util.Properties

import kafka.consumer.{ConsumerConfig, Consumer}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig}
import org.apache.kafka.common.serialization.Serializer

object Utils {

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
      .createMessageStreams(Map(topic -> 1))
      .get(topic)
      .get
      .head
      .iterator()
  }
}
