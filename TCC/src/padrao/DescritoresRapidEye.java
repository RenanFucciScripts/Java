package padrao;

import java.io.FileWriter;
import java.util.Locale;

/**
 * <b> Classe DescritoresRapidEye <\b>
 * Esta classe monta os descritores NDVI, EVI, NDWI e NIR baseados nos calculos de composicao e nas bandas spectrais do Sensor Remoto RapidEye.
 * As bandas de 16bits do RapidEye sao: Red (Banda1), Green (Banda2), Blue (Banda3), Red_Edge (Banda4) e NIR (Banda5). 
 * Os calculos pre-definidos para o Sensor Remoto do RapidEye				 
 * NDVI = (nir-red) / (nir+red)
 * EVI = 2,5 * {(nir-vermelho) / (nir+6*vermelhor-7.5*azul+1 )}
 * NDWI = (nir - green) / (nir + green)
 * */
public class DescritoresRapidEye {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try{
			MetodosRNA methods = new MetodosRNA();
			String dirPadrao = "C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/SaoLourenco Bandas Spectrais/";
			int[][] blue=methods.leImagem(dirPadrao+"Banda3_BLUE_16Bits.png","Blue" );
			int[][] green=  methods.leImagem(dirPadrao+"Banda2_GREEN_16Bits.png","Green");
			int[][] red= methods.leImagem(dirPadrao+"Banda1_RED_16Bits.png","Red");
			int[][] nir =  methods.leImagem(dirPadrao+"Banda5_NIR_16Bits.png","Red");

			System.out.println("1");
			
			String dirAmostra = "C:\\Users\\Renan Fucci\\Dropbox\\Projeto - Embrapa\\Imagens\\Amostra Final\\";
			
			int[][] imgAmostrasRed =  methods.leImagem(dirAmostra+"BARRA_SOLO1_PNG.png", "Red");
			int[][] imgAmostrasGreen =  methods.leImagem(dirAmostra+"BARRA_VEG1_PNG.png", "Green");
			int[][] imgAmostrasBlue =  methods.leImagem(dirAmostra+"BARRA_AGUA1_PNG.png", "Blue");

			/* Variaveis para escrita de arquivos*/
			FileWriter amostasAgua = new FileWriter(dirPadrao+"amostraAgua.txt");
			FileWriter amostasSolo = new FileWriter(dirPadrao+"amostraSolo.txt");
			FileWriter amostasVeget =  new FileWriter(dirPadrao+"amostraVeget.txt");

			/*Variaveis para calculo dos indices*/
			double ndvi;
			double evi;
			double ndwi;
			double band5;

			//Variavel para calcular limite do indice NIR
			double maxBand5=0;

			//Variavel auxiliar para evitar divisão por zero (Exception)
			double aux;


			/* Matrizes temporarias para testar a posição das amostras*/
			int tmpagua [][] = new int[nir.length][nir[0].length];
			int tmpsolo [][] = new int[nir.length][nir[0].length];
			int tmpveget [][]= new int[nir.length][nir[0].length];


			/*
			 * Pega  o máximo da banda5, pra dividir depois pelo seu máximo. 
			 * Pra manter seu índice entre -1 e 1;
			 */
			for (int i = 0; i < nir.length; i++) {
				for (int j = 0; j < nir[i].length; j++) {
					if(i==0 && j==0){maxBand5=nir[i][j];}
					if(nir[i][j]>maxBand5){maxBand5=nir[i][j];}
				}
			}


			for (int h = 0; h <= 20 ;h++) { //Vai fazer 10 vezes pra igualar a qntd de amostras maior, da agua.
				/*
				 * Calculos para indices das amostras de Solo
				 * */
				for (int i = 0; i < imgAmostrasRed.length; i++) {
					for (int j = 0; j < imgAmostrasRed[i].length; j++) {
						if(imgAmostrasRed[i][j]<=55){

							//ndvi
							ndvi=(nir[i][j]-red[i][j]);
							aux=nir[i][j]+red[i][j];
							if(aux==0){ndvi= ndvi/ (-1) ;}
							else{ ndvi= ndvi / aux; aux=0;}

							//evi
							evi= (nir[i][j]- red[i][j]);
							aux=nir[i][j]+6*red[i][j]-7.5*blue[i][j]+1;
							if(aux==0){evi= 2.5*(evi/ (-1));	}
							else{evi = 2.5 * (evi/aux); aux=0;}		

							//NWDI
							ndwi=nir[i][j]-green[i][j];
							aux= nir[i][j]+green[i][j];
							if(aux==0){ndwi = ndwi / (-1);}
							else{ndwi = ndwi /aux;}

							//band5

							tmpsolo[i][j]= (int) (255* (band5=nir[i][j]/maxBand5));

							amostasSolo.write(i+","+j+","+String.format(Locale.US,"%.6f", ndvi)+","+String.format(Locale.US,"%.6f",evi)+","+String.format(Locale.US,"%.6f",ndwi)+","+String.format(Locale.US,"%.6f",band5)+"\n");
						}
					}
				}

			}

