package piramide;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Stack;

import javax.imageio.ImageIO;

import com.google.zxing.common.DetectorResult;
import com.sun.corba.se.pept.transport.Connection;

import boofcv.struct.image.ImageFloat32;
import metodos.MetodosRF;

public class TestePiramide {
	public static void main(String[] args) {

		String dir = "C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2016-01-21\\";

		try{
			MetodosRF metodosRF =  new MetodosRF();
			Stack<File> pilhaArqs =  new Stack<File>();
			File diret =  new File(dir+"imgs\\");
			pilhaArqs = metodosRF.empilharArquivosDiretorio(diret, pilhaArqs);
			
			ExamplePyramidFloat<ImageFloat32> app = new ExamplePyramidFloat<ImageFloat32>(ImageFloat32.class);
			
			/* -----------   Pyramid Float ------------*/		
			QRCode qrCode;
			Connection conn = null;
			DetectorResult detectorResult;
			BufferedImage outImg;
			BufferedImage image;
			long start;
			FileWriter flWriter ;
			BufferedWriter bufferedWriter ;
			for (File fl : pilhaArqs) {
				image = ImageIO.read(fl);		
				flWriter =  new FileWriter(dir+"pyramidFloat\\"+fl.getName()+".txt");
				bufferedWriter = new BufferedWriter(flWriter);	
				bufferedWriter.write("\n Imagem: "+fl.getName()+"\n");
				bufferedWriter.write("\n Dimensão orig: "+image.getWidth()+" x "+image.getHeight()+"\n");	
				for (int i = 1; i <= 5; i++) {
					start =  System.currentTimeMillis();	
					app.standard();
					outImg = app.process(image, i);		
					String tempoPyramid =  ((System.currentTimeMillis() - start)/1000.0)+" segundos";
					qrCode = new QRCode();
					detectorResult = qrCode.leQRCode(conn , outImg);
					System.out.println(detectorResult);
					bufferedWriter.write("\n Pyramid Float Nivel: "+i+"\n");
					bufferedWriter.write("\n Tempo de processamento: "+tempoPyramid+"\n");
					bufferedWriter.write("\n Reconhecido: "+(detectorResult != null ? ""+true: ""+false)+"\n");
					bufferedWriter.write("\n Dimensão pyramid: "+outImg.getWidth()+" x "+outImg.getHeight()+"\n");				
					System.out.println(((System.currentTimeMillis() - start)/1000.0)+" segundos");
					ImageIO.write(outImg, "JPG", new File(dir+"pyramidFloat\\"+fl.getName()+i+".jpg"));
				}
				bufferedWriter.close();
				flWriter.close();
				
				ExamplePyramidDiscrete<ImageFloat32> app1 = new ExamplePyramidDiscrete<ImageFloat32>(ImageFloat32.class);
			
				flWriter =  new FileWriter(dir+"pyramidDiscrete\\"+fl.getName()+".txt");
				bufferedWriter = new BufferedWriter(flWriter);	
				bufferedWriter.write("\n Imagem: "+fl.getName()+"\n");
				bufferedWriter.write("\n Dimensão orig: "+image.getWidth()+" x "+image.getHeight()+"\n");	
				for (int i = 1; i <= 3; i++) {
					start =  System.currentTimeMillis();	
					
					app1.standard();
					outImg = app1.process(image, i);		
					String tempoDiscrete =  ((System.currentTimeMillis() - start)/1000.0)+" segundos";
					qrCode = new QRCode();
					detectorResult = qrCode.leQRCode(conn , outImg);
					System.out.println(detectorResult);
					bufferedWriter.write("\n Pyramid Discrete Nivel: "+i+"\n");
					bufferedWriter.write("\n Tempo de processamento: "+tempoDiscrete+"\n");
					bufferedWriter.write("\n Reconhecido: "+(detectorResult != null ? ""+true: ""+false)+"\n");
					bufferedWriter.write("\n Dimensão pyramid: "+outImg.getWidth()+" x "+outImg.getHeight()+"\n");				
					System.out.println(((System.currentTimeMillis() - start)/1000.0)+" segundos");
					ImageIO.write(outImg, "JPG", new File(dir+"pyramidDiscrete\\"+fl.getName()+i+".jpg"));
				}
				bufferedWriter.close();
				flWriter.close();
			}	
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
