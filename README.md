# yfcc100m
1.Goal process yfcc100m dataset into elasticsearch
2.Environment
  MacBook Pro (Retina, 13-inch, Early 2015)
  2.7 GHz Intel Core i5
  8 GB 1867 MHz DDR3
3.benchmark
Using
 def source(filename: String): Source[String, NotUsed] = {
    val fis = new FileInputStream(filename)
    val bcis = new BZip2CompressorInputStream(fis)
    val iter = new StringIterator(bcis)
    Source.fromIterator(() => iter)
  }
read processing speed max:5700+ line/seconds