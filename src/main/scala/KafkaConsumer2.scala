/**
  * Created by tejashree.aher on 12/12/2016.
  */


import java.util.Properties
import java.util.concurrent._

import com.datastax.driver.core.Cluster
import kafka.utils.Logging
import kafka.consumer.{Consumer, ConsumerConfig, KafkaStream}

class ScalaConsumerEx(val zookeeper: String,val groupId: String, val topic: String, val delay: Long) extends Logging {
  // /localhost:2181 group1 test_topic 10 0

  var config = createConsumerConfig("localhost:2181", "1")
  var consumer = Consumer.create(config)

  var executor: ExecutorService = null

  def shutdown() = {
    if (consumer != null)
      consumer.shutdown();
    if (executor != null)
      executor.shutdown();
  }

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

  def run(numThreads: Int) = {
    val topicCountMap = Map(topic -> numThreads)
    val consumerMap = consumer.createMessageStreams(topicCountMap);
    val streams = consumerMap.get(topic).get;
    println("STREAMS : "+ streams)
    println("SIZE : "+ streams.size)
    executor = Executors.newFixedThreadPool(numThreads);
    var threadNumber = 0;
    for (stream <- streams) {
      executor.submit(new ScalaConsumerTest(stream, threadNumber, delay))
      threadNumber += 1
    }
  }
}

object ScalaConsumer2 extends App {
  val example = new ScalaConsumerEx("localhost:2181", "group1", "test",0)
  example.run(10)
}

class ScalaConsumerTest(val stream: KafkaStream[Array[Byte], Array[Byte]], val threadNumber: Int, val delay: Long) extends Logging with Runnable {
  def run {
    val it = stream.iterator()
    //it.hasNext() waits until it receives the data, hence thread doesn't stop
    while (it.hasNext()) {
      val msg = new String(it.next().message())
      System.out.println("RECEIVED: "+ System.currentTimeMillis() + ",Thread " + threadNumber + ": " + msg)
      val inputArr = msg.split(":")
      val name = inputArr(0)
      val urn = inputArr(1)
      def insertIntoDB(name: String, urn: String): Unit ={
        println("Trying to insert : "+ name + " and "+ urn)
        val cluster = Cluster.builder()
          .addContactPoint("127.0.0.1")
          .build();
        val session = cluster.connect(); // (2)
        val rs = session.execute("insert into tracking_aggregation_test.actor_urns(name, actor_urn) values('"+name+"', '"+urn+"')");
      }
      //insertIntoDB(name, urn)
    }
    println("Shutting down Thread: " + threadNumber);
  }
}

