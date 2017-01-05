package perceptronLinear;

import java.io.BufferedReader;
import java.io.FileReader;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.Perceptron;
import org.neuroph.util.TransferFunctionType;

import padrao.MetodosRNA;



public class NDVI_EVI_NDWI {
	public static void main(String[] args) {
		try {

			MetodosRNA methds = new MetodosRNA();
			NeuralNetwork neuralNetwork= new Perceptron(3, 1, TransferFunctionType.RAMP);
			DataSet trainingSet =  new DataSet(3,1);

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

			while(true){
				if(contAlternado%2==0){
					if(linha!=null){
						div=linha.split(",");
						double ndvi = Double.parseDouble(div[2]);
						double evi = Double.parseDouble(div[3]);
						double indNir =  Double.parseDouble(div[4]);
						double ndwi = Double.parseDouble(div[5]);


						double[] vet ={ndvi,evi,ndwi};
						trainingSet.addRow (new DataSetRow (vet, new double[]{0}));
						linha = leitura.readLine();
						//System.out.println("0");
					}
					contAlternado+=1;
				}else{
					if(linha1!=null){
						div1=linha1.split(",");
						double ndvi = Double.parseDouble(div1[2]);
						double evi = Double.parseDouble(div1[3]);
						double indNir= Double.parseDouble(div1[4]);
						double ndwi = Double.parseDouble(div1[5]);

						double[] vet ={ndvi,evi,ndwi};
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
			
			neuralNetwork.learn(trainingSet);

			neuralNetwork.save("C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/Execucao/NDVI, EVI e NDWI/ndvi_evi_ndwi.nnet");
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
				double ndvi = Double.parseDouble(div2[2]);
				double evi = Double.parseDouble(div2[3]);
				double indNir =  Double.parseDouble(div2[4]);
				double ndwi = Double.parseDouble(div2[5]);

				/*//	System.out.println("ndvi: "+ndvi+", evi:"+evi);
				neuralNetwork.setInput(ndvi,evi);
				neuralNetwork.calculate();
				for (double ks : neuralNetwork.getOutput()) {
				//	System.out.println("c1:"+ks);
					imgCodrasa[i][j]= (int) (255*ks) ;
				}		*/
				DataSet testingSet = new DataSet( 3, 1);
				double[] vetDescritores={ndvi,evi,ndwi};
				testingSet.addRow(new DataSetRow(vetDescritores, new double[]{0}));
				int novaIntensidade = testNeuralNetwork(neuralNetwork, testingSet);
				imgCodrasa[i][j]=novaIntensidade;
				linha2 =codrasa.readLine();
			}
			//methds.gravarImagem(imgCodrasa, "D:/Usuarios/renan.fucci/Desktop/Imagens/Execucao/NDVI, EVI e NDWI/", "imgcodrasa");
			methds.gravarImagem(imgCodrasa, "C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/Execucao/NDVI, EVI e NDWI/", "imgcodrasa");
			
			
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
				double ndvi = Double.parseDouble(div3[2]);
				double evi = Double.parseDouble(div3[3]);
				double indNir =  Double.parseDouble(div3[4]);
				double ndwi = Double.parseDouble(div3[5]);

				DataSet testingSet = new DataSet(3, 1);
				double[] vetDescritores={ndvi,evi,ndwi};
				testingSet.addRow(new DataSetRow(vetDescritores, new double[]{0}));
				int novaIntensidade = testNeuralNetwork(neuralNetwork, testingSet);

				//System.out.println("ndvi: "+ndvi+", evi:"+evi);
				/*neuralNetwork.setInput(ndvi,evi);
				neuralNetwork.calculate();*/
				/*for (double ks : neuralNetwork.getOutput()) {
					System.out.println("c2:"+ks);
					imgsaolourneco[i][j]= (int) (255*ks) ;

				 */	

				//}
				imgsaolourneco[i][j]= novaIntensidade;

				linha3 =saolourenc.readLine();
			}
		//	methds.gravarImagem(imgsaolourneco, "D:/Usuarios/renan.fucci/Desktop/Imagens/Execucao/NDVI, EVI e NDWI/", "imgsaolourenco");
			methds.gravarImagem(imgsaolourneco, "C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/Execucao/NDVI, EVI e NDWI/", "imgsaolourenco");

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
