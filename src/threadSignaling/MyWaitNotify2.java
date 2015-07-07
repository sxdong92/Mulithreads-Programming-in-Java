package threadSignaling;

/**
 * 如果一个线程先于被通知线程调用wait()前调用了notify()，等待的线程将错过这个信号。在某些情况下，这可能使等待线程永远在等待，不再醒来.
 * 为了避免丢失信号，必须把它们保存在信号类里。在MyWaitNotify的例子中，通知信号应被存储在MyWaitNotify实例的一个成员变量里。
 */
public class MyWaitNotify2 {
	MonitorObject myMonitorObject = new MonitorObject();
	boolean wasSignalled = false;
	
	public void doWait() {
		synchronized(myMonitorObject) {
			if(!wasSignalled) {
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
