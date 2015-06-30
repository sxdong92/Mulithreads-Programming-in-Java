package threads;

public class MyRunnable implements Runnable {

	@Override
	public void run() {
		System.out.println("MyRunnable running");
	}
	
	public static void main(String[] args) {
		// Ϊ��ʹ�߳��ܹ�ִ��run()��������Ҫ��Thread��Ĺ��캯���д��� MyRunnable��ʵ������
		Thread thread = new Thread(new MyRunnable());
		thread.start();
		
		// ͬ����Ҳ���Դ���һ��ʵ����Runnable�ӿڵ�������
		Runnable myRunnable = new Runnable() {
			public void run() {
				System.out.println("MyRunnable running 2");
			}
		};
		Thread thread2 = new Thread(myRunnable);
		thread2.start();
	}
}
