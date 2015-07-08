package deadLock;

public class Synchronizer {
	/**
	 * �����һ�����ϵ��̵߳���doSynchronized()�������ڵ�һ����÷��ʵ��߳�δ���ǰ�������߳̽�һֱ��������״̬�����������ֶ��̱߳������ĳ����£������������ĸ��̻߳�÷�����û�б��ϵġ�
	 * Ϊ����ߵȴ��̵߳Ĺ�ƽ�ԣ�����Ӧ��ʹ������ʽ�����ͬ���顣
	 */
	public synchronized void doSynchronizedWithKeyword() {
		//do a lot of work which takes a long time
	}
	
	Lock lock = new Lock();
	/**
	 * ע�⵽doSynchronized()��������Ϊsynchronized��������lock.lock()��lock.unlock()�������
	 * 
	 * ע�⵽�����Lock��ʵ�֣�������ڶ��̲߳�������lock()����Щ�߳̽������ڶ�lock()�����ķ����ϡ�
	 * ���⣬������Ѿ����ϣ�У��ע������ָ����isLocked����trueʱ������Щ�߳̽�������while(isLocked)ѭ����wait()�������档
	 * ����ζ�Ŵ󲿷�ʱ�����ڵȴ��������ͽ����ٽ����Ĺ���������wait()�ĵȴ��У������Ǳ���������ͼ����lock()�����С�
	 * 
	 * ͬ���鲻��Եȴ�����Ķ���߳�˭�ܻ�÷������κα��ϣ�ͬ��������notify()ʱ��wait()Ҳ����������һ���ܻ����̣߳�����Ϊʲô���뿴�߳�ͨ�ţ���
	 * �������汾��Lock���doSynchronized()�Ǹ��汾�ͱ��Ϲ�ƽ�Զ��ԣ�û���κ�����
	 * 
	 * �������ܸı������������ǰ��Lock��汾�����Լ���wait()���������ÿ���߳��ڲ�ͬ�Ķ����ϵ���wait()����ôֻ��һ���̻߳��ڸö����ϵ���wait()��
	 * Lock����Ծ����ĸ������ܶ������notify()�������������Ч��ѡ�����ĸ��߳�.
	 */
	public void doSynchronized() throws InterruptedException {
		this.lock.lock();
		//critical section, do a lot of work which takes a long time
		this.lock.unlock();
	}
}

class Lock {
	private boolean isLocked = false;
	private Thread lockingThread = null;
	
	public synchronized void lock() throws InterruptedException {
		while(isLocked) {
			wait();
		}
		isLocked = true;
		lockingThread = Thread.currentThread();
	}
	
	public synchronized void unlock() {
		if(this.lockingThread != Thread.currentThread()) {
			throw new IllegalMonitorStateException("Calling thread has not locked this lock");
		}
		isLocked = false;
		lockingThread = null;
		notify();
	}
}