package faixaScanner;

import java.io.FileWriter;
import java.util.Locale;

import metodos.MetodosRF;

public class DescritoresRuidoMargRGB {

	public static void main(String[] args) {
		try{
			MetodosRF methods =  new MetodosRF();
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
			

			for (int i = 0; i < imgNaoRuidoR.length; i++) {
				for (int j = 0; j < imgNaoRuidoR[i].length; j++) {
					double r=imgNaoRuidoR[i][j]/255.0;
					double g=imgNaoRuidoG[i][j]/255.0;
					double b= imgNaoRuidoB[i][j]/255.0;
					amosNaoRuidosMarg0.write((i+","+j+","+String.format(Locale.US,"%.6f", r)+
							","+String.format(Locale.US,"%.6f",g)+","+String.format(Locale.US,"%.6f",b)+",0\n"));
				}
			}

			for (int i = 0; i < imgAmosRuidoR.length; i++) {
				for (int j = 0; j < imgAmosRuidoR[i].length; j++) {
					double r=imgAmosRuidoR[i][j]/255.0;
					double g=imgAmosRuidoG[i][j]/255.0;
					double b= imgAmosRuidoB[i][j]/255.0;
					amosRuidosMarg1.write((i+","+j+","+String.format(Locale.US,"%.6f", r)+
							","+String.format(Locale.US,"%.6f",g)+","+String.format(Locale.US,"%.6f",b)+",1\n"));
				}
			}
			amosNaoRuidosMarg0.close();
			amosRuidosMarg1.close();
			
		}catch(Exception e){
			System.err.println(e.getLocalizedMessage());
		}

	}
}
