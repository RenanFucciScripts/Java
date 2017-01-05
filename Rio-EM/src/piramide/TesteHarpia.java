package piramide;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;

import com.google.zxing.common.DetectorResult;
import com.sun.corba.se.pept.transport.Connection;

import boofcv.alg.enhance.EnhanceImageOps;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.struct.image.ImageUInt8;
import metodos.MetodosEM;
import metodos.MetodosRF;

public class TesteHarpia {

	public static void main(String[] args) throws LineUnavailableException {
		String dirIn = "C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Alex_Araujo\\2016-01-21\\";
		String dirOut = "C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2016-01-22\\teste_final\\";
		try{
			MetodosRF metodosRF =  new MetodosRF();

			Stack<File> pilhaArqs =  new Stack<File>();
			File diret =  new File(dirIn);

			pilhaArqs = metodosRF.empilharArquivosDiretorio(diret, pilhaArqs);
			
			ExamplePyramidDiscrete<ImageUInt8> app = new ExamplePyramidDiscrete<ImageUInt8>(ImageUInt8.class);

			BufferedImage outImg;
			BufferedImage image;
			long start;
			long tempoFinal = 0;
			QRCode qrCode;
			Connection conn = null;
			DetectorResult detectorResult;
			int reconhecidos =0;
			for (File fl : pilhaArqs) {
				image = ImageIO.read(fl);		
				start =  System.currentTimeMillis();	
				app.standard();
				outImg = app.process(image, 2);	
				
				ImageUInt8 input = ConvertBufferedImage.convertFromSingle(outImg, null, ImageUInt8.class);;
				ImageUInt8 output = new ImageUInt8(input.width, input.height);
				EnhanceImageOps.sharpen8(input, output);
				outImg=ConvertBufferedImage.convertTo(output,null,true);
				long elapsedTime =  (System.currentTimeMillis() - start);					
				if(tempoFinal==0)
					tempoFinal = elapsedTime;
				else
					tempoFinal = (tempoFinal+elapsedTime)/2;
				qrCode = new QRCode();
				detectorResult = qrCode.leQRCode(conn , outImg);
				reconhecidos = (detectorResult!=null ? (reconhecidos+=1):(reconhecidos));
				
				System.out.println("QRResult: "+detectorResult);
				System.out.println("Tempo médio: "+tempoFinal/1000.0);
				System.out.println("Reconhecidos: "+reconhecidos);
				ImageIO.write(outImg, "JPG", new File(dirOut+fl.getName()+".jpg"));
			}	
			MetodosEM.fazerSom();
			
		}catch(Exception ex){
			System.err.println("erro");
			ex.printStackTrace();
			MetodosEM.fazerSom();
		}
	}
}


