package faixaScanner;

import java.io.FileWriter;
import java.util.Locale;

import metodos.MetodosRF;

public class DescritoresRuidoMargText {

	public static void main(String[] args) {
		try{
			MetodosRF  methods =  new MetodosRF();
			String strDir="C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2015-08-17\\"
					+"imagensFaixaScanner\\";
			String strNaoRuido = "imgNaoRuido.jpg";
			String strAmosRuido = "imgRuidoMarginal.jpg";
			
			int[][] imgNaoRuidoR = methods.leImagem(strDir+strNaoRuido, "Red");
			int[][] imgNaoRuidoG = methods.leImagem(strDir+strNaoRuido, "Green");
			int[][] imgNaoRuidoB = methods.leImagem(strDir+strNaoRuido, "Blue");

			int[][] imgAmosRuidoR = methods.leImagem(strDir+strAmosRuido, "Red");
			int[][] imgAmosRuidoG = methods.leImagem(strDir+strAmosRuido, "Green");
			int[][] imgAmosRuidoB = methods.leImagem(strDir+strAmosRuido, "Blue");

			FileWriter amosNaoRuidosMarg0 = new FileWriter(strDir+"AmosNaoRuidosMarg0.txt");
			FileWriter amosRuidosMarg1 = new FileWriter(strDir+"AmosRuidosMarg1.txt");

			int[][] janela= new int[3][3];

			int[][] imgPixelR =  new int[janela.length][janela.length];
			int[][] imgPixelG =  new int[janela.length][janela.length];
			int[][] imgPixelB =  new int[janela.length][janela.length];
			
			for (int i = (int) Math.floor(janela.length/2); i < imgAmosRuidoR.length - (int) Math.floor(janela.length/2); i++) {
				for (int j = (int) Math.floor(janela.length/2); j < imgAmosRuidoR[i].length -(int) Math.floor(janela.length/2); j++) {
					imgPixelR =  new int[janela.length][janela.length];
					imgPixelG =  new int[janela.length][janela.length];
					imgPixelB =  new int[janela.length][janela.length];
					for (int k = - (int) Math.floor(janela.length/2); k <= (int) Math.floor(janela.length/2) ; k++) {
						for (int l = - (int) Math.floor(janela.length/2); l <= (int) Math.floor(janela.length/2); l++) {
							imgPixelR[k+(int) Math.floor(janela.length/2)][l+(int) Math.floor(janela.length/2)]=imgNaoRuidoR[i+k][j+l];
							imgPixelG[k+(int) Math.floor(janela.length/2)][l+(int) Math.floor(janela.length/2)]=imgNaoRuidoG[i+k][j+l];
							imgPixelB[k+(int) Math.floor(janela.length/2)][l+(int) Math.floor(janela.length/2)]=imgNaoRuidoB[i+k][j+l];
						}
					}
					double r=imgNaoRuidoR[i][j]/255.0;
					double g=imgNaoRuidoG[i][j]/255.0;
					double b= imgNaoRuidoB[i][j]/255.0;
					int textR= methods.getLBPVizinhos(imgPixelR);
					int textG= methods.getLBPVizinhos(imgPixelG);
					int textB= methods.getLBPVizinhos(imgPixelB);
					amosNaoRuidosMarg0.write((i+","+j+","+String.format(Locale.US,"%.6f", r)+
							","+String.format(Locale.US,"%.6f",g)+","+String.format(Locale.US,"%.6f",b)
							+","+(textR)+","+(textG)
							+","+(textB)+",0\n"));

				}
			}

			for (int i = (int) Math.floor(janela.length/2); i < imgAmosRuidoR.length - (int) Math.floor(janela.length/2); i++) {
				for (int j = (int) Math.floor(janela.length/2); j < imgAmosRuidoR[i].length -(int) Math.floor(janela.length/2); j++) {
					imgPixelR =  new int[janela.length][janela.length];
					imgPixelG =  new int[janela.length][janela.length];
					imgPixelB =  new int[janela.length][janela.length];
					for (int k = - (int) Math.floor(janela.length/2); k <= (int) Math.floor(janela.length/2) ; k++) {
						for (int l = - (int) Math.floor(janela.length/2); l <= (int) Math.floor(janela.length/2); l++) {
							imgPixelR[k+(int) Math.floor(janela.length/2)][l+(int) Math.floor(janela.length/2)]=imgAmosRuidoR[i+k][j+l];
							imgPixelG[k+(int) Math.floor(janela.length/2)][l+(int) Math.floor(janela.length/2)]=imgAmosRuidoG[i+k][j+l];
							imgPixelB[k+(int) Math.floor(janela.length/2)][l+(int) Math.floor(janela.length/2)]=imgAmosRuidoB[i+k][j+l];
						}
					}
					double r=imgAmosRuidoR[i][j]/255.0;
					double g=imgAmosRuidoG[i][j]/255.0;
			 		double b= imgAmosRuidoB[i][j]/255.0;
					int textR= methods.getLBPVizinhos(imgPixelR);
					int textG= methods.getLBPVizinhos(imgPixelG);
					int textB= methods.getLBPVizinhos(imgPixelB);

					amosRuidosMarg1.write((i+","+j+","+String.format(Locale.US,"%.6f", r)+
							","+String.format(Locale.US,"%.6f",g)+","+String.format(Locale.US,"%.6f",b)
							+","+(textR)+","+(textG)
							+","+(textB)+",1\n"));

				}
			}
			amosNaoRuidosMarg0.close();
			amosRuidosMarg1.close();
		}catch(Exception e){
			System.err.println(e.getLocalizedMessage());
			e.printStackTrace();
		}

	}
}
