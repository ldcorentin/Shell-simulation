import jmetal.util.*;
import java.util.*;

public class TestRand {

	public static void main(String[] args) {
	
		ArrayList<Integer> list1 = new ArrayList<Integer>();

		MersenneTwisterFast mt = new MersenneTwisterFast();
		for(int i=0; i<15; i++){
				list1.add(Math.abs(mersenneTwisterFast.nextInt())%1000);
		}
		
		for(Integer it : list1) {
			System.out.println(it);
		}
		
	}
}
