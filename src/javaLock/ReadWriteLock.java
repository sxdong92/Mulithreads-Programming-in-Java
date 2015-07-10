package javaLock;

import java.util.HashMap;
import java.util.Map;

/**
 * 读写锁：读-读能共存，读-写不能共存，写-写不能共存
 * 
 * 1. 基本读写锁（不可重入）:
 * 读取: 没有线程正在做写操作，且没有线程在请求写操作。
 * 写入: 没有线程正在做读写操作。
 * 我们假设对写操作的请求比对读操作的请求更重要，就要提升写请求的优先级。此外，如果读操作发生的比较频繁，我们又没有提升写操作的优先级，那么就会产生“饥饿”现象。
 * 
 * 需要注意的是，在两个释放锁的方法（unlockRead，unlockWrite）中，都调用了notifyAll方法，而不是notify。我们可以想象下面一种情形：
 * 如果有线程在等待获取读锁，同时又有线程在等待获取写锁。如果这时其中一个等待读锁的线程被notify方法唤醒，但因为此时仍有请求写锁的线程存在（writeRequests>0），所以被唤醒的线程会再次进入阻塞状态。
 * 然而，等待写锁的线程一个也没被唤醒，就像什么也没发生过一样（译者注：信号丢失现象）。如果用的是notifyAll方法，所有的线程都会被唤醒，然后判断能否获得其请求的锁。
 * 用notifyAll还有一个好处。如果有多个读线程在等待读锁且没有线程在等待写锁时，调用unlockWrite()后，所有等待读锁的线程都能立马成功获取读锁 —— 而不是一次只允许一个。 
 * 
 * 2。 reentrant:
 * 读锁重入: 要保证某个线程中的读锁可重入，要么满足获取读锁的条件（没有写或写请求），要么已经持有读锁（不管是否有写请求）。 
 * 写锁重入: 仅当一个线程已经持有写锁，才允许写锁重入（再次获得写锁）。
 * 代码中我们可以看到，只有在没有线程拥有写锁的情况下才允许读锁的重入。此外，重入的读锁比写锁优先级高。 
 * 
 * 3. 读写转换:
 * 读锁升级到写锁: 想要允许这样的操作，要求这个线程是唯一一个拥有读锁的线程。
 * 写锁降级到读锁: 如果一个线程拥有了写锁，那么自然其它线程是不可能拥有读锁或写锁了。所以对于一个拥有写锁的线程，再获得读锁，是不会有什么危险的。
 */
public class ReadWriteLock {
	private Map<Thread, Integer> readingThreads = new HashMap<Thread, Integer>();
	
	private int 	writeAccesses   = 0;
	private int 	writeRequests   = 0;
	private Thread 	writingThread 	= null;
	
	public synchronized void lockRead() throws InterruptedException {
		Thread callingThread = Thread.currentThread();
		while(! canGrantReadAccess(callingThread)) {
			wait();
		}
		readingThreads.put(callingThread, getReadAccessCount(callingThread) + 1);
	}
	
	private boolean canGrantReadAccess(Thread callingThread) {
		if(isWriter(callingThread)) return true;
		if(hasWriter()) return false;
		if(isReader(callingThread)) return true;
		if(hasWriteRequests()) return false;
		return true;
	}
	
	public synchronized void unlockRead() {
		Thread callingThread = Thread.currentThread();
		if(!isReader(callingThread)) {
			throw new IllegalMonitorStateException("Calling Thread does not hold a read lock on this ReadWriteLock");
		}
		int accessCount = getReadAccessCount(callingThread);
		if(accessCount == 1) {
			readingThreads.remove(callingThread);
		} 
		else {
			readingThreads.put(callingThread, (accessCount -1));
		}
		notifyAll();
	}
	
	public synchronized void lockWrite() throws InterruptedException {
		writeRequests++;
		Thread callingThread = Thread.currentThread();
		while(!canGrantWriteAccess(callingThread)){
			wait();
		}
		writeRequests--;
		writeAccesses++;
		writingThread = callingThread;
	}
	
	public synchronized void unlockWrite() throws InterruptedException {
		if(!isWriter(Thread.currentThread())) {
			throw new IllegalMonitorStateException("Calling Thread does not hold the write lock on this ReadWriteLock");
		}
		writeAccesses--;
		if(writeAccesses == 0){
			writingThread = null;
		}
		notifyAll();
	}
	
	private boolean canGrantWriteAccess(Thread callingThread) {
		if(isOnlyReader(callingThread)) return true;
		if(hasReaders()) return false;
		if(writingThread == null) return true;
		if(!isWriter(callingThread)) return false;
		return true;
	}
	
	private int getReadAccessCount(Thread callingThread) {
		Integer accessCount = readingThreads.get(callingThread);
		if(accessCount == null) return 0;
		return accessCount.intValue();
	}
	
	private boolean hasReaders() {
		return readingThreads.size() > 0;
	}
	
	private boolean isReader(Thread callingThread) {
		return readingThreads.get(callingThread) != null;
	}
	
	private boolean isOnlyReader(Thread callingThread) {
		return readingThreads.size() == 1 && readingThreads.get(callingThread) != null;
	}
	
	private boolean hasWriter() {
		return writingThread != null;
	}
	
	private boolean isWriter(Thread callingThread) {
		return writingThread == callingThread;
	}
	
	private boolean hasWriteRequests() {
		return this.writeRequests > 0;
	}
}


//依旧要注意在使用lock时，在finally中调用unlock()，保证临界区中抛出异常时ReadWriteLock也会被释放。