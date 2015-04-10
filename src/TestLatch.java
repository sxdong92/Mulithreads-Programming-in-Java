import java.util.concurrent.CountDownLatch;


public class TestLatch {

	private static class Single extends Thread {
		private int id;
		
		public Single(int id) {
			this.id = id;
		}
		
		public void run() {
//			for(int i=0; i<3; i++) {
//				System.out.println("NO-" + id + " : " + i + "haha!" );
//			}
			System.out.println("NO-" + id + " : haha!" );
		}
	}
	
	private static class SingleWithLatch extends Thread {
		private int id;
		private CountDownLatch startGate;
		
		public SingleWithLatch(int id, CountDownLatch startGate) {
			this.id = id;
			this.startGate = startGate;
		}
		
		public void run() {
			try {
				startGate.await();
//				for(int i=0; i<3; i++) {
//					System.out.println("NO-" + id + " : " + i + "haha!" );
//				}
				System.out.println("NO-" + id + " : haha!" );
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void startThreads() {
		for(int i=0; i<20; i++) {
			Single s = new Single(i);
			s.start();
		}
	}
	
	public void startThreadsWithLatch() {
		final CountDownLatch startGate = new CountDownLatch(1);
		for(int i=0; i<20; i++) {
			SingleWithLatch s = new SingleWithLatch(i, startGate);
			s.start();
		}
		startGate.countDown();
	}
	
	public static void main(String[] args) {
		TestLatch t = new TestLatch();
		
		System.out.println("----------Do without latch----------");
		t.startThreads();
		
		try {
			Thread.sleep(2000);
			System.out.println("");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("----------Do with start latch----------");
		t.startThreadsWithLatch();
	}
	
}
