package realceCabecalho;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

import metodos.MetodosEM.JanelasConv;
import metodos.MetodosRF;

public class RealceCabecalho {

	

	/**
	 * <b>@author Alex Araujo<br></b>
	 * <b> Metodo realceCabecalho</b>
	 * Metodo para fazer realce nos cabecalhos que estejam muito claros.
	 *
	 * @param buff arquivo imagem que sera feito o realce.
	 * @return buff bufferedImage da imagem realcada.
	 *
	 */
	public static BufferedImage realceCabecalho(BufferedImage buff) throws IOException,Exception {
		int[][] red =buffToMatriz(buff, "Red");
		int[][] green =buffToMatriz(buff, "Green");
		int[][] blue =buffToMatriz(buff,  "Blue");

		int[][] medR =filtroMedianaRF(red, JanelasConv.tres);
		int[][] medG =filtroMedianaRF(green, JanelasConv.tres);
		int[][] medB =filtroMedianaRF(blue, JanelasConv.tres);

		int[] janela= new int[JanelasConv.tres.getValue()];
		int limiarBranco=245;

		for (int i = 0; i < red.length; i++) {
			for (int j = 0; j < red[i].length; j++) {
				if(red[i][j]>limiarBranco && green[i][j]>limiarBranco && blue[i][j]>limiarBranco){
					red[i][j]=medR[i][j];
					green[i][j]=medG[i][j];
					blue[i][j]=medB[i][j];
				}
			}
		}
		int[][] entradaR = eliminarPontosOrfaos(red, janela.length);
		int[][] entradaG = eliminarPontosOrfaos(green, janela.length);
		int[][] entradaB = eliminarPontosOrfaos(blue, janela.length);

		int[][] saidaR=new int[entradaR.length][entradaR[0].length];
		int[][] saidaG=new int[entradaR.length][entradaR[0].length];
		int[][] saidaB=new int[entradaR.length][entradaR[0].length];

		for (int i = 0; i < saidaB.length; i++) {
			for (int j = 0; j < saidaB[i].length; j++) {
				saidaR[i][j]=entradaR[i][j];
				saidaG[i][j]=entradaG[i][j];
				saidaB[i][j]=entradaB[i][j];
			}
		}
		int limiarShadow=93;
		int limiarMiddleTone =245;

		for (int i = 0; i < saidaB.length; i++) {
			for (int j = 0; j < saidaB[i].length; j++) {
				if(entradaR[i][j]<limiarShadow) 
					saidaR[i][j]=0;
				else if(entradaR[i][j]<limiarMiddleTone)
					saidaR[i][j]=(int) (0.03*entradaR[i][j]);
				else
					saidaR[i][j]=entradaR[i][j];
				if(entradaG[i][j]<limiarShadow)
					saidaG[i][j]=0;
				else if(entradaG[i][j]<limiarMiddleTone)
					saidaG[i][j]=(int) (0.03*entradaG[i][j]);
				else
					saidaG[i][j]=entradaG[i][j];
				if(entradaB[i][j]<limiarShadow)
					saidaB[i][j]=0;
				else if(entradaB[i][j]<limiarMiddleTone)
					saidaB[i][j]=(int) (0.03*entradaB[i][j]);
				else
					saidaB[i][j]=entradaB[i][j];
			}

		}

		for (int i = 0; i < saidaB.length; i++) {
			for (int j = 0; j < saidaB[i].length; j++) {
				saidaR[i][j]=(saidaR[i][j]+entradaR[i][j])/2;
				saidaG[i][j]=(saidaG[i][j]+entradaG[i][j])/2;
				saidaB[i][j]=(saidaB[i][j]+entradaB[i][j])/2;

			}
		}

		BufferedImage buff1 = MetodosRF.matrizColorToBufferImage(saidaR, saidaG, saidaB);
		return buff1;
	}
	/**
	 * <b>@author Alex Araujo<br></b>
	 * <b> Metodo eliminarPontosOrfaos</b>
	 * Metodo para eliminar os pontos isolados na imagem.
	 *
	 * @param imagem uma das bandas RGB da imagem.
	 * @param tamJanelaConv tamanho da janela de convolucao.
	 * @return imagemSaida uma da bandas da imagem sem os pontos isolados.
	 *
	 */
	private static int[][] eliminarPontosOrfaos(int[][] imagem, int tamJanelaConv) throws Exception{
		int[][] imagemSaida = new int[imagem.length][imagem[0].length];
		int[] janela = new int[tamJanelaConv];

		int numeroVizinhosIguais=0;
		for (int i =(int) Math.floor(janela.length/2); i < imagem.length - (int) Math.floor(janela.length/2); i++) {
			for (int j = (int) Math.floor(janela.length/2); j < imagem[i].length - (int) Math.floor(janela.length/2); j++) {
				numeroVizinhosIguais=0;
				for (int k = -(int) Math.floor(janela.length/2); k <= (int)Math.floor(janela.length/2); k++) {
					for (int l = -(int) Math.floor(janela.length/2); l <=(int)Math.floor(janela.length/2); l++) {
						if(imagem[i+k][j+l]==imagem[i][j])
							numeroVizinhosIguais+=1;
					}
				}
				if (numeroVizinhosIguais>=janela.length) {
					imagemSaida[i][j]=imagem[i][j];
				}
				else{
					int media=0;
					for (int k = -(int) Math.floor(janela.length/2); k <= (int)Math.floor(janela.length/2); k++) {
						for (int l = -(int) Math.floor(janela.length/2); l <=(int)Math.floor(janela.length/2); l++) {
							media+=imagem[i+k][j+l];
						}
					}
					media= (int) Math.abs(media/((janela.length*janela.length)));
					imagemSaida[i][j]=media;
				}
			}
		}
		return imagemSaida;
	}
	
	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo buffToMatriz<br></b>
	 * Metodo para converter um BufferedImage para uma matriz;
	 * @param buff imagem a ser convertida.
	 * @return imgSaida imagem no formado de matriz.
	 *
	 */
	public static int[][] buffToMatriz(BufferedImage buff, String bandaRGB) throws Exception {
		int[][] imgOut = new int[buff.getHeight()][buff.getWidth()];
		for (int i = 0; i < buff.getHeight(); i++) {
			for (int j = 0; j < buff.getWidth(); j++) {
				if(bandaRGB.contentEquals("Red")){
					imgOut[i][j] = new Color(buff.getRGB(j, i)).getRed();
				}
				else if(bandaRGB.contentEquals("Green")){
					imgOut[i][j] = new Color(buff.getRGB(j, i)).getGreen();
				}
				else if(bandaRGB.contentEquals("Blue")){
					imgOut[i][j] = new Color(buff.getRGB(j, i)).getGreen();
				}
			}
		}
		return imgOut;
	}
	
