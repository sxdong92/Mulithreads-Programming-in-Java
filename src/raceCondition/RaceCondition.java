package raceCondition;
/**
 * @NotThreadSafe
 *
 */
public class RaceCondition {
	// 类变量，不安全，可被多个线程共享。
	protected long count = 0;
	
	public void add(long value) {
		this.count = this.count + value;
	}
	
	public void someMethod() {
		// 局部变量， 线程安全。
		long threadSafeInt = 0;
		System.out.print(++threadSafeInt);
	}
	
	/**
	 * 对象的局部引用和基础类型的局部变量不太一样。尽管引用本身没有被共享，但引用所指的对象并没有存储在线程的栈内。所有的对象都存在共享堆中。
	 * 如果在某个方法中创建的对象不会逃逸出（译者注：即该对象不会被其它方法获得，也不会被非局部变量引用到）该方法，那么它就是线程安全的。
	 * 
	 * 实际上，哪怕将这个对象作为参数传给其它方法，只要别的线程获取不到这个对象，那它仍是线程安全的。
	 * 当然，如果obj通过某些方法被传给了别的线程，那它就不再是线程安全的了。 
	 */
	public void someMethod2() {
		RaceCondition obj = new RaceCondition(); //ThreadSafe，因为obj不会逸出
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
