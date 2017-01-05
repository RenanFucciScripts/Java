package teste2MLP;

import padrao.MetodosRNA;

public class PorcentAcerto {

	public static void main(String[] args) {
		MetodosRNA metods =  new MetodosRNA();

		/*Imagens Classificadas*/

		String dir="C:\\Users\\Renan Fucci\\Dropbox\\Projeto - Embrapa\\Imagens\\SaoLourenco Bandas Spectrais\\Classificacao\\";

		int[][] SIG_BLUE= metods.leImagem(dir+"\\NDVI, EVI, NDWI, NIR\\Agua\\agua (Sigmoid).bmp", "Blue");
		int[][] SIG_GREEN = metods.leImagem(dir+"Vegeta\\vegetaSIG.bmp", "Green");
		int[][] SIG_RED = metods.leImagem(dir+"Solo\\soloSIG.bmp", "Red");

		int[][] GAU_BLUE= metods.leImagem(dir+"\\NDVI, EVI, NDWI, NIR\\Agua\\agua (Gaussian).bmp", "Blue");
		int[][] GAU_GREEN = metods.leImagem(dir+"Vegeta\\vegetaGAU.bmp", "Green");
		int[][] GAU_RED = metods.leImagem(dir+"Solo\\soloGAU.bmp", "Red");

		int[][] RAMP_BLUE= metods.leImagem(dir+"\\NDVI, EVI, NDWI, NIR\\Agua\\agua (Ramp).bmp", "Blue");
		int[][] RAMP_GREEN = metods.leImagem(dir+"Vegeta\\vegetaRAMP.bmp", "Green");
		int[][] RAMP_RED = metods.leImagem(dir+"Solo\\soloRAMP.bmp", "Red");


		/*Imagens Parametro*/

		String dir1= "C:\\Users\\Renan Fucci\\Dropbox\\Projeto - Embrapa\\Imagens\\Amostra Final\\";

		int[][] PARAM_AGUA = metods.leImagem(dir1+"BARRA_AGUA2_PNG.png", "Red");
		int[][] PARAM_SOLO = metods.leImagem(dir1+"BARRA_SOLO2_PNG.png", "Red");
		int[][] PARAM_VEGETA = metods.leImagem(dir1+"BARRA_VEG2_PNG.png", "Red");

		double porcentSIG=0;
		double totalGREEN=0;

		double porcentGAU=0;
		double totalBLUE=0;

		double porcentRAMP=0;
		double totalRED=0;

		/*Fazendo o solo*/
		/*
		for (int i = 0; i < GAU_BLUE.length; i++) {
			for (int j = 0; j < GAU_BLUE[i].length; j++) {
				if(SIG_BLUE[i][j]<10 && SIG_GREEN[i][j]<40){
					SIG_RED[i][j]=255;
				}
				if(GAU_BLUE[i][j]<40 && GAU_GREEN[i][j]<40){
					GAU_RED[i][j]=255;
				}
				if(RAMP_BLUE[i][j]<40 && RAMP_GREEN[i][j]<40){
					RAMP_RED[i][j]=255;
				}
			}
		}
		String dir2= "C:\\Users\\Renan Fucci\\Dropbox\\Projeto - Embrapa\\Imagens\\SaoLourenco Bandas Spectrais\\Classificacao\\";

		metods.gravarImagemRed(RAMP_RED, dir2, "soloRAMP");
		metods.gravarImagemRed(GAU_RED, dir2, "soloGAU");
		metods.gravarImagemRed(SIG_RED, dir2, "soloSIG");
		*/
		for (int i = 0; i < PARAM_AGUA.length; i++) {
			for (int j = 0; j < PARAM_AGUA[i].length; j++) {
				if(PARAM_AGUA[i][j]<40)
					totalBLUE+=1;
				if(PARAM_VEGETA[i][j]<40)
					totalGREEN+=1;
				if(PARAM_SOLO[i][j]<40)
					totalRED+=1;
			}
		}
		
		for (int i = 0; i < PARAM_VEGETA.length; i++) {
			for (int j = 0; j < PARAM_VEGETA[i].length; j++) {
				if(GAU_BLUE[i][j]>250 && PARAM_AGUA[i][j]<40){
					porcentGAU+=1;
				}
				if(SIG_BLUE[i][j]>250 && PARAM_AGUA[i][j]<40){
					porcentSIG+=1;
				}
				if(RAMP_BLUE[i][j]>250 && PARAM_AGUA[i][j]<40){
					porcentRAMP+=1;
				}
			}
		}
		System.out.println(porcentGAU+"; "+porcentRAMP+"; "+porcentSIG);
		System.out.println("\n\n\nAzul");
		System.out.println("Param Total Blue: "+totalBLUE);
		System.out.println("Gaussiano: "+((porcentGAU/totalBLUE)*100.0));
		System.out.println("Sigmoig: "+((porcentSIG/totalBLUE)*100.0));
		System.out.println("Ramp: "+((porcentRAMP/totalBLUE)*100.0));
		
		porcentGAU=0.0;
		porcentSIG=0.0;
		porcentRAMP=0.0;
		
		for (int i = 0; i < PARAM_VEGETA.length; i++) {
			for (int j = 0; j < PARAM_VEGETA[i].length; j++) {
				if(GAU_GREEN[i][j]>250 && PARAM_VEGETA[i][j]<40){
					porcentGAU+=1;
				}
				if(SIG_GREEN[i][j]>250 && PARAM_VEGETA[i][j]<40){
					porcentSIG+=1;
				}
				if(RAMP_GREEN[i][j]>250 && PARAM_VEGETA[i][j]<40){
					porcentRAMP+=1;
				}
			}
		}
		System.out.println("\n\n\nVerde");
		System.out.println("Param Total Green: "+totalGREEN);
		System.out.println("Gaussiano: "+((porcentGAU/totalGREEN)*100.0));
		System.out.println("Sigmoig: "+((porcentSIG/totalGREEN)*100.0));
		System.out.println("Ramp Blue: "+((porcentRAMP/totalGREEN)*100.0));
	
		porcentGAU=0;
		porcentSIG=0;
		porcentRAMP=0;
		
		for (int i = 0; i < PARAM_VEGETA.length; i++) {
			for (int j = 0; j < PARAM_VEGETA[i].length; j++) {
				if(GAU_RED[i][j]>250 && PARAM_SOLO[i][j]<40){
					porcentGAU+=1;
				}
				if(SIG_RED[i][j]>250 && PARAM_SOLO[i][j]<40){
					porcentSIG+=1;
				}
				if(RAMP_RED[i][j]>250 && PARAM_SOLO[i][j]<40){
					porcentRAMP+=1;
				}
			}
		}

		
		System.out.println("\n\n\nParam Total Red: "+totalRED);
		System.out.println("Gaussiano: "+((porcentGAU/totalRED)*100.0));
		System.out.println("Sigmoig: "+((porcentSIG/totalRED)*100.0));
		System.out.println("Ramp: "+((porcentRAMP/totalRED)*100.0));
		
		//metods.gravarImagemColorida(matrizRed, matrizGreen, matrizBlue, dir, nome);
	
		
	/*	System.out.println("Total: "+cont);
		System.out.println("Qntd PL: "+porcentLP);
		System.out.println("Qntd MLP: "+porcentMLP);
	*/}
}
