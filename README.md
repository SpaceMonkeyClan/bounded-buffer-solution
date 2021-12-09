# Race condition with a shared buffer

## Project objective

Implement the solution to the bounded buffer problem from the section titled Semaphores, but without any P or V operations. Observe and eliminate a **race condition**, which refers to a situation where multiple processes manipulate some shared data concurrently and the outcome depends on the order of execution.

## Description

- The buffer is a large array of n integers, initialized to all zeros.
- The producer and the consumer are separate concurrent threads in a process.
- The producer executes short bursts of random duration. During each burst of length k1, the producer adds a 1 to the next k1 slots of the buffer, modulo n.
- The consumer also executes short bursts of random duration. During each burst of length k2, the consumer reads the next k2 slots and resets each to 0.
- If any slot contains a number greater than 1, then a race condition has been detected: The consumer was unable to keep up and thus the producer has added a 1 to a slot that has not yet been reset.
- Both producer and consumer sleep periodically for random time intervals to emulate unpredictable execution speeds.

![App Icon](bounded-buffer-solution/images/promt_pic.PNG)

## Assignment

1. Experiment with different values of n, k, and t until a race condition is observed.
2. Modify the solution by including the necessary P and V operations in the code.
If general P and V operations are not provided by the tread library then first implement P and V using binary semaphores (mutex lock or spin locks.)
