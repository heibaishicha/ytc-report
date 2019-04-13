//package compute.batch.exercises
//
//import org.apache.kafka.common.TopicPartition
//import org.apache.kafka.common.serialization.StringDeserializer
//import org.apache.spark.streaming.kafka010.{CanCommitOffsets, HasOffsetRanges, KafkaUtils}
//import org.apache.spark.streaming.{Seconds, StreamingContext}
//import org.springframework.expression.spel.ast.Assign
//
//object GetZhiDingOffset {
//  def main(args: Array[String]): Unit = {
//    val kafkaParams = Map[String, Object](
//      "bootstrap.servers" -> "localhost:9092",
//      "key.deserializer" -> classOf[StringDeserializer],
//      "value.deserializer" -> classOf[StringDeserializer],
//      "group.id" -> "use_a_separate_group_id_for_each_stream",
//      //      "auto.offset.reset" -> "latest",
//      "enable.auto.commit" -> (false: java.lang.Boolean)
//    )
//    val ssc = new StreamingContext(OpContext.sc, Seconds(2))
//    val fromOffsets = Map(new TopicPartition("test", 0) -> 1100449855L)
//    val stream = KafkaUtils.createDirectStream[String, String](
//      ssc,
//      PreferConsistent,
//      Assign[String, String](fromOffsets.keys.toList, kafkaParams, fromOffsets)
//    )
//
//    stream.foreachRDD(rdd => {
//      val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
//      for (o <- offsetRanges) {
//        println(s"${o.topic} ${o.partition} ${o.fromOffset} ${o.untilOffset}")
//      }
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
