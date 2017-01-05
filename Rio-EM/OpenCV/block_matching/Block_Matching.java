package block_matching;

import org.opencv.calib3d.StereoBM;
import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import _openCV.ImageViewer;

/**
 * EXAMPLE: https://fossies.org/dox/opencv-3.1.0/SBM__Sample_8cpp_source.html
 * 
 * */
public class Block_Matching {

	static{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args) {
		ImageViewer viewer =  new ImageViewer();

		String filename = "C:/Users/Renan Fucci/Dropbox/Estante Magica_Renan_Fucci/Pasta_Renan_Fucci/2016-04-28/teste/8";

		Mat left = Imgcodecs.imread(filename+".jpg" , Imgcodecs.IMREAD_GRAYSCALE);//image
		Mat right = Imgcodecs.imread(filename+".jpg" , Imgcodecs.IMREAD_GRAYSCALE);//image
		Mat disparity16S = new Mat(left.size(), CvType.CV_16S);
		Mat disparity8U = new Mat(left.size(), CvType.CV_8UC1);


		int numDisparities = 16*5; //divisivel p 16
		int blockSize = 21; // 5 <= blockSize <=255
		StereoBM stereoBM = StereoBM.create(numDisparities, blockSize);
		stereoBM.compute(left, right, disparity16S);

		MinMaxLocResult minMaxresult = Core.minMaxLoc(disparity16S);

		double minVal = minMaxresult.minVal;
		double maxVal = minMaxresult.maxVal;

		disparity16S.convertTo(disparity8U, CvType.CV_8UC1, 255/(maxVal-minVal));


		viewer.show(left, "left");
		viewer.show(right, "right");
		viewer.show(disparity16S, "disparity16");
		viewer.show(disparity8U, "disparity8");

		Imgcodecs.imwrite(filename+"_blockMatching(disparity16).png", disparity16S);
		Imgcodecs.imwrite(filename+"_blockMatching(disparity8).png", disparity8U);

	}
}
