//package compute.batch.exercises
//
//import kafka.serializer.StringDecoder
//import org.apache.spark.sql.SparkSession
//import org.apache.spark.streaming.kafka.KafkaUtils
//import org.apache.spark.streaming.{Seconds, StreamingContext}
///*
//* spark消费kafka例子
//    <dependency>
//      <groupId>org.apache.spark</groupId>
//      <artifactId>spark-streaming-kafka-0-8_2.11</artifactId>
//      <version>2.3.0</version>
//    </dependency>
//*/
//object SparkKafkaTest {
//  def main(args: Array[String]): Unit = {
//
//    val spark = SparkSession.builder().appName("spark_kafka").master("local[*]").getOrCreate()
//    val batchDuration = Seconds(5) //时间单位为秒
//    val streamContext = new StreamingContext(spark.sparkContext, batchDuration)
//    streamContext.checkpoint("/Users/eric/SparkWorkspace/checkpoint")
//    val topics = Array("behaviors").toSet
//    val kafkaParams = Map[String, String]("metadata.broker.list" -> "localhost:9092")
//    val stream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](streamContext, kafkaParams, topics)
//    stream.foreachRDD(rdd => {
//      rdd.foreach(line => {
//        println("key=" + line._1 + "  value=" + line._2)
//      })
//    })
//    streamContext.start()  //spark stream系统启动
//    streamContext.awaitTermination() //
//  }
//}
