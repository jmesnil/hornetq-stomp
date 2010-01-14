package org.hornetq.stomp;

import javax.jms.ConnectionFactory;

import org.codehaus.stomp.jms.StompConnect;
import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.core.config.Configuration;
import org.hornetq.core.config.impl.ConfigurationImpl;
import org.hornetq.core.remoting.impl.invm.InVMAcceptorFactory;
import org.hornetq.core.remoting.impl.invm.InVMConnectorFactory;
import org.hornetq.core.server.HornetQServer;
import org.hornetq.core.server.HornetQServers;
import org.hornetq.core.server.cluster.QueueConfiguration;
import org.hornetq.jms.server.JMSServerManager;
import org.hornetq.jms.server.impl.JMSServerManagerImpl;

/**
 * Create HornetQ Server with a Stomp Connector.
 */
public class HornetQStompServer {

	public static void main(String[] args) throws Exception {

		// to keep things simple, we disable persistence and security
		Configuration configuration = new ConfigurationImpl();
		configuration.setSecurityEnabled(false);
		// we add a In-VM acceptor to HornetQ as the server will be accessible
		// outside using Stomp
		configuration.getAcceptorConfigurations().add(
				new TransportConfiguration(InVMAcceptorFactory.class.getName()));
		// we add a Queue which will be available to Stomp under /queue/a
		configuration.getQueueConfigurations().add(
				new QueueConfiguration("jms.queue.a", "jms.queue.a",null, true));
		
		// we create the HornetQ server using this config
		HornetQServer hornetqServer = HornetQServers
				.newHornetQServer(configuration);
		// we also create a JMS server manager as Stomp is using the JMS API
		JMSServerManager jmsServer = new JMSServerManagerImpl(hornetqServer);
		// starting the JMS server will also start the underneath HornetQ server
		jmsServer.start();

		// We create directly a JMS ConnectionFactory which will be 
		// connected to the HornetQ server using In-VM connection
		ConnectionFactory connectionFactory = HornetQJMSClient
				.createConnectionFactory(new TransportConfiguration(
						InVMConnectorFactory.class.getName()));

		// We inject the connection factory in Stomp
		StompConnect stompConnect = new StompConnect(connectionFactory);
		// and start it using default Stomp config
		stompConnect.start();
	}

}
