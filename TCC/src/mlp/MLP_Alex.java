package mlp;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.Perceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;


public class MLP_Alex {


	public static void main(String[] args) {

		// create training set (logical XOR function)
		DataSet trainingSet = new DataSet(4, 1);
		//	        trainingSet.addRow(new DataSetRow(new double[]{0, 0}, new double[]{0}));
		//	        trainingSet.addRow(new DataSetRow(new double[]{0, 1}, new double[]{1}));
		//	        trainingSet.addRow(new DataSetRow(new double[]{1, 0}, new double[]{1}));
		//	        trainingSet.addRow(new DataSetRow(new double[]{1, 1}, new double[]{0}));

		int contadorElementos, quantidadeAmostras;

		quantidadeAmostras = 100000;
		// create training set (logical XOR function)
		try {
			FileReader amostraC0 = new FileReader("D:/Usuarios/renan.fucci/Desktop/Imagens/Amostras/amostrasClasse0.txt");
			FileReader amostraC1 = new FileReader("D:/Usuarios/renan.fucci/Desktop/Imagens/Amostras/amostrasClasse1.txt");
			BufferedReader lerArqC0 = new BufferedReader(amostraC0);
			BufferedReader lerArqC1 = new BufferedReader(amostraC1);

			String[] linha0 = lerArqC0.readLine().split(","); // lê a primeira linha
			String[] linha1 = lerArqC1.readLine().split(","); // lê a primeira linha

			// a variável "linha" recebe o valor "null" quando o processo
			// de repetição atingir o final do arquivo texto
			int contaElementosTeste = 0;
			while (contaElementosTeste < quantidadeAmostras && (linha0 != null && linha1 != null)) {
				//System.out.println(contaElementosTeste);
				contaElementosTeste++;
				// monta o vetor de características e de saida para um elemento da classe 0
				double vetDescritores[] = new double[linha0.length - 3];
				for (contadorElementos = 2; contadorElementos < linha0.length - 1; contadorElementos++) {
					vetDescritores[contadorElementos - 2] = Double.parseDouble(linha0[contadorElementos]);
					//System.out.println(vetDescritores[contadorElementos-2] + " -> "+Double.parseDouble(linha0[linha0.length-1]));
				}
				trainingSet.addRow(new DataSetRow(vetDescritores, new double[]{Double.parseDouble(linha0[linha0.length - 1])}));

				// monta o vetor de características e de saida para um elemento da classe 1
				double vetDescritoresC1[] = new double[linha0.length - 3];
				for (contadorElementos = 2; contadorElementos < linha1.length - 1; contadorElementos++) {
					vetDescritoresC1[contadorElementos - 2] = Double.parseDouble(linha1[contadorElementos]);
					//System.out.println(vetDescritoresC1[contadorElementos-2] + " -> "+Double.parseDouble(linha1[linha1.length-1]));
				}
				trainingSet.addRow(new DataSetRow(vetDescritoresC1, new double[]{Double.parseDouble(linha1[linha1.length - 1])}));

				linha0 = lerArqC0.readLine().split(","); // lê da segunda até a última linha
				linha1 = lerArqC1.readLine().split(","); // lê da segunda até a última linha
			}

			amostraC0.close();
			amostraC1.close();
		} catch (IOException e) {
			System.err.printf("Erro na abertura do arquivo: %s.\n",
					e.getMessage());
		}

		// create multi layer perceptron
		//MultiLayerPerceptron myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.RAMP, 4, 10, 1);
		NeuralNetwork  myMlPerceptron =  new Perceptron(4,1,TransferFunctionType.SIGMOID);
		// learn the training set
		BackPropagation bk = new BackPropagation();
		//bk.setMaxIterations(1000000000);
		bk.setMaxError(0);

		myMlPerceptron.learn(trainingSet);

		// imprime os pesos
		System.out.println("-------------------------------------- PESOS -------------------");
		for (int contaPesos = 0; contaPesos < myMlPerceptron.getWeights().length; contaPesos++) {
			System.out.println("Peso: " + contaPesos + " -> " + myMlPerceptron.getWeights()[contaPesos]);
		}
		System.out.println("-------------------------------------- PESOS -------------------");

		// test perceptron
		System.out.println("Testing trained neural network");
		try {
			FileReader dadosExecucao = new FileReader("D:/Usuarios/renan.fucci/Desktop/Imagens/Execucao/CODRASA.txt");
			BufferedReader lerArqC0 = new BufferedReader(dadosExecucao);

			String linha0 = lerArqC0.readLine(); // lê a primeira linha

			// a variável "linha" recebe o valor "null" quando o processo
			// de repetição atingir o final do arquivo texto
			BufferedImage img = new BufferedImage(2500, 2500, BufferedImage.TYPE_INT_RGB);
			while ((linha0 != null)) {
				// monta o vetor de características e de saida para um elemento da classe 0
				String vetLinha[] = linha0.split(",");
				double vetDescritores[] = new double[vetLinha.length - 2];
				for (contadorElementos = 2; contadorElementos < vetLinha.length; contadorElementos++) {
					vetDescritores[contadorElementos - 2] = Double.parseDouble(vetLinha[contadorElementos]);
					//System.out.println(vetDescritores[contadorElementos-2] + " -> "+Double.parseDouble(linha0[linha0.length-1]));
				}
				DataSet testingSet = new DataSet(4, 1);
				testingSet.addRow(new DataSetRow(vetDescritores, new double[]{0}));
				int novaIntensidade = testNeuralNetwork(myMlPerceptron, testingSet);
				img.setRGB(Integer.parseInt(vetLinha[1]) - 1, Integer.parseInt(vetLinha[0]) - 1, new Color(novaIntensidade, novaIntensidade, novaIntensidade).getRGB());
				linha0 = lerArqC0.readLine(); // lê da segunda até a última linha
			}
			ImageIO.write(img, "PNG", new File("../classificacao.png"));
			dadosExecucao.close();

		} catch (IOException e) {
			System.err.printf("Erro na abertura do arquivo: %s.\n",
					e.getMessage());
		}

		//testNeuralNetwork(myMlPerceptron, testingSet);
		// save trained neural network
		myMlPerceptron.save("myMlPerceptron.nnet");

		// load saved neural network
		//	        NeuralNetwork loadedMlPerceptron = NeuralNetwork.createFromFile("myMlPerceptron.nnet");
		// test loaded neural network
		//	        System.out.println("Testing loaded neural network");
		//	        testNeuralNetwork(loadedMlPerceptron, trainingSet);
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

