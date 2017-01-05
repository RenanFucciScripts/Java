/**
 * 
 */
package metodos;



import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import boofcv.abst.filter.blur.BlurFilter;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.factory.filter.blur.FactoryBlurFilter;
import boofcv.struct.image.ImageFloat32;
import metodos.MetodosEM.JanelasConv;

/**
 * @author <a href="mailto:renanfucci@hotmail.com"> Renan Fucci </a>
 */

public class MetodosRF {

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo filtroGaussianBoofCV<br></b>
	 * Metodo para aplicar o filtro gaussiano da biblioteca BoofCV em uma imagem.
	 * @param img imagem a ser processada;
	 * @param radius tamanho do raio.  
	 * @return saida imagem com o filtro.
	 */
	private static BufferedImage filtroGaussianBoofCV (BufferedImage imagem, int radius){
		ImageFloat32 input = ConvertBufferedImage.convertFromSingle(imagem , null, ImageFloat32.class);
		ImageFloat32 output = new ImageFloat32(input.width, input.height);

		BlurFilter<ImageFloat32> filter =  FactoryBlurFilter.gaussian(ImageFloat32.class, 1, radius);
		filter.process(input, output);
		BufferedImage saida =ConvertBufferedImage.convertTo(output, new BufferedImage(imagem.getWidth(), imagem.getHeight(), 5));

		return saida;
	}


	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo filtroGaussianBoofCV<br></b>
	 * Metodo para aplicar o filtro da mediana com janela de convolucao em uma imagem.
	 * @param matriz imagem a ser processada;
	 * @param janelaConv tamanho da janela de convolucao.  
	 * @return matrizResultado imagem com o filtro.
	 */
	public static int[][] filtroMedianaRF(int matriz[][],JanelasConv janelaConv){
		int matrizAux[][] = new int[matriz.length][matriz[0].length];
		int matrizResultado[][]=new int[matriz.length][matriz[0].length];
		int janela[][]= new int[janelaConv.getValue()][janelaConv.getValue()];

		for (int g=0; g<janela.length;g++){
			for (int h=0; h<janela[g].length;h++){
				janela[g][h]=1;
			}
		}
		for (int i=0; i<matriz.length; i++){
			for(int j=0; j<matriz[i].length;j++){
				matrizAux[i][j]=matriz[i][j];
			}
		}
		int vetor[]=new int[janelaConv.getValue()*janelaConv.getValue()];
		int mediana = 0;
		int x;
		int cont;
		for (int k=(int) Math.floor((janela.length/2)); k<matrizAux.length-(int) Math.floor((janela.length/2));k++){
			for (int l=(int) Math.floor((janela.length/2));l<matrizAux[0].length-(int) Math.floor((janela.length/2));l++){
				cont=0;
				for (int m= -(int) Math.floor((janela.length/2));m<=(int) Math.floor((janela.length/2));m++){
					for (int n= -(int) Math.floor((janela.length/2));n<=(int) Math.floor((janela.length/2));n++){
						vetor[cont]=janela[m+(int) Math.floor((janela.length/2))][(int) Math.floor((janela.length/2))+n] * matrizAux[k+m][l+n];
						cont+=1;
						Arrays.sort(vetor);
						x= (int) Math.ceil(vetor.length/2);
						mediana=vetor[x];
					}
				}
				matrizResultado[k][l]=mediana;
			}
		}
		return matrizResultado;
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo filtroGaussianBoofCV<br></b>
	 * Metodo para aplicar o filtro da media da biblioteca BoofCV em uma imagem.
	 * @param img imagem a ser processada;
	 * @param radius tamanho do raio.  
	 * @return saida imagem com o filtro.
	 */
	private static BufferedImage filtroMediaBoofCV(BufferedImage imagem, int radius){
		ImageFloat32 input = ConvertBufferedImage.convertFromSingle(imagem , null, ImageFloat32.class);
		ImageFloat32 output = new ImageFloat32(input.width, input.height);

		BlurFilter<ImageFloat32> filter =  FactoryBlurFilter.mean(ImageFloat32.class, radius);
		filter.process(input, output);
		BufferedImage saida =ConvertBufferedImage.convertTo(output, new BufferedImage(imagem.getWidth(), imagem.getHeight(), 5));

		return saida;
	}


	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo buffToMatriz<br></b>
	 * Metodo para converter um BufferedImage para uma matriz;
	 * @param buff imagem a ser convertida.
	 * @return imgSaida imagem no formado de matriz.
	 *
	 */
	public int[][] buffToMatriz(BufferedImage buff, String bandaRGB) throws Exception {
		int[][] imgOut = new int[buff.getHeight()][buff.getWidth()];
		for (int i = 0; i < buff.getHeight(); i++) {
			for (int j = 0; j < buff.getWidth(); j++) {
				if(bandaRGB.contentEquals("Red")){
					imgOut[i][j] = new Color(buff.getRGB(j, i)).getRed();
				}
				else if(bandaRGB.contentEquals("Green")){
					imgOut[i][j] = new Color(buff.getRGB(j, i)).getGreen();
				}
				else if(bandaRGB.contentEquals("Blue")){
					imgOut[i][j] = new Color(buff.getRGB(j, i)).getGreen();
				}
			}
		}
		return imgOut;
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo colorToRGB<br></b>
	 * Metodo para converter 1(UM) pixel R, G, B, A para o um padrao de 8 bits.
	 * @param alpha canal alpha do pixel.
	 * @param red canal red do pixel.
	 * @param green canal green do pixel.
	 * @param blue canal blue do pixel.
	 * @return newPixel pixel no formato de 8 bits.
	 *
	 */
	private static int colorToRGB(int alpha, int red, int green, int blue) throws Exception {
		int newPixel = 0;
		newPixel += alpha;
		newPixel = newPixel << 8;
		newPixel += red;
		newPixel = newPixel << 8;
		newPixel += green;
		newPixel = newPixel << 8;
		newPixel += blue;
		return newPixel;
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo empilharArquivosDiretorio</b>
	 * Metodo para fazer pilha de arquivos de um diretorio, todos os arquivos na extensao JPG.
	 * <p> dentro das pastas e subpastas do diretorio.  </p>
	 * @param folder diretorio a ser empilhado;
	 * @param pilhaArqs varialvel na qual serao empilhados os arquivos.
	 * @return pilhaArqs variavel de arquivos empilhadas.
	 */
	public Stack<File> empilharArquivosDiretorio(final File folder, Stack<File> pilhaArqs) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				empilharArquivosDiretorio(fileEntry, pilhaArqs);
			} else {
				//if(FilenameUtils.getExtension(fileEntry.getName()).contentEquals("jpg") || FilenameUtils.getExtension(fileEntry.getName()).contentEquals("png"))
					pilhaArqs.push(fileEntry);

			}
		}
		return pilhaArqs;
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo getMinMax<br></b>
	 * Metodo para encontrar o valor minimo e maximo de um vetor;
	 * @param vetor
	 * @return vetMinMax[](int) vetor com minimo e maximo de um vetor, no qual a posicao vet[0] eh o min e o vet[1] eh o max.
	 */
	private static int[] getMinMax(int[] vetor) throws Exception {
		int min=vetor[0], max=vetor[vetor.length-1];
		for (int i = 0; i < vetor.length; i++) {
			if(vetor[i]>max){
				max=vetor[i];
			}else if(vetor[i]<= min){
				min=vetor[i];
			}
		}
		int[] vetMinMax={min,max};
		return vetMinMax;
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo imageHistogram</b>
	 * Metodo para fazer um histograma em tons de cinza a partir de um imagem
	 *
	 * @param input BufferImage a ser processado.
	 * @return histogram vetor de histograma em tons de cinza.
	 *
	 */
	private static int[] imageHistogram(BufferedImage input) {
		int[] histogram = new int[256];
		for (int i = 0; i < histogram.length; i++) {
			histogram[i] = 0;
		}

		for (int i = 0; i < input.getWidth(); i++) {
			for (int j = 0; j < input.getHeight(); j++) {
				int red = new Color(input.getRGB(i, j)).getRed();
				histogram[red]++;
			}
		}
		return histogram;
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo sequenciaRealces<br></b>
	 * Metodo para fazer uma sequencia predefinidas de realces nas imagens, e a cada passo dessa sequencia,
	 * tenta ler o QRCode desta imagem.
	 * @param imagem(BufferImage) a ser realcada.
	 * @return strQRCode String contendo ou nao o QRCode da imagem.
	 */
	private  String sequenciaRealces(BufferedImage imagem) {
		String strQRCode="";
		String charset = "UTF-8"; // or "ISO-8859-1"
		try{
			Map<DecodeHintType, Object> hintMap = new HashMap<DecodeHintType, Object>();
			hintMap.put(DecodeHintType.TRY_HARDER, true);
			strQRCode=readQRCode(imagem, charset, hintMap);
			if(strQRCode.length()>13 && strQRCode.length()<16){
				return strQRCode;
			}
			BufferedImage imgRealce;
			imgRealce = realceEM(imagem);
			strQRCode = readQRCode(imgRealce, charset, hintMap);
			if(strQRCode.length()>13 && strQRCode.length()<16){
				return strQRCode;
			}
			imgRealce =  realceBinario(imagem);
			strQRCode =readQRCode(imgRealce, charset, hintMap);
			if(strQRCode.length()>13 && strQRCode.length()<16){
				return strQRCode;
			}
			imgRealce = realceEM(realceBinario(imagem));
			strQRCode =readQRCode(imgRealce, charset, hintMap);
			if(strQRCode.length()>13 && strQRCode.length()<16){
				return strQRCode;
			}
		}catch(Exception ex1){
			return "QRCode Não encontrado!";
		}
		return "QRCode Não encontrado!";
	}


	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo lerQRCodeImagem<br></b>
	 * Metodo para fazer leitura de QRCode de uma imagem .
	 * @param imagem imagem(BufferImage) a ser encontrada.
	 * @return strQRCode String contendo ou nao o QRCode da imagem.
	 */
	public String lerQRCodeImagem(BufferedImage imagem){
		BufferedImage buffAux = new BufferedImage(imagem.getWidth(), imagem.getHeight(), 5);
		String strQRCode="";
		try{
			strQRCode=sequenciaRealces(imagem);
			if(strQRCode.length()>13 && strQRCode.length()<16){
				return strQRCode;
			}

			System.out.println("Filtro Gaussiano Janela de Convolução 3");
			buffAux=filtroGaussianBoofCV(imagem, 3);
			strQRCode=sequenciaRealces(buffAux);
			if(strQRCode.length()>13 && strQRCode.length()<16){
				return strQRCode;
			}
			System.out.println("Filtro Gaussiano Janela de Convolução 5");
			buffAux=filtroGaussianBoofCV(imagem, 5);
			strQRCode=sequenciaRealces(buffAux);
			if(strQRCode.length()>13 && strQRCode.length()<16){
				return strQRCode;
			}
			System.out.println("Filtro Media Janela de Convolução 3\n\n");
			buffAux=filtroMediaBoofCV(imagem,3);
			strQRCode=sequenciaRealces(buffAux);
			if(strQRCode.length()>13 && strQRCode.length()<16){
				return strQRCode;
			}
		}catch(Exception ex4){
			return "QRCode Não encontrado!";
		}
		return "QRCode Não encontrado!";
	}


	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b> Metodo limiarBinario</b>
	 * Metodo para calcular para achar limiar binario utilizando codigo de <b><i>Otsu</i></b>.<br> 
	 * @param original BufferImage a ser calculado
	 * @return threshold limiar binario da imagem.
	 *
	 */
	private static int limiarBinario(BufferedImage original) {

		int[] histogram = imageHistogram(original);
		int total = original.getHeight() * original.getWidth();
		float sum = 0;
		for (int i = 0; i < 256; i++) {
			sum += i * histogram[i];
		}
		float sumB = 0;
		int wB = 0;
		int wF = 0;

		float varMax = 0;
		int threshold = 0;

		for (int i = 0; i < 256; i++) {
			wB += histogram[i];
			if (wB == 0) {
				continue;
			}
			wF = total - wB;

			if (wF == 0) {
				break;
			}

			sumB += (float) (i * histogram[i]);
			float mB = sumB / wB;
			float mF = (sum - sumB) / wF;

			float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

			if (varBetween > varMax) {
				varMax = varBetween;
				threshold = i;
			}
		}
		return threshold;
	}


	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo matrizToBufferImage </b>
	 * Metodo para converter uma matriz em BufferedImage
	 *
	 * @param matriz[][](int) matriz a ser convertida.
	 * @return saida(BufferedImage)
	 *
	 */
	public static BufferedImage matrizCinzaToBufferImage(int[][] matriz) throws Exception {
		BufferedImage saida = new BufferedImage(matriz[0].length, matriz.length, 5);
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[i].length; j++) {
				saida.setRGB(j, i, new Color(matriz[i][j], matriz[i][j], matriz[i][j]).getRGB());
			}
		}
		return saida;
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo matrizToBufferImage </b>
	 * Metodo para converter uma matriz em BufferedImage
	 *
	 * @param matriz[][](int) matriz a ser convertida.
	 * @return saida(BufferedImage)
	 *
	 */
	public static BufferedImage matrizCinzaToBufferImage(double[][] matriz) throws Exception {
		BufferedImage saida = new BufferedImage(matriz[0].length, matriz.length, 5);
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[i].length; j++) {
				saida.setRGB(j, i, new Color((int) matriz[i][j], (int) matriz[i][j],(int) matriz[i][j]).getRGB());
			}
		}
		return saida;
	}


	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo matrizToBufferImage </b>
	 * Metodo para converter tres matrizes (RGB) em BufferedImage
	 *
	 * @param matriz[][](int) matriz a ser convertida.
	 * @return saida(BufferedImage)
	 *
	 */
	public BufferedImage matrizColorToBufferImage(int[][] matrizR, int[][] matrizG, int[][] matrizB) throws Exception {
		BufferedImage saida = new BufferedImage(matrizR[0].length, matrizR.length, 5);
		for (int i = 0; i < matrizR.length; i++) {
			for (int j = 0; j < matrizR[i].length; j++) {
				saida.setRGB(j, i, new Color(matrizR[i][j], matrizG[i][j], matrizB[i][j]).getRGB());
			}
		}
		return saida;
	}




	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b> Metodo mediaLinhaVetor</b>
	 * Metodo para calcular a media de todos as posicoes de um vetor. Esse
	 * metodo eh utilizado dentro do @see {@link realceEM()}
	 *
	 * @param vetor vetor a ser calculado
	 * @param qntdLinhasBrancas divisor do calculo de media
	 * @return vetor vetor calculado, com a media de todas as posicoes
	 *
	 */
	private static double[] mediaLinhaVetor(double[] vetor, int qntdLinhasBrancas) throws Exception {
		for (int i = 0; i < vetor.length; i++) {
			vetor[i] = vetor[i] / qntdLinhasBrancas;
		}
		return vetor;
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo readQRCode<br></b>
	 * Metodo para fazer a leitura de um QRCODE em uma imagem utilizando a biblioteca <a href="https://github.com/zxing/zxing"> ZXING<\a> em uma imagem.
	 * @param imagem a ser lida;
	 * @param charset padrao de codificacao;
	 * @param hintMap variavel de configuracao de leitura exigida pela biblioteca.
	 * @return String contendo ou nao o QRCode da imagem.
	 */
	private static String readQRCode(BufferedImage imagem, String charset, Map<DecodeHintType,Object> hintMap)
			throws FileNotFoundException, IOException, NotFoundException {
		BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
				new BufferedImageLuminanceSource(imagem)));
		Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,
				hintMap);
		return qrCodeResult.getText();
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo realceBinario<br></b>
	 * Metodo para fazer realce binario em uma imagem.
	 * @param original imagem a ser realcada.
	 * @return binarized imagem realcada em formato binario.
	 *
	 */
	private static BufferedImage realceBinario(BufferedImage original) throws Exception {
		int red;
		int newPixel;
		int threshold = limiarBinario(original);
		BufferedImage binarized = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
		for (int i = 0; i < original.getWidth(); i++) {
			for (int j = 0; j < original.getHeight(); j++) {
				red = new Color(original.getRGB(i, j)).getRed();
				int alpha = new Color(original.getRGB(i, j)).getAlpha();
				if (red > threshold) {
					newPixel = 255;
				} else {
					newPixel = 0;
				}
				newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
				binarized.setRGB(i, j, newPixel);

			}
		}
		return binarized;

	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo realceEM</b>
	 * Metodo para realce de uma imagem;
	 *
	 * @param img imagem a ser realcada.
	 * @return imgSaida imagem ja realcada.
	 *
	 */
	private BufferedImage realceEM(BufferedImage img) throws Exception {
		int[][] arrayGrayScale = buffToMatriz(img, "Green");

		int[][] saidaGrayScale = new int[arrayGrayScale.length][arrayGrayScale[0].length];
		double[] novaLinha = new double[img.getWidth()];
		double[] saidaLinha = new double[img.getWidth()];

		int[] minMax;
		int diferenca = 0;

		int countLinhasBrancas = 0;
		for (int i = 1; i < Math.abs(img.getHeight() * 0.10); i++) {
			minMax= getMinMax(arrayGrayScale[i]);
			diferenca = minMax[1]- minMax[0];
			if (minMax[1] > 196 && diferenca < 64) {
				countLinhasBrancas += 1;
				for (int j2 = 0; j2 < saidaGrayScale[0].length; j2++) {
					novaLinha[j2] = novaLinha[j2] + (arrayGrayScale[i][j2]);
				}
			}
		}
		novaLinha = mediaLinhaVetor(novaLinha, countLinhasBrancas);
		for (int i = 2; i < img.getHeight() - 1; i++) {
			for (int j = 0; j < saidaLinha.length; j++) {
				saidaLinha[j] = (novaLinha[j] - arrayGrayScale[i][j]) / 255;
			}
			for (int j = 0; j < saidaGrayScale[i].length; j++) {
				if (saidaLinha[j] < 0.1) {
					saidaGrayScale[i][j] = 255;
				} else if (saidaLinha[j] > 0.5) {
					saidaGrayScale[i][j] = 0;

				}
			}
		}
		for (int i = 1; i < img.getHeight() - 2; i++) {
			for (int j = 1; j < saidaGrayScale[0].length - 1; j++) {
				int vizinhosIguais = 0;
				if (saidaGrayScale[i][j - 1] == 0)
					vizinhosIguais += 1;
				else if (saidaGrayScale[i][j + 1] == 0)
					vizinhosIguais += 1;
				else if (saidaGrayScale[i - 1][j] == 0)
					vizinhosIguais += 1;
				else if (saidaGrayScale[i + 1][j] == 0)
					vizinhosIguais += 1;
				if (vizinhosIguais > 2)
					saidaGrayScale[i][j] = 0;
			}
		}
		for (int i = 1; i < img.getHeight() - 1; i++) {
			for (int j = 1; j < saidaGrayScale[0].length - 1; j++) {
				int vizinhosIguais = 0;
				if (saidaGrayScale[i][j - 1] == 255)
					vizinhosIguais += 1;
				else if (saidaGrayScale[i][j + 1] == 255) 
					vizinhosIguais += 1;
				else if (saidaGrayScale[i - 1][j] == 255) 
					vizinhosIguais += 1;
				else if (saidaGrayScale[i + 1][j] == 255) 
					vizinhosIguais += 1;
				if (vizinhosIguais > 2) 
					saidaGrayScale[i][j] = 255;
			}
		}
		BufferedImage imgSaida = matrizCinzaToBufferImage(saidaGrayScale);
		return imgSaida;
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo testNeuralNetwork</b>
	 * Metodo para realizar os testes em uma Rede Neural Artificial.
	 *
	 * @param nnet a rede neural a ser testada.
	 * @param testSet conjunto de dados a ser testado.
	 * @return int	valor inteiro entre 0 e 255.
	 *
	 */
	public  int testNeuralNetwork( NeuralNetwork<?> nnet, DataSet testSet) {
		double[ ] networkOutput=null;
		for (DataSetRow dataRow : testSet.getRows()) {
			nnet.setInput(dataRow.getInput());
			nnet.calculate();
			networkOutput = nnet.getOutput();
		}
		return (int) (255* networkOutput[0]);
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo leImagem</b>
	 * Metodo para ler uma banda, canal RGB, da imagem e transforma-la em matriz;
	 *
	 * @param dirCompleto diretorio completo da imagem.
	 * @param canalRgb nome do canal RGB a ser lido.
	 * @return array canal RGB no formato de matriz.
	 *
	 */
	public static  int[][] leImagem(String dirCompleto, String canalRgb){
		int array[][]=null;
		BufferedImage objeto;
		try {
			objeto = ImageIO.read(new File(dirCompleto));
			array= new int[objeto.getHeight()][objeto.getWidth()];
			for (int i=0; i<objeto.getHeight(); i++){
				for(int j=0; j<objeto.getWidth();j++){
					if(canalRgb=="Red")
						array[i][j]=new Color(objeto.getRGB(j, i)).getRed();
					else if(canalRgb=="Green")
						array[i][j]=new Color(objeto.getRGB(j, i)).getGreen();
					else if(canalRgb=="Blue")
						array[i][j]=new Color(objeto.getRGB(j, i)).getBlue();
				}
			}
			return array;
		} 
		catch (IOException e) {
			e.printStackTrace();
			return array;

		}
	}


	public void LBP(BufferedImage buff){
		try{
			int[][] red= buffToMatriz(buff, "Red");
			int[][] green= buffToMatriz(buff, "Green");
			int[][] blue= buffToMatriz(buff, "Blue");



			int[][] janela= new int[3][3];

			//Pixels da janela de convolução
			int[][] pixelR =  new int[janela.length][janela.length];
			int[][] pixelG =  new int[janela.length][janela.length];
			int[][] pixelB =  new int[janela.length][janela.length];

			//Imagem textura
			int[][] textR = new int[red.length][red[0].length];
			int[][] textG = new int[red.length][red[0].length];
			int[][] textB = new int[red.length][red[0].length];

			//Loop para formar o LBPs
			for (int i = (int) Math.floor(janela.length/2); i < textR.length - (int) Math.floor(janela.length/2); i++) {
				for (int j = (int) Math.floor(janela.length/2); j < textR[i].length - (int) Math.floor(janela.length/2); j++) {
					pixelR =  new int[janela.length][janela.length];
					pixelG =  new int[janela.length][janela.length];
					pixelB =  new int[janela.length][janela.length];
					for (int k = -(int) Math.floor(janela.length/2); k <= (int) Math.floor(janela.length/2); k++) {
						for (int l = -(int) Math.floor(janela.length/2); l <= (int) Math.floor(janela.length/2); l++) {
							pixelR[k+(int) Math.floor(janela.length/2)][l+ (int) Math.floor(janela.length/2)]= red[k+i][l+j];
							pixelG[k+(int) Math.floor(janela.length/2)][l+ (int) Math.floor(janela.length/2)]= green[k+i][l+j];
							pixelB[k+(int) Math.floor(janela.length/2)][l+ (int) Math.floor(janela.length/2)]= blue[k+i][l+j];
						}
					}
					textR[i][j]= getLBPVizinhos(pixelR);
					textG[i][j]= getLBPVizinhos(pixelG);
					textB[i][j]= getLBPVizinhos(pixelB);
				}
			}
		}catch(Exception ex){
			System.err.println(ex.getMessage());
			ex.printStackTrace();
		}
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo getLBPVizinhos</b>
	 * Metodo para pegar o valor do LBP(Linear Binary Pattern), isto e, padrao linear binario de uma matriz.
	 *
	 * @param img matriz que sera calculado o LBP.
	 * @return int valor inteiro correspondente ao binario calculado.
	 *
	 */
	public int getLBPVizinhos(int[][] img) {
		int[][] janela= new int[3][3];
		String binary ="";
		int cent = (int) Math.floor(janela.length/2);
		int centro =  img[cent][cent];

		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < janela.length; j++) {
				if(centro>img[i][j])
					binary+=0;
				else
					binary+=1;
			}
		}

		for (int i = 1; i <janela.length ; i++) {
			for (int j = janela.length-1; j <janela.length ; j++) {
				if(centro>img[i][j])
					binary+=0;
				else
					binary+=1;
			}
		}

		for (int i =janela.length-1 ; i < janela.length; i++) {
			for (int j = janela.length-2; j >= 0; j--) {
				if(centro>img[i][j])
					binary+=0;
				else
					binary+=1;
			}
		}
		for (int i = janela.length-2 ; i > 0; i--) {
			for (int j = 0; j >= 0; j--) {
				if(centro>img[i][j])
					binary+=0;
				else
					binary+=1;
			}
		}
		return Integer.parseInt(binary, 2);
	}

	/**
	 * <b>@author Alex Araujo<br></b>
	 * <b> Metodo realceCabecalho</b>
	 * Metodo para fazer realce nos cabecalhos que estejam muito claros.
	 *
	 * @param buff arquivo imagem que sera feito o realce.
	 * @return buff bufferedImage da imagem realcada.
	 *
	 */
	public  BufferedImage realceCabecalho(BufferedImage buff) throws IOException,Exception {
		int[][] red =buffToMatriz(buff, "Red");
		int[][] green =buffToMatriz(buff, "Green");
		int[][] blue =buffToMatriz(buff,  "Blue");

		int[][] medR =filtroMedianaRF(red, JanelasConv.tres);
		int[][] medG =filtroMedianaRF(green, JanelasConv.tres);
		int[][] medB =filtroMedianaRF(blue, JanelasConv.tres);

		int[] janela= new int[JanelasConv.tres.getValue()];
		int limiarBranco=245;

		for (int i = 0; i < red.length; i++) {
			for (int j = 0; j < red[i].length; j++) {
				if(red[i][j]>limiarBranco && green[i][j]>limiarBranco && blue[i][j]>limiarBranco){
					red[i][j]=medR[i][j];
					green[i][j]=medG[i][j];
					blue[i][j]=medB[i][j];
				}
			}
		}
		int[][] entradaR = eliminarPontosOrfaos(red, janela.length);
		int[][] entradaG = eliminarPontosOrfaos(green, janela.length);
		int[][] entradaB = eliminarPontosOrfaos(blue, janela.length);

		int[][] saidaR=new int[entradaR.length][entradaR[0].length];
		int[][] saidaG=new int[entradaR.length][entradaR[0].length];
		int[][] saidaB=new int[entradaR.length][entradaR[0].length];

		for (int i = 0; i < saidaB.length; i++) {
			for (int j = 0; j < saidaB[i].length; j++) {
				saidaR[i][j]=entradaR[i][j];
				saidaG[i][j]=entradaG[i][j];
				saidaB[i][j]=entradaB[i][j];
			}
		}
		int limiarShadow=93;
		int limiarMiddleTone =245;

		for (int i = 0; i < saidaB.length; i++) {
			for (int j = 0; j < saidaB[i].length; j++) {
				if(entradaR[i][j]<limiarShadow) 
					saidaR[i][j]=0;
				else if(entradaR[i][j]<limiarMiddleTone)
					saidaR[i][j]=(int) (0.03*entradaR[i][j]);
				else
					saidaR[i][j]=entradaR[i][j];
				if(entradaG[i][j]<limiarShadow)
					saidaG[i][j]=0;
				else if(entradaG[i][j]<limiarMiddleTone)
					saidaG[i][j]=(int) (0.03*entradaG[i][j]);
				else
					saidaG[i][j]=entradaG[i][j];
				if(entradaB[i][j]<limiarShadow)
					saidaB[i][j]=0;
				else if(entradaB[i][j]<limiarMiddleTone)
					saidaB[i][j]=(int) (0.03*entradaB[i][j]);
				else
					saidaB[i][j]=entradaB[i][j];
			}

		}

		for (int i = 0; i < saidaB.length; i++) {
			for (int j = 0; j < saidaB[i].length; j++) {
				saidaR[i][j]=(saidaR[i][j]+entradaR[i][j])/2;
				saidaG[i][j]=(saidaG[i][j]+entradaG[i][j])/2;
				saidaB[i][j]=(saidaB[i][j]+entradaB[i][j])/2;

			}
		}

		BufferedImage buff1 = matrizColorToBufferImage(saidaR, saidaG, saidaB);
		return buff1;
	}

	/**
	 * <b>@author Alex Araujo<br></b>
	 * <b> Metodo eliminarPontosOrfaos</b>
	 * Metodo para eliminar os pontos isolados na imagem.
	 *
	 * @param imagem uma das bandas RGB da imagem.
	 * @param tamJanelaConv tamanho da janela de convolucao.
	 * @return imagemSaida uma da bandas da imagem sem os pontos isolados.
	 *
	 */
	private static int[][] eliminarPontosOrfaos(int[][] imagem, int tamJanelaConv) throws Exception{
		int[][] imagemSaida = new int[imagem.length][imagem[0].length];
		int[] janela = new int[tamJanelaConv];

		int numeroVizinhosIguais=0;
		for (int i =(int) Math.floor(janela.length/2); i < imagem.length - (int) Math.floor(janela.length/2); i++) {
			for (int j = (int) Math.floor(janela.length/2); j < imagem[i].length - (int) Math.floor(janela.length/2); j++) {
				numeroVizinhosIguais=0;
				for (int k = -(int) Math.floor(janela.length/2); k <= (int)Math.floor(janela.length/2); k++) {
					for (int l = -(int) Math.floor(janela.length/2); l <=(int)Math.floor(janela.length/2); l++) {
						if(imagem[i+k][j+l]==imagem[i][j])
							numeroVizinhosIguais+=1;
					}
				}
				if (numeroVizinhosIguais>=janela.length) {
					imagemSaida[i][j]=imagem[i][j];
				}
				else{
					int media=0;
					for (int k = -(int) Math.floor(janela.length/2); k <= (int)Math.floor(janela.length/2); k++) {
						for (int l = -(int) Math.floor(janela.length/2); l <=(int)Math.floor(janela.length/2); l++) {
							media+=imagem[i+k][j+l];
						}
					}
					media= (int) Math.abs(media/((janela.length*janela.length)));
					imagemSaida[i][j]=media;
				}
			}
		}
		return imagemSaida;
	}


	/**
	 * <b>@author Renan Fucci</b><br>
	 * <b>Metodo readImage</b>
	 * Metodo para ler uma imagem em um diretorio e transforma-la num {@link BufferedImage}.
	 * @param dirCompletoComExtens diretorio completo com a extensao do arquivo.
	 * @return um {@link BufferedImage}.
	 *
	 */
	public BufferedImage readImage(String dirCompletoComExtens){ 
		try {
			return ImageIO.read(new File(dirCompletoComExtens));
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage()
					+ "\nCertifique-se que o diretorio esta correto e com a mesma extensao do arquivo.");
		}
		return null;
	}

	/**
	 * <b>@author Renan Fucci</b><br>
	 * <b>Metodo writeImage</b>
	 * Metodo para gravar uma imagem em um diretorio.
	 * @param dirCompletoComExtens diretorio completo com a extensao do arquivo.
	 * @return boolean com o resultado da gravacao do arquivo.
	 *
	 */
	public boolean writeImage(BufferedImage image, String dirCompletoComExtens ){ 
		try {
			return ImageIO.write(image, FilenameUtils.getExtension(dirCompletoComExtens), new File(dirCompletoComExtens));
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage()
					+ "\nCertifique-se que o diretorio esta correto e com a uma extensao de arquivo.");
		}
		return false;
	}
}