import java.io._
import scala.io.Source
import scala.math.random
import org.apache.spark._

//Crime data analysis with community code and number of crimes.
//Please check the district data by https://data.cityofchicago.org/d/fthy-xz3r
object CrimeByDistrict extends App {
  
  override def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Spark Sort").setMaster("local")
    val sc = new SparkContext(conf)
    
    var tf = sc.textFile("file:////home/cloudera/workspace/sparkexample/crimes.csv")

    var counts = tf.map(_.split(",")(11)).filter(x => x.length() ==3 ).map(word => (word.trim(), 1)).reduceByKey(_ + _).sortBy(_._2,false)

    val writer = new PrintWriter(new File("CrimeByDistrict.txt"))
    val res = counts.collect()
    for (n <- res){ writer.println(n.toString())}
    
    writer.close()
  }
}