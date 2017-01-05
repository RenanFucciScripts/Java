package procImg;

public class LenaCor extends MetodosProcImagens {
	public static void main(String[] args) {

		/*
		 * double x=1.0/25.0; double [][] elem={{x,x,x,x,x}, {x,x,x,x,x},
		 * {x,x,x,x,x}, {x,x,x,x,x}, {x,x,x,x,x}};
		 */

		int matrizRed[][] = leImagem(
				"C:/Users/Re/Dropbox/workspace/JavaX/imagem/img11.bmp", "Red");

		matrizRed = convoluçãoMediana(matrizRed, 3, 1);
		gravarImagem(matrizRed, "img12");

	}
}
