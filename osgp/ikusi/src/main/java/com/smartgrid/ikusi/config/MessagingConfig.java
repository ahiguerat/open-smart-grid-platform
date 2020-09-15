package com.smartgrid.ikusi.config;

import javax.jms.Destination;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class MessagingConfig {

	@Bean
	public ActiveMQConnectionFactory senderConnectionFactory() {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL("failover:(tcp://localhost:61616)");
		activeMQConnectionFactory.setTrustAllPackages(true);

		return activeMQConnectionFactory;
	}

	@Bean
	public CachingConnectionFactory cachingConnectionFactory() {
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(senderConnectionFactory());
		cachingConnectionFactory.setSessionCacheSize(10);

		return cachingConnectionFactory;
	}

	@Bean
	public Destination orderDestination() {
		return new ActiveMQQueue("protocol-orm.1_0.osgp-core.1_0.requests");
	}

	@Bean
	public JmsTemplate orderJmsTemplate() {
		JmsTemplate jmsTemplate = new JmsTemplate(cachingConnectionFactory());
		jmsTemplate.setDefaultDestination(orderDestination());
		jmsTemplate.setReceiveTimeout(5000);

		return jmsTemplate;
	}
}
