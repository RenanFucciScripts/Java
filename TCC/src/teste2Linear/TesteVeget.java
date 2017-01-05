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


public class TesteVeget {


	static TransferFunctionType fAtiv =  TransferFunctionType.STEP;
	static int entradas =4; 
	static String indices ="NDVI, EVI, NDWI, NIR";
	//static String indices ="NDVI, EVI";
	//static String indices ="NDVI, EVI e NDWI";
	//static String indices ="NDVI, EVI, NIR";
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try {

			TesteVeget cls = new TesteVeget();
			MetodosRNA methds = new MetodosRNA();
			

			File f =  new File("C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/SaoLourenco Bandas Spectrais/Classificacao/"+indices+"/"+cls.getClass().getSimpleName()); 
			f.mkdir();

			String dirsaolourenco="C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/Execucao/SAO_LOURENCO.txt";

			NeuralNetwork<?> neuralNetwork1= new Perceptron(entradas, 1, fAtiv);
			DataSet trainingSet1 =  new DataSet(entradas,1);



			String dirArq1 = "C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/SaoLourenco Bandas Spectrais/amostraAgua.txt";
			FileReader arq1 =  new FileReader(dirArq1);
			BufferedReader leitura1 =  new BufferedReader(arq1);
			String linha1 =  leitura1.readLine();
			String[] div1; 

			while(linha1!=null){
				div1=linha1.split(",");
				double ndvi = Double.parseDouble(div1[2]);
				double evi = Double.parseDouble(div1[3]);
				double nir =  Double.parseDouble(div1[4]);
				double ndwi = Double.parseDouble(div1[5]);


				double[] vet ={ndvi,evi,ndwi, nir};
				trainingSet1.addRow (new DataSetRow (vet, new double[]{1}));
				linha1 = leitura1.readLine();
				//System.out.println("0");
			}



			System.out.println("--------------------- Treinamento Vegeta ----------------------");

			neuralNetwork1.learn(trainingSet1);

			neuralNetwork1.save("C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/SaoLourenco Bandas Spectrais/Classificacao/"+indices+"/"+cls.getClass().getSimpleName()+"/agua ("+fAtiv.getTypeLabel()+").nnet");
			//neuralNetwork.save("D:/Usuarios/renan.fucci/Desktop/Imagens/Execucao/NDVI, EVI e NDWI/ndvi_evi_ndwi.nnet");
			for (double  db : neuralNetwork1.getWeights()) {
				System.out.println(db);
			}


			System.out.println("--------------------- Execução Vegeta ----------------------");


			FileReader readsaolourencoAgua= new FileReader(dirsaolourenco);
			BufferedReader buffSaolourencAgua =  new BufferedReader(readsaolourencoAgua);

			String linha3= buffSaolourencAgua.readLine();
			String[] div3;

			int[][] imgsaolournecoAgua= new int[2139][973];

			while(linha3!=null){
				div3=linha3.split(",");
				int i = Integer.parseInt(div3[0]);
				int j = Integer.parseInt(div3[1]);
				double ndvi = Double.parseDouble(div3[2]);
				double evi = Double.parseDouble(div3[3]);
				double nir =  Double.parseDouble(div3[4]);
				double ndwi = Double.parseDouble(div3[5]);

				DataSet testingSet = new DataSet(entradas, 1);
				double[] vetDescritores={ndvi,evi,ndwi, nir};
				testingSet.addRow(new DataSetRow(vetDescritores, new double[]{0}));
				int novaIntensidade = testNeuralNetwork(neuralNetwork1, testingSet);

				imgsaolournecoAgua[i][j]= novaIntensidade;
				linha3 =buffSaolourencAgua.readLine();
			}
			methds.gravarImagemGreen(imgsaolournecoAgua, "C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/SaoLourenco Bandas Spectrais/Classificacao/"+indices+"/"+cls.getClass().getSimpleName()+"/", "vegeta ("+fAtiv.getTypeLabel()+")");
		}catch(Exception ex){
			ex.printStackTrace();
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
