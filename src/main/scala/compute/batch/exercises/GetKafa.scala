package compute.batch.exercises

import org.apache.spark.sql.SparkSession

object GetKafa {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder()
      .appName("Spark structured streaming Kafka example")
      .master("local[2]")
      .getOrCreate()

    val inputstream = spark.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "127.0.0.1:9092")
      .option("subscribe", "testss")
      .load()
    import spark.implicits._

    val query = inputstream.select($"key", $"value")
      .as[(String, String)].map(kv => kv._1 + " " + kv._2).as[String]
      .writeStream
      .outputMode("append")
      .format("console")
      .start()

    query.awaitTermination()
  }

}
