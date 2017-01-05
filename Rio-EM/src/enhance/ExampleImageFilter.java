package enhance;



import java.awt.image.BufferedImage;

import boofcv.alg.filter.blur.BlurImageOps;
import boofcv.alg.filter.derivative.GradientSobel;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.core.image.border.FactoryImageBorderAlgs;
import boofcv.gui.image.ShowImages;
import boofcv.gui.image.VisualizeImageData;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.ImageSInt16;
import boofcv.struct.image.ImageUInt8;

/**
 * An introductory example designed to introduce basic BoofCV concepts.  Each function
 * shows how to perform basic filtering and display operations using different techniques.
 *
 * @author Peter Abeles
 */
@SuppressWarnings({"unchecked"})
public class ExampleImageFilter {

	private static int blurRadius = 10;
	
	public static void procedural( ImageUInt8 input )
	{
		ImageUInt8 blurred = new ImageUInt8(input.width,input.height);
		ImageSInt16 derivX = new ImageSInt16(input.width,input.height);
		ImageSInt16 derivY = new ImageSInt16(input.width,input.height);
 
		// Gaussian blur: Convolve a Gaussian kernel
		BlurImageOps.gaussian(input,blurred,-1,blurRadius,null);
 
		// Calculate image's derivative
		GradientSobel.process(blurred, derivX, derivY, FactoryImageBorderAlgs.extend(input));
 
		// display the results
		BufferedImage outputImage = VisualizeImageData.colorizeSign(derivX,null,-1);
		ShowImages.showWindow(outputImage,"Procedural Fixed Type");
	}

	public static void main( String args[] ) {

		BufferedImage image = UtilImageIO.loadImage("C:\\Users\\Renan Fucci\\Dropbox\\RIO 45º\\testeQRCode\\020715000009525.jpg");

		// produces the same results
		procedural(ConvertBufferedImage.convertFromSingle(image, null, ImageUInt8.class));
		
	}
}