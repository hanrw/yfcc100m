package org.madeinchina.yfcc100m.stream

import akka.NotUsed
import akka.stream.scaladsl.Source

object StreamUtils {
  def bz2asSource(filename: String): Source[String, NotUsed] = {
    val iter = new StringIterator(BZip2Utils.load(filename))
    Source.fromIterator(() => iter)
  }
}
