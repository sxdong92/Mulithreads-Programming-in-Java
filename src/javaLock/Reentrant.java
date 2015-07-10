package javaLock;

/**
 * Java中的synchronized同步块是可重入的。这意味着如果一个java线程进入了代码中的synchronized同步块，
 * 并因此获得了该同步块使用的同步对象对应的管程上的锁，那么这个线程可以进入由同一个管程对象所同步的另一个java代码块。
 */
public class Reentrant {
	/**
	 * 注意outer()和inner()都被声明为synchronized，这在Java中和synchronized(this)块等效。
	 */
	public synchronized void outer() {
		inner();
	}
	
	public synchronized void inner() {
		// do something...
	}
}

/**
 * 前面给出的锁实现不是可重入的。如果我们像下面这样重写Reentrant类，当线程调用outer()时，会在inner()方法的lock.lock()处阻塞住。
 */
class Reentrant2 {
	Lock lock = new Lock();
	
	public void outer() throws InterruptedException {
		lock.lock();
		inner();
		lock.unlock();
	}
	
	public synchronized void inner() throws InterruptedException {
		lock.lock();
		//do something ...
		lock.unlock();
	}
}

/**
 * 为了让这个Lock类具有可重入性，我们需要对它做一点小的改动
 */
class ReentrantLock {
	boolean isLocked = false;
	Thread  lockedBy = null;
	int lockedCount = 0;
	
	public synchronized void lock() throws InterruptedException {
		Thread callingThread = Thread.currentThread();
		while(isLocked && lockedBy != callingThread) {
			wait();
		}
		isLocked = true;
		lockedCount++;
		lockedBy = callingThread;
	}
	
	public synchronized void unlock() {
		if(Thread.currentThread() == this.lockedBy) {
			lockedCount--;
			if(lockedCount == 0) {
				isLocked = false;
				notify();
			}
		}
	}
}

/**
 * 如果用Lock来保护临界区，并且临界区有可能会抛出异常，那么在finally语句中调用unlock()就显得非常重要了。这样可以保证这个锁对象可以被解锁以便其它线程能继续对其加锁。
 * 
 * 这个简单的结构可以保证当临界区抛出异常时Lock对象可以被解锁。
 * 如果不是在finally语句中调用的unlock()，当临界区抛出异常时，Lock对象将永远停留在被锁住的状态，这会导致其它所有在该Lock对象上调用lock()的线程一直阻塞。 
 */
class Reentrant3 {
	Lock lock = new Lock();
	
	public void outer() throws InterruptedException {
		lock.lock();
		try {
			inner();
		} finally {
			lock.unlock();
		}
	}
	
	public synchronized void inner() throws InterruptedException {
		lock.lock();
		try {
			//do something ...
		}
		finally {
			lock.unlock();
		}
	}
}
