package org.oss.focussnip.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitmqConfig {
    @Autowired
    private RabbitTemplate amqpTemplate;


    @PostConstruct
    public void init(){
        amqpTemplate.setConfirmCallback(new ConfirmCallbackService());            //指定 ConfirmCallback
        amqpTemplate.setMandatory(true);
        /**
         * 消息投递到队列失败回调处理
         */
        amqpTemplate.setReturnsCallback(new ReturnCallbackService());
    }

    @Bean("directExchange") // 定义的name会在绑定时用到
    public DirectExchange getDirectExchange() {
        return new DirectExchange("DIRECT_EXCHANGE");
    }

    @Bean("snapQueue")
    public Queue getFirstQueue(){
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-message-ttl",6000); // 设置超时
        Queue queue = new Queue("FIRST_QUEUE", false, false, true, args);
        return queue;
    }

    @Bean
    public Binding bindFirst(@Qualifier("snapQueue") Queue queue, // 队列
                             @Qualifier("directExchange") DirectExchange exchange){ // 交换机
        return BindingBuilder.bind(queue).to(exchange).with("snapOrder"); // 路由键
    }

}
