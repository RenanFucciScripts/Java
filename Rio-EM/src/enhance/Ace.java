package enhance;

public class Ace {
	
	final int DEGREE = 5;
	
	public static double Factorial(int n){
		long Table[] =  {1, 1, 2, 6, 24, 120, 720, 5040, 40320,362880, 3628800, 39916800, 479001600 };
	
		if(n<0)
			return 0;
		else if(n<15)
			return Table[n];
		else
			return n * Factorial(n-1);
		
	}
}
