package com.example.reactor_demo.demo;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;
import reactor.core.publisher.SignalType;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;

import java.time.Duration;
import java.util.List;
import java.util.Map;

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

//         fluxDemo.testBuffer().subscribe(System.out::println);

//        fluxDemo.testCollectMap().subscribe(System.out::println);

//        fluxDemo.testDoOnEach().subscribe();

//        Disposable disposable = fluxDemo.testDoOnCancel().subscribe(); // gives an instance of disposable
//        Thread.sleep(3000); // sleeps for 3 secs
//        disposable.dispose(); // this will cancel the subscription


//        fluxDemo.testOnErrorContinue().subscribe(System.out::println);

        fluxDemo.testOnErrorReturn().subscribe(System.out::println);

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


    /*
     *  Zip -
     *  It will provide 1-1 element from each publisher as a tuple. We can zip upto 8 publishers.
     *  - If any one of the publisher is exhausted then the other publisher will not produce any result.
     *  - Useful when we want to do something with the result of 2 publishers.(like 2 REST calls)
     *  Sample result -
     *  [1,11]
     *  [2,12]
     *  [3,13]
     */
    private Flux<Tuple2<Integer, Integer>> testZip(){
        Flux<Integer> flux1 = Flux.range(1, 10).delayElements(Duration.ofMillis(500));
        Flux<Integer> flux2 = Flux.range(11, 10).delayElements(Duration.ofMillis(500));

        return Flux.zip(flux1, flux2);
    }

    /**
    * We can even have more than 2 publisher zip together, the tuple value will increase
    * We can zip up to 8 publisher - will give Tuple8
    *
    * We can also zip a Mono and a flux , but it will return only 1 element
    * */
    private Flux<Tuple3<Integer, Integer, Integer>> testZip3Publishers(){
        Flux<Integer> flux1 = Flux.range(1, 20).delayElements(Duration.ofMillis(500));
        Flux<Integer> flux2 = Flux.range(1, 20).delayElements(Duration.ofMillis(500));
        Flux<Integer> flux3 = Flux.range(1, 20).delayElements(Duration.ofMillis(500));

        return Flux.zip(flux1, flux2, flux3);
    }


    /**
    *   CollectList -
    *   It will convert the publisher into a mono of list, as it will collect the elements into one list.
     */
    private Mono<List<Integer>> getCollectList(){
           return Flux.range(1, 20).collectList();
    }


    /**
     * Buffer -
     * get some elements and collect it in a list and provide it in flux
     */
    private Flux<List<Integer>> testBuffer(){
        Flux<Integer> flux = Flux.range(0,20).delayElements(Duration.ofMillis(500));

        // return flux.buffer();  // This returns entire element in one list - no buffering
        // return flux.buffer(3); // this returns elements as flux of list with 3 elements in each list
        return flux.buffer(Duration.ofSeconds(3)); // Buffer is created on the basis of time.
    }

    /**
     * collectMap() -
     * collect entire elements and creates a map from it, returns a Mono of Map
     */
    private Mono<Map<Integer, Integer>> testCollectMap(){
        return Flux.range(0,10)
                .collectMap(i -> i, e -> e*2);
    }


    /**
     * doOnEach() -
     * <p>
     * do tasks based on each signal
     */
    private Flux<Integer> testDoOnEach(){
        Flux<Integer> range = Flux.range(0, 10);
//        return range.doOnEach(signal -> System.out.println("Signal : "+signal.get()));

        return range.doOnEach(signal -> {
            if (signal.getType() == SignalType.ON_COMPLETE)
                System.out.println("I am done");
            else
                System.out.println("Signal value is : " + signal.get());
        });
    }

    /**
     * doOnComplete() -
     * do tasks based on onComplete signal
     */
    private Flux<Integer> testDoOnComplete(){
        Flux<Integer> range = Flux.range(0, 10);
        return range
                .doOnEach(signal -> System.out.println("Signal : "+signal.get()))
                .doOnComplete(() -> System.out.println("I am done"));
    }

    /**
     * doOnSubscribe() -
     * do tasks anytime a publisher is subscribed
     */
    private Flux<Integer> testDoOnSubscribe(){
        Flux<Integer> range = Flux.range(0, 10);
        return range
                .doOnSubscribe(subscription -> System.out.println("Start subscription !!!"));
    }

    /**
     * doOnCancel() -
     * do tasks when subscription is cancelled
     */
    private Flux<Integer> testDoOnCancel(){
        Flux<Integer> range = Flux.range(0, 10);
        return range.doOnCancel(() -> System.out.println("Cancelled !!!!"));
    }

    // ##################################### Exception Handling ########################################################

    /**
     * doOnError() -
     * do tasks when error occurs during element is emitted
     * when error occurred and unhandled, remaining flux will be stopped
     */
    private Flux<Integer> testOnErrorContinue(){
        Flux<Integer> range = Flux.range(0, 10);
        return range
                .map(integer -> {
                    if (integer ==3){
                        throw  new RuntimeException("Unexpected error!!!!");
                    }
                    return integer;
                })
                .onErrorContinue(((throwable, o) -> {
                    System.out.println("Error handled for : " + o.toString());
                }));
    }

    /**
     * onErrorReturn() -
     * return a fallback value when error is occurred, and stops upcoming values
     */
    private Flux<Integer> testOnErrorReturn(){
        Flux<Integer> range = Flux.range(0, 10);
        return range
                .map(integer -> {
                    if (integer ==3){
                        throw  new RuntimeException("Unexpected error!!!!");
                    }
                    return integer;
                })
//                .onErrorReturn(RuntimeException.class, -1000) // when this RuntimeException comes, return -1000
                .onErrorReturn(-1);
    }

    /**
     * onErrorResume() -
     * subscribes to a new publisher when error is occurred, and start emitting values from new flux
     */
    private Flux<Integer> testOnErrorResume(){
        Flux<Integer> range = Flux.range(0, 10);
        Flux<Integer> errorHandlerFlux = Flux.range(50, 5);
        return range
                .map(integer -> {
                    if (integer ==3){
                        throw  new RuntimeException("Unexpected error!!!!");
                    }
                    return integer;
                })
//                .onErrorResume(throwable -> Mono.just(1))
                .onErrorResume(throwable -> errorHandlerFlux);
    }

    /**
     * onErrorMap() -
     * when error is occurred, map that error to a new Error
     * Useful to standardising the exception from the publisher
     */
    private Flux<Integer> testOnErrorMap(){
        Flux<Integer> range = Flux.range(0, 10);
        return range
                .map(integer -> {
                    if (integer ==3){
                        throw  new RuntimeException("Unexpected error!!!!");
                    }
                    return integer;
                })
                .onErrorMap(throwable -> new UnsupportedOperationException(throwable.getMessage()));
    }

}





























