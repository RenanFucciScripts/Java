package faixaScanner;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import enhance.MetodosPDI;

public class PaletaCores {

	public static void main(String[] args) {
		try{
			MetodosPDI methods =  new MetodosPDI();
			String dir="C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2015-08-17\\imagensFaixaScanner\\";
			int[][] tam= methods.leImagem(dir+"imgRuidoMarginal.jpg", "Red");
			BufferedImage image = new BufferedImage(tam[0].length, tam.length, 5);
			image = montarPaletaCor(image);
			ImageIO.write(image, "JPG", new File(dir+"imgNaoRuido2.jpg"));
		}catch(Exception e){
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public static BufferedImage montarPaletaCor(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[] data = new int[width * height];
		int index = 0;
		for (int i = 0; i < height; i++) {
			int red = (i * 255) / (height - 1);
			for (int j = 0; j < width; j++) {
				int green = (j * 255) / (width - 1);
				int blue = 127;
				data[index++] = (red << 16) | (green << 8) | blue;
			}
		}

		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		image.setRGB(0, 0, width, height, data, 0, width);
		return image;
	}
}