			for (int h = 0; h <= 5; h++) {

				/*
				 * Calculos para indices das amostras de Vegetacao
				 * */
				for (int i = 0; i < imgAmostrasGreen.length; i++) {
					for (int j = 0; j < imgAmostrasGreen[i].length; j++) {
						if(imgAmostrasGreen[i][j]<=55){

							//ndvi
							ndvi=(nir[i][j]-red[i][j]);
							aux=nir[i][j]+red[i][j];
							if(aux==0){ndvi= ndvi/ (-1) ;}
							else{ ndvi= ndvi / aux; aux=0;}

							//evi
							evi= (nir[i][j]- red[i][j]);
							aux=nir[i][j]+6*red[i][j]-7.5*blue[i][j]+1;
							if(aux==0){evi= 2.5*(evi/ (-1));	}
							else{evi = 2.5 * (evi/aux); aux=0;}		

							//NWDI
							ndwi=nir[i][j]-green[i][j];
							aux= nir[i][j]+green[i][j];
							if(aux==0){ndwi = ndwi / (-1);}
							else{ndwi = ndwi /aux;}

							//band5
							tmpveget[i][j]= (int) (255* (band5=nir[i][j]/maxBand5));

							amostasVeget.write(i+","+j+","+String.format(Locale.US,"%.6f", ndvi)+","+String.format(Locale.US,"%.6f",evi)+","+String.format(Locale.US,"%.6f",ndwi)+","+String.format(Locale.US,"%.6f",band5)+"\n");
						}
					}
				}

			}
			/*
			 * Calculos para indices das amostras de Agua
			 * */
			for (int i = 0; i < imgAmostrasBlue.length; i++) {
				for (int j = 0; j < imgAmostrasBlue[i].length; j++) {
					if(imgAmostrasBlue[i][j]<=55){

						//ndvi
						ndvi=(nir[i][j]-red[i][j]);
						aux=nir[i][j]+red[i][j];
						if(aux==0){ndvi= ndvi/ (-1) ;}
						else{ ndvi= ndvi / aux; aux=0;}

						//evi
						evi= (nir[i][j]- red[i][j]);
						aux=nir[i][j]+6*red[i][j]-7.5*blue[i][j]+1;
						if(aux==0){evi= 2.5*(evi/ (-1));	}
						else{evi = 2.5 * (evi/aux); aux=0;}		

						//NWDI
						ndwi=nir[i][j]-green[i][j];
						aux= nir[i][j]+green[i][j];
						if(aux==0){ndwi = ndwi / (-1);}
						else{ndwi = ndwi /aux;}

						//band5
						tmpagua[i][j]= (int) (255* (band5=nir[i][j]/maxBand5));

						amostasAgua.write(i+","+j+","+String.format(Locale.US,"%.6f", ndvi)+","+String.format(Locale.US,"%.6f",evi)+","+String.format(Locale.US,"%.6f",ndwi)+","+String.format(Locale.US,"%.6f",band5)+"\n");
					}
				}
			}

			amostasAgua.close();
			amostasSolo.close();
			amostasVeget.close();
			
			methods.gravarImagem(tmpsolo, "C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/SaoLourenco Bandas Spectrais/", "testeSolo");
			methods.gravarImagem(tmpagua, "C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/SaoLourenco Bandas Spectrais/", "testeagua");
			methods.gravarImagem(tmpveget, "C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/SaoLourenco Bandas Spectrais/", "testeVeget");

		}catch(Exception ex){
			System.err.println(ex.getLocalizedMessage());
			ex.printStackTrace();
		}

	}
}