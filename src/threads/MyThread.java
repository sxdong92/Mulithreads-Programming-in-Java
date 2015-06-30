package threads;

public class MyThread extends Thread {
	public void run() {
		System.out.println("MyThread running");
	}
	
	public static void main(String[] args) {
		// ����Thread�����ʵ���������߳�
		MyThread myThread = new MyThread();
		myThread.start();
		
		// Ҳ���Դ�����������
		Thread myThread2 = new Thread() {
		    public void run() {		    	
		        System.out.println("Thread Running 2");
		    }
		};
		myThread2.start();
		
		// С����: JVM�Ͳ���ϵͳһ��������̵߳�ִ��˳�������̵߳�����˳�򲢷�һ����һ�µġ� 
		System.out.println(Thread.currentThread().getName());
		for(int i = 0; i < 10; i++) {
			new Thread("" + i) {
				public void run() {
					System.out.println("Thread: " + getName() + "running");
				}
			}.start();
		}
	}
}