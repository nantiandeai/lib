package com.lianyitech.modules.catalog.utils;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.lianyitech.core.utils.CommonUtils.isImportServer;

/**
 * 生成者（导入Mq）
 */
public class ImportProductMq {
    //private static Logger logger = LoggerFactory.getLogger(ImportProductMq.class);
    public  Channel channel;
    public  Channel getChannel(String queue,ConnectionFactory factory) throws IOException, TimeoutException {
        //创建一个新的连接
        Connection connection = factory.newConnection();
        //创建一个频道
        Channel channelNew= connection.createChannel();
        channelNew.exchangeDeclare(queue+"change", BuiltinExchangeType.DIRECT);
        /**声明一个队列 -- 在RabbitMQ中，队列声明是幂等性的
         （一个幂等操作的特点是其任意多次执行所产生的影响均与一次执行的影响相同），
         也就是说，如果不存在，就创建，如果存在，不会对已经存在的队列产生任何影响。
         autoDelete:为true不存在消费者的情况下自动删除*/
        channelNew.basicQos(0,1, true);
        channelNew.queueDeclare(queue, true, false, false, null);
        channelNew.queueBind(queue, queue+"change", queue);
        return channelNew;
    }
    public ImportProductMq(){}
    public ImportProductMq(String queue, ConnectionFactory factory, ImportConsumeMq.Handler handler, byte[] data)  throws Exception{
        // 创建连接工厂
        channel = this.getChannel(queue,factory);
        channel.basicPublish(queue+"change", queue, null, data);
//      关闭频道和连接
        channel.close();
        channel.getConnection().close();
        if(isImportServer()){
            ImportConsumeMq.consume(queue, factory, handler);
        }
    }
}
