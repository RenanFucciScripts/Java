package teste2MLP;

import padrao.MetodosRNA;

public class ContarCorDiferencaMLP_LP {

	public static void main(String[] args) {
		MetodosRNA metods =  new MetodosRNA();
		String dir="C:\\Users\\Renan Fucci\\Dropbox\\Projeto - Embrapa\\Imagens\\SaoLourenco Bandas Spectrais\\Classificacao\\NDVI, EVI, NDWI, NIR\\Agua\\";
		String nm="agua (Ramp).bmp";
		int[][] redDIF = metods.leImagem(dir+nm, "Red");
		int[][] greenDIF = metods.leImagem(dir+nm, "Green");
		int[][] blueDIF = metods.leImagem(dir+nm, "Blue");

		/*System.out.println("------------ Red ------------");
		metods.contarCorCinzas(redDIF);
		
		System.out.println("\n\n------------ Green ------------");
		metods.contarCorCinzas(greenDIF);
		
		System.out.println("\n\n------------ Blue ------------");
		metods.contarCorCinzas(blueDIF);
*/

		for (int i = 0; i < blueDIF.length; i++) {
			for (int j = 0; j < blueDIF[i].length; j++) {
				if(blueDIF[i][j]==242)
					blueDIF[i][j]=255;
				else
					blueDIF[i][j]=0;
				greenDIF[i][j]=0;
				redDIF[i][j]=0;
			}
		}
		metods.gravarImagemColorida(redDIF, greenDIF, blueDIF, dir, "agua(Ramp) filtro.bmp");
	}
}
