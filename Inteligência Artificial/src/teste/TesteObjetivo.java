package teste;

public class TesteObjetivo {

	public static void main(String[] args) {
			System.out.println("Teste Objetivo");
		
			int[][] tmp = {{1,2},
						   {3,0}};
			
			int[][] tmp2 = {{1,3},
						    {2,0}};
		
			
			int cont=0;
			for (int i = 0; i < tmp.length; i++) {
				for (int j = 0; j < tmp.length; j++) {
					tmp[i][j]=Math.abs(tmp[i][j]-tmp2[i][j]);
				}
			}
			
			for (int i = 0; i < tmp.length; i++) {
				for (int j = 0; j < tmp.length; j++) {
					if(tmp[i][j]==0){
						cont+=1;
						}
				}
			}
			System.out.println("\n CONTADOR:"+cont);
			System.out.println("\n");
		//	if(cont==4){return true;}
			//else return false;
			
		
	}
}
