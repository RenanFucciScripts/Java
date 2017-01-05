package redesNeuraisArtificiais;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class TestePerceptron {
	static Aritm�ticaMatricial arit =  new Aritm�ticaMatricial();
	static long MAX;  
	public static void main(String[] args) {

		ArrayList<double[][]> vetorPadroes= new ArrayList<double[][]>();
		ArrayList<double[][]> vetorPesos= new ArrayList<double[][]>();
		ArrayList<Double> vetorDesejados= new ArrayList<Double>();

		double eta= 0.1;
		double limiar=0.0;

		try {

/*			String dirArq =  "C:/Users/Renan Fucci/Desktop/amostras.txt";
			FileReader arq =  new FileReader(dirArq);
			BufferedReader leitura =  new BufferedReader(arq);
			String linha= leitura.readLine();
			String[] div=linha.split(",");
*/
			double[]w={-0.5441,0.5562,-0.4074};
			double[] x1 ={-1,2,2};
			vetorPadroes.add(arit.transpostaVetor(x1));
			vetorDesejados.add(1.0);
			double[] x2 ={-1,4,4};
			vetorPadroes.add(arit.transpostaVetor(x2));
			vetorDesejados.add(0.0);
			vetorPesos.add(arit.transpostaVetor(w));

			//setando o vetor de pesos randomico
			/*	for (int i = 0; i < w.length; i++) {
					if(i%2==0){
						w[i]=rand.nextDouble() * .5;
					}
					else{
						w[i]=rand.nextDouble() * -.5;
					}
				}
			 */

/*			while(linha!=null){
				div=linha.split(",");
				double bias=-1;
				double ndvi = Double.parseDouble(div[2]);
				double evi = Double.parseDouble(div[3]);
				double desejado= Double.parseDouble(div[4]);

				if(ndvi>1){	ndvi=1;}
				if(ndvi<-1){ndvi=-1;}
				if(evi>1){	evi=1;}
				if(evi<-1){	evi=-1;}


				vetorPadroes.add(arit.transpostaVetor(vet));
				vetorDesejados.add(desejado);
				linha = leitura.readLine();
			}
			MAX =  vetorDesejados.size() *10; */
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		double[][] wfinal=trainingPerceptron(eta, vetorPadroes, vetorDesejados, vetorPesos, limiar);
		System.out.println("\n\nWfinal");
		arit.imprimirMatriz(wfinal);
		double[] vetorExec={-1,-0.252236,0.097401};
		int x=execPerceptron(wfinal, vetorExec, limiar);
		System.out.println("classe: "+x);

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
	public static double[][] trainingPerceptron(double eta , ArrayList<double[][]> vetorPadroes, ArrayList<Double> vetorDesejados, ArrayList<double[][]> vetorPesos, double limiar ){


		double u=0;
		double[][] esqF=new double[vetorPesos.get(0).length][(vetorPesos.get(0))[0].length]; 
		double[][] dirF= new double[vetorPesos.get(0).length][(vetorPesos.get(0))[0].length]; 
		double[][] resultF= new double[vetorPesos.get(0).length][(vetorPesos.get(0))[0].length]; 
		double[][] wfinal=new double[vetorPesos.get(0).length][(vetorPesos.get(0))[0].length]; 
		int padr�es=1;
		int n=0;
		int parada=0;
		int m=0;
		while(parada<vetorPadroes.size()){

			/*if(wfinal[0][0]>1 || wfinal[1][0]>1 || wfinal[2][0]>1 || wfinal[0][0]<-1 || wfinal[1][0]<-1 || wfinal[2][0]<-1 ){
					System.err.println("Estorou o limite do Vetor de Pesos");
					return wfinal;

				}*/
/*			if(m==MAX){

				System.err.println("Estorou o limite de ("+MAX+") itera��es");
				return wfinal;
			}
			*/
			System.out.println("\n n: "+n);
			System.out.println("\n ite: "+m);


			if(n==vetorPadroes.size()){
				n=0;
			}

			System.out.println("------------------- w("+(m+1)+") --------------------");
			if(vetorDesejados.get(n)==1){
				System.out.println("F(u): 1.0 - "+(arit.fun��oU(vetorPadroes.get(n), vetorPesos.get(m), limiar)));
				u=Math.floor(1-(arit.fun��oU(vetorPadroes.get(n), vetorPesos.get(m), limiar)));
			}
			else{
				System.out.println("F(u): 0.0 - "+(arit.fun��oU(vetorPadroes.get(n), vetorPesos.get(m), limiar)));
				u=Math.floor(0-(arit.fun��oU(vetorPadroes.get(n), vetorPesos.get(m), limiar)));
			}
			System.out.println("u:"+u+"* padr�o("+vetorDesejados.get(n)+")");
			arit.imprimirMatriz(vetorPadroes.get(n));
			 
			dirF= arit.setarMatriz(dirF, (arit.multiplica��oConstMatriz(u, vetorPadroes.get(n)))); 

			System.out.println("� Igual");
				arit.imprimirMatriz(dirF);

				System.out.println("Eta:"+eta+"* dir � igual:");
			 
			esqF= arit.setarMatriz(esqF, (arit.multiplica��oConstMatriz(eta, dirF)));
			arit.imprimirMatriz(esqF);

				System.out.println("Mais + Vetor de pesos:");
				arit.imprimirMatriz(vetorPesos.get(m));
			 		
			System.out.println("Resultado final W("+(m+2)+")");
			wfinal=resultF = arit.setarMatriz(resultF ,(arit.somaMxM(esqF, vetorPesos.get(m), resultF)));
			arit.imprimirMatriz(resultF);
			vetorPesos.add(m+1, resultF);
			arit.reta(vetorPadroes.get(n), wfinal, limiar, vetorPesos);
			parada=arit.funcaoErro(u,  parada);
			n+=1;
			m+=1;

		}
		return wfinal;
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
		double u= arit.fun��oU(arit.transpostaVetor(vetorExec), wfinal, limiar);
		return (int) u;
	}
}
