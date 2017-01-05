package enhance;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;


/*
 * Esta classe contém métodos essenciais para processamento de imagens digitais.
 */
public class MetodosPDI {

	//Variáveis globais
	static String dir="1";
	static int ite;


	//Matriz global para controle do método de conectividade
	public static int[][] arrayObjeto;

	public static BufferedImage rotacionar_180(BufferedImage img){

		int width  = img.getWidth();
		int height = img.getHeight();

		double angle = Math.toRadians(180.0);
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);
		double x0 = 0.5 * (width  - 1);     // point to rotate about
		double y0 = 0.5 * (height - 1);     // center of image

		BufferedImage imgRot = new BufferedImage(width, height, 5);
		// rotation
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				double a = x - x0;
				double b = y - y0;
				int xx = (int) (+a * cos - b * sin + x0);
				int yy = (int) (+a * sin + b * cos + y0);

				// plot pixel (x, y) the same color as (xx, yy) if it's in bounds
				if (xx >= 0 && xx < width && yy >= 0 && yy < height) {
					imgRot.setRGB(x, y, img.getRGB(xx, yy));
				}
			}
		}

		return imgRot;
	}

	/*
	 * Método que conta quantidade de pixel/cor a partir de uma matriz em um vetor
	 */
	public static int[] contarCorCinzas(int array[][]){
		//Instanciando o vetor
		int[] cores=new int[256];
		//Variável de Controle
		int x=0;

		//Estrutura repetição para percorrer a linha do matriz
		for (int i=0; i<array.length; i++){
			//Estrutura de repetição para percorrer a coluna do matriz
			for(int j=0; j<array[i].length;j++){
				//A variavel de controle recebe a cor do pixel em questão
				x=array[i][j];
				//No vetor, incrementa-se a quantidade de pixel da cor em questão
				cores[x]+=1;


			}
		}

		/*
		 * Estrutura de repetição para percorrer o vetor e imprimir as cores e 
		 * 	sua quantidade de pixel
		 */
		for (int i = 0; i < cores.length; i++) {
			if(cores[i]!=0)
				System.out.println(+cores[i]+"\tpixels de cor:\t"+i);
		}	

		//Retorna o vetor de pixel/cor
		return cores;
	}

	//Método que conta quantidade de pixels por cor a partir de uma matriz, sem mostrar cores
	public int[] contarCorCinzasSemPrint(int[][] array) {
		//Instanciando o vetor
		int[] cores=new int[256];
		//Variável de Controle
		int x=0;

		//Estrutura repetição para percorrer a linha do matriz
		for (int i=0; i<array.length; i++){
			//Estrutura de repetição para percorrer a coluna do matriz
			for(int j=0; j<array[i].length;j++){
				//A variavel de controle recebe a cor do pixel em questão
				x=array[i][j];
				//No vetor, incrementa-se a quantidade de pixel da cor em questão
				cores[x]+=1;


			}
		}
		//Retorna o vetor de pixel/cor
		return cores;

	}

	//Métodos para contar Objetos de conectividade 4
	public static int contarObjetos4(int array[][], int limLinha, int limColuna, int conect){
		//Variável para contabilizar a qntd de objetos
		int obj=0;
		//Instanciando uma matriz auxiliar do tamanho da que está sendo passada como parâmetro
		int arrayAux[][]= new int[limLinha][limColuna];

		//Estrutura repetição para percorrer a linha da matriz
		for (int i=0;i<limLinha;i++){
			//Estrutura repetição para percorrer a coluna da matriz
			for(int j=0;j<limColuna;j++){
				//copiando a matriz de parâmetro na matriz auxiliar
				arrayAux[i][j]=array[i][j];
			}
		}

		//Estrutura repetição para percorrer a linha da matriz
		for (int linha=0;linha<limLinha;linha++){
			//Estrutura repetição para percorrer a coluna da matriz
			for(int coluna=0;coluna<limColuna;coluna++){
				//testa se o pixel da posição é igual a várialvel de conexão passada como parâmetro
				if(arrayAux[linha][coluna]==conect){
					//incrementa a quantidade de objetos
					obj+=1;
					//chama, de maneira recursiva, a função para visitar a posição da matriz dessa iteração
					visitar4(arrayAux,linha,coluna,limLinha,limColuna,conect);
				}
			}
		}
		//retorna a variável que contém a quantidade de objetos
		return obj;
	}



	//Métodos para contar Objetos de conectividade 8
	public static int contarObjetos8(int array[][], int limLinha, int limColuna, int conect){
		//Variável para contabilizar a qntd de objetos
		int obj=0;
		//Instanciando uma matriz auxiliar do tamanho da que está sendo passada como parâmetro
		int arrayAux[][]= new int[limLinha][limColuna];

		//Estrutura repetição para percorrer a linha da matriz
		for (int i=0;i<limLinha;i++){
			//Estrutura repetição para percorrer a coluna da matriz
			for(int j=0;j<limColuna;j++){
				//copiando a matriz de parâmetro na matriz auxiliar
				arrayAux[i][j]=array[i][j];
			}
		}

		//Estrutura repetição para percorrer a linha da matriz
		for (int linha=0;linha<limLinha;linha++){
			//Estrutura repetição para percorrer a coluna da matriz
			for(int coluna=0;coluna<limColuna;coluna++){
				//testa se o pixel da posição é igual a várialvel de conexão passada como parâmetro
				if(arrayAux[linha][coluna]==conect){
					//incrementa a quantidade de objetos
					obj+=1;
					//chama, de maneira recursiva, a função para visitar a posição da matriz dessa iteração
					visitar8(arrayAux, linha, coluna, limLinha, limColuna,conect);
				}
			}
		}

		//retorna a variável que contém a quantidade de objetos
		return obj;
	}

	//Métodos para contar Objetos de conectividade 8
	public static int contarObjetos8_Borda(int array[][], int limLinha, int limColuna, int conect){
		//Variável para contabilizar a qntd de objetos
		int obj=0;
		//Instanciando uma matriz auxiliar do tamanho da que está sendo passada como parâmetro
		int arrayAux[][]= new int[limLinha][limColuna];

		//Estrutura repetição para percorrer a linha da matriz
		for (int i=limLinha-20;i<limLinha;i++){
			//Estrutura repetição para percorrer a coluna da matriz
			for(int j=0;j<limColuna;j++){
				//copiando a matriz de parâmetro na matriz auxiliar
				arrayAux[i][j]=array[i][j];
			}
		}

		//Estrutura repetição para percorrer a linha da matriz
		for (int linha=0;linha<limLinha;linha++){
			//Estrutura repetição para percorrer a coluna da matriz
			for(int coluna=0;coluna<limColuna;coluna++){
				//testa se o pixel da posição é igual a várialvel de conexão passada como parâmetro
				if(arrayAux[linha][coluna]==conect){
					//incrementa a quantidade de objetos
					obj+=1;
					//chama, de maneira recursiva, a função para visitar a posição da matriz dessa iteração
					visitar8(arrayAux, linha, coluna, limLinha, limColuna,conect);
				}
			}
		}

		//retorna a variável que contém a quantidade de objetos
		return obj;
	}

	//Método para suavização de ruído com filtro de média e janela de convolução
	public static int[][] convolução(int matriz[][], double[][] elem){

		//Instanciando matriz auxiliar e matriz para resultado, a partir da matriz de parâmetro
		int matrizAux[][] = new int[matriz.length][matriz.length];
		int matrizResultado[][]=new int[matriz.length][matriz.length];

		//Estrutura repetição para percorrer a linha da matriz
		for (int i=0; i<matriz.length; i++){
			//Estrutura repetição para percorrer a coluna da matriz
			for(int j=0; j<matriz.length;j++){
				//copiando a matriz de parâmetro na matriz auxiliar
				matrizAux[i][j]=matriz[i][j];
			}
		}

		@SuppressWarnings("unused")
		//Variável de controle
		int cont;
		//Variáel para auxiliar em cálculos 
		double novoValor;
		//Estrutura repetição para percorrer a linha da matriz
		for (int k=(int) Math.floor((elem.length/2)); k<matrizAux.length-(int) Math.floor((elem.length/2));k++){
			//Estrutura repetição para percorrer a linha da matriz
			for (int l=(int) Math.floor((elem.length/2));l<matrizAux.length-(int) Math.floor((elem.length/2));l++){
				//Instanciando a variável de controle
				cont=0;
				//Instanciando a variável para cálculos
				novoValor=0;
				//Estrutura repetição para percorrer a linha da matriz, neste caso, a janela de convolução
				for (int m= -(int) Math.floor((elem.length/2));m<=(int) Math.floor((elem.length/2));m++){
					//Estrutura repetição para percorrer a coluna da matriz, neste caso, a janela de convolução
					for (int n= -(int) Math.floor((elem.length/2));n<=(int) Math.floor((elem.length/2));n++){
						//variavel recebe o calculo da média entre a a posição da matriz X a respectiva posição da janela de convolução 
						novoValor+=elem[m+(int) Math.floor((elem.length/2))][(int) Math.floor((elem.length/2))+n] * matrizAux[k+m][l+n];
						//incrementa a variável de controle
						cont+=1;
						//A matriz resultado recebe o calculo da média na posição da iteração em questão
						matrizResultado[k][l]=(int)novoValor;
					}

				}


			}
		}
		//retorna a matriz resultado
		return matrizResultado;
	}


	//Método para suavização de ruído com filtro da mediana e janela de convolução
	public static int[][] convoluçãoMediana(int matriz[][],int tamJanela, int elementoEstruturante){

		//Instanciando a matriz auxiliar, a matriz resultado e a matriz da janela de convolução
		int matrizAux[][] = new int[matriz.length][matriz.length];
		int matrizResultado[][]=new int[matriz.length][matriz.length];
		int janela[][]= new int[tamJanela][tamJanela];

		//Estrutura repetição para percorrer a linha da matriz, neste caso, a janela de convolução
		for (int g=0; g<janela.length;g++){
			//Estrutura repetição para percorrer a coluna da matriz, neste caso, a janela de convolução
			for (int h=0; h<janela.length;h++){
				//A posição em questão da janela de convolução recebe o elemento estruturante, passado por parâmetro
				janela[g][h]=elementoEstruturante;
			}
		}
		//Estrutura repetição para percorrer a linha da matriz
		for (int i=0; i<matriz.length; i++){
			//Estrutura repetição para percorrer a linha da matriz
			for(int j=0; j<matriz.length;j++){
				//copiando a matriz de parâmetro na matriz auxiliar
				matrizAux[i][j]=matriz[i][j];
			}
		}
		//Instanciando um vertor para fazer a operação da mediana
		int vetor[]=new int[tamJanela*tamJanela];
		//Instanciando variável que vai receber a mediana
		int mediana = 0;
		//Declarando a variável que vai receber a posição da mediana
		int x;
		//VariáveL de controle
		int cont;

		//Estrutura repetição para percorrer a linha da matriz
		for (int k=(int) Math.floor((janela.length/2)); k<matrizAux.length-(int) Math.floor((janela.length/2));k++){
			//Estrutura repetição para percorrer a coluna da matriz
			for (int l=(int) Math.floor((janela.length/2));l<matrizAux.length-(int) Math.floor((janela.length/2));l++){
				//Inicializando a variavel de controle
				cont=0;
				//Estrutura repetição para percorrer a linha da matriz, neste caso, a janela de convolução
				for (int m= -(int) Math.floor((janela.length/2));m<=(int) Math.floor((janela.length/2));m++){
					//Estrutura repetição para percorrer a coluna da matriz, neste caso, a janela de convolução
					for (int n= -(int) Math.floor((janela.length/2));n<=(int) Math.floor((janela.length/2));n++){
						//vetor recebe os valores do calculo da mediana
						vetor[cont]=janela[m+(int) Math.floor((janela.length/2))][(int) Math.floor((janela.length/2))+n] * matrizAux[k+m][l+n];
						//incrementa a variável de controle
						cont+=1;
						//Ordena o vetor que contém os valores dos calculos para a mediana
						Arrays.sort(vetor);
						//Variável recebe a posição da mediana
						x= (int) Math.ceil(vetor.length/2);
						//Variavel recebe o valor do vetor a partir da posição  da mediana
						mediana=vetor[x];
					}
				}
				//Matriz resultado na posição em questão, recebe o valor da mediana
				matrizResultado[k][l]=mediana;
			}
		}

		//retorna a matriz resultado
		return matrizResultado;
	}

	//Método para suavização de ruído com filtro da média, no entando com janela de convolução seletiva
	public static int[][] convoluçãoMédiaSeletiva(int matriz[][], double[][] elem){
		//Instanciando a matriz auxiliar, a matriz resultado 
		int matrizAux[][] = new int[matriz.length][matriz.length];
		int matrizResultado[][]=new int[matriz.length][matriz.length];

		//Estrutura repetição para percorrer a linha da matriz
		for (int i=0; i<matriz.length; i++){
			//Estrutura repetição para percorrer a linha da matriz
			for(int j=0; j<matriz.length;j++){
				//copiando a matriz de parâmetro na matriz auxiliar
				matrizAux[i][j]=matriz[i][j];
			}
		}
		//Variável que receberá o calculo da média
		double novoValor;
		//Estrutura repetição para percorrer a linha da matriz
		for (int k=(int) Math.floor((elem.length/2)); k<matrizAux.length-(int) Math.floor((elem.length/2));k++){
			//Estrutura repetição para percorrer a coluna da matriz
			for (int l=(int) Math.floor((elem.length/2));l<matrizAux.length-(int) Math.floor((elem.length/2));l++){
				//inicializando a variavel que receberá o calculo da média
				novoValor=0;
				//Testa se a tom da cor é 255 (branco) ou 0(preto) 
				if(matrizAux[k][l]==255 || matrizAux[k][l]==0){
					//Estrutura repetição para percorrer a linha da matriz, neste caso, a janela de convolução
					for (int m= -(int) Math.floor((elem.length/2));m<=(int) Math.floor((elem.length/2));m++){
						//Estrutura repetição para percorrer a coluna da matriz, neste caso, a janela de convolução
						for (int n= -(int) Math.floor((elem.length/2));n<=(int) Math.floor((elem.length/2));n++){
							//variavel incrementa o cálculo da média entre a posição da matriz e a respctiva posição da janela de convolução
							novoValor+=elem[m+(int) Math.floor((elem.length/2))][(int) Math.floor((elem.length/2))+n] * matrizAux[k+m][l+n];
							//matriz resultado recebe o valor do cálculo da média
							matrizResultado[k][l]=(int)novoValor;
						}
					}

				}
				//Se tom não for 255 ou 0
				else{
					//matriz resultado recebe o valor da matriz auxiliar, isto é, o valor original
					matrizResultado[k][l]=matrizAux[k][l];
				}
			}
		}
		//retorna a matriz resultado
		return matrizResultado;
	}


	/*
	 * Método para suavização de ruído com filtro da média com janela de convolução seletiva, 
	 * no entanto, sem elemento estruturante
	 */
	public static int[][] convoluçãoMédiaSeletivaSemElemento(int matriz[][], double[][] elem){
		//Instanciando a matriz auxiliar, a matriz resultado 
		int matrizAux[][] = new int[matriz.length][matriz.length];
		int matrizResultado[][]=new int[matriz.length][matriz.length];


		//Estrutura repetição para percorrer a linha da matriz
		for (int i=0; i<matriz.length; i++){
			//Estrutura repetição para percorrer a linha da matriz
			for(int j=0; j<matriz.length;j++){
				//copiando a matriz de parâmetro na matriz auxiliar
				matrizAux[i][j]=matriz[i][j];
			}
		}
		//variavel de controle
		double contMedia;
		//variável que receberá o calculo da média 
		double novoValor;
		//Estrutura repetição para percorrer a linha da matriz
		for (int k=(int) Math.floor((elem.length/2)); k<matrizAux.length-(int) Math.floor((elem.length/2));k++){
			//Estrutura repetição para percorrer a coluna da matriz
			for (int l=(int) Math.floor((elem.length/2));l<matrizAux.length-(int) Math.floor((elem.length/2));l++){
				//inicializando a variavel de controle, e a que receberá o calculo da média
				contMedia=0;
				novoValor=0;
				//Estrutura repetição para percorrer a linha da matriz, neste caso, a janela de convolução
				for (int m= -(int) Math.floor((elem.length/2));m<=(int) Math.floor((elem.length/2));m++){
					//Estrutura repetição para percorrer a coluna da matriz, neste caso, a janela de convolução
					for (int n= -(int) Math.floor((elem.length/2));n<=(int) Math.floor((elem.length/2));n++){
						//Testa se a o valor da matriz, ou seja, o tom de cor é diferente de 0 (preto) 
						if(matrizAux[m+k][n+l]!=0){
							//Matriz da janela de convolução, recebe o valor do elemento estruturante igual a 1
							elem[m+(int) Math.floor((elem.length/2))][(int) Math.floor((elem.length/2))+n]=1;
							//incrementa o variável de controle
							contMedia+=1;
						}
						//Se o tom de cor for igual a zero
						else{
							//Matriz da janela de convolução, recebe o valor do elemento estruturante igual a 0
							elem[m+(int) Math.floor((elem.length/2))][(int) Math.floor((elem.length/2))+n]=0;
						}
					}
				}
				//Estrutura repetição para percorrer a linha da matriz, neste caso, a janela de convolução
				for (int j = 0; j < elem.length; j++) {
					//Estrutura repetição para percorrer a coluna da matriz, neste caso, a janela de convolução
					for (int j2 = 0; j2 < elem.length; j2++) {
						//Testa se elemento estruturante da janela de convolução é igual a 1
						if(elem[j][j2]==1){
							//Matriz da janela de convolução recebe o cálculo automático do elemento estruturante pela média 
							elem[j][j2]=1.0/contMedia;
						}
					}
				}

				//Testa se a tom da cor é 255 (branco) ou 0(preto) 
				if(matrizAux[k][l]==255 || matrizAux[k][l]==0){
					//Estrutura repetição para percorrer a linha da matriz, neste caso, a janela de convolução
					for (int m= -(int) Math.floor((elem.length/2));m<=(int) Math.floor((elem.length/2));m++){
						//Estrutura repetição para percorrer a coluna da matriz, neste caso, a janela de convolução
						for (int n= -(int) Math.floor((elem.length/2));n<=(int) Math.floor((elem.length/2));n++){
							//variavel incrementa o cálculo da média entre a posição da matriz e a respctiva posição da janela de convolução
							novoValor+=elem[m+(int) Math.floor((elem.length/2))][(int) Math.floor((elem.length/2))+n] * matrizAux[k+m][l+n];
							//matriz resultado recebe o valor da cálculo da média
							matrizResultado[k][l]=(int)novoValor;
						}
					}

				}
				//Se o tom não for 255 ou 0
				else{
					//matriz resultado recebe o valor da matriz auxiliar, isto é, o valor original
					matrizResultado[k][l]=matrizAux[k][l];
				}
			}
		}
		//retorna a matriz resultado
		return matrizResultado;
	}


	/*
	 *  ---- PROTÓTIPO ----
	 * Método para induzir ruído na imagem
	 */
	public static int[][] induzirRuido(int matriz[][]){
		int matrizAux[][] = new int[matriz.length][matriz.length];
		int matrizResultado[][]=new int[matriz.length][matriz.length];
		for (int i=0; i<matriz.length; i++){
			for(int j=0; j<matriz.length;j++){
				matrizAux[i][j]=matriz[i][j];
			}
		}
		int cont=0;
		for (int k=2; k<matrizAux.length-2;k++){
			for (int l=2;l<matrizAux.length-2;l++){
				if(cont==4){
					matrizResultado[k][l]=0;
					matrizResultado[k][l+2]=0;
					matrizResultado[k+2][l]=0;
					matrizResultado[k][l-2]=0;
					matrizResultado[k-2][l]=0;
					matrizResultado[k+2][l+2]=0;
					matrizResultado[k+2][l+2]=0;
					matrizResultado[k-2][l-2]=0;
					matrizResultado[k-2][l-2]=0;
					cont=0;
				}
				else{
					matrizResultado[k][l]=matrizAux[k][l];
					cont+=1;
				}

			}
		}
		return matrizResultado;
	}


	/*
	 * Método de transformada de distância ChessBoard (Tabuleiro de Xadrez)
	 */
	public static void distChess(int array[][]){
		//Estrutura repetição para percorrer a linha da matriz
		for (int i=0; i<array.length ;i++){
			//Estrutura repetição para percorrer a coluna da matriz
			for (int j=0; j<array[i].length;j++){
				//Testa se a posição da matriz é a central
				if(array[i][j]==array[5][5]){
					//Declara três variáveis para cálculo a partir da posição central
					int x,x1,x2;
					//Variáveis recebem a posição central menos a posição da interação
					x1=(5-i);
					x2=(5-j);
					//Cálculo específico da transformada de distância ChessBoard
					x=Math.max((Math.abs(x1)),(Math.abs(x2)));
					//Matriz recebe o valor do cáluclo da distância ChessBoard
					array[i][j]=x;
				}
			}
			//Imprimir a linha do matriz
			System.out.println("\n\n");
			imprimirMatrizInt(array);
		}

	}


	/*
	 * Método de transformada de distância Euclididana (Menor distância entre dois pontos)
	 */
	public static void distEuclidiana(double array[][]){
		//Estrutura repetição para percorrer a linha da matriz
		for (int i=0; i<array.length ;i++){
			//Estrutura repetição para percorrer a coluna da matriz
			for (int j=0; j<array[i].length;j++){
				//Testa se a posição da matriz é a central
				if(array[i][j]==array[5][5]){
					//Declara três variáveis para cálculo a partir da posição central
					double x,x1,x2;
					//Variáveis recebem a posição central menos a posição da interação
					x1=(5-i);
					x2=(5-j);
					//Cálculo específico da transformada de distância euclidiana
					x=(Math.pow(x1, 2))+(Math.pow(x2,2));
					x=Math.sqrt(x);
					//Matriz recebe o valor do cálculo da distância euclidiana
					array[i][j]=x;
				}
			}
		}
		//Imprimi a linha da matriz
		System.out.println("\n\n");
		imprimirMatrizDouble(array);
	}

	/*
	 * Método de transformada de distância Manhattan ou City Block 
	 */
	public static void distManhattan(int array[][]){
		//Estrutura repetição para percorrer a linha da matriz
		for (int i=0; i<array.length ;i++){
			//Estrutura repetição para percorrer a coluna da matriz
			for (int j=0; j<array[i].length;j++){
				//Testa se a posição da matriz é a central
				if(array[i][j]==array[5][5]){
					//Declara três variáveis para cálculo a partir da posição central
					int x,x1,x2;
					//Variáveis recebem a posição central menos a posição da interação
					x1=(5-i);
					x2=(5-j);
					//Cálculo específico da transformada de distância Manhattan ou City Block
					x=(Math.abs(x1))+(Math.abs(x2));
					//Matriz recebe o valor do cálculo da distância Manhattan ou City Block
					array[i][j]=x+20;
				}
			}
		}
		//imprimir a linha da matriz
		System.out.println("\n\n");
		imprimirMatrizInt(array);
	}

	/*
	 * Método de transformada de distância Manhattan ou City Block 
	 */
	public static void distManhattanCor(int array[][]){
		//Estrutura repetição para percorrer a linha da matriz
		for (int i=0; i<array.length ;i++){
			//Estrutura repetição para percorrer a coluna da matriz
			for (int j=0; j<array[i].length;j++){
				//Testa se a posição da matriz é a central
				if(array[i][j]==array[5][5]){
					//Declara três variáveis para cálculo a partir da posição central
					int x,x1,x2;
					//Variáveis recebem a posição central menos a posição da interação
					x1=(5-i);
					x2=(5-j);
					//Cálculo específico da transformada de distância Manhattan ou City Block
					x=(Math.abs(x1))+(Math.abs(x2));
					//Matriz recebe o valor do cálculo da distância Manhattan ou City Block
					array[i][j]=x+5;
				}
			}
		}
		//imprimir a linha da matriz
		System.out.println("\n\n");
		imprimirMatrizInt(array);
	}
	
	/*
	 * Método que faz a diferença entre duas matrizes
	 */
	public int[][] diferença(int[][] matriz1, int[][] matriz2){
		//instanciando a matriz resultado
		int[][] matrizResult= new int[Math.max(matriz1.length,matriz2.length)][Math.max(matriz1.length,matriz2.length)];
		//Estrutura repetição para percorrer a linha da matriz
		for (int i = 0; i < matrizResult.length; i++) {
			//Estrutura repetição para percorrer a coluna da matriz
			for (int j = 0; j < matrizResult.length; j++) {
				//Matriz resultado recebe o diferença absoluta entre duas matrizes
				matrizResult[i][j]=Math.abs(matriz1[i][j]-matriz2[i][j]);
			}
		}
		//retorna a matriz resultado
		return matrizResult;
	}


	/*
	 * Método para equalização de imagem a partir de um histograma

	public static int[] equalizar(int array[][]){
		//Instanciando vetor para contar a quantidade de pixel/cor
		int cores[]=new int[256];
		//Instanciando vetor que irá receber o resultado da equalização
		int novaCor[]= new int[256];
		//Intanciando matriz auxiliar 
		int arrayAux[][] = new int[array.length][array.length];

		//Declarando variavel que receberá o tom da cor
		int x;
		//Inicializando variável de controle
		//int qntdCores=0;
		//Estrutura repetição para percorrer a linha da matriz
		for (int i=0; i<array.length; i++){
			//Estrutura repetição para percorrer a coluna da matriz
			for(int j=0; j<array[i].length;j++){
				//Copiando matriz original para a matriz auxiliar
				arrayAux[i][j]=array[i][j];
				//Variavel recebe o tom de cor
				x=array[i][j];
				//vetor incrementa a quantidade de pixel de cor em questão
				cores[x]+=1;


			}
		}

		//Estrutura repetição para percorrer o vetor
		for (int k=0; k<cores.length; k++){
			//Copia o vetor original para o novo vetor
			novaCor[k]= cores[k];
			//Testa se a posição do vetor é diferente de 0
			if(cores[k]!=0){
				//System.out.println("Cor="+k);

				//Incrementa a quantidade de cor diferente de 0 
				//qntdCores+=1;
			}
		}
		//Inicializa a variável que será utilizada para contar espaços
		int livre=0;
		//Estrutura de repetição para percorrer o vetor
		for(int l=0; l<novaCor.length;l++){

			/*
	 * Testa se o valor da posição do vetor é maior ou menor do que os extremos do histograma.
	 * OBS.: Esse teste não é "abstrato", ou seja, tem que ser feito manualmente de acordo com o histograma

			if(novaCor[l]<200 || novaCor[l]>500){
				//Seta 0 para as posições da matriz que satisfazem o teste anterior
				novaCor[l]=0;
				//Incrementa a variável que contém a quantidade de espaços livres
				livre+=1;

			}
		}

		/*	---- TESTE ----
	 * for (int i = 0; i < novaCor.length; i++) {
			if(novaCor[i]!=0){
				System.out.println(novaCor[i]+" pixels de cor "+i);	
			}
		}


		//Constrói o histograma com o novo vetor e seus espaços livres
		mostrarGrafico(novaCor, "Intermediário");
		//Imprime a quantidade de espaços livres
		System.out.println(livre);

		//Faz o cálculo dos espaços que serão atribuidos na equalização, basendo-se nos espaçoes livres e o limites de cores
		int result=Math.abs(256-livre);
		int espaço=(int) Math.floor(livre/result);
		//Imprime tais resultados
		System.out.println("resultado:"+result+" espaço:"+espaço);


		//Inicializa variável de controle
		int cont=0;
		//Inicializa um vetor para receber a equalização
		int corFinal[]= new int[257];

		//Estrutura de repetição para percorrer o vetor
		for (int i = 0; i < novaCor.length; i++) {
			//Testa se o valor da posição do vetor é diferente da posição inicial ou menor que a posição limite
			if(novaCor[i]!=0 && cont+espaço<256){
				//Atribui o valor no vetor final
				corFinal[cont]=novaCor[i];
				//Incrementa a variavel de controle com base nos espaços
				cont+=espaço;

			}
		}

		/* ---- TESTE ----
	 * for (int l=0; l<corFinal.length;l++){
			if(corFinal[l]!=0){
				System.out.println(corFinal[l]+" pixels de cor "+l);
			}
		}

		//Instancia matriz que será retornada 
		int matriz[][]= new int[2][256];

		//Estrutura de repetição para percorrer a linha da matriz
		for (int i = 0; i <2; i++) {
			//Estrutura de repetição para percorrer a coluna da matriz
			for (int j = 0; j < 256; j++) {
				//Testa se o é a primeira linha da matriz e se o valor do vetor é diferente de 0
				if(i==0 && novaCor[j]!=0 ){
					//Atribui o vetor à matriz
					matriz[i][j]=novaCor[j];
				}
				//Testa se é a segunda linha da matriz e se o valor do vetor é diferente de 0
				else if(i==1 && corFinal[j]!=0){
					//Atribui o vetor à matriz
					matriz[i][j]=corFinal[j];
				}
			}

		}


		/* ---- TESTE ----
		for (int i = 0; i <2; i++) {
			for (int j = 0; j < 256; j++) {
				System.out.print(matriz[i][j]+" ");
			}
			System.out.println("\n");
		}
		System.out.println("Existem "+qntdCores+" cores");
		return novaCor;


		//O VETOR FINAL
		return corFinal;

	}

	 */


	/*
	 * Método para dar zoom na imagem sem perder muito a resolução
	 */
	public static int[][] zoom(int[][] matriz, int tamZoom){
		//Instanciando a matriz que será retornada por este método e a matriz auxiliar
		int[][] matrizSaida= new int[tamZoom*matriz.length][tamZoom*matriz.length];
		int[][] matrizAux= new int[matriz.length][matriz.length];

		//Estrutura de repetição para percorrer a linha da matriz
		for (int i = 0; i < matriz.length; i++) {
			//Estrutura de repetição para percorrer a coluna da matriz
			for (int j = 0; j < matriz.length; j++) {
				//Copia a matriz original do parâmetro para a matriz auxiliar
				matrizAux[i][j]=matriz[i][j];
			}
		}

		//Estrutura de repetição para percorrer a linha da matriz
		for (int i=0; i<matrizSaida.length; i++){
			//Estrutura de repetição para percorrer a coluna da matriz
			for(int j=0; j<matrizSaida.length;j++){
				//Seta 0 para a matriz de saída
				matrizSaida[i][j]=0;
			}
		}

		//Estrutura de repetição para percorrer a linha da matriz
		for (int i=0; i<matrizAux.length-1; i++){
			//Estrutura de repetição para percorrer a coluna da matriz
			for(int j=0; j<matrizAux.length-1;j++){
				//Matriz de sáida recebe o valor da matriz auxiliar na posição da iteraão X o tamanho do zoom, passado pelo parâmetro
				matrizSaida[i*tamZoom][j*tamZoom]=matrizAux[i][j];
			}
		}

		//Instancia uma matriz para janela de convolução
		double[][] elem= new double[tamZoom+1][tamZoom+1];

		//Aplica o método da convolução  da média seletiva na matriz de saída
		matrizSaida=convoluçãoMédiaSeletivaSemElemento(matrizSaida, elem);

		/* ---- TESTE ----
		for (int i = 0; i < elem.length; i++) {
			for (int j = 0; j < elem.length; j++) {
				if((i==0 && j==0) || (i==0 && j==elem.length-1) || (i==elem.length-1 && j==0) || (i==elem.length-1 && j==elem.length-1)  ){
					elem[i][j]=x;
				}
				else{
					elem[i][j]=0;
				}
			}
		}

		double x1=1.0/4.0;
		double elem2[][]={{0,x1,0},
						  {x1,0,x1},	
						  {0,x1,0}};

		matrizSaida=convoluçãoMédiaSeletiva(matrizSaida, elem2);
		 */	

		//retorna a matriz de saída, isto é, a matriz com zoom
		return matrizSaida;
	}

	/*
	 * Método que grava imagem em um diretório local a partir de uma matriz, no entanto só utiliza um dos 3 canais RGB,
	 *  ou seja, grava imagem em tons de cinza.
	 */
	public static void gravarImagem(int matriz[][], String dir){
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
			ImageIO.write(imagem, "BMP", new File("C:/Users/Re/Dropbox/workspace/PDI/src/Imagens/"+dir+".bmp"));
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
	 * Método que grava imagem em um diretório local a partir de 3 matrizes utilizando os 3 canais RGB,
	 *  ou seja, grava imagem em colorida.
	 */
	public void gravarImagemColorida(int matrizRed[][], int matrizGreen[][], int matrizBlue[][], String dir, String folder){
		//Tratamento de exceções
		try{
			//Instancia uma imagem em um BufferImage
			BufferedImage imagem= new BufferedImage(matrizRed.length, matrizRed.length, 5);
			System.out.println("entra gravar imagem");

			//Estrutura de repetição para percorrer a linha da matriz
			for (int i=0; i<matrizRed.length; i++){
				//Estrutura de repetição para percorrer a coluna da matriz
				for(int j=0; j<matrizRed.length;j++){
					//Seta no buffer os três canais RGB na sua respectiva posição da iteração
					imagem.setRGB(j, i, new Color(matrizRed[i][j],matrizGreen[i][j],matrizBlue[i][j]).getRGB());

				}
			}
			//Escreve a imagem em um diretório
			ImageIO.write(imagem, "JPG", new File("C:/ImagensPDI/Imagens/"+folder+"/"+dir+".jpg"));
			System.out.println("sai gravar imagem");
		}
		//Captura exceções
		catch(Exception ex){
			//Imprime a exceção
			ex.getMessage();
		}
	}


	/*
	 * Método para imprimir uma matriz de valores com ponto flutuante
	 */
	public static void  imprimirMatrizDouble(double array[][]){

		//Estrutura de repetição para percorrer a linha da matriz
		for (int i=0; i<array.length; i++) {
			//Estrutura de repetição para percorrer a coluna da matriz
			for (int j=0; j<array[i].length; j++) {
				//Imprime o valor da posição da matriz
				System.out.printf(" %.1f ",array[i][j]);
			}
			//Pula uma linha da hora da impressão ao fim da immperssão da coluna 
			System.out.printf("\n");
		}
		//Pula uma linha ao fim da impressão da matriz
		System.out.printf("\n");

	}


	/*
	 * Método para imprimir uma matriz de valores inteiros
	 */
	public static void  imprimirMatrizInt(int array[][]){
		//Estrutura de repetição para percorrer a linha da matriz
		for (int i=0; i<array.length; i++) {
			//Estrutura de repetição para percorrer a coluna da matriz
			for (int j=0; j<array[i].length; j++) {
				//Testa pra saber de o valor da posição é decimal, simplesmente por estética na hora da impressão
				if(array[i][j]==10){
					//Imprime o valor da posição da matriz
					System.out.printf("%d ", array[i][j]);
				}
				//Se não for decimal
				else {
					//Imprime o valor da posição da matriz
					System.out.printf(" %d ", array[i][j]);
				}

			}
			//Pula uma linha da hora da impressão ao fim da immperssão da coluna 
			System.out.printf("\n");
		}
		//Pula uma linha ao fim da impressão da matriz
		System.out.printf("\n");

	}


	/*
	 * Método que transforma um bufferImage para uma matriz, para manipulação da imagem.
	 * No entanto, so utiliza um dos três canais RGB.
	 */
	public int[][] imagemParaMatriz(BufferedImage img, String canalRgb) throws IOException{
		//Seta null para a matriz
		int array[][]=null;
		//Declara uma variavel do tipo BufferImage
		BufferedImage objeto;

		//Variavel recebe o buffer passado por parâmetro
		objeto = img;

		//Instancia a matriz baseando-se no tamanho do Buffer 
		array= new int[objeto.getWidth()][objeto.getWidth()];

		//Variavel para controle que será usada para colocar cabeçalho e rodapé na imagem
		int difMet=((objeto.getWidth()-objeto.getHeight())/2);

		//Estrutura de repetição para percorrer a linha da matriz
		for (int i=0; i<objeto.getHeight(); i++){
			//Estrutura de repetição para percorrer a coluna da matriz
			for(int j=0; j<objeto.getWidth();j++){
				//Testa se o canal passado por parâmetro é o Red
				if(canalRgb=="Red"){
					//Matriz recebe o tom da cor 
					array[i+difMet][j]=new Color(objeto.getRGB(j, i)).getRed();
				}
				//Testa se o canal passado por parâmetro é o Green
				else if(canalRgb=="Green"){
					//Matriz recebe o tom da cor
					array[i+difMet][j]=new Color(objeto.getRGB(j, i)).getGreen();
				}
				//Testa se o canal passado por parâmetro é o Blue
				else if(canalRgb=="Blue"){
					//Matriz recebe o tom da cor
					array[i+difMet][j]=new Color(objeto.getRGB(j, i)).getBlue();
				}
				//Se não for nenhum dois canais RGB
				else{
					//Imprime mensagem de erro
					System.out.println("Canal RGB inexistente \nOs canais são existentes são: Red, Green e Blue");
				}
			}
		}

		//Estrutura de repetição para percorrer a linha da matriz
		for (int i=objeto.getHeight()+difMet; i<objeto.getWidth(); i++){
			//Estrutura de repetição para percorrer a coluno da matriz
			for(int j=0; j<objeto.getWidth();j++){
				//seta zero para o cabeçalho
				array[i][j]=0;
			}
		}
		//Estrutura de repetição para percorrer a linha da matriz
		for (int i=0; i<difMet; i++){
			//Estrutura de repetição para percorrer a coluna da matriz
			for(int j=0; j<objeto.getWidth();j++){
				//Seta zero para o rodapé
				array[i][j]=0;
			}
		}

		//retorna a matriz
		return array;
	}


	/*
	 * Método que transforma um imagem colorida e transforma para uma imagem em tons de cinza
	 */
	public int[][] RGBparaCinza(int matrizRed[][], int matrizGreen[][], int matrizBlue[][]){
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
	public int[][] leImagem(String nome, String canalRgb){
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



	/*
	 * Método para mostrar o vetor de pixels/cor em um grafico 

	public static  void mostrarGrafico(int[] cores, String nome){
		String x;
		//Instanciando uma frame para mostrar o grafico
		JFrame janela = new JFrame();
		//Instanciado o tipo de dados do gráfico
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		////Estrutura de repetição para percorrer o vetor
		for (int i = 0; i < cores.length; i++) {
			//Testes para separar o indices no gráfico
			if(i==256){
				x=i+"";

			}
			else if(i==180){
				x=i+"";

			}
			else if(i==90){
				x=i+"";

			}
			else{ x=i+"";}
			//adiciona o vetor ao gráfico na iteração em questão
			dataset.addValue(cores[i], "Cor",x );

		}
		//Instanciando o tipo do gráfico, neste caso, gráfico de barras
		JFreeChart grafi = ChartFactory.createBarChart(nome,
				"Intensidade",
				"Frequencia de pixels ",
				dataset,
				PlotOrientation.VERTICAL,true, true, false);

		//Adicionando os frames 
		janela.add(new ChartPanel(grafi));
		janela.setVisible(true);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janela.pack();

	}
	 */
	/*
	 * Método para segmentar uma imgame utilizando limiarização
	 */
	public static int[][] segmentaçãoPorLimiarização(int matriz[][],int limiar[] ){
		//Instanciando a matriz auxiliar
		int matrizAux[][] = new int[matriz.length][matriz.length];

		//Estrutura de repetição para percorrer a linha da matriz
		for (int i=0; i<matriz.length; i++){
			//Estrutura de repetição para percorrer a coluna da matriz
			for(int j=0; j<matriz.length;j++){
				//Copiando a matriz original na auxliar
				matrizAux[i][j]=matriz[i][j];

			}
		}

		//Estrutura de repetição para percorrer a linha da matriz
		for (int i=0; i<matrizAux.length; i++){
			//Estrutura de repetição para percorrer a coluna da matriz
			for(int j=0; j<matrizAux[i].length;j++){
				//Estrutura de repetição para percorrer o vetor de limiar passado por parâmetro
				for(int k=0;k<limiar.length;k++){
					//Testa qual iteração está, neste caso, a primeira
					if(k==0){
						//Testa se o primeiro valor do limiar é maior que volar da posição da matriz 
						if(matrizAux[i][j]<limiar[k] ){
							//Seta o valor pra matriz 0(preto)
							matrizAux[i][j]=0;
							//Interrompe a execução
							break;
						}
						//Se o limiar não for maior
						else{
							//Seta o valor prar a matriz 255(preto)
							matrizAux[i][j]=255;
							break;
						}
					}
					//Testa qual iteração está, neste caso, a última
					else if(k==limiar.length-1 ){
						////Testa se o primeiro valor do limiar é menor que volar da posição da matriz 
						if(matrizAux[i][j]>=limiar[k]){
							////Seta o valor pra matriz 0(preto)
							matrizAux[i][j]=255;
							//Interrompe a execução
							break;
						}
					}
					//Se não é nem a primeira nem a última interação
					else{
						//Testa se o valor da matriz está entre o primeiro e o último valor do limitar (limiar <=matriz <limiar)
						if(matrizAux[i][j]>=limiar[k-1] && matrizAux[i][j]<limiar[k]){
							//Seta o valor do limiar na matriz
							matrizAux[i][j]=limiar[k];
							break;
						}
					}


				}
			}
		}
		//retorna a matriz
		return matrizAux;
	}



	/*
	 * Método recursivo que ao achar um objeto separado dentro de uma imagem
	 */
	public static void separar8(int array[][], int linha, int coluna, int limLinha , int limColuna, int conect){
		//Incrementando a variável global
		ite+=1;

		//System.out.println(ite);
		//Testa se é a primeira iteração
		if(ite==1){
			//Estrutura de repetição para percorrer a linha da matriz
			for (int i=0;i<limLinha;i++){
				//Estrutura de repetição para percorrer a coluna da matriz
				for(int j=0;j<limColuna;j++){
					//seta branco pra toda matriz
					arrayObjeto[i][j]=255;
				}
			}
		}

		//seta o -1(visitado)
		array[linha][coluna]=-1;
		//testa a pra não dar exceção e visita o da esquerda
		if(linha-1 >=0 && array[linha-1][coluna]==conect){
			arrayObjeto[linha-1][coluna]=0;
			separar8(array, linha-1, coluna, limLinha, limColuna,conect);

		}

		//testa pra ver se não excede o limite linha da mtriz e visita o da direita
		if(linha+1 < limLinha && array[linha+1][coluna]==conect){
			arrayObjeto[linha+1][coluna]=0;
			separar8(array, linha+1, coluna, limLinha, limColuna,conect);

		}
		//testa a pra não dar exceção e visita o de cima
		if(coluna-1 >=0 && array[linha][coluna-1]==conect){
			arrayObjeto[linha][coluna-1]=0;
			separar8(array, linha, coluna-1, limLinha, limColuna,conect);
		}
		//testa a pra ver se não excede o limite coluna da matriz e visita o de baixo
		if(coluna+1 < limColuna && array[linha][coluna+1]==conect){
			arrayObjeto[linha][coluna+1]=0;
			separar8(array, linha, coluna+1, limLinha, limColuna,conect);
		}
		//diagonal direita pra baixo
		if(coluna+1 < limColuna && linha+1<limLinha && array[linha+1][coluna+1]==conect){
			arrayObjeto[linha+1][coluna+1]=0;
			separar8(array, linha+1, coluna+1, limLinha, limColuna,conect);
		}

		//diagonal esquerda pra baixo
		if(coluna-1 >=0 && linha+1<limLinha && array[linha+1][coluna-1]==conect){
			arrayObjeto[linha+1][coluna-1]=0;
			separar8(array, linha+1, coluna-1, limLinha, limColuna,conect);

		}
		//diagonal esquerda pra cima
		if(coluna-1 >=0 && linha-1 >=0 && array[linha-1][coluna-1]==conect){
			arrayObjeto[linha-1][coluna-1]=0;
			separar8(array, linha-1, coluna-1, limLinha, limColuna,conect);
		}
		//diagonal direita pra cima
		if(linha-1 >=0 && coluna+1 < limColuna && array[linha-1][coluna+1]==conect){
			arrayObjeto[linha-1][coluna+1]=0;
			separar8(array, linha-1, coluna+1, limLinha, limColuna,conect);
		}

	}


	/*
	 * Método recursivo que utiliza de outro metodo recursivo para seprar objetos na imagem e faz a gravação somente do objeto
	 */
	public static int separarObjetos(int array[][], int limLinha, int limColuna, int conect){
		//Instanciando variável que ira controlar a qntd de objetos
		int obj=0;
		//Instanciando matriz auxiliar
		int arrayAux[][]= new int[limLinha][limColuna];
		//Estrutura de repetição para percorrer a linha da matriz
		for (int i=0;i<limLinha;i++){
			//Estrutura de repetição para percorrer a coluna da matriz
			for(int j=0;j<limColuna;j++){
				//Copiando a matriz original na auxiliar
				arrayAux[i][j]=array[i][j];
			}
		}
		//instanciando a matriz do objeto
		arrayObjeto= new int[limLinha][limColuna];

		//Estrutura de repetição para percorrer a linha da matriz
		for (int linha=0;linha<limLinha;linha++){
			//Estrutura de repetição para percorrer a coluna da matriz
			for(int coluna=0;coluna<limColuna;coluna++){
				//Testa se o valor da posição da matriz é igual ao conector
				if(arrayAux[linha][coluna]==conect){
					obj+=1;
					System.out.println(obj);
					dir=(""+obj);

					ite=0;
					//Chama o método recursivo
					separar8(arrayAux, linha, coluna, limLinha, limColuna,conect);
					//Grava a imagem
					//gravarImagem(arrayObjeto, dir);

				}
			}
		}
		return obj;
	}

	/*
	 * Método recursivo que seta quais são as posições das matrizes que já foram visitadas para conectividade 4
	 */

	public static void visitar4(int array[][], int linha, int coluna, int limLinha , int limColuna, int conect){
		//seta o -1(visitado)
		array[linha][coluna]=-1;
		//testa a pra não dar exceção e visita o da esquerda
		if(linha-1 >=0 && array[linha-1][coluna]==conect){
			visitar4(array, linha-1, coluna, limLinha, limColuna,conect);
		}

		//testa pra ver se não excede o limite linha da mtriz e visita o da direita
		if(linha+1 < limLinha && array[linha+1][coluna]==conect){
			visitar4(array, linha+1, coluna, limLinha, limColuna,conect);
		}
		//testa a pra não dar exceção e visita o de cima
		if(coluna-1 >=0 && array[linha][coluna-1]==conect){
			visitar4(array, linha, coluna-1, limLinha, limColuna, conect);
		}
		//testa a pra ver se não excede o limite coluna da matriz e visita o de baixo
		if(coluna+1 < limColuna && array[linha][coluna+1]==conect){
			visitar4(array, linha, coluna+1, limLinha, limColuna,conect);
		}
	}


	/*
	 * Método recursivo que seta quais são as posições das matrizes que já foram visitadas para conectividade 4
	 */
	public static void visitar8(int array[][], int linha, int coluna, int limLinha , int limColuna, int conect){
		//seta o -1(visitado)
		array[linha][coluna]=-1;
		//testa a pra não dar exceção e visita o da esquerda
		if(linha-1 >=0 && array[linha-1][coluna]==conect){
			visitar8(array, linha-1, coluna, limLinha, limColuna,conect);
		}

		//testa pra ver se não excede o limite linha da mtriz e visita o da direita
		if(linha+1 < limLinha && array[linha+1][coluna]==conect){
			visitar8(array, linha+1, coluna, limLinha, limColuna,conect);
		}
		//testa a pra não dar exceção e visita o de cima
		if(coluna-1 >=0 && array[linha][coluna-1]==conect){
			visitar8(array, linha, coluna-1, limLinha, limColuna,conect);
		}
		//testa a pra ver se não excede o limite coluna da matriz e visita o de baixo
		if(coluna+1 < limColuna && array[linha][coluna+1]==conect){
			visitar8(array, linha, coluna+1, limLinha, limColuna,conect);
		}
		//diagonal direita pra baixo
		if(coluna+1 < limColuna && linha+1<limLinha && array[linha+1][coluna+1]==conect){
			visitar8(array, linha+1, coluna+1, limLinha, limColuna,conect);
		}

		//diagonal esquerda pra baixo
		if(coluna-1 >=0 && linha+1<limLinha && array[linha+1][coluna-1]==conect){
			visitar8(array, linha+1, coluna-1, limLinha, limColuna,conect);
		}
		//diagonal esquerda pra cima
		if(coluna-1 >=0 && linha-1 >=0 && array[linha-1][coluna-1]==conect){
			visitar8(array, linha-1, coluna-1, limLinha, limColuna,conect);
		}
		//diagonal direita pra cima
		if(linha-1 >=0 && coluna+1 < limColuna && array[linha-1][coluna+1]==conect){
			visitar8(array, linha-1, coluna+1, limLinha, limColuna,conect);
		}
	}


	/*
	 * Método que utiliza técnica de esteganografia para esconder uma imagem dentro da outra

	public static int[][] codificarImg(int[][] matrizOriginal, int[][] matrizMsg){
		int[][] matrizResult= new int[matrizOriginal.length][matrizOriginal.length];
		int[][] matrizresult0;
		int[][] matrizresult1;
		int[][] matrizresult2;
		int[][] matrizresult3;
		int[][] matrizresult4;
		int[][] matrizresult5;
		int[][] matrizresult6;
		int[][] matrizresult7;

		//menos signficativo 7 e o mais segnificativo 0
		matrizresult0=separaMatrizBinária(matrizOriginal, 0);
		matrizresult1=separaMatrizBinária(matrizOriginal, 1);
		matrizresult2=separaMatrizBinária(matrizOriginal, 2);
		matrizresult3=separaMatrizBinária(matrizOriginal, 3);
		matrizresult4=separaMatrizBinária(matrizOriginal, 4);
		matrizresult5=separaMatrizBinária(matrizOriginal, 5);
		matrizresult6=separaMatrizBinária(matrizOriginal, 6);
		matrizresult7=separaMatrizBinária(matrizOriginal, 7);

		for (int i = 0; i < matrizMsg.length; i++) {
			for (int j = 0; j < matrizMsg.length; j++) {
				if(matrizMsg[i][j]==0){
					matrizresult7[i][j]=1;
				}
				else{
					matrizresult7[i][j]=0;}
			}
		}

		matrizResult=juntarMatrizesBináParaUmaMatrizInteira(matrizresult0, matrizresult1, matrizresult2, matrizresult3, matrizresult4, matrizresult5, matrizresult6, matrizresult7);

		return matrizResult;
	}*/

	/*
	 * Método que decodifica o método "codificarImg" 
	 */

	public static int[][] decodificarImg(int[][] matriz){

		matriz=separaMatrizBináriaMenosSign(matriz, 7);
		return matriz;

	}


	/*
	 * Método que utiliza técnica de esteganografia para esconder um texto dentro de uma img 

	public static int[][] codificarMsgBinária(int[][] matrizOriginal, String msg){
		String letra;

		int[][] matrizResult= new int[matrizOriginal.length][matrizOriginal.length];
		int[][] matrizMsg= new int[matrizOriginal.length][matrizOriginal.length];
		int[][] matrizresult0;
		int[][] matrizresult1;
		int[][] matrizresult2;
		int[][] matrizresult3;
		int[][] matrizresult4;
		int[][] matrizresult5;
		int[][] matrizresult6;
		int[][] matrizresult7;


		//menos signficativo 7 e o mais segnificativo 0
		matrizresult0=separaMatrizBinária(matrizOriginal, 0);
		matrizresult1=separaMatrizBinária(matrizOriginal, 1);
		matrizresult2=separaMatrizBinária(matrizOriginal, 2);
		matrizresult3=separaMatrizBinária(matrizOriginal, 3);
		matrizresult4=separaMatrizBinária(matrizOriginal, 4);
		matrizresult5=separaMatrizBinária(matrizOriginal, 5);
		matrizresult6=separaMatrizBinária(matrizOriginal, 6);
		matrizresult7=separaMatrizBinária(matrizOriginal, 7);

		ArrayList<String> vet =new ArrayList<String>();


		for (char c: msg.toCharArray()) {  
			//System.out.println(c);
			letra=Integer.toBinaryString(c); 
			letra=StringUtils.leftPad(letra, 8,'0');
			//System.out.println(letra);

			for(char d:letra.toCharArray()){
				//System.out.println(Character.toString(d));
				vet.add(Character.toString(d));

			}
		}
	//	System.out.println(vet);

		int cont=0;

		System.out.println(matrizMsg.length+" "+matrizresult7.length);
		for (int i = 0; i < matrizresult7.length; i++) {
			for (int j = 0; j < matrizresult7.length; j++) {
				if(vet.size()==cont){
		//			System.out.println("saiu");
					break;
				}
				else{
					matrizMsg[i][j]=Integer.parseInt(vet.get(cont));
					cont+=1;
					//System.out.println(matrizMsg[i][j]);

				}
			}
			if(vet.size()==cont){
				break;
			}

		}

		matrizResult=juntarMatrizesBináParaUmaMatrizInteira(matrizresult0, matrizresult1, matrizresult2, matrizresult3, matrizresult4, matrizresult5, matrizresult6, matrizMsg);


		return matrizResult;

	}*/


	/*
	 * Método que decodifica codificarMsgBinária
	 */
	public static void decodificarMsgBin(int[][] matrizOriginal){
		int[][] matrizAux= new int[matrizOriginal.length][matrizOriginal.length];
		int cont=0;
		String palavra="";
		String texto = "";
		int charCode;

		matrizAux=separaMatrizBinária(matrizOriginal, 7);

		System.out.println("---------------");

		for (int i = 0; i <matrizAux.length; i++) {
			for (int j = 0; j < matrizAux.length; j++) {
				if(cont<8){
					cont+=1;
					palavra += Integer.toString( matrizAux[i][j]);

					//System.out.println(palavra);
				}
				else{
					if(! palavra.contentEquals("00000000")){
						//	System.out.println(palavra);
						charCode = Integer.parseInt(palavra, 2);
						String x= (new Character((char)charCode).toString());
						texto+= x;
						//System.out.println(x);
						//System.out.println(texto);
						palavra=""+matrizAux[i][j];
						cont=1;
					}	

				}
			}
		}
		System.out.println(texto);	
	}


	/*
	 * Método que separa uma imagem normal para uma imagem binária de pos(n)
	 */	 
	public static int[][] separaMatrizBinária(int[][] matriz, int pos){
		int[][] matrizBin = new int[matriz.length][matriz[0].length];
		String x;
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[i].length; j++) {
				x=Integer.toBinaryString(matriz[i][j]);
				x=StringUtils.leftPad(x, 8,"0");
				//System.out.println(x);
				x=x.substring(pos, pos+1);
				//System.out.println(x);
				matrizBin[i][j]=(Integer.parseInt(x));
				//System.out.println(matrizBin[i][j]);
			}
		}

		return matrizBin;

	}


	/*
	 * Método que junta 8 imagens binária em uma imagem normal
	 */

	public static int[][] juntarMatrizesBináParaUmaMatrizInteira(int[][] matriz, int[][] matriz1,int[][] matriz2,int[][] matriz3,int[][] matriz4,int[][] matriz5,int[][] matriz6,int[][] matriz7){
		int[][] matrizInt = new int[matriz.length][matriz.length];
		String x;

		for (int i = 0; i < matrizInt.length; i++) {
			for (int j = 0; j < matrizInt[i].length; j++) {
				x="";	
				x+=Integer.toString(matriz[i][j]);
				x+=Integer.toString(matriz1[i][j]);
				x+=Integer.toString(matriz2[i][j]);
				x+=Integer.toString(matriz3[i][j]);
				x+=Integer.toString(matriz4[i][j]);
				x+=Integer.toString(matriz5[i][j]);
				x+=Integer.toString(matriz6[i][j]);
				x+=Integer.toString(matriz7[i][j]);
				//System.out.println();

				matrizInt[i][j]=Integer.parseInt(x,2);
				//System.out.println(matrizBin[i][j]);
			}
		}

		return matrizInt;

	}

	/*
	 * Método que separa uma imagem normal para uma imagem binária de pos(7), pois a posição 7 é o bit menos significativo 
	 * Obs.: posição 0 é o bit mais significativo
	 */
	public static int[][] separaMatrizBináriaMenosSign(int[][] matriz, int pos){
		int[][] matrizBin = new int[matriz.length][matriz[0].length];
		String x;
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[i].length; j++) {
				x=Integer.toBinaryString(matriz[i][j]);
				x=StringUtils.leftPad(x, 8,"0");
				//System.out.println(x);
				x=x.substring(pos, pos+1);
				//System.out.println(x);
				matrizBin[i][j]=255*(Integer.parseInt(x));
				//System.out.println(matrizBin[i][j]);
			}
		}

		return matrizBin;

	}

}





