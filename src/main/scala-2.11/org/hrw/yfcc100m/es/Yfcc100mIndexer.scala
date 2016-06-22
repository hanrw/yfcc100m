package org.hrw.yfcc100m.es

import java.text.SimpleDateFormat
import java.util.Calendar

import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import org.hrw.yfcc100m.Yfcc100m
import org.hrw.yfcc100m.Yfcc100mReader._

class Yfcc100mIndexer(client: ElasticClient) {

  def indexData(data: String) = {
    client.execute(insertDsl(data))
  }


  def exIndexer(datetaken: Long): String = {
    val postDate = {
      val calendar = Calendar.getInstance()
      calendar.setTimeInMillis(datetaken)
      calendar
    }
    "yfcc100m_" + new SimpleDateFormat("yyyy_MM").format(postDate.getTime)
  }

  def insertDsl(data: Yfcc100m) = {
    val id = data.id
    update id id in exIndexer(data.dateTaken) / "post" docAsUpsert Map(
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


}

object Yfcc100mIndexer {

}