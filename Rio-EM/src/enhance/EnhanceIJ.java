package enhance;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;

import javax.imageio.ImageIO;

import com.itextpdf.text.html.simpleparser.ImageProcessor;

public class EnhanceIJ {
	public static void main(String[] args) {
		try{
			Opener opener = new Opener();  
			
			BackgroundSubtracter bs = new BackgroundSubtracter();
			
			ImagePlus imp = opener.openImage("C:\\Users\\Renan Fucci\\Dropbox\\RIO 45º\\Enhance\\grayscale.jpg");  

			long start= System.nanoTime();
			ImageProcessor ip= imp.getProcessor();
			/*Acho que a sequencia do paramtros eh:
			 *	rollingBallBackground ( ip, radius, createBackground, lightBackground, useParaboloid, doPresmooth, correctCorners)
			 */
			bs.rollingBallBackground(ip, 50, false, true,true, false, true);
			bs.subtractBackround(ip, 50);
			
			long elapsedTime = System.nanoTime() - start;

			BufferedImage bf = ip.getBufferedImage();
			System.out.println(elapsedTime/ 1000000000.0); 

			String dir = "C:\\Users\\Renan Fucci\\Dropbox\\RIO 45º\\Enhance\\"+bs.getClass().getSimpleName();
			File ouptut = new File(dir+".jpg");
			ImageIO.write(bf, "jpg", ouptut);

			FileWriter fw =  new FileWriter(dir+".txt");
			fw.write(elapsedTime/ 1000000000.0+"\t segundos \n");
			fw.close();		

		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
