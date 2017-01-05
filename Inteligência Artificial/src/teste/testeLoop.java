package teste;

public class testeLoop {

	public static void main(String[] args) {
		
		int[][] estado={{1,1,2},
						{5,0,3},
						{4,7,8}};
		
		
		System.out.println(contarLoop(estado));
	}
	
	public static int contarLoop(int[][] estado){
		for (int i = 0; i < estado.length; i++) {
			for (int j = 0; j < estado[i].length; j++) {
				if(((i==0 && j==0) || (i==0 && j==2) || (i==2 && j==0) || (i==2 && j==2)) && estado[i][j]==0 ){
					return 2;
				}
				else if(((i==0 && j==1) || (i==1 && j==0) || (i==1 && j==2) || (i==2 && j==1)) && estado[i][j]==0 ){
					return 3;
				}
				else if((i==1 && j==1) && estado[i][j]==0 ){
					return 4;
				}
			}
		}
		return 0;
	}
	
}
