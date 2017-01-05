package procImg;

public class Lena extends MetodosProcImagens {
	public static void main(String[] args) {
		/*
		 * double x=1.0/25.0; double [][] elementoEstruturante={{x,x,x,x,x},
		 * {x,x,x,x,x}, {x,x,x,x,x}, {x,x,x,x,x}, {x,x,x,x,x}};
		 */

		double x = 1.0 / 9.0;
		double[][] elementoEstruturante = { { x, x, x }, { x, x, x },
				{ x, x, x } };

		int matriz[][] = leImagem(
				"C:/Users/Re/Dropbox/workspace/JavaX/imagem/img7.bmp", "Red");
		// matriz=convoluçãoMediana(matriz, 3, 1);
		matriz = convolução(matriz, elementoEstruturante);
		gravarImagem(matriz, "img7Media3x3");
	}
}
