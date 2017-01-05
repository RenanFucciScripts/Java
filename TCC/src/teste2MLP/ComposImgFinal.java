package teste2MLP;

import padrao.MetodosRNA;

public class ComposImgFinal {
	public static void main(String[] args) {
		try{
		MetodosRNA metods =  new MetodosRNA();
		String dir="C:\\Users\\Renan Fucci\\Dropbox\\Projeto - Embrapa\\Imagens\\SaoLourenco Bandas Spectrais\\Classificacao\\";
		
		int[][] redRAMP= metods.leImagem(dir+"Solo\\soloRAMP.bmp", "Red");
		int[][] greenRAMP= metods.leImagem(dir+"Vegeta\\vegetaRAMP.bmp", "Green");
		int[][] blueRAMP= metods.leImagem(dir+"Agua\\agua (Ramp).bmp", "Blue");
		
		int[][] redSIG= metods.leImagem(dir+"Solo\\soloSIG.bmp", "Red");
		int[][] greenSIG= metods.leImagem(dir+"Vegeta\\vegetaSIG.bmp", "Green");
		int[][] blueSIG= metods.leImagem(dir+"Agua\\agua (Sigmoid).bmp", "Blue");
		
		int[][] redGAU= metods.leImagem(dir+"Solo\\soloGAU.bmp", "Red");
		int[][] greenGAU= metods.leImagem(dir+"Vegeta\\vegetaGAU.bmp", "Green");
		int[][] blueGAU= metods.leImagem(dir+"Agua\\agua (Gaussian).bmp", "Blue");
		
		/*
		metods.gravarImagemBlue(blue, dir, "agua filtro");
		metods.gravarImagemGreen(green, dir, "vegeta filtro");
		metods.gravarImagemRed(red, dir, "solo filtro");
		metods.gravarImagemColorida(red, green, blue, dir, "imgFinalComposi.bmp");
		*/

		metods.gravarImagemColorida(redGAU, greenGAU, blueGAU, dir, "Final Linear\\finalGAU.jpg");
		metods.gravarImagemColorida(redSIG, greenSIG, blueSIG, dir, "Final Linear\\finalSIG.jpg");
		metods.gravarImagemColorida(redRAMP, greenRAMP, blueRAMP, dir, "Final Linear\\finalRAMP.jpg");
		
		}catch(Exception e){
			System.err.println(e.getLocalizedMessage());
		}
		
	}
	

}
