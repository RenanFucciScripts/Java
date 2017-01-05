package faixaScanner;

import java.io.File;
import java.io.FileWriter;
import java.util.Locale;

import javax.imageio.ImageIO;

import metodos.MetodosEM;

public class DescritoresRuidoMargJanela3 {

	public static void main(String[] args) {
		try{
			String strDir="C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2015-08-17\\"
					+"imagensFaixaScanner\\";
			String strNaoRuido = "imgNaoRuido.jpg";
			String strAmosRuido = "imgRuidoMarginal.jpg";

			int[][] imgAmosRuido = MetodosEM.buffToMatriz(ImageIO.read(new File(strDir+strAmosRuido)));
			int[][] imgNaoRuido = MetodosEM.buffToMatriz(ImageIO.read(new File(strDir+strNaoRuido)));

			FileWriter arqAmosNaoRuidoMarg0 = new FileWriter(strDir+"arqAmosNaoRuidoMargJanela0.txt");
			FileWriter arqAmosRuidosMarg1 = new FileWriter(strDir+"arqAmosRuidosMargJanela1.txt");

			int[][] janela= new int[3][3];

			for (int i = (int) Math.floor(janela.length/2); i < imgNaoRuido.length-(int) Math.floor(janela.length/2); i++) {
				for (int j = (int) Math.floor(janela.length/2); j < imgNaoRuido[i].length-(int) Math.floor(janela.length/2); j++) {
					double somat=0;
					for (int k = -(int) Math.floor(janela.length/2); k <= (int)Math.floor(janela.length/2); k++) {
						for (int l = -(int) Math.floor(janela.length/2); l <= (int) Math.floor(janela.length/2); l++) {
							somat+=imgNaoRuido[i+k][j+l];
						}
					}
					double media=somat/9;
					double var=(((int)(somat-media))^2)/255.0;
					double dp= Math.sqrt(var);
					arqAmosNaoRuidoMarg0.write((i+","+j+","+String.format(Locale.US,"%.6f", media)+
							","+String.format(Locale.US,"%.6f",var)+","+String.format(Locale.US,"%.6f",dp)+",0\n"));
				}
			}

			for (int i = (int) Math.floor(janela.length/2); i < imgAmosRuido.length-(int) Math.floor(janela.length/2); i++) {
				for (int j = (int) Math.floor(janela.length/2); j < imgAmosRuido[i].length-(int) Math.floor(janela.length/2); j++) {
					double somat=0;
					for (int k = -(int) Math.floor(janela.length/2); k <= (int)Math.floor(janela.length/2); k++) {
						for (int l = -(int) Math.floor(janela.length/2); l <= (int) Math.floor(janela.length/2); l++) {
							somat+=imgAmosRuido[i+k][j+l];
						}
					}
					double media=somat/9;
					double var=(((int)(somat-media))^2)/255.0;
					double dp= Math.sqrt(var);
					arqAmosRuidosMarg1.write((i+","+j+","+String.format(Locale.US,"%.6f", media)+
							","+String.format(Locale.US,"%.6f",var)+","+String.format(Locale.US,"%.6f",dp)+",1\n"));
				}
			}
			arqAmosNaoRuidoMarg0.close();
			arqAmosRuidosMarg1.close();
		}catch(Exception e){
			System.err.println(e.getLocalizedMessage());
		}

	}
}
