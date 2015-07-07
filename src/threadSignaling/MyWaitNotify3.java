package threadSignaling;

/**
 * 由于莫名其妙的原因，线程有可能在没有调用过notify()和notifyAll()的情况下醒来。这就是所谓的假唤醒（spurious wakeups）。这可能导致你的应用程序出现严重问题。
 *
 * 为了防止假唤醒，保存信号的成员变量将在一个while循环里接受检查，而不是在if表达式里。
 * 这样的一个while循环叫做自旋锁（校注：这种做法要慎重，目前的JVM实现自旋会消耗CPU，如果长时间不调用doNotify方法，doWait方法会一直自旋，CPU会消耗太大）。
 * 被唤醒的线程会自旋直到自旋锁(while循环)里的条件变为false。
 * 
 * 留意wait()方法是在while循环里，而不在if表达式里。如果等待线程没有收到信号就唤醒，wasSignalled变量将变为false,while循环会再执行一次，促使醒来的线程回到等待状态。
 */
public class MyWaitNotify3 {
	MonitorObject myMonitorObject = new MonitorObject();
	boolean wasSignalled = false;
	
	public void doWait() {
		synchronized(myMonitorObject) {
			while(!wasSignalled) {
				try {
					myMonitorObject.wait();
				} catch(InterruptedException e) {
					System.out.println("Interrupted Exception");
				}
			}
			wasSignalled = false;
		}
	}
	
	public void doNotify() {
		synchronized(myMonitorObject) {
			wasSignalled = true;
			myMonitorObject.notify();
		}
	}
}
