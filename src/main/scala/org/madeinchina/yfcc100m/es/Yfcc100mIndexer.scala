package org.madeinchina.yfcc100m.es

import java.text.SimpleDateFormat
import java.util.Calendar

import com.sksamuel.elastic4s.http.ElasticDsl._
import com.sksamuel.elastic4s.http.HttpClient
import com.sksamuel.elastic4s.update._
import org.madeinchina.yfcc100m.es.Yfcc100mIndexer._

class Yfcc100mIndexer(client: HttpClient) {

  def indexData(data: String) = {
    client.execute(generateIndexingDsl(data))
  }

  def bulkIndex(lists: List[UpdateDefinition]) = {
    println("lists:" + lists.size)
    client.execute {
      bulk(lists)
    }
  }

}

object Yfcc100mIndexer {

  def stringToUpdateDefinition(doc: String) = {
    Yfcc100mIndexer.generateIndexingDsl(doc)
  }

  def generateIndexingDsl(data: Yfcc100m) = {
    val id = data.id
    update(id) in exIndexer(data.dateUploaded) / "post" docAsUpsert Map(
      "id" -> id,
      "linenumber" -> data.lineNumber,
      "id" -> data.id,
      "hash" -> data.hash,
      "usernsid" -> data.userNsid,
      "nickname" -> data.nickName,
      "datetaken" -> data.dateTaken,
      "dateuploaded" -> data.dateUploaded,
      "capturedevice" -> data.captureDevice,
      "title" -> data.title,
      "description" -> data.description,
      "usertags" -> data.userTags,
      "machinetags" -> data.machineTags,
      "geo" -> Map("lat" -> data.latitude, "lon" -> data.longitude),
      "accuracy" -> data.accuracy,
      "url" -> data.url,
      "downloadurl" -> data.downloadUrl,
      "licensename" -> data.licenseName,
      "licenseurl" -> data.licenseUrl,
      "serveridentifier" -> data.serverIdentifier,
      "farmidentifier" -> data.farmIdentifier,
      "secret" -> data.secret,
      "secretoriginal" -> data.secretOriginal,
      "marker" -> data.marker
    )
  }

  def exIndexer(datetaken: Long): String = {
    val postDate = {
      val calendar = Calendar.getInstance()
      calendar.setTimeInMillis(datetaken)
      calendar
    }
    "yfcc100m_" + new SimpleDateFormat("yyyy_MM").format(postDate.getTime)
  }

  implicit def string2Yfcc100m(data: String): Yfcc100m = {
    val splitData = data.split("\t")
    val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    Yfcc100m(
      lineNumber = splitData(0).toLong,
      id = splitData(1),
      hash = splitData(2),
      userNsid = splitData(3),
      nickName = splitData(4),
      dateTaken = Option(splitData(5)) match {
        case Some(x) if (!x.equals("null")) => sdf.parse(x).getTime
        case _                              => 0L
      },
      dateUploaded = Option(splitData(6)) match {
        case Some(x) => x.toLong * 1000
        case None    => 0L
      },
      captureDevice = splitData(7),
      title = splitData(8),
      description = splitData(9),
      userTags = splitData(10).split(","),
      machineTags = splitData(11).split(","),
      longitude = Option(splitData(12)).filter(_.nonEmpty) match {
        case Some(x) => x.toDouble
        case None    => 0D
      },
      latitude = Option(splitData(13)).filter(_.nonEmpty) match {
        case Some(x) => x.toDouble
        case None    => 0D
      },
      accuracy = Option(splitData(14)).filter(_.nonEmpty) match {
        case Some(x) => x.toInt
        case None    => 0
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
        case None    => 0
      }
    )
  }
}

case class Yfcc100m(lineNumber: Long,
                    id: String,
                    hash: String,
                    userNsid: String,
                    nickName: String,
                    dateTaken: Long,
                    dateUploaded: Long,
                    captureDevice: String,
                    title: String,
                    description: String,
                    userTags: Array[String],
                    machineTags: Array[String],
                    longitude: Double,
                    latitude: Double,
                    accuracy: Int,
                    url: String,
                    downloadUrl: String,
                    licenseName: String,
                    licenseUrl: String,
                    serverIdentifier: String,
                    farmIdentifier: String,
                    secretOriginal: String,
                    secret: String,
                    extension: String,
                    marker: Int)
