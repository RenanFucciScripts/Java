package procImg;

public class PintarUFMSpreto extends MetodosProcImagens {

	@SuppressWarnings("unused")
	public static void main(String[] args) {

		int arrayR[][]= leImagem("C:/Users/Re/Desktop/logoufmsjpg.jpg" ,"Red");
		int arrayG[][]= leImagem("C:/Users/Re/Desktop/logoufmsjpg.jpg" ,"Green");
		int arrayB[][]= leImagem("C:/Users/Re/Desktop/logoufmsjpg.jpg" ,"Blue");

		contarCorCinzas(arrayB);
		int[] x={230};
		arrayB=segmentaçãoPorLimiarização(arrayB, x);
		arrayB=convoluçãoMediana(arrayB, 3, 1);
		contarCorCinzas(arrayB);
		
		gravarImagem(arrayB, "LogoUFMSSegm");
		
		/*arrayR=pintarPreto(arrayR);
		arrayG=pintarPreto(arrayG);
		arrayB=pintarPreto(arrayB);
		
		gravarImagemColorida(arrayR, arrayG, arrayB, "logoUfmsPretoeBranco");*/
		
	}

	public static int[][] pintarPreto(int[][] array){
		
		int[][] arrayResult= new int[array.length][array[0].length];
		for (int i = 0; i < arrayResult.length; i++) {
			for (int j = 0; j < arrayResult[i].length; j++) {
				if(array[i][j]!=255){
					arrayResult[i][j]=0;
				}
				else{
					arrayResult[i][j]=array[i][j];
				}
			}
		}
		
		return arrayResult;
	}
}
