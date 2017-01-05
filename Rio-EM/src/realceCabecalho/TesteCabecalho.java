package realceCabecalho;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;

import org.apache.commons.io.FilenameUtils;

import comparacaoDesemp.GetRGBSeparado;
import metodos.MetodosEM;
import metodos.MetodosEM.JanelasConv;
import metodos.MetodosRF;

public class TesteCabecalho {

	public static void main(String[] args) throws LineUnavailableException {
		try {
			String dir="C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2015-11-09\\";
			MetodosRF methods =  new MetodosRF();

			File folder =  new File(dir);
			Stack<File> pilhaArqs =  new Stack<File>();

			methods.empilharArquivosDiretorio(folder, pilhaArqs );
			FileWriter flW ;
			BufferedWriter bfW ;
			for (File file2 : pilhaArqs) {
				long start=System.currentTimeMillis();
				BufferedImage bfI = realceCabecalhoByteArray(ImageIO.read(file2));
				ImageIO.write(bfI, "JPG", new File( dir+"realceGetRGB\\"+file2.getName()));
				long elapsedTime= System.currentTimeMillis() -start;
				flW= new FileWriter(dir+"realceGetRGB\\"+(FilenameUtils.removeExtension(file2.getName()))+".txt");
				bfW =  new BufferedWriter(flW);
				bfW.write((elapsedTime/1000.0)+"\tsegundos");
				bfW.close();
			
			}
			/*
			FileWriter flW ;
			BufferedWriter bfW ;
			for (File file : pilhaArqs) {
				long start=System.currentTimeMillis();
				BufferedImage bfI = realceCabecalho(ImageIO.read(file));
				ImageIO.write(bfI, "JPG", new File( dir+"realceGetRGB\\"+file.getName()));
				long elapsedTime= System.currentTimeMillis() -start;
				flW= new FileWriter(dir+"realceGetRGB\\"+(FilenameUtils.removeExtension(file.getName()))+".txt");
				bfW =  new BufferedWriter(flW);
				bfW.write((elapsedTime/1000.0)+"\tsegundos");
				bfW.close();
				//System.out.println((elapsedTime/1000.0)+"\tsegundos");
			}

			for (File file : pilhaArqs) {
				long start=System.currentTimeMillis();
				BufferedImage bfI = realceCabecalhoByteArray(ImageIO.read(file));
				ImageIO.write(bfI, "JPG", new File( dir+"realceGetByteRGB\\"+file.getName()));
				long elapsedTime= System.currentTimeMillis() -start;
				flW= new FileWriter(dir+"realceGetByteRGB\\"+(FilenameUtils.removeExtension(file.getName()))+".txt");
				bfW =  new BufferedWriter(flW);
				bfW.write((elapsedTime/1000.0)+"\tsegundos");
				bfW.close();
				//System.out.println((elapsedTime/1000.0)+"\tsegundos");
			}*/
			MetodosEM.fazerSom();
		} catch (Exception e) {
			MetodosEM.fazerSom();
			System.err.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	/**
	 * <b>@author Alex Araujo<br></b>
	 * <b> Metodo realceCabecalho</b>
	 * Metodo para fazer realce nos cabecalhos que estejam muito claros.
	 *
	 * @param file arquivo imagem que sera feito o realce.
	 * @return buff bufferedImage da imagem realcada.
	 *
	 */
	public static BufferedImage realceCabecalhoByteArray(BufferedImage img) throws IOException,Exception {
		int[][] red=  GetRGBSeparado.getByteArrayRGB_Separado(img, "Red");
		int[][] green= GetRGBSeparado.getByteArrayRGB_Separado(img, "Green");
		int[][] blue = GetRGBSeparado.getByteArrayRGB_Separado(img, "Blue");

		int[][] medR =MetodosRF.filtroMedianaRF(red, JanelasConv.tres);
		int[][] medG =MetodosRF.filtroMedianaRF(green, JanelasConv.tres);
		int[][] medB =MetodosRF.filtroMedianaRF(blue, JanelasConv.tres);

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

		BufferedImage buff = array_rasterToBuffer(saidaR, saidaG, saidaB);

		return buff;
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
	 * <b>@author Alex Araujo<br></b>
	 * <b> Metodo realceCabecalho</b>
	 * Metodo para fazer realce nos cabecalhos que estejam muito claros.
	 *
	 * @param buff arquivo imagem que sera feito o realce.
	 * @return buff bufferedImage da imagem realcada.
	 *
	 */
	public static BufferedImage realceCabecalho(BufferedImage buff) throws IOException,Exception {
		int[][] red =GetRGBSeparado.getRGB_Separado(buff, "Red");
		int[][] green =GetRGBSeparado.getRGB_Separado(buff, "Green");
		int[][] blue =GetRGBSeparado.getRGB_Separado(buff,  "Blue");

		int[][] medR =MetodosRF.filtroMedianaRF(red, JanelasConv.tres);
		int[][] medG =MetodosRF.filtroMedianaRF(green, JanelasConv.tres);
		int[][] medB =MetodosRF.filtroMedianaRF(blue, JanelasConv.tres);

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

		BufferedImage buff1 = MetodosRF.matrizColorToBufferImage(saidaR, saidaG, saidaB);
		return buff1;
	}
	/**----Método Um 
	 * 1,43% mais rápido*/ 
	/*public static BufferedImage array_rasterToBuffer (int[][] imgR,  int[][] imgG, int[][] imgB ){
		final int width = imgR[0].length;
		final int height = imgR.length;

		//int[][] result = new int[height][width];

		int numBandas = 3;
		int[] pixels = new int[width*height*numBandas];
		
		System.out.println("max: "+width*height*3);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				for (int band = 0; band < numBandas; band++) {
					int value;
					if (band == 0) {
					   value = imgR[i][j] & 0xFF;
					} else if (band == 1) {
					   value = imgG[i][j] & 0xFF;
					} else {
					   value = imgB[i][j] & 0xFF;
					}
					pixels[(((i*width)+j)*numBandas +band)] = value;
				}
			}
		}

			for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[i].length; j++) {
				int newPixel = 0xff000000 | (imgR[i][j] & 0xff) >> 16 | (imgG[i][j] & 0xff) >> 8 | (imgB[i][j] & 0xff);
				//System.out.println(Math.abs(newPixel));
				result[i][j]=newPixel;
			}
		}

		BufferedImage bufferImg = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);

		WritableRaster rast = (WritableRaster) bufferImg.getData(); 
		rast.setPixels(0, 0, width, height, pixels);
		bufferImg.setData(rast);

		return bufferImg;
	}*/
	/** Método Dois
	 * 5,67 % mais rápido que o método UM
	 * 7,1 % mais rápido que o método usado atualmente */
	public static BufferedImage array_rasterToBuffer(int[][] imgR, int[][] imgG, int[][] imgB) {
		final int width = imgR[0].length;
		final int height = imgR.length;
		int[] pixels = new int[width * height];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pixels[((y * width) + x)] = ((imgR[y][x] & 0xff) << 16 | (imgG[y][x] & 0xff) << 8 | (imgB[y][x] & 0xff));
			}
		}

		BufferedImage bufferImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		WritableRaster rast = bufferImg.getRaster();
		rast.setDataElements(0, 0, width, height, pixels);
		return bufferImg;
	}
	
}
