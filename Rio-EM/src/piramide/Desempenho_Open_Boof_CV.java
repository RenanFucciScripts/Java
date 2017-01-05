package piramide;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Stack;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Desempenho_Open_Boof_CV {

	static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}

	public static void main(String[] args) {
		try{
			long elapsedtime_boofCV=0;
			long elapsedtime_openCV=0;
			Stack<File> pilhaArqs =  new Stack<File>();

			String dir = "C:/Users/Renan Fucci/Dropbox/Estante Magica_Renan_Fucci/Pasta_Renan_Fucci/2016-04-02/imgs/";
			File folder = new File(dir);
			empilharArquivosDiretorio(folder, pilhaArqs);

			MetodosPyramid_BoofCV metodosPyramid =  new MetodosPyramid_BoofCV();

			for (File fileEntry : pilhaArqs) {
				long start_boofCV = System.currentTimeMillis();
				BufferedImage buffresult = metodosPyramid.processDiscretePyramid(ImageIO.read(fileEntry), 1);
				elapsedtime_boofCV =  (elapsedtime_boofCV==0) ? (System.currentTimeMillis() - start_boofCV) : (((System.currentTimeMillis() - start_boofCV)+elapsedtime_boofCV)/2) ;
				ImageIO.write(buffresult, "png", new File(dir+"/boofCV/"+fileEntry.getName()));

				long start_openCV = System.currentTimeMillis();
				Mat source = Imgcodecs.imread(fileEntry.getAbsolutePath());
				Mat destination = new Mat(source.rows()/2,source.cols()/2, source.type());
				destination = source;
				Imgproc.pyrDown(source, destination, new Size(source.cols()/2,  source.rows()/2));
				elapsedtime_openCV =  (elapsedtime_openCV==0) ? (System.currentTimeMillis() - start_openCV) : (((System.currentTimeMillis() - start_openCV)+elapsedtime_openCV)/2) ;	
				Imgcodecs.imwrite(dir+"/openCV/"+fileEntry.getName(), destination);

			}
			System.out.println("Quantidade de Imagens: "+pilhaArqs.size());
			System.out.println("Tempo Médio BoofCV: "+(elapsedtime_boofCV/1000.0)+" segundos.");
			System.out.println("Tempo Médio OpenCV: "+(elapsedtime_openCV/1000.0)+" segundos.");
			
		}catch(Exception ex){
			ex.printStackTrace();
			System.err.println(ex.getLocalizedMessage());
		}
	}

	public Mat openFile(String fileName) throws Exception{
		Mat newImage = Imgcodecs.imread(fileName);
		if(newImage.dataAddr()==0){
			throw new Exception ("Couldn't open file "+fileName);
		}
		return newImage;
	}


	public static Stack<File> empilharArquivosDiretorio(final File folder, Stack<File> pilhaArqs) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				empilharArquivosDiretorio(fileEntry, pilhaArqs);
			} else {
				pilhaArqs.push(fileEntry);
			}
		}

		return pilhaArqs;
	}
}
