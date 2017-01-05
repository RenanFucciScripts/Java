package teste2MLP;

import padrao.MetodosRNA;

public class DiferencaSolo {

	public static void main(String[] args) {
		MetodosRNA met =  new MetodosRNA();

		String dir="C:\\Users\\Renan Fucci\\Dropbox\\Projeto - Embrapa\\Imagens\\SaoLourenco Bandas Spectrais\\Classificacao\\Composicao\\";
		int[][] soloRed=  new int[2139][973];

		int[][] agua=  met.leImagem(dir+"agua (Sigmoid).bmp", "Blue");
		int[][] vegeta= met.leImagem(dir+"vegeta (Sigmoid).bmp", "Green");

		for (int i = 0; i < soloRed.length; i++) {
			for (int j = 0; j < soloRed[i].length; j++) {
				soloRed[i][j]=255;
			}

		}

		for (int i = 0; i < soloRed.length; i++) {
			for (int j = 0; j < soloRed[i].length; j++) {
				soloRed[i][j]=Math.abs(vegeta[i][j]-soloRed[i][j]);
			}
		}

		for (int i = 0; i < soloRed.length; i++) {
			for (int j = 0; j < soloRed[i].length; j++) {
				soloRed[i][j]=Math.abs(agua[i][j]-soloRed[i][j]);
			}
		}

		met.gravarImagemRed(soloRed, dir, "solo (sigmoid).bmp");
	}
}
