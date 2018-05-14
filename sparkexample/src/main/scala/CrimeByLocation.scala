import java.io._
import scala.io.Source
import scala.math.random
import org.apache.spark._

//Crime data with location and the corresponding numbers of crimes. 
object CrimeByLocation extends App {
  override def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Spark Sort").setMaster("local")
    val sc = new SparkContext(conf)
    
    var tf = sc.textFile("file:////home/cloudera/workspace/sparkexample/crimes.csv")
    var counts = tf.map(_.split(",")(7)).map(word => (word.trim(), 1)).reduceByKey(_ + _).sortBy(_._2,false)
    val writer = new PrintWriter(new File("CrimeByLocation.txt"))
    val res = counts.collect()
    for (n <- res)
        { writer.println(n.toString())}
    writer.close()
  }  
}