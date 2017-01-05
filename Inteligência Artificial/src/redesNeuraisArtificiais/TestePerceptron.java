package redesNeuraisArtificiais;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class TestePerceptron {
	static AritméticaMatricial arit =  new AritméticaMatricial();
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
	public static double[][] trainingPerceptron(double eta , ArrayList<double[][]> vetorPadroes, ArrayList<Double> vetorDesejados, ArrayList<double[][]> vetorPesos, double limiar ){


		double u=0;
		double[][] esqF=new double[vetorPesos.get(0).length][(vetorPesos.get(0))[0].length]; 
		double[][] dirF= new double[vetorPesos.get(0).length][(vetorPesos.get(0))[0].length]; 
		double[][] resultF= new double[vetorPesos.get(0).length][(vetorPesos.get(0))[0].length]; 
		double[][] wfinal=new double[vetorPesos.get(0).length][(vetorPesos.get(0))[0].length]; 
		int padrões=1;
		int n=0;
		int parada=0;
		int m=0;
		while(parada<vetorPadroes.size()){

			/*if(wfinal[0][0]>1 || wfinal[1][0]>1 || wfinal[2][0]>1 || wfinal[0][0]<-1 || wfinal[1][0]<-1 || wfinal[2][0]<-1 ){
					System.err.println("Estorou o limite do Vetor de Pesos");
					return wfinal;

				}*/
/*			if(m==MAX){

				System.err.println("Estorou o limite de ("+MAX+") iterações");
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
				System.out.println("F(u): 1.0 - "+(arit.funçãoU(vetorPadroes.get(n), vetorPesos.get(m), limiar)));
				u=Math.floor(1-(arit.funçãoU(vetorPadroes.get(n), vetorPesos.get(m), limiar)));
			}
			else{
				System.out.println("F(u): 0.0 - "+(arit.funçãoU(vetorPadroes.get(n), vetorPesos.get(m), limiar)));
				u=Math.floor(0-(arit.funçãoU(vetorPadroes.get(n), vetorPesos.get(m), limiar)));
			}
			System.out.println("u:"+u+"* padrão("+vetorDesejados.get(n)+")");
			arit.imprimirMatriz(vetorPadroes.get(n));
			 
			dirF= arit.setarMatriz(dirF, (arit.multiplicaçãoConstMatriz(u, vetorPadroes.get(n)))); 

			System.out.println("é Igual");
				arit.imprimirMatriz(dirF);

				System.out.println("Eta:"+eta+"* dir é igual:");
			 
			esqF= arit.setarMatriz(esqF, (arit.multiplicaçãoConstMatriz(eta, dirF)));
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
	 * <b>Método execPerceptron</b>
	 * Esse método vai fazer a execução do Perceptron Linear após sua etapa de treinamento.
	 * Essse método utiliza-se do método <b>"funcaoU"<\b>.
	 * @param wfinal[][](double) é a transposta do vetor de pesos já treinado. 
	 * @param vetorExec[](double) é vetor de caracteristica que deve ser classificado.
	 * @param limiar(double) é a constante limiar, neste caso da função degrau.
	 * @return u(int) é classe correspondente ao vetor de caracteristica a ser classificado.
	 * */
	public static int execPerceptron(double[][] wfinal, double[] vetorExec, double limiar){
		double u= arit.funçãoU(arit.transpostaVetor(vetorExec), wfinal, limiar);
		return (int) u;
	}
}
