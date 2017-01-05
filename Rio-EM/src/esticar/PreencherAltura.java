package esticar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PreencherAltura {

	public static void main(String[] args) throws IOException {
		String dir="C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2015-11-10\\descompactadas\\";
		String str="230915000031915.jpg";
		preencherAltura(ImageIO.read(new File(dir+str)));
	}
	public static BufferedImage preencherAltura(BufferedImage image){
		int w=550;
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight()+2*w, image.getType());

		Graphics g = newImage.getGraphics();

		g.setColor(Color.white);
		g.fillRect(0,0,image.getWidth(),image.getHeight()+2*w);
		g.drawImage(image, 0, w, null);
		g.dispose();
		return newImage;
	}
}
