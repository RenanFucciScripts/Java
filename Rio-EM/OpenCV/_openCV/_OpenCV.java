package _openCV;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.objdetect.HOGDescriptor;

public class _OpenCV {


	static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}//nao apagar


	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo getHuMoments</b><br>
	 * Metodo para pegar os Hu Moments de uma imagem.
	 * @param img imagem que serao extraidos os huMoments.
	 * @return vetor de double com os Hu Moments.
	 */
	public static double[] getHuMoments(BufferedImage img){
		Moments moments = new Moments();
		moments = Imgproc.moments(bufferImgToMat_Gray(img), true);
		Mat hu = new Mat();
		Imgproc.HuMoments(moments, hu);
		System.out.println(moments);
		System.out.println(hu.cols());
		System.out.println(hu.rows());
		double[] huMoments= new double[7]; 
		hu.get(0, 0, huMoments);

		return huMoments;
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo bufferImgToMat_Gray<br></b>
	 * Metodo para converter uma imagem no padrao Java em uma imagem no padrao da biblioteca OpenCV com uma so banda.<br>
	 * @param img imagem a ser convertida;
	 * @return imagem convertida.
	 */
	public static Mat bufferImgToMat_Gray(BufferedImage img) {
		Mat mat = new Mat(img.getHeight(), img.getWidth(), CvType.CV_8U);
		byte[] data = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
		mat.put(0, 0, data);
		return mat;
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo processPyramidDinamic_OpenCV<br></b>
	 * Metodo para aplicar o metodo Pyramid da biblioteca OpenCV em uma imagem com o nivel dinamico, entre 0 e 2, somente.<br>
	 * Aplica o nivel da pyramid ate que o menor dos lados (altura ou largura) seja maior que 400 pixels, contudo que o nivel esteja entre 0 e 2.
	 * @param image imagem a ser processada;
	 * @return saida imagem resultante do metodo piramide e seu respectivo nivel.
	 */
	public static BufferedImage processPyramidDinamic_OpenCV(BufferedImage image) {
		String ladoMenor; //var para saber lado menor da imagem
		double nivel; //nivel a ser calculado
		Mat source = bufferImgToMat_RGB(image); //ImgIn padrao OpenCV
		Mat destination = new Mat(source.rows()/2,source.cols()/2, source.type());//ImgOut padrao OpenCV
		destination = source;
		ladoMenor = (source.height() > source.width()) ? "w" : "h";
		nivel  = ladoMenor.contentEquals("w")? (Math.ceil(source.width() /400.0)) : (Math.ceil(source.height()/400.0));
		for (int i = 2; i < nivel; i+=2) {
			Imgproc.pyrDown(source, destination, new Size(source.cols()/2,  source.rows()/2));
			source = destination;
		}
		return matToBufferImg(destination);
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo bufferImgToMat_RGB<br></b>
	 * Metodo para converter uma imagem no padrao Java em uma imagem no padrao da biblioteca OpenCV.<br>
	 * @param img imagem a ser convertida;
	 * @return imagem convertida.
	 */
	public static Mat bufferImgToMat_RGB(BufferedImage img) {
		Mat mat = new Mat(img.getHeight(), img.getWidth(), CvType.CV_8UC3);
		byte[] data = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
		mat.put(0, 0, data);
		return mat;
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo matToBufferImg<br></b>
	 * Metodo para converter uma imagem no padrao da biblioteca OpenCV em uma imagem no padrao Java.<br>
	 * @param img imagem a ser convertida;
	 * @return imagem convertida.
	 */
	public static BufferedImage matToBufferImg(Mat img){
		int type = BufferedImage.TYPE_BYTE_GRAY;
		if ( img.channels() > 1 ) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}
		int bufferSize = img.channels()*img.cols()*img.rows();
		byte [] buffer = new byte[bufferSize];
		img.get(0,0,buffer);
		BufferedImage image = new BufferedImage(img.cols(),img.
				rows(), type);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().
				getDataBuffer()).getData();
		System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);
		return image;
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo getHogDescriptor<br></b>
	 * Metodo para pegar os descritores HOG de uma imagem da biblioteca OpenCV.<br>
	 * @param img imagem que sera extraido o descritor;
	 * @return HOGDescriptor.
	 */
	public static HOGDescriptor getHogDescriptor(BufferedImage img){
		HOGDescriptor hogDescriptor =  new HOGDescriptor();
		Mat mat = bufferImgToMat_RGB(img);
		MatOfFloat descriptors = new MatOfFloat();
		hogDescriptor.compute(mat, descriptors);			
		return hogDescriptor;

	}



	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo filtroMediana<br></b>
	 * Metodo para aplicar o filtro da mediana da biblioteca OpenCV.<br>
	 * @param image imagem que sera aplicado o filtro.
	 * @param kernelSize tamanho da janela de convolucao.
	 * @return imagem com o filtro da mediana.
	 */
	public static BufferedImage filtroMediana(BufferedImage image, int kernelSize){
		try{
			Mat src =  bufferImgToMat_RGB(image);
			Mat dst = new Mat();
			Imgproc.medianBlur(src, dst, kernelSize);
			return matToBufferImg(dst);
		}catch(Exception ex){
			System.err.println(ex.getLocalizedMessage());
			ex.printStackTrace();
		}
		return null;
	} 

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo filtroMedia<br></b>
	 * Metodo para aplicar o filtro da media da biblioteca OpenCV.<br>
	 * @param image imagem que sera aplicado o filtro.
	 * @param kernelSize tamanho da janela de convolucao.
	 * @return imagem com o filtro da mediana.
	 */
	public static BufferedImage filtroMedia(BufferedImage image, int kernelSize){
		try{
			Mat src  = bufferImgToMat_RGB(image);
			Mat dst = new Mat();
			Imgproc.blur(src, dst, new Size(kernelSize, kernelSize));
			return matToBufferImg(dst);
		}catch(Exception ex){
			System.err.println(ex.getLocalizedMessage());
			ex.printStackTrace();
		}
		return null;
	}

	public enum BandsRGB_OpenCV_MAT {
		RED(0), GREEN(1), BLUE(2);
		
		private int bandAsInteger;
		
		private BandsRGB_OpenCV_MAT(int i) {
			this.bandAsInteger = i;
		}
		
		public int getBandAsInteger() {
			return bandAsInteger;
		}
		
	}
	
	public static Mat separarBanda_Mat(Mat img, BandsRGB_OpenCV_MAT bands){
		Mat band = new Mat(img.size(), CvType.CV_8UC1);
		for (int i = 0; i < img.rows(); i++) {
			for (int j = 0; j < img.cols(); j++) {
				double[] data = img.get(i, j);
				band.put(i, j, data[bands.getBandAsInteger()]);
			}
		}
		return band;
	}

	public static Mat juntarBandas_Mat(Mat band_1, Mat band_2, Mat band_3){
		Mat img = new Mat(band_1.size(), CvType.CV_8UC3);
		for (int i = 0; i < img.rows(); i++) {
			for (int j = 0; j < img.cols(); j++) {
				double[] rs = band_1.get(i, j);
				double[] gs = band_2.get(i, j);
				double[] bs = band_3.get(i, j);

				double[] data =  {rs[0], gs[0], bs[0]};
				img.put(i, j, data);

			}
		}
		return img;
	}

	/**
	 * <b>@author Renan Fucci</b><br>
	 * <b>Metodo slides_pptToImgs</b><br>
	 * Metodo que dentro de um documento de apresentacao PPT ou PPTX pega todas as imagens e converte pra JPG, se necessário.
	 * @param fl arquivo no formato ppt ou pptx.
	 * @return lista de imagens no formato jpg.
	 */
	protected HashMap<String, BufferedImage> imgs_pptToImgs(File fl){
		HashMap<String, BufferedImage> listImgs = new HashMap<String, BufferedImage>();
		try {
			File file=fl;
			XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(file));
			List<XSLFPictureData> imgs = ppt.getPictureData();
			
			for (int i = 0; i < imgs.size(); i++) {
				byte[] imageInByte = imgs.get(i).getData();
				InputStream in = new ByteArrayInputStream(imageInByte);
				BufferedImage img= ImageIO.read(in);
				String key = (i==0)?("capa.jpg"):("ilustracao-"+i+".jpg");
				listImgs.put(key, toJPG(img));
			}
			ppt.close();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}		
		return listImgs;
	}
	
	/**
	 * <b>@author Renan Fucci</b><br>
	 * <b>Metodo toJPG</b><br>
	 * Metodo para converter qualquer tipo de extensao de imagem para jpg.
	 * @param bufferedImage imagem a ser convertida.
	 * @return imagem no padrao jpg.
	 */
	private static BufferedImage toJPG(BufferedImage bufferedImage){
		BufferedImage newBufferedImage ;//Buffer da img com o aplha
		newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
				bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);

		return newBufferedImage;
	}
	
//	public static void main(String[] args) {
//		String pathname = "C:/Users/Renan Fucci/Dropbox/Estante Magica_Renan_Fucci/Pasta_Renan_Fucci/2016-06-16/Imagens_Manchas/img096";
//		Mat src = Imgcodecs.imread(pathname+".jpg");
//		
//	}
	
	
	

	

}