	/**
	 * <b>@author Renan Fucci<br></b>
	 * <b>Metodo filtroGaussianBoofCV<br></b>
	 * Metodo para aplicar o filtro da mediana com janela de convolucao em uma imagem.
	 * @param matriz imagem a ser processada;
	 * @param janelaConv tamanho da janela de convolucao.  
	 * @return matrizResultado imagem com o filtro.
	 */
	public static int[][] filtroMedianaRF(int matriz[][],JanelasConv janelaConv){
		int matrizAux[][] = new int[matriz.length][matriz[0].length];
		int matrizResultado[][]=new int[matriz.length][matriz[0].length];
		int janela[][]= new int[janelaConv.getValue()][janelaConv.getValue()];

		for (int g=0; g<janela.length;g++){
			for (int h=0; h<janela[g].length;h++){
				janela[g][h]=1;
			}
		}
		for (int i=0; i<matriz.length; i++){
			for(int j=0; j<matriz[i].length;j++){
				matrizAux[i][j]=matriz[i][j];
			}
		}
		int vetor[]=new int[janelaConv.getValue()*janelaConv.getValue()];
		int mediana = 0;
		int x;
		int cont;
		for (int k=(int) Math.floor((janela.length/2)); k<matrizAux.length-(int) Math.floor((janela.length/2));k++){
			for (int l=(int) Math.floor((janela.length/2));l<matrizAux[0].length-(int) Math.floor((janela.length/2));l++){
				cont=0;
				for (int m= -(int) Math.floor((janela.length/2));m<=(int) Math.floor((janela.length/2));m++){
					for (int n= -(int) Math.floor((janela.length/2));n<=(int) Math.floor((janela.length/2));n++){
						vetor[cont]=janela[m+(int) Math.floor((janela.length/2))][(int) Math.floor((janela.length/2))+n] * matrizAux[k+m][l+n];
						cont+=1;
						Arrays.sort(vetor);
						x= (int) Math.ceil(vetor.length/2);
						mediana=vetor[x];
					}
				}
				matrizResultado[k][l]=mediana;
			}
		}
		return matrizResultado;
	}

}
