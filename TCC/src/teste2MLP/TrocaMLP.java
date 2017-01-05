package teste2MLP;

import padrao.MetodosRNA;

public class TrocaMLP {

	public static void main(String[] args) {
		MetodosRNA methds = new MetodosRNA();
		String dir="C:\\Users\\Renan Fucci\\Dropbox\\Projeto - Embrapa\\Imagens\\SaoLourenco Bandas Spectrais\\Classificacao\\NDVI, EVI, NDWI, NIR\\TesteMLP2\\";
		
		int[][] redSIN=methds.leImagem(dir+"mlp (Sin).jpg", "Red");
		int[][] greenSIN=methds.leImagem(dir+"mlp (Sin).jpg", "Green");
		int[][] blueSIN=methds.leImagem(dir+"mlp (Sin).jpg", "Blue");
	
		int[][] red= new int[redSIN.length][redSIN[0].length];
		int[][] green=new int[redSIN.length][redSIN[0].length];
		int[][] blue=new int[redSIN.length][redSIN[0].length];
	
		
		for (int i = 0; i < blue.length; i++) {
			for (int j = 0; j < blue[i].length; j++) {
				if(redSIN[i][j]>8 && greenSIN[i][j]<50 && blueSIN[i][j]<50 )
					red[i][j]=255;
	
			}
		}
		methds.gravarImagemColorida(red, blue, green, dir, "mlp(sin)_2.jpg");
	}
}
