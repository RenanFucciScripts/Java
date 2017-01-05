package procImg;

public class Conectividade4 extends MetodosProcImagens {
	public static void main(String[] args) {
		int[][] array =   { { 0, 0, 0, 0, 0, 0, 0, 0 },
							{ 0, 1, 1, 1, 1, 1, 1, 0 },
							{ 0, 1, 1, 0, 1, 1, 1, 0 },
							{ 0, 1, 1, 1, 0, 0, 1, 0 }, 
							{ 0, 1, 1, 0, 0, 1, 1, 0 },
							{ 0, 1, 0, 1, 0, 0, 1, 0 }, 
							{ 0, 1, 1, 1, 1, 1, 1, 0 },
							{ 0, 0, 0, 0, 0, 0, 0, 0 } };

		// função contar
		int result = contarObjetos4(array, 8, 8, 0);
		imprimirMatrizInt(array);
		System.out.println("Existem " + result + " objetos conectividade 4.");

	}

}