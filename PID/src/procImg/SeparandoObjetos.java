package procImg;

public class SeparandoObjetos extends MetodosProcImagens {
	//http://www.alexaraujo.com.pt/ufms/pdi/img2.bmp
	public static void main(String[] args) {
		int matriz[][]= leImagem("C:/Users/Re/Dropbox/workspace/JavaX/imagem/img10.bmp", "Red");
		int[] limiar={10};
		int[][] matrizSegmentada=segmenta��oPorLimiariza��o(matriz, limiar);
		contarCorCinzas(matrizSegmentada);
		gravarImagem(matrizSegmentada, "hu");
		/*
		 * Pegar pontos que n�o sejam o fundo
		 * */
		separarObjetos(matrizSegmentada, matrizSegmentada.length, matrizSegmentada.length,127);
		
	}
}
