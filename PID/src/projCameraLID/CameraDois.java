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

public class CameraDois implements Runnable, WebcamMotionListener {

	// Variáveis globais para datetime
	static Calendar cal2 = Calendar.getInstance();
	static DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	static String folder2 =dateFormat2.format(cal2.getTime());
	static File pasta2 =  new File("C:/ImagensPDI/Imagens/camera2/"+folder2);


	static int[][] cabeçalho = MétodosPDI.leImagem("C:/ImagensPDI/Imagens/Cabecalho.bmp", "Red");
	static int[][] rodape = MétodosPDI.leImagem("C:/ImagensPDI/Imagens/rodapé.bmp", "Red");

	//Mudar pra pegar camera dois
	Webcam camera2 =Webcam.getDefault();
	MétodosPDI metodos2= new MétodosPDI();


	public CameraDois() {
		// TODO Auto-generated constructor stub
		camera2.close();
		camera2.setViewSize(WebcamResolution.VGA.getSize());
		camera2.open();
	//	System.out.println("MKDIR"+pasta2.getPath());
		pasta2.mkdir();

		WebcamMotionDetector detectorCam2 = new WebcamMotionDetector(camera2);
		detectorCam2.setInterval(500); //testa a cada 500 frames
		detectorCam2.addMotionListener(this);
		detectorCam2.start();


	}

	public void run() {
		System.out.println("CAMERA DOIS (02)");
		new CameraDois();
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void motionDetected(WebcamMotionEvent ex) {
		BufferedImage imagem = camera2.getImage();

		//Transforma o bufferImage para uma matriz para cada canal RGB
		try {
			int[][] imgInicialR  = metodos2.imagemParaMatriz(imagem, "Red");
			int[][] imgInicialG= metodos2.imagemParaMatriz(imagem, "Green");
			int[][] imgInicialB= metodos2.imagemParaMatriz(imagem, "Blue");
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
			String nomeArq=dateFormat2.format(cal1.getTime());
			String camera= "camera2";
			//grava a imagem final com rodapé
			metodos2.gravarImagemColorida(imgInicialR, imgInicialG, imgInicialB,camera, nomeArq, folder2);


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
