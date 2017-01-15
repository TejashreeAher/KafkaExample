import java.util.Properties

import com.google.common.collect.ImmutableMap
import jdk.nashorn.internal.ir.annotations.Immutable
import kafka.consumer.{Consumer, ConsumerConfig, KafkaStream}

/**
  * Created by tejashree.aher on 13/12/2016.
  */
object SimpleKafkaConsumerConfig {
  //below values can also be read from a config file
  val zookeeper = "localhost:2181"
  val groupId = "2" //some groupId
  val topic = "test"  //name of the topic

  def createConsumerConfig(zookeeper: String, groupId: String): ConsumerConfig = {
    val props = new Properties()
    props.put("zookeeper.connect", zookeeper);
    props.put("group.id", groupId);
    props.put("auto.offset.reset", "largest");
    props.put("zookeeper.session.timeout.ms", "400");
    props.put("zookeeper.sync.time.ms", "200");
    props.put("auto.commit.interval.ms", "1000");
    val config = new ConsumerConfig(props)
    config
  }

  val config = createConsumerConfig(zookeeper, groupId)
  val consumer = Consumer.create(config)


  val consumerMap = consumer.createMessageStreams(Map(topic -> 1));
  val streams = consumerMap.get(topic).get(0); //get one stream, it ideally is one

  def startConsuming(streams : KafkaStream[Array[Byte], Array[Byte]]): Unit ={
    val iter = streams.iterator();
    while(iter.hasNext()){
      val msg = new String(iter.next().message())
      println("Received !!!!!!!!!! : "+ msg)
    }
  }
  def main(args: Array[String]): Unit ={
//    val thread = new ConsumerThread(streams)
//    streamsthread.start() //starting in a different thread is one way of doing which can be easily replaced by a multithreaded scenario
    startConsuming(streams)
  }

  class ConsumerThread(val stream : KafkaStream[Array[Byte], Array[Byte]]) extends  Thread{
    override def run(): Unit ={
      val iter = streams.iterator();
      while(iter.hasNext()){
        val msg = new String(iter.next().message())
        println("Received !!!!!!!!!! : "+ msg)
      }
      println("shutting down thread")
    }
  }
}
