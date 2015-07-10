package javaLock;

/**
 * Java�е�synchronizedͬ�����ǿ�����ġ�����ζ�����һ��java�߳̽����˴����е�synchronizedͬ���飬
 * ����˻���˸�ͬ����ʹ�õ�ͬ�������Ӧ�Ĺܳ��ϵ�������ô����߳̿��Խ�����ͬһ���̶ܳ�����ͬ������һ��java����顣
 */
public class Reentrant {
	/**
	 * ע��outer()��inner()��������Ϊsynchronized������Java�к�synchronized(this)���Ч��
	 */
	public synchronized void outer() {
		inner();
	}
	
	public synchronized void inner() {
		// do something...
	}
}

/**
 * ǰ���������ʵ�ֲ��ǿ�����ġ��������������������дReentrant�࣬���̵߳���outer()ʱ������inner()������lock.lock()������ס��
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
 * Ϊ�������Lock����п������ԣ�������Ҫ������һ��С�ĸĶ�
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
 * �����Lock�������ٽ����������ٽ����п��ܻ��׳��쳣����ô��finally����е���unlock()���Ե÷ǳ���Ҫ�ˡ��������Ա�֤�����������Ա������Ա������߳��ܼ������������
 * 
 * ����򵥵Ľṹ���Ա�֤���ٽ����׳��쳣ʱLock������Ա�������
 * ���������finally����е��õ�unlock()�����ٽ����׳��쳣ʱ��Lock������Զͣ���ڱ���ס��״̬����ᵼ�����������ڸ�Lock�����ϵ���lock()���߳�һֱ������ 
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
