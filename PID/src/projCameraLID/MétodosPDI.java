package projCameraLID;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JFrame;




import org.apache.commons.lang3.StringUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/*
 * Esta classe cont�m m�todos essenciais para processamento de imagens digitais.
 */
public class M�todosPDI {

	//Vari�veis globais
	static String dir="1";
	static int ite;


	//Matriz global para controle do m�todo de conectividade
	public static int[][] arrayObjeto;

	/*
	 * M�todo que conta quantidade de pixel/cor a partir de uma matriz em um vetor
	 */
	public int[] contarCorCinzas(int array[][]){
		//Instanciando o vetor
		int[] cores=new int[256];
		//Vari�vel de Controle
		int x=0;

		//Estrutura repeti��o para percorrer a linha do matriz
		for (int i=0; i<array.length; i++){
			//Estrutura de repeti��o para percorrer a coluna do matriz
			for(int j=0; j<array[i].length;j++){
				//A variavel de controle recebe a cor do pixel em quest�o
				x=array[i][j];
				//No vetor, incrementa-se a quantidade de pixel da cor em quest�o
				cores[x]+=1;


			}
		}

		/*
		 * Estrutura de repeti��o para percorrer o vetor e imprimir as cores e 
		 * 	sua quantidade de pixel
		 */
		for (int i = 0; i < cores.length; i++) {
			System.out.println(+cores[i]+"pixels de cor"+i);
		}	

		//Retorna o vetor de pixel/cor
		return cores;
	}

	//M�todo que conta quantidade de pixels por cor a partir de uma matriz, sem mostrar cores
	public int[] contarCorCinzasSemPrint(int[][] array) {
		//Instanciando o vetor
		int[] cores=new int[256];
		//Vari�vel de Controle
		int x=0;

		//Estrutura repeti��o para percorrer a linha do matriz
		for (int i=0; i<array.length; i++){
			//Estrutura de repeti��o para percorrer a coluna do matriz
			for(int j=0; j<array[i].length;j++){
				//A variavel de controle recebe a cor do pixel em quest�o
				x=array[i][j];
				//No vetor, incrementa-se a quantidade de pixel da cor em quest�o
				cores[x]+=1;


			}
		}
		//Retorna o vetor de pixel/cor
		return cores;

	}

	//M�todos para contar Objetos de conectividade 4
	public static int contarObjetos4(int array[][], int limLinha, int limColuna, int conect){
		//Vari�vel para contabilizar a qntd de objetos
		int obj=0;
		//Instanciando uma matriz auxiliar do tamanho da que est� sendo passada como par�metro
		int arrayAux[][]= new int[limLinha][limColuna];

		//Estrutura repeti��o para percorrer a linha da matriz
		for (int i=0;i<limLinha;i++){
			//Estrutura repeti��o para percorrer a coluna da matriz
			for(int j=0;j<limColuna;j++){
				//copiando a matriz de par�metro na matriz auxiliar
				arrayAux[i][j]=array[i][j];
			}
		}

		//Estrutura repeti��o para percorrer a linha da matriz
		for (int linha=0;linha<limLinha;linha++){
			//Estrutura repeti��o para percorrer a coluna da matriz
			for(int coluna=0;coluna<limColuna;coluna++){
				//testa se o pixel da posi��o � igual a v�rialvel de conex�o passada como par�metro
				if(arrayAux[linha][coluna]==conect){
					//incrementa a quantidade de objetos
					obj+=1;
					//chama, de maneira recursiva, a fun��o para visitar a posi��o da matriz dessa itera��o
					visitar4(arrayAux,linha,coluna,limLinha,limColuna,conect);
				}
			}
		}
		//retorna a vari�vel que cont�m a quantidade de objetos
		return obj;
	}



	//M�todos para contar Objetos de conectividade 8
	public static int contarObjetos8(int array[][], int limLinha, int limColuna, int conect){
		//Vari�vel para contabilizar a qntd de objetos
		int obj=0;
		//Instanciando uma matriz auxiliar do tamanho da que est� sendo passada como par�metro
		int arrayAux[][]= new int[limLinha][limColuna];

		//Estrutura repeti��o para percorrer a linha da matriz
		for (int i=0;i<limLinha;i++){
			//Estrutura repeti��o para percorrer a coluna da matriz
			for(int j=0;j<limColuna;j++){
				//copiando a matriz de par�metro na matriz auxiliar
				arrayAux[i][j]=array[i][j];
			}
		}

		//Estrutura repeti��o para percorrer a linha da matriz
		for (int linha=0;linha<limLinha;linha++){
			//Estrutura repeti��o para percorrer a coluna da matriz
			for(int coluna=0;coluna<limColuna;coluna++){
				//testa se o pixel da posi��o � igual a v�rialvel de conex�o passada como par�metro
				if(arrayAux[linha][coluna]==conect){
					//incrementa a quantidade de objetos
					obj+=1;
					//chama, de maneira recursiva, a fun��o para visitar a posi��o da matriz dessa itera��o
					visitar8(arrayAux, linha, coluna, limLinha, limColuna,conect);
				}
			}
		}

		//retorna a vari�vel que cont�m a quantidade de objetos
		return obj;
	}

