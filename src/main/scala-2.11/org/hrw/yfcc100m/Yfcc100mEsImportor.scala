package org.hrw.yfcc100m

import akka.actor.ActorSystem
import akka.stream.scaladsl._
import akka.stream.{ActorMaterializer, ActorMaterializerSettings, Supervision}
import com.sksamuel.elastic4s.{ElasticClient, ElasticsearchClientUri}
import org.elasticsearch.common.settings.Settings
import org.hrw.yfcc100m.es.Yfcc100mIndexer
import org.hrw.yfcc100m.flow.Yfcc100mFlow
import org.hrw.yfcc100m.source.Yfcc100mSource

object Yfcc100mEsImportor extends App {
  private lazy implicit val system = ActorSystem("Yfcc100mReader")
  val decider: Supervision.Decider = { e =>
    println("Unhandled exception in stream", e)
    Supervision.Stop
  }
  val materializerSettings = ActorMaterializerSettings(system).withSupervisionStrategy(decider)
  implicit val materializer = ActorMaterializer(materializerSettings)(system)
  val client = {
    val settings = Settings.settingsBuilder().put("cluster.name", "es-cluster").build()
    ElasticClient.transport(settings, ElasticsearchClientUri("elasticsearch://192.168.2.14:9300"))
  }
  val yfcc100mIndexer = new Yfcc100mIndexer(client)

  Yfcc100mSource.bz2AsSource("/home/hanrenwei/Github/yfcc100m/src/main/resources/yfcc100m_dataset.bz2").via(Yfcc100mFlow.flow(Yfcc100mIndexer.stringToUpdateDefinition(_))).grouped(1000).mapAsync(8)(updates => yfcc100mIndexer.bulkIndex(updates.toList)).runWith(Sink.ignore)
}
