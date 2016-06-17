package org.hrw.yfcc100m
import org.scalatest.{Matchers, FlatSpec}

class Yfcc100mReaderSpec extends FlatSpec with Matchers{
val data = "6639\t" +
  "6162540254\t" +
  "4ccb23d8746b8d9d76ffd603fc7988\t" +
  "15087210@N00\t" +
  "henribergius\t" +
  "2011-09-10 21:13:32.0\t" +
  "1316432672\t" +
  "Nokia+N950\t" +
  "Around+the+waterpipe\t" +
  "\t" +
  "helsinki,party,waterpipe\t" +
  "\t" +
  "\t" +
  "\t" +
  "\t" +
  "http://www.flickr.com/photos/15087210@N00/6162540254/\t" +
  "http://farm7.staticflickr.com/6167/6162540254_5d22abd711.jpg\t" +
  "Attribution-ShareAlike License\thttp://creativecommons.org/licenses/by-sa/2.0/\t" +
  "6167\t" +
  "7\t" +
  "5d22abd711\t" +
  "44e5acb0f2\t" +
  "jpg\t" +
  "0"

  "the reader" should "transform data into yfcc100m" in {
    Yfcc100mReader.string2Yfcc100m(data).id should be("6162540254")
  }

}
