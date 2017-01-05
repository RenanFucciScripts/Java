package _openCV;

import org.opencv.core.Core;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class OpenCVs {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

	}

	public class OpenCVImgproc extends Imgproc {

	}

	public class OpenCVImgcodecs extends Imgcodecs {

	}

}
