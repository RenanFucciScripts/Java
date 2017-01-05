package piramide;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import boofcv.alg.enhance.EnhanceImageOps;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.factory.transform.pyramid.FactoryPyramid;
import boofcv.struct.image.ImageUInt8;
import boofcv.struct.pyramid.PyramidDiscrete;
import boofcv.struct.pyramid.PyramidFloat;

public class MetodosPyramid_BoofCV {


	public static void main(String[] args) {
		int[][] pontos = {{36,26},
				{37,110},
				{387,27},
				{387,111}};
		MetodosPyramid_BoofCV methdos =  new MetodosPyramid_BoofCV();
		int[][] final_pontos = methdos.getPontosCorrespPyramidDiscrete(pontos, 2);
		String dir = "C:/Users/Renan Fucci/Dropbox/Estante Magica_Renan_Fucci/Pasta_Renan_Fucci/2016-02-29/teste/";
		try {
			BufferedImage buff = ImageIO.read(new File(dir+"bg2.jpg"));
			long start = System.currentTimeMillis();
			BufferedImage buffresult = methdos.processFloatPyramidDinamic(buff);
			long elapsedtime =  System.currentTimeMillis() -start;
			ImageIO.write(buffresult, "JPG", new File(dir+"final.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo processFloatPyramid<br></b>
	 * Metodo para aplicar o metodo FloatPyramid da biblioteca BoofCV em uma imagem com o nivel dinamico.<br>
	 * Aplica o nivel da pyramidFloat ate que o menor dos lados (altura ou largura) seja maior que 400 pixels.
	 * @param image imagem a ser processada;
	 * @return saida imagem resultante do metodo piramide e seu respectivo nivel.
	 */
	public BufferedImage processFloatPyramidDinamic(BufferedImage image) {
		String ladoMenor; //var para saber lado menor da imagem
		int nivel; //nivel a ser calculado
		Class<ImageUInt8> imageType= (ImageUInt8.class); //Padrão da lib para parâmetro da imagem
		ImageUInt8 imageAtScale;//Padrão da lib para resultado da pyramid
		double scales[] = new double[]{1,1.5,2,2.5,3,5,8,15}; //Padrão da lib para escalas
		double sigmas[] = new double[]{1,1,1,1,1,1,1,1}; // Padrão da lib para sigmas
		PyramidFloat<ImageUInt8> pyramid= FactoryPyramid.floatGaussian(scales,sigmas,imageType); //Pyramid de ponto flutuante
		ImageUInt8 input = ConvertBufferedImage.convertFromSingle(image, null, imageType);//Imagem de entrada no padrão da Boofcv
		
		pyramid.process(input);
		ladoMenor = (image.getHeight() > image.getWidth()) ? "w" : "h";
		if(ladoMenor.contentEquals("w")){
			nivel  = (int)Math.abs( Math.floor(input.getWidth() /400.0));
			imageAtScale = pyramid.getLayer(nivel);
			return ConvertBufferedImage.convertTo(imageAtScale,null,true);
		}
		else if(ladoMenor.contentEquals("h")){
			nivel = (int)Math.abs(Math.floor(input.getHeight()/400.0));
			imageAtScale = pyramid.getLayer(nivel);
			return ConvertBufferedImage.convertTo(imageAtScale,null,true);
		}
		return null;
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo processDiscretePyramid<br></b>
	 * Metodo para aplicar o metodo DiscretePyramid da biblioteca BoofCV em uma imagem.
	 * @param image imagem a ser processada;
	 * @param nivel nivel da discrete a ser usado (Permitido somente 1 ou 2);  
	 * @return saida imagem resultante do metodo piramide e seu respectivo nivel.
	 */
	public BufferedImage processDiscretePyramid(BufferedImage image, int nivel) {
		int ladoMenor;
		if(nivel!=1 && nivel!=2){
			System.err.println("Nivel Indisponível\nSomente nivel 1 ou 2");	
		}
		else{

			Class<ImageUInt8> imageType= (ImageUInt8.class);
			PyramidDiscrete<ImageUInt8> pyramid= FactoryPyramid.discreteGaussian(new int[]{1,2,4,8},-1,2,true,imageType);
			ImageUInt8 input = ConvertBufferedImage.convertFromSingle(image, null, imageType);
			pyramid.process(input);
			ImageUInt8 imageAtScale = pyramid.getLayer(nivel);
			return ConvertBufferedImage.convertTo(imageAtScale,null,true);
		}
		return null; 
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo sharpenBoofCV<br></b>
	 * Metodo para aplicar o metodo sharpen da biblioteca BoofCV em uma imagem.
	 * @param image imagem a ser processada;
	 * @param nivel nivel do sharpen a ser usado (Permitido somente 4 ou 8);  
	 * @return saida imagem resultante do metodo sharpen e seu respectivo nivel.
	 */
	public BufferedImage sharpenBoofCV(BufferedImage image, int nivel){
		if(nivel!= 4 && nivel != 8){
			System.err.println("Nivel Indisponível\nSomente nivel 4 ou 8");	
		}else{
			ImageUInt8 input = ConvertBufferedImage.convertFromSingle(image, null, ImageUInt8.class);;
			ImageUInt8 output = new ImageUInt8(input.width, input.height);

			if(nivel ==4){
				EnhanceImageOps.sharpen4(input, output);
				return ConvertBufferedImage.convertTo(output,null,true);
			}
			else if(nivel ==8){
				EnhanceImageOps.sharpen8(input, output);
				return ConvertBufferedImage.convertTo(output,null,true);
			}
		}
		return null;
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo getPontosCorrespPyramidDiscrete<br></b>
	 * Metodo para pegar o ponto correspondente da imagem menor na imagem maior do metodo {@link #processDiscretePyramid(BufferedImage, int)}. <br>
	 * OBS.: O parametro nivel deste metodo, deve ser igual ao {@link #processDiscretePyramid(BufferedImage, int)}.
	 * @param pontos matriz com os pontos da imagem pequena;
	 * @param nivel nivel do usado no {@link #processDiscretePyramid}. 
	 * @return matriz com os resultante dos pontos da imagem maior.
	 */
	public int[][] getPontosCorrespPyramidDiscrete(int[][] pontos, int nivel){
		if(nivel!=1 && nivel!=2){
			System.err.println("Nivel Indisponível\nSomente nivel 1 ou 2");	
		}
		else{
			int[][] final_pontos = new int[pontos.length][pontos[0].length];
			if(nivel==1){
				for (int i = 0; i < final_pontos.length; i++) {
					for (int j = 0; j < final_pontos[i].length; j++) {
						final_pontos[i][j] = pontos[i][j] * 2;
					}
				}
				return final_pontos;
			}
			else if(nivel==2){
				for (int i = 0; i < final_pontos.length; i++) {
					for (int j = 0; j < final_pontos[i].length; j++) {
						final_pontos[i][j] = pontos[i][j] * 4;
					}
				}
				return final_pontos;
			}
		}
		return null;
	}
}