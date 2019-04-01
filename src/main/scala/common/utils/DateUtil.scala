package common.utils

import java.util.{Calendar, Date}

import org.apache.commons.lang3.time.DateFormatUtils

object DateUtil {
  def getEndTimeByPeriod(period: Int, localtime: Long): String = {
    if (period == 300) {
      getEndTimeByFiveMinite(localtime)
    } else if (period == 600) {
      getEndTimeByTenMinite(localtime)
    } else {
      getEndTimeByDay(localtime)
    }
  }


  def main(args: Array[String]): Unit = {
    println(getEndTimeByTenMinite(new Date().getTime))
  }

  def getEndTimeByAnHour(date: Date): String = {
    val calendar = Calendar.getInstance
    calendar.setTime(date)
    calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 1)
    calendar.set(Calendar.MINUTE, 0)
    DateFormatUtils.format(calendar.getTime, "yyyy-MM-dd HH")
  }

  def getEndTimeByDay(date: Long): String = {
    val calendar = Calendar.getInstance
    calendar.setTimeInMillis(date)
    DateFormatUtils.format(calendar.getTime, "yyyy-MM-dd")
  }

  def getEndTimeByFiveMinite(date: Long): String = {
    val calendar = Calendar.getInstance
    calendar.setTimeInMillis(date)
    val minites = calendar.get(Calendar.MINUTE)
    var newMinites = 0
    if (minites % 10 < 5) newMinites = minites - minites % 10 + 5
    else newMinites = minites - minites % 10 + 10
    calendar.set(Calendar.MINUTE, newMinites)
    DateFormatUtils.format(calendar.getTime, "yyyy-MM-dd HH:mm")
  }

  def getEndTimeByTenMinite(date: Long): String = {
    val calendar = Calendar.getInstance
    calendar.setTimeInMillis(date)
    val minites = calendar.get(Calendar.MINUTE)
    var newMinites = minites - minites % 10 + 10
    calendar.set(Calendar.MINUTE, newMinites)
    DateFormatUtils.format(calendar.getTime, "yyyy-MM-dd HH:mm")
  }

}
