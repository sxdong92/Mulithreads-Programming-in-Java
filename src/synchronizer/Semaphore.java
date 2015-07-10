package synchronizer;

/**
 * Take��������һ���������Semaphore�ڲ����źţ���Release������ȴ�һ���źţ�������յ��źź󣬱��λsignal����գ�Ȼ��÷�����ֹ��
 * 
 * ʹ�����semaphore���Ա����ʧĳЩ�ź�֪ͨ����take����������notify��release����������wait��
 * ���ĳ�߳��ڵ���release�ȴ�֮ǰ����take��������ô����release�������߳���Ȼ֪��take�����Ѿ���ĳ���̵߳��ù��ˣ���Ϊ��Semaphore�ڲ�������take�����������źš�
 * ��wait��notify������û�������Ĺ��ܡ�
 * 
 * ����semaphore�������ź�ʱ��take��release�������������������е���֡�
 * ������������Դ�ں����semaphore�����������ӣ��������ϸ����������ӣ��ڸ������У�take��release���������ֻ��úܺ���
 */
public class Semaphore {
	private boolean signal = false;
	
	public synchronized void take() {
		this.signal = true;
		this.notify();
	}
	
	public synchronized void release() throws InterruptedException {
		while(!this.signal) {
			wait();
		}
		this.signal = false;
	}
	
	public static void main(String[] args) {
		Semaphore semaphore = new Semaphore();
		SendingThread sender = new SendingThread(semaphore);
		ReceivingThread receiver = new ReceivingThread(semaphore);
		receiver.start();
		sender.start();
	}
}

class SendingThread extends Thread {
	Semaphore semaphore = null;
	
	public SendingThread(Semaphore semaphore) {
		this.semaphore = semaphore;
	}
	
	public void run() {
		while(true) {
			//do something, then signal
			try {
				System.out.println("wait to send signal");
				sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.semaphore.take();
		}
	}
}

class ReceivingThread extends Thread {
	Semaphore semaphore = null;
	
	public ReceivingThread(Semaphore semaphore) {
		this.semaphore = semaphore;
	}
	
	public void run() {
		while(true) {
			try {
				this.semaphore.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//receive signal, then do something...
			System.out.println("receive signal then do something");
		}
	}
}
