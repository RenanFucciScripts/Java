package comparacaoDesemp;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;

import javax.imageio.ImageIO;


public class GetRGBSeparado {


	public static void main(String[] args) throws Exception {
	
		String dir = "C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2015-10-29\\";
		String nameIMG = "realce 230915000031980.jpg";
		BufferedImage imagem = ImageIO.read(new File(dir+nameIMG));
		
		System.out.println("------------- Metodo de copia com getByteArray (Buffer p/ byte[][])  ----------");
		long start = System.currentTimeMillis();
		int[][] resultR = getByteArrayRGB_Separado(imagem, "Red");
		int[][] resultG = getByteArrayRGB_Separado(imagem, "Green");
		int[][] resultB = getByteArrayRGB_Separado(imagem, "Blue");
		BufferedImage buff = matrizColorToBufferImage(resultR, resultG, resultB);
		ImageIO.write(buff, "JPG",new File(dir+" (Buffer p byte)"+nameIMG));
		System.out.println("Tempo: "+((System.currentTimeMillis() - start)/1000.0)+" segundos");
	
		
		System.out.println("------------- Metodo de copia com getInt (Buffer p/ int[][])  ----------");
		long start1 = System.currentTimeMillis();
		int[][] resultR1 = getRGB_Separado(imagem,  "Red");
		int[][] resultG1 = getRGB_Separado(imagem, "Green");
		int[][] resultB1 = getRGB_Separado(imagem, "Blue");
		BufferedImage buff1 = matrizColorToBufferImage(resultR1, resultG1, resultB1);
		ImageIO.write(buff1, "JPG",new File(dir+" (Buffer p int)"+nameIMG));
		System.out.println("Tempo: "+((System.currentTimeMillis() - start1)/1000.0)+" segundos");
		
		
	}
	
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

	public static int[][] getRGB_Separado(BufferedImage buff, String bandaRGB) throws Exception {
		int height = buff.getHeight();
		int weight = buff.getWidth();
		int[][] imgOut = new int[height][weight];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < weight; j++) {
				if(bandaRGB.contentEquals("Red")){
					imgOut[i][j] = new Color(buff.getRGB(j, i)).getRed();
				}
				else if(bandaRGB.contentEquals("Green")){
					imgOut[i][j] = new Color(buff.getRGB(j, i)).getGreen();
				}
				else if(bandaRGB.contentEquals("Blue")){
					imgOut[i][j] = new Color(buff.getRGB(j, i)).getBlue();
				}
			}
		}
		return imgOut;
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
	public static BufferedImage matrizColorToBufferImage(int[][] matrizR, int[][] matrizG, int[][] matrizB) throws Exception {
		BufferedImage saida = new BufferedImage(matrizR[0].length, matrizR.length, 5);
		for (int i = 0; i < matrizR.length; i++) {
			for (int j = 0; j < matrizR[i].length; j++) {
				saida.setRGB(j, i, new Color(matrizR[i][j], matrizG[i][j], matrizB[i][j]).getRGB());
			}
		}
		return saida;
	}
}
