package taskExecution;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 必须注意到，在enqueue和dequeue方法内部，只有队列的大小等于上限（limit）或者下限（0）时，才调用notifyAll方法。
 * 如果队列的大小既不等于上限，也不等于下限，任何线程调用enqueue或者dequeue方法时，都不会阻塞，都能够正常的往队列中添加或者移除元素。 
 * @param <E>
 */
public class BlockingQueue<E> {
	private Queue<E> queue = new LinkedList<E>();
	private int  limit = 10;
	
	public BlockingQueue(int limit) {
		this.limit = limit;
	}
	
	public synchronized void enqueue(E item) throws InterruptedException {
		while(this.queue.size() == this.limit) {
			wait();
		}
		if(this.queue.size() == 0) {
			notifyAll();
		}
		this.queue.add(item);
	}
	
	public synchronized E dequeue() throws InterruptedException {
		while(this.queue.size() == 0) {
			wait();
		}
		if(this.queue.size() == this.limit) {
			notifyAll();
		}
		return this.queue.poll();
	}
}
