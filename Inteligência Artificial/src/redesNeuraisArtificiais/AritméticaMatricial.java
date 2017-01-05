package redesNeuraisArtificiais;

import java.util.ArrayList;

import sun.security.util.Length;
import jdk.nashorn.internal.objects.annotations.Constructor;

/**
 * Classe com métodos para aritmética matricial
 * */
public class AritméticaMatricial {
	/** 
	 * Método que faz a soma de uma constante e uma matriz.
	 * @param constante, matriz(double)
	 * @return matriz(double) - resultado da soma
	 * */
	public double[][] somaConstMatriz(double k, double[][] m){
		double[][] tmp= new double[m.length][m[0].length];

		/** Teste de constante igual a 0, pois se a K=0, a matriz não vai mudar.*/
		if(k==0){
			return m;
		}

		/** Multiplicando cada elemento da matriz pela constante.*/

		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[i].length; j++) {
				tmp[i][j]= (k+(m[i][j]));
			}
		}

		return tmp;
	}

	/** 
	 * 
	 * Método que faz a multiplicação de uma constante e uma matriz. */

	public double[][] multiplicaçãoConstMatriz(double k, double[][] m){
		double[][] tmp= new double[m.length][m[0].length];

		/** Multiplicando cada elemento da matriz pela constante.*/
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[i].length; j++) {
				tmp[i][j]= (k*(m[i][j]));
			}
		}

		return tmp;
	}


	/** 
	 * Método que faz a multiplicação entre duas matrizes.
	 *  */
	public double[][] multiplicaçãoMxM(double[][] m, double[][] m1, double[][] tmp){
		/**
		 *  Testa a regra de Produto Mtricial, na qual diz que o Nº de colunas da MatrizA = Nº linha  da MatrizB.
		 **/
		if(m[0].length != m1.length ){
			System.err.println("Matrizes não possuem a regra de Ai =Bj ");
			System.exit(1);
		}

		/** Instância a matriz resultado(tmp) com o a Linha da M e a Coluna da M1, seguindo as definições matemáticas*/
		tmp= new double[m.length][m1[0].length];


		/** Multiplicando cada elemento da matriz pela constante.*/
		for (int i = 0; i < tmp.length; i++) {
			for (int j = 0; j < tmp[i].length; j++) {
				/** Multiplicação de linha por coluna, pela representação do tabuleiro*/
				for (int k = 0; k < (m1.length); k++) {
					tmp[i][j]+= m[i][k]* m1[k][j];

				}
			}
		}
		return tmp;
	}
	
	
	/** 
	 * Método que faz a soma entre duas matrizes.
	 * 
	 *  */
	public double[][] somaMxM(double[][] m, double[][] m1, double[][] tmp){
		/** Multiplicando cada elemento da matriz pela constante.*/
		for (int i = 0; i < tmp.length; i++) {
			for (int j = 0; j < tmp[i].length; j++) {
				tmp[i][j]= m[i][j]+ m1[i][j];

			}
		}
		return tmp;
	}

	/**
	 * Método para calcular a transposta de uma matriz
	 * @param matriz(double)
	 * @return matriz transposta(double)
	 * */
	public double[][] transpostaMatriz(double[][] m){
		/**
		 * A partir da do parametro ij, intancia uma nova matriz ji, isto é,
		 * o @param com tamanho M[i][j], se torna uma matriz @return com tamanho TMP[M[j]][M[i]]  
		 * */
		double[][] tmp= new double[m[0].length][m.length];

		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[i].length; j++) {
				tmp[j][i]=m[i][j];
			}
		}

		return tmp;
	}

	/**
	 * <b>Método transpostaVetor</b>
	 * Método para calcular a transposta de uma vetor
	 * @param vetor(double)
	 * @return matriz transposta(double)
	 * */

	public double[][] transpostaVetor(double[] m){
		/**
		 * Como o parametro é um vetor,intancia a matriz com o i do tamanho do vetor
		 * */
		double[][] tmp= new double[m.length][1];

		for (int i = 0; i <tmp.length; i++) {
			tmp[i][0]=m[i];
		}
		return tmp;
	}


	/**
	 * <b>Método funçãoU</b>
	 * Esse método vai calcular o f(U(i)) de 
	 * @param m[][](double) é a matriz de padrões
	 * @param w[][](double) é a matriz de pesos
	 * @param limiar(double) é a constante de limiarização
	 * @return x(double) que é a resultado da somatorias dos padrões vezes os pesos, com base no limiar.
	 * */
	public double funçãoU(double[][] m, double[][] w , double limiar){
		double x=0;
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[i].length; j++) {
				x += ((m[i][j])*(w[i][j]));
				//System.out.println("x:"+x);
			}
		}
		
		/**
		 * 
		 * 
		 * 
		 * -------------------------- Conectivoss -------------------------- 
		 * 
		 * 
		 * 
		 * 
		 * */
		if(x>=limiar){
			return 1;
		}
		else{
			return 0;
		}
	}
	
	/**
	 * <b>Método funcaoErroQuadrático</b>
	 * Esse método vai calcular o f(U(i)) de 
	 * @param m[][](double) é a matriz de padrões
	 * @param w[][](double) é a matriz de pesos
	 * @param limiar(double) é a constante de limiarização
	 * @return x(double) que é a resultado da somatorias dos padrões vezes os pesos, com base no limiar.
	 * */
	public double funcaoErroQuadrático(double[][] m, double[][] w , double limiar){
		double x=0;
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[i].length; j++) {
				x += ((m[i][j])*(w[i][j]));
				//System.out.println("x:"+x);
			}
		}
		if(x>=limiar){
			return 1;
		}
		else{
			return 0;
		}
	}
	
	
	/**
	 * <b>Método funçãoConectivoE</b>
	 * Esse método vai calcular a condição de parada com conectivo E.
	 * A condição de parada para o conectivo E(^) é quando o F(u) é igual a zero(0) para os dois padrões(classes).
	 * @param u(double) que é o resultado da funçãoF(u(i));
	 * @param padrão(int) que é o padrão ou classe.
	 * @param parada(int) que é contador para a condição de parada
	 * @return x(int) que é o resultado do incremento da condição de parada.
	 * */
	public int funcaoErro(double u, int parada){
		if(u==0){
			parada+=1;
			if(parada==2){
				return 2;
			}
			else
				return parada;
		}
		return 0;
	}
	
	
	
	/**
	 * Método para imprimir todos elementos de uma matriz
	 * @param matriz(double)
	 * @return null
	 * */
	public void imprimirMatriz(double[][] m){
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[i].length; j++) {
				//System.out.println("i: "+i+" j:"+j);
				System.out.printf("%.4f ",m[i][j]);
			}
			System.out.println("");
		}

	}

	/**
	 * <b>Método setarMatriz</b>
	 * Esse método seta uma matriz, pois usando somente o sinal de igual(=), 
	 * 		ele só faz referência de memória e não seta uma nova matriz.
	 * @param tmp é a matriz que será setada;
	 * @param m é a matriz que vai ser copiada.
	 * @return tmp que é a matriz setada, a cópia da matriz(m) para a matriz(tmp).
	 * */
	public double[][] setarMatriz(double[][] tmp,double[][] m){

		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[i].length; j++) {
				tmp[i][j]=m[i][j];
			}
		}
		return tmp;
	}
	
	public void reta(double[][] x, double[][] w, double limiar, ArrayList<double[][]> amostras){
		double x2; 
		x2= -((w[1][0]/w[2][0])*x[1][0])  + (limiar/w[1][0]) ;
		LineChartDemo6.criarReta(x2,amostras);
		
	}
}
