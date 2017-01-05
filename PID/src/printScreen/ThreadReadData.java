package printScreen;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;

public class ThreadReadData extends Thread {

	public TransparentFrame windowReference;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			while(true){
				if(this.windowReference.isShowing()){
					Robot robot;
					try{
						robot =  new Robot();
						BufferedImage screenShot =  robot.createScreenCapture(new Rectangle(windowReference.getLocationOnScreen().x, windowReference.getLocationOnScreen().y, windowReference.getSize().width, windowReference.getSize().height));
						Graphics2D  graphics =  screenShot.createGraphics();
						ImageIO.write(screenShot, "PNG", new File("C:/Users/Renan Fucci/Desktop/screen1.png"));

						String dir =  "C:/Users/Renan Fucci/Desktop/Tesseract-OCR/";
						/* Chamada do txt
						 * Ordem: Dir do Exec, Dir do Processavel, Dir do arq Saída 
						 * */
						Process process = new ProcessBuilder(dir+"tesseract.exe", dir+"/cod.jpg",dir+"texto").start();

						String texto =  this.readFile(dir+"texto.txt");

						System.out.println("\tOCR\n"+texto);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				this.sleep(1000);
			}
		}catch(Exception ex){

			ex.printStackTrace();
		}
	}

	public String readFile(String dir){
		String texto= "";
		BufferedReader br;
		try{
			br= new BufferedReader(new FileReader(dir));
			StringBuilder sb =  new StringBuilder();
			String line = br.readLine();
			while(line !=null){
				sb.append(line);
				sb.append(System.lineSeparator());
				line= br.readLine();
			}
			texto= sb.toString();
			br.close();
		}catch(Exception exe){
			exe.printStackTrace();
		}
		return texto;
	}
}
