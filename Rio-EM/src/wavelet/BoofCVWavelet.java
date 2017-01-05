package wavelet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import boofcv.abst.transform.wavelet.impl.WaveletTransformFloat32;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.factory.transform.wavelet.GFactoryWavelet;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.wavelet.WaveletDescription;
import boofcv.struct.wavelet.WlCoef_F32;

public class BoofCVWavelet {

	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException {
		String dir = "C:\\Users\\Renan Fucci\\Desktop\\ImgTeste\\";

		ImageFloat32 image =UtilImageIO.loadImage(dir+"img.jpg", ImageFloat32.class);
		
		//Daubechies only j=4;
		//Bioorthogonal only j=5;
		//Coiflet only j=6;
		//Haar não tem j;
		WaveletDescription<WlCoef_F32> desc = GFactoryWavelet.haar(ImageFloat32.class);
		WaveletTransformFloat32 transform = new WaveletTransformFloat32(desc, 1, 0, 255);
		ImageFloat32 imageFloat32 = new ImageFloat32(image.width, image.height);
		imageFloat32=  transform.transform(image, imageFloat32);
		
		for (int i = 0; i < imageFloat32.height; i++) {
			for (int j = 0; j < imageFloat32.width; j++) {
				if(imageFloat32.get(j, i)<0){
					System.out.println(i+"\t"+j);
					System.out.println(imageFloat32.get(j, i));
				}
			}
		}
		BufferedImage buffForwad = ConvertBufferedImage.convertTo(imageFloat32, null);
		
		ImageIO.write(buffForwad, "PNG", new File(dir+"forward.png"));
		/*
		ImageFloat32 invertF32 = new ImageFloat32(image.width, image.height);
		transform.invert(imageFloat32, invertF32);
		
		BufferedImage buffInverse = ConvertBufferedImage.convertTo(invertF32, null);
		ImageIO.write(buffInverse, "PNG", new File(dir+"invert.png"));*/
		
		
		
		
		/*
		ImageUInt16 aux = UtilImageIO.loadImage(dir+"img.png", ImageUInt16.class);
		
		ImageSInt16 input = new ImageSInt16(aux.width, aux.height);
		input =ConvertImage.convert(aux, input);
		ImageSInt32 output = new ImageSInt32(aux.width, aux.height);

		int minPixelValue = 0;
		int maxPixelValue = 255;
		WaveletTransform<ImageSInt16, ImageSInt32, WlCoef_I32> transform =  
				FactoryWaveletTransform.create_I(FactoryWaveletHaar.generate(true, 0), 1, 
						minPixelValue, maxPixelValue,ImageSInt16.class);
		output=transform.transform(input, output);

		//BufferedImage out = ConvertBufferedImage.convertTo(output, null);
		BufferedImage out = new BufferedImage(input.width*6, input.height*6, 5);
		 out = VisualizeBinaryData.renderLabeled(output, 0, out);
		ImageIO.write(out, "PNG", new File(dir+"out.png"));
		
		WaveletTransformOps wavTransformOps= new WaveletTransformOps();
		BorderIndex1D border = new BorderIndex1D() {

			@Override
			public int getIndex(int arg0) {
				// TODO Auto-generated method stub
				return 0;
			}
		};
		WlCoef_F32 forward = new WlCoef_F32();
		WlBorderCoef<WlCoef_F32> inverse = null;

		WaveletDescription<WlCoef_F32> desc = new WaveletDescription<WlCoef_F32>(border, forward,
																					inverse);
		wavTransformOps.transform1(desc, input, output, storage);

		BufferedImage out = ConvertBufferedImage.convertTo(output, null);
		ImageIO.write(out, "PNG", new File(dir+"out.png"));
		 */	
	}
}
