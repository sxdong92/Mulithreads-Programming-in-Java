package threadSignaling;

/**
 * ����Ī�������ԭ���߳��п�����û�е��ù�notify()��notifyAll()��������������������ν�ļٻ��ѣ�spurious wakeups��������ܵ������Ӧ�ó�������������⡣
 *
 * Ϊ�˷�ֹ�ٻ��ѣ������źŵĳ�Ա��������һ��whileѭ������ܼ�飬��������if���ʽ�
 * ������һ��whileѭ��������������Уע����������Ҫ���أ�Ŀǰ��JVMʵ������������CPU�������ʱ�䲻����doNotify������doWait������һֱ������CPU������̫�󣩡�
 * �����ѵ��̻߳�����ֱ��������(whileѭ��)���������Ϊfalse��
 * 
 * ����wait()��������whileѭ���������if���ʽ�����ȴ��߳�û���յ��źžͻ��ѣ�wasSignalled��������Ϊfalse,whileѭ������ִ��һ�Σ���ʹ�������̻߳ص��ȴ�״̬��
 * 
 * ������ж���߳��ڵȴ�����notifyAll()���ѣ���ֻ��һ�����������ִ�У�ʹ��whileѭ��Ҳ�Ǹ��÷�����
 * ÿ��ֻ��һ���߳̿��Ի�ü���������������ζ��ֻ��һ���߳̿����˳�wait()���ò����wasSignalled��־����Ϊfalse����
 * һ������߳��˳�doWait()��ͬ���飬�����߳��˳�wait()���ã�����whileѭ������wasSignalled����ֵ��
 * ���ǣ������־�Ѿ�����һ�����ѵ��߳�����ˣ����������������߳̽��ص��ȴ�״̬��ֱ���´��źŵ�����
 */
public class MyWaitNotify3 {
	MonitorObject myMonitorObject = new MonitorObject();
	boolean wasSignalled = false;
	
	public void doWait() {
		synchronized(myMonitorObject) {
			while(!wasSignalled) {
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
