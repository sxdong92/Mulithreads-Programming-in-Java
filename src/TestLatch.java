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
		private CountDownLatch closeGate;
		
		public SingleWithLatch(int id, CountDownLatch startGate, CountDownLatch closeGate) {
			this.id = id;
			this.startGate = startGate;
			this.closeGate = closeGate;
		}
		
		public void run() {
			try {
				startGate.await();
//				for(int i=0; i<3; i++) {
//					System.out.println("NO-" + id + " : " + i + "haha!" );
//				}
				System.out.println("NO-" + id + " : haha!" );
				closeGate.countDown();
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
		System.out.println("finished!");
	}
	
	public void startThreadsWithLatch() {
		final CountDownLatch startGate = new CountDownLatch(1);
		final CountDownLatch closeGate = new CountDownLatch(20);
		for(int i=0; i<20; i++) {
			SingleWithLatch s = new SingleWithLatch(i, startGate, closeGate);
			s.start();
		}
		startGate.countDown();
		try {
			closeGate.await();
			System.out.println("finished!");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
