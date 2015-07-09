package javaLock;

public class Counter {
	private int count = 0;
	
	public int inc() {
		synchronized(this) {
			return ++count;
		}
	}
}

/**
 * ���µ�Counter����Lock����synchronized�ﵽ��ͬ����Ŀ�ģ�
 */
class CounterWithLock {
	private Lock lock = new Lock();
	private int count = 0;
	
	public int inc() throws InterruptedException {
		lock.lock();
		int newCount = ++count;
		lock.unlock();
		return newCount;
	}
}

/**
 * ע�����е�while(isLocked)ѭ�������ֱ�����������������
 * Ϊ��ֹ���߳�û���յ�notify()����Ҳ��wait()�з��أ�Ҳ������ٻ��ѣ�������̻߳�����ȥ���isLocked�����Ծ�����ǰ�Ƿ���԰�ȫ�ؼ���ִ�л�����Ҫ���±��ֵȴ�����������Ϊ�̱߳������˾Ϳ��԰�ȫ�ؼ���ִ���ˡ� 
 * ���߳�������ٽ�����λ��lock()��unlock()֮�䣩�еĴ��룬�ͻ����unlock()��
 * ִ��unlock()�����½�isLocked����Ϊfalse������֪ͨ�����ѣ�����һ�������еĻ�����lock()�����е�����wait()���������ڵȴ�״̬���̡߳�
 */
class Lock {
	private boolean isLocked = false;
	
	public synchronized void lock() throws InterruptedException {
		while(isLocked) {
			wait();
		}
		isLocked = true;
	}
	
	public synchronized void unlock() {
		isLocked = false;
		notify();
	}
}