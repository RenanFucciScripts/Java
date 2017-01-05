package faixaScanner;

import metodos.MetodosEM;

public class LBP {

	public static void main(String[] args) {
		int[][] img={{157,178,220},
					 {219,218,255},
					 {215,219,255}};
		int[][] janela= new int[3][3];
		int[][] imgPixel =  new int[janela.length][janela.length];

		MetodosEM meEM =  new MetodosEM();
		
		for (int i = (int) Math.floor(janela.length/2); i < janela.length - (int) Math.floor(janela.length/2); i++) {
			for (int j = (int) Math.floor(janela.length/2); j < janela[i].length -(int) Math.floor(janela.length/2); j++) {
				imgPixel =  new int[janela.length][janela.length];
				for (int k = - (int) Math.floor(janela.length/2); k <= (int) Math.floor(janela.length/2) ; k++) {
					for (int l = - (int) Math.floor(janela.length/2); l <= (int) Math.floor(janela.length/2); l++) {
						System.out.println(k+", "+l);
						imgPixel[k+(int) Math.floor(janela.length/2)][l+(int) Math.floor(janela.length/2)]=img[i+k][j+l];
					}	
				}
				getLBPVizinhos(imgPixel);
			}
		}
		
		//imprimirMatrizDouble(imgPixel);
		//	getLBPVizinhos(img);
	}


	public static int getLBPVizinhos(int[][] img) {
		// TODO Auto-generated constructor stub

		int[][] janela= new int[3][3];
		String binary ="";
		int cent = (int) Math.floor(janela.length/2);
		int centro =  img[cent][cent];
		
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < janela.length; j++) {
				if(centro>img[i][j])
					binary+=0;
				else
					binary+=1;
			}
		}
		
		for (int i = 1; i <janela.length ; i++) {
			for (int j = janela.length-1; j <janela.length ; j++) {
				if(centro>img[i][j])
					binary+=0;
				else
					binary+=1;
			}
		}
		
		for (int i =janela.length-1 ; i < janela.length; i++) {
			for (int j = janela.length-2; j >= 0; j--) {
				if(centro>img[i][j])
					binary+=0;
				else
					binary+=1;
			}
		}
		for (int i = janela.length-2 ; i > 0; i--) {
			for (int j = 0; j >= 0; j--) {
				if(centro>img[i][j])
					binary+=0;
				else
					binary+=1;
			}
		}
		return Integer.parseInt(binary, 2);
	}

	/*
	 * Método para imprimir uma matriz de valores com ponto flutuante
	 */
	public static void  imprimirMatrizDouble(int array[][]){

		//Estrutura de repetição para percorrer a linha da matriz
		for (int i=0; i<array.length; i++) {
			//Estrutura de repetição para percorrer a coluna da matriz
			for (int j=0; j<array[i].length; j++) {
				//Imprime o valor da posição da matriz
				System.out.printf(" %d ",array[i][j]);
			}
			//Pula uma linha da hora da impressão ao fim da immperssão da coluna 
			System.out.printf("\n");
		}
		//Pula uma linha ao fim da impressão da matriz
		System.out.printf("\n");

	}
}
