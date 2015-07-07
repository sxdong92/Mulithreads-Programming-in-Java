package threadSignaling;
/**
 * 一个线程一旦调用了任意对象的wait()方法，就会变为非运行状态，直到另一个线程调用了同一个对象的notify()方法。
 * 为了调用wait()或者notify()，线程必须先获得那个对象的锁。也就是说，线程必须在同步块里调用wait()或者notify()。
 * 
 * 等待线程将调用doWait()，而唤醒线程将调用doNotify()。
 * 
 * 如你所见，不管是等待线程还是唤醒线程都在同步块里调用wait()和notify()。这是强制性的！
 * 一个线程如果没有持有对象锁，将不能调用wait()，notify()或者notifyAll()。否则，会抛出IllegalMonitorStateException异常。
 * 
 * 但是，这怎么可能？等待线程在同步块里面执行的时候，不是一直持有监视器对象（myMonitor对象）的锁吗？等待线程不能阻塞唤醒线程进入doNotify()的同步块吗？
 * 答案是：的确不能一直持有。一旦线程调用了wait()方法，它就释放了所持有的监视器对象上的锁。这将允许其他线程也可以调用wait()或者notify()。
 * 
 * 一旦一个线程被唤醒，不能立刻就退出wait()的方法调用，直到调用notify()的线程退出了它自己的同步块。
 * 换句话说：被唤醒的线程必须重新获得监视器对象的锁，才可以退出wait()的方法调用，因为wait方法调用运行在同步块里面。
 * 如果多个线程被notifyAll()唤醒，那么在同一时刻将只有一个线程可以退出wait()方法，因为每个线程在退出wait()前必须获得监视器对象的锁。
 * (即原wait线程们依次重新获得锁以便退出wait方法...)
 */
public class MyWaitNotify {
	MonitorObject myMonitorObject = new MonitorObject();
	
	public void doWait() {
		synchronized(myMonitorObject) {
			try {
				myMonitorObject.wait();
			} catch(InterruptedException e) {
				System.out.println("Interrupted Exception");
			}
		}
	}
	
	public void doNotify() {
		synchronized(myMonitorObject) {
			myMonitorObject.notify();
		}
	}
}

class MonitorObject {
	
}