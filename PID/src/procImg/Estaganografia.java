package procImg;

import projFinal.M�todosPDI;

public class Estaganografia extends M�todosPDI{

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
		matriz2=segmenta��oPorLimiariza��o(matriz2, vet);
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
		matrizresult0=separaMatrizBin�ria(matrizR, 0);
		matrizresult1=separaMatrizBin�ria(matrizR, 1);
		matrizresult2=separaMatrizBin�ria(matrizR, 2);
		matrizresult3=separaMatrizBin�ria(matrizR, 3);
		matrizresult4=separaMatrizBin�ria(matrizR, 4);
		matrizresult5=separaMatrizBin�ria(matrizR, 5);
		matrizresult6=separaMatrizBin�ria(matrizR, 6);
		matrizresult7=separaMatrizBin�ria(matrizR, 7);
		
		matrizFinal=juntarMatrizesBin�ParaUmaMatrizInteira(matrizresult0, matrizresult1, matrizresult2, matrizresult3, matrizresult4, matrizresult5, matrizresult6, matrizresult7);
		
		
		gravarImagemColorida(matrizR, matrizG, matrizB, dir, "dirpantanalEsconderTexto.bmp");
		
		matriz=esconderMsg(matriz, matriz2);
		
		gravarImagem(matriz, "imgEscondida");
		*/
		
		// ESTEGANOGRAFIA PARA TEXTO DENTRO DE UMA IMAGEM
		String dir="C:\\Users\\Renan Fucci\\Desktop\\ImgTeste\\";
		int[][] matrizR= leImagem(dir+"teste.jpg", "Red");
		int[][] matrizG= leImagem(dir+"teste.jpg", "Green");
		int[][] matrizB= leImagem(dir+"teste.jpg", "Blue");
		
		matrizR=codificarMsgBin�ria(matrizR, "Enquanto a mudan�a da pol�tica econ�mica ainda est� em fase de ensaio, o governo Dilma Rousseff ao menos j� mostra uma guinada rumo a um realismo maior em suas previs�es para o futuro"
				+ "Documento enviado nesta quinta-feira (4) ao Congresso reduziu a proje��o oficial para o crescimento do PIB (Produto Interno Bruto, medida da renda nacional) no pr�ximo ano, de 2% para 0,8%."
				+ "Pela primeira vez na gest�o da presidente, trabalha-se com uma expectativa similar � dos analistas de mercado, hoje em torno de 0,77%, mas com tend�ncia de queda.");
		//gravarImagemColorida(matrizR, matrizG, matrizB, dir, "dirpantanalEsconderTexto.bmp");
		
		decodificarMsgBin(matrizR);
/*		decodificarMsgBin(matrizG);
		decodificarMsgBin(matrizB);*/
		
	}
	
}
