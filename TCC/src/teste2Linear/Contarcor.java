package teste2Linear;

import padrao.MetodosRNA;

public class Contarcor {
	public static void main(String[] args) {
		MetodosRNA met =  new MetodosRNA();
		int[][] array = met.leImagem("C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/SaoLourenco Bandas Spectrais/Classificacao/NDVI, EVI e NDWI/TesteAgua/agua_Step.bmp", "Blue");
		met.contarCorCinzas(array);
	}
}
