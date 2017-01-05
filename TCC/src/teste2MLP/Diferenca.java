package teste2MLP;

import padrao.MetodosRNA;

public class Diferenca {

	public static void main(String[] args) {
		MetodosRNA metods =  new MetodosRNA();
		String dir="C:\\Users\\Renan Fucci\\Dropbox\\Projeto - Embrapa\\Imagens\\SaoLourenco Bandas Spectrais\\Classificacao\\Composicao\\";
		int[][] redPL = metods.leImagem(dir+"FinalLinear.jpg", "Red");
		int[][] greenPL = metods.leImagem(dir+"FinalLinear.jpg", "Green");
		int[][] bluePL = metods.leImagem(dir+"FinalLinear.jpg", "Blue");

		int[][] redMLP = metods.leImagem(dir+"FinalMLP.jpg", "Red");
		int[][] greenMLP = metods.leImagem(dir+"FinalMLP.jpg", "Green");
		int[][] blueMLP = metods.leImagem(dir+"FinalMLP.jpg", "Blue");

		int[][] resultR= new int[redPL.length][redPL[0].length];
		int[][] resultG=new int[redPL.length][redPL[0].length];;
		int[][] resultB=new int[redPL.length][redPL[0].length];;

		
		for (int i = 0; i < resultR.length; i++) {
			for (int j = 0; j < resultR[i].length; j++) {
				
				int x= Math.abs(redMLP[i][j]-redPL[i][j]);
				if(x<1)
					resultR[i][j]=255;
				else
					resultR[i][j]=x;
				
			}
		}

		for (int i = 0; i < resultG.length; i++) {
			for (int j = 0; j < resultG[i].length; j++) {
				
				int x= Math.abs(greenMLP[i][j]-greenPL[i][j]);
				if(x<1)
					resultG[i][j] =255;
				else
					resultG[i][j] = x;
				
			}
		}

		for (int i = 0; i < resultB.length; i++) {
			for (int j = 0; j < resultB[i].length; j++) {
				
				int x= Math.abs(blueMLP[i][j]-bluePL[i][j]);
				if(x<1)
					resultB[i][j] =255;
				else
					resultB[i][j] = x;
				
			}
		}
	metods.gravarImagemColorida(resultR, resultG, resultB, dir, "difMLP_PL.jpg");
	}
	
}
