package raceCondition;
/**
 * @NotThreadSafe
 *
 */
public class RaceCondition {
	protected long count = 0;
	
	public void add(long value) {
		this.count = this.count + value;
	}
}
