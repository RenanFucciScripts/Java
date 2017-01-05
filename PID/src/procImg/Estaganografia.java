package procImg;

import projFinal.MétodosPDI;

public class Estaganografia extends MétodosPDI{

	public static void main(String[] args) {
		
		/*
		 * ESTEGANOGRAFIA PARA IMG DENTRO DE OUTRA IMAGEM
		 */
		String dir="C:\\Users\\Renan Fucci\\Desktop\\ImgTeste\\";
		
		int[][] matrizR= leImagem(dir+"esconder.jpg", "Red");
		int[][] matrizG= leImagem(dir+"esconder.jpg", "Green");
		int[][] matrizB= leImagem(dir+"esconder.jpg", "Blue");
		
		int[][] matriz2R= leImagem(dir+"esconder.jpg", "Red");
		int[][] matriz2G= leImagem(dir+"esconder.jpg", "Green");
		int[][] matriz2B= leImagem(dir+"esconder.jpg", "Blue");
		
		/*int[] vet={200};
		matriz2=segmentaçãoPorLimiarização(matriz2, vet);
		*/
		
		int[][] matrizresult0;
		int[][] matrizresult1;
		int[][] matrizresult2;
		int[][] matrizresult3;
		int[][] matrizresult4;
		int[][] matrizresult5;
		int[][] matrizresult6;
		int[][] matrizresult7;
		int[][] matrizFinal;
		
		
		//menos signficativo 7 e o mais segnificativo 0
		matrizresult0=separaMatrizBinária(matrizR, 0);
		matrizresult1=separaMatrizBinária(matrizR, 1);
		matrizresult2=separaMatrizBinária(matrizR, 2);
		matrizresult3=separaMatrizBinária(matrizR, 3);
		matrizresult4=separaMatrizBinária(matrizR, 4);
		matrizresult5=separaMatrizBinária(matrizR, 5);
		matrizresult6=separaMatrizBinária(matrizR, 6);
		matrizresult7=separaMatrizBinária(matrizR, 7);
		
		matrizFinal=juntarMatrizesBináParaUmaMatrizInteira(matrizresult0, matrizresult1, matrizresult2, matrizresult3, matrizresult4, matrizresult5, matrizresult6, matrizresult7);
		
		
		gravarImagemColorida(matrizR, matrizG, matrizB, dir, "dirpantanalEsconderTexto.bmp");
		
		matriz=esconderMsg(matriz, matriz2);
		
		gravarImagem(matriz, "imgEscondida");
		*/
		
		// ESTEGANOGRAFIA PARA TEXTO DENTRO DE UMA IMAGEM
		String dir="C:\\Users\\Renan Fucci\\Desktop\\ImgTeste\\";
		int[][] matrizR= leImagem(dir+"teste.jpg", "Red");
		int[][] matrizG= leImagem(dir+"teste.jpg", "Green");
		int[][] matrizB= leImagem(dir+"teste.jpg", "Blue");
		
		matrizR=codificarMsgBinária(matrizR, "Enquanto a mudança da política econômica ainda está em fase de ensaio, o governo Dilma Rousseff ao menos já mostra uma guinada rumo a um realismo maior em suas previsões para o futuro"
				+ "Documento enviado nesta quinta-feira (4) ao Congresso reduziu a projeção oficial para o crescimento do PIB (Produto Interno Bruto, medida da renda nacional) no próximo ano, de 2% para 0,8%."
				+ "Pela primeira vez na gestão da presidente, trabalha-se com uma expectativa similar à dos analistas de mercado, hoje em torno de 0,77%, mas com tendência de queda.");
		//gravarImagemColorida(matrizR, matrizG, matrizB, dir, "dirpantanalEsconderTexto.bmp");
		
		decodificarMsgBin(matrizR);
/*		decodificarMsgBin(matrizG);
		decodificarMsgBin(matrizB);*/
		
	}
	
}
