package org.oss.focussnip.rabbitmq;


import lombok.extern.slf4j.Slf4j;
import org.oss.focussnip.exception.BusinessErrorException;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConfirmCallbackService implements RabbitTemplate.ConfirmCallback {
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (!ack) {
            log.error("消息发送异常! cause={}", cause);
            throw new BusinessErrorException("111111","无法进入消息队列");
        } else {
            log.info("发送者已经收到确认");
        }
    }
}
