package redesNeuraisArtificiais;

import java.util.ArrayList;

import sun.security.util.Length;
import jdk.nashorn.internal.objects.annotations.Constructor;

/**
 * Classe com m�todos para aritm�tica matricial
 * */
public class Aritm�ticaMatricial {
	/** 
	 * M�todo que faz a soma de uma constante e uma matriz.
	 * @param constante, matriz(double)
	 * @return matriz(double) - resultado da soma
	 * */
	public double[][] somaConstMatriz(double k, double[][] m){
		double[][] tmp= new double[m.length][m[0].length];

		/** Teste de constante igual a 0, pois se a K=0, a matriz n�o vai mudar.*/
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
	 * M�todo que faz a multiplica��o de uma constante e uma matriz. */

	public double[][] multiplica��oConstMatriz(double k, double[][] m){
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
	 * M�todo que faz a multiplica��o entre duas matrizes.
	 *  */
	public double[][] multiplica��oMxM(double[][] m, double[][] m1, double[][] tmp){
		/**
		 *  Testa a regra de Produto Mtricial, na qual diz que o N� de colunas da MatrizA = N� linha  da MatrizB.
		 **/
		if(m[0].length != m1.length ){
			System.err.println("Matrizes n�o possuem a regra de Ai =Bj ");
			System.exit(1);
		}

		/** Inst�ncia a matriz resultado(tmp) com o a Linha da M e a Coluna da M1, seguindo as defini��es matem�ticas*/
		tmp= new double[m.length][m1[0].length];


		/** Multiplicando cada elemento da matriz pela constante.*/
		for (int i = 0; i < tmp.length; i++) {
			for (int j = 0; j < tmp[i].length; j++) {
				/** Multiplica��o de linha por coluna, pela representa��o do tabuleiro*/
				for (int k = 0; k < (m1.length); k++) {
					tmp[i][j]+= m[i][k]* m1[k][j];

				}
			}
		}
		return tmp;
	}
	
	
	/** 
	 * M�todo que faz a soma entre duas matrizes.
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
	 * M�todo para calcular a transposta de uma matriz
	 * @param matriz(double)
	 * @return matriz transposta(double)
	 * */
	public double[][] transpostaMatriz(double[][] m){
		/**
		 * A partir da do parametro ij, intancia uma nova matriz ji, isto �,
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
	 * <b>M�todo transpostaVetor</b>
	 * M�todo para calcular a transposta de uma vetor
	 * @param vetor(double)
	 * @return matriz transposta(double)
	 * */

	public double[][] transpostaVetor(double[] m){
		/**
		 * Como o parametro � um vetor,intancia a matriz com o i do tamanho do vetor
		 * */
		double[][] tmp= new double[m.length][1];

		for (int i = 0; i <tmp.length; i++) {
			tmp[i][0]=m[i];
		}
		return tmp;
	}


	/**
	 * <b>M�todo fun��oU</b>
	 * Esse m�todo vai calcular o f(U(i)) de 
	 * @param m[][](double) � a matriz de padr�es
	 * @param w[][](double) � a matriz de pesos
	 * @param limiar(double) � a constante de limiariza��o
	 * @return x(double) que � a resultado da somatorias dos padr�es vezes os pesos, com base no limiar.
	 * */
	public double fun��oU(double[][] m, double[][] w , double limiar){
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
	 * <b>M�todo funcaoErroQuadr�tico</b>
	 * Esse m�todo vai calcular o f(U(i)) de 
	 * @param m[][](double) � a matriz de padr�es
	 * @param w[][](double) � a matriz de pesos
	 * @param limiar(double) � a constante de limiariza��o
	 * @return x(double) que � a resultado da somatorias dos padr�es vezes os pesos, com base no limiar.
	 * */
	public double funcaoErroQuadr�tico(double[][] m, double[][] w , double limiar){
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
	 * <b>M�todo fun��oConectivoE</b>
	 * Esse m�todo vai calcular a condi��o de parada com conectivo E.
	 * A condi��o de parada para o conectivo E(^) � quando o F(u) � igual a zero(0) para os dois padr�es(classes).
	 * @param u(double) que � o resultado da fun��oF(u(i));
	 * @param padr�o(int) que � o padr�o ou classe.
	 * @param parada(int) que � contador para a condi��o de parada
	 * @return x(int) que � o resultado do incremento da condi��o de parada.
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
	 * M�todo para imprimir todos elementos de uma matriz
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
	 * <b>M�todo setarMatriz</b>
	 * Esse m�todo seta uma matriz, pois usando somente o sinal de igual(=), 
	 * 		ele s� faz refer�ncia de mem�ria e n�o seta uma nova matriz.
	 * @param tmp � a matriz que ser� setada;
	 * @param m � a matriz que vai ser copiada.
	 * @return tmp que � a matriz setada, a c�pia da matriz(m) para a matriz(tmp).
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
