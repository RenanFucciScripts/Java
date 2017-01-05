package piramide;



import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.zxing.common.DetectorResult;
import com.sun.corba.se.pept.transport.Connection;

import boofcv.core.image.ConvertBufferedImage;
import boofcv.core.image.GeneralizedImageOps;
import boofcv.factory.filter.kernel.FactoryKernel;
import boofcv.factory.transform.pyramid.FactoryPyramid;
import boofcv.gui.image.DiscretePyramidPanel;
import boofcv.struct.convolve.Kernel1D;
import boofcv.struct.image.ImageSingleBand;
import boofcv.struct.image.ImageUInt8;
import boofcv.struct.pyramid.PyramidDiscrete;

/**
 * Demonstrates how to construct and display a {@link PyramidDiscrete}.  Discrete pyramids require that
 * each level has a relative scale with an integer ratio and is updated by sparsely sub-sampling.  These
 * restrictions allows a very quick update across scale space.
 *
 * @author Peter Abeles
 */
public class ExamplePyramidDiscrete<T extends ImageSingleBand> {

	// specifies the image type
	Class<T> imageType;
	// The pyramid data structure
	PyramidDiscrete<T> pyramid;
	static BufferedImage outImg;

	public ExamplePyramidDiscrete(Class<T> imageType) {
		this.imageType = imageType;
	}

	/**
	 * Creates a fairly standard pyramid and updater.
	 */
	public void standard() {
		// Each level in the pyramid must have a ratio with the previously layer that is an integer value
		pyramid = FactoryPyramid.discreteGaussian(new int[]{1,2,4,8},-1,2,true,imageType);
	}

	/**
	 * Creates a more unusual pyramid and updater.
	 */
	public void unusual() {
		// Note that the first level does not have to be one
		pyramid = FactoryPyramid.discreteGaussian(new int[]{2,6},-1,2,true,imageType);

		// Other kernels can also be used besides Gaussian
		Kernel1D kernel;
		if(GeneralizedImageOps.isFloatingPoint(imageType) ) {
			kernel = FactoryKernel.table1D_F32(2,true);
		} else {
			kernel = FactoryKernel.table1D_I32(2);
		}
	}

	/**
	 * Updates and displays the pyramid.
	 * @return 
	 */
	public BufferedImage process( BufferedImage image, int level) {
		T input = ConvertBufferedImage.convertFromSingle(image, null, imageType);
		pyramid.process(input);

		DiscretePyramidPanel gui = new DiscretePyramidPanel();
		gui.setPyramid(pyramid);
		gui.render();

		//ShowImages.showWindow(gui,"Image Pyramid");

		// To get an image at any of the scales simply call this get function
		T imageAtScale = pyramid.getLayer(level);
		outImg=ConvertBufferedImage.convertTo(imageAtScale,null,true);
		//ShowImages.showWindow(ConvertBufferedImage.convertTo(imageAtScale,null,true),"Image at layer 1");
		return outImg;
	}

	public static void main( String[] args ) throws IOException {
		String dir = "C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2016-01-20\\imgs\\230915000031705.jpg";

		BufferedImage image = ImageIO.read(new File(dir));
		long start =  System.currentTimeMillis();
		int level = 1;

		//ExamplePyramidDiscrete<ImageFloat32> app = new ExamplePyramidDiscrete<ImageFloat32>(ImageFloat32.class);
		ExamplePyramidDiscrete<ImageUInt8> app = new ExamplePyramidDiscrete<ImageUInt8>(ImageUInt8.class);

		app.standard();
//		app.unusual();
		app.process(image, level);
		QRCode qrCode = new QRCode();
		Connection conn = null;
		DetectorResult detectorResult = qrCode.leQRCode(conn , outImg);
		System.out.println(detectorResult);
		System.out.println(((System.currentTimeMillis() - start)/1000.0)+" segundos");
		ImageIO.write(outImg, "JPG", new File("C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2016-01-18\\piramideDiscrete (java)\\img("+level+").jpg"));
		
	}
}	