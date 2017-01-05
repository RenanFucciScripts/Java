package procImg;

public class Segmenta��o extends MetodosProcImagens {
	
	public static void main(String[] args) {
		int matriz[][] = leImagem("C:/Users/Re/Dropbox/workspace/JavaX/imagem/img5.bmp", "Red");
		int limiar[]={190};
		contarCorCinzas(matriz);
		matriz=segmenta��oPorLimiariza��o(matriz, limiar);
		gravarImagem(matriz, "segmenta��o");
	}

	public static int[][] segmenta��oPorLimiariza��o(int matriz[][],int limiar[] ){
		int matrizAux[][] = new int[matriz.length][matriz.length];

		for (int i=0; i<matriz.length; i++){
			for(int j=0; j<matriz[i].length;j++){
				matrizAux[i][j]=matriz[i][j];

			}
		}

		for (int i=0; i<matrizAux.length; i++){
			for(int j=0; j<matrizAux[i].length;j++){
				for(int k=0;k<limiar.length;k++){
					if(k==0){
						if(matrizAux[i][j]<limiar[k] ){
							//System.out.println("=0");
							
							matrizAux[i][j]=0;
							break;
						}
						else{
							matrizAux[i][j]=255;
							break;
						}
					}
					else if(k==limiar.length-1 ){
						if(matrizAux[i][j]>=limiar[k]){
							//System.out.println(">ultimo");
							matrizAux[i][j]=255;
							
							break;
						}
					}

					else{
						if(matrizAux[i][j]>=limiar[k-1] && matrizAux[i][j]<limiar[k]){
							System.out.println("=limiar");
							matrizAux[i][j]=limiar[k];
							break;
						}
					}


				}
			}
		}
		return matrizAux;
	}

}
