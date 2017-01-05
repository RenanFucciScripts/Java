package _openCV;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.opencv.core.Core;
import org.opencv.core.Mat;


public class GammaCorrection {

	static{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}


	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo gammaCorrection</b><br>
	 * Metodo para aplicar um metodo de gammaCorrection em uma imagem no padrao {@link Mat}.
	 * @param img imagem em que sera aplicado o gammaCorrection.
	 * @param gamma	intensidade do GammaCorrection.
	 * @return imagem com o metodo gammaCorrection.
	 */
	static Mat gammaCorrection(Mat img, double gamma) {
		BufferedImage original  = _OpenCV.matToBufferImg(img);
		int alpha, red, green, blue;
		int newPixel;

		double gamma_new = 1 / gamma;
		int[] gamma_LUT = gamma_LUT(gamma_new);

		BufferedImage gamma_cor = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

		for(int i=0; i<original.getWidth(); i++) {
			for(int j=0; j<original.getHeight(); j++) {

				alpha = new Color(original.getRGB(i, j)).getAlpha();
				red = new Color(original.getRGB(i, j)).getRed();
				green = new Color(original.getRGB(i, j)).getGreen();
				blue = new Color(original.getRGB(i, j)).getBlue();

				red = gamma_LUT[red];
				green = gamma_LUT[green];
				blue = gamma_LUT[blue];

				newPixel = colorToRGB(alpha, red, green, blue);

				gamma_cor.setRGB(i, j, newPixel);

			}

		}

		return _OpenCV.bufferImgToMat_RGB(gamma_cor);        

	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo gamma_LUT</b><br>
	 * Metodo para criar um LUT  a partir de um novo valor de gamma.
	 * @param gamma_new novo valor gamma.
	 * @return LUT a partir do novo valor gamma.
	 */
	private static int[] gamma_LUT(double gamma_new) {
		int[] gamma_LUT = new int[256];     
		for(int i=0; i<gamma_LUT.length; i++) {
			gamma_LUT[i] = (int) (255 * (Math.pow((double) i / (double) 255, gamma_new)));
		}
		return gamma_LUT;
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo colorToRGB</b><br>
	 * Metodo para pegar os valores ALPHA, RED, GREEN e BLUE pra converte-los em um int RGB.
	 * @param alpha valor de alpha.
	 * @param red valor de red
	 * @param green valor de green.
	 * @param blue valor de blue.
	 * @return LUT a partir do novo valor gamma.
	 */
	private static int colorToRGB(int alpha, int red, int green, int blue) {
		int newPixel = 0;
		newPixel += alpha;
		newPixel = newPixel << 8;
		newPixel += red; newPixel = newPixel << 8;
		newPixel += green; newPixel = newPixel << 8;
		newPixel += blue;
		return newPixel;
	}

}

