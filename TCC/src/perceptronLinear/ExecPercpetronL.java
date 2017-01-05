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
	 * <b> M�todo main(Principal)<\b>
	 * Wfinal[][](double) � o vetor de pesos j� treinado
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
				//System.out.println("U:"+fun��oU(m, wfinal, limiar));
				img[i][j]=fun��oU(m, wfinal, limiar);
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
	 * <b>M�todo execPerceptron</b>
	 * Esse m�todo vai fazer a execu��o do Perceptron Linear ap�s sua etapa de treinamento.
	 * Essse m�todo utiliza-se do m�todo <b>"funcaoU"<\b>.
	 * @param wfinal[][](double) � a transposta do vetor de pesos j� treinado. 
	 * @param vetorExec[](double) � vetor de caracteristica que deve ser classificado.
	 * @param limiar(double) � a constante limiar, neste caso da fun��o degrau.
	 * @return u(int) � classe correspondente ao vetor de caracteristica a ser classificado.
	 * */
	public static int execPerceptron(double[][] wfinal, double[] vetorExec, double limiar){
		double u= fun��oU(transpostaVetor(vetorExec), wfinal, limiar);
		return (int) u;
	}

	/**
	 * <b>M�todo fun��oU</b>
	 * Esse m�todo vai calcular o f(U(i)), que � a somat�ria entre o a matriz de padr�es e a de pesos.
	 * @param m[][](double) � a matriz de padr�es
	 * @param w[][](double) � a matriz de pesos
	 * @param limiar(double) � a � a constante limiar, neste caso da fun��o degrau.
	 * @return x(double)  � o resultado da somatoria dos padr�es vezes os pesos, com base no limiar.
	 * */
	public static int fun��oU(double[][] m, double[][] w , double limiar){
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
	 * <b>M�todo transpostaVetor</b>
	 * M�todo para calcular a transposta de uma vetor.
	 * @param m[](double) vetor para c�lculo da transposta.
	 * @return tmp[][](double) transposta do vetor.
	 * */

	public static double[][] transpostaVetor(double[] m){
		/**
		 * Como o parametro � um vetor,intancia a matriz com o i do tamanho do vetor
		 * */
		double[][] tmp= new double[m.length][1];

		for (int i = 0; i <tmp.length; i++) {
			tmp[i][0]=m[i];
		}
		return tmp;
	}

	/*
	 * M�todo que grava imagem em um diret�rio local a partir de uma matriz, no entanto s� utiliza um dos 3 canais RGB,
	 *  ou seja, grava imagem em tons de cinza.
	 */
	public static void gravarImagem(int matriz[][], String dir, String nomeArquivo){
		//Tratamento de exce��o 
		try{
			//Instancia uma imagem em um BufferImage
			BufferedImage imagem= new BufferedImage(matriz.length, matriz.length, 5);
			//Imprime que comelou a gravar a imagem
			System.out.println("entra gravar imagem");

			//Estrutura de repeti��o para percorrer a linha da matriz
			for (int i=0; i<matriz.length; i++){
				//Estrutura de repeti��o para percorrer a coluna da matriz
				for(int j=0; j<matriz.length;j++){
					//Seta no buffer os tr�s canais RGB na sua respectiva posi��o da itera��o
					imagem.setRGB(j, i, new Color(matriz[i][j],matriz[i][j],matriz[i][j]).getRGB());

				}
			}
			//Escreve a imagem em um diret�rio
			ImageIO.write(imagem, "BMP", new File(dir+"/"+nomeArquivo+".bmp"));
			//Imprime que a imagem terminou de ser gravada
			System.out.println("sai gravar imagem");
		}
		//Captura exce��es
		catch(Exception ex){
			//Imprime a exce��o
			ex.getMessage();
		}
	}


	/*
	 * M�todo que transforma um imagem colorida e transforma para uma imagem em tons de cinza
	 */
	public static int[][] RGBparaCinza(int matrizRed[][], int matrizGreen[][], int matrizBlue[][]){
		//Instancia matriz resultado
		int[][] matrizRes=new int[matrizRed.length][matrizRed.length];
		//Estrutura de repeti��o para percorrer a linha da matriz
		for (int i = 0; i < matrizRes.length; i++) {
			//Estrutura de repeti��o para percorrer a coluna da matriz
			for (int j = 0; j < matrizRes.length; j++) {
				//Matriz resultado recebe c�lculo especifico de RBG para Tons de Cinza
				matrizRes[i][j]=((matrizRed[i][j]*(21/100))+(matrizGreen[i][j]*(72/100)+(matrizBlue[i][j]*(7/100))));
			}
		}

		//Retorna a matriz resultado
		return matrizRes;
	}



	/*
	 * M�todo que l� uma imagem de um diret�rio e j� faz a convers�o para uma matriz
	 */
	public static int[][] leImagem(String nome, String canalRgb){
		//Seta nulo para a matriz 
		int array[][]=null;
		//Declara uma variavel do tipo BufferImage
		BufferedImage objeto;
		//Tratamento de exce��es
		try {
			//Instanciando o bufferImage
			objeto = ImageIO.read(new File(nome));
			//System.out.println("Tipo objeto: "+objeto.getType());
			//Instanciando a matriz
			array= new int[objeto.getHeight()][objeto.getWidth()];
			//Estrutura de repeti��o para percorrer a linha da matriz
			for (int i=0; i<objeto.getHeight(); i++){
				//Estrutura de repeti��o para percorrer a coluna da matriz
				for(int j=0; j<objeto.getWidth();j++){
					//Testa qual � o canal passado por par�metro
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


		} //Captura a exce��o
		catch (IOException e) {
			//imprime o rastro de pilha
			e.printStackTrace();
			//retorna a matriz
			return array;

		}



	}
}
