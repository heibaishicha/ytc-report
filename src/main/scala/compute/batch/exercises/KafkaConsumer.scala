//package compute.batch.exercises
//
//import kafka.serializer.StringDecoder
//import org.apache.spark.SparkConf
//import org.apache.spark.streaming.{Seconds, StreamingContext}
//import org.apache.spark.streaming.kafka._
//
//
///**
//  * Created by   spark1.6版本
//  */
//object KafkaConsumer {
//
//  val numThreads = 1
//  val topics = "mytest"
//  val zkQuorum = "192.168.1.115:2181"
//  val group = "consumer1"
//  val brokers = "192.168.1.115:9092"
//
//
//
//  def main(args: Array[String]): Unit = {
//    createstream
//  }
//
//  /**
//    *bin/kafka-console-producer.sh –broker-list localhost:9092 –topic mytest
//    */
//  def createstream()={
//    val conf = new SparkConf().setAppName("kafka test").setMaster("local[2]")
//    val ssc = new StreamingContext(conf,Seconds(10));
//
//    val topicMap = topics.split(",").map((_, numThreads.toInt)).toMap
//    val lines = KafkaUtils.createStream(ssc, zkQuorum, group, topicMap).map(_._2)
//    val words = lines.flatMap(_.split(" ")).map(x=>(x,1))
//    words.reduceByKey(_ + _).print()
//    ssc.start()
//    ssc.awaitTermination()
//  }
//
//
//  def direct()={
//    val conf = new SparkConf().setMaster("local[2]").setAppName("kafka test")
//    val ssc = new StreamingContext(conf,Seconds(10))
//    val topicsSet = topics.split(",").toSet
//    val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers)
//    val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
//      ssc, kafkaParams, topicsSet)
//    val lines = messages.map(_._2)
//    val words = lines.flatMap(_.split(" ")).map(x=>(x,1))
//
//    words.reduceByKey(_ + _).print()
//    ssc.start()
//    ssc.awaitTermination()
//  }
//}