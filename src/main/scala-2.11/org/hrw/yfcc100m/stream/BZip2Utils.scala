package org.hrw.yfcc100m.stream

import java.io.FileInputStream

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream

object BZip2Utils {

  def load(file: String) = {
    val fis = new FileInputStream(file)
    new BZip2CompressorInputStream(fis)
  }

}
