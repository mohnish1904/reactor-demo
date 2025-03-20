# Spring Reactor 

## Intro

### Synchronous Programming
It is a blocking type of programming where tasks are executed sequentially. Each task waits for the previous one 
to complete before starting.

#### Benefits:
- Simplicity: Easier to write, understand, and debug since tasks are executed in a predictable, sequential order.
Deterministic 
- Behavior: Easier to predict the flow of execution, making it simpler to manage state and handle errors.

#### Disadvantages:
- Blocking: Tasks wait for each other to complete, which can lead to inefficiencies, especially for I/O operations or long-running tasks.
- Scalability: Not suitable for applications requiring high concurrency or responsiveness, as it can lead to performance bottlenecks.


### Asynchronous Programming
It is a non-blocking type of programming where tasks can be executed concurrently. 
Tasks do not wait for others to complete and can run in parallel.

#### Benefits:
- Non-blocking: Tasks can run concurrently, improving the efficiency and responsiveness of applications, especially for I/O-bound or network-bound tasks.
- Scalability: Better suited for applications that require handling many tasks simultaneously, such as web servers or real-time systems.

#### Disadvantages:
- Complexity: More challenging to write, understand, and debug due to the concurrent nature of task execution. Managing state and handling errors can be more complex.
- Race Conditions: Increased risk of race conditions and other concurrency-related issues, requiring careful synchronization and handling.

## Why Reactive ??

- Asynchronous and Non blocking
- Better for streaming data
- Utilizes resources efficiently
- Back pressure

## Reactive Workflow

There are 2 key players in the workflow -
- **Publisher** : Publisher is basically the data source, which publishes the data, It could be Mono or Flux.
- **Subscriber** : Subscriber, subscribe to the publisher and acts as a listener to the published data and 
do processing over it

#### Workflow : 

1. Subscriber sends _subscribe()_ request to the Publisher.
    > [**Publisher**] <---- *subscribe()* ---- [**Subscriber**]
   
2. Publisher then sends _subscription()_ to the Subscriber, at this point, the subscriber is subscribed and ready 
   to receive the data from Publisher.\
   > [**Publisher**] ---- *subscription()* ----> [**Subscriber**]

3. Subscriber then request N data from the Publisher.
   > [**Publisher**] <---- *requestData(N)* ---- [**Subscriber**]
   
4. Publisher sends data 1 by 1 to Subscriber till N data is reached with OnNext().
   > [**Publisher**] ---- *onNext(1)* ----> [**Subscriber**]
    
   > [**Publisher**] ---- *onNext(2)* ----> [**Subscriber**]

   > [**Publisher**] ---- *onNext(N)* ----> [**Subscriber**]

5. After that Publisher sends off a onComplete(), denoting the end of data.
   > [**Publisher**] ---- *onComplete()* ----> [**Subscriber**]


## Publishers :

### Mono :
Mono<T>, is a Publisher<T> that produces zero or one value.