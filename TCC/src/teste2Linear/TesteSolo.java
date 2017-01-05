package teste2Linear;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.Perceptron;
import org.neuroph.util.TransferFunctionType;

import padrao.MetodosRNA;



public class TesteSolo {
	static TransferFunctionType fAtiv =  TransferFunctionType.STEP;
	static int entradas =3;
	static String indices ="NDVI, EVI e NDWI";
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try {
			TesteSolo cls = new TesteSolo();
			MetodosRNA methds = new MetodosRNA();

			File f =  new File("C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/SaoLourenco Bandas Spectrais/Classificacao/"+indices+"/"+cls.getClass().getSimpleName()); 
			f.mkdir();
			NeuralNetwork<?> neuralNetwork= new Perceptron(entradas, 1, fAtiv);
			DataSet trainingSet =  new DataSet(entradas,1);

			String dirsaolourenco="C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/Execucao/SAO_LOURENCO.txt";

			
			System.out.println("--------------------- Leitura Solo ----------------------");

			String dirArq0 = "C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/SaoLourenco Bandas Spectrais/amostraSolo.txt";
			FileReader arq =  new FileReader(dirArq0);
			BufferedReader leitura =  new BufferedReader(arq);

			String linha= leitura.readLine();
			String[] div;

			System.out.println(".");


			while(linha!=null){
				div=linha.split(",");
				double ndvi = Double.parseDouble(div[2]);
				double evi = Double.parseDouble(div[3]);
				double nir =  Double.parseDouble(div[4]);
				double ndwi = Double.parseDouble(div[5]);


				double[] vet ={ndvi,evi,ndwi};
				trainingSet.addRow (new DataSetRow (vet, new double[]{0}));
				linha = leitura.readLine();
				//System.out.println("0");
			}


			System.out.println("--------------------- Treinamento Solo ----------------------");

			neuralNetwork.learn(trainingSet);

			neuralNetwork.save("C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/SaoLourenco Bandas Spectrais/Classificacao/NDVI, EVI e NDWI/"+cls.getClass().getSimpleName()+"/ndvi_evi_ndwi.nnet");
			//neuralNetwork.save("D:/Usuarios/renan.fucci/Desktop/Imagens/Execucao/NDVI, EVI e NDWI/ndvi_evi_ndwi.nnet");
			for (double  db : neuralNetwork.getWeights()) {
				System.out.println(db);
			}


			System.out.println("--------------------- Execução Solo ----------------------");


			FileReader readsaolourencoSolo= new FileReader(dirsaolourenco);
			BufferedReader buffSaolourencSolo =  new BufferedReader(readsaolourencoSolo);

			String linha4= buffSaolourencSolo.readLine();
			String[] div4;

			int[][] imgsaolournecoSolo= new int[2139][973];

			while(linha4!=null){
				div4=linha4.split(",");
				int i = Integer.parseInt(div4[0]);
				int j = Integer.parseInt(div4[1]);
				double ndvi = Double.parseDouble(div4[2]);
				double evi = Double.parseDouble(div4[3]); 
				double nir =  Double.parseDouble(div4[4]);
				double ndwi = Double.parseDouble(div4[5]);

				DataSet testingSet = new DataSet(entradas, 1);
				double[] vetDescritores={ndvi,evi,ndwi};
				testingSet.addRow(new DataSetRow(vetDescritores, new double[]{0}));
				int novaIntensidade = testNeuralNetwork(neuralNetwork, testingSet);
				imgsaolournecoSolo[i][j]= novaIntensidade;

				linha4 =buffSaolourencSolo.readLine();
			}
			methds.gravarImagemRed(imgsaolournecoSolo, "C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/SaoLourenco Bandas Spectrais/Classificacao/NDVI, EVI e NDWI/"+cls.getClass().getSimpleName()+"/", "solo ("+fAtiv.getTypeLabel()+")");

			

		}catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getLocalizedMessage());
		}
	}

	public static int testNeuralNetwork(NeuralNetwork<?> nnet, DataSet testSet) {
		double[] networkOutput = null;
		for (DataSetRow dataRow : testSet.getRows()) {
			nnet.setInput(dataRow.getInput());
			nnet.calculate();
			networkOutput = nnet.getOutput();
		}
		return (int) (255* networkOutput[0]);
	}

}
