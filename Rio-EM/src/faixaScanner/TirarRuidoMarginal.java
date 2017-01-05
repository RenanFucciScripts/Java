package faixaScanner;

import java.io.File;
import java.util.Stack;

import javax.sound.sampled.LineUnavailableException;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

import metodos.MetodosRF;

public class TirarRuidoMarginal {
	@SuppressWarnings("unused")
	public static void main(String[] args) throws LineUnavailableException {
		TirarRuidoMarginal fMarginal = new TirarRuidoMarginal();
	}

	public TirarRuidoMarginal() throws LineUnavailableException {
		// TODO Auto-generated constructor stub
		try{
			MetodosRF methods =  new MetodosRF();
			String dir = "C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2015-08-17\\imagensFaixaScanner\\";

			NeuralNetwork neuralNetwork = NeuralNetwork.createFromFile(dir+"ruidoMarginal (SIGMOID 50000 s text amost new).nnet");

			for (double db: neuralNetwork.getWeights()) {
				System.out.println(db);
			}

			Stack<File> pilhaArqs =  new Stack<File>();
			File fl =  new File(dir+"teste3\\");
			pilhaArqs=	methods.empilharArquivosDiretorio(fl, pilhaArqs);

			for (File file : pilhaArqs) {
				System.out.println("----------- "+file.getName().substring(0, file.getName().length()-4)+" -----------");
				int[][] imgOrigR = methods.leImagem(dir+file.getName(), "Red");
				int[][] imgOrigG = methods.leImagem(dir+file.getName(), "Green");
				int[][] imgOrigB = methods.leImagem(dir+file.getName(), "Blue");
				
				int[][] imgNewR= new int[imgOrigR.length][imgOrigR[0].length];
				int[][] imgNewG = new int[imgOrigR.length][imgOrigR[0].length];
				int[][] imgNewB = new int[imgOrigR.length][imgOrigR[0].length];

				for (int i = 0; i < imgNewR.length; i++) {
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
				methods.gravarImagemColorida(imgNewR, imgNewG, imgNewB,dir,"teste3\\"+file.getName().substring(0, file.getName().length()-4)+" (s ruido marg).jpg" );
			}
			MetodosRF.fazerSom();
		}catch(Exception e){
			System.err.println(e.getLocalizedMessage());
			e.printStackTrace();
			MetodosRF.fazerSom();
		}
	}
}
