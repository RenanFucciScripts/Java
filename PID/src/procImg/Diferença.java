package procImg;



public class Diferen�a extends MetodosProcImagens {
	public static void main(String[] args) {

		int[][] matriz1=leImagem("C:/Users/Re/Dropbox/workspace/JavaXx/imagens/foto1.bmp", "Red");
		int[][] matriz2=leImagem("C:/Users/Re/Dropbox/workspace/JavaXx/imagens/foto2.bmp", "Red");
		int[][] matriz;
		
		matriz=diferen�a(matriz1, matriz2);
		
		
		int[] x={120};
		matriz=segmenta��oPorLimiariza��o(matriz, x);
		contarCorCinzas(matriz);
		
		
		
		gravarImagem(matriz, "fotoResultado");
	}
}
