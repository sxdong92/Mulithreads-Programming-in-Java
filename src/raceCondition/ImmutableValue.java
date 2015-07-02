package raceCondition;

/**
 * ���ǿ���ͨ���������ɱ�Ĺ����������֤�������̼߳乲��ʱ���ᱻ�޸ģ��Ӷ�ʵ���̰߳�ȫ��
 * 
 * ��ע��ImmutableValue��ĳ�Ա����value��ͨ�����캯����ֵ�ģ�����������û��set������
 * ����ζ��һ��ImmutableValueʵ����������value�����Ͳ����ٱ��޸ģ�����ǲ��ɱ��ԡ�
 * �������ͨ��getValue()������ȡ���������ֵ��
 */
public class ImmutableValue {
	private int value = 0;
	
	public ImmutableValue(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public ImmutableValue add(int valueToAdd) {
		return new ImmutableValue(this.value + valueToAdd);
	}
}

/**
 * ��Ҫ����Ҫ��ס����ʹһ���������̰߳�ȫ�Ĳ��ɱ����ָ��������������Ҳ���ܲ����̰߳�ȫ�ġ�
 *
 * Calculator�����һ��ָ��ImmutableValueʵ�������á�ע�⣬ͨ��setValue()������add()�������ܻ�ı�������á�
 */
class Calculator {
	private ImmutableValue currentValue = null;
	
	public ImmutableValue getValue() {
		return this.currentValue;
	}
	
	public void setValue(ImmutableValue newValue) {
		this.currentValue = newValue;
	}
	
	public void add(int newValue) {
		this.currentValue = this.currentValue.add(newValue);
	}
}