	//M�todo para suaviza��o de ru�do com filtro de m�dia e janela de convolu��o
	public static int[][] convolu��o(int matriz[][], double[][] elem){

		//Instanciando matriz auxiliar e matriz para resultado, a partir da matriz de par�metro
		int matrizAux[][] = new int[matriz.length][matriz.length];
		int matrizResultado[][]=new int[matriz.length][matriz.length];

		//Estrutura repeti��o para percorrer a linha da matriz
		for (int i=0; i<matriz.length; i++){
			//Estrutura repeti��o para percorrer a coluna da matriz
			for(int j=0; j<matriz.length;j++){
				//copiando a matriz de par�metro na matriz auxiliar
				matrizAux[i][j]=matriz[i][j];
			}
		}

		@SuppressWarnings("unused")
		//Vari�vel de controle
		int cont;
		//Vari�el para auxiliar em c�lculos 
		double novoValor;
		//Estrutura repeti��o para percorrer a linha da matriz
		for (int k=(int) Math.floor((elem.length/2)); k<matrizAux.length-(int) Math.floor((elem.length/2));k++){
			//Estrutura repeti��o para percorrer a linha da matriz
			for (int l=(int) Math.floor((elem.length/2));l<matrizAux.length-(int) Math.floor((elem.length/2));l++){
				//Instanciando a vari�vel de controle
				cont=0;
				//Instanciando a vari�vel para c�lculos
				novoValor=0;
				//Estrutura repeti��o para percorrer a linha da matriz, neste caso, a janela de convolu��o
				for (int m= -(int) Math.floor((elem.length/2));m<=(int) Math.floor((elem.length/2));m++){
					//Estrutura repeti��o para percorrer a coluna da matriz, neste caso, a janela de convolu��o
					for (int n= -(int) Math.floor((elem.length/2));n<=(int) Math.floor((elem.length/2));n++){
						//variavel recebe o calculo da m�dia entre a a posi��o da matriz X a respectiva posi��o da janela de convolu��o 
						novoValor+=elem[m+(int) Math.floor((elem.length/2))][(int) Math.floor((elem.length/2))+n] * matrizAux[k+m][l+n];
						//incrementa a vari�vel de controle
						cont+=1;
						//A matriz resultado recebe o calculo da m�dia na posi��o da itera��o em quest�o
						matrizResultado[k][l]=(int)novoValor;
					}

				}


			}
		}
		//retorna a matriz resultado
		return matrizResultado;
	}


	//M�todo para suaviza��o de ru�do com filtro da mediana e janela de convolu��o
	public static int[][] convolu��oMediana(int matriz[][],int tamJanela, int elementoEstruturante){

		//Instanciando a matriz auxiliar, a matriz resultado e a matriz da janela de convolu��o
		int matrizAux[][] = new int[matriz.length][matriz.length];
		int matrizResultado[][]=new int[matriz.length][matriz.length];
		int janela[][]= new int[tamJanela][tamJanela];

		//Estrutura repeti��o para percorrer a linha da matriz, neste caso, a janela de convolu��o
		for (int g=0; g<janela.length;g++){
			//Estrutura repeti��o para percorrer a coluna da matriz, neste caso, a janela de convolu��o
			for (int h=0; h<janela.length;h++){
				//A posi��o em quest�o da janela de convolu��o recebe o elemento estruturante, passado por par�metro
				janela[g][h]=elementoEstruturante;
			}
		}
		//Estrutura repeti��o para percorrer a linha da matriz
		for (int i=0; i<matriz.length; i++){
			//Estrutura repeti��o para percorrer a linha da matriz
			for(int j=0; j<matriz.length;j++){
				//copiando a matriz de par�metro na matriz auxiliar
				matrizAux[i][j]=matriz[i][j];
			}
		}
		//Instanciando um vertor para fazer a opera��o da mediana
		int vetor[]=new int[tamJanela*tamJanela];
		//Instanciando vari�vel que vai receber a mediana
		int mediana = 0;
		//Declarando a vari�vel que vai receber a posi��o da mediana
		int x;
		//Vari�veL de controle
		int cont;

		//Estrutura repeti��o para percorrer a linha da matriz
		for (int k=(int) Math.floor((janela.length/2)); k<matrizAux.length-(int) Math.floor((janela.length/2));k++){
			//Estrutura repeti��o para percorrer a coluna da matriz
			for (int l=(int) Math.floor((janela.length/2));l<matrizAux.length-(int) Math.floor((janela.length/2));l++){
				//Inicializando a variavel de controle
				cont=0;
				//Estrutura repeti��o para percorrer a linha da matriz, neste caso, a janela de convolu��o
				for (int m= -(int) Math.floor((janela.length/2));m<=(int) Math.floor((janela.length/2));m++){
					//Estrutura repeti��o para percorrer a coluna da matriz, neste caso, a janela de convolu��o
					for (int n= -(int) Math.floor((janela.length/2));n<=(int) Math.floor((janela.length/2));n++){
						//vetor recebe os valores do calculo da mediana
						vetor[cont]=janela[m+(int) Math.floor((janela.length/2))][(int) Math.floor((janela.length/2))+n] * matrizAux[k+m][l+n];
						//incrementa a vari�vel de controle
						cont+=1;
						//Ordena o vetor que cont�m os valores dos calculos para a mediana
						Arrays.sort(vetor);
						//Vari�vel recebe a posi��o da mediana
						x= (int) Math.ceil(vetor.length/2);
						//Variavel recebe o valor do vetor a partir da posi��o  da mediana
						mediana=vetor[x];
					}
				}
				//Matriz resultado na posi��o em quest�o, recebe o valor da mediana
				matrizResultado[k][l]=mediana;
			}
		}

		//retorna a matriz resultado
		return matrizResultado;
	}

