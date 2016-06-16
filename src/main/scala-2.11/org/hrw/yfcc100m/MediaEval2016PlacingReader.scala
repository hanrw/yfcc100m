package org.hrw.yfcc100m

import java.io.InputStream

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream

import scala.io.{Source => IOSource}

object MediaEval2016PlacingReader {

  private lazy implicit val system = ActorSystem("mediaeval2016")
  private implicit val materializer = ActorMaterializer()

  def main(args: Array[String]) {
    source("mediaeval2016_placing_train_photo.bz2").via(flow()).runWith(Sink.ignore)
  }

  def flow() = Flow[String].map { doc =>
    println(doc)
  }

  def source(filename: String): Source[String, NotUsed] = {
    val fis = ClassLoader.getSystemResourceAsStream(filename)
    val bcis = new BZip2CompressorInputStream(fis)
    val iter = new WikiArticleIterator(bcis)
    Source.fromIterator(() => iter)
  }

}


case class WikiArticle(title: String, text: String)

class WikiArticleIterator(val is: InputStream) extends Iterator[String] {
  val source = IOSource.fromInputStream(is).getLines()

  override def hasNext: Boolean = source.hasNext

  override def next(): String = source.next()
}
