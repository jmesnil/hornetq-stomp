# Using StompConnect with HornetQ #

This example shows how to setup a [HornetQ][hornetq] server with [Stomp][stomp] (in less than 30 lines of
code) so that Stomp clients can connect to it.

# Run The Example#

## Prerequisites ##

For simplicity, all the jars are included with the examples in the `lib/` directory.
You can use Apache Ant to run the server.

## Start the server ##

The server is named [HornetQStompServer](src/org/hornetq/stomp/HornetQStompServer.java).

     $ ant server
     ...

     server:
          [java] 14 janv. 2010 10:57:30 org.hornetq.core.logging.impl.JULLogDelegate info
          [java] INFO: live server is starting..
          [java] 14 janv. 2010 10:57:30 org.hornetq.core.logging.impl.JULLogDelegate warn
          [java] ATTENTION: Security risk! It has been detected that the cluster admin user and password have not been changed from the installation default. Please see the HornetQ user guide, cluster chapter, for instructions on how to do this.
          [java] 14 janv. 2010 10:57:30 org.hornetq.core.logging.impl.JULLogDelegate info
          [java] INFO: HornetQ Server version 2.0.0.GA (Hornet Queen, 113) started
          [java] 14 janv. 2010 10:57:30 org.codehaus.stomp.tcp.TcpTransportServer doStart
          [java] INFO: Listening for connections at: tcp://BlackBook.local:61613


## Stomp Client ##

We'll use telnet as our Stomp client:

      $ telnet localhost 61613
      
First, we connect to the server.
To keep things simple, we've disable security from the server so that
we can connect to it anonymously:

     CONNECT
     login:
     passcode:
     
     ^@
     
(^@ is Ctl-@)
The server replies that we are connected:

     CONNECTED
     session:null
     
We send a message to the destination `/queue/a`:
     
     SEND 
     destination:/queue/a
     
     hello, hornetq!
     ^@
     
And we subscribe to this destination:
     
     SUBSCRIBE
     destination: /queue/a
     ack:client
     
     ^@
     
As soon as we are subscribed, we will receive the message that was sent to the destination:

     MESSAGE
     message-id:ID:7b28be24-00f1-11df-b27f-001c42000009:0000000000000000
     destination:/queue/a
     timestamp:1263462299779
     JMSXDeliveryCount:1
     expires:0
     subscription:/subscription-to//queue/a
     priority:4
     
     hello, hornetq!

Finally, we acknowledge the message:

     ACK    
     message-id: ID:7b28be24-00f1-11df-b27f-001c42000009:0000000000000000
     
[hornetq]:http://jboss.org/hornetq/
[stomp]:  http://stomp.codehaus.org/
     
     