	//M�todo para suaviza��o de ru�do com filtro da m�dia, no entando com janela de convolu��o seletiva
	public static int[][] convolu��oM�diaSeletiva(int matriz[][], double[][] elem){
		//Instanciando a matriz auxiliar, a matriz resultado 
		int matrizAux[][] = new int[matriz.length][matriz.length];
		int matrizResultado[][]=new int[matriz.length][matriz.length];

		//Estrutura repeti��o para percorrer a linha da matriz
		for (int i=0; i<matriz.length; i++){
			//Estrutura repeti��o para percorrer a linha da matriz
			for(int j=0; j<matriz.length;j++){
				//copiando a matriz de par�metro na matriz auxiliar
				matrizAux[i][j]=matriz[i][j];
			}
		}
		//Vari�vel que receber� o calculo da m�dia
		double novoValor;
		//Estrutura repeti��o para percorrer a linha da matriz
		for (int k=(int) Math.floor((elem.length/2)); k<matrizAux.length-(int) Math.floor((elem.length/2));k++){
			//Estrutura repeti��o para percorrer a coluna da matriz
			for (int l=(int) Math.floor((elem.length/2));l<matrizAux.length-(int) Math.floor((elem.length/2));l++){
				//inicializando a variavel que receber� o calculo da m�dia
				novoValor=0;
				//Testa se a tom da cor � 255 (branco) ou 0(preto) 
				if(matrizAux[k][l]==255 || matrizAux[k][l]==0){
					//Estrutura repeti��o para percorrer a linha da matriz, neste caso, a janela de convolu��o
					for (int m= -(int) Math.floor((elem.length/2));m<=(int) Math.floor((elem.length/2));m++){
						//Estrutura repeti��o para percorrer a coluna da matriz, neste caso, a janela de convolu��o
						for (int n= -(int) Math.floor((elem.length/2));n<=(int) Math.floor((elem.length/2));n++){
							//variavel incrementa o c�lculo da m�dia entre a posi��o da matriz e a respctiva posi��o da janela de convolu��o
							novoValor+=elem[m+(int) Math.floor((elem.length/2))][(int) Math.floor((elem.length/2))+n] * matrizAux[k+m][l+n];
							//matriz resultado recebe o valor do c�lculo da m�dia
							matrizResultado[k][l]=(int)novoValor;
						}
					}

				}
				//Se tom n�o for 255 ou 0
				else{
					//matriz resultado recebe o valor da matriz auxiliar, isto �, o valor original
					matrizResultado[k][l]=matrizAux[k][l];
				}
			}
		}
		//retorna a matriz resultado
		return matrizResultado;
	}


	/*
	 * M�todo para suaviza��o de ru�do com filtro da m�dia com janela de convolu��o seletiva, 
	 * no entanto, sem elemento estruturante
	 */
	public static int[][] convolu��oM�diaSeletivaSemElemento(int matriz[][], double[][] elem){
		//Instanciando a matriz auxiliar, a matriz resultado 
		int matrizAux[][] = new int[matriz.length][matriz.length];
		int matrizResultado[][]=new int[matriz.length][matriz.length];


		//Estrutura repeti��o para percorrer a linha da matriz
		for (int i=0; i<matriz.length; i++){
			//Estrutura repeti��o para percorrer a linha da matriz
			for(int j=0; j<matriz.length;j++){
				//copiando a matriz de par�metro na matriz auxiliar
				matrizAux[i][j]=matriz[i][j];
			}
		}
		//variavel de controle
		double contMedia;
		//vari�vel que receber� o calculo da m�dia 
		double novoValor;
		//Estrutura repeti��o para percorrer a linha da matriz
		for (int k=(int) Math.floor((elem.length/2)); k<matrizAux.length-(int) Math.floor((elem.length/2));k++){
			//Estrutura repeti��o para percorrer a coluna da matriz
			for (int l=(int) Math.floor((elem.length/2));l<matrizAux.length-(int) Math.floor((elem.length/2));l++){
				//inicializando a variavel de controle, e a que receber� o calculo da m�dia
				contMedia=0;
				novoValor=0;
				//Estrutura repeti��o para percorrer a linha da matriz, neste caso, a janela de convolu��o
				for (int m= -(int) Math.floor((elem.length/2));m<=(int) Math.floor((elem.length/2));m++){
					//Estrutura repeti��o para percorrer a coluna da matriz, neste caso, a janela de convolu��o
					for (int n= -(int) Math.floor((elem.length/2));n<=(int) Math.floor((elem.length/2));n++){
						//Testa se a o valor da matriz, ou seja, o tom de cor � diferente de 0 (preto) 
						if(matrizAux[m+k][n+l]!=0){
							//Matriz da janela de convolu��o, recebe o valor do elemento estruturante igual a 1
							elem[m+(int) Math.floor((elem.length/2))][(int) Math.floor((elem.length/2))+n]=1;
							//incrementa o vari�vel de controle
							contMedia+=1;
						}
						//Se o tom de cor for igual a zero
						else{
							//Matriz da janela de convolu��o, recebe o valor do elemento estruturante igual a 0
							elem[m+(int) Math.floor((elem.length/2))][(int) Math.floor((elem.length/2))+n]=0;
						}
					}
				}
				//Estrutura repeti��o para percorrer a linha da matriz, neste caso, a janela de convolu��o
				for (int j = 0; j < elem.length; j++) {
					//Estrutura repeti��o para percorrer a coluna da matriz, neste caso, a janela de convolu��o
					for (int j2 = 0; j2 < elem.length; j2++) {
						//Testa se elemento estruturante da janela de convolu��o � igual a 1
						if(elem[j][j2]==1){
							//Matriz da janela de convolu��o recebe o c�lculo autom�tico do elemento estruturante pela m�dia 
							elem[j][j2]=1.0/contMedia;
						}
					}
				}

				//Testa se a tom da cor � 255 (branco) ou 0(preto) 
				if(matrizAux[k][l]==255 || matrizAux[k][l]==0){
					//Estrutura repeti��o para percorrer a linha da matriz, neste caso, a janela de convolu��o
					for (int m= -(int) Math.floor((elem.length/2));m<=(int) Math.floor((elem.length/2));m++){
						//Estrutura repeti��o para percorrer a coluna da matriz, neste caso, a janela de convolu��o
						for (int n= -(int) Math.floor((elem.length/2));n<=(int) Math.floor((elem.length/2));n++){
							//variavel incrementa o c�lculo da m�dia entre a posi��o da matriz e a respctiva posi��o da janela de convolu��o
							novoValor+=elem[m+(int) Math.floor((elem.length/2))][(int) Math.floor((elem.length/2))+n] * matrizAux[k+m][l+n];
							//matriz resultado recebe o valor da c�lculo da m�dia
							matrizResultado[k][l]=(int)novoValor;
						}
					}

				}
				//Se o tom n�o for 255 ou 0
				else{
					//matriz resultado recebe o valor da matriz auxiliar, isto �, o valor original
					matrizResultado[k][l]=matrizAux[k][l];
				}
			}
		}
		//retorna a matriz resultado
		return matrizResultado;
	}


