package comparacaoDesemp;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;

import javax.imageio.ImageIO;

import metodos.MetodosEM;

public class ComparacaoDesemp {

	public static void main(String[] args) throws Exception {

		String dir = "C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2015-10-29\\";
		String nameIMG = "realce 230915000031980.jpg";
		BufferedImage imagem = ImageIO.read(new File(dir+nameIMG));
		int qntdVezes = 10;
		System.out.println("------------- Metodo de copia com getRGB (Buffer p/ int[][]) para "+qntdVezes+" imagems----------");
		long start = System.currentTimeMillis();
		int[][] resultR = metodoComGetRGB(imagem);
		BufferedImage buffResult = matrizByteToBufferImage(resultR);
		ImageIO.write(buffResult, "JPG", new File(dir+" (Buffer p int)"+nameIMG));
		System.out.println("Tempo: "+((System.currentTimeMillis() - start)/1000.0)+" segundos");

		System.out.println("\n\n");

		System.out.println("------------- Metodo de copia com getByteArray (Buffer p/ byte[][]) para "+qntdVezes+" imagems ----------");
		start = System.currentTimeMillis();
		int[][] result1 = metodoComGetByteArray(imagem);
		BufferedImage buffResult1 = matrizByteToBufferImage(result1);
		ImageIO.write(buffResult1, "JPG", new File(dir+" (Buffer p byte)"+nameIMG));

		System.out.println("Tempo: "+((System.currentTimeMillis() - start)/1000.0)+" segundos");

		System.out.println("\n\n");

		System.out.println("------------- Metodo de copia sem getRGB, so Buffer para "+qntdVezes+" imagens ----------");
		start = System.currentTimeMillis();
		BufferedImage result11 = metodoSoBuffer(imagem);
		ImageIO.write(result11, "JPG",  new File(dir+" (Buffer p bufffer)"+nameIMG));
		System.out.println("Tempo: "+((System.currentTimeMillis() - start)/1000.0)+" segundos");

		System.out.println("\n\n");

		MetodosEM.fazerSom();
	}

	public static int[][] metodoComGetRGB(BufferedImage buff) throws Exception {
		int[][] imgOut = new int[buff.getHeight()][buff.getWidth()];
		for (int i = 0; i < buff.getHeight(); i++) {
			for (int j = 0; j < buff.getWidth(); j++) {
					imgOut[i][j] = buff.getRGB(j, i);
			}
		}
		return imgOut;
	}

	public static BufferedImage metodoSoBuffer(BufferedImage imagem){
		int width =imagem.getWidth();
		int height = imagem.getHeight();
		BufferedImage buff =  new BufferedImage(width, height, 5);

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				buff.setRGB(j, i, imagem.getRGB(j, i));
			}
		}

		return buff;
	}

	public static int[][] metodoComGetByteArray(BufferedImage imagem) {

		final byte[] pixels = ((DataBufferByte) imagem.getRaster().getDataBuffer()).getData();
		final int width = imagem.getWidth();
		final int height = imagem.getHeight();
		final boolean hasAlphaChannel = imagem.getAlphaRaster() != null;

		int[][] result = new int[height][width];
		if (hasAlphaChannel) {
			final int pixelLength = 4;
			for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
				int argb = 0;
				argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
				argb += ((int) pixels[pixel + 1] & 0xff); // blue
				argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
				argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
				result[row][col] = argb;
				col++;
				if (col == width) {
					col = 0;
					row++;
				}
			}
		} else {
			final int pixelLength = 3;
			for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
				int argb = 0;
				argb += -16777216; // 255 alpha
				argb += ((int) pixels[pixel] & 0xff); // blue
				argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
				argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
				result[row][col] = argb;
				col++;
				if (col == width) {
					col = 0;
					row++;
				}
			}
		}

		return result;
	}

	
	public static BufferedImage matrizByteToBufferImage(int[][] matriz) throws Exception {
		BufferedImage saida = new BufferedImage(matriz[0].length, matriz.length, 5);
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[i].length; j++) {
				saida.setRGB(j, i, new Color(matriz[i][j]).getRGB());
			}
		}
		return saida;
	}
}
