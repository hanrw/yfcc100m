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

split yfcc100m dataset into multi files
split -b 1024m "yfcc100m_dataset.bz2" "yfcc100m_dataset.bz2.part-"
split results:
yfcc100m_dataset.bz2.part-aa	yfcc100m_dataset.bz2.part-af	yfcc100m_dataset.bz2.part-ak
yfcc100m_dataset.bz2.part-ab	yfcc100m_dataset.bz2.part-ag	yfcc100m_dataset.bz2.part-al
yfcc100m_dataset.bz2.part-ac	yfcc100m_dataset.bz2.part-ah	yfcc100m_dataset.bz2.part-am
yfcc100m_dataset.bz2.part-ad	yfcc100m_dataset.bz2.part-ai	yfcc100m_dataset.bz2.part-an
yfcc100m_dataset.bz2.part-ae	yfcc100m_dataset.bz2.part-aj
