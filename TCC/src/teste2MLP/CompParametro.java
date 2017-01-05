package teste2MLP;

import padrao.MetodosRNA;

public class CompParametro {

	public static void main(String[] args) {
		MetodosRNA metods =  new MetodosRNA();

		String dir = "C:\\Users\\Renan Fucci\\Dropbox\\Projeto - Embrapa\\Imagens\\Amostra Final\\";
		
		int[][] red = metods.leImagem(dir+"BARRA_SOLO2_PNG.png", "Red");
		int[][] green = metods.leImagem(dir+"BARRA_VEG2_PNG.png", "Green");
		int[][] blue = metods.leImagem(dir+"BARRA_AGUA2_PNG.png", "Blue");
		
		
		for (int i = 0; i < blue.length; i++) {
			for (int j = 0; j < blue[i].length; j++) {
				if(red[i][j]<80)
					red[i][j]=255;
				else
					red[i][j]=0;
				
				if(blue[i][j]<80)
					blue[i][j]=255;
				else
					blue[i][j]=0;

				if(green[i][j]<80)
					green[i][j]=255;
				else
					green[i][j]=0;
				
			}
		}
		metods.gravarImagemColorida(red, green, blue, dir, "imgParametro.jpg");
	}
}
