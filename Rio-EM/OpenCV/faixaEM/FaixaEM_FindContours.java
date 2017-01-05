package faixaEM;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * http://stackoverflow.com/questions/10262600/how-to-detect-region-of-large-of-white-pixels-using-opencv
 * */
public class FaixaEM_FindContours extends Imgproc{

	static{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

	}

	public static void main(String[] args) {
		try{
			String filename = "C:/Users/Renan Fucci/Dropbox/Estante Magica_Renan_Fucci/Pasta_Renan_Fucci/2016-05-12/teste_FindCountours/Capa (1)";
			Mat img = Imgcodecs.imread(filename+".jpeg");
			Mat src = img;
			img = new Mat();
			resize(src, img, new Size(400, 500));
			Mat dst;
			Mat gray;
			Mat gray2;
			Mat mask;
			int mode = RETR_LIST;
			int method = CHAIN_APPROX_SIMPLE;
			int tamMin_contour = 5000;
			int tamMax_contour = 50000;

			dst = new Mat();
			gray = new Mat();
			gray2 = new Mat();

			cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);
			threshold(gray, dst, 180, 255, Imgcodecs.IMREAD_GRAYSCALE);
			gray = dst;
			gray.copyTo(gray2);
			mask = new Mat(gray.rows(), gray.cols(), gray.type(), new Scalar(0));

			List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

			Mat hierarchy = new Mat();
			findContours(gray, contours, hierarchy , mode, method);
			for (MatOfPoint cnt : contours) {
				if(tamMin_contour<contourArea(cnt) && contourArea(cnt)<tamMax_contour){
					int contourIdx=0;
					int thickness=2;
					Scalar color = new Scalar(0, 255, 0);
					drawContours(img, contours, contourIdx, color ,thickness);
					Scalar color2 = new Scalar(255);
					int thickness2= -1;
					drawContours(mask, contours, contourIdx, color2, thickness2  );


				}
			}
			Core.bitwise_not(gray2, gray2, mask);
			
			Imgcodecs.imwrite(filename+"_countours.jpg", img);
			Imgcodecs.imwrite(filename+"_mask.jpg", mask);
			Imgcodecs.imwrite(filename+"_gray.jpg", gray);
			Imgcodecs.imwrite(filename+"_gray2.jpg", gray2);
			Imgcodecs.imwrite(filename+"_hierarchy.jpg", hierarchy);

		}catch(Exception ex){
			System.err.println(ex.getMessage());
			ex.printStackTrace();
		}
	}
}
