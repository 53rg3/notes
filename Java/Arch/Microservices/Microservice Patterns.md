# Microservice Patterns



## Basics

- **What is messaging?**
  When you call someone via phone the other person must be available at that time to receive the call. I.e. you as the publisher establish a connection to the consumer and then communicate synchronously. With messaging it's like voice mail. You as the publisher create a voice mail (message) which is stored on some medium (message queue) and the consumer can retrieve it whenever he wants. To respond the receiver can now also publish a message to the message queue and you can pick it up whenever you want. I.e. it's asynchronous.  

- **What is a messaging system?**
  - Like a database for messages. It stores messages from publishers and makes them available to consumers. I.e. it knows which consumer is subscribed to which queue and when a publisher stores a message in the system it notifies consumers about this. So messages are like rows in a table with and there columns for states, e.g. if the retrieval of the message was acknowledged by a consumer so that it can be deleted.
  - Like you configure a database system via schemata you configure a messaging system with queues and how they shall behave, e.g. how many messages a consumer should prefetch to save round trips, how retries should be handled in case a consumer doesn't acknowledge messages, message priorities or want kind of queue to use (e.g. topic). 
- **RPC vs Messaging**
  - Throttling
    In a message system the consumers can determine how many messages they receive and thereby throttle their consumption. With RPC you have to implement this yourself on the publisher. 
  - Load balancing
    The message queue is basically a load balancer and you can just add consumers to increase through-put.
  - Store-and-forward
    A messaging system comes with functionality to make network communication reliable. A sender (publisher or message queue) can store failed messages and retry to send them till a receiver (consumer or message queue) becomes available again. With RPC you have to implement this yourself.