package faixaScanner;

import java.io.BufferedReader;
import java.io.FileReader;

import javax.sound.sampled.LineUnavailableException;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;

import metodos.MetodosRF;

public class RemovePerceptronText {


	public static void main(String[] args) {
		String dir="C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2015-08-17\\"
				+"imagensFaixaScanner\\";
		String nomeArq="AmosRuidosMargFinal.txt";
		int maxIte=500;
/*		TransferFunctionType[] transFunction =TransferFunctionType.values();
		for (TransferFunctionType tp : transFunction) {
			System.out.println(tp.name());
			iniciarRemocao(dir, nomeArq, tp, maxIte);

		}
*/		TransferFunctionType transfunction = TransferFunctionType.SIN;
		iniciarRemocao(dir, nomeArq, transfunction, maxIte);
		try {
			 MetodosRF.fazerSom();
		 } catch (LineUnavailableException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		 }
	}

	public static void iniciarRemocao(String dir, String nomeArq, TransferFunctionType transfunction, int maxIte){
		try{
			MetodosRF methods = new MetodosRF();
			
			FileReader fr = new FileReader(dir+nomeArq);
			BufferedReader bf = new BufferedReader(fr);
			int in=6;
			MultiLayerPerceptron neuralNetwork1 = new MultiLayerPerceptron(transfunction,in, 5, 1);
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
					double red= Double.parseDouble(div[2]);
					double green= Double.parseDouble(div[3]);
					double blue= Double.parseDouble(div[4]);
					int textR =  Integer.parseInt(div[5]);
					int textG =  Integer.parseInt(div[6]);
					int textB =  Integer.parseInt(div[7]);

					double vet[]={red,green,blue,textR,textG,textB};
					trainingSet.addRow(new DataSetRow(vet, new double[]{0}));
					cont+=1;
					linha=bf.readLine();
				}
				else{
					/*RUIDO*/
					div=linha.split(",");
					double red= Double.parseDouble(div[2]);
					double green= Double.parseDouble(div[3]);
					double blue= Double.parseDouble(div[4]);
					int textR =  Integer.parseInt(div[5]);
					int textG =  Integer.parseInt(div[6]);
					int textB =  Integer.parseInt(div[7]);

					double vet[]={red,green,blue,textR,textG,textB};
					trainingSet.addRow(new DataSetRow(vet, new double[]{1}));
					cont+=1;
					linha=bf.readLine();
				}
			}
			bf.close();
			neuralNetwork1.learn(trainingSet,bk);
			neuralNetwork1.save(dir+"ruidoMarginal ("+transfunction.name()+") .nnet");
			for (double db: neuralNetwork1.getWeights()) {
				System.out.println(db);
			}

			/*Execucao*/
			int[][] imgOrigR = methods.leImagem(dir+"img20150814_20045845.jpg", "Red");
			int[][] imgOrigG = methods.leImagem(dir+"img20150814_20045845.jpg", "Green");
			int[][] imgOrigB = methods.leImagem(dir+"img20150814_20045845.jpg", "Blue");

			int[][] janela =  new int[3][3];

			int[][] imgNewR = new int[imgOrigR.length][imgOrigR[0].length];
			int[][] imgNewG = new int[imgOrigR.length][imgOrigR[0].length];
			int[][] imgNewB = new int[imgOrigR.length][imgOrigR[0].length];

			int[][] imgPixelR =  new int[janela.length][janela.length];
			int[][] imgPixelG =  new int[janela.length][janela.length];
			int[][] imgPixelB =  new int[janela.length][janela.length];

			double nv=0;
			for (int i = (int) Math.floor(janela.length/2); i < imgOrigR.length - (int) Math.floor(janela.length/2); i++) {
				for (int j = (int) Math.floor(janela.length/2); j < imgOrigR[i].length -(int) Math.floor(janela.length/2); j++) {
					imgPixelR =  new int[janela.length][janela.length];
					imgPixelG =  new int[janela.length][janela.length];
					imgPixelB =  new int[janela.length][janela.length];
					for (int k = - (int) Math.floor(janela.length/2); k <= (int) Math.floor(janela.length/2) ; k++) {
						for (int l = - (int) Math.floor(janela.length/2); l <= (int) Math.floor(janela.length/2); l++) {
							imgPixelR[k+(int) Math.floor(janela.length/2)][l+(int) Math.floor(janela.length/2)]=imgOrigR[i+k][j+l];
							imgPixelG[k+(int) Math.floor(janela.length/2)][l+(int) Math.floor(janela.length/2)]=imgOrigG[i+k][j+l];
							imgPixelB[k+(int) Math.floor(janela.length/2)][l+(int) Math.floor(janela.length/2)]=imgOrigB[i+k][j+l];
						}
					}
					double red=imgOrigR[i][j]/255.0;
					double green=imgOrigG[i][j]/255.0;
					double blue= imgOrigB[i][j]/255.0;
					int textR= methods.getLBPVizinhos(imgPixelR);
					int textG= methods.getLBPVizinhos(imgPixelG);
					int textB= methods.getLBPVizinhos(imgPixelB);
					//System.out.println(textR+", "+textG+", "+textB);
					DataSet testingSet = new DataSet(in,1);
					double vetExec[] = {red,green,blue, textR, textG, textB};
					//System.out.println("Exec M: "+media+"\tV: "+var+"\tD: "+dp);	
					testingSet.addRow(new DataSetRow(vetExec, new double[]{0}));
					double novaIntensidade= testNeuralNetwork(neuralNetwork1, testingSet);

					//if(novaIntensidade<=0.5){ // Limiar Ramp
					//if(novaIntensidade<=0.4){ // Limiar Sigmoid
					//if(novaIntensidade>=0.7){ // Limiar TANH
					//if(novaIntensidade<=0.6){ // Limiar Gaussian
					if(novaIntensidade>=0.1){ // Limiar SIN
						imgNewR[i][j]=255;
						imgNewG[i][j]=255;
						imgNewB[i][j]=255;
					}
					else{
						imgNewR[i][j]=imgOrigR[i][j];
						imgNewG[i][j]=imgOrigG[i][j];
						imgNewB[i][j]=imgOrigB[i][j];
					}
					/*if(nv!=novaIntensidade){
						nv=novaIntensidade;
						System.out.println(novaIntensidade);
					}
					 */				
				}
			}
			methods.gravarImagemColorida(imgNewR, imgNewG, imgNewB, dir, ("img20150814_20045845 ("+transfunction.name()+").jpg"));
			MetodosRF.fazerSom();
		}catch(Exception e){
			e.printStackTrace();
			System.err.println(e.getLocalizedMessage());
		}
	}

	public static double testNeuralNetwork(NeuralNetwork nnet, DataSet testSet) {
		double[ ] networkOutput=null;
		for (DataSetRow dataRow : testSet.getRows()) {
			nnet.setInput(dataRow.getInput());
			nnet.calculate();
			networkOutput = nnet.getOutput();
			//	System.out.print("Input: " + Arrays.toString(dataRow.getInput()) );
			//System.out.println(" Output: " + Arrays.toString(networkOutput) );	 
		}
		//	System.out.println(String.format(Locale.US,"%.4f", networkOutput[0]));
		return networkOutput[0];
		//return (int) (255* networkOutput[0]);
	}
}
