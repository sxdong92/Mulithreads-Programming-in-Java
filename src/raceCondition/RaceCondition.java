package raceCondition;
/**
 * @NotThreadSafe
 *
 */
public class RaceCondition {
	// �����������ȫ���ɱ�����̹߳���
	protected long count = 0;
	
	public void add(long value) {
		this.count = this.count + value;
	}
	
	public void someMethod() {
		// �ֲ������� �̰߳�ȫ��
		long threadSafeInt = 0;
		System.out.print(++threadSafeInt);
	}
	
	/**
	 * ����ľֲ����úͻ������͵ľֲ�������̫һ�����������ñ���û�б�������������ָ�Ķ���û�д洢���̵߳�ջ�ڡ����еĶ��󶼴��ڹ�����С�
	 * �����ĳ�������д����Ķ��󲻻����ݳ�������ע�����ö��󲻻ᱻ����������ã�Ҳ���ᱻ�Ǿֲ��������õ����÷�������ô�������̰߳�ȫ�ġ�
	 * 
	 * ʵ���ϣ����½����������Ϊ������������������ֻҪ����̻߳�ȡ��������������������̰߳�ȫ�ġ�
	 * ��Ȼ�����objͨ��ĳЩ�����������˱���̣߳������Ͳ������̰߳�ȫ���ˡ� 
	 */
	public void someMethod2() {
		RaceCondition obj = new RaceCondition(); //ThreadSafe����Ϊobj�����ݳ�
		someMethod3(obj);
	}
	
	public void someMethod3(RaceCondition obj) {
		obj.someMethod();
	}
	
	StringBuilder builder = new StringBuilder(); //NotThreadSafe
	
	public void notSafeAdd(String text) {
		builder.append(text);
	}
}
