package deadLock;

public class Synchronizer {
	/**
	 * 如果有一个以上的线程调用doSynchronized()方法，在第一个获得访问的线程未完成前，其他线程将一直处于阻塞状态，而且在这种多线程被阻塞的场景下，接下来将是哪个线程获得访问是没有保障的。
	 * 为了提高等待线程的公平性，我们应该使用锁方式来替代同步块。
	 */
	public synchronized void doSynchronizedWithKeyword() {
		//do a lot of work which takes a long time
	}
	
	Lock lock = new Lock();
	/**
	 * 注意到doSynchronized()不再声明为synchronized，而是用lock.lock()和lock.unlock()来替代。
	 * 
	 * 注意到上面对Lock的实现，如果存在多线程并发访问lock()，这些线程将阻塞在对lock()方法的访问上。
	 * 另外，如果锁已经锁上（校对注：这里指的是isLocked等于true时），这些线程将阻塞在while(isLocked)循环的wait()调用里面。
	 * 这意味着大部分时间用在等待进入锁和进入临界区的过程是用在wait()的等待中，而不是被阻塞在试图进入lock()方法中。
	 * 
	 * 同步块不会对等待进入的多个线程谁能获得访问做任何保障，同样当调用notify()时，wait()也不会做保障一定能唤醒线程（至于为什么，请看线程通信）。
	 * 因此这个版本的Lock类和doSynchronized()那个版本就保障公平性而言，没有任何区别。
	 * 
	 * 但我们能改变这种情况。当前的Lock类版本调用自己的wait()方法，如果每个线程在不同的对象上调用wait()，那么只有一个线程会在该对象上调用wait()，
	 * Lock类可以决定哪个对象能对其调用notify()，因此能做到有效的选择唤醒哪个线程.
	 */
	public void doSynchronized() throws InterruptedException {
		this.lock.lock();
		//critical section, do a lot of work which takes a long time
		this.lock.unlock();
	}
}

class Lock {
	private boolean isLocked = false;
	private Thread lockingThread = null;
	
	public synchronized void lock() throws InterruptedException {
		while(isLocked) {
			wait();
		}
		isLocked = true;
		lockingThread = Thread.currentThread();
	}
	
	public synchronized void unlock() {
		if(this.lockingThread != Thread.currentThread()) {
			throw new IllegalMonitorStateException("Calling thread has not locked this lock");
		}
		isLocked = false;
		lockingThread = null;
		notify();
	}
}