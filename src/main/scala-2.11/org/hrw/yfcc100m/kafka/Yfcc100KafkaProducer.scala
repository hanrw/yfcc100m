package org.hrw.yfcc100m.kafka

import akka.NotUsed
import akka.actor.ActorSystem
import akka.kafka._
import akka.kafka.scaladsl._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{ByteArraySerializer, StringSerializer}

class Yfcc100KafkaProducer(system: ActorSystem) {

  private val producerSettings = ProducerSettings(system, new ByteArraySerializer, new StringSerializer)
    .withBootstrapServers("localhost:9092")


  def run(source: Source[String,NotUsed])(implicit  actorMaterializer:ActorMaterializer) {
    source.map(elem => ProducerMessage.Message(new ProducerRecord[Array[Byte], String]("test", elem), elem))
      .via(Producer.flow(producerSettings))
      .map { result =>
        val record = result.message.record
        println(s"${record.topic}/${record.partition} ${result.offset}: ${record.value} (${result.message.passThrough}")
        result
      }.runWith(Sink.ignore)
  }
}
