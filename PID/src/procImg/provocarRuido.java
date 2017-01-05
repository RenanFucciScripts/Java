package procImg;

public class provocarRuido extends MetodosProcImagens{

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		int matriz[][];

		double x=1.0/5.0;
		double [][] elem={{x,0,0,0,0},
						  {0,0,x,0,0},
						  {0,0,0,0,x},
						  {0,x,0,0,0},
						  {0,0,0,x,0}};
		
		double y=1.0/9.0;
		double [][] elem1={{y,y,y,y,y},
						   {y,y,y,y,y},
						   {y,y,y,y,y},
						   {y,y,y,y,y},
						   {y,y,y,y,y}};
		
		
		matriz=leImagem("C:/Users/Re/Dropbox/workspace/JavaXx/imagens/lena.bmp", "Red");
		
		
		matriz=ruido(matriz);
		gravarImagem(matriz, "img12ruido");
		matriz=convolução(matriz, elem1);
		gravarImagem(matriz, "img12ruidoarrumado");
		
	}
	

}
