package threadSignaling;

/**
 * ���һ���߳����ڱ�֪ͨ�̵߳���wait()ǰ������notify()���ȴ����߳̽���������źš���ĳЩ����£������ʹ�ȴ��߳���Զ�ڵȴ�����������.
 * Ϊ�˱��ⶪʧ�źţ���������Ǳ������ź������MyWaitNotify�������У�֪ͨ�ź�Ӧ���洢��MyWaitNotifyʵ����һ����Ա�����
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