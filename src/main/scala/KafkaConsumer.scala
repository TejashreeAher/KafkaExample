import kafka.consumer.ConsumerConfig
import java.util.Properties
import kafka.consumer.Consumer

/**
  * Created by tejashree.aher on 12/12/2016.
  */
object KafkaConsumer {

  class KafkaConsumerObject {
    var config = createConsumerConfig("localhost:2181", "1")
    var consumer = Consumer.create(config)


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
      val topicCountMap = Map("test" -> numThreads)
      val consumerMap = consumer.createMessageStreams(topicCountMap);
      val streams = consumerMap.get("test").get;
      val it = streams.iterator
      while (it.hasNext) {
        val msg = new String(it.next().toString());
        System.out.println("RECEIVED at:  " + System.currentTimeMillis() + ", message: " + msg);
      }
      println("KIlling thread")
    }
  }
  def main(args: Array[String]):Unit={
    new KafkaConsumerObject().run(10)
  }
}
