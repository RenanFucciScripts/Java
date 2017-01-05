package piramide;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.zxing.common.DetectorResult;
import com.sun.corba.se.pept.transport.Connection;

import boofcv.core.image.ConvertBufferedImage;
import boofcv.factory.transform.pyramid.FactoryPyramid;
import boofcv.gui.image.ImagePyramidPanel;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageSingleBand;
import boofcv.struct.pyramid.PyramidDiscrete;
import boofcv.struct.pyramid.PyramidFloat;

public class ExamplePyramidFloat<T extends ImageSingleBand> {
	
	/**
	 * Demonstrates how to construct and display a {@link PyramidFloat}.  Float pyramids require only require
	 * that each layer's scale be larger than the scale of the previous layer. Interpolation is used to allow
	 * sub-sampling at arbitrary scales.  All of this additional flexibility comes at the cost of speed
	 * when compared to a {@link PyramidDiscrete}.
	 *
	 * @author Peter Abeles
	 */
	
	// specifies the image type
	Class<T> imageType;
	// The pyramid data structure
	PyramidFloat<T> pyramid;
	static BufferedImage outImg;
	private static int level;
	
	public ExamplePyramidFloat(Class<T> imageType) {
		this.imageType =  imageType;
	}

	
	/**
	 * Creates a fairly standard pyramid and updater.
	 */
	public void standard() {
		// Scale factory for each layer can be any floating point value which is larger than
		// the previous layer's scale.
		double scales[] = new double[]{1,1.5,2,2.5,3,5,8,15};
		// the amount of blur which is applied to each layer in the pyramid after the previous layer has been sampled
		double sigmas[] = new double[]{1,1,1,1,1,1,1,1};
		pyramid = FactoryPyramid.floatGaussian(scales,sigmas,imageType);
	}

	/**
	 * Updates and displays the pyramid.
	 * @return 
	 */
	public BufferedImage process( BufferedImage image, int level ) {
		T input = ConvertBufferedImage.convertFromSingle(image, null, imageType);
		pyramid.process(input);
		
		ImagePyramidPanel<T> gui = new ImagePyramidPanel<T>();
		gui.set(pyramid, true);
		gui.render();

		//ShowImages.showWindow(gui,"Image Pyramid Float");

		// To get an image at any of the scales simply call this get function
		/*
		 * Niveis permitidos
		 * 1,00
		 * 1,50
		 * 2,00
		 * 2,50
		 * 3,00
		 * 5,00
		 * 8,00
		 * 15,00
		 * */
		T imageAtScale = pyramid.getLayer(level);
		outImg = ConvertBufferedImage.convertTo(imageAtScale,null,true);
		//ShowImages.showWindow(ConvertBufferedImage.convertTo(imageAtScale,null,true),"Image at layer 1");
		return outImg;
	}

	public static void main(String[] args) throws IOException {
		String dir = "C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2016-01-20\\imgs\\230915000031708.jpg";

		BufferedImage image = ImageIO.read(new File(dir));
		long start =  System.currentTimeMillis();
		int level = 1;
		ExamplePyramidFloat<ImageFloat32> app = new ExamplePyramidFloat<ImageFloat32>(ImageFloat32.class);
		//ExamplePyramidFloat<ImageUInt8> app = new ExamplePyramidFloat<ImageUInt8>(ImageUInt8.class);
		app.standard();
		app.process(image, level);
	
		QRCode qrCode = new QRCode();
		Connection conn = null;
		DetectorResult detectorResult = qrCode.leQRCode(conn , outImg);
		System.out.println(detectorResult);
		System.out.println(((System.currentTimeMillis() - start)/1000.0)+" segundos");
		//ImageIO.write(outImg, "JPG", new File("C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2016-01-18\\piramide (java)\\img(2).jpg"));
	}
}
