package org.hrw.yfcc100m

import java.io.FileInputStream
import java.text.SimpleDateFormat

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl._
import akka.stream.{ActorMaterializer, ClosedShape}
import com.sksamuel.elastic4s.{ElasticClient, ElasticsearchClientUri}
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream
import org.elasticsearch.common.settings.Settings
import org.hrw.yfcc100m.es.Yfcc100mIndexer

object Yfcc100mReader {
  implicit def string2Yfcc100m(data: String): Yfcc100m = {
    val splitData = data.split("\t")
    val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    Yfcc100m(
      lineNumber = splitData(0).toLong,
      id = splitData(1),
      hash = splitData(2),
      userNsid = splitData(3),
      nickName = splitData(4),
      dateTaken = sdf.parse(splitData(5)).getTime,
      dateUploaded = splitData(6).toLong * 1000,
      captureDevice = splitData(7),
      title = splitData(8),
      description = splitData(9),
      userTags = splitData(10).split(","),
      machineTags = splitData(11).split(","),
      longitude = Option(splitData(12)).filter(_.nonEmpty) match {
        case Some(x) => x.toDouble
        case None => 0D
      },
      latitude = Option(splitData(13)).filter(_.nonEmpty) match {
        case Some(x) => x.toDouble
        case None => 0D
      },
      accuracy = Option(splitData(14)).filter(_.nonEmpty) match {
        case Some(x) => x.toInt
        case None => 0
      },
      url = splitData(15),
      downloadUrl = splitData(16),
      licenseName = splitData(17),
      licenseUrl = splitData(18),
      serverIdentifier = splitData(19),
      farmIdentifier = splitData(20),
      secretOriginal = splitData(21),
      secret = splitData(22),
      extension = splitData(23),
      marker = Option(splitData(24)).filter(_.nonEmpty) match {
        case Some(x) => x.toInt
        case None => 0
      })
  }

  private lazy implicit val system = ActorSystem("mediaeval2016")
  private implicit val materializer = ActorMaterializer()

  def main(args: Array[String]) {
    val client = {
      val settings = Settings.settingsBuilder().put("client.transport.ignore_cluster_name", true).build()
      ElasticClient.transport(settings, ElasticsearchClientUri("elasticsearch://192.168.99.100:9300"))
    }

    val numCPUs = Runtime.getRuntime().availableProcessors()

//    def flow(yfcc100mIndexer: Yfcc100mIndexer) = Flow[String].map { doc =>
//      println(doc)
//      doc
//    }.mapAsyncUnordered(numCPUs)(yfcc100mIndexer.indexData)

    def flow(yfcc100mIndexer: Yfcc100mIndexer) = Flow[String].map { doc =>
      println(doc)
      yfcc100mIndexer.indexData(doc)
    }

    val yfcc100mIndexer = new Yfcc100mIndexer(client)
            source("yfcc100m_dataset.bz2").via(flow(yfcc100mIndexer)).runWith(Sink.ignore)



//    val graph = RunnableGraph.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[akka.NotUsed] =>
//      import GraphDSL.Implicits._
//      val src = source("/Users/hanrenwei/百度云同步盘/我的文档/DOC/yfcc100m_dataset.bz2")
//
//      src ~> flow(yfcc100mIndexer) ~> Sink.ignore
//      ClosedShape
//    })
//    graph.run()
  }


  def source(filename: String): Source[String, NotUsed] = {
    val fis = new FileInputStream(filename)
    //    val fis = ClassLoader.getSystemResourceAsStream(filename)
    val bcis = new BZip2CompressorInputStream(fis)
    val iter = new StringIterator(bcis)
    Source.fromIterator(() => iter)
  }

  def transform(data: String) = {

  }

}

case class Yfcc100m(lineNumber: Long, id: String, hash: String, userNsid: String, nickName: String,
                    dateTaken: Long, dateUploaded: Long, captureDevice: String, title: String,
                    description: String, userTags: Array[String], machineTags: Array[String], longitude: Double,
                    latitude: Double, accuracy: Int, url: String, downloadUrl: String, licenseName: String,
                    licenseUrl: String, serverIdentifier: String, farmIdentifier: String, secretOriginal: String,
                    secret: String, extension: String, marker: Int)
