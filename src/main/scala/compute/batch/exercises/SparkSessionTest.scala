package compute.batch.exercises

import org.apache.spark.sql.SparkSession

object SparkSessionTest {

  def main(args: Array[String]): Unit = {

    val spark:SparkSession = SparkSession.builder()
                                       .appName("")
                                       .master("local[2]")
                                       //.enableHiveSupport()
                                       //.config("","")
                                       .getOrCreate()
    //set the runtime options
    spark.conf.set("spark.sql.shuffle.partitions",6);
    spark.conf.set("spark.executor.memory","2g")

    //get all settings
    val configMap:Map[String,String] = spark.conf.getAll

    //fetch meta data from the catalog
    spark.catalog.listDatabases().show(false)
    spark.catalog.listTables().show(false)

    //create DataSet by range()
    val numDS = spark.range(5,100,5)
    numDS.orderBy("id").show(5)
    numDS.describe().show()

    //create DataFrame by createDataFrame()
    val langPercentDF = spark.createDataFrame(List(("Scala",35),("Python",30),("R",17),("Java",2)))
    val lpDF = langPercentDF.withColumnRenamed("_1","language").withColumnRenamed("_2","percent")
    lpDF.orderBy("percent").show(false)

    //read files
    import  spark.implicits
    val jsonFile = args(0)
    val zipsDF = spark.read.json(jsonFile)

    //use SparkSQL
    zipsDF.createOrReplaceTempView("zips_table")
    zipsDF.cache()
    val resultsDF = spark.sql("select city,pop, state,zip from zips_table")
    resultsDF.show(10)

    //read table in hive

    //......

  }
}
