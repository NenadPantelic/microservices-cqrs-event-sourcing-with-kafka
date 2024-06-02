# CQRS

- Command Query Responsibility Segregation
  - Command alters the data
  - Query returns that data
- Why do we need it?

  - Data is often more frequently queries than altered, or vise vera
  - segregating commands and queries enables us to optimize each for high performance
  - Read and write representation of data often differ substantially
  - Executing command and query operations on the same model can cause data contention
  - segregating read and write concerns enables you to manage read and write security separately

- Microservices:
  - should fail independently
  - should not communicate directly
  - loose coupling, high cohesion

Benefits of Event Sourcing:

- the event store provides a complete log of every state change
- the state of an object/aggregate can be recreated by replaying the event store
- improves write performance. All event types are simply appended to the event store. There are no update or delete
  operations
- in the case of failure, the event store can be used to restore read database

```
docker network create --attachable -d bridge techbank-net
```

- Kafka brokers are stateless, that's why ZK is needed to manage Kafka cluster

- MongoDB:

```
docker run -it -d --name mongodb -p 27017:27017 --network techbank-net --restart always -v mongodb_data:/data/db mongo:latest
```

- MySQL

```
docker run -it -d --name mysql -p 3306:3306 --network techbank-net \
-e MYSQL_ROOT_PASSWORD=techbank123 --restart always -v mysql_data:/var/lib/mysql mysql:latest
```

Adminer:

```
docker run -it -d --name adminer -p 8080:8080 --network techbank-net \
-e ADMINER_DEFAULT_SERVER=mysql --restart always adminer:latest
```

# Messages
In CQRS, there are 3 types of messages that can be used:
  - Commands: a combination of expressed intent (describes something that we want to be done). 
It also contains the information required to undertake action based on that intent
  They are named with a verb in the imperative mood.
  - Events: objects that describe something that has occurred in the application. A typical source
of events is the aggregate. When something important has occurred within the aggregate, it will raise an event.
They are named with a past particle construction: FundsDepositedEvent, AccountOpenedEvent...
  - Queries 

## Mediator pattern
- behavioral design pattern that promotes loose coupling by preventing objects from 
referring to each other explicitly
- simplifies communication between objects by introducing a single object known as the mediator that manages
the distribution of messages among other objects
- messages can be commands