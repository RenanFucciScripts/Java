package wavelet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import metodos.MetodosRF;

public class Daub {
	
	static ArrayList<double[]> fowardList = new ArrayList<double[]>();
	static ArrayList<double[]> inverseList = new ArrayList<double[]>();
	private static int width=0;
	private static int height = 0;
	//
	protected final double sqrt_3 = Math.sqrt( 3 );
	protected final double denom = 4 * Math.sqrt( 2 );
	
	//
	// forward transform scaling (smoothing) coefficients
	//
	protected final double h0 = (1 + sqrt_3)/denom;
	protected final double h1 = (3 + sqrt_3)/denom; 
	protected final double h2 = (3 - sqrt_3)/denom; 
	protected final double h3 = (1 - sqrt_3)/denom;
	
	//
	// forward transform wavelet coefficients
	//
	protected final double g0 =  h3;
	protected final double g1 = -h2;
	protected final double g2 =  h1;
	protected final double g3 = -h0;

	//
	// Inverse transform coefficients for smoothed values
	//
	protected final double Ih0 = h2;
	protected final double Ih1 = g2;  // h1
	protected final double Ih2 = h0;
	protected final double Ih3 = g0;  // h3
	//
	// Inverse transform for wavelet values
	//
	protected final double Ig0 = h3;
	protected final double Ig1 = g3;  // -h0
	protected final double Ig2 = h1;
	protected final double Ig3 = g1;  // -h2

	
	/**
     <p>
     	Forward wavelet transform.
     </p>
     <p>
	    Note that at the end of the computation the
	    calculation wraps around to the beginning of
	    the signal.
	 </p>
	 */
	protected double[] transform( double a[], int n ){
		if (n >= 4) {
			int i, j;
			int half = n >> 1;
			double tmp[] = new double[n];
			i = 0;
			for (j = 0; j < n-3; j = j + 2) {
				tmp[i]      = a[j]*h0 + a[j+1]*h1 + a[j+2]*h2 + a[j+3]*h3;
				tmp[i+half] = a[j]*g0 + a[j+1]*g1 + a[j+2]*g2 + a[j+3]*g3;
				i++;
			}

			tmp[i]      = a[n-2]*h0 + a[n-1]*h1 + a[0]*h2 + a[1]*h3;
			tmp[i+half] = a[n-2]*g0 + a[n-1]*g1 + a[0]*g2 + a[1]*g3;

			for (i = 0; i < n; i++) {
				a[i] = tmp[i];
			}
		}
		return a;
	} // transform


	protected double[] invTransform( double a[], int n ){
		if (n >= 4) {
			int i, j;
			int half = n >> 1;
			int halfPls1 = half + 1;
			double tmp[] = new double[n];
			//      last smooth val  last coef.  first smooth  first coef
			tmp[0] = a[half-1]*Ih0 + a[n-1]*Ih1 + a[0]*Ih2 + a[half]*Ih3;
			tmp[1] = a[half-1]*Ig0 + a[n-1]*Ig1 + a[0]*Ig2 + a[half]*Ig3;
			j = 2;
			for (i = 0; i < half-1; i++) {
				//     smooth val     coef. val       smooth val    coef. val
				tmp[j++] = a[i]*Ih0 + a[i+half]*Ih1 + a[i+1]*Ih2 + a[i+halfPls1]*Ih3;
				tmp[j++] = a[i]*Ig0 + a[i+half]*Ig1 + a[i+1]*Ig2 + a[i+halfPls1]*Ig3;
			}
			for (i = 0; i < n; i++) {
				a[i] = tmp[i];
			}
		}
		return a;
	}


	
	/**
     Forward Daubechies D4 transform
	 */
	public void daubTrans( double s[] ){
		final int N = s.length;
		int n;
		for (n = N; n >= 4; n >>= 1) {
			transform( s, n );
		}
	}


	/**
     Inverse Daubechies D4 transform
	 */
	public void invDaubTrans( double coef[]){
		final int N = coef.length;
		int n;
		for (n = 4; n <= N; n <<= 1) {
			invTransform( coef, n );
		}
	}

	public static double[] array2dto1d(int[][] image){
		double[] array = new double[image.length * image[0].length];
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				array[(i * image[0].length) +j] =  (double) image[i][j];
			}
		}
		return array;
	}
	
	public static int[][] array1dto2d(double[] image){
		int[][] imagemOut = new int[height][width];
	
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				imagemOut[i][j] = (int)Math.abs( image[(i * width) +j]);
				
			}
		}
		return imagemOut;
	}
	
	public static void main(String[] args) throws Exception {
		MetodosRF methods = new MetodosRF();
		Daub daub =  new Daub();
		
		String dir = "C:\\Users\\Renan Fucci\\Desktop\\ImgTeste\\";
		
		int[][] red = methods.leImagem(dir+"img.jpg", "Red");
		width=red[0].length;
		height=red.length;
		int[][] green= methods.leImagem(dir+"img.jpg", "Green");
		int[][] blue = methods.leImagem(dir+"img.jpg", "Blue");
		
		System.out.println("w: "+width);
		System.out.println("h: "+height);
		
		double[] vetRed = array2dto1d(red);
		System.out.println("len: "+vetRed.length);
		System.out.println("Size: "+fowardList.size());
		daub.daubTrans(vetRed);
		daub.invDaubTrans(vetRed);
		int cont=0;
		for (double[] ds : inverseList) {
			int[][] redOUt = array1dto2d(ds);
			imprimir(redOUt);
			BufferedImage buff =  methods.matrizCinzaToBufferImage(redOUt);
			ImageIO.write(buff, "JPG", new File(dir+cont+"imgOut.jpg"));
		}
		
		
		daub.invDaubTrans(vetRed);
		

		double[] vetGreen = array2dto1d(green);
		double[] vetBlue = array2dto1d(blue);
		
		int[][] greenOUt = array1dto2d(vetGreen);
		int[][] blueOUt = array1dto2d(vetBlue);
		
		
		//BufferedImage buffer = methods.matrizColorToBufferImage(redOUt, greenOUt, blueOUt);	
		//ImageIO.write(buffer, "JPG", new File(dir+"imgOut.jpg"));
		
		
	}
	
	private static void imprimir(int[][] img) {
		for (int i = 0; i < img.length; i++) {
			for (int j = 0; j < img[0].length; j++) {
				if(img[i][j]>255 || img[i][j]<0)
					System.out.println(img[i][j]);
			}
		}
	}
} 


