package pyramid;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * @author Renan Fucci
 * <br> A classe {@link EM_piramide} possui dois construtores.
 * <br> O construtor {@link EM_piramide#EM_piramide(BufferedImage)} vai fazer o metodo da piramide ate que um dos lados seja menor que 400 px.
 * <br> O construtor {@link EM_piramide#EM_piramide(BufferedImage, int)} vai fazer o metodo da piramide com o nivel passado como parâmetro.
 * <br> Para pegar o resultado da piramide, utilizando qualquer construtor primeiro, utilizar o {@link EM_piramide#getImg_piramide()}.
 * <br> E possivel pegar o nivel real aplicado na imagem atraves do {@link EM_piramide#getNivel()}.
 * <br> O {@link EM_piramide#remapearCords_img_pyrToOrig(int[][])} deve ser usado, utilizando as cordenadas da imagem resultado da piramide e seu respectivo nivel.
 */
public class EM_piramide {

	private BufferedImage img_piramide;
	private int nivel = 0;	


	/**
	 * @return the nivel
	 */
	public int getNivel() {
		return nivel;
	}

	/**
	 * @return the img_piramide
	 */
	public BufferedImage getImg_piramide() {
		return img_piramide;
	}


	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Construtor EM_piramide<br></b>
	 * Contrutor que ira receber uma imagem {@link BufferedImage}, 
	 * aplica o nivel da pyramid ate que o menor dos lados (altura ou largura) seja maior que 400 pixels.<br>
	 * Obs.: Para pegar a imagem resultado da piramide usar {@link EM_piramide#getImg_piramide()}}}
	 * @param image {@link BufferedImage} que sera aplicado a piramide;
	 */
	public EM_piramide(BufferedImage image) {
		this.img_piramide=processPyramidDinamic_OpenCV(image);
	}


	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Construtor EM_piramide<br></b>
	 * Contrutor que ira receber uma imagem {@link BufferedImage}, 
	 * aplica o nivel da pyramid desejado.<br>
	 * <b>O @param nivel deve ser um numero multiplo de 2.</b>
	 * Obs.: Para pegar a imagem resultado da piramide usar {@link EM_piramide#getImg_piramide()}}}
	 * @param image {@link BufferedImage} que sera aplicado a piramide;
	 */
	public EM_piramide(BufferedImage image, int nivel_div_Two){
		if((nivel_div_Two%2)==0){
			this.img_piramide=processPyramid_OpenCV(image, nivel_div_Two);
		}
		else{
			System.err.println("Nivel deve ser multiplo de 2.");
		}
	}


	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo getPontosCorrespPyramidDiscrete<br></b>
	 * Metodo para pegar o ponto correspondente da imagem menor na imagem maior do metodo {@link #processDiscretePyramid(BufferedImage, int)}. <br>
	 * OBS.: O parametro nivel deste metodo, deve ser igual ao {@link #processDiscretePyramid(BufferedImage, int)}.
	 * @param pontos matriz com os pontos da imagem pequena;
	 * @return matriz com os resultante dos pontos da imagem maior.
	 */
	public int[][] remapearCords_img_pyrToOrig(int[][] pontos){
		if(nivel==0){
			return pontos;
		}
		else if(nivel>0){
			int[][] final_pontos = new int[pontos.length][pontos[0].length];
			for (int i = 0; i < final_pontos.length; i++) {
				for (int j = 0; j < final_pontos[i].length; j++) {
					final_pontos[i][j] = pontos[i][j] * nivel;
				}
			}
			return final_pontos;
		}
		return null;
	}


	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo processPyramid_OpenCV<br></b>
	 * Metodo para aplicar o metodo Pyramid da biblioteca OpenCV em uma imagem com o nivel desejado.<br>
	 * @param image imagem a ser processada;
	 * @param nível do metodo pyramid.
	 * @return saida imagem resultante do metodo piramide e seu respectivo nivel.
	 */
	private BufferedImage processPyramid_OpenCV(BufferedImage image, int nivel_div_Two) {
		Mat source = bufferImgToMat_RGB(image); //ImgIn padrao OpenCV
		Mat destination = new Mat(source.rows()/2,source.cols()/2, source.type());//ImgOut padrao OpenCV
		for (int i = 2; i <= nivel_div_Two; i+=2) {
			this.nivel = i;
			Imgproc.pyrDown(source, destination, new Size(source.cols()/2,  source.rows()/2));
			source = destination;
		}
		return matToBufferImg(destination);
	}


	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo processPyramidDinamic_OpenCV<br></b>
	 * Metodo para aplicar o metodo Pyramid da biblioteca OpenCV em uma imagem com o nivel dinamico, entre 0 e 2, somente.<br>
	 * Aplica o nivel da pyramid ate que o menor dos lados (altura ou largura) seja maior que 400 pixels, contudo que o nivel esteja entre 0 e 2.
	 * @param image imagem a ser processada;
	 * @return saida imagem resultante do metodo piramide e seu respectivo nivel.
	 */
	private BufferedImage processPyramidDinamic_OpenCV(BufferedImage image) {
		String ladoMenor; //var para saber lado menor da imagem
		double nivel; //nivel a ser calculado
		Mat source = bufferImgToMat_RGB(image); //ImgIn padrao OpenCV
		Mat destination = new Mat(source.rows()/2,source.cols()/2, source.type());//ImgOut padrao OpenCV
		destination = source;
		ladoMenor = (source.height() > source.width()) ? "w" : "h";
		nivel  = ladoMenor.contentEquals("w")? (Math.floor(source.width() /400.0)) : (Math.floor(source.height()/400.0));
		for (int i = 2; i <= nivel; i+=2) {
			this.nivel = (int) nivel;
			Imgproc.pyrDown(source, destination, new Size(source.cols()/2,  source.rows()/2));
			source = destination;
		}
		return matToBufferImg(destination);
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo matToBufferImg<br></b>
	 * Metodo para converter uma imagem no padrao Java em uma imagem no padrao da biblioteca OpenCV.<br>
	 * @param img imagem a ser convertida;
	 * @return imagem convertida.
	 */
	private Mat bufferImgToMat_RGB(BufferedImage img) {
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
	private BufferedImage matToBufferImg(Mat img){
		int type = BufferedImage.TYPE_BYTE_GRAY;
		if ( img.channels() > 1 ) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}
		int bufferSize = img.channels()*img.cols()*img.rows();
		byte [] buffer = new byte[bufferSize];
		img.get(0,0,buffer); // get all the pixels
		BufferedImage image = new BufferedImage(img.cols(),img.
				rows(), type);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().
				getDataBuffer()).getData();
		System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);
		return image;
	}

	static{ 
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}


}
