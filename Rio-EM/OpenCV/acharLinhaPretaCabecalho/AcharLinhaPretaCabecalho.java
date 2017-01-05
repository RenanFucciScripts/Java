package acharLinhaPretaCabecalho;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


public class AcharLinhaPretaCabecalho{

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

	}
	
	private Mat ImgSemLinhaPreta = new Mat();

	public AcharLinhaPretaCabecalho(String filePath, short limtlinhas) {
		try {
			Mat src = Imgcodecs.imread(filePath);
			double limit_qntdConectivos = src.cols() * 0.85d;

			Mat dst = src.submat(0, limtlinhas, 0, src.cols());

			Mat gray = new Mat();
			Imgproc.cvtColor(dst, gray, Imgproc.COLOR_RGB2GRAY);

			Mat dst_threshold = new Mat();
			Imgproc.threshold(gray, dst_threshold, 127, 255, 0);

			List<MatOfPoint> contours = new ArrayList<>();
			Mat hierarchy = new Mat();

			Imgproc.findContours(dst_threshold, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
			for (int i = 0; i < contours.size(); i++) {
				double qntdConectivos = Imgproc.contourArea(contours.get(i));
				if ((qntdConectivos > limit_qntdConectivos) && (qntdConectivos / limit_qntdConectivos) <= limtlinhas) {
					MatOfPoint pt = contours.get(i);
					for (int j = 0; j < dst.rows(); j++) {
						for (int k = 0; k < dst.cols(); k++) {
							if (naoExistePt(pt, k, j)) {
								dst.put(j, k, new double[]{255,255,255});
							}
						}
					}

				}
			}
			for (int i = 0; i < dst.rows(); i++) {
				for (int j = 0; j < dst.cols(); j++) {
					src.put(i, j, dst.get(i, j));
				}
			}
			ImgSemLinhaPreta = src;
		} catch (Exception ex) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
		}
	}

	private boolean naoExistePt(MatOfPoint pt, int x, int y) {
		for (Point point : pt.toList()) {
			if (((int) point.x == x) && ((int) point.y == y))
				return false;
		}
		return true;
	}

	public Mat getImgSemLinhaPreta() {
		return ImgSemLinhaPreta;
	}

}
