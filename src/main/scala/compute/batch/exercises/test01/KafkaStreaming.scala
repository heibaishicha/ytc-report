package compute.batch.exercises.test01
import java.text.SimpleDateFormat
import java.util.Date
import java.sql.Connection
import org.apache.log4j.PropertyConfigurator
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Time,Seconds, StreamingContext}
import org.apache.spark.{SparkContext, SparkConf}
import org.joda.time.DateTime
import org.slf4j.LoggerFactory
import scala.collection.mutable.Map

object KafkaStreaming {

  val logger = LoggerFactory.getLogger(this.getClass)
  PropertyConfigurator.configure(System.getProperty("user.dir")+"\\src\\log4j.properties")

  case class result(ftime:String,hour:String,orderid:Long,memberid:Long,platform:String,iamount:Double,orderamount:Double)extends Serializable{
    override  def toString: String="%s\t%s\t%d\t%d\t%s\t%.2f\t%.2f".format(ftime, hour,orderid,memberid,platform,iamount,orderamount)
  }


  def getFormatDate(date:Date,format:SimpleDateFormat): String ={
    format.format(date)
  }
  def stringFormatTime(time:String,simpleformat:SimpleDateFormat): Date ={
    simpleformat.parse(time)
  }

  // kafka中的value解析为Map
  def valueSplit(value:String): Map[String,String] ={
    val x = value.split("&")
    val valueMap:Map[String,String] = Map()
    x.foreach { kvs =>
      if (!kvs.startsWith("__")){
        val kv = kvs.split("=")
        if (kv.length==2) {
          valueMap += (kv(0) -> kv(1))
        }
      }

    }
    valueMap
  }

  // 实现类似where的条件,tips:优先过滤条件大的减少后续操作
  def filterRegex(map:Map[String,String]): Boolean ={
    //过滤操作类型，控制为支付操作
    val oper_type = map.getOrElse("oper_type","-1")
    if(!oper_type.equals("2") && !oper_type.equals("3"))
      return false
    // 过滤未支付成功记录
    if(!map.getOrElse("paymentstatus","0").equals("1"))
      return false
    // 过滤无效支付ip
    val paymentip =  map.getOrElse("paymentip",null)
    if (paymentip.startsWith("10.10")||paymentip.startsWith("183.62.134")||paymentip.contains("127.0.0.1"))
      return false
    return true
  }
  // 实现类似 case when的方法，上报的p字段不一定为数值
  def getPlatform(p:String,x:Int): String ={
    val platformname = (p,x) match{
      case (p,x) if(Array[String]("1","2","3").contains(p)) => "wap"
      case (p,x) if(Array[String]("4","8").contains(p)&& x!=18) =>"andriod"
      case (p,x) if((Array[String]("5","7","51","100").contains(p))&&(p!=18)) => "ios"
      case _ => "pc"
    }
    platformname
  }
  // 数据库写入
  def insertIntoMySQL(con:Connection,sql:String,data:result): Unit ={
    // println(data.toString)
    try {
      val ps = con.prepareStatement(sql)
      ps.setString(1, data.ftime)
      ps.setString(2, data.hour)
      ps.setLong(3,data.orderid)
      ps.setLong(4, data.memberid)
      ps.setString(5, data.platform)
      ps.setDouble(6, data.iamount)
      ps.setDouble(7, data.orderamount)
      ps.executeUpdate()
      ps.close()

    }catch{
      case exception:Exception=>
        logger.error("Error in execution of query "+exception.getMessage+"\n-----------------------\n"+exception.printStackTrace()+"\n-----------------------------")
    }
  }
  def createContext(zkqurm:String,topic:scala.Predef.Map[String,Int],checkPointDir:String): StreamingContext ={


    val simpleformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val dateFormat = new SimpleDateFormat("yyyyMMdd")
    val timeFormat = new SimpleDateFormat("HH:mm")

    val sql ="insert into t_ssc_toufang_result_mi(ftime,hour,orderid,memberid,platform,iamount,orderamount) values(?,?,?,?,?,?,?);"


    val conf = new SparkConf()
    conf.setAppName("Scala Streaming read kafka")
    // VM option -Dspark.master=local
    // conf.setMaster("local[4]")
    val sc = new SparkContext(conf)

    val totalcounts = sc.accumulator(0L,"Total count")

    val ssc =  new StreamingContext(sc,Seconds(60))
    //ssc.checkpoint(checkPointDir)
    //统计各平台最近一分钟实时注册收入 时间段，平台，金额，订单数
    val lines = KafkaUtils.createStream(ssc, zkqurm, "mytopic_local",topic).map(_._2)

    val filterRecord = lines.filter(x => !x.isEmpty).map(valueSplit).filter(filterRegex).map{x =>
      val orderdate = stringFormatTime(x.getOrElse("orderdate",null),simpleformat)
      val day = getFormatDate(orderdate,dateFormat)
      val hour = getFormatDate(orderdate,timeFormat)
      var orderamount = x.getOrElse("orderamount","0").toDouble
      if (x.getOrElse("oper_type",-1)==3)
        orderamount = -1*orderamount
      val res = new result(
        day
        ,hour
        ,x.getOrElse("orderid",null).toLong
        ,x.getOrElse("memberid",null).toLong
        ,getPlatform(x.getOrElse("ordersrc",null),x.getOrElse("itype",null).toInt)
        ,x.getOrElse("iamount","0").toDouble
        ,orderamount
      )
      res
    }

    filterRecord.foreachRDD((x: RDD[result],time: Time) =>{
      if(!x.isEmpty()) {
        // 打印一下这一批batch的处理时间段以及累计的有效记录数(不含档次)
        println("--"+new DateTime(time.milliseconds).toString("yyyy-MM-dd HH:mm:ss")+"--totalcounts:"+totalcounts.value+"-----")
        x.foreachPartition{res =>
        {
          if(!res.isEmpty){
            val connection = ConnectionPool.getConnection.getOrElse(null)
            res.foreach {
              r: result =>totalcounts.add(1L)
                insertIntoMySQL(connection, sql, r)
            }
            ConnectionPool.closeConnection(connection)
          }
        }
        }
      }
    })

    ssc
  }
  // =================================================================

  def  main(args:Array[String]): Unit ={
    val zkqurm = "10.10.10.177:2181,10.10.10.175:2181,10.10.10.179:2181"

    val topic = scala.Predef.Map("t_fw_00015"->30)
    val checkPointDir ="/user/root/sparkcheck"
    val ssc = StreamingContext.getOrCreate(checkPointDir,
      () => {
        createContext(zkqurm, topic,checkPointDir)
      })
    ssc.start()
    ssc.awaitTermination()
  }
}
