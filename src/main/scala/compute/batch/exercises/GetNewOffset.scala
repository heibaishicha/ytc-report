//package compute.batch.exercises
//
//import com.google.common.eventbus.Subscribe
//import org.apache.kafka.common.serialization.StringDeserializer
//import org.apache.spark.TaskContext
//import org.apache.spark.streaming.{Seconds, StreamingContext}
//import org.apache.spark.streaming.kafka010.{CanCommitOffsets, HasOffsetRanges, KafkaUtils, OffsetRange}
//import org.spark_project.guava.eventbus.Subscribe
//
//object GetNewOffset {
//
//  def main(args: Array[String]): Unit = {
//    val kafkaParams = Map[String, Object](
//      "bootstrap.servers" -> "localhost:9092",
//      "key.deserializer" -> classOf[StringDeserializer],
//      "value.deserializer" -> classOf[StringDeserializer],
//      "group.id" -> "use_a_separate_group_id_for_each_stream",
//      "auto.offset.reset" -> "latest",
//      "enable.auto.commit" -> (false: java.lang.Boolean)
//    )
//
//    val ssc =new StreamingContext(OpContext.sc, Seconds(2))
//    val topics = Array("test")
//    val stream = KafkaUtils.createDirectStream[String, String](
//      ssc,
//      PreferConsistent,
//      Subscribe[String, String](topics, kafkaParams)
//    )
//    stream.foreachRDD(rdd=>{
//      val offsetRanges=rdd.asInstanceOf[HasOffsetRanges].offsetRanges
//      rdd.foreachPartition(iter=>{
//        val o: OffsetRange = offsetRanges(TaskContext.get.partitionId)
//        println(s"${o.topic} ${o.partition} ${o.fromOffset} ${o.untilOffset}")
//      })
//      stream.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges)
//    })
//
//    //    stream.map(record => (record.key, record.value)).print(1)
//    ssc.start()
//    ssc.awaitTermination()
//  }
//
//
//}
