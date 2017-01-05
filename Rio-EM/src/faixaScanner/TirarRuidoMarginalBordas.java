package faixaScanner;

import java.io.File;
import java.util.Stack;

import javax.sound.sampled.LineUnavailableException;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

import metodos.MetodosRF;

public class TirarRuidoMarginalBordas {
	public static void main(String[] args) throws LineUnavailableException {
		long start= System.currentTimeMillis();
		new TirarRuidoMarginalBordas();
		long elapsedTime= System.currentTimeMillis()-start;
		System.out.println((elapsedTime/1000.0)+" segundos");
	}

	public  TirarRuidoMarginalBordas() throws LineUnavailableException {
		// TODO Auto-generated constructor stub
		try{
			int tamBordaInfe =20;
			int tamBordaLados =5;

			MetodosRF methods =  new MetodosRF();
			String dir = "C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2015-08-17\\imagensFaixaScanner\\";

			NeuralNetwork neuralNetwork = NeuralNetwork.createFromFile(dir+"ruidoMarginal (SIGMOID 50000 s text amost new).nnet");

			for (double db: neuralNetwork.getWeights()) {
				System.out.println(db);
			}

			Stack<File> pilhaArqs =  new Stack<File>();
			File fl =  new File(dir+"imagens_Sem_Faixas\\");
			pilhaArqs=	methods.empilharArquivosDiretorio(fl, pilhaArqs);

			for (File file : pilhaArqs) {
				System.out.println("----------- "+file.getName().substring(0, file.getName().length()-4)+" -----------");
				int[][] imgOrigR = methods.leImagem(dir+"imagens_Sem_Faixas\\"+file.getName(), "Red");
				int[][] imgOrigG = methods.leImagem(dir+"imagens_Sem_Faixas\\"+file.getName(), "Green");
				int[][] imgOrigB = methods.leImagem(dir+"imagens_Sem_Faixas\\"+file.getName(), "Blue");

				
				int[][] imgNewR= new int[imgOrigR.length][imgOrigR[0].length];
				int[][] imgNewG = new int[imgOrigR.length][imgOrigR[0].length];
				int[][] imgNewB = new int[imgOrigR.length][imgOrigR[0].length];
				// COPIANDO AS IMAGENS 
				for (int i = 0; i < imgOrigR.length; i++) {
					for (int j = 0; j < imgOrigR[i].length; j++) {
						imgNewR[i][j]=imgOrigR[i][j];
						imgNewG[i][j]=imgOrigG[i][j];
						imgNewB[i][j]=imgOrigB[i][j];
					}
				}
				
				// -----------Borda Superior -------------//
				for (int i = 0; i < tamBordaLados; i++) {
					for (int j = 0; j < imgNewR[i].length; j++) {

						double red=imgOrigR[i][j]/255.0;
						double green=imgOrigG[i][j]/255.0;
						double blue=imgOrigB[i][j]/255.0;
						int in=3;
						DataSet testingSet = new DataSet(in,1);
						double vetExec[] = {red,green,blue};
						//System.out.println("Exec M: "+media+"\tV: "+var+"\tD: "+dp);	
						testingSet.addRow(new DataSetRow(vetExec, new double[]{0}));
						int novaIntensidade= methods.testNeuralNetwork(neuralNetwork, testingSet);
						if(novaIntensidade>200){
							imgNewR[i][j]=255;
							imgNewG[i][j]=255;
							imgNewB[i][j]=255;
						}
						else{
							imgNewR[i][j]=imgOrigR[i][j];
							imgNewG[i][j]=imgOrigG[i][j];
							imgNewB[i][j]=imgOrigB[i][j];
						}
					}
				}
				// -----------Borda Esquerda -------------//
				for (int i = 0; i < imgNewR.length; i++) {
					for (int j = 0; j < tamBordaLados; j++) {

						double red=imgOrigR[i][j]/255.0;
						double green=imgOrigG[i][j]/255.0;
						double blue=imgOrigB[i][j]/255.0;
						int in=3;
						DataSet testingSet = new DataSet(in,1);
						double vetExec[] = {red,green,blue};
						//System.out.println("Exec M: "+media+"\tV: "+var+"\tD: "+dp);	
						testingSet.addRow(new DataSetRow(vetExec, new double[]{0}));
						int novaIntensidade= methods.testNeuralNetwork(neuralNetwork, testingSet);
						if(novaIntensidade>200){
							imgNewR[i][j]=255;
							imgNewG[i][j]=255;
							imgNewB[i][j]=255;
						}
						else{
							imgNewR[i][j]=imgOrigR[i][j];
							imgNewG[i][j]=imgOrigG[i][j];
							imgNewB[i][j]=imgOrigB[i][j];
						}
					}
				}	
				// -----------Borda Inferior -------------//
				for (int i =imgNewR.length-1 ; i > imgNewR.length-tamBordaInfe; i--) {
					for (int j = 0; j < imgNewR[i].length; j++) {
						double red=imgOrigR[i][j]/255.0;
						double green=imgOrigG[i][j]/255.0;
						double blue=imgOrigB[i][j]/255.0;
						int in=3;
						DataSet testingSet = new DataSet(in,1);
						double vetExec[] = {red,green,blue};
						//System.out.println("Exec M: "+media+"\tV: "+var+"\tD: "+dp);	
						testingSet.addRow(new DataSetRow(vetExec, new double[]{0}));
						int novaIntensidade= methods.testNeuralNetwork(neuralNetwork, testingSet);
						if(novaIntensidade>200){
							imgNewR[i][j]=255;
							imgNewG[i][j]=255;
							imgNewB[i][j]=255;
						}
						else{
							imgNewR[i][j]=imgOrigR[i][j];
							imgNewG[i][j]=imgOrigG[i][j];
							imgNewB[i][j]=imgOrigB[i][j];
						}
					}
				}	
				// -----------Borda Direita -------------//
				for (int i = 0; i < imgNewR.length; i++) {
					for (int j =imgNewR[i].length-1 ; j > imgNewR[i].length-tamBordaLados; j--) {
						double red=imgOrigR[i][j]/255.0;
						double green=imgOrigG[i][j]/255.0;
						double blue=imgOrigB[i][j]/255.0;
						int in=3;
						DataSet testingSet = new DataSet(in,1);
						double vetExec[] = {red,green,blue};
						//System.out.println("Exec M: "+media+"\tV: "+var+"\tD: "+dp);	
						testingSet.addRow(new DataSetRow(vetExec, new double[]{0}));
						int novaIntensidade= methods.testNeuralNetwork(neuralNetwork, testingSet);
						if(novaIntensidade>200){
							imgNewR[i][j]=255;
							imgNewG[i][j]=255;
							imgNewB[i][j]=255;
						}
						else{
							imgNewR[i][j]=imgOrigR[i][j];
							imgNewG[i][j]=imgOrigG[i][j];
							imgNewB[i][j]=imgOrigB[i][j];
						}
					}
				}	
				methods.gravarImagemColorida(imgNewR, imgNewG, imgNewB,dir,"imagens_Sem_Faixas\\"+file.getName().substring(0, file.getName().length()-4)+" (s ruido marg bordas).jpg" );
			}
			MetodosRF.fazerSom();
		}catch(Exception e){
			System.err.println(e.getLocalizedMessage());
			e.printStackTrace();
			MetodosRF.fazerSom();
		}
	}
}
