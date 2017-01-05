package redesNeuraisArtificiais;

import java.util.ArrayList;

/**
 * <b><i>Classe Perceptron</i></b>
 * <br><u>Terá como base o referencial bibliográfico:</u><br>
 * 	  <i>Braga, A. P.; Carvalho, A. P L. F & Ludermir, T. B. Redes Neurais Artificiais: 
 * 	   A teoria e aplicações. 2ª Edição. Rio de Janeiro: LTC, 2012</i> 
 * <br><br>
 * Classe responsável por um processo de treinamento supervisionado de RNA conhecido como Perceptron.<br>
 * Esse perceptron só é possível para resolução de problemas de separação linear de duas classes.
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
	 * <b><i>Método trainingPerceptron</i></b>
	 * <br>Este método irá fazer a etapa de treinamento de um problema para separação linear com duas classes.
	 * <br><i>(Braga, 2012, pág. 28 - 35):</i>
	 * <p>Utilizara a equação: w(n+1) = w(n) + &eta;ex(n)</p> 
	 * @param eta(double)  é a constante com que o vetor de pesos será atualizado; 
	 * @param x0[][](double)  é o vetor do primeiro padrão inicial;
	 * @param x1[][](double)  é o vetor do segundo padrões inicial;  
	 * @param y0,y1(double)  é a representação respectivas das classes x0 e x1;
	 * @param w[][](double)  é o o vetor inicial de pesos;
	 * @param limiar(double) é a constante que será responsável por limiarizar a função(U(i)).
	 * */
	public static void trainingPerceptron(double eta , double[] x0, double[] x1, double y0, double y1, double[] w, double limiar ){
		AritméticaMatricial arit =  new AritméticaMatricial();

		ArrayList<double[][]> vetorPadrões= new ArrayList<double[][]>();
		vetorPadrões.add(arit.transpostaVetor(x0));
		vetorPadrões.add(arit.transpostaVetor(x1));

		ArrayList<double[][]> vetorPesos= new ArrayList<double[][]>();
		vetorPesos.add(arit.transpostaVetor(w));

		double u=0;
		double[][] esqF=new double[vetorPesos.get(0).length][(vetorPesos.get(0))[0].length]; 
		double[][] dirF= new double[vetorPesos.get(0).length][(vetorPesos.get(0))[0].length]; 
		double[][] resultF= new double[vetorPesos.get(0).length][(vetorPesos.get(0))[0].length]; 

		int padrões=0;
		int n=0;
		int parada=0;

		while(parada<2){
			if(padrões==2){
				padrões=0;
			}
		
			System.out.println("------------------- w("+(n+1)+") --------------------");
			if(padrões==1){
				System.out.println("F(u): 1.0 - "+(arit.funçãoU(vetorPadrões.get(padrões), vetorPesos.get(n), limiar)));
				u=Math.floor(1-(arit.funçãoU(vetorPadrões.get(padrões), vetorPesos.get(n), limiar)));
			}
			else{
				System.out.println("F(u): 0.0 - "+(arit.funçãoU(vetorPadrões.get(padrões), vetorPesos.get(n), limiar)));
				u=Math.floor(0-(arit.funçãoU(vetorPadrões.get(padrões), vetorPesos.get(n), limiar)));
			}
			System.out.println("u:"+u+"* padrão("+padrões+")");
			arit.imprimirMatriz(vetorPadrões.get(padrões));
			dirF= arit.setarMatriz(dirF, (arit.multiplicaçãoConstMatriz(u, vetorPadrões.get(padrões)))); 
			
			System.out.println("é Igual");
			arit.imprimirMatriz(dirF);
			
			System.out.println("Eta:"+eta+"* dir é igual:");
			esqF= arit.setarMatriz(esqF, (arit.multiplicaçãoConstMatriz(eta, dirF)));
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
			padrões+=1;
			
			
		}
	}
}
