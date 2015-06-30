package threads;

public class MyThread extends Thread {
	public void run() {
		System.out.println("MyThread running");
	}
	
	public static void main(String[] args) {
		// 创建Thread子类的实例来创建线程
		MyThread myThread = new MyThread();
		myThread.start();
		
		// 也可以创建匿名子类
		Thread myThread2 = new Thread() {
		    public void run() {		    	
		        System.out.println("Thread Running 2");
		    }
		};
		myThread2.start();
		
		// 小例子: JVM和操作系统一起决定了线程的执行顺序，他和线程的启动顺序并非一定是一致的。 
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