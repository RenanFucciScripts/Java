package faixaScanner;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import enhance.MetodosPDI;

public class TesteRF {

	public static void main(String[] args) {
		try{
		String dir="C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2015-08-17\\"
				+"imagensFaixaScanner\\";
		String arq="img20150814_20045845";
		
		BufferedImage img= ImageIO.read(new File(dir+arq+".jpg"));
		//int[][] imgMM = MetodosEM.buffToMatriz(img);
		
		BufferedImage imgRot = MetodosPDI.rotacionar_180(img);
		
		//MetodosPDI.contarCorCinzas(MetodosEM.buffToMatriz(img));
		
		File ouptut = new File(dir+arq+"(180).jpg");
		ImageIO.write(imgRot, "jpg", ouptut);
		}catch(Exception e){
			System.err.println(e.getLocalizedMessage());
		}
	}
}
