import java.util.*;

public class MainTest {

	public static void main(String[] args) {
		//initialization paramaters
		int k=14;
		int m=7;
		int Nmax = (int)(binomial((long)(k+m),(long)(k)));
		
		System.out.println(Nmax+"\n");
		
		Key resKey;
		int counterNode=0, pointer, numKey;
		String combinationString;
		String[] cchar = new String[k];
		Combination combination = new Combination(Nmax,k+m,k);
		int[] resCombinationInt = new int[Nmax+1];
					
		//test if the combinations are good
		System.out.println();
		for(int i=0; i<10; i++) {
			System.out.println(combination.possibilities[i]);
		}
	}
	
	public static long binomial(long n, long p)
	{
		/* Si p>n ---> 0, cas particuliers */
		if((p < 0) || (n < 0))
		{
			System.out.println("");
			return -1;
		}

		else if(p > n)
		{
			System.out.println("");
			return -1;
		}
		else if((p == 0) || (p == n))
		{
			return 1 ;
		}
		else
			/* mise en place de la récursivité */
			return binomial (n-1, p-1) + binomial (n-1, p) ;
	}
	
}			
