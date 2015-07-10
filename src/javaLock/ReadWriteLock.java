package javaLock;

import java.util.HashMap;
import java.util.Map;

/**
 * ��д������-���ܹ��棬��-д���ܹ��棬д-д���ܹ���
 * 
 * 1. ������д�����������룩:
 * ��ȡ: û���߳�������д��������û���߳�������д������
 * д��: û���߳���������д������
 * ���Ǽ����д����������ȶԶ��������������Ҫ����Ҫ����д��������ȼ������⣬��������������ıȽ�Ƶ����������û������д���������ȼ�����ô�ͻ����������������
 * 
 * ��Ҫע����ǣ��������ͷ����ķ�����unlockRead��unlockWrite���У���������notifyAll������������notify�����ǿ�����������һ�����Σ�
 * ������߳��ڵȴ���ȡ������ͬʱ�����߳��ڵȴ���ȡд���������ʱ����һ���ȴ��������̱߳�notify�������ѣ�����Ϊ��ʱ��������д�����̴߳��ڣ�writeRequests>0�������Ա����ѵ��̻߳��ٴν�������״̬��
 * Ȼ�����ȴ�д�����߳�һ��Ҳû�����ѣ�����ʲôҲû������һ��������ע���źŶ�ʧ���󣩡�����õ���notifyAll���������е��̶߳��ᱻ���ѣ�Ȼ���ж��ܷ��������������
 * ��notifyAll����һ���ô�������ж�����߳��ڵȴ�������û���߳��ڵȴ�д��ʱ������unlockWrite()�����еȴ��������̶߳�������ɹ���ȡ���� ���� ������һ��ֻ����һ���� 
 * 
 * 2�� reentrant:
 * ��������: Ҫ��֤ĳ���߳��еĶ��������룬Ҫô�����ȡ������������û��д��д���󣩣�Ҫô�Ѿ����ж����������Ƿ���д���󣩡� 
 * д������: ����һ���߳��Ѿ�����д����������д�����루�ٴλ��д������
 * ���������ǿ��Կ�����ֻ����û���߳�ӵ��д��������²�������������롣���⣬����Ķ�����д�����ȼ��ߡ� 
 * 
 * 3. ��дת��:
 * ����������д��: ��Ҫ���������Ĳ�����Ҫ������߳���Ψһһ��ӵ�ж������̡߳�
 * д������������: ���һ���߳�ӵ����д������ô��Ȼ�����߳��ǲ�����ӵ�ж�����д���ˡ����Զ���һ��ӵ��д�����̣߳��ٻ�ö������ǲ�����ʲôΣ�յġ�
 */
public class ReadWriteLock {
	private Map<Thread, Integer> readingThreads = new HashMap<Thread, Integer>();
	
	private int 	writeAccesses   = 0;
	private int 	writeRequests   = 0;
	private Thread 	writingThread 	= null;
	
	public synchronized void lockRead() throws InterruptedException {
		Thread callingThread = Thread.currentThread();
		while(! canGrantReadAccess(callingThread)) {
			wait();
		}
		readingThreads.put(callingThread, getReadAccessCount(callingThread) + 1);
	}
	
	private boolean canGrantReadAccess(Thread callingThread) {
		if(isWriter(callingThread)) return true;
		if(hasWriter()) return false;
		if(isReader(callingThread)) return true;
		if(hasWriteRequests()) return false;
		return true;
	}
	
	public synchronized void unlockRead() {
		Thread callingThread = Thread.currentThread();
		if(!isReader(callingThread)) {
			throw new IllegalMonitorStateException("Calling Thread does not hold a read lock on this ReadWriteLock");
		}
		int accessCount = getReadAccessCount(callingThread);
		if(accessCount == 1) {
			readingThreads.remove(callingThread);
		} 
		else {
			readingThreads.put(callingThread, (accessCount -1));
		}
		notifyAll();
	}
	
	public synchronized void lockWrite() throws InterruptedException {
		writeRequests++;
		Thread callingThread = Thread.currentThread();
		while(!canGrantWriteAccess(callingThread)){
			wait();
		}
		writeRequests--;
		writeAccesses++;
		writingThread = callingThread;
	}
	
	public synchronized void unlockWrite() throws InterruptedException {
		if(!isWriter(Thread.currentThread())) {
			throw new IllegalMonitorStateException("Calling Thread does not hold the write lock on this ReadWriteLock");
		}
		writeAccesses--;
		if(writeAccesses == 0){
			writingThread = null;
		}
		notifyAll();
	}
	
	private boolean canGrantWriteAccess(Thread callingThread) {
		if(isOnlyReader(callingThread)) return true;
		if(hasReaders()) return false;
		if(writingThread == null) return true;
		if(!isWriter(callingThread)) return false;
		return true;
	}
	
	private int getReadAccessCount(Thread callingThread) {
		Integer accessCount = readingThreads.get(callingThread);
		if(accessCount == null) return 0;
		return accessCount.intValue();
	}
	
	private boolean hasReaders() {
		return readingThreads.size() > 0;
	}
	
	private boolean isReader(Thread callingThread) {
		return readingThreads.get(callingThread) != null;
	}
	
	private boolean isOnlyReader(Thread callingThread) {
		return readingThreads.size() == 1 && readingThreads.get(callingThread) != null;
	}
	
	private boolean hasWriter() {
		return writingThread != null;
	}
	
	private boolean isWriter(Thread callingThread) {
		return writingThread == callingThread;
	}
	
	private boolean hasWriteRequests() {
		return this.writeRequests > 0;
	}
}


//����Ҫע����ʹ��lockʱ����finally�е���unlock()����֤�ٽ������׳��쳣ʱReadWriteLockҲ�ᱻ�ͷš