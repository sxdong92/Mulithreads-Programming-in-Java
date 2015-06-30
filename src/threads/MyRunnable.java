package threads;

public class MyRunnable implements Runnable {

	@Override
	public void run() {
		System.out.println("MyRunnable running");
	}
	
	public static void main(String[] args) {
		// 为了使线程能够执行run()方法，需要在Thread类的构造函数中传入 MyRunnable的实例对象
		Thread thread = new Thread(new MyRunnable());
		thread.start();
		
		// 同样，也可以创建一个实现了Runnable接口的匿名类
		Runnable myRunnable = new Runnable() {
			public void run() {
				System.out.println("MyRunnable running 2");
			}
		};
		Thread thread2 = new Thread(myRunnable);
		thread2.start();
	}
}
