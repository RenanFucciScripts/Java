package enhance;

import java.awt.image.BufferedImage;

import boofcv.alg.enhance.EnhanceImageOps;
import boofcv.alg.misc.ImageStatistics;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.struct.image.ImageUInt8;

public class RealceEscuro {

	public static void main(String[] args) {
	/*	String dir= "C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2016-01-31\\";


		BufferedImage imageOut = autoContrast((new MetodosRF()).readImage(dir+"grayjpg"));
		
		
		System.out.println((new MetodosRF()).writeImage(imageOut, dir+"autocontraste.jpg"));
	*/}
	
	/**
	 * <b>@author Renan Fucci</b><br>
	 * <b>Metodo autoContrast</b>
	 * Metodo para aplicar um realce de autoContraste uma imagem.
	 * @param image {@link BufferedImage} em que sera aplicado o autoContraste.
	 * @return um {@link BufferedImage}.
	 */
	public BufferedImage autoContrast(BufferedImage image){
		Class<ImageUInt8> imageType= (ImageUInt8.class);
		
		ImageUInt8 input = ConvertBufferedImage.convertFromSingle(image, null, imageType);
		ImageUInt8 output = new ImageUInt8(input.getWidth(), input.getHeight());
		int[] histogram = new int[256];
		int[] transform = new int[256];

		ImageStatistics.histogram(input, histogram);
		EnhanceImageOps.equalize(histogram, transform);
		EnhanceImageOps.applyTransform(input, transform, output);
		return ConvertBufferedImage.convertTo(output,null,true);		
	}
	
	
}
