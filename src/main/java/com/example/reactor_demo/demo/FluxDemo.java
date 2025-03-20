package com.example.reactor_demo.demo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.List;

public class FluxDemo {

    /**
     * Mono : publishes one or zero value.
     *
     *  using Flux.just(), we can return the specific item, and it is captured at instantiation time.
     *  Subscriber will not process data until we subscribe to the publisher.
     *
     *  Note* Don't return Null when handling Mono, instead use Mono.empty()
     */

    private List<String> list = List.of("Hello", "Namaste", "Ciao", "Konnichiwa");

    public static void main(String[] args) throws InterruptedException {
        FluxDemo fluxDemo = new FluxDemo();

//        fluxDemo.greetings().subscribe(System.out::println);

//        fluxDemo.fluxMap().subscribe(System.out::println);

//        fluxDemo.fluxFlatMap().subscribe(System.out::println);

//        fluxDemo.testComplexSkip().subscribe(System.out::println);

//        fluxDemo.testConcat().subscribe(System.out::println);

//        fluxDemo.testMerge().subscribe(System.out::println);

//        fluxDemo.testZip().subscribe(System.out::println);

//        Thread.sleep(10000);
    }


    Flux<String> greetings(){
        //return Flux.just("Hello", "Namaste", "Ciao", "Konnichiwa");
        return Flux.fromIterable(list);
    }

    /**
     *  map will perform operations on each of the flux element and return normal value
     *  1 - 1 mapping
     */
    Flux<String> fluxMap(){
        Flux<String> greetings = Flux.fromIterable(list);
        return greetings.map(String::toUpperCase);
    }


    /**
     * flat map will perform operations and will return a publisher (Ex : if each iteration is a call to db)
     *  1 - n mapping
     */
    Flux<String> fluxFlatMap(){
        Flux<String> greetings = Flux.fromIterable(list);
        return greetings.flatMap(e -> Mono.just(e.toUpperCase()));
    }

    /**
     *  .skip() -> skips the elements w.r.t no. of elements, duration etc
     */
    private Flux<String> testSkip(){
        return Flux.fromIterable(list)
                .delayElements(Duration.ofSeconds(1))  // wait the processing for 1 sec after each element
                .skip(1)                       // this will skip 6 elements
                .skip(Duration.ofSeconds(2))           // this will skip elements for provided time (can be used with delayElements)
               ;

    }

    private Flux<Integer> testComplexSkip(){
        return Flux.range(1, 20)
                //.skipWhile(e -> e<10)     // this will skip the element till the condition remain true
                .skipUntil(r -> r == 12); // skips the flux until the condition returns true.
    }

    /**
     *  Concat : concat multiple flux publishers, supports varargs, so we can add as many as we want
     *          - publishers are subscribed sequentially as per the order provided in args
     *          - publisher2 will wait for publisher1 to complete
     */
    private Flux<Integer> testConcat(){
        Flux<Integer> flux1 = Flux.range(1, 10);
        Flux<Integer> flux2 = Flux.range(11, 20);
        Flux<Integer> flux3 = Flux.range(31, 30);

        return Flux.concat(flux3, flux1, flux2);
    }

    /**
     *  Merge -
     *  In case of merge, unlike concat the publishers are subscribed simultaneously. they won't wait for one publisher
     *  to finish before proceeding.
     *
     */
    private Flux<Integer> testMerge(){
        Flux<Integer> flux1 = Flux.range(1, 10).delayElements(Duration.ofMillis(500));
        Flux<Integer> flux2 = Flux.range(11, 10).delayElements(Duration.ofMillis(500));

        return Flux.merge(flux1, flux2);
    }


    /**
     *  Zip -
     *  It will provide 1-1 element from each publisher as a tuple. We can zip upto 8 publishers.
     *  - If any one of the publisher is exhausted then other publisher will not produce any result.
     *  - Useful when we want to something with the result of 2 publishers.(like 2 REST calls)
     *  Sample result -
     *  [1,11]
     *  [2,12]
     *  [3,13]
     *      ....
     */
    private Flux<Tuple2<Integer, Integer>> testZip(){
        Flux<Integer> flux1 = Flux.range(1, 10).delayElements(Duration.ofMillis(500));
        Flux<Integer> flux2 = Flux.range(11, 10).delayElements(Duration.ofMillis(500));

        return Flux.zip(flux1, flux2);
    }
}
