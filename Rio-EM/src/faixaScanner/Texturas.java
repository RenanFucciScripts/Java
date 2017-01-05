package faixaScanner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import metodos.MetodosEM;
import metodos.MetodosRNAA;

public class Texturas {

	public static void main(String[] args) {
		try{
			String dir="C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2015-08-17\\"
					+"imagensFaixaScanner\\";

			FileReader fr =  new FileReader(new File(dir+"AmosRuidosMargFinal.txt"));
			BufferedReader br =  new BufferedReader(fr);

			String linha=br.readLine();
			String[] div = null;

			int contInterp =  0;
			int[][] textRRuido = new int[55][1346];
			int[][] textRNaoRuido= new int[textRRuido.length][textRRuido[0].length];
			
			int[][] textGRuido = new int[textRRuido.length][textRRuido[0].length];
			int[][] textGNaoRuido= new int[textRRuido.length][textRRuido[0].length];
			
			int[][] textBRuido = new int[textRRuido.length][textRRuido[0].length];
			int[][] textBNaoRuido= new int[textRRuido.length][textRRuido[0].length];
			
			
			while(linha!=null){
				if(contInterp%2==0){
					div=linha.split(",");
					int i= Integer.parseInt(div[0]);
					int j= Integer.parseInt(div[1]);
					textRNaoRuido[i][j]= Integer.parseInt(div[5]);
					textGNaoRuido[i][j]=Integer.parseInt(div[6]);
					textBNaoRuido[i][j]=Integer.parseInt(div[7]);
					//System.out.println(i+", "+j+", "+div[5]+", "+div[6]+", "+div[7]);
					linha=br.readLine();
					contInterp+=1;
				}
				else{
					div=linha.split(",");
					int i= Integer.parseInt(div[0]);
					int j= Integer.parseInt(div[1]);
					textRRuido[i][j]= Integer.parseInt(div[5]);
					textGRuido[i][j]=Integer.parseInt(div[6]);
					textBRuido[i][j]=Integer.parseInt(div[7]);
					//System.out.println(i+", "+j+", "+div[5]+", "+div[6]+", "+div[7]);
					linha=br.readLine();

					contInterp+=1;	
				}

			}
			
			br.close();
			fr.close();
			MetodosRNAA methods =  new MetodosRNAA();
			methods.gravarImagem(textRNaoRuido, dir+"2015-09-15\\", "T 1 (Nao Ruido).jpg");
			methods.gravarImagem(textGNaoRuido, dir+"2015-09-15\\", "T 2 (Nao Ruido).jpg");
			methods.gravarImagem(textBNaoRuido, dir+"2015-09-15\\", "T 3 (Nao Ruido).jpg");
			methods.gravarImagem(textRRuido, dir+"2015-09-15\\", "T 1 (Ruido).jpg");
			methods.gravarImagem(textGRuido, dir+"2015-09-15\\", "T 2 (Ruido).jpg");
			methods.gravarImagem(textBRuido, dir+"2015-09-15\\", "T 3 (Ruido).jpg");
			
			int[][] imgAleatoriaUmR =  methods.leImagem(dir+"img20150814_20045845.jpg", "Red");
			int[][] imgAleatoriaUmG =  methods.leImagem(dir+"img20150814_20045845.jpg", "Green");
			int[][] imgAleatoriaUmB =  methods.leImagem(dir+"img20150814_20045845.jpg", "Blue");
		
			int[][] imgAleatoriaDoisR =  methods.leImagem(dir+"img20150814_20050753.jpg", "Red");
			int[][] imgAleatoriaDoisG =  methods.leImagem(dir+"img20150814_20050753.jpg", "Green");
			int[][] imgAleatoriaDoisB =  methods.leImagem(dir+"img20150814_20050753.jpg", "Blue");
			
			int[][] textAleatoriaUmR =  new int[imgAleatoriaUmR.length][imgAleatoriaUmR[0].length];
			int[][] textAleatoriaUmG =  new int[imgAleatoriaUmR.length][imgAleatoriaUmR[0].length];
			int[][] textAleatoriaUmB =  new int[imgAleatoriaUmR.length][imgAleatoriaUmR[0].length];

			int[][] textAleatoriaDoisR =  new int[imgAleatoriaDoisR.length][imgAleatoriaDoisR[0].length];
			int[][] textAleatoriaDoisG =  new int[imgAleatoriaDoisR.length][imgAleatoriaDoisR[0].length];
			int[][] textAleatoriaDoisB =  new int[imgAleatoriaDoisR.length][imgAleatoriaDoisR[0].length];
			

			int[][] varAleatoriaUmR =  new int[imgAleatoriaUmR.length][imgAleatoriaUmR[0].length];
			int[][] varAleatoriaUmG =  new int[imgAleatoriaUmR.length][imgAleatoriaUmR[0].length];
			int[][] varAleatoriaUmB =  new int[imgAleatoriaUmR.length][imgAleatoriaUmR[0].length];

			int[][] varAleatoriaDoisR =  new int[imgAleatoriaDoisR.length][imgAleatoriaDoisR[0].length];
			int[][] varAleatoriaDoisG =  new int[imgAleatoriaDoisR.length][imgAleatoriaDoisR[0].length];
			int[][] varAleatoriaDoisB =  new int[imgAleatoriaDoisR.length][imgAleatoriaDoisR[0].length];
			
			
			int[][] janela =  new int[3][3];
			
			int[][] pixelR =  new int[janela.length][janela.length];
			int[][] pixelG =  new int[janela.length][janela.length];
			int[][] pixelB =  new int[janela.length][janela.length];
			
			/* ------------ 	Aleatoria Um	----------- */
			for (int i = (int) Math.floor(janela.length/2); i < imgAleatoriaUmR.length - (int) Math.floor(janela.length/2); i++) {
				for (int j = (int) Math.floor(janela.length/2); j < imgAleatoriaUmR[i].length - (int) Math.floor(janela.length/2); j++) {
					pixelR =  new int[janela.length][janela.length];
					pixelG =  new int[janela.length][janela.length];
					pixelB =  new int[janela.length][janela.length];
					for (int k = -(int) Math.floor(janela.length/2); k <= (int) Math.floor(janela.length/2); k++) {
						for (int l = -(int) Math.floor(janela.length/2); l <= (int) Math.floor(janela.length/2); l++) {
							pixelR[k+(int) Math.floor(janela.length/2)][l+ (int) Math.floor(janela.length/2)]= imgAleatoriaUmR[k+i][l+j];
							pixelG[k+(int) Math.floor(janela.length/2)][l+ (int) Math.floor(janela.length/2)]= imgAleatoriaUmG[k+i][l+j];
							pixelB[k+(int) Math.floor(janela.length/2)][l+ (int) Math.floor(janela.length/2)]= imgAleatoriaUmB[k+i][l+j];
						}
					}
					textAleatoriaUmR[i][j]= methods.getLBPVizinhos(pixelR);
					textAleatoriaUmG[i][j]= methods.getLBPVizinhos(pixelG);
					textAleatoriaUmB[i][j]= methods.getLBPVizinhos(pixelB);
				}
			}
			methods.gravarImagem(textAleatoriaUmR, dir+"2015-09-15\\", "T 4 (aleatória um).jpg");
			methods.gravarImagem(textAleatoriaUmG, dir+"2015-09-15\\", "T 5 (aleatória um).jpg");
			methods.gravarImagem(textAleatoriaUmB, dir+"2015-09-15\\", "T 6 (aleatória um).jpg");
			
			/* ------------ 	Aleatoria Dois	----------- */
			for (int i = (int) Math.floor(janela.length/2); i < imgAleatoriaDoisR.length - (int) Math.floor(janela.length/2); i++) {
				for (int j = (int) Math.floor(janela.length/2); j < imgAleatoriaDoisR[i].length - (int) Math.floor(janela.length/2); j++) {
					pixelR =  new int[janela.length][janela.length];
					pixelG =  new int[janela.length][janela.length];
					pixelB =  new int[janela.length][janela.length];
					for (int k = -(int) Math.floor(janela.length/2); k <= (int) Math.floor(janela.length/2); k++) {
						for (int l = -(int) Math.floor(janela.length/2); l <= (int) Math.floor(janela.length/2); l++) {
							pixelR[k+(int) Math.floor(janela.length/2)][l+ (int) Math.floor(janela.length/2)]= imgAleatoriaDoisR[k+i][l+j];
							pixelG[k+(int) Math.floor(janela.length/2)][l+ (int) Math.floor(janela.length/2)]= imgAleatoriaDoisG[k+i][l+j];
							pixelB[k+(int) Math.floor(janela.length/2)][l+ (int) Math.floor(janela.length/2)]= imgAleatoriaDoisB[k+i][l+j];
						}
					}
					textAleatoriaDoisR[i][j]= methods.getLBPVizinhos(pixelR);
					textAleatoriaDoisG[i][j]= methods.getLBPVizinhos(pixelG);
					textAleatoriaDoisB[i][j]= methods.getLBPVizinhos(pixelB);
				}
			}
			methods.gravarImagem(textAleatoriaDoisR, dir+"2015-09-15\\", "T 4 (aleatória dois).jpg");
			methods.gravarImagem(textAleatoriaDoisG, dir+"2015-09-15\\", "T 5 (aleatória dois).jpg");
			methods.gravarImagem(textAleatoriaDoisB, dir+"2015-09-15\\", "T 6 (aleatória dois).jpg");
			
			/* ------------ 	Tarefa 7	----------- */
			methods.gravarImagem(imgAleatoriaUmR, dir+"2015-09-15\\", "T 7 (aleatória um [Red]).jpg");
			methods.gravarImagem(imgAleatoriaUmG, dir+"2015-09-15\\", "T 7 (aleatória um [Green]).jpg");
			methods.gravarImagem(imgAleatoriaUmB, dir+"2015-09-15\\",  "T 7 (aleatória um [Blue]).jpg");
			
			methods.gravarImagem(imgAleatoriaDoisR, dir+"2015-09-15\\", "T 7 (aleatória dois [Red]).jpg");
			methods.gravarImagem(imgAleatoriaDoisG, dir+"2015-09-15\\", "T 7 (aleatória dois [Green]).jpg");
			methods.gravarImagem(imgAleatoriaDoisB, dir+"2015-09-15\\",  "T 7 (aleatória dois [Blue]).jpg");
			
	
			/* ------------ 	Tarefa 8 - 10 (Aleatoria um)	-----------*/
			for (int i = (int) Math.floor(janela.length/2); i < imgAleatoriaUmR.length-(int) Math.floor(janela.length/2); i++) {
				for (int j = (int) Math.floor(janela.length/2); j < imgAleatoriaUmR[i].length-(int) Math.floor(janela.length/2); j++) {
					double somatR=0;
					double somatG=0;
					double somatB=0;
					for (int k = -(int) Math.floor(janela.length/2); k <= (int)Math.floor(janela.length/2); k++) {
						for (int l = -(int) Math.floor(janela.length/2); l <= (int) Math.floor(janela.length/2); l++) {
							somatR+=imgAleatoriaUmR[i+k][j+l];
							somatG+=imgAleatoriaUmG[i+k][j+l];
							somatB+=imgAleatoriaUmB[i+k][j+l];
						}
					}
					double mediaR=somatR/9;
					double mediaG=somatG/9;
					double mediaB=somatB/9;
					
					double varR=(((int)(somatR-mediaR))^2)/255.0;
					double varG=(((int)(somatG-mediaG))^2)/255.0;
					double varB=(((int)(somatB-mediaB))^2)/255.0;
				//	System.out.println(((int)varR)+", "+((int)varG)+", "+((int)+varB));
					
					varAleatoriaUmR[i][j] = (int) varR;
					varAleatoriaUmG[i][j] = (int) varG;
					varAleatoriaUmB[i][j] = (int) varB;
				}
			}
			methods.gravarImagem(varAleatoriaUmR, dir+"2015-09-15\\", "T 8 (aleatória um [Red]).jpg");
			methods.gravarImagem(varAleatoriaUmG, dir+"2015-09-15\\", "T 9 (aleatória um [Green]).jpg");
			methods.gravarImagem(varAleatoriaUmB, dir+"2015-09-15\\", "T 10 (aleatória um [Blue]).jpg");
			
			/* ------------ 	Tarefa 8 - 10 (Aleatoria Dois)	-----------*/
			for (int i = (int) Math.floor(janela.length/2); i < imgAleatoriaDoisR.length-(int) Math.floor(janela.length/2); i++) {
				for (int j = (int) Math.floor(janela.length/2); j < imgAleatoriaDoisR[i].length-(int) Math.floor(janela.length/2); j++) {
					double somatR=0;
					double somatG=0;
					double somatB=0;
					for (int k = -(int) Math.floor(janela.length/2); k <= (int)Math.floor(janela.length/2); k++) {
						for (int l = -(int) Math.floor(janela.length/2); l <= (int) Math.floor(janela.length/2); l++) {
							somatR+=imgAleatoriaDoisR[i+k][j+l];
							somatG+=imgAleatoriaDoisG[i+k][j+l];
							somatB+=imgAleatoriaDoisB[i+k][j+l];
						}
					}
					double mediaR=somatR/9;
					double mediaG=somatG/9;
					double mediaB=somatB/9;
					
					double varR=(((int)(somatR-mediaR))^2)/255.0;
					double varG=(((int)(somatG-mediaG))^2)/255.0;
					double varB=(((int)(somatB-mediaB))^2)/255.0;
					//System.out.println(((int)varR)+", "+((int)varG)+", "+((int)+varB));
					
					
					varAleatoriaDoisR[i][j] = (int) varR;
					varAleatoriaDoisG[i][j] = (int) varG;
					varAleatoriaDoisB[i][j] = (int) varB;
				}
			}
			methods.gravarImagem(varAleatoriaDoisR, dir+"2015-09-15\\", "T 8 (aleatória dois [Red]).jpg");
			methods.gravarImagem(varAleatoriaDoisG, dir+"2015-09-15\\", "T 9 (aleatória dois [Green]).jpg");
			methods.gravarImagem(varAleatoriaDoisB, dir+"2015-09-15\\", "T 10 (aleatória dois [Blue]).jpg");

			MetodosEM.fazerSom();
		}catch (Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}
}
