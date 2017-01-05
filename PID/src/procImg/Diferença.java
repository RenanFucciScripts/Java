package procImg;



public class Diferença extends MetodosProcImagens {
	public static void main(String[] args) {

		int[][] matriz1=leImagem("C:/Users/Re/Dropbox/workspace/JavaXx/imagens/foto1.bmp", "Red");
		int[][] matriz2=leImagem("C:/Users/Re/Dropbox/workspace/JavaXx/imagens/foto2.bmp", "Red");
		int[][] matriz;
		
		matriz=diferença(matriz1, matriz2);
		
		
		int[] x={120};
		matriz=segmentaçãoPorLimiarização(matriz, x);
		contarCorCinzas(matriz);
		
		
		
		gravarImagem(matriz, "fotoResultado");
	}
}
