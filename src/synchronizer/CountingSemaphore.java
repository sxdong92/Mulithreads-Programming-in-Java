package synchronizer;

/**
 * Semaphore的简单实现并没有计算通过调用take方法所产生信号的数量。可以把它改造成具有计数功能的Semaphore。下面是一个可计数的Semaphore的简单实现。
 */
public class CountingSemaphore {
	private int signals = 0;
	
	public synchronized void take() {
		this.signals++;
		this.notify();
	}
	
	public synchronized void release() throws InterruptedException {
		while(this.signals == 0) {
			wait();
		}
		this.signals--;
	}
}
