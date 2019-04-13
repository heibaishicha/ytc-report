package compute.batch.exercises

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object WordCount {

  def main(args : Array[String]){
    val conf =new SparkConf().setAppName("WordCountScala").setMaster("local")
    val sc = new SparkContext(conf)
    val lines=sc.textFile("C:\\work\\myItem\\bigdata\\spark-report\\README.md")
    val words = lines.flatMap(line => line.split(" "))
    val pairs = words.map(word => (word,1))
    val wordCounts = pairs.reduceByKey(_+_)
    wordCounts.foreach(wordCount => println (wordCount._1 +"apperared" +wordCount._2 + " times."))
  }

}
