package teste;



public class contarDi {

	public static void main(String[] args) {
		
	int[][] solução={{1,2},
					 {3,0}};
	
	int[][] no={{1,0},
		     	 {2,3}};


	int[][] tmp = new int[no.length][no.length];
		int cont=0;
		/*System.out.println("Nó estado - "+(no.getEtiqueta()));
		for (int i = 0; i < tmp.length; i++) {
			for (int j = 0; j < tmp.length; j++) {
				System.out.print(no.estado[i][j]+"");
			}
			System.out.println("");
		}
		
		System.out.println("No Solução");
		for (int i = 0; i < tmp.length; i++) {
			for (int j = 0; j < tmp.length; j++) {
				System.out.print(this.solução[i][j]+"");
			}
			System.out.println("");
		}
		*/
		//System.out.println("No diferença");
		for (int i = 0; i < tmp.length; i++) {
			for (int j = 0; j < tmp.length; j++) {
				//System.out.print((tmp[i][j]=Math.abs(this.solução[i][j]-no.estado[i][j]))+"");
				tmp[i][j]=Math.abs(solução[i][j]-no[i][j]);
				if(tmp[i][j]!=0){
					cont+=1;
				}
			}
			System.out.println("");
		}
		
	/*	System.out.println("\n CONTADOR:"+cont);
		System.out.println("\n");
	*/	System.out.println(cont);	
	}
}
