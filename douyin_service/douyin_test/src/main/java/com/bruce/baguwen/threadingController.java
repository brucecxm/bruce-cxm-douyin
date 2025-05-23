package com.bruce.baguwen;

import com.bruce.utils.MyCallable;
import com.bruce.utils.MyRunnable;
import com.bruce.utils.MyThread;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.lang.Thread.sleep;

@RestController
@RequestMapping("/threading")
public class threadingController {

    @GetMapping
    public  void  testThreading()
    {
        ExecutorService executor= Executors.newFixedThreadPool(5);
        Future<?> stockFuture = executor.submit(() -> {
            try {
                sleep(2000); // 模拟一个耗时操作
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("threading one");
            // 检查任务是否已经完成


        });
        /*
        * submit() 的默认行为是根据传入的参数来决定提交 Runnable 任务还是 Callable 任务：

如果你传入一个 Runnable，它会作为 Runnable 执行。  无返回值
如果你传入一个 Callable，它会作为 Callable 执行。  有返回值
        * */
        Future<?> stockFutureone = executor.submit(() -> {
            try {
                sleep(2000); // 模拟一个耗时操作
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("threading one111");
            // 检查任务是否已经完成


        });
        try {

            stockFuture.get();
            stockFutureone.get();
            if (stockFuture.isDone()) {
                System.out.println("Task is done");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();  // 关闭线程池
        }


/*
* Service 类不应该继承 Thread 类。在 Spring Boot 等现代框架中，服务类通常不直接继承 Thread 类，
* 因为这不符合 面向对象设计原则，而且可能会带来一些管理上的困难。相比之下，
* 使用线程池（例如 ExecutorService）或 Spring 提供的异步机制来管理线程会更加高效、灵活且符合现代开发的最佳实践。
* */

        /*
        * 在 Controller 和 Service 中不会直接使用 Runnable、Callable 或 Thread 来处理异步任务，
        * 而是通过以下两种方式来简化和管理线程：
        *
        * 1. 使用线程池（ThreadPool）
        *
        * 2. 使用 @Async 注解
        * */


        //我这里演示通过thread类实现多线程 基于一个工具类 已经继承了thread类

        MyThread thread = new MyThread();
        thread.start();  // 启动线程


        // 创建 MyRunnable 对象
        MyRunnable myRunnable = new MyRunnable();
        Thread threadone = new Thread(myRunnable);
        threadone.start();  // 启动线程，执行 run() 方法


/*
* Callable 主要是结合线程池来使用的，因为线程池提供了对线程的管理和调度功能，
* 使得并发任务的执行更加高效和可靠。虽然在技术上，你也可以不结合线程池直接使用 Callable，
* 但是这不是一个推荐的做法，因为这样会失去线程池的一些优势，比如线程复用、线程管理、任务调度等。
* */

        /*
        * ExecutorService 通常与 Callable 结合使用，以便能够获取任务执行的结果。
        * 不结合的话 无法获取返回值
        * */

        /*
        * ExecutorService一般和Callable或者runable结合使用  要返回值选callable 反之
        * 一般不会和thread类一起使用 效率很低
         *
        * */

        // 创建一个线程池，最大线程数为 1
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        MyCallable myCallable = new MyCallable();
        Future<Integer> future = executorService.submit(myCallable);
        try {
            Integer result = future.get();
            System.out.println("Task result: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            // 关闭线程池
            executorService.shutdown();
        }




    }

/*
* 在电商业务中，线程的状态转换方法可以用来处理各种任务，比如订单处理、支付验证、库存更新等。以下是一些典型的应用场景，以及如何使用这些线程状态转换方法：

### 1. **`start()`**
   - **应用场景**：在电商系统中，订单处理、支付、发送通知等操作通常是异步执行的，可以使用 `start()` 方法启动线程来处理这些任务。
   - **举例**：当用户下单时，可以启动一个新线程来处理库存更新、支付处理、发货等任务。`start()` 方法用于启动新的线程。

### 2. **`sleep(long millis)`**
   - **应用场景**：当电商系统需要等待某个操作完成时，可以使用 `sleep()` 来让当前线程暂停执行一段时间，避免浪费资源。
   - **举例**：在订单支付成功后，系统可能需要等一定的时间（比如 3 秒）来验证支付结果，再执行后续操作（如库存扣减）。此时可以使用 `sleep()` 方法来延迟处理。

### 3. **`wait()`** / **`notify()` / `notifyAll()`**
   - **应用场景**：在电商系统中，`wait()` 和 `notify()` 方法可以用于线程间的通信，尤其是在涉及库存操作、支付确认等需要等待其他线程结果的情况。
   - **举例**：
     - **`wait()`**：如果用户支付成功，系统需要等待库存线程确认商品是否有货。如果没有商品，支付线程可以调用 `wait()` 方法进入等待状态，直到库存线程更新库存并通过 `notify()` 唤醒支付线程。
     - **`notify()`**：库存线程处理完库存更新后，可以通过 `notify()` 唤醒支付线程，让支付线程继续执行订单确认操作。

### 4. **`join()`**
   - **应用场景**：当电商系统中有多个异步操作需要依赖其他线程的完成时，可以使用 `join()` 方法等待其他线程执行完毕后再继续进行。
   - **举例**：
     - 在用户下单时，可能需要同时执行多个操作（比如库存检查、支付验证、订单生成）。可以为每个任务启动一个线程，并使用 `join()` 方法在主线程中等待所有子线程执行完毕，确保所有操作完成后再进行下一步。

### 5. **`interrupt()`**
   - **应用场景**：如果电商系统中的某些操作超时，或者需要响应取消请求时，可以使用 `interrupt()` 方法来中断当前正在执行的线程。
   - **举例**：
     - 如果用户在支付过程中取消了订单，系统可以调用 `interrupt()` 来中断支付线程，避免不必要的后续处理。
     - 如果在订单处理中遇到延迟或者超时，可以调用 `interrupt()` 中断当前线程，抛出 `InterruptedException` 进行超时处理。

### 6. **`stop()`**（已弃用）
   - **应用场景**：虽然 `stop()` 方法已经不推荐使用，但如果在某些情况下需要立即停止线程（比如发生严重错误时），可以考虑通过其他方式来实现类似的效果，比如通过中断标志。
   - **举例**：
     - 在电商平台的订单处理过程中，如果遇到严重的系统错误或者用户要求取消操作，可以考虑停止相关任务线程。不过，推荐使用中断和其他异常处理机制来代替 `stop()`。

### 7. **`yield()`**
   - **应用场景**：当系统负载过重时，某些低优先级的任务可以使用 `yield()` 来让其他高优先级的线程有更多的时间来执行，从而提高系统的响应性。
   - **举例**：
     - 当系统有大量订单需要处理时，低优先级的库存更新线程可以调用 `yield()`，将执行时间让给更重要的任务（如支付确认），避免任务堆积和响应延迟。

### 总结：
在电商业务中，线程管理和状态转换方法可以帮助提升系统的效率和响应性，确保订单、支付、库存等任务的顺利执行。合理使用线程的状态转换可以有效避免线程阻塞、超时、资源浪费等问题，提高系统的并发处理能力。
* */


    @GetMapping("/shunxu")
    public  void  shunxuzhixing() {
        Thread t1 = new Thread(() -> {
            System.out.println("Thread t1 is executing");
        });

        Thread t2 = new Thread(() -> {
            try {
                t1.join(); // 等待 t1 执行完毕后再执行 t2
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Thread t2 is executing");
        });

        Thread t3 = new Thread(() -> {
            try {
                t2.join(); // 等待 t2 执行完毕后再执行 t3
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Thread t3 is executing");
        });

        t3.start();
        t1.start();
        t2.start();


    }






    private static int counter = 0;


    /*
    * 1. 实例同步方法（普通同步方法）：---------对象级别 就是类实例出来的级别
锁定的是当前对象的实例：当使用 synchronized 修饰一个实例方法时，锁是作用在调用该方法的对象实例上。在 increment() 方法中，锁是对 SynchronizedExample 类的实例对象加锁的。也就是说，同一时刻只有一个线程能够执行该实例的 increment() 方法。
影响同一对象的并发：多个线程可以并发执行不同对象的同步方法，但在同一个对象实例上，只有一个线程能执行同步方法，其他线程需要等待。
    *
    *
    * 2. 静态同步方法： ------------类的级别  类没有实例  类本身的级别
锁定的是类级别的 Class 对象：静态方法是属于类的，而不是某个具体的对象实例。
* 当一个静态方法使用 synchronized 修饰时，锁定的是类的 Class 对象（即 StaticSynchronizedExample.class）。
* 这意味着所有线程在执行 increment() 方法时，不仅要考虑对象实例的同步，还需要考虑类的级别锁。
* 因此，同一时刻，只能有一个线程执行该静态同步方法。
影响类的所有线程访问：多个线程可以并发执行不同对象的实例方法，但对于静态方法来说，
* 多个线程即使操作不同的对象实例，也会在访问静态同步方法时竞争同一个类级别的锁。
    *
    * */



    // 同步静态方法
    // 同步静态方法
    // 同步静态方法
    // 同步静态方法
    /*
    * 如果方法是静态的，锁的是类的 Class 对象，而不是当前对象。这样可以确保多个线程在访问同一个类的静态方法时的同步。
    * */
    public synchronized static  void increment() {
        counter++;
    }
    // 同步代码块
    // 同步代码块
    // 同步代码块
    // 同步代码块

    public void incrementone() {
        // 只对共享资源部分加锁
        synchronized (this) {
            counter++;
        }
    }
    public int getCounter() {
        return counter;
    }




    private static final Object lock1 = new Object();  // 用于同步 counter1 的操作
    private static final Object lock2 = new Object();  // 用于同步 counter2 的操作

    private static int counter1 = 0;
    private static int counter2 = 0;

    public static void incrementCounter1() {
        synchronized (lock1) {  // 使用 lock1 锁定代码块，确保线程安全
            counter1++;
        }
    }

    public static void incrementCounter2() {
        synchronized (lock2) {  // 使用 lock2 锁定代码块，确保线程安全
            counter2++;
        }
    }

    public static int getCounter1() {
        return counter1;
    }

    public static int getCounter2() {
        return counter2;
    }




    @RequestMapping("/Synchronized")
    public  void SynchronizedExample(String[] args) {

        // 多个线程操作同一对象
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                increment();
                System.out.println("thread1"+":"+counter);

            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
               increment();
                System.out.println("thread2"+":"+counter);
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread1.start();
        thread2.start();

        /*
        * 加入了上面的代码 主线程会等待t1和t2执行完毕后 才会继续执行后面的代码
        *
        * join() 方法会使当前线程（主线程）等待调用它的线程（thread1 和 thread2）执行完毕后再继续执行。
        * 也就是说，主线程会等待这两个线程完成后，才会输出最终的计数值 counter。
         * */

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Final Counter Value: " + getCounter());

/*
*************************下面是演示synchronized (lock1) 和 synchronized (lock2)的格式
* */

        Thread thread1one = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                incrementCounter1();  // 操作 counter1
            }
        });

        Thread thread2one = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
               incrementCounter2();  // 操作 counter2
            }
        });

        thread1one.start();
        thread2one.start();

        try {
            thread1one.join();  // 等待 thread1 完成
            thread2one.join();  // 等待 thread2 完成
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Counter 1 Value: " + getCounter1());
        System.out.println("Counter 2 Value: " + getCounter2());
    }


















    }












