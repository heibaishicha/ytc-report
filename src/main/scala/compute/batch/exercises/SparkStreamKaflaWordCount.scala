package compute.batch.exercises

import org.apache.spark.SparkConf
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.kafka.common.serialization.StringDeserializer

object SparkStreamKaflaWordCount {

  def main(args: Array[String]): Unit = {

    //创建streamingContext
    var conf=new SparkConf().setMaster("spark://192.168.68.160:7077")
      .setAppName("SparkStreamKaflaWordCount Demo");
    var ssc=new StreamingContext(conf,Seconds(4));
    //创建topic
    //var topic=Map{"test" -> 1}
    var topic=Array("test");
    //指定zookeeper
    //创建消费者组
    var group="con-consumer-group"

    //消费者配置
    val kafkaParam = Map(
      "bootstrap.servers" -> "192.168.68.160:9092,anotherhost:9092",//用于初始化链接到集群的地址
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      //用于标识这个消费者属于哪个消费团体
      "group.id" -> group,
      //如果没有初始化偏移量或者当前的偏移量不存在任何服务器上，可以使用这个配置属性
      //可以使用这个配置，latest自动重置偏移量为最新的偏移量
      "auto.offset.reset" -> "latest",
      //如果是true，则这个消费者的偏移量会在后台自动提交
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    //创建DStream，返回接收到的输入数据
    var stream=KafkaUtils.createDirectStream[String,String](ssc, PreferConsistent,Subscribe[String,String](topic,kafkaParam))

    //每一个stream都是一个ConsumerRecord
    stream.map(s =>(s.key(),s.value())).print();
    ssc.start();
    ssc.awaitTermination();
  }
}