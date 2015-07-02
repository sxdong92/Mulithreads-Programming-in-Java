package threadSignaling;

/**
 * Signaling via Shared Objects.
 * 
 * �߳�A��B������ָ��һ��MySignal����ʵ�������ã��Ա����ͨ�š�
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
 * Busy wait æ�ȴ�
 * 
 * protected MySignal sharedSignal = ...
 * ...
 * while(!sharedSignal.hasDataToProcess()) {
 * 		//do nothing... busy waiting
 * }
 * ׼���������ݵ��߳�B���ڵȴ����ݱ�Ϊ���á����仰˵�����ڵȴ��߳�A��һ���źţ�����ź�ʹhasDataToProcess()����true���߳�B������һ��ѭ����Եȴ�����źţ�
 * æ�ȴ�û�ж����еȴ��̵߳�CPU������Ч�����ã�����ƽ���ȴ�ʱ��ǳ��̡������õȴ��߳̽���˯�߻��߷�����״̬��Ϊ���ǣ�ֱ�������յ����ȴ����źš�
 * Java��һ���ڽ��ĵȴ������������߳��ڵȴ��źŵ�ʱ���Ϊ������״̬��java.lang.Object �ඨ��������������wait()��notify()����notifyAll()��ʵ������ȴ����ơ�
 */

