package compute.batch.exercises.test01

import java.sql.{Connection, ResultSet}
import com.jolbox.bonecp.{BoneCP, BoneCPConfig}
import org.slf4j.LoggerFactory

object ConnectionPool {
  val logger = LoggerFactory.getLogger(this.getClass)
  private val connectionPool = {
    try{
      Class.forName("com.mysql.jdbc.Driver")
      val config = new BoneCPConfig()
      config.setJdbcUrl("jdbc:mysql://localhost:3306/test")
      config.setUsername("etl")
      config.setPassword("xxxxx")
      config.setLazyInit(true)

      config.setMinConnectionsPerPartition(3)
      config.setMaxConnectionsPerPartition(5)
      config.setPartitionCount(5)
      config.setCloseConnectionWatch(true)
      config.setLogStatementsEnabled(false)

      Some(new BoneCP(config))
    } catch {
      case exception:Exception=>
        logger.warn("Error in creation of connection pool"+exception.printStackTrace())
        None
    }
  }
  def getConnection:Option[Connection] ={
    connectionPool match {
      case Some(connPool) => Some(connPool.getConnection)
      case None => None
    }
  }
  def closeConnection(connection:Connection): Unit = {
    if(!connection.isClosed) {
      connection.close()

    }
  }
}
