package org.madeinchina.yfcc100m.source

import org.madeinchina.yfcc100m.stream.StreamUtils

object Yfcc100mSource {

  def bz2AsSource(file: String) = {
    StreamUtils.bz2asSource(file)
  }

}
