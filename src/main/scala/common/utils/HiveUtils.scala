package common.utils

import java.sql.{Connection, DriverManager, ResultSet, Statement}

object HiveUtils {

  def operatorHive: Unit = {
    Class.forName("org.apache.hive.jdbc.HiveDriver")
    val url = "jdbc:hive2://192.168.68.160:10000"
    val connection: Connection = DriverManager.getConnection(url, "root", "123456")
    val createStatement: Statement = connection.createStatement()
    val query: ResultSet = createStatement.executeQuery("select * from userdb.people limit 5")
    while (query.next()) {
      println(query.getString(1))
    }
  }

  def main(args: Array[String]): Unit = {
    operatorHive
  }

}
