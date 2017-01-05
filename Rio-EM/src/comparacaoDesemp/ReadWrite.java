package comparacaoDesemp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import boofcv.io.image.UtilImageIO;

public class ReadWrite {

	public static void main(String[] args) throws IOException {
		BufferedImage buff ;
		long start;
		long elapsedTime;
		String dir = "C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Alex_Araujo\\2016-01-21\\35174\\";
		
		System.out.println("\nUtilimageIO.read");
		start= System.currentTimeMillis();		
		buff =UtilImageIO.loadImage(dir+"DC-260-D34E743_Page_04.jpg");
		elapsedTime = System.currentTimeMillis() - start;
		System.out.println((elapsedTime/1000.0) +" segundos");
		
		
		System.out.println("\nImageIO.read");
		start= System.currentTimeMillis();		
		buff =ImageIO.read(new File(dir+"DC-260-D34E743_Page_05.jpg"));
		elapsedTime = System.currentTimeMillis() - start;
		System.out.println((elapsedTime/1000.0) +" segundos");
		
		System.out.println("\nImageIO.read (inputstream)");
		start= System.currentTimeMillis();		
		buff =ImageIO.read(Files.newInputStream(Paths.get(dir+"DC-260-D34E743_Page_06.jpg")));
		elapsedTime = System.currentTimeMillis() - start;
		System.out.println((elapsedTime/1000.0) +" segundos");
	
		/*---------------- write ------------------*/

		System.out.println("\nUtilimageIO.write");
		start= System.currentTimeMillis();		
		UtilImageIO.saveImage(buff, dir+"1.jpg");
		elapsedTime = System.currentTimeMillis() - start;
		System.out.println((elapsedTime/1000.0) +" segundos");
		
		System.out.println("\nImageIO.write");
		start= System.currentTimeMillis();		
		ImageIO.write(buff,"JPG", new  File(dir+"2.jpg"));
		elapsedTime = System.currentTimeMillis() - start;
		System.out.println((elapsedTime/1000.0) +" segundos");
		
		System.out.println("\nImageIO.write (inputstream)");
		start= System.currentTimeMillis();		
		ImageIO.write(buff, "JPG", Files.newOutputStream(Paths.get(dir+"3.jpg")));
		elapsedTime = System.currentTimeMillis() - start;
		System.out.println((elapsedTime/1000.0) +" segundos");
		
	
	}
}
