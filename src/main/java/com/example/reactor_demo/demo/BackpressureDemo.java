package com.example.reactor_demo.demo;

import reactor.core.publisher.BufferOverflowStrategy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class BackpressureDemo {

    private Flux<Long> craeteNoOverflowFlux(){
        return Flux.range(1, Integer.MAX_VALUE)
                .log()
                .concatMap(x -> Mono.delay(Duration.ofMillis(100)));
    }


    /**
     * Here we are emitting each element every 1ms, but it takes 100 ms to process it,
     * so the publisher goes into overflow state.
     * this will give OverflowException.
     */
    private Flux<Long> createOverflowFlux(){
        return Flux.interval(Duration.ofMillis(1))
                .log()
                .concatMap(x -> Mono.delay(Duration.ofMillis(100)));
    }

    /**
     * onBackpressureDrop() -
     * When consumer goes to overflow state, we drop the records
     * disadvantage - may cause loss of some critical data
     */
    private Flux<Long> createDropOnBackpressure(){
        return Flux.interval(Duration.ofMillis(1))
                .onBackpressureDrop()
                .concatMap(x -> Mono.delay(Duration.ofMillis(100)).thenReturn(x))
                .doOnNext(a-> System.out.println("Elements retained after drop : " + a));
    }

    /**
     * onBackpressureBuffer() -
     * When consumer goes to overflow state, the overflown elements goes to the buffer (till its max size)
     * disadvantage - buffer will eventually fill and will again cause the same error
     *
     * there are some strategies we can use when buffer is full like drop oldest, drop latest or Error, it will
     *  drop oldest/latest element from the buffer
     */
    private Flux<Long> createBufferOnBackpressure(){
        return Flux.interval(Duration.ofMillis(1))
//                .onBackpressureBuffer(50)
                .onBackpressureBuffer(50, BufferOverflowStrategy.DROP_OLDEST)
                .concatMap(x -> Mono.delay(Duration.ofMillis(100)).thenReturn(x))
                .doOnNext(a-> System.out.println("Elements retained after drop : " + a));
    }


    public static void main(String[] args) {
        BackpressureDemo demo = new BackpressureDemo();

//        demo.craeteNoOverflowFlux().blockLast();
//        demo.createOverflowFlux().blockLast();

//        demo.createDropOnBackpressure().blockLast();
        demo.createBufferOnBackpressure().blockLast();
    }
}
