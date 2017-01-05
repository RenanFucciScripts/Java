package faixaEM;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * http://stackoverflow.com/questions/13538748/crop-black-edges-with-opencv
 * */
public class FaixaEM_blackBorderRemoval extends Imgproc{
	
	static{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	public static void main(String[] args) {
		String filename= "C:/Users/Renan Fucci/Dropbox/Estante Magica_Renan_Fucci/Pasta_Renan_Fucci/2016-05-13/black_border_removal/";
		Mat img =Imgcodecs.imread(filename+"Black_Border_Removal.jpeg");
		Mat gray = new Mat();
	
		cvtColor(img, gray, COLOR_BGR2GRAY);
		Mat thresh = new Mat();
		threshold(gray, thresh , 10, 255, THRESH_BINARY);
		List<MatOfPoint> contours =  new ArrayList<>();
		Mat hierarchy = new Mat();
		int mode = RETR_EXTERNAL;
		int method = CHAIN_APPROX_SIMPLE;
		findContours(thresh, contours , hierarchy , mode, method);
		MatOfPoint cnt = contours.get(0);
		Rect rect = boundingRect(cnt);

		Mat crop = img.submat(rect);
		Imgcodecs.imwrite(filename+"Black_Border_Removal_crop.jpg", crop);
	}

}
