package redesNeuraisArtificiais;

import java.util.ArrayList;

/**
 * <b><i>Classe Perceptron</i></b>
 * <br><u>Ter� como base o referencial bibliogr�fico:</u><br>
 * 	  <i>Braga, A. P.; Carvalho, A. P L. F & Ludermir, T. B. Redes Neurais Artificiais: 
 * 	   A teoria e aplica��es. 2� Edi��o. Rio de Janeiro: LTC, 2012</i> 
 * <br><br>
 * Classe respons�vel por um processo de treinamento supervisionado de RNA conhecido como Perceptron.<br>
 * Esse perceptron s� � poss�vel para resolu��o de problemas de separa��o linear de duas classes.
 * 
 * @author Renan Fucci
 * @version 1.0
 * */
public class IAPerceptron {
	public static void main(String[] args) {

		double[]w= {-0.5441,0.5562,-0.4074};
		double eta= 0.1;
		double y0=1, y1=0;
		double[] x0={-1,-0.252236,0.097401};
		double[] x1={-1,0.407141,-0.473686};
		double limiar=0.5;
		trainingPerceptron(eta, x0, x1, y0, y1, w, limiar);
	}
	/**
	 * <b><i>M�todo trainingPerceptron</i></b>
	 * <br>Este m�todo ir� fazer a etapa de treinamento de um problema para separa��o linear com duas classes.
	 * <br><i>(Braga, 2012, p�g. 28 - 35):</i>
	 * <p>Utilizara a equa��o: w(n+1) = w(n) + &eta;ex(n)</p> 
	 * @param eta(double)  � a constante com que o vetor de pesos ser� atualizado; 
	 * @param x0[][](double)  � o vetor do primeiro padr�o inicial;
	 * @param x1[][](double)  � o vetor do segundo padr�es inicial;  
	 * @param y0,y1(double)  � a representa��o respectivas das classes x0 e x1;
	 * @param w[][](double)  � o o vetor inicial de pesos;
	 * @param limiar(double) � a constante que ser� respons�vel por limiarizar a fun��o(U(i)).
	 * */
	public static void trainingPerceptron(double eta , double[] x0, double[] x1, double y0, double y1, double[] w, double limiar ){
		Aritm�ticaMatricial arit =  new Aritm�ticaMatricial();

		ArrayList<double[][]> vetorPadr�es= new ArrayList<double[][]>();
		vetorPadr�es.add(arit.transpostaVetor(x0));
		vetorPadr�es.add(arit.transpostaVetor(x1));

		ArrayList<double[][]> vetorPesos= new ArrayList<double[][]>();
		vetorPesos.add(arit.transpostaVetor(w));

		double u=0;
		double[][] esqF=new double[vetorPesos.get(0).length][(vetorPesos.get(0))[0].length]; 
		double[][] dirF= new double[vetorPesos.get(0).length][(vetorPesos.get(0))[0].length]; 
		double[][] resultF= new double[vetorPesos.get(0).length][(vetorPesos.get(0))[0].length]; 

		int padr�es=0;
		int n=0;
		int parada=0;

		while(parada<2){
			if(padr�es==2){
				padr�es=0;
			}
		
			System.out.println("------------------- w("+(n+1)+") --------------------");
			if(padr�es==1){
				System.out.println("F(u): 1.0 - "+(arit.fun��oU(vetorPadr�es.get(padr�es), vetorPesos.get(n), limiar)));
				u=Math.floor(1-(arit.fun��oU(vetorPadr�es.get(padr�es), vetorPesos.get(n), limiar)));
			}
			else{
				System.out.println("F(u): 0.0 - "+(arit.fun��oU(vetorPadr�es.get(padr�es), vetorPesos.get(n), limiar)));
				u=Math.floor(0-(arit.fun��oU(vetorPadr�es.get(padr�es), vetorPesos.get(n), limiar)));
			}
			System.out.println("u:"+u+"* padr�o("+padr�es+")");
			arit.imprimirMatriz(vetorPadr�es.get(padr�es));
			dirF= arit.setarMatriz(dirF, (arit.multiplica��oConstMatriz(u, vetorPadr�es.get(padr�es)))); 
			
			System.out.println("� Igual");
			arit.imprimirMatriz(dirF);
			
			System.out.println("Eta:"+eta+"* dir � igual:");
			esqF= arit.setarMatriz(esqF, (arit.multiplica��oConstMatriz(eta, dirF)));
			arit.imprimirMatriz(esqF);
			
			System.out.println("Mais + Vetor de pesos:");
			arit.imprimirMatriz(vetorPesos.get(n));
			
			System.out.println("Resultado final W("+(n+2)+")");
			resultF = arit.setarMatriz(resultF ,(arit.somaMxM(esqF, vetorPesos.get(n), resultF)));
			arit.imprimirMatriz(resultF);
			System.out.println("\n");
			vetorPesos.add(n+1, resultF);
			parada=arit.funcaoErro(u, parada);;
			n+=1;
			padr�es+=1;
			
			
		}
	}
}
