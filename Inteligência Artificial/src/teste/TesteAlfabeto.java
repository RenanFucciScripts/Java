package teste;

import java.util.LinkedList;
import java.util.Queue;

public class TesteAlfabeto {
	public static void main(String[] args) {
		String[] et={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		Queue<String> etiquetas= new LinkedList<String>();
		
		for (String x: et) {
			etiquetas.add(x);
		}
		
		for (String x: et) {
			String y= (x+""+x);
			etiquetas.add(y);
		} 
		for (String x: et) {
			String z= (x+""+x+""+x);
			etiquetas.add(z);
		} 
		
		
		System.out.println(etiquetas);
		String y =etiquetas.poll();
		System.out.println(y);
		System.out.println(etiquetas);
	}
}
