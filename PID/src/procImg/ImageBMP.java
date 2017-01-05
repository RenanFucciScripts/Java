package procImg;

public class ImageBMP extends MetodosProcImagens {
	public static void main(String[] args) {
		int array[][] = leImagem(
				"C:/Users/Re/Dropbox/workspace/JavaX/imagem/img4.bmp", "Red");
		contarCorCinzas(array);
		int conector = 128;
		int c4 = contarObjetos4(array, array.length, array.length, conector);
		int c8 = contarObjetos8(array, array.length, array.length, conector);
		System.out.println("Existem " + c4
				+ " objetos conectividade 4 e conector " + conector);
		System.out.println("Existem " + c8
				+ " objetos conectividade 8 e conector " + conector);
	}
}
