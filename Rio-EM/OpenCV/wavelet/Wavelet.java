package wavelet;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * @author Renan Fucci
 *
 *http://answers.opencv.org/question/42273/wavelet-transform/
 *http://stackoverflow.com/questions/20071854/wavelet-transform-in-opencv
 */
public class Wavelet {


	static{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	String pathname = "C:/Users/Renan Fucci/Dropbox/Estante Magica_Renan_Fucci/Pasta_Renan_Fucci/2016-06-16/Imagens_Manchas/img096";


	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		Wavelet wavelet = new Wavelet();
		wavelet.applyHaarFoward();
		System.out.println(((System.currentTimeMillis() - start)/1000.0)+"\t segundos");
		
		//imd.convertTo(imd, CvType.CV_8U);
		Imgcodecs.imwrite(wavelet.pathname+"imi.jpg", wavelet.imi);
		Imgcodecs.imwrite(wavelet.pathname+"imd.jpg", wavelet.imd);
		//imr.convertTo(imr, CvType.CV_8U);
		Imgcodecs.imwrite(wavelet.pathname+"imr.jpg", wavelet.imr);
	}

	Mat im,im1,im2,im3,im4,im5,im6,temp,im11,im12,im13,im14,imi,imd,imr;
	float a,b,c,d;

	private void applyHaarFoward(){
		try{
			im = Imgcodecs.imread(pathname+".jpg", 0);
			imi = new Mat(im.rows(), im.cols(), CvType.CV_8U);
			im.copyTo(imi);

			//in CvType. If the number of channels is omitted, it evaluates to 1. 
			im.convertTo(im, CvType.CV_32F, 1.0, 0.0);
			im1 = new Mat(im.rows()/2, im.cols(), CvType.CV_32F);
			im2 = new Mat(im.rows()/2, im.cols(), CvType.CV_32F);

			im3 = new Mat(im.rows()/2, im.cols()/2, CvType.CV_32F);
			im4 = new Mat(im.rows()/2, im.cols()/2, CvType.CV_32F);

			im5 = new Mat(im.rows()/2, im.cols()/2, CvType.CV_32F);
			im6 = new Mat(im.rows()/2, im.cols()/2, CvType.CV_32F);


			// ------------------- Decomposition ------------------- 

			for (int rcnt = 0; rcnt < im.rows(); rcnt+=2) {
				for (int ccnt = 0; ccnt < im.cols(); ccnt++) {
					//even though the CvType is float with only one channel
					//the method Mat.get() return a double array 
					//with only one position, [0].
					a = (float) im.get(rcnt, ccnt)[0];
					b = (float) im.get(rcnt+1, ccnt)[0];
					c = (float) ((a+b)*0.707);
					d = (float) ((a-b)*0.707);
					int _rcnt= rcnt/2;
					im1.put(_rcnt, ccnt, c);
					im2.put(_rcnt, ccnt, d);

				}
			}

			//Second
			for (int rcnt = 0; rcnt < im.rows()/2; rcnt++) {
				for (int ccnt = 0; ccnt < im.cols() - 2; ccnt+=2) {
					a = (float) im1.get(rcnt, ccnt)[0];
					b = (float) im1.get(rcnt, ccnt+1)[0];
					c = (float) ((a+b)*0.707);
					d = (float) ((a-b)*0.707);
					int _ccnt = ccnt/2;
					im3.put(rcnt, _ccnt, c);
					im4.put(rcnt, _ccnt, d);
				}
			}

			//Third
			for (int rcnt = 0; rcnt < im.rows()/2; rcnt++) {
				for (int ccnt = 0; ccnt < im.cols() - 2; ccnt+=2) {
					a = (float) im2.get(rcnt, ccnt)[0];
					b = (float) im2.get(rcnt, ccnt+1)[0];
					c = (float) ((a+b)*0.707);
					d = (float) ((a-b)*0.707);
					int _ccnt = ccnt/2;
					im5.put(rcnt, _ccnt, c);
					im6.put(rcnt, _ccnt, d);
				}
			}

			imr = Mat.zeros(im.rows(), im.cols(), CvType.CV_32F);//imr = Mat.zeros(512, 512, CvType.CV_32F);
			imd = Mat.zeros(512, 512, CvType.CV_32F);
			im3.copyTo(imd.adjustROI(0, 0, 256, 256));
			im4.copyTo(imd.adjustROI(0, 255, 256, 256));
			im5.copyTo(imd.adjustROI(255, 0, 256, 256));
			im6.copyTo(imd.adjustROI(255, 255, 256, 256));





		}catch(Exception ex){
			System.err.println(ex.getLocalizedMessage());
			ex.printStackTrace();
		}
	}
	
	private void applyHaarReverse(){
		// ------------------- Reconstruction ------------------- 
				im11 = Mat.zeros(im.rows()/2, im.cols(), CvType.CV_32F);
				im12 = Mat.zeros(im.rows()/2, im.cols(), CvType.CV_32F);
				im13 = Mat.zeros(im.rows()/2, im.cols(), CvType.CV_32F);
				im14 = Mat.zeros(im.rows()/2, im.cols(), CvType.CV_32F);
				
				for (int rcnt = 0; rcnt < im.rows()/2; rcnt++) {
					for (int ccnt = 0; ccnt < im.cols()/2; ccnt++) {
						int _ccnt  = ccnt*2;
						im11.put(rcnt, _ccnt, im3.get(rcnt, ccnt));
						im12.put(rcnt, _ccnt, im4.get(rcnt, ccnt));
						im13.put(rcnt, _ccnt, im5.get(rcnt, ccnt));
						im14.put(rcnt, _ccnt, im6.get(rcnt, ccnt));
					}
				}
				
				for (int rcnt = 0; rcnt < im.rows()/2; rcnt++) {
					for (int ccnt = 0; ccnt < im.cols() - 2; ccnt+=2) {
						a = (float) im11.get(rcnt, ccnt)[0];
						b = (float) im12.get(rcnt, ccnt)[0];
						c = (float) ((a+b)*0.707);
						im11.put(rcnt, ccnt, c);
						d = (float) ((a-b)*0.707);
						im11.put(rcnt, ccnt+1, d);
						
						a = (float) im13.get(rcnt, ccnt)[0];
						b = (float) im14.get(rcnt, ccnt)[0];
						c = (float) ((a+b)*0.707);
						im13.put(rcnt, ccnt, c);
						d = (float) ((a-b)*0.707);
						im13.put(rcnt, ccnt+1, d);
					}
				}
				
				temp = Mat.zeros(im.rows(), im.cols(), CvType.CV_32F);
				
				for (int rcnt = 0; rcnt < im.rows()/2; rcnt++) {
					for (int ccnt = 0; ccnt < im.cols(); ccnt++) {
						int _rcnt = rcnt*2;
						imr.put(_rcnt, ccnt, im11.get(rcnt, ccnt));
						temp.put(_rcnt, ccnt, im13.get(rcnt, ccnt));
					}
				}
				
				
				for (int rcnt = 0; rcnt < im.rows()-2; rcnt+=2) {
					for (int ccnt = 0; ccnt < im.cols(); ccnt++) {
						a = (float) imr.get(rcnt, ccnt)[0];
						b = (float) temp.get(rcnt, ccnt)[0];
						c = (float) ((a+b)*0.707);
						imr.put(rcnt, ccnt, c);
						d = (float) ((a-b)*0.707);
						imr.put(rcnt+1, ccnt, d);
					}
				}
				
	}
}
