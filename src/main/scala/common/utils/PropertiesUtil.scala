package common.utils

import java.util.Properties

/**
  * Properties的工具类
  * Created by
  */
object PropertiesUtil {

  /**
    * 获取配置文件Properties对象
    * @author make
    * @return java.util.Properties
    */
  def getProperties() :Properties = {
    val properties = new Properties()
    //读取源码中resource文件夹下的my.properties配置文件,得到一个properties
    val reader = getClass.getResourceAsStream("/my.properties")
    properties.load(reader)
    properties
  }

  /**
    * 获取配置文件中key对应的value
    * @author make
    * @return java.util.Properties
    */
  def getPropString(key : String) : String = {
    getProperties().getProperty(key)
  }

  /**
    * 获取配置文件中key对应的整数值，可能后面这里会需要其他的值
    * @author yore
    * @return java.util.Properties
    */
  def getPropInt(key : String) : Int = {
    getProperties().getProperty(key).toInt
  }

  /**
    * 获取配置文件中key对应的布尔值
    * @author make
    * @return java.util.Properties
    */
  def getPropBoolean(key : String) : Boolean = {
    getProperties().getProperty(key).toBoolean
  }

}
