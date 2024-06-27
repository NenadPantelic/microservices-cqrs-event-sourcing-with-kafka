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

## Aggregate

- an entity or a group of entities that is always kept in a consistent state
- the aggregate root is the entity within the aggregate that is responsible for maintaining this consistent state
- this makes the aggregate the primary building block for implementing a command model in any CQRS based application

#### Aggregate root

- it maintains the list of uncommitted changes in the form of events, that needs to be applied to the aggregate and be
  persisted to the event store
- the aggregate root is the entity within the aggregate that is responsible for keeping the aggregate in a consistent
  state
- contains a method that can be invoked to commit the changes that have been applied to the aggregate
- manages which apply method is invoked on the aggregate based on the event type


- the command that "creates" an instance of the Aggregate should always be handled in the constructor of the aggregate
- the validation logic of a command should be set before it raises an event

## Event store

- an event store must be append-only store, no update or delete operations should be allowed
- each event that is saved should represent the version or state of an aggregate at any given point in time
- events should be stored in chronological order and new events should be appended to the previous event
- the state of the aggregate should be recreatable by replaying the event store
- implement optimistic concurrency control: it is important that the ordering of the events is
  enforced that only the expected event versions can alter the state of the aggregate at any given point in time
  (especially when two or more client requests are made at the same time to alter the state of the aggregate)

- Commands does not necessarily have any fields as long as it express some intent, e.g. RestoreReadDbCommand

## Domain Driven Design
- DDD was coined by Eric Evans in 2003
- an approach to structure and model software in a way that it matches the business domain
- it places the primary focus of a software project on the core area of the business (the core domain)
- refers to problems as domains and aims to establish a common language to talk about these problems
- describes independent problem areas as `Bounded Contexts`

`Bounded Context`:
- an independent problem area
- describes a logical boundary within which a particular model is defined and applicable
- each bounded context correlates to a microservice (e.g. Bank account microservice)

`EventSourcingHandler` vs `EventHandler`

- `EventSourcingHandler` - to retrieve all events for a given aggregate from the Event store and to invoke the 
replayEvents method on the `AggregateRoot` to recreate the latest state of the aggregate
- `EventHandler` is responsible to update the read database via the `BankAccountRepository` after a new event was 
consumed from Kafka