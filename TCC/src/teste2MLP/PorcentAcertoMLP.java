package teste2MLP;

import padrao.MetodosRNA;

public class PorcentAcertoMLP {

	public static void main(String[] args) {
		MetodosRNA metods =  new MetodosRNA();

		/*Imagens Classificadas*/

		String dir="C:\\Users\\Renan Fucci\\Dropbox\\Projeto - Embrapa\\Imagens\\SaoLourenco Bandas Spectrais\\Classificacao\\NDVI, EVI, NDWI, NIR\\TesteMLP2\\";
		
		int[][] SIN_BLUE= metods.leImagem(dir+"mlp (Sin).jpg", "Blue");
		int[][] SIN_GREEN = metods.leImagem(dir+"mlp (Sin).jpg", "Green");
		int[][] SIN_RED = metods.leImagem(dir+"mlp (Sin).jpg", "Red");


		int[][] TANH_BLUE= metods.leImagem(	dir+"mlp (Tanh).jpg", "Blue");
		int[][] TANH_GREEN = metods.leImagem(dir+"mlp (Tanh).jpg", "Green");
		int[][] TANH_RED = metods.leImagem(dir+"mlp (Tanh).jpg", "Red");


		/*Imagens Parametro*/

		String dir1= "C:\\Users\\Renan Fucci\\Dropbox\\Projeto - Embrapa\\Imagens\\Amostra Final\\";

		int[][] PARAM_AGUA = metods.leImagem(dir1+"BARRA_AGUA2_PNG.png", "Red");
		int[][] PARAM_SOLO = metods.leImagem(dir1+"BARRA_SOLO2_PNG.png", "Red");
		int[][] PARAM_VEGETA = metods.leImagem(dir1+"BARRA_VEG2_PNG.png", "Red");

		double porcentSIN=0;
		double porcentTANH=0;

		double totalGREEN=0;
		double totalBLUE=0;
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
				if(SIN_BLUE[i][j]>250 && PARAM_AGUA[i][j]<40){
					porcentSIN+=1;
				}
				if(TANH_BLUE[i][j]>250 && PARAM_AGUA[i][j]<40){
					porcentTANH+=1;
				}
			}
		}
		System.out.println("\n\n\nAzul");
		System.out.println(porcentTANH+"; "+porcentSIN);
		System.out.println("Param Total Blue: "+totalBLUE);
		System.out.println("TANH: "+((porcentTANH/totalBLUE)*100.0));
		System.out.println("SIN: "+((porcentSIN/totalBLUE)*100.0));
		
		porcentTANH=0.0;
		porcentSIN=0.0;
		
		for (int i = 0; i < PARAM_VEGETA.length; i++) {
			for (int j = 0; j < PARAM_VEGETA[i].length; j++) {
				if(TANH_GREEN[i][j]>250 && PARAM_VEGETA[i][j]<40){
					porcentTANH+=1;
				}
				if(SIN_GREEN[i][j]>250 && PARAM_VEGETA[i][j]<40){
					porcentSIN+=1;
				}
			}
		}
	
		System.out.println("\n\n\nVerde");
		System.out.println(porcentTANH+"; "+porcentSIN);
		System.out.println("Param Total Green: "+totalGREEN);
		System.out.println("TANH: "+((porcentTANH/totalGREEN)*100.0));
		System.out.println("SIN: "+((porcentSIN/totalGREEN)*100.0));
	
		porcentTANH=0;
		porcentSIN=0;
		
		for (int i = 0; i < PARAM_VEGETA.length; i++) {
			for (int j = 0; j < PARAM_VEGETA[i].length; j++) {
				if(TANH_RED[i][j]>250 && PARAM_SOLO[i][j]<40){
					porcentTANH+=1;
				}
				if(SIN_RED[i][j]>250 && PARAM_SOLO[i][j]<40){
					porcentSIN+=1;
				}
			}
		}

		
		System.out.println("\n\n\nParam Total Red: "+totalRED);
		System.out.println(porcentTANH+"; "+porcentSIN);
		System.out.println("Gaussiano: "+((porcentTANH/totalRED)*100.0));
		System.out.println("Sigmoig: "+((porcentSIN/totalRED)*100.0));
		
		//metods.gravarImagemColorida(matrizRed, matrizGreen, matrizBlue, dir, nome);
	
		
	/*	System.out.println("Total: "+cont);
		System.out.println("Qntd PL: "+porcentLP);
		System.out.println("Qntd MLP: "+porcentMLP);
	*/}
}
