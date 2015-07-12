package synchronizer;

import java.util.LinkedList;
import java.util.Queue;

/**
 * ����ע�⵽����enqueue��dequeue�����ڲ���ֻ�ж��еĴ�С�������ޣ�limit���������ޣ�0��ʱ���ŵ���notifyAll������
 * ������еĴ�С�Ȳ��������ޣ�Ҳ���������ޣ��κ��̵߳���enqueue����dequeue����ʱ�����������������ܹ�����������������ӻ����Ƴ�Ԫ�ء� 
 */
public class BlockingQueue {
	private Queue queue = new LinkedList();
	private int  limit = 10;
	
	public BlockingQueue(int limit) {
		this.limit = limit;
	}
	
	public synchronized void enqueue(Object item) throws InterruptedException {
		while(this.queue.size() == this.limit) {
			wait();
		}
		if(this.queue.size() == 0) {
			notifyAll();
		}
		this.queue.add(item);
	}
	
	public synchronized Object dequeue() throws InterruptedException {
		while(this.queue.size() == 0) {
			wait();
		}
		if(this.queue.size() == this.limit) {
			notifyAll();
		}
		return this.queue.poll();
	}
}
