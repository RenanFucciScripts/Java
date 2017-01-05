package mlp;

import java.io.BufferedReader;
import java.io.FileReader;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;

import padrao.MetodosRNA;



public class Ndvi_Evi_Ndwi {
	static final TransferFunctionType funcAtiv =  TransferFunctionType.STEP;
	static final int qntEntradas = 3;  
	static final String pasta = "Ndvi_Evi_Ndwi";
	public static void main(String[] args) {
		try {

			MetodosRNA methds = new MetodosRNA();
			MultiLayerPerceptron neuralNetwork = new MultiLayerPerceptron(funcAtiv, qntEntradas, 10, 1);
			BackPropagation bk =  new BackPropagation();
			bk.setMaxError(0.01);
			bk.setMaxIterations(1000000000);
			
			DataSet trainingSet =  new DataSet(qntEntradas,1);

			System.out.println("--------------------- Treinamento ----------------------");
			//String dirArq =  "C:/Users/Renan Fucci/Desktop/amostras.txt";
			/** LaDesP
			String dirArq =  "D:/Usuarios/renan.fucci/Desktop/Imagens/Amostras/amostrasClasse0.txt";
			String dirArq1 =  "D:/Usuarios/renan.fucci/Desktop/Imagens/Amostras/amostrasClasse1.txt";
			 */
			String dirArq =  "C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/Amostras/amostrasClasse0.txt";
			String dirArq1 =  "C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/Amostras/amostrasClasse1.txt";


			FileReader arq =  new FileReader(dirArq);
			BufferedReader leitura =  new BufferedReader(arq);

			FileReader arq1 =  new FileReader(dirArq1);
			BufferedReader leitura1 =  new BufferedReader(arq1);


			String linha= leitura.readLine();
			String[] div;
			System.out.println(".");
			String linha1= leitura1.readLine();
			String[] div1;

			int contAlternado=0;

			double ndvi;
			double evi;
			double nir;
			double ndwi;
			while(true){
				if(contAlternado%2==0){
					if(linha!=null){
						div=linha.split(",");
						ndvi = Double.parseDouble(div[2]);
						evi = Double.parseDouble(div[3]);
						nir =  Double.parseDouble(div[4]);
						ndwi = Double.parseDouble(div[5]);
						
						double[] vet={ndvi, evi,ndwi};
						
						trainingSet.addRow (new DataSetRow (vet, new double[]{0}));
						linha = leitura.readLine();
						//System.out.println("0");
					}
					contAlternado+=1;
				}else{
					if(linha1!=null){
						div1=linha1.split(",");
						ndvi = Double.parseDouble(div1[2]);
						evi = Double.parseDouble(div1[3]);
						nir= Double.parseDouble(div1[4]);
						ndwi = Double.parseDouble(div1[5]);

						double[] vet={ndvi, evi,ndwi};
						trainingSet. addRow (new DataSetRow (vet, new double[]{1}));
						linha1 = leitura1.readLine();
						//System.out.println("1");
					}else{break;}
					contAlternado+=1;
				}
			}
			System.out.println(".");

			/**
			 * Treinamento
			 * */

			neuralNetwork.learn(trainingSet,bk);

			neuralNetwork.save("C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/Execucao/MLP/"+pasta+"/"+pasta+".nnet");
			//neuralNetwork.save("D:/Usuarios/renan.fucci/Desktop/Imagens/Execucao/NDVI, EVI e NDWI/ndvi_evi_ndwi.nnet");
			for (double  db : neuralNetwork.getWeights()) {
				System.out.println(db);
			}

			System.out.println(".");

			/**
			 * Execução
			 * */
			System.out.println("--------------------- Execução ----------------------");

			//String dirCodrasa="D:/Usuarios/renan.fucci/Desktop/Imagens/Execucao/CODRASA.txt";
			String dirCodrasa="C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/Execucao/CODRASA.txt";

			FileReader read= new FileReader(dirCodrasa);
			BufferedReader codrasa =  new BufferedReader(read);

			String linha2= codrasa.readLine();
			String[] div2;
			int[][] imgCodrasa= new int[2501][2501];

			while(linha2!=null){

				div2=linha2.split(",");
				int i= Integer.parseInt(div2[0]);
				int j= Integer.parseInt(div2[1]);
				ndvi = Double.parseDouble(div2[2]);
				evi = Double.parseDouble(div2[3]);
				nir =  Double.parseDouble(div2[4]);
				ndwi = Double.parseDouble(div2[5]);

				DataSet testingSet = new DataSet( qntEntradas, 1);
				double[] vetDescritores={ndvi, evi,ndwi};
				testingSet.addRow(new DataSetRow(vetDescritores, new double[]{0}));
				int novaIntensidade = testNeuralNetwork(neuralNetwork, testingSet);
				imgCodrasa[i][j]=novaIntensidade;
				linha2 =codrasa.readLine();
			}
			//methds.gravarImagem(imgCodrasa, "D:/Usuarios/renan.fucci/Desktop/Imagens/Execucao/NDVI, EVI e NDWI/", "imgcodrasa");
			methds.gravarImagem(imgCodrasa, "C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/Execucao/MLP/"+pasta+"/", "imgcodrasa");


			//String dirsaolourenco="D:/Usuarios/renan.fucci/Desktop/Imagens/Execucao/SAO_LOURENCO.txt";
			String dirsaolourenco="C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/Execucao/SAO_LOURENCO.txt";

			FileReader readsaolourenco= new FileReader(dirsaolourenco);
			BufferedReader saolourenc =  new BufferedReader(readsaolourenco);

			String linha3= saolourenc.readLine();
			String[] div3;
			int[][] imgsaolourneco= new int[2139][973];

			while(linha3!=null){
				div3=linha3.split(",");
				int i = Integer.parseInt(div3[0]);
				int j = Integer.parseInt(div3[1]);
				ndvi = Double.parseDouble(div3[2]);
				evi = Double.parseDouble(div3[3]);
				nir =  Double.parseDouble(div3[4]);
				ndwi = Double.parseDouble(div3[5]);

				DataSet testingSet = new DataSet(qntEntradas, 1);
				double[] vetDescritores={ndvi, evi,ndwi};
				testingSet.addRow(new DataSetRow(vetDescritores, new double[]{0}));
				int novaIntensidade = testNeuralNetwork(neuralNetwork, testingSet);


				imgsaolourneco[i][j]= novaIntensidade;

				linha3 =saolourenc.readLine();
			}
			//	methds.gravarImagem(imgsaolourneco, "D:/Usuarios/renan.fucci/Desktop/Imagens/Execucao/NDVI, EVI e NDWI/", "imgsaolourenco");
			methds.gravarImagem(imgsaolourneco, "C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/Execucao/MLP/"+pasta+"/", "imgsaolourenco");

		}catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getLocalizedMessage());
		}
	}

	public static int testNeuralNetwork(NeuralNetwork nnet, DataSet testSet) {
		double[] networkOutput = null;
		int novaIntensidade;
		for (DataSetRow dataRow : testSet.getRows()) {
			nnet.setInput(dataRow.getInput());
			nnet.calculate();
			networkOutput = nnet.getOutput();
			//System.out.print("Input: " + Arrays.toString(dataRow.getInput()));
			//System.out.print(" Output: " + Arrays.toString(networkOutput));
			//System.out.println(" Pixel: " + intensidadeSaida);
		}

		//	        if(networkOutput[0] <= 0.9)
		//	            novaIntensidade = 0;
		//	        else
		//	            novaIntensidade = 255;
		return (int) (255 * networkOutput[0]);
	}

}
