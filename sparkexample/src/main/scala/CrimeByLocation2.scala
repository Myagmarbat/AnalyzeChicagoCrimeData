import java.io._
import scala.io.Source
import scala.math.random
import org.apache.spark._

//Crime analysis with location and the percentage of corresponding numbers of crimes by total crimes count. 
object CrimeByLocation2 extends App {
    override def main(args: Array[String]) {
      val conf = new SparkConf().setAppName("Spark Sort").setMaster("local")
      val sc = new SparkContext(conf)
      
      var tf = sc.textFile("file:////home/cloudera/workspace/sparkexample/crimes.csv")
      var counts = tf.map(_.split(",")(7)).map(word => (word.trim(), 1)).reduceByKey(_ + _).sortBy(_._2,false)
      var sum = counts.map(_._2).reduce(_+_).toFloat
  
      val writer = new PrintWriter(new File("CrimeByLocation2.txt"))
      writer.println(sum.toString())
      val res = counts.map(x => (x._1, x._2/sum))collect()
      for (n <- res){ writer.println(n.toString())}
      
      writer.close()
  }
}