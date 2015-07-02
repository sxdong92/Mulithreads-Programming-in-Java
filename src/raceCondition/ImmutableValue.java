package raceCondition;

/**
 * 我们可以通过创建不可变的共享对象来保证对象在线程间共享时不会被修改，从而实现线程安全。
 * 
 * 请注意ImmutableValue类的成员变量value是通过构造函数赋值的，并且在类中没有set方法。
 * 这意味着一旦ImmutableValue实例被创建，value变量就不能再被修改，这就是不可变性。
 * 但你可以通过getValue()方法读取这个变量的值。
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
 * 重要的是要记住，即使一个对象是线程安全的不可变对象，指向这个对象的引用也可能不是线程安全的。
 *
 * Calculator类持有一个指向ImmutableValue实例的引用。注意，通过setValue()方法和add()方法可能会改变这个引用。
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
