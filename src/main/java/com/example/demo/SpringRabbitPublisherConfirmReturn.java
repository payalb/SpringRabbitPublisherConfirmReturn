package com.example.demo;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class SpringRabbitPublisherConfirmReturn {


	public static void main(String[] args) {
		ConfigurableApplicationContext ctx=SpringApplication.run(SpringRabbitPublisherConfirmReturn.class, args);
		RabbitTemplate template=ctx.getBean(RabbitTemplate.class);
			
			template.setConfirmCallback(new ConfirmCallback() {

				@Override
				public void confirm(CorrelationData correlationData, boolean ack, String cause) {
					System.out.println("Message received by broker with correlation id:"+correlationData );
					System.out.println("ack:"+ ack);
					System.out.println("cause:"+ cause);
				}
				
				
			});
			//If message cannot be sent to any queue, it is returned if manadatory set to true. Else would be dropped.
			template.setReturnCallback(new ReturnCallback() {

				@Override
				public void returnedMessage(Message message, int replyCode, String replyText, String exchange,
						String routingKey) {
					System.out.println("Message returned by broker "+message+", replyText:"+ replyText );
					System.out.println("replyCode:"+ replyCode);
					
				}
				
			});
			template.send("amq.topic","foo",new Message("hello world".getBytes(),new MessageProperties()));
	}
		
}
