package procImg;

public class ZoomIn extends MetodosProcImagens {

	public static void main(String[] args) {
		int matrizRed[][] = leImagem(
				"C:/Users/Re/Dropbox/workspace/JavaXx/imagens/lenaCor.bmp",
				"Red");
		int matrizGreen[][] = leImagem(
				"C:/Users/Re/Dropbox/workspace/JavaXx/imagens/lenaCor.bmp",
				"Green");
		int matrizBlue[][] = leImagem(
				"C:/Users/Re/Dropbox/workspace/JavaXx/imagens/lenaCor.bmp",
				"Blue");
		int tamZoom = 6;

		matrizRed = zoom(matrizRed, tamZoom);
		matrizGreen = zoom(matrizGreen, tamZoom);
		matrizBlue = zoom(matrizBlue, tamZoom);

		gravarImagemColorida(matrizRed, matrizGreen, matrizBlue, "lenaCorZoom6");
	}
}
