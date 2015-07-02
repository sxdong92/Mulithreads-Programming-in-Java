package threadSignaling;

/**
 * Signaling via Shared Objects.
 * 
 * 线程A和B必须获得指向一个MySignal共享实例的引用，以便进行通信。
 * 
 */
public class MySignal {
	protected boolean hasDataToProcess = false;
	
	public synchronized boolean hasDataToProcess() {
		return this.hasDataToProcess;
	}
	
	public synchronized void setHasDataToProcess(boolean hasData) {
		this.hasDataToProcess = hasData;
	}
}

/**
 * Busy wait 忙等待
 * 
 * protected MySignal sharedSignal = ...
 * ...
 * while(!sharedSignal.hasDataToProcess()) {
 * 		//do nothing... busy waiting
 * }
 * 准备处理数据的线程B正在等待数据变为可用。换句话说，它在等待线程A的一个信号，这个信号使hasDataToProcess()返回true。线程B运行在一个循环里，以等待这个信号：
 * 忙等待没有对运行等待线程的CPU进行有效的利用，除非平均等待时间非常短。否则，让等待线程进入睡眠或者非运行状态更为明智，直到它接收到它等待的信号。
 * Java有一个内建的等待机制来允许线程在等待信号的时候变为非运行状态。java.lang.Object 类定义了三个方法，wait()、notify()、和notifyAll()来实现这个等待机制。
 */

