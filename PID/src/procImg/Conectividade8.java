package procImg;

public class Conectividade8 extends MetodosProcImagens {
	public static void main(String[] args) {
		int[][] array = { { 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 1, 1, 1, 1, 1, 1, 0 }, { 0, 1, 1, 0, 1, 1, 1, 0 },
				{ 0, 1, 1, 1, 0, 0, 1, 0 }, { 0, 1, 1, 0, 0, 1, 1, 0 },
				{ 0, 1, 0, 1, 0, 0, 1, 0 }, { 0, 1, 1, 1, 1, 1, 1, 0 },
				{ 0, 0, 0, 0, 0, 0, 0, 0 } };

		// função contar
		imprimirMatrizInt(array);
		int result = contarObjetos8(array, 8, 8, 0);
		System.out.println("Existem " + result + " objetos conectividade 8.");

	}

}
