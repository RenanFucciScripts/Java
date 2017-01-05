package faixaScanner;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;

import metodos.MetodosEM;
import metodos.MetodosRF;

public class RemovePerceptronJanela3 {


	public static void main(String[] args) throws LineUnavailableException {
		String dir="C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2015-08-17\\"
				+"imagensFaixaScanner\\";
		String nomeArq="arqAmosRuidosMargJanelaFinal.txt";
		int maxIte=50000;
		TransferFunctionType transfunction =  TransferFunctionType.SIGMOID;
		iniciarRemocao(dir, nomeArq, transfunction, maxIte);
		/*	TransferFunctionType[] transFunction =TransferFunctionType.values();
		for (TransferFunctionType tp : transFunction) {
			iniciarRemocao(dir, nomeArq, tp, maxIte);

		}
		*/
		MetodosRF.fazerSom();
	}

	public static void iniciarRemocao(String dir, String nomeArq, TransferFunctionType transfunction, int maxIte){
		try{
			FileReader fr = new FileReader(dir+nomeArq);
			BufferedReader bf = new BufferedReader(fr);
			int in=3;
			MultiLayerPerceptron neuralNetwork1 = new MultiLayerPerceptron(transfunction,in, 10, 1);
			BackPropagation bk =  new BackPropagation();
			bk.setMaxError(0.0);
			bk.setMaxIterations(maxIte);

			DataSet trainingSet =  new DataSet(in,1);
			String linha= bf.readLine();
			String[] div;
			int cont=0;

			/*Treinamento*/

			while(linha!=null){
				if(cont%2==0){
					/*NÃO RUIDO*/
					div=linha.split(",");
					double media= Double.parseDouble(div[2]);
					double var= Double.parseDouble(div[3]);
					double dp= Double.parseDouble(div[4]);
					//System.out.println("NÃO M: "+media+"\tV: "+var+"\tD: "+dp);	
					double vet[]={media,var,dp};
					trainingSet.addRow(new DataSetRow(vet, new double[]{0}));
					cont+=1;
					linha=bf.readLine();
				}
				else{
					/*RUIDO*/
					div=linha.split(",");
					double media= Double.parseDouble(div[2]);
					double var= Double.parseDouble(div[3]);
					double dp= Double.parseDouble(div[4]);
					//System.out.println("SIM M: "+media+"\tV: "+var+"\tD: "+dp);	
					double vet[]={media,var,dp};
					trainingSet.addRow(new DataSetRow(vet, new double[]{1}));
					cont+=1;
					linha=bf.readLine();
				}
			}
			bf.close();
			neuralNetwork1.learn(trainingSet,bk);
			neuralNetwork1.save(dir+"ruidoMarginal janela ("+transfunction.name()+") .nnet");
			for (double db: neuralNetwork1.getWeights()) {
				System.out.println(db);
			}

			/*Execucao*/
			int[][] imgOrig = MetodosEM.buffToMatriz(ImageIO.read(new File(dir+"img20150814_20045845.jpg")));
			int[][] imgNew = new int[imgOrig.length][imgOrig[0].length];
			int[][] janela =  new int[3][3];

			for (int i =(int) Math.floor(janela.length/2); i < imgOrig.length - (int) Math.floor(janela.length/2); i++) {
				for (int j = (int) Math.floor(janela.length/2); j < imgOrig[i].length-(int) Math.floor(janela.length/2); j++) {
					double somat=0;
					for (int k = -(int) Math.floor(janela.length/2); k <= (int)Math.floor(janela.length/2); k++) {
						for (int l = -(int) Math.floor(janela.length/2); l <= (int) Math.floor(janela.length/2); l++) {
							somat+=imgOrig[i+k][j+l];
						}
					}
					double media=somat/9.0;
					double var=(((int)(somat-media))^2)/255.0;
					double dp= Math.sqrt(var);
					DataSet testingSet = new DataSet(in,1);
					double vetExec[] = {media,var,dp};
					//System.out.println("Exec M: "+media+"\tV: "+var+"\tD: "+dp);	
					testingSet.addRow(new DataSetRow(vetExec, new double[]{0}));
					int novaIntensidade= testNeuralNetwork(neuralNetwork1, testingSet);
					imgNew[i][j]=novaIntensidade;
				}
			}
			BufferedImage imagemFinal=	MetodosEM.matrizToBufferImage(imgNew);
			ImageIO.write(imagemFinal, "jpg", new File(dir+"img20150814_20045845 janela ("+transfunction.name()+").jpg"));
		}catch(Exception e){
			e.printStackTrace();
			System.err.println(e.getLocalizedMessage());
		}
	}

	public static int testNeuralNetwork(NeuralNetwork<BackPropagation> nnet, DataSet testSet) {
		double[ ] networkOutput=null;
		for (DataSetRow dataRow : testSet.getRows()) {
			nnet.setInput(dataRow.getInput());
			nnet.calculate();
			networkOutput = nnet.getOutput();
			//System.out.print("Input: " + Arrays.toString(dataRow.getInput()) );
			//System.out.println(" Output: " + Arrays.toString(networkOutput) );
		}
		//	System.out.println(String.format(Locale.US,"%.4f", networkOutput[0]));
		return (int) (255* networkOutput[0]);
	}
}