	/*
	 *  ---- PROT�TIPO ----
	 * M�todo para induzir ru�do na imagem
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
	 * M�todo de transformada de dist�ncia ChessBoard (Tabuleiro de Xadrez)
	 */
	public static void distChess(int array[][]){
		//Estrutura repeti��o para percorrer a linha da matriz
		for (int i=0; i<array.length ;i++){
			//Estrutura repeti��o para percorrer a coluna da matriz
			for (int j=0; j<array[i].length;j++){
				//Testa se a posi��o da matriz � a central
				if(array[i][j]==array[5][5]){
					//Declara tr�s vari�veis para c�lculo a partir da posi��o central
					int x,x1,x2;
					//Vari�veis recebem a posi��o central menos a posi��o da intera��o
					x1=(5-i);
					x2=(5-j);
					//C�lculo espec�fico da transformada de dist�ncia ChessBoard
					x=Math.max((Math.abs(x1)),(Math.abs(x2)));
					//Matriz recebe o valor do c�luclo da dist�ncia ChessBoard
					array[i][j]=x;
				}
			}
			//Imprimir a linha do matriz
			System.out.println("\n\n");
			imprimirMatrizInt(array);
		}

	}


	/*
	 * M�todo de transformada de dist�ncia Euclididana (Menor dist�ncia entre dois pontos)
	 */
	public static void distEuclidiana(double array[][]){
		//Estrutura repeti��o para percorrer a linha da matriz
		for (int i=0; i<array.length ;i++){
			//Estrutura repeti��o para percorrer a coluna da matriz
			for (int j=0; j<array[i].length;j++){
				//Testa se a posi��o da matriz � a central
				if(array[i][j]==array[5][5]){
					//Declara tr�s vari�veis para c�lculo a partir da posi��o central
					double x,x1,x2;
					//Vari�veis recebem a posi��o central menos a posi��o da intera��o
					x1=(5-i);
					x2=(5-j);
					//C�lculo espec�fico da transformada de dist�ncia euclidiana
					x=(Math.pow(x1, 2))+(Math.pow(x2,2));
					x=Math.sqrt(x);
					//Matriz recebe o valor do c�lculo da dist�ncia euclidiana
					array[i][j]=x;
				}
			}
		}
		//Imprimi a linha da matriz
		System.out.println("\n\n");
		imprimirMatrizDouble(array);
	}

	/*
	 * M�todo de transformada de dist�ncia Manhattan ou City Block 
	 */
	public static void distManhattan(int array[][]){
		//Estrutura repeti��o para percorrer a linha da matriz
		for (int i=0; i<array.length ;i++){
			//Estrutura repeti��o para percorrer a coluna da matriz
			for (int j=0; j<array[i].length;j++){
				//Testa se a posi��o da matriz � a central
				if(array[i][j]==array[5][5]){
					//Declara tr�s vari�veis para c�lculo a partir da posi��o central
					int x,x1,x2;
					//Vari�veis recebem a posi��o central menos a posi��o da intera��o
					x1=(5-i);
					x2=(5-j);
					//C�lculo espec�fico da transformada de dist�ncia Manhattan ou City Block
					x=(Math.abs(x1))+(Math.abs(x2));
					//Matriz recebe o valor do c�lculo da dist�ncia Manhattan ou City Block
					array[i][j]=x;
				}
			}
		}
		//imprimir a linha da matriz
		System.out.println("\n\n");
		imprimirMatrizInt(array);
	}

	/*
	 * M�todo que faz a diferen�a entre duas matrizes
	 */
	public int[][] diferen�a(int[][] matriz1, int[][] matriz2){
		//instanciando a matriz resultado
		int[][] matrizResult= new int[Math.max(matriz1.length,matriz2.length)][Math.max(matriz1.length,matriz2.length)];
		//Estrutura repeti��o para percorrer a linha da matriz
		for (int i = 0; i < matrizResult.length; i++) {
			//Estrutura repeti��o para percorrer a coluna da matriz
			for (int j = 0; j < matrizResult.length; j++) {
				//Matriz resultado recebe o diferen�a absoluta entre duas matrizes
				matrizResult[i][j]=Math.abs(matriz1[i][j]-matriz2[i][j]);
			}
		}
		//retorna a matriz resultado
		return matrizResult;
	}


	/*
	 * M�todo para equaliza��o de imagem a partir de um histograma
	 */
	public static int[] equalizar(int array[][]){
		//Instanciando vetor para contar a quantidade de pixel/cor
		int cores[]=new int[256];
		//Instanciando vetor que ir� receber o resultado da equaliza��o
		int novaCor[]= new int[256];
		//Intanciando matriz auxiliar 
		int arrayAux[][] = new int[array.length][array.length];

		//Declarando variavel que receber� o tom da cor
		int x;
		//Inicializando vari�vel de controle
		//int qntdCores=0;
		//Estrutura repeti��o para percorrer a linha da matriz
		for (int i=0; i<array.length; i++){
			//Estrutura repeti��o para percorrer a coluna da matriz
			for(int j=0; j<array[i].length;j++){
				//Copiando matriz original para a matriz auxiliar
				arrayAux[i][j]=array[i][j];
				//Variavel recebe o tom de cor
				x=array[i][j];
				//vetor incrementa a quantidade de pixel de cor em quest�o
				cores[x]+=1;


			}
		}

		//Estrutura repeti��o para percorrer o vetor
		for (int k=0; k<cores.length; k++){
			//Copia o vetor original para o novo vetor
			novaCor[k]= cores[k];
			//Testa se a posi��o do vetor � diferente de 0
			if(cores[k]!=0){
				//System.out.println("Cor="+k);

				//Incrementa a quantidade de cor diferente de 0 
				//qntdCores+=1;
			}
		}
		//Inicializa a vari�vel que ser� utilizada para contar espa�os
		int livre=0;
		//Estrutura de repeti��o para percorrer o vetor
		for(int l=0; l<novaCor.length;l++){

			/*
			 * Testa se o valor da posi��o do vetor � maior ou menor do que os extremos do histograma.
			 * OBS.: Esse teste n�o � "abstrato", ou seja, tem que ser feito manualmente de acordo com o histograma
			 */
			if(novaCor[l]<200 || novaCor[l]>500){
				//Seta 0 para as posi��es da matriz que satisfazem o teste anterior
				novaCor[l]=0;
				//Incrementa a vari�vel que cont�m a quantidade de espa�os livres
				livre+=1;

			}
		}

		/*	---- TESTE ----
		 * for (int i = 0; i < novaCor.length; i++) {
			if(novaCor[i]!=0){
				System.out.println(novaCor[i]+" pixels de cor "+i);	
			}
		}
		 */	

		//Constr�i o histograma com o novo vetor e seus espa�os livres
		mostrarGrafico(novaCor, "Intermedi�rio");
		//Imprime a quantidade de espa�os livres
		System.out.println(livre);

		//Faz o c�lculo dos espa�os que ser�o atribuidos na equaliza��o, basendo-se nos espa�oes livres e o limites de cores
		int result=Math.abs(256-livre);
		int espa�o=(int) Math.floor(livre/result);
		//Imprime tais resultados
		System.out.println("resultado:"+result+" espa�o:"+espa�o);


		//Inicializa vari�vel de controle
		int cont=0;
		//Inicializa um vetor para receber a equaliza��o
		int corFinal[]= new int[257];

		//Estrutura de repeti��o para percorrer o vetor
		for (int i = 0; i < novaCor.length; i++) {
			//Testa se o valor da posi��o do vetor � diferente da posi��o inicial ou menor que a posi��o limite
			if(novaCor[i]!=0 && cont+espa�o<256){
				//Atribui o valor no vetor final
				corFinal[cont]=novaCor[i];
				//Incrementa a variavel de controle com base nos espa�os
				cont+=espa�o;

			}
		}

		/* ---- TESTE ----
		 * for (int l=0; l<corFinal.length;l++){
			if(corFinal[l]!=0){
				System.out.println(corFinal[l]+" pixels de cor "+l);
			}
		}*/

		//Instancia matriz que ser� retornada 
		int matriz[][]= new int[2][256];

		//Estrutura de repeti��o para percorrer a linha da matriz
		for (int i = 0; i <2; i++) {
			//Estrutura de repeti��o para percorrer a coluna da matriz
			for (int j = 0; j < 256; j++) {
				//Testa se o � a primeira linha da matriz e se o valor do vetor � diferente de 0
				if(i==0 && novaCor[j]!=0 ){
					//Atribui o vetor � matriz
					matriz[i][j]=novaCor[j];
				}
				//Testa se � a segunda linha da matriz e se o valor do vetor � diferente de 0
				else if(i==1 && corFinal[j]!=0){
					//Atribui o vetor � matriz
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
		 */

		//O VETOR FINAL
		return corFinal;

	}




	/*
	 * M�todo para dar zoom na imagem sem perder muito a resolu��o
	 */
	public static int[][] zoom(int[][] matriz, int tamZoom){
		//Instanciando a matriz que ser� retornada por este m�todo e a matriz auxiliar
		int[][] matrizSaida= new int[tamZoom*matriz.length][tamZoom*matriz.length];
		int[][] matrizAux= new int[matriz.length][matriz.length];

		//Estrutura de repeti��o para percorrer a linha da matriz
		for (int i = 0; i < matriz.length; i++) {
			//Estrutura de repeti��o para percorrer a coluna da matriz
			for (int j = 0; j < matriz.length; j++) {
				//Copia a matriz original do par�metro para a matriz auxiliar
				matrizAux[i][j]=matriz[i][j];
			}
		}

		//Estrutura de repeti��o para percorrer a linha da matriz
		for (int i=0; i<matrizSaida.length; i++){
			//Estrutura de repeti��o para percorrer a coluna da matriz
			for(int j=0; j<matrizSaida.length;j++){
				//Seta 0 para a matriz de sa�da
				matrizSaida[i][j]=0;
			}
		}

		//Estrutura de repeti��o para percorrer a linha da matriz
		for (int i=0; i<matrizAux.length-1; i++){
			//Estrutura de repeti��o para percorrer a coluna da matriz
			for(int j=0; j<matrizAux.length-1;j++){
				//Matriz de s�ida recebe o valor da matriz auxiliar na posi��o da itera�o X o tamanho do zoom, passado pelo par�metro
				matrizSaida[i*tamZoom][j*tamZoom]=matrizAux[i][j];
			}
		}

		//Instancia uma matriz para janela de convolu��o
		double[][] elem= new double[tamZoom+1][tamZoom+1];

		//Aplica o m�todo da convolu��o  da m�dia seletiva na matriz de sa�da
		matrizSaida=convolu��oM�diaSeletivaSemElemento(matrizSaida, elem);

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

		matrizSaida=convolu��oM�diaSeletiva(matrizSaida, elem2);
		 */	

		//retorna a matriz de sa�da, isto �, a matriz com zoom
		return matrizSaida;
	}

	/*
	 * M�todo que grava imagem em um diret�rio local a partir de uma matriz, no entanto s� utiliza um dos 3 canais RGB,
	 *  ou seja, grava imagem em tons de cinza.
	 */
	public static void gravarImagem(int matriz[][], String dir){
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
			ImageIO.write(imagem, "BMP", new File("C:/Users/Re/Dropbox/workspace/PDI/src/Imagens/"+dir+".bmp"));
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
	 * M�todo que grava imagem em um diret�rio local a partir de 3 matrizes utilizando os 3 canais RGB,
	 *  ou seja, grava imagem em colorida.
	 */
	public void gravarImagemColorida(int matrizRed[][], int matrizGreen[][], int matrizBlue[][], String camera, String dir, String folder){
		//Tratamento de exce��es
		try{
			//Instancia uma imagem em um BufferImage
			BufferedImage imagem= new BufferedImage(matrizRed.length, matrizRed.length, 5);
			System.out.println("entra gravar imagem");

			//Estrutura de repeti��o para percorrer a linha da matriz
			for (int i=0; i<matrizRed.length; i++){
				//Estrutura de repeti��o para percorrer a coluna da matriz
				for(int j=0; j<matrizRed.length;j++){
					//Seta no buffer os tr�s canais RGB na sua respectiva posi��o da itera��o
					imagem.setRGB(j, i, new Color(matrizRed[i][j],matrizGreen[i][j],matrizBlue[i][j]).getRGB());

				}
			}
			//Escreve a imagem em um diret�rio
		//	System.out.println("C:/ImagensPDI/Imagens/"+camera+"/"+folder+"/"+dir+".jpg");
			ImageIO.write(imagem, "JPG", new File("C:/ImagensPDI/Imagens/"+camera+"/"+folder+"/"+dir+".jpg"));
			System.out.println("sai gravar imagem");
		}
		//Captura exce��es
		catch(Exception ex){
			//Imprime a exce��o
			ex.getMessage();
		}
	}


	/*
	 * M�todo para imprimir uma matriz de valores com ponto flutuante
	 */
	public static void  imprimirMatrizDouble(double array[][]){

		//Estrutura de repeti��o para percorrer a linha da matriz
		for (int i=0; i<array.length; i++) {
			//Estrutura de repeti��o para percorrer a coluna da matriz
			for (int j=0; j<array[i].length; j++) {
				//Imprime o valor da posi��o da matriz
				System.out.printf(" %.1f ",array[i][j]);
			}
			//Pula uma linha da hora da impress�o ao fim da immperss�o da coluna 
			System.out.printf("\n");
		}
		//Pula uma linha ao fim da impress�o da matriz
		System.out.printf("\n");

	}


	/*
	 * M�todo para imprimir uma matriz de valores inteiros
	 */
	public static void  imprimirMatrizInt(int array[][]){
		//Estrutura de repeti��o para percorrer a linha da matriz
		for (int i=0; i<array.length; i++) {
			//Estrutura de repeti��o para percorrer a coluna da matriz
			for (int j=0; j<array[i].length; j++) {
				//Testa pra saber de o valor da posi��o � decimal, simplesmente por est�tica na hora da impress�o
				if(array[i][j]==10){
					//Imprime o valor da posi��o da matriz
					System.out.printf("%d ", array[i][j]);
				}
				//Se n�o for decimal
				else {
					//Imprime o valor da posi��o da matriz
					System.out.printf(" %d ", array[i][j]);
				}

			}
			//Pula uma linha da hora da impress�o ao fim da immperss�o da coluna 
			System.out.printf("\n");
		}
		//Pula uma linha ao fim da impress�o da matriz
		System.out.printf("\n");

	}


	/*
	 * M�todo que transforma um bufferImage para uma matriz, para manipula��o da imagem.
	 * No entanto, so utiliza um dos tr�s canais RGB.
	 */
	public int[][] imagemParaMatriz(BufferedImage img, String canalRgb) throws IOException{
		//Seta null para a matriz
		int array[][]=null;
		//Declara uma variavel do tipo BufferImage
		BufferedImage objeto;

		//Variavel recebe o buffer passado por par�metro
		objeto = img;

		//Instancia a matriz baseando-se no tamanho do Buffer 
		array= new int[objeto.getWidth()][objeto.getWidth()];

		//Variavel para controle que ser� usada para colocar cabe�alho e rodap� na imagem
		int difMet=((objeto.getWidth()-objeto.getHeight())/2);

		//Estrutura de repeti��o para percorrer a linha da matriz
		for (int i=0; i<objeto.getHeight(); i++){
			//Estrutura de repeti��o para percorrer a coluna da matriz
			for(int j=0; j<objeto.getWidth();j++){
				//Testa se o canal passado por par�metro � o Red
				if(canalRgb=="Red"){
					//Matriz recebe o tom da cor 
					array[i+difMet][j]=new Color(objeto.getRGB(j, i)).getRed();
				}
				//Testa se o canal passado por par�metro � o Green
				else if(canalRgb=="Green"){
					//Matriz recebe o tom da cor
					array[i+difMet][j]=new Color(objeto.getRGB(j, i)).getGreen();
				}
				//Testa se o canal passado por par�metro � o Blue
				else if(canalRgb=="Blue"){
					//Matriz recebe o tom da cor
					array[i+difMet][j]=new Color(objeto.getRGB(j, i)).getBlue();
				}
				//Se n�o for nenhum dois canais RGB
				else{
					//Imprime mensagem de erro
					System.out.println("Canal RGB inexistente \nOs canais s�o existentes s�o: Red, Green e Blue");
				}
			}
		}

		//Estrutura de repeti��o para percorrer a linha da matriz
		for (int i=objeto.getHeight()+difMet; i<objeto.getWidth(); i++){
			//Estrutura de repeti��o para percorrer a coluno da matriz
			for(int j=0; j<objeto.getWidth();j++){
				//seta zero para o cabe�alho
				array[i][j]=0;
			}
		}
		//Estrutura de repeti��o para percorrer a linha da matriz
		for (int i=0; i<difMet; i++){
			//Estrutura de repeti��o para percorrer a coluna da matriz
			for(int j=0; j<objeto.getWidth();j++){
				//Seta zero para o rodap�
				array[i][j]=0;
			}
		}

		//retorna a matriz
		return array;
	}


	/*
	 * M�todo que transforma um imagem colorida e transforma para uma imagem em tons de cinza
	 */
	public int[][] RGBparaCinza(int matrizRed[][], int matrizGreen[][], int matrizBlue[][]){
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



	/*
	 * M�todo para mostrar o vetor de pixels/cor em um grafico 
	 */
	public static  void mostrarGrafico(int[] cores, String nome){
		String x;
		//Instanciando uma frame para mostrar o grafico
		JFrame janela = new JFrame();
		//Instanciado o tipo de dados do gr�fico
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		////Estrutura de repeti��o para percorrer o vetor
		for (int i = 0; i < cores.length; i++) {
			//Testes para separar o indices no gr�fico
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
			//adiciona o vetor ao gr�fico na itera��o em quest�o
			dataset.addValue(cores[i], "Cor",x );

		}
		//Instanciando o tipo do gr�fico, neste caso, gr�fico de barras
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

	/*
	 * M�todo para segmentar uma imgame utilizando limiariza��o
	 */
	public static int[][] segmenta��oPorLimiariza��o(int matriz[][],int limiar[] ){
		//Instanciando a matriz auxiliar
		int matrizAux[][] = new int[matriz.length][matriz.length];

		//Estrutura de repeti��o para percorrer a linha da matriz
		for (int i=0; i<matriz.length; i++){
			//Estrutura de repeti��o para percorrer a coluna da matriz
			for(int j=0; j<matriz.length;j++){
				//Copiando a matriz original na auxliar
				matrizAux[i][j]=matriz[i][j];

			}
		}

		//Estrutura de repeti��o para percorrer a linha da matriz
		for (int i=0; i<matrizAux.length; i++){
			//Estrutura de repeti��o para percorrer a coluna da matriz
			for(int j=0; j<matrizAux[i].length;j++){
				//Estrutura de repeti��o para percorrer o vetor de limiar passado por par�metro
				for(int k=0;k<limiar.length;k++){
					//Testa qual itera��o est�, neste caso, a primeira
					if(k==0){
						//Testa se o primeiro valor do limiar � maior que volar da posi��o da matriz 
						if(matrizAux[i][j]<limiar[k] ){
							//Seta o valor pra matriz 0(preto)
							matrizAux[i][j]=0;
							//Interrompe a execu��o
							break;
						}
						//Se o limiar n�o for maior
						else{
							//Seta o valor prar a matriz 255(preto)
							matrizAux[i][j]=255;
							break;
						}
					}
					//Testa qual itera��o est�, neste caso, a �ltima
					else if(k==limiar.length-1 ){
						////Testa se o primeiro valor do limiar � menor que volar da posi��o da matriz 
						if(matrizAux[i][j]>=limiar[k]){
							////Seta o valor pra matriz 0(preto)
							matrizAux[i][j]=255;
							//Interrompe a execu��o
							break;
						}
					}
					//Se n�o � nem a primeira nem a �ltima intera��o
					else{
						//Testa se o valor da matriz est� entre o primeiro e o �ltimo valor do limitar (limiar <=matriz <limiar)
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
	 * M�todo recursivo que ao achar um objeto separado dentro de uma imagem
	 */
	public static void separar8(int array[][], int linha, int coluna, int limLinha , int limColuna, int conect){
		//Incrementando a vari�vel global
		ite+=1;

		//System.out.println(ite);
		//Testa se � a primeira itera��o
		if(ite==1){
			//Estrutura de repeti��o para percorrer a linha da matriz
			for (int i=0;i<limLinha;i++){
				//Estrutura de repeti��o para percorrer a coluna da matriz
				for(int j=0;j<limColuna;j++){
					//seta branco pra toda matriz
					arrayObjeto[i][j]=255;
				}
			}
		}

		//seta o -1(visitado)
		array[linha][coluna]=-1;
		//testa a pra n�o dar exce��o e visita o da esquerda
		if(linha-1 >=0 && array[linha-1][coluna]==conect){
			arrayObjeto[linha-1][coluna]=0;
			separar8(array, linha-1, coluna, limLinha, limColuna,conect);

		}

		//testa pra ver se n�o excede o limite linha da mtriz e visita o da direita
		if(linha+1 < limLinha && array[linha+1][coluna]==conect){
			arrayObjeto[linha+1][coluna]=0;
			separar8(array, linha+1, coluna, limLinha, limColuna,conect);

		}
		//testa a pra n�o dar exce��o e visita o de cima
		if(coluna-1 >=0 && array[linha][coluna-1]==conect){
			arrayObjeto[linha][coluna-1]=0;
			separar8(array, linha, coluna-1, limLinha, limColuna,conect);
		}
		//testa a pra ver se n�o excede o limite coluna da matriz e visita o de baixo
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
	 * M�todo recursivo que utiliza de outro metodo recursivo para seprar objetos na imagem e faz a grava��o somente do objeto
	 */
	public static int separarObjetos(int array[][], int limLinha, int limColuna, int conect){
		//Instanciando vari�vel que ira controlar a qntd de objetos
		int obj=0;
		//Instanciando matriz auxiliar
		int arrayAux[][]= new int[limLinha][limColuna];
		//Estrutura de repeti��o para percorrer a linha da matriz
		for (int i=0;i<limLinha;i++){
			//Estrutura de repeti��o para percorrer a coluna da matriz
			for(int j=0;j<limColuna;j++){
				//Copiando a matriz original na auxiliar
				arrayAux[i][j]=array[i][j];
			}
		}
		//instanciando a matriz do objeto
		arrayObjeto= new int[limLinha][limColuna];

		//Estrutura de repeti��o para percorrer a linha da matriz
		for (int linha=0;linha<limLinha;linha++){
			//Estrutura de repeti��o para percorrer a coluna da matriz
			for(int coluna=0;coluna<limColuna;coluna++){
				//Testa se o valor da posi��o da matriz � igual ao conector
				if(arrayAux[linha][coluna]==conect){
					obj+=1;
					System.out.println(obj);
					dir=(""+obj);

					ite=0;
					//Chama o m�todo recursivo
					separar8(arrayAux, linha, coluna, limLinha, limColuna,conect);
					//Grava a imagem
					//gravarImagem(arrayObjeto, dir);

				}
			}
		}
		return obj;
	}

	/*
	 * M�todo recursivo que seta quais s�o as posi��es das matrizes que j� foram visitadas para conectividade 4
	 */

	public static void visitar4(int array[][], int linha, int coluna, int limLinha , int limColuna, int conect){
		//seta o -1(visitado)
		array[linha][coluna]=-1;
		//testa a pra n�o dar exce��o e visita o da esquerda
		if(linha-1 >=0 && array[linha-1][coluna]==conect){
			visitar4(array, linha-1, coluna, limLinha, limColuna,conect);
		}

		//testa pra ver se n�o excede o limite linha da mtriz e visita o da direita
		if(linha+1 < limLinha && array[linha+1][coluna]==conect){
			visitar4(array, linha+1, coluna, limLinha, limColuna,conect);
		}
		//testa a pra n�o dar exce��o e visita o de cima
		if(coluna-1 >=0 && array[linha][coluna-1]==conect){
			visitar4(array, linha, coluna-1, limLinha, limColuna, conect);
		}
		//testa a pra ver se n�o excede o limite coluna da matriz e visita o de baixo
		if(coluna+1 < limColuna && array[linha][coluna+1]==conect){
			visitar4(array, linha, coluna+1, limLinha, limColuna,conect);
		}
	}

	
	/*
	 * M�todo recursivo que seta quais s�o as posi��es das matrizes que j� foram visitadas para conectividade 4
	 */
	public static void visitar8(int array[][], int linha, int coluna, int limLinha , int limColuna, int conect){
		//seta o -1(visitado)
		array[linha][coluna]=-1;
		//testa a pra n�o dar exce��o e visita o da esquerda
		if(linha-1 >=0 && array[linha-1][coluna]==conect){
			visitar8(array, linha-1, coluna, limLinha, limColuna,conect);
		}

		//testa pra ver se n�o excede o limite linha da mtriz e visita o da direita
		if(linha+1 < limLinha && array[linha+1][coluna]==conect){
			visitar8(array, linha+1, coluna, limLinha, limColuna,conect);
		}
		//testa a pra n�o dar exce��o e visita o de cima
		if(coluna-1 >=0 && array[linha][coluna-1]==conect){
			visitar8(array, linha, coluna-1, limLinha, limColuna,conect);
		}
		//testa a pra ver se n�o excede o limite coluna da matriz e visita o de baixo
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
	 * M�todo que utiliza t�cnica de esteganografia para esconder uma imagem dentro da outra
	 */
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
		matrizresult0=separaMatrizBin�ria(matrizOriginal, 0);
		matrizresult1=separaMatrizBin�ria(matrizOriginal, 1);
		matrizresult2=separaMatrizBin�ria(matrizOriginal, 2);
		matrizresult3=separaMatrizBin�ria(matrizOriginal, 3);
		matrizresult4=separaMatrizBin�ria(matrizOriginal, 4);
		matrizresult5=separaMatrizBin�ria(matrizOriginal, 5);
		matrizresult6=separaMatrizBin�ria(matrizOriginal, 6);
		matrizresult7=separaMatrizBin�ria(matrizOriginal, 7);
		
		for (int i = 0; i < matrizMsg.length; i++) {
			for (int j = 0; j < matrizMsg.length; j++) {
				if(matrizMsg[i][j]==0){
					matrizresult7[i][j]=1;
				}
				else{
					matrizresult7[i][j]=0;}
			}
		}

		matrizResult=juntarMatrizesBin�ParaUmaMatrizInteira(matrizresult0, matrizresult1, matrizresult2, matrizresult3, matrizresult4, matrizresult5, matrizresult6, matrizresult7);

		return matrizResult;
	}

	/*
	 * M�todo que decodifica o m�todo "codificarImg" 
	 */
	
	public static int[][] decodificarImg(int[][] matriz){
		
		matriz=separaMatrizBin�riaMenosSign(matriz, 7);
		return matriz;
		
	}

	
	/*
	 * M�todo que utiliza t�cnica de esteganografia para esconder um texto dentro de uma img 
	 */
	public static int[][] codificarMsgBin�ria(int[][] matrizOriginal, String msg){
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
		matrizresult0=separaMatrizBin�ria(matrizOriginal, 0);
		matrizresult1=separaMatrizBin�ria(matrizOriginal, 1);
		matrizresult2=separaMatrizBin�ria(matrizOriginal, 2);
		matrizresult3=separaMatrizBin�ria(matrizOriginal, 3);
		matrizresult4=separaMatrizBin�ria(matrizOriginal, 4);
		matrizresult5=separaMatrizBin�ria(matrizOriginal, 5);
		matrizresult6=separaMatrizBin�ria(matrizOriginal, 6);
		matrizresult7=separaMatrizBin�ria(matrizOriginal, 7);
	
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
		
		matrizResult=juntarMatrizesBin�ParaUmaMatrizInteira(matrizresult0, matrizresult1, matrizresult2, matrizresult3, matrizresult4, matrizresult5, matrizresult6, matrizMsg);
		
		
		return matrizResult;
	
	}

	
	/*
	 * M�todo que decodifica codificarMsgBin�ria
	 */
	public static void decodificarMsgBin(int[][] matrizOriginal){
		int[][] matrizAux= new int[matrizOriginal.length][matrizOriginal.length];
		int cont=0;
		String palavra="";
		String texto = "";
		int charCode;
		
		matrizAux=separaMatrizBin�ria(matrizOriginal, 7);
		
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
	 * M�todo que separa uma imagem normal para uma imagem bin�ria de pos(n)
	 */
	public static int[][] separaMatrizBin�ria(int[][] matriz, int pos){
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
	 * M�todo que junta 8 imagens bin�ria em uma imagem normal
	 */
	
	public static int[][] juntarMatrizesBin�ParaUmaMatrizInteira(int[][] matriz, int[][] matriz1,int[][] matriz2,int[][] matriz3,int[][] matriz4,int[][] matriz5,int[][] matriz6,int[][] matriz7){
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
	 * M�todo que separa uma imagem normal para uma imagem bin�ria de pos(7), pois a posi��o 7 � o bit menos significativo 
	 * Obs.: posi��o 0 � o bit mais significativo
	 */
	public static int[][] separaMatrizBin�riaMenosSign(int[][] matriz, int pos){
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





