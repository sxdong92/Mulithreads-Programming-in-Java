package deadLock;

import java.util.ArrayList;
import java.util.List;

/**
 * FairLock�´�����һ��QueueObject��ʵ��������ÿ������lock()���߳̽�������С�
 * ����unlock()���߳̽��Ӷ���ͷ����ȡQueueObject�����������doNotify()���Ի����ڸö����ϵȴ����̡߳�
 * ͨ�����ַ�ʽ����ͬһʱ�����һ���ȴ��̻߳�û��ѣ����������еĵȴ��̡߳���Ҳ��ʵ��FairLock��ƽ�Եĺ������ڡ�
 * 
 * ��ע�⣬��ͬһ��ͬ�����У���״̬��Ȼ���������ã��Ա�����ֻ�©������
 * 
 * ����ע�⵽��QueueObjectʵ����һ��semaphore��doWait()��doNotify()������QueueObject�б������źš�
 * �������Ա���һ���߳��ڵ���queueObject.doWait()֮ǰ����һ������unlock()����֮����queueObject.doNotify()���߳����룬�Ӷ������źŶ�ʧ��
 * queueObject.doWait()���÷�����synchronized(this)��֮�⣬�Ա��ⱻmonitorǶ������������������߳̿��Խ�����ֻҪ��û���߳���lock������synchronized(this)����ִ�м��ɡ�
 * 
 * ���ע�⵽queueObject.doWait()��try �C catch�������������õġ���InterruptedException�׳�������£��̵߳����뿪lock()�����������Ӷ������Ƴ���
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
				// ���û�������߳�ռ��FairLock�Ҷ�����ֻ�е�ǰ�߳�, ��ô����wait�� (����������if��)
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