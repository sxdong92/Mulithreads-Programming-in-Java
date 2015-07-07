package threadSignaling;
/**
 * һ���߳�һ����������������wait()�������ͻ��Ϊ������״̬��ֱ����һ���̵߳�����ͬһ�������notify()������
 * Ϊ�˵���wait()����notify()���̱߳����Ȼ���Ǹ����������Ҳ����˵���̱߳�����ͬ���������wait()����notify()��
 * 
 * �ȴ��߳̽�����doWait()���������߳̽�����doNotify()��
 * 
 * ���������������ǵȴ��̻߳��ǻ����̶߳���ͬ���������wait()��notify()������ǿ���Եģ�
 * һ���߳����û�г��ж������������ܵ���wait()��notify()����notifyAll()�����򣬻��׳�IllegalMonitorStateException�쳣��
 * 
 * ���ǣ�����ô���ܣ��ȴ��߳���ͬ��������ִ�е�ʱ�򣬲���һֱ���м���������myMonitor���󣩵����𣿵ȴ��̲߳������������߳̽���doNotify()��ͬ������
 * ���ǣ���ȷ����һֱ���С�һ���̵߳�����wait()�����������ͷ��������еļ����������ϵ������⽫���������߳�Ҳ���Ե���wait()����notify()��
 * 
 * һ��һ���̱߳����ѣ��������̾��˳�wait()�ķ������ã�ֱ������notify()���߳��˳������Լ���ͬ���顣
 * ���仰˵�������ѵ��̱߳������»�ü���������������ſ����˳�wait()�ķ������ã���Ϊwait��������������ͬ�������档
 * �������̱߳�notifyAll()���ѣ���ô��ͬһʱ�̽�ֻ��һ���߳̿����˳�wait()��������Ϊÿ���߳����˳�wait()ǰ�����ü��������������
 * (��ԭwait�߳����������»�����Ա��˳�wait����...)
 */
public class MyWaitNotify {
	MonitorObject myMonitorObject = new MonitorObject();
	
	public void doWait() {
		synchronized(myMonitorObject) {
			try {
				myMonitorObject.wait();
			} catch(InterruptedException e) {
				System.out.println("Interrupted Exception");
			}
		}
	}
	
	public void doNotify() {
		synchronized(myMonitorObject) {
			myMonitorObject.notify();
		}
	}
}

class MonitorObject {
	
}