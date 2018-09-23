public class Counter {

	protected int count;

	public Counter() {
		this.count=0;
	}

	public void incr() {
		this.count++;
	}

	public void decr() {
		this.count--;
	}

	public int getValue() {
		return count;
	}

}
