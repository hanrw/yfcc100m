package org.hrw.yfcc100m.stream

import java.io.InputStream

import scala.io.{Source => IOSource}


class StringIterator(val is: InputStream) extends Iterator[String] {
  val source = IOSource.createBufferedSource(is,500000).getLines()

  override def hasNext: Boolean = source.hasNext

  override def next(): String = source.next()
}
