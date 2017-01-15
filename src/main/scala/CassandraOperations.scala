/**
  * Created by tejashree.aher on 13/12/2016.
  */
import com.datastax.driver.core.{Cluster, Session}

object CassandraOperations{
  val cluster = Cluster.builder()
    .addContactPoint("127.0.0.1")
    .build();
  val session = cluster.connect();

  def main(args: Array[String]):Unit = {
    insertIntoDB("val3", "urn3")
    getFromCassandra()
  }

  def insertIntoDB(name: String, urn: String): Unit ={
    val rs = session.execute("insert into tracking_aggregation_test.actor_urns(name, actor_urn) values('"+name+"', '"+urn+"')");

  }


  def getFromCassandra(): Unit ={
      val rs = session.execute("select name from tracking_aggregation_test.actor_urns");
      val row = rs.one();
      println(row.getString("name")); // (4)
  }
}
