package transformada;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.imageio.ImageIO;

import metodos.MetodosEM;

public class FWT{

	public static void main(String[] args) {
		try {
			String dir ="C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2015-11-06\\";
			BufferedImage imgDB=
					ImageIO.read(new File(dir+"230915000031660.jpg"));
			int[][] img = MetodosEM.buffToMatriz(ImageIO.read(new File(dir+"230915000031660.jpg")));
			/*
			double[][] imgDB = new double[img.length][img[0].length];
			double[][] imgFDB = new double[img.length][img[0].length];
			double[][] imgRDB = new double[img.length][img[0].length];

			for (int i = 0; i < img.length; i++) {
				for (int j = 0; j < img[i].length; j++) {
					imgDB[i][j] =  (double) img[i][j];
				}
			}*/
			//ImageIO.write(MetodosRF.matrizCinzaToBufferImage(imgFDB), "JPG", new File(dir+"(FowrdFWT) 230915000031660.jpg"));

			BufferedImage imgFDB = fowardFastWaveletTransform(imgDB);
			ImageIO.write(imgFDB, "JPG", new File(dir+"(FowrdFWT) 230915000031660.jpg"));

			BufferedImage imgRDB = reverseFastWaveletTransform(imgFDB);
			ImageIO.write(imgRDB, "JPG", new File(dir+"(ReverFWT) 230915000031660.jpg"));
			//ImageIO.write(MetodosRF.matrizCinzaToBufferImage(imgRDB), "JPG", new File(dir+"(ReverFWT) 230915000031660.jpg"));

			
		} catch (Exception e) {
			System.err.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	/*
	public  double[][] fowardFastWaveletTransform(double[][] img ) {
		double[][] imgFoward =  new double[img.length][img[0].length];
		//double delta=1.e-12;
		Transform t = new Transform(new FastWaveletTransform(Filter),this.levels);
		imgFoward = t.forward(img);
		return imgFoward;

	}

	public static double[][] reverseFastWaveletTransform(double[][] img ) {
		/*double[][] imgReverse =  new double[img.length][img[0].length];
		Transform t =  new Transform(new AncientEgyptianDecomposition(new FastWaveletTransform(new Haar1())));
		imgReverse = t.reverse(img);
		return imgReverse;

	}
	*/

	private static BufferedImage reverseFastWaveletTransform(BufferedImage image) {
		int idxsrc=1;
		int[] arraySrc=((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		IndexedIntArray src = new IndexedIntArray(arraySrc, idxsrc);
		
		int idxdst=idxsrc+idxsrc;
		int[] arrayDst = new int[arraySrc.length];
		IndexedIntArray dst =  new IndexedIntArray(arrayDst, idxdst);
		
		DWT_CDF_9_7 transform =  new DWT_CDF_9_7(image.getWidth(), image.getHeight());
		transform.inverse(src, dst);
		
		BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		WritableRaster raster =  (WritableRaster) out.getRaster();
		raster.setPixels(0, 0, out.getWidth(), out.getHeight(), dst.array);
		out.setData(raster);

		return out;
	}

	private static BufferedImage fowardFastWaveletTransform(BufferedImage image) {
		int idxsrc=1;
		int[] arraySrc=((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		IndexedIntArray src = new IndexedIntArray(arraySrc, idxsrc);
		
		int idxdst=idxsrc+idxsrc;
		int[] arrayDst = new int[arraySrc.length];
		IndexedIntArray dst =  new IndexedIntArray(arrayDst, idxdst);
		
		DWT_CDF_9_7 transform =  new DWT_CDF_9_7(image.getWidth(), image.getHeight());
		transform.forward(src, dst);
		
		BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		WritableRaster raster =  (WritableRaster) out.getRaster();
		raster.setPixels(0, 0, out.getWidth(), out.getHeight(), dst.array);
		out.setData(raster);

		return out;
	}
}
