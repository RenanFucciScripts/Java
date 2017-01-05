package teste2MLP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;

import padrao.MetodosRNA;


public class Vegeta {
	static TransferFunctionType fAtiv =TransferFunctionType.SIGMOID;	
	static int entradas =3; 
	static long startTrain;
	static long startExec;
	//static String indices ="NDVI, EVI, NDWI, NIR";
	//static String indices ="NDVI, EVI";
	//static String indices ="NDVI, EVI e NDWI";
	static String indices ="NDVI, EVI, NIR";
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try {
			Vegeta cls = new Vegeta();
			MetodosRNA methds = new MetodosRNA();
			startTrain= System.currentTimeMillis();
			MultiLayerPerceptron neuralNetwork1 = new MultiLayerPerceptron(fAtiv,entradas, 10, 1);
			/*
			Neuron n =  new Neuron();
			n.getTransferFunction();	
			TransferFunction transferFunction = null;
			n.setTransferFunction(transferFunction);
			 */
			BackPropagation bk =  new BackPropagation();
			bk.setMaxError(0.0);
			bk.setMaxIterations(5000);

			File f =  new File("C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/SaoLourenco Bandas Spectrais/Classificacao/"+indices+"/"+cls.getClass().getSimpleName()); 
			f.mkdir();

			DataSet trainingSet1 =  new DataSet(entradas,1);

			String dirArq1 = "C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/SaoLourenco Bandas Spectrais/amostraAgua.txt";
			FileReader arq1 =  new FileReader(dirArq1);
			BufferedReader leitura1 =  new BufferedReader(arq1);
			String linha1 =  leitura1.readLine();
			String[] div1; 

			String dirArq2 = "C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/SaoLourenco Bandas Spectrais/amostraSolo.txt";
			FileReader arq2 =  new FileReader(dirArq2);
			BufferedReader leitura2 =  new BufferedReader(arq2);
			String linha2 =  leitura2.readLine();
			String[] div2; 

			String dirArq3 = "C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/SaoLourenco Bandas Spectrais/amostraVeget.txt";
			FileReader arq3 =  new FileReader(dirArq3);
			BufferedReader leitura3 =  new BufferedReader(arq3);
			String linha3 =  leitura3.readLine();
			String[] div3; 

			int contAgua=0;
			int contSolo=0;
			int contVegeta=0;


			int cont=0;
			String testeAgua = "";
			String testeSolo = "";
			String testeVegeta = "";
			while(true){
				if(cont==0){
					if(linha1==null){
						System.out.println("Ultima linha agua: "+testeAgua);
						System.out.println("Ultima linha Solo: "+testeSolo);
						System.out.println("Ultima linha vegeta: "+testeVegeta);
						break;
					}
					else{
						/*-------------- AGUA -------------------*/
						div1=linha1.split(",");
						double ndvi = Double.parseDouble(div1[2]);
						double evi = Double.parseDouble(div1[3]);
						double nir =  Double.parseDouble(div1[4]);
						double ndwi = Double.parseDouble(div1[5]);

						if(indices.contentEquals("NDVI, EVI")==true){
							double[] vet ={evi,ndvi};
							trainingSet1.addRow (new DataSetRow (vet, new double[]{0}));
						}
						else if(indices.contentEquals("NDVI, EVI, NIR")==true){
							double[] vet ={evi,ndvi, nir};
							trainingSet1.addRow (new DataSetRow (vet, new double[]{0}));
						} 
						else if(indices.contentEquals("NDVI, EVI e NDWI")==true){
							double[] vet ={evi,ndwi, ndvi};
							trainingSet1.addRow (new DataSetRow (vet, new double[]{0}));
						}
						else if(indices.contentEquals("NDVI, EVI, NDWI, NIR")==true){
							double[] vet ={evi,ndwi, ndvi, nir};
							trainingSet1.addRow (new DataSetRow (vet, new double[]{0}));
						}
						testeAgua=linha1;
						linha1 = leitura1.readLine();
						//	System.out.println("0");
						contAgua+=1;
					}
					cont+=1;
				}
				if(cont==1){
					if(linha2==null){
						//System.out.println("Ultima linha Solo: "+testeSolo);

					}
					if(linha2!=null){
						/*-------------- SOLO -------------------*/
						div2=linha2.split(",");
						double ndvi = Double.parseDouble(div2[2]);
						double evi = Double.parseDouble(div2[3]);
						double nir =  Double.parseDouble(div2[4]);
						double ndwi = Double.parseDouble(div2[5]);

						if(indices.contentEquals("NDVI, EVI")==true){
							double[] vet ={evi,ndvi};
							trainingSet1.addRow (new DataSetRow (vet, new double[]{1}));
						}
						else if(indices.contentEquals("NDVI, EVI, NIR")==true){
							double[] vet ={evi,ndvi, nir};
							trainingSet1.addRow (new DataSetRow (vet, new double[]{1}));
						} 
						else if(indices.contentEquals("NDVI, EVI e NDWI")==true){
							double[] vet ={evi,ndwi, ndvi};
							trainingSet1.addRow (new DataSetRow (vet, new double[]{1}));
						}
						else if(indices.contentEquals("NDVI, EVI, NDWI, NIR")==true){
							double[] vet ={evi,ndwi, ndvi, nir};
							trainingSet1.addRow (new DataSetRow (vet, new double[]{1}));
						}
						testeSolo=linha2;
						linha2 = leitura2.readLine();
						//		System.out.println("1-a");
						contSolo+=1;
					}
					cont+=1;
				}
				else {
					if(linha3==null){
						//System.out.println("Ultima linha vegeta: "+testeVegeta);

					}
					if(linha3!=null){
						/*-------------- VEGETACAO -------------------*/
						div3=linha3.split(",");
						double ndvi = Double.parseDouble(div3[2]);
						double evi = Double.parseDouble(div3[3]);
						double nir =  Double.parseDouble(div3[4]);
						double ndwi = Double.parseDouble(div3[5]);

						if(indices.contentEquals("NDVI, EVI")==true){
							double[] vet ={evi,ndvi};
							trainingSet1.addRow (new DataSetRow (vet, new double[]{1}));
						}
						else if(indices.contentEquals("NDVI, EVI, NIR")==true){
							double[] vet ={evi,ndvi, nir};
							trainingSet1.addRow (new DataSetRow (vet, new double[]{1}));
						} 
						else if(indices.contentEquals("NDVI, EVI e NDWI")==true){
							double[] vet ={evi,ndwi, ndvi};
							trainingSet1.addRow (new DataSetRow (vet, new double[]{1}));
						}
						else if(indices.contentEquals("NDVI, EVI, NDWI, NIR")==true){
							double[] vet ={evi,ndwi, ndvi, nir};
							trainingSet1.addRow (new DataSetRow (vet, new double[]{1}));
						}
						testeVegeta= linha3;
						linha3 = leitura3.readLine();
						//System.out.println("1-b");
						contVegeta+=1;
					}

					cont=0;
				}
			}



			System.out.println("--------------------- Treinamento Vegeta ----------------------");

			System.out.println("Cont Agua: "+contAgua);
			System.out.println("Cont Solo: "+contSolo);
			System.out.println("Cont Vegeta: "+contVegeta);

			neuralNetwork1.learn(trainingSet1,bk);

			neuralNetwork1.save("C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/SaoLourenco Bandas Spectrais/Classificacao/"+indices+"/"+cls.getClass().getSimpleName()+"/vegeta ("+fAtiv.getTypeLabel()+").nnet");
			//neuralNetwork.save("D:/Usuarios/renan.fucci/Desktop/Imagens/Execucao/NDVI, EVI e NDWI/ndvi_evi_ndwi.nnet");
			for (double  db : neuralNetwork1.getWeights()) {
				System.out.println(db);
			}

			long elappsedTimeTrain = System.currentTimeMillis() - startTrain;
			double segtrain = elappsedTimeTrain/1000.0;
			System.out.println("--------------------- Execução Vegeta ----------------------");


			String dirsaolourenco="C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/Execucao/SAO_LOURENCO.txt";
			FileReader readsaolourencoVegeta= new FileReader(dirsaolourenco);
			BufferedReader buffSaolourencVegeta =  new BufferedReader(readsaolourencoVegeta);

			String linha4= buffSaolourencVegeta.readLine();
			String[] div4;

			int[][] imgsaolournecoVegeta= new int[2139][973];

			while(linha4!=null){
				div4=linha4.split(",");
				int i = Integer.parseInt(div4[0]);
				int j = Integer.parseInt(div4[1]);
				double ndvi = Double.parseDouble(div4[2]);
				double evi = Double.parseDouble(div4[3]);
				double nir =  Double.parseDouble(div4[4]);
				double ndwi = Double.parseDouble(div4[5]);

				DataSet testingSet = new DataSet(entradas, 1);
				if(indices.contentEquals("NDVI, EVI")==true){
					double[] vetDescritores={evi,ndvi};
					testingSet.addRow (new DataSetRow (vetDescritores, new double[]{0}));
				}
				else if(indices.contentEquals("NDVI, EVI, NIR")==true){
					double[] vetDescritores={evi,ndvi, nir};
					testingSet.addRow (new DataSetRow (vetDescritores, new double[]{0}));
				} 
				else if(indices.contentEquals("NDVI, EVI e NDWI")==true){
					double[] vetDescritores={evi,ndwi, ndvi};
					testingSet.addRow (new DataSetRow (vetDescritores, new double[]{0}));
				}
				else if(indices.contentEquals("NDVI, EVI, NDWI, NIR")==true){
					double[] vetDescritores={evi,ndwi, ndvi, nir};
					testingSet.addRow (new DataSetRow (vetDescritores, new double[]{0}));
				}
				int novaIntensidade = testNeuralNetwork(neuralNetwork1, testingSet);

				imgsaolournecoVegeta[i][j]= novaIntensidade;
				linha4 =buffSaolourencVegeta.readLine();
			}
			long elapsedTime = System.currentTimeMillis()- startExec;
			double segexec = elapsedTime/1000.0;
			System.out.println("Tempo: "+(elapsedTime/1000.0)+" segundos");
			methds.gravarImagemGreen(imgsaolournecoVegeta, "C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/SaoLourenco Bandas Spectrais/Classificacao/"+indices+"/"+cls.getClass().getSimpleName()+"/", "vegeta ("+fAtiv.getTypeLabel()+") train="+segtrain+" exe="+segexec);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public static int testNeuralNetwork(NeuralNetwork<BackPropagation> nnet, DataSet testSet) {
		double[] networkOutput = null;
		for (DataSetRow dataRow : testSet.getRows()) {
			nnet.setInput(dataRow.getInput());
			nnet.calculate();
			networkOutput = nnet.getOutput();
		}
		return (int) (255* networkOutput[0]);
	}
}