package projCameraLID;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamMotionDetector;
import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;
import com.github.sarxos.webcam.WebcamResolution;

public class CameraUm implements Runnable, WebcamMotionListener {

	// Variáveis globais para datetime
	static Calendar cal = Calendar.getInstance();
	static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	static String folder=dateFormat.format(cal.getTime());
	static File pasta =  new File("C:/ImagensPDI/Imagens/camera1/"+folder);


	static int[][] cabeçalho = MétodosPDI.leImagem("C:/ImagensPDI/Imagens/Cabecalho.bmp", "Red");
	static int[][] rodape = MétodosPDI.leImagem("C:/ImagensPDI/Imagens/rodapé.bmp", "Red");

	Webcam camera1 =Webcam.getDefault();
	MétodosPDI metodos1= new MétodosPDI();


	public CameraUm() {
		camera1.close();
		camera1.setViewSize(WebcamResolution.VGA.getSize());
		camera1.open();
		System.out.println("MKDIR"+pasta.getPath());
		pasta.mkdir();

		WebcamMotionDetector detectorCam1 = new WebcamMotionDetector(camera1);
		detectorCam1.setInterval(500); //testa a cada 500 frames
		detectorCam1.addMotionListener(this);
		detectorCam1.start();


	}

	public void run() {
		System.out.println("CAMERA UM (01)");
		new CameraUm();
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void motionDetected(WebcamMotionEvent ex) {
		BufferedImage imagem = camera1.getImage();

		//Transforma o bufferImage para uma matriz para cada canal RGB
		try {
			int[][] imgInicialR  = metodos1.imagemParaMatriz(imagem, "Red");
			int[][] imgInicialG= metodos1.imagemParaMatriz(imagem, "Green");
			int[][] imgInicialB= metodos1.imagemParaMatriz(imagem, "Blue");
			//adiciona o cabeçalho na imagem
			for (int i = 0; i < 80; i++) {
				for (int j = 0; j < imgInicialR[i].length; j++) {
					imgInicialR[i][j]=cabeçalho[i][j];
					imgInicialG[i][j]=cabeçalho[i][j];
					imgInicialB[i][j]=cabeçalho[i][j];
				}
			}
			//adiciona o rodapé à imagem
			for (int i = 80; i > 0 ; i--) {
				for (int j = 0; j < imgInicialR[i].length; j++) {
					imgInicialR[640 -i][j]=rodape[80-i][j];
					imgInicialG[640-i][j]=rodape[80-i][j];
					imgInicialB[640-i][j]=rodape[80-i][j];
				}
			}
			//grava a imagem final com rodapé
			Calendar cal1 = Calendar.getInstance();
			String nomeArq=dateFormat.format(cal1.getTime());
			String camera= "camera1";
			//grava a imagem final com rodapé
			metodos1.gravarImagemColorida(imgInicialR, imgInicialG, imgInicialB,camera, nomeArq, folder);


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//grava a Imagem de controle




	}
}
