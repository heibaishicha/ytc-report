package common.utils

import java.util.Properties


/**
  * Created by pengshuangbao on 2017/11/13.
  */
object ConfigUtils {

  val in = this.getClass.getResourceAsStream("/config.properties")
  val props = new Properties()
  props.load(in)
  val profile = props.getProperty("profile", "prod")

  val configInProfile = this.getClass.getResourceAsStream("/config-" + profile + ".properties")
  val propsInProfile = new Properties();
  propsInProfile.load(configInProfile);
  props.putAll(propsInProfile);

  def getProperty(key: String): String = {
    return props.getProperty(key)
  }

  def getOrDefault(key: String, defaultValue: String): String = {
    return props.getProperty(key, defaultValue)
  }
}
