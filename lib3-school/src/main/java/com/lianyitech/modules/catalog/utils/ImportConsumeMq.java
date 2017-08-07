package com.lianyitech.modules.catalog.utils;

import com.lianyitech.core.enmu.Action;
import com.lianyitech.modules.catalog.service.MqRetryException;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

/**
 * 消费者（导入Mq）
 */
public class ImportConsumeMq {

    private static Logger logger = LoggerFactory.getLogger(ImportConsumeMq.class);
    public static void consume(String queue, ConnectionFactory factory, Handler handler) throws Exception {
        ImportProductMq p = new ImportProductMq();
        Channel channel = p.getChannel(queue, factory);
        if (channel.consumerCount(queue) == 0) {
            //DefaultConsumer类实现了Consumer接口，通过传入一个频道，告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    Action action = Action.ACCEPT;
                    try {
                        handler.handle(channel, consumerTag, envelope, properties, body);
                        action = Action.ACCEPT;
                    } catch (MqRetryException e1){
                        logger.debug("Retry Message queue:"+ queue);
                        action = Action.RETRY;
                    } catch (Exception e) {
                        action = Action.REJECT;
                    } finally {
                        if (action == Action.ACCEPT) {
                            channel.basicAck(envelope.getDeliveryTag(), false);
                        }else if(Action.RETRY == action) {
                            channel.basicNack(envelope.getDeliveryTag(), false, true);
                        }
                        else {
                            channel.basicNack(envelope.getDeliveryTag(), false, false);
                        }
                    }
                }
            };

            channel.basicConsume(queue, false, consumer);
        }else {
            channel.close();
            channel.getConnection().close();
        }
    }
    public interface Handler {
        void handle(Channel channel, String consumerTag,
                    Envelope envelope,
                    AMQP.BasicProperties properties,
                    byte[] body) throws Exception, MqRetryException;
    }
}
