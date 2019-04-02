package com.ytc.sparkreport.exercises

import java.sql.{Connection, DriverManager, PreparedStatement, Timestamp}

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.hive.HiveContext
import java.util
import java.util.{UUID, Calendar, Properties}
import org.apache.spark.rdd.JdbcRDD
import org.apache.spark.sql.{Row, SaveMode, SQLContext}
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{sql, SparkContext, SparkConf}


/**
  * temp http_content
  **/
case class Temp_Http_Content_ParserResult(success: String, lnglatType: String, longitude: String, Latitude: String, radius: String)

/**
  * Created by Administrator on 2016/11/15.
  */
object ParserMain {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    //.setAppName("XXX_ParserHttp").setMaster("local[1]").setMaster("spark://172.21.7.10:7077").setJars(List("xxx.jar"))
    //.set("spark.executor.memory", "10g")
    val sc = new SparkContext(conf)
    val hiveContext = new HiveContext(sc)

    // use abc_hive_db;
    hiveContext.sql("use userdb")
    // error date format:2016-11-15，date format must be 20161115
    val rdd = hiveContext.sql("select host,http_content from default.http where hour>='20161115' and hour<'20161116'")

    // toDF() method need this line...
    import hiveContext.implicits._

    // (success, lnglatType, longitude, latitude, radius)
    val rdd2 = rdd.map(s => parse_http_context(s.getAs[String]("host"), s.getAs[String]("http_content"))).filter(s => s._1).map(s => Temp_Http_Content_ParserResult(s._1.toString(), s._2, s._3, s._4, s._5)).toDF()
    rdd2.registerTempTable("Temp_Http_Content_ParserResult_20161115")
    hiveContext.sql("create table Temp_Http_Content_ParserResult20161115 as select * from Temp_Http_Content_ParserResult_20161115")

    sc.stop()
  }

  /**
    * @ summary: 解析http_context字段信息
    * @ param http_context 参数信息
    * @ result 1:是否匹配成功；
    * @ result 2:匹配出的是什么经纬度的格式：
    * @ result 3:经度；
    * @ result 4:纬度,
    * @ result 5:radius
    **/
  def parse_http_context(host: String, http_context: String): (Boolean, String, String, String, String) = {
    if (host == null || http_context == null) {
      return (false, "", "", "", "")
    }

    //    val result2 = parse_http_context(“api.map.baidu.com”,"renderReverse&&renderReverse({\"status\":0,\"result\":{\"location\":{\"lng\":120.25088311933617,\"lat\":30.310684375444877},\"formatted_address\":\"???????????????????????????????????????\",\"business\":\"\",\"addressComponent\":{\"country\":\"??????\",\"country_code\":0,\"province\":\"?????????\",\"city\":\"?????????\",\"district\":\"?????????\",\"adcode\":\"330104\",\"street\":\"????????????\",\"street_number\":\"\",\"direction\":\"\",\"distance\":\"\"},\"pois\":[{\"addr\":\"????????????5277???\",\"cp\":\" \",\"direction\":\"???\",\"distance\":\"68\",\"name\":\"????????????????????????????????????\",\"poiType\":\"????????????\",\"point\":{\"x\":120.25084961536486,\"y\":30.3112150")
    //    println(result2._1 + ":" + result2._2 + ":" + result2._3 + ":" + result2._4 + ":" + result2._5)

    var success = false
    var lnglatType = ""
    var longitude = ""
    var latitude = ""
    var radius = ""
    var lowerCaseHost = host.toLowerCase().trim();
    val lowerCaseHttp_Content = http_context.toLowerCase()
    //    api.map.baidu.com
    //    "result":{"location":{"lng":120.25088311933617,"lat":30.310684375444877},
    //    "confidence":25
    //     --renderReverse&&renderReverse({"status":0,"result":{"location":{"lng":120.25088311933617,"lat":30.310684375444877},"formatted_address":"???????????????????????????????????????","business":"","addressComponent":{"country":"??????","country_code":0,"province":"?????????","city":"?????????","district":"?????????","adcode":"330104","street":"????????????","street_number":"","direction":"","distance":""},"pois":[{"addr":"????????????5277???","cp":" ","direction":"???","distance":"68","name":"????????????????????????????????????","poiType":"????????????","point":{"x":120.25084961536486,"y":30.3112150
    if (lowerCaseHost.equals("api.map.baidu.com")) {
      val indexLng = lowerCaseHttp_Content.indexOf("\"lng\"")
      val indexLat = lowerCaseHttp_Content.indexOf("\"lat\"")
      if (lowerCaseHttp_Content.indexOf("\"location\"") != -1 && indexLng != -1 && indexLat != -1) {
        var splitstr: String = "\\,|\\{|\\}"
        var uriItems: Array[String] = lowerCaseHttp_Content.split(splitstr)
        var tempItem: String = ""
        lnglatType = "BD"
        success = true
        for (uriItem <- uriItems) {
          tempItem = uriItem.trim()
          if (tempItem.startsWith("\"lng\":")) {
            longitude = tempItem.replace("\"lng\":", "").trim()
          } else if (tempItem.startsWith("\"lat\":")) {
            latitude = tempItem.replace("\"lat\":", "").trim()
          } else if (tempItem.startsWith("\"confidence\":")) {
            radius = tempItem.replace("\"confidence\":", "").trim()
          }
        }
      }
    }
    else if (lowerCaseHost.equals("loc.map.baidu.com")) {
        //......
    }

    longitude = longitude.replace("\"", "")
    latitude = latitude.replace("\"", "")
    radius = radius.replace("\"", "")

    (success, lnglatType, longitude, latitude, radius)
  }
}