package teste;



public class contarDi {

	public static void main(String[] args) {
		
	int[][] solu��o={{1,2},
					 {3,0}};
	
	int[][] no={{1,0},
		     	 {2,3}};


	int[][] tmp = new int[no.length][no.length];
		int cont=0;
		/*System.out.println("N� estado - "+(no.getEtiqueta()));
		for (int i = 0; i < tmp.length; i++) {
			for (int j = 0; j < tmp.length; j++) {
				System.out.print(no.estado[i][j]+"");
			}
			System.out.println("");
		}
		
		System.out.println("No Solu��o");
		for (int i = 0; i < tmp.length; i++) {
			for (int j = 0; j < tmp.length; j++) {
				System.out.print(this.solu��o[i][j]+"");
			}
			System.out.println("");
		}
		*/
		//System.out.println("No diferen�a");
		for (int i = 0; i < tmp.length; i++) {
			for (int j = 0; j < tmp.length; j++) {
				//System.out.print((tmp[i][j]=Math.abs(this.solu��o[i][j]-no.estado[i][j]))+"");
				tmp[i][j]=Math.abs(solu��o[i][j]-no[i][j]);
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
