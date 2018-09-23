public class Combination {

	protected String[] possibilities;

	public Combination(int size, int n, int tuple) {
		Counter counter = new Counter();
		possibilities=new String[size];
		couple(n,0,0,tuple,null,counter);
	}

	public void couple (int nbElement, int cur, int loop, int nTuple, String prefixe, Counter counter) {
		String str;
		for (int i=cur; i<nbElement; i++) {
		str="";
			if (prefixe==null) {
				str+= i;
			}
			else {
				str = prefixe +" "+ i;
			}
			if (loop<nTuple-1) {
				couple(nbElement, i+1, loop+1, nTuple, str, counter);
			} else {
				possibilities[counter.getValue()]=str;
				counter.incr();
			}
		}
	}
}
