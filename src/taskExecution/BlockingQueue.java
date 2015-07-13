package taskExecution;

import java.util.LinkedList;
import java.util.Queue;

/**
 * ����ע�⵽����enqueue��dequeue�����ڲ���ֻ�ж��еĴ�С�������ޣ�limit���������ޣ�0��ʱ���ŵ���notifyAll������
 * ������еĴ�С�Ȳ��������ޣ�Ҳ���������ޣ��κ��̵߳���enqueue����dequeue����ʱ�����������������ܹ�����������������ӻ����Ƴ�Ԫ�ء� 
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
