package procImg;


public class Histograma extends MetodosProcImagens{
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		int matriz[][];
		int cores[];
		int novaCor[];
		
		matriz=leImagem("C:/Users/Re/Dropbox/workspace/JavaXx/imagens/img12.bmp", "Red");
		contarCorCinzas(matriz);
	//	mostrarGrafico(cores, "Original");
		novaCor=equalizar(matriz);
		mostrarGrafico(novaCor,"Final");
		
		
	
	}
	
	
	
}


