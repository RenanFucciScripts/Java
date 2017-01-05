package realceCabecalho;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;

import metodos.MetodosEM.JanelasConv;
import metodos.MetodosRF;

public class RealceCabecalhoByteArray {


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
		int[][] red=  getByteArrayRGB_Separado(img, "Red");
		int[][] green=getByteArrayRGB_Separado(img, "Green");
		int[][] blue = getByteArrayRGB_Separado(img, "Blue");
		
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

		BufferedImage buff = MetodosRF.matrizColorToBufferImage(saidaR, saidaG, saidaB);
	
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
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo getByteArrayRGB_Separado<br></b>
	 * Metodo para converter um BufferedImage para uma matriz utilizando um raster (ByteArray).
	 * @param imagem bufferedImage a ser convertido.
	 * @return imagem no formato de matriz.
	 *
	 */
	public static int[][] getByteArrayRGB_Separado(BufferedImage imagem, String bandaRGB) {

		final byte[] pixels = ((DataBufferByte) imagem.getRaster().getDataBuffer()).getData();
		final int width = imagem.getWidth();
		final int height = imagem.getHeight();
		final boolean hasAlphaChannel = imagem.getAlphaRaster() != null;

		int[][] result = new int[height][width];

		final int pixelLength = 3;
		for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
			int argb = 0;
			if(bandaRGB.contentEquals("Blue")){

				argb = new Color((int) pixels[pixel] & 0xff).getBlue(); // blue
			}
			else if(bandaRGB.contentEquals("Green")){

				argb =new Color(((int) pixels[pixel + 1] & 0xff) << 8).getGreen(); // green
			}
			else if(bandaRGB.contentEquals("Red")){
				argb = new Color(((int) pixels[pixel + 2] & 0xff) << 16).getRed(); // red
			}
			result[row][col] = argb;
			col++;
			if (col == width) {
				col = 0;
				row++;
			}
		}

		return result;
	}
}


