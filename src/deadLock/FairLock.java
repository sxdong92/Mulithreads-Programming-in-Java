package deadLock;

import java.util.ArrayList;
import java.util.List;

/**
 * FairLock新创建了一个QueueObject的实例，并对每个调用lock()的线程进行入队列。
 * 调用unlock()的线程将从队列头部获取QueueObject，并对其调用doNotify()，以唤醒在该对象上等待的线程。
 * 通过这种方式，在同一时间仅有一个等待线程获得唤醒，而不是所有的等待线程。这也是实现FairLock公平性的核心所在。
 * 
 * 请注意，在同一个同步块中，锁状态依然被检查和设置，以避免出现滑漏条件。
 * 
 * 还需注意到，QueueObject实际是一个semaphore。doWait()和doNotify()方法在QueueObject中保存着信号。
 * 这样做以避免一个线程在调用queueObject.doWait()之前被另一个调用unlock()并随之调用queueObject.doNotify()的线程重入，从而导致信号丢失。
 * queueObject.doWait()调用放置在synchronized(this)块之外，以避免被monitor嵌套锁死，所以另外的线程可以解锁，只要当没有线程在lock方法的synchronized(this)块中执行即可。
 * 
 * 最后，注意到queueObject.doWait()在try – catch块中是怎样调用的。在InterruptedException抛出的情况下，线程得以离开lock()，并需让它从队列中移除。
 *
 */
public class FairLock {
	private boolean           isLocked       = false;
	private Thread            lockingThread  = null;
	private List<QueueObject> waitingThreads = new ArrayList<QueueObject>();
	
	public void lock() throws InterruptedException {
		QueueObject queueObject 			= new QueueObject();
		boolean     isLockedForThisThread 	= true;
		synchronized(this) {
			waitingThreads.add(queueObject);
		}
		
		while(isLockedForThisThread) {
			synchronized(this) {
				// 如果没有其他线程占用FairLock且队列中只有当前线程, 那么不必wait。 (即跳入下面if中)
				isLockedForThisThread = isLocked || waitingThreads.get(0) != queueObject;
				if(!isLockedForThisThread) {
					isLocked = true;
					waitingThreads.remove(queueObject);
					lockingThread = Thread.currentThread();
					return;
				}
			}
			try {
				queueObject.doWait();
			} catch(InterruptedException e) {
				synchronized(this) {
					waitingThreads.remove(queueObject);
				}
				throw e;
			}
		}
	}
	
	public synchronized void unlock() {
		if(this.lockingThread != Thread.currentThread()) {
			throw new IllegalMonitorStateException("Calling thread has not locked this lock");
		}
		isLocked      = false;
		lockingThread = null;
		if(waitingThreads.size() > 0) {
			waitingThreads.get(0).doNotify();
		}
	}
}

class QueueObject {
	private boolean isNotified = false;
	
	public synchronized void doWait() throws InterruptedException {
		while(!isNotified) {
			this.wait();
		}
		this.isNotified = false;
	}
	
	public synchronized void doNotify() {
		this.isNotified = true;
		this.notify();
	}
	
	public boolean equals(Object o) {
		return this == o;
	}
}
