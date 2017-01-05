package realceNovo;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class RealceEM{

	public static void main(String[] args) {
/*			
		String dir="C:\\Users\\Renan Fucci\\Dropbox\\RIO 45º\\Enhance\\01";
			BufferedImage original  = ImageIO.read(new File(dir+".jpg"));
			BufferedImage grayScale = toGray(original);
			long start= System.currentTimeMillis();
			int[][] arrayGrayScale =  buffToMatriz(grayScale);

			int[][] saidaGrayScale= new int[arrayGrayScale.length][arrayGrayScale[0].length];
			double[] novaLinha=new  double[grayScale.getWidth()];
			double[] saidaLinha=new  double[grayScale.getWidth()];

			int max=0, min=0;
			int diferenca=0;


			int countLinhasBrancas = 0;
			for (int i = 1; i < Math.abs(grayScale.getHeight() *0.10); i++) {//Considera soh 10 porc da linhas
				max= getMaxValor(arrayGrayScale[i]);
				//		System.out.println("Max: "+max);
				min = getMinValor(arrayGrayScale[i]);
				//	System.out.println("Min: "+min);
				diferenca = max-min;
				//System.out.println("diferenca: "+diferenca);
				if(max>196 && diferenca <64 ){
					//Faz a linha ficar branca
					countLinhasBrancas+=1;
					//
					for (int j2 = 0; j2 < saidaGrayScale[0].length; j2++) {
						//saidaGrayScale[i][j2]= 0;
						novaLinha[j2]= novaLinha[j2] + (arrayGrayScale[i][j2]);
						//System.out.println(String.format(Locale.US,"%.6f", novaLinha[j2]));
					}
				}
			}

			//		System.out.println("\n linhas brancas: "+countLinhasBrancas);
			novaLinha= mediaLinhaVetor(novaLinha, countLinhasBrancas);


			for (int i = 2; i < grayScale.getHeight()-1; i++) {//Considera soh 10 porc da linhas
				//calcular linha 
				for (int j = 0; j < saidaLinha.length; j++) {
					saidaLinha[j]=(novaLinha[j] - arrayGrayScale[i][j])/255;
				}
				for (int j = 0; j < saidaGrayScale[i].length; j++) {
					//System.out.println(saidaLinha[j]);
					if(saidaLinha[j]<0.1){
						//	System.out.println("<0.1");
						saidaGrayScale[i][j]=255;
					}
					else if(saidaLinha[j]>0.5){
						saidaGrayScale[i][j]=0;
						//System.out.println(">0.5");

					}
				}

			}
			//Vizinhos pretos
			for (int i = 1; i < grayScale.getHeight()-2; i++) {//Considera soh 10 porc da linhas
				for (int j = 1; j < saidaGrayScale[0].length-1; j++) {
					int vizinhosIguais = 0;

					if(saidaGrayScale[i][j-1]==0)//vizinho esquerdo
						vizinhosIguais+=1;
					else if(saidaGrayScale[i][j+1]==0) //vizinho direito
						vizinhosIguais+=1;
					else if(saidaGrayScale[i-1][j]==0) //vizinho cima
						vizinhosIguais+=1;
					else if(saidaGrayScale[i+1][j]==0) //vizinho baixo
						vizinhosIguais+=1;

					if(vizinhosIguais>2)
						saidaGrayScale[i][j]=0;
				}	
			}

			//Vizinhos Brancos
			for (int i = 1; i <grayScale.getHeight()-1; i++) {//Considera soh 10 porc da linhas
				for (int j = 1; j < saidaGrayScale[0].length-1; j++) {
					int vizinhosIguais = 0;

					if(saidaGrayScale[i][j-1]==255)//vizinho esquerdo
						vizinhosIguais+=1;
					else if(saidaGrayScale[i][j+1]==255) //vizinho direito
						vizinhosIguais+=1;
					else if(saidaGrayScale[i-1][j]==255) //vizinho cima
						vizinhosIguais+=1;
					else if(saidaGrayScale[i+1][j]==255) //vizinho baixo
						vizinhosIguais+=1;

					if(vizinhosIguais>2)
						saidaGrayScale[i][j]=255;
				}	
			}
			long elapsedTime = System.currentTimeMillis() - start;
			System.out.println(elapsedTime/1000.0);
			//array to Buffer
			BufferedImage imgSaida =matrizToBufferImage(saidaGrayScale);
			//gravar image
			//gravarImagem(imgSaida, dir);
*/	}

	/**
	 * <b> Metodo matrizToBufferImage <\b>
	 * Metodo para converter uma matriz em BufferedImage
	 * @param matriz[][](int) matriz a ser convertida.
	 * @return saida(BufferedImage)
	 * */
	public static BufferedImage matrizToBufferImage(int[][] matriz) throws Exception{
		BufferedImage saida =  new BufferedImage(matriz[0].length, matriz.length,5);
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[i].length; j++) {
				saida.setRGB(j, i,new Color( matriz[i][j],matriz[i][j],matriz[i][j]).getRGB());
			}
		}
		return saida;
	} 

	/**
	 * <b> Metodo gravarImagem<\b>
	 * Metodo para gravar imagem
	 * Obs.: Nao colocar extensao do arquivo.
	 * @param img bufferedImage a ser gravado;
	 * @param dirAbsolutoSaida diretorio de saida da imagem, sem extensao.
	 * */
	public static void gravarImagem(BufferedImage img, String dirAbsolutoSaida) throws IOException {
		File file = new File(dirAbsolutoSaida+".png");
		ImageIO.write(img, "png", file);
	}

	/**
	 * <b> Metodo mediaLinhaVetor<\b>
	 * Metodo para calcular a media de todos as posicoes de um vetor.
	 * @param vetor vetor a ser calculado
	 * @param qntdLinhasBrancas divisor do calculo de media
	 * @return vetor vetor calculado, com a media de todas as posicoes
	 * */
	public static double[] mediaLinhaVetor(double[] vetor, int qntdLinhasBrancas) throws Exception{
		for (int i = 0; i < vetor.length; i++) {
			vetor[i]=vetor[i]/qntdLinhasBrancas; 
		}
		return vetor;
	}
	
	/**
	 * <b> Metodo getMaxValor<\b>
	 * Metodo para encontrar o valor maximo de um vetor;
	 * @param vetor 
	 * @return maxValor valor maximo do vetor
	 * */
	public static int getMaxValor(int[] vetor) throws Exception{  
		int maxValor = vetor[0];  
		for(int i=1;i < vetor.length;i++){
			if(vetor[i] > maxValor){  
				maxValor = vetor[i];  
			}  
		}  
		return maxValor;  
	}  

	/**
	 * <b> Metodo getMinValor<\b>
	 * Metodo para encontrar o valor minimo de um vetor;
	 * @param vetor 
	 * @return minValor valor minimo do vetor
	 * */
	public static int getMinValor(int[] vetor)	throws Exception{  
		int minValor = vetor[0];  
		for(int i=1;i<vetor.length;i++){  
			if(vetor[i] < minValor){  
				minValor = vetor[i];  
			}  
		}  
		return minValor;  
	}  


	/**
	 * <b>Metodo realceEM<\b>
	 * Metodo para realce de uma imagem;
	 * @param img imagem a ser realcada.
	 * @return imgSaida imagem ja realcada.
	 * */
	public static BufferedImage realceEM(BufferedImage img) throws Exception{
			//BufferedImage original  = ImageIO.read(new File(dir+".jpg")); //Leitura
			//BufferedImage grayScale = toGray(original); //Tons de Cinza
			
			long start= System.currentTimeMillis();
			int[][] arrayGrayScale =  buffToMatriz(img);

			int[][] saidaGrayScale= new int[arrayGrayScale.length][arrayGrayScale[0].length];
			double[] novaLinha=new  double[img.getWidth()];
			double[] saidaLinha=new  double[img.getWidth()];

			int max=0, min=0;
			int diferenca=0;


			int countLinhasBrancas = 0;
			for (int i = 1; i < Math.abs(img.getHeight() *0.10); i++) {//Considera soh 10 porc da linhas
				max= getMaxValor(arrayGrayScale[i]);
				//		System.out.println("Max: "+max);
				min = getMinValor(arrayGrayScale[i]);
				//	System.out.println("Min: "+min);
				diferenca = max-min;
				//System.out.println("diferenca: "+diferenca);
				if(max>196 && diferenca <64 ){
					//Faz a linha ficar branca
					countLinhasBrancas+=1;
					//
					for (int j2 = 0; j2 < saidaGrayScale[0].length; j2++) {
						//saidaGrayScale[i][j2]= 0;
						novaLinha[j2]= novaLinha[j2] + (arrayGrayScale[i][j2]);
						//System.out.println(String.format(Locale.US,"%.6f", novaLinha[j2]));
					}
				}
			}

			//		System.out.println("\n linhas brancas: "+countLinhasBrancas);
			novaLinha= mediaLinhaVetor(novaLinha, countLinhasBrancas);


			for (int i = 2; i < img.getHeight()-1; i++) {//Considera soh 10 porc da linhas
				//calcular linha 
				for (int j = 0; j < saidaLinha.length; j++) {
					saidaLinha[j]=(novaLinha[j] - arrayGrayScale[i][j])/255;
				}
				for (int j = 0; j < saidaGrayScale[i].length; j++) {
					//System.out.println(saidaLinha[j]);
					if(saidaLinha[j]<0.1){
						//	System.out.println("<0.1");
						saidaGrayScale[i][j]=255;
					}
					else if(saidaLinha[j]>0.5){
						saidaGrayScale[i][j]=0;
						//System.out.println(">0.5");

					}
				}

			}
			//Vizinhos pretos
			for (int i = 1; i < img.getHeight()-2; i++) {//Considera soh 10 porc da linhas
				for (int j = 1; j < saidaGrayScale[0].length-1; j++) {
					int vizinhosIguais = 0;

					if(saidaGrayScale[i][j-1]==0)//vizinho esquerdo
						vizinhosIguais+=1;
					else if(saidaGrayScale[i][j+1]==0) //vizinho direito
						vizinhosIguais+=1;
					else if(saidaGrayScale[i-1][j]==0) //vizinho cima
						vizinhosIguais+=1;
					else if(saidaGrayScale[i+1][j]==0) //vizinho baixo
						vizinhosIguais+=1;

					if(vizinhosIguais>2)
						saidaGrayScale[i][j]=0;
				}	
			}

			//Vizinhos Brancos
			for (int i = 1; i <img.getHeight()-1; i++) {//Considera soh 10 porc da linhas
				for (int j = 1; j < saidaGrayScale[0].length-1; j++) {
					int vizinhosIguais = 0;

					if(saidaGrayScale[i][j-1]==255)//vizinho esquerdo
						vizinhosIguais+=1;
					else if(saidaGrayScale[i][j+1]==255) //vizinho direito
						vizinhosIguais+=1;
					else if(saidaGrayScale[i-1][j]==255) //vizinho cima
						vizinhosIguais+=1;
					else if(saidaGrayScale[i+1][j]==255) //vizinho baixo
						vizinhosIguais+=1;

					if(vizinhosIguais>2)
						saidaGrayScale[i][j]=255;
				}	
			}
			long elapsedTime = System.currentTimeMillis() - start;
			System.out.println("Reace EM em: "+elapsedTime/1000.0+" segundos.");
			//array to Buffer
			BufferedImage imgSaida =matrizToBufferImage(saidaGrayScale);
			//gravar image
			//gravarImagem(imgSaida, dir);
			return imgSaida;
		
	}


	/**
	 * <b>Metodo buffToMatriz<\b>
	 * Metodo para converter um BufferedImage para uma matriz;
	 * @param img imagem a ser convertida.
	 * @return imgSaida imagem no formado de matriz.
	 * */
	public static int[][] buffToMatriz(BufferedImage img) throws Exception{
		int[][] imgOut =  new int[img.getHeight()][img.getWidth()];
		for (int i = 0; i < imgOut.length; i++) {
			for (int j = 0; j < imgOut[i].length; j++) {
				imgOut[i][j]= new Color(img.getRGB(j, i)).getGreen();
			}
		}
		return imgOut;

	}


	/**
	 * <b>Metodo toGray<\b>
	 * Metodo para converter uma imagem colorida em uma imagem em tons de cinza;
	 * @param original imagem a ser convertida.
	 * @return imgSaida imagem em tons de cinza.
	 * */
	public static BufferedImage toGray(BufferedImage original) throws Exception{
		int alpha, red, green, blue;
		int newPixel;
		BufferedImage imgSaida = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
		for(int i=0; i<original.getWidth(); i++) {
			for(int j=0; j<original.getHeight(); j++) {

				alpha = new Color(original.getRGB(i, j)).getAlpha();
				red = new Color(original.getRGB(i, j)).getRed();
				green = new Color(original.getRGB(i, j)).getGreen();
				blue = new Color(original.getRGB(i, j)).getBlue();

				red = (int) (0.21 * red + 0.71 * green + 0.07 * blue);
				
				// Formado de 8 bits
				newPixel = colorToRGB(alpha, red, red, red);
				imgSaida.setRGB(i, j, newPixel);
			}
		}

		return imgSaida;

	}
	
	/**
	 * <b>Metodo colorToRGB<\b>
	 * Metodo para converter 1(UM) pixel R, G, B, A  para o um padrao de 8 bits.
	 * @param alpha canal alpha do pixel.
	 * @param red canal red do pixel.
	 * @param green canal green do pixel.
	 * @param blue canal blue do pixel.
	 * @return newPixel pixel no formato de 8 bits.
	 * */
	public static int colorToRGB(int alpha, int red, int green, int blue) throws Exception {

		int newPixel = 0;
		newPixel += alpha;
		newPixel = newPixel << 8;
		newPixel += red; newPixel = newPixel << 8;
		newPixel += green; newPixel = newPixel << 8;
		newPixel += blue;

		return newPixel;

	}
}
