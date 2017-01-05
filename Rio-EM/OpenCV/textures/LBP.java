package textures;

import java.awt.image.BufferedImage;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import _openCV._OpenCV;
import metodos.MetodosRF;

public class LBP {

	protected int ksize;
	public int cont=0;

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public LBP(int ksize) {
		this.ksize = ksize;
	}
	public Mat calcLBP(Mat src){
		try{
			BufferedImage buff = _OpenCV.matToBufferImg(src);
			MetodosRF rf = new MetodosRF();
			int[][] red= rf.buffToMatriz(buff, "Red");
			int[][] green= rf.buffToMatriz(buff, "Green");
			int[][] blue= rf.buffToMatriz(buff, "Blue");


			int[][] janela= new int[ksize][ksize];

			//Pixels da janela de convolução
			int[][] pixelR =  new int[janela.length][janela.length];
			int[][] pixelG =  new int[janela.length][janela.length];
			int[][] pixelB =  new int[janela.length][janela.length];

			//Imagem textura
			int[][] textR = new int[red.length][red[0].length];
			int[][] textG = new int[red.length][red[0].length];
			int[][] textB = new int[red.length][red[0].length];

			int maior = 0;
			//Loop para formar o LBPs
			for (int i = (int) Math.floor(janela.length/2); i < textR.length - (int) Math.floor(janela.length/2); i++) {
				for (int j = (int) Math.floor(janela.length/2); j < textR[i].length - (int) Math.floor(janela.length/2); j++) {
					pixelR =  new int[janela.length][janela.length];
					pixelG =  new int[janela.length][janela.length];
					pixelB =  new int[janela.length][janela.length];
					cont+=1;
					for (int k = -(int) Math.floor(janela.length/2); k <= (int) Math.floor(janela.length/2); k++) {
						for (int l = -(int) Math.floor(janela.length/2); l <= (int) Math.floor(janela.length/2); l++) {
							pixelR[k+(int) Math.floor(janela.length/2)][l+ (int) Math.floor(janela.length/2)]= red[k+i][l+j];
							pixelG[k+(int) Math.floor(janela.length/2)][l+ (int) Math.floor(janela.length/2)]= green[k+i][l+j];
							pixelB[k+(int) Math.floor(janela.length/2)][l+ (int) Math.floor(janela.length/2)]= blue[k+i][l+j];
						}
					}
					textR[i][j]= getLBPVizinhos(pixelR);
					textG[i][j]= getLBPVizinhos(pixelG);
					textB[i][j]= getLBPVizinhos(pixelB);
					if(textG[i][j]>maior){
						maior =textG[i][j];  
						System.out.println(maior);

					}
					
				}
			}
			return _OpenCV.bufferImgToMat_RGB((rf.matrizColorToBufferImage(textR, textG, textB)));
		}catch(Exception ex){
			System.err.println(ex.getMessage());
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo getLBPVizinhos</b>
	 * Metodo para pegar o valor do LBP(Linear Binary Pattern), isto e, padrao linear binario de uma matriz.
	 *
	 * @param img matriz que sera calculado o LBP.
	 * @return int valor inteiro correspondente ao binario calculado.
	 *
	 */
	public int getLBPVizinhos(int[][] img) {
		try{
			int[][] janela= new int[ksize][ksize];
			String binary ="";
			int floorJanela = (int) Math.floor(janela.length/2);
			int cent = floorJanela;
			int centro =  img[cent][cent];
			for (int k = -floorJanela; k <= floorJanela; k++) {
				for (int l = -floorJanela; l <= floorJanela; l++) {
					if(this.cont==1 && cont==1){
						System.out.println("k: "+k+"\t"+"l: "+l+"\t");
						System.out.println("k+floor: "+(k+floorJanela)+"\t"+"l+floor: "+(l+floorJanela));
					}
					if(centro>img[k+floorJanela][l+floorJanela])
						binary+=0;
					else
						binary+=1;	
					
					//System.out.println(binary);
				}
			}
			System.out.println(binary);
			int div =binary.length();
			int value =Integer.parseInt(binary, 2);
			int result = Math.floorDiv(value, (2^div));
			return result;
			
		}catch(Exception ex){
			System.err.println(ex.getLocalizedMessage());
			ex.printStackTrace();
		}
		return -1;
	}
}