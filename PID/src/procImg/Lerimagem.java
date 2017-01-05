package procImg;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Lerimagem {

	
	public static int[][] leImagem(String nome, String canalRgb){
		int array[][]=null;
		BufferedImage objeto;
	
		try {
			objeto = ImageIO.read(new File(nome));
			array= new int[objeto.getHeight()][objeto.getWidth()];
			for (int i=0; i<objeto.getHeight(); i++){
				for(int j=0; j<objeto.getWidth();j++){
					if(canalRgb=="Red")
						array[i][j]=new Color(objeto.getRGB(j, i)).getRed();
					else if(canalRgb=="Green")
						array[i][j]=new Color(objeto.getRGB(j, i)).getGreen();
					else if(canalRgb=="Blue")
						array[i][j]=new Color(objeto.getRGB(j, i)).getBlue();
				}
			}
			return array;


		} 
		catch (IOException e) {
			e.printStackTrace();
			return array;

		}

}
