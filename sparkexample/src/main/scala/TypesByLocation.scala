import java.io._
import scala.io.Source
import scala.math.random
import org.apache.spark._

//(STREET,0.2709482)
//(RESIDENCE,0.16153274)
//(SIDEWALK,0.12116238)
//(APARTMENT,0.099130526)

//(BATTERY,0.19910744)
//(THEFT,0.1952602)
//(CRIMINAL DAMAGE,0.12052117)
//(NARCOTICS,0.11480161)
//(ASSAULT,0.061966196)
//(OTHER OFFENSE,0.057708584)
//(BURGLARY,0.05575932)
//(MOTOR VEHICLE THEFT,0.0420375)
//(ROBBERY,0.035009876)

//The crime type and its' associative each crimes location percentage 
object TypesByLocation extends App {
  
    override def main(args: Array[String]) {
      
    val conf = new SparkConf().setAppName("Spark Sort").setMaster("local")
    val sc = new SparkContext(conf)
    
    var types = Array("BATTERY","THEFT","CRIMINAL DAMAGE","NARCOTICS","ASSAULT", "BURGLARY", "MOTOR VEHICLE THEFT", "ROBBERY")
    val writer = new PrintWriter(new File("TypesByLocation.txt"))
    for(crimetype <- types){
       byTypes(sc, crimetype, writer)
    }
    writer.close()
  }

  def byTypes(sc: SparkContext, crimeType: String, writer: PrintWriter) {
    var tf = sc.textFile("file:////home/cloudera/workspace/sparkexample/crimes.csv")
    var counts = tf.map(line => {
      val lines = line.split(',')
      (lines(5), lines(7))
    }).map(word => (word._1.trim(), word._2.trim())).filter(f => f._1.equalsIgnoreCase(crimeType))

    var types = counts.map(x => (x._2, 1)).reduceByKey(_ + _).sortBy(_._2, false)
    var sum = types.map(_._2).reduce(_ + _).toFloat

    writer.println("--------------------------------------")
    writer.println(crimeType)
    val res = types.map(x => (x._1, x._2 / sum)) collect ()
    for (n <- res) { writer.println(n.toString()) }

  }
  
}