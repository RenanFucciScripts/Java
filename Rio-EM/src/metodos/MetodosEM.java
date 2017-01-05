package metodos;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

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


public class MetodosEM {

	static String passo="";

	static int[] comb1= new int[13];
	static double tempoComb1=0.0;
	static double tempoRadiusComb1=0.0;
	static long tempoTotalComb1=0;

	static int[] comb2=new int[13];
	static double tempoComb2=0.0;
	static double tempoRadiusComb2=0.0;
	static long tempoTotalComb2=0;

	static int[] comb3=new int[13];
	static double tempoComb3=0.0;
	static double tempoRadiusComb3=0.0;
	static long tempoTotalComb3=0;

	static int comb4=0;
	static double tempoComb4=0.0;
	static double tempoRadiusComb4=0.0;
	static long tempoTotalComb4=0;
	static int imgReconhecidas=0;

	static int cont1=0, cont2=0;
	public static void main(String[] args) {
		ArrayList<String> nRec = new ArrayList<String>();
		try {
			String strQRCode="";
			String strDir ="C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2015-07-25\\";
			//String strDir = "C:\\Users\\Renan Fucci\\Dropbox\\RIO 45º\\2015-07-25\\";
			File dir =  new File(strDir);
			Stack<File> pilhaArqs = new Stack<File>();
			pilhaArqs=empilharArquivosDiretorio(dir, pilhaArqs);
			//JanelasConv janelasConv = JanelasConv.all;
			//int[] janelas = janelasConv.getJanelas();
			//long start;
			//long elapsedTime;
			for (File file : pilhaArqs) {
				System.out.println(strDir+(file.getName()).substring(0, (file.getName().length()-4)));
				//FileWriter txt = new FileWriter(strDir+(file.getName()).substring(0, (file.getName().length()-4))+".txt");
				BufferedImage buffIMG=toGray(ImageIO.read(file));
				long start = System.currentTimeMillis();
				strQRCode=sequnciaFiltros(buffIMG);
				long elap = System.currentTimeMillis() - start;
				if(tempoComb1==0){
					tempoComb1= elap;
				}else{
					tempoComb1 = ((tempoComb1 + (elap/1000.0))/2);
				}
				if(!strQRCode.substring(0, 4).contentEquals("0000")){
					nRec.add(file.getName());
				}
			}
			System.out.println("Tempo Médio: "+tempoComb1);
			System.out.println("Imagens Não Reconhecidas:");
			for (String str : nRec) {
				System.out.println("\n"+str);
			}
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	/*BufferedImage buffIMG=toGray(ImageIO.read(file));
				BufferedImage buffOut=buffIMG;
				int vez=0;
				for (int i = 0; i < 5 ;i++) {
					if(i==0){
						long tempRadiusInicio=System.currentTimeMillis();
						for (int radius : janelas) {
							txt.write("---->  Media Janela ("+radius+") \n");
							start= System.currentTimeMillis();
							buffOut=filtroMediaBoofCV(buffIMG, radius);
							strQRCode=lerQRCodeImagem(buffOut);
							elapsedTime = System.currentTimeMillis() -start;
							if(strQRCode.substring(0, 4).contentEquals("0000")){
								if(vez==0){
									imgReconhecidas+=1;
									vez+=1;
								}
								comb1[radius]+=1;
								if(tempoComb1==0)
									tempoComb1 = elapsedTime/1000.0;
								else
									tempoComb1 = ((tempoComb1+(elapsedTime/1000.0))/2);
							}
							txt.write("Passo: "+passo);
							txt.write("\nResultado: "+strQRCode);
							txt.write("\nTempo: "+(String.format(Locale.US,"%.6f",(elapsedTime/1000.0)))+"\n\n");

						}
						long elapsedtimeRadius= System.currentTimeMillis() - tempRadiusInicio;
						if(tempoRadiusComb1==0){
							tempoTotalComb1=elapsedtimeRadius;
							tempoRadiusComb1= (elapsedtimeRadius/1000.0);
						}
						else{
							tempoTotalComb1+=elapsedtimeRadius;
							tempoRadiusComb1= ((tempoRadiusComb1+(elapsedtimeRadius/1000.0))/2);
						}
					}
					else if(i==1){
						long tempRadiusInicio=System.currentTimeMillis();
						for (int radius : janelas) {
							txt.write("\n\n---->  Mediana Janela ("+radius+") \n");
							start= System.currentTimeMillis();
							buffOut=filtroMedianaBoofCV(buffIMG, radius);
							strQRCode=lerQRCodeImagem(buffOut);
							elapsedTime = System.currentTimeMillis() -start;
							if(strQRCode.substring(0, 4).contentEquals("0000")){
								if(vez==0){
									imgReconhecidas+=1;
									vez+=1;
								}
								comb2[radius]+=1;
								if(tempoComb2==0)
									tempoComb2 = elapsedTime/1000.0;
								else
									tempoComb2 = ((tempoComb2+(elapsedTime/1000.0))/2);
							}
							txt.write("Passo: "+passo);
							txt.write("\nResultado: "+strQRCode);
							txt.write("\nTempo: "+(String.format(Locale.US,"%.6f",(elapsedTime/1000.0)))+"\n\n");
						}
						long elapsedtimeRadius= System.currentTimeMillis() - tempRadiusInicio;
						if(tempoRadiusComb2==0){
							tempoTotalComb2=elapsedtimeRadius;
							tempoRadiusComb2= (elapsedtimeRadius/1000.0);
						}
						else{
							tempoTotalComb2+=elapsedtimeRadius;
							tempoRadiusComb2= ((tempoRadiusComb2+(elapsedtimeRadius/1000.0))/2);
						}
					}
					else if(i==2){
						long tempRadiusInicio=System.currentTimeMillis();
						for (int radius : janelas) {
							txt.write("\n\n---->  Gaussiana Janela ("+radius+") \n");
							start= System.currentTimeMillis();
							buffOut=filtroGaussianBoofCV(buffIMG, radius);
							strQRCode=lerQRCodeImagem(buffOut);
							elapsedTime = System.currentTimeMillis() -start;
							if(strQRCode.substring(0, 4).contentEquals("0000")){
								if(vez==0){
									imgReconhecidas+=1;
									vez+=1;
								}
								comb3[radius]+=1;
								if(tempoComb3==0)
									tempoComb3 = elapsedTime/1000.0;
								else
									tempoComb3 = ((tempoComb3+(elapsedTime/1000.0))/2);
							}
							txt.write("Passo: "+passo);
							txt.write("\nResultado: "+strQRCode);
							txt.write("\nTempo: "+(String.format(Locale.US,"%.6f",(elapsedTime/1000.0)))+"\n\n");

						}
						long elapsedtimeRadius= System.currentTimeMillis() - tempRadiusInicio;
						if(tempoRadiusComb3==0){
							tempoTotalComb3=elapsedtimeRadius;
							tempoRadiusComb3= (elapsedtimeRadius/1000.0);
						}
						else{
							tempoTotalComb3+=elapsedtimeRadius;
							tempoRadiusComb3= ((tempoRadiusComb3+(elapsedtimeRadius/1000.0))/2);
						}
					}
					else if(i==3){
						long tempRadiusInicio=System.currentTimeMillis();
						txt.write("\n\n---->  Background Corretion, Ite=50 e Raio=50\n");
						start= System.currentTimeMillis();
						buffOut=new Background_Correction_IJ().limpaFundo(buffIMG, 50, 50, true);
						strQRCode=lerQRCodeImagem(buffOut);
						elapsedTime = System.currentTimeMillis() -start;
						if(strQRCode.substring(0, 4).contentEquals("0000")){
							if(vez==0){
								imgReconhecidas+=1;
								vez+=1;
							}
							comb4+=1;
							if(tempoComb4==0)
								tempoComb4=elapsedTime/1000.0;
							else
								tempoComb4 = ((tempoComb4+(elapsedTime/1000.0))/2);
						}
						txt.write("Passo: "+passo);
						txt.write("\nResultado: "+strQRCode);
						txt.write("\nTempo: "+(String.format(Locale.US,"%.6f",(elapsedTime/1000.0)))+"\n\n");
						long elapsedtimeRadius= System.currentTimeMillis() - tempRadiusInicio;
						if(tempoRadiusComb4==0){
							tempoTotalComb4=elapsedtimeRadius;
							tempoRadiusComb4= (elapsedtimeRadius/1000.0);
						}
						else{
							tempoTotalComb4+=elapsedtimeRadius;
							tempoRadiusComb4= ((tempoRadiusComb4+(elapsedtimeRadius/1000.0))/2);
						}
					}

				}
				txt.close();
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 

	}
	 */


	/**
	 * <b> Metodo gravarImagem</b>
	 * Metodo para gravar imagem
	 * Obs.: Nao colocar extensao do arquivo.
	 * @param img bufferedImage a ser gravado;
	 * @param dirAbsolutoSaida diretorio de saida da imagem, sem extensao.
	 * */
	public static  void gravarImagem(BufferedImage img, String dirAbsolutoSaida) throws IOException {
		File file = new File(dirAbsolutoSaida+".png");
		ImageIO.write(img, "png", file);
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Enum {@link JanelasConv}</b>
	 * Enumerador para limitar os tamanhos das janelas de convolucao.
	 * */
	public enum JanelasConv{
		tres(3),cinco(5),sete(7),onze(11),all();

		private final int n;
		private JanelasConv() {this.n=0;}
		private JanelasConv(int n) {this.n=n;}
		public int getValue() { return n; }
	}

	/**
	 * 
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo filtroMedia</b>
	 * 
	 * Metodo para suavizacao de ruido com filtro de media e janela de convolucao (3, 5, 7 ou 11).
	 * @param imagem imagem em forma de matriz;
	 * @param janelaConv Enumerador do tamanho da janela de convolucao.
	 * @return matrizResultado imagem em forma de matriz com filtro da media.
	 *
	 */
	public static int[][] filtroMedia(int matriz[][], JanelasConv janelaConv){
		int matrizAux[][] = new int[matriz.length][matriz[0].length];
		int matrizResultado[][]=new int[matriz.length][matriz[0].length];
		double[][] elem = new double[janelaConv.getValue()][janelaConv.getValue()];
		final double div =janelaConv.getValue()*janelaConv.getValue();

		for (int i = 0; i < elem.length; i++) {
			for (int j = 0; j < elem[0].length; j++) {
				elem[i][j]= 1.0/div;
			}
		}

		for (int i=0; i<matriz.length; i++){
			for(int j=0; j<matriz[0].length;j++){
				matrizAux[i][j]=matriz[i][j];
			}
		}
		double novoValor;
		for (int k=(int) Math.floor((elem.length/2)); k<matrizAux.length-(int) Math.floor((elem.length/2));k++){
			for (int l=(int) Math.floor((elem.length/2));l<matrizAux[0].length-(int) Math.floor((elem.length/2));l++){
				novoValor=0;
				for (int m= -(int) Math.floor((elem.length/2));m<=(int) Math.floor((elem.length/2));m++){
					for (int n= -(int) Math.floor((elem.length/2));n<=(int) Math.floor((elem.length/2));n++){
						//	System.out.println(m+", "+n);
						novoValor+=elem[m+(int) Math.floor((elem.length/2))][(int) Math.floor((elem.length/2))+n] * matrizAux[k+m][l+n];
						matrizResultado[k][l]=(int)novoValor;
					}

				}


			}
		}
		return matrizResultado;
	}


	/**
	 * 
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo filtroMediana</b>
	 * 
	 * Metodo para suavizacao de ruido com filtro de mediana e janela de convolucao (3, 5, 7 ou 11).
	 * @param imagem imagem em forma de matriz;
	 * @param janelaConv Enumerador do tamanho da janela de convolucao.
	 * @return matrizResultado imagem em forma de matriz com filtro da media.
	 *
	 */
	public static int[][] filtroMediana(int matriz[][],JanelasConv janelaConv){
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
						System.out.println(m+", "+n);
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

	public static int[][] filtroGaussiano(int matriz[][],JanelasConv radius, double sigma){
		int matrizAux[][] = new int[matriz.length][matriz[0].length];
		int matrizResultado[][]=new int[matriz.length][matriz[0].length];
		double janela[][]= new double[radius.getValue()][radius.getValue()];

		double sigm=sigma;
		double r,s =2*sigm*sigm;
		double sum=0.0;

		for (int i=0; i<matriz.length; i++){
			for(int j=0; j<matriz[i].length;j++){
				matrizAux[i][j]=matriz[i][j];
			}
		}
		for (int m= -(int) Math.floor((janela.length/2));m<=(int) Math.floor((janela.length/2));m++){
			for (int n= -(int) Math.floor((janela.length/2));n<=(int) Math.floor((janela.length/2));n++){
				r=Math.sqrt(m*m+n*n);
				sum+=janela[m+(int) Math.floor((janela.length/2))][(int) Math.floor((janela.length/2))+n]=(Math.exp(-(r*r)/s))/(Math.PI*s);
			}
		}
		for (int i = 0; i < janela.length; i++) {
			for (int j = 0; j < janela[0].length; j++) {
				janela[i][j]/=sum;
			}
		}
		double novoValor=0;
		for (int k=(int) Math.floor((janela.length/2)); k<matrizAux.length-(int) Math.floor((janela.length/2));k++){
			for (int l=(int) Math.floor((janela.length/2));l<matrizAux[0].length-(int) Math.floor((janela.length/2));l++){
				novoValor=0;
				for (int m= -(int) Math.floor((janela.length/2));m<=(int) Math.floor((janela.length/2));m++){
					for (int n= -(int) Math.floor((janela.length/2));n<=(int) Math.floor((janela.length/2));n++){
						novoValor+=janela[m+(int) Math.floor((janela.length/2))][(int) Math.floor((janela.length/2))+n] * matrizAux[k+m][l+n];
						matrizResultado[k][l]=(int)novoValor;
					}
				}
			}
		}
		return matrizResultado;
	}

	/**
	 * @author <a href="mailto:renanfucci@hotmail.com"> Renan Fucci </a>
	 */

	public static BufferedImage filtroGaussianBoofCV (BufferedImage imagem, int radius){
		ImageFloat32 input = ConvertBufferedImage.convertFromSingle(imagem , null, ImageFloat32.class);
		ImageFloat32 output = new ImageFloat32(input.width, input.height);
		BlurFilter<ImageFloat32> filter =  FactoryBlurFilter.gaussian(ImageFloat32.class, 1, radius);
		filter.process(input, output);
		BufferedImage saida =ConvertBufferedImage.convertTo(output, new BufferedImage(imagem.getWidth(), imagem.getHeight(), 5));
		return saida;
	}

	public static BufferedImage filtroMediaBoofCV(BufferedImage imagem, int radius){
		ImageFloat32 input = ConvertBufferedImage.convertFromSingle(imagem , null, ImageFloat32.class);
		ImageFloat32 output = new ImageFloat32(input.width, input.height);
		BlurFilter<ImageFloat32> filter =  FactoryBlurFilter.mean(ImageFloat32.class, radius);
		filter.process(input, output);
		BufferedImage saida =ConvertBufferedImage.convertTo(output, new BufferedImage(imagem.getWidth(), imagem.getHeight(), 5));
		return saida;
	}

	public static BufferedImage filtroMedianaBoofCV(BufferedImage imagem, int radius){
		ImageFloat32 input = ConvertBufferedImage.convertFromSingle(imagem , null, ImageFloat32.class);
		ImageFloat32 output = new ImageFloat32(input.width, input.height);
		BlurFilter<ImageFloat32> filter =  FactoryBlurFilter.median(ImageFloat32.class, radius);
		filter.process(input, output);
		BufferedImage saida =ConvertBufferedImage.convertTo(output, new BufferedImage(imagem.getWidth(), imagem.getHeight(), 5));
		return saida;
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo buffToMatriz<br></b>
	 * Metodo para converter um BufferedImage para uma matriz;
	 * @param img imagem a ser convertida.
	 * @return imgSaida imagem no formado de matriz.
	 *
	 */
	public static int[][] buffToMatriz(BufferedImage img) throws Exception {
		int[][] imgOut = new int[img.getHeight()][img.getWidth()];
		for (int i = 0; i < img.getHeight(); i++) {
			for (int j = 0; j < img.getWidth(); j++) {
				imgOut[i][j] = new Color(img.getRGB(j, i)).getGreen();
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
	 * Metodo para fazer pilha de arquivos de um diretorio, todos os arquivos
	 * <p> dentro das pastas e subpastas do diretorio.  </p>
	 * @param folder diretorio a ser empilhado;
	 * @param pilhaArqs varialvel na qual serao empilhados os arquivos.
	 * @return pilhaArqs variavel de arquivos empilhadas.
	 */
	public static Stack<File> empilharArquivosDiretorio(final File folder, Stack<File> pilhaArqs) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				empilharArquivosDiretorio(fileEntry, pilhaArqs);
			} else {
				if(FilenameUtils.getExtension(fileEntry.getName()).contentEquals("jpg")  )
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
	 * <b>Metodo lerQRCodeImagem<br></b>
	 * Metodo para fazer leitura de QRCode de uma imagem .
	 * @param imagem imagem(BufferImage) a ser encontrada.
	 * @return strQRCode String contendo ou nao o QRCode da imagem.
	 */
	public static String lerQRCodeImagem(BufferedImage imagem) {
		String strQRCode="";
		String charset = "UTF-8"; // or "ISO-8859-1"
		try{
			Map<DecodeHintType, Object> hintMap = new HashMap<DecodeHintType, Object>();
			hintMap.put(DecodeHintType.TRY_HARDER, true);
			passo="Sem realce";
			strQRCode=readQRCode(imagem, charset, hintMap);
			if(strQRCode.substring(0, 4).contentEquals("0000")){
				return strQRCode;
			}
			BufferedImage imgRealce;
			passo="Realce EM";
			imgRealce = realceEM(imagem);
			strQRCode = readQRCode(imgRealce	, charset, hintMap);
			if(strQRCode.substring(0, 4).contentEquals("0000")){
				return strQRCode;
			}
			passo="Realce Binario";
			imgRealce =  realceBinario(imagem);
			strQRCode =readQRCode(imgRealce, charset, hintMap);
			if(strQRCode.substring(0, 4).contentEquals("0000")){
				return strQRCode;
			}
			passo="Realce Binario, depois EM";
			imgRealce = realceEM(realceBinario(imagem));
			strQRCode =readQRCode(imgRealce, charset, hintMap);
			if(strQRCode.substring(0, 4).contentEquals("0000")){
				return strQRCode;
			}
		}catch(Exception ex1){
			return "QRCode Não encontrado!";
		}
		return "QRCode Não encontrado!";
	}


	public static String sequnciaFiltros(BufferedImage imagem){
		BufferedImage buffAux = new BufferedImage(imagem.getWidth(), imagem.getHeight(), 5);
		String strQRCode="";
		try{
			strQRCode=lerQRCodeImagem(imagem);
			if(strQRCode.substring(0, 4).contentEquals("0000")){
				return strQRCode;
			}
			System.out.println("Filtro Gaussiano Janela de Convolução 3\n");
			buffAux=filtroGaussianBoofCV(imagem, 3);
			strQRCode=lerQRCodeImagem(buffAux);
			if(strQRCode.substring(0, 4).contentEquals("0000")){
				return strQRCode;
			}
			System.out.println("Filtro Gaussiano Janela de Convolução 5\n");
			buffAux=filtroGaussianBoofCV(imagem, 5);
			strQRCode=lerQRCodeImagem(buffAux);
			if(strQRCode.substring(0, 4).contentEquals("0000")){
				return strQRCode;
			}
			System.out.println("Filtro Media Janela de Convolução 3\n");
			buffAux=filtroMediaBoofCV(imagem,3);
			strQRCode=lerQRCodeImagem(buffAux);
			if(strQRCode.substring(0, 4).contentEquals("0000")){
				return strQRCode;
			}
			/*System.out.println("Background Correction 50-50\n");
			buffAux=new Background_Correction_IJ().limpaFundo(imagem, 50, 50, true);
			strQRCode=lerQRCodeImagem(buffAux);
			if(strQRCode.substring(0, 4).contentEquals("0000")){
				cont2+=1;
				return strQRCode;
			}*/
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
	public static BufferedImage matrizToBufferImage(int[][] matriz) throws Exception {
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
	public static BufferedImage realceBinario(BufferedImage original) throws Exception {
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
	private static BufferedImage realceEM(BufferedImage img) throws Exception {
		int[][] arrayGrayScale = buffToMatriz(img);

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
		BufferedImage imgSaida = matrizToBufferImage(saidaGrayScale);
		return imgSaida;
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo toGray</b>
	 * Metodo para converter uma imagem colorida em uma imagem em tons de cinza;
	 *
	 * @param original imagem a ser convertida.
	 * @return imgSaida imagem em tons de cinza.
	 *
	 */
	static BufferedImage toGray(BufferedImage original) throws Exception {
		int alpha, red, green, blue;
		int newPixel;
		BufferedImage imgSaida = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
		for (int i = 0; i < original.getWidth(); i++) {
			for (int j = 0; j < original.getHeight(); j++) {

				alpha = new Color(original.getRGB(i, j)).getAlpha();
				red = new Color(original.getRGB(i, j)).getRed();
				green = new Color(original.getRGB(i, j)).getGreen();
				blue = new Color(original.getRGB(i, j)).getBlue();
				red = (int) (0.21 * red + 0.71 * green + 0.07 * blue);
				newPixel = colorToRGB(alpha, red, red, red);
				imgSaida.setRGB(i, j, newPixel);
			}
		}
		return imgSaida;
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo fazerSom</b>
	 * Metodo para fazer som.
	 *
	 */
	public static void fazerSom(){
		try{
			byte[] buf = new byte[ 40 ];;
			AudioFormat af = new AudioFormat( (float )44100, 8, 1, true, false );
			SourceDataLine sdl = AudioSystem.getSourceDataLine( af );
			sdl.open();
			sdl.start();
			for( int i = 0; i < 1000 * (float )44100 / 1000; i++ ) {
				double angle = i / ( (float )44100 / 440 ) * 2.0 * Math.PI;
				buf[ 0 ] = (byte )( Math.sin( angle ) * 100 );
				sdl.write( buf, 0, 1 );
			}
			sdl.drain();
			sdl.stop();

			byte[] buf1 = new byte[ 40 ];;
			AudioFormat af1 = new AudioFormat( (float )44100, 8, 1, true, false );
			SourceDataLine sdl1 = AudioSystem.getSourceDataLine( af1 );
			sdl1.open();
			sdl1.start();
			for( int i = 0; i < 1000 * (float )44100 / 1000; i++ ) {
				double angle = i / ( (float )44100 / 440 ) * 2.0 * Math.PI;
				buf1[ 0 ] = (byte )( Math.sin( angle ) * 100 );
				sdl1.write( buf1, 0, 1 );
			}
			sdl1.drain();
			sdl1.stop();
		}catch(Exception ex){
			System.err.println(ex.getLocalizedMessage());
			ex.printStackTrace();
		}
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo temEspacoBranco<br></b><br>
	 * Metodo para testar se o nome de um arquivo possui espaco em branco.
	 * @param fileEntry arquivo de entrada que sera testado o espaco em branco.
	 * @return booleano com o teste de escpaco em branco no nome do arquivo.
	 */
	private static boolean temEspacoBranco(File fileEntry){
		String fileName = fileEntry.getName();//Nome do Arquivo de entrada
		Pattern pattern = Pattern.compile("\\s");//Padrao de compilacao, neste caso espaco em branco
		Matcher matcher = pattern.matcher(fileName);//Combina o espaco em branco com o padrao de compilacao
		return matcher.find();//Busca o espaco em branco no nome do arquivo
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo renomearArqEspacosBrancos</b><br>
	 * Metodo para renomear um arquivo com espaco em branco.
	 * @param fileEntry arquivo de entrada que sera renomeado.
	 */
	private static File renomearArqEspacosBrancos(File fileEntry){
		String fileName; //nome do arquivo
		File flrenomeado;//arquivo renomeado

		fileName = fileEntry.getName().replaceAll("\\s", "_");
		flrenomeado = new File(fileEntry.getParent()+File.separator+fileName);
		try {
			FileUtils.copyFile(fileEntry, flrenomeado);
			System.err.println("\nArquivo com espacos em branco, renomeado para: "+flrenomeado);
			fileEntry.delete();
			return flrenomeado;

		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return null;
	}

	public static void criarTxtdoTempo(String fileName, String tempo){
		try {
			String str_tempo = tempo+"\n";
			PrintWriter writer = new PrintWriter(fileName+".txt", "UTF-8");
			writer.println(str_tempo);
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
