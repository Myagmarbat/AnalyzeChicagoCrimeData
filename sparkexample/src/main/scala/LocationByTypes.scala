import java.io._
import scala.io.Source
import scala.math.random
import org.apache.spark._

//(STREET,0.2709482)
//(RESIDENCE,0.16153274)
//(SIDEWALK,0.12116238)
//(APARTMENT,0.099130526)

//The location and its' associative each crime types percentage
object LocationByTypes extends App {
    override def main(args: Array[String]) {
      
    val conf = new SparkConf().setAppName("Spark Sort").setMaster("local")
    val sc = new SparkContext(conf)

    
    var locs = Array("STREET","RESIDENCE","SIDEWALK","APARTMENT")
    val writer = new PrintWriter(new File("LocationByTypes.txt"))
    for(loc <- locs){
       byTypes(sc, loc, writer)
    }
    writer.close()

  }
    
  def byTypes(sc: SparkContext, item:String, writer: PrintWriter){
    var tf = sc.textFile("file:////home/cloudera/workspace/sparkexample/crimes.csv")
    var counts = tf.map(line => {
                  val lines = line.split(',')
                  (lines(7), lines(5))
                }).map(word => (word._1.trim(), word._2.trim())).filter(f => f._1.equalsIgnoreCase(item))
                
    
    var types = counts.map(x => (x._2, 1)).reduceByKey(_ + _).sortBy(_._2,false)
    var sum = types.map(_._2).reduce(_+_).toFloat
   
    writer.println("--------------------------------------")
    writer.println(item)
    val res = types.map(x => (x._1, x._2/sum))collect()
    for (n <- res){ writer.println(n.toString())}
    
  }
  
}