package perceptronLinear;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ExecPercpetronL {

	/**
	 * <b> Método main(Principal)<\b>
	 * Wfinal[][](double) é o vetor de pesos já treinado
	 * */
	public static void main(String[] args) {
		try{
			double[][] wfinal={{-0.0941},{1.0574},{-0.0655}}; 

			double limiar=0.0;
			String caminho = "C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/dadosExecucao.txt";
			FileReader file = new FileReader(caminho);
			BufferedReader leitura= new BufferedReader(file);

			int[][] img= new int[2501][2501];
			

			String linha= leitura.readLine();
			int i;
			int j;
			double ndvi;
			double evi;
			String[] div;
			while(linha!=null){
				div= linha.split(",");
				i=Integer.parseInt(div[0]);
				j=Integer.parseInt(div[1]);
				ndvi=Double.parseDouble(div[2]);
				evi=Double.parseDouble(div[3]);
				//System.out.println("i:"+i+" j:"+j+" ndvi:"+ndvi+" evi:"+evi);
				double[][] m={{-1},{ndvi},{evi}};
				//System.out.println("U:"+funçãoU(m, wfinal, limiar));
				img[i][j]=funçãoU(m, wfinal, limiar);
				linha=leitura.readLine();
			}
		String dir= "C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens";
		String nomeArquivo= "classificacao";
		gravarImagem(img, dir, nomeArquivo);
		}catch(Exception ex){
			ex.getMessage();
		}

	}

	/**
	 * <b>Método execPerceptron</b>
	 * Esse método vai fazer a execução do Perceptron Linear após sua etapa de treinamento.
	 * Essse método utiliza-se do método <b>"funcaoU"<\b>.
	 * @param wfinal[][](double) é a transposta do vetor de pesos já treinado. 
	 * @param vetorExec[](double) é vetor de caracteristica que deve ser classificado.
	 * @param limiar(double) é a constante limiar, neste caso da função degrau.
	 * @return u(int) é classe correspondente ao vetor de caracteristica a ser classificado.
	 * */
	public static int execPerceptron(double[][] wfinal, double[] vetorExec, double limiar){
		double u= funçãoU(transpostaVetor(vetorExec), wfinal, limiar);
		return (int) u;
	}

	/**
	 * <b>Método funçãoU</b>
	 * Esse método vai calcular o f(U(i)), que é a somatória entre o a matriz de padrões e a de pesos.
	 * @param m[][](double) é a matriz de padrões
	 * @param w[][](double) é a matriz de pesos
	 * @param limiar(double) é a é a constante limiar, neste caso da função degrau.
	 * @return x(double)  é o resultado da somatoria dos padrões vezes os pesos, com base no limiar.
	 * */
	public static int funçãoU(double[][] m, double[][] w , double limiar){
		double x=0;
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[i].length; j++) {
				x += ((m[i][j])*(w[i][j]));
				//System.out.println("x:"+x);
			}
		}
		if(x>=limiar){
			return 255;
		}
		else{
			return 0;
		}
	}

	/**
	 * <b>Método transpostaVetor</b>
	 * Método para calcular a transposta de uma vetor.
	 * @param m[](double) vetor para cálculo da transposta.
	 * @return tmp[][](double) transposta do vetor.
	 * */

	public static double[][] transpostaVetor(double[] m){
		/**
		 * Como o parametro é um vetor,intancia a matriz com o i do tamanho do vetor
		 * */
		double[][] tmp= new double[m.length][1];

		for (int i = 0; i <tmp.length; i++) {
			tmp[i][0]=m[i];
		}
		return tmp;
	}

	/*
	 * Método que grava imagem em um diretório local a partir de uma matriz, no entanto só utiliza um dos 3 canais RGB,
	 *  ou seja, grava imagem em tons de cinza.
	 */
	public static void gravarImagem(int matriz[][], String dir, String nomeArquivo){
		//Tratamento de exceção 
		try{
			//Instancia uma imagem em um BufferImage
			BufferedImage imagem= new BufferedImage(matriz.length, matriz.length, 5);
			//Imprime que comelou a gravar a imagem
			System.out.println("entra gravar imagem");

			//Estrutura de repetição para percorrer a linha da matriz
			for (int i=0; i<matriz.length; i++){
				//Estrutura de repetição para percorrer a coluna da matriz
				for(int j=0; j<matriz.length;j++){
					//Seta no buffer os três canais RGB na sua respectiva posição da iteração
					imagem.setRGB(j, i, new Color(matriz[i][j],matriz[i][j],matriz[i][j]).getRGB());

				}
			}
			//Escreve a imagem em um diretório
			ImageIO.write(imagem, "BMP", new File(dir+"/"+nomeArquivo+".bmp"));
			//Imprime que a imagem terminou de ser gravada
			System.out.println("sai gravar imagem");
		}
		//Captura exceções
		catch(Exception ex){
			//Imprime a exceção
			ex.getMessage();
		}
	}


	/*
	 * Método que transforma um imagem colorida e transforma para uma imagem em tons de cinza
	 */
	public static int[][] RGBparaCinza(int matrizRed[][], int matrizGreen[][], int matrizBlue[][]){
		//Instancia matriz resultado
		int[][] matrizRes=new int[matrizRed.length][matrizRed.length];
		//Estrutura de repetição para percorrer a linha da matriz
		for (int i = 0; i < matrizRes.length; i++) {
			//Estrutura de repetição para percorrer a coluna da matriz
			for (int j = 0; j < matrizRes.length; j++) {
				//Matriz resultado recebe cálculo especifico de RBG para Tons de Cinza
				matrizRes[i][j]=((matrizRed[i][j]*(21/100))+(matrizGreen[i][j]*(72/100)+(matrizBlue[i][j]*(7/100))));
			}
		}

		//Retorna a matriz resultado
		return matrizRes;
	}



	/*
	 * Método que lê uma imagem de um diretório e já faz a conversão para uma matriz
	 */
	public static int[][] leImagem(String nome, String canalRgb){
		//Seta nulo para a matriz 
		int array[][]=null;
		//Declara uma variavel do tipo BufferImage
		BufferedImage objeto;
		//Tratamento de exceções
		try {
			//Instanciando o bufferImage
			objeto = ImageIO.read(new File(nome));
			//System.out.println("Tipo objeto: "+objeto.getType());
			//Instanciando a matriz
			array= new int[objeto.getHeight()][objeto.getWidth()];
			//Estrutura de repetição para percorrer a linha da matriz
			for (int i=0; i<objeto.getHeight(); i++){
				//Estrutura de repetição para percorrer a coluna da matriz
				for(int j=0; j<objeto.getWidth();j++){
					//Testa qual é o canal passado por parâmetro
					if(canalRgb=="Red")
						array[i][j]=new Color(objeto.getRGB(j, i)).getRed();
					else if(canalRgb=="Green")
						array[i][j]=new Color(objeto.getRGB(j, i)).getGreen();
					else if(canalRgb=="Blue")
						array[i][j]=new Color(objeto.getRGB(j, i)).getBlue();

					//System.out.println(array[i][j]);
				}
			}
			//retorna a matriz
			return array;


		} //Captura a exceção
		catch (IOException e) {
			//imprime o rastro de pilha
			e.printStackTrace();
			//retorna a matriz
			return array;

		}



	}
}
