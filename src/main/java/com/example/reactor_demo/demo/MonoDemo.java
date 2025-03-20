package com.example.reactor_demo.demo;

import reactor.core.publisher.Mono;

public class MonoDemo {

    /**
     * Mono : publishes one or zero value.
     *
     *  using Mono.just(), we can return the specific item, and it is captured at instantiation time.
     *  Subscriber will not process data until we subscribe to the publisher.
     *
     *  Note* Don't return Null when handling Mono, instead use Mono.empty()
     */


    public static void main(String[] args) {
        MonoDemo monoDemo = new MonoDemo();

        // This is a subscriber,
        // without subscribe() nothing will happen and program will end.
        monoDemo.greetings().subscribe(System.out::println);
    }


    // This is a publisher
    Mono<String> greetings(){
        return Mono.just("Greetings");
    }


}
