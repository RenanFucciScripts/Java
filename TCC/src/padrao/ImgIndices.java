package padrao;

import java.io.FileWriter;

public class ImgIndices {

	public static void main(String[] args) {
		try{
			MetodosRNA methods = new MetodosRNA();
			String dirPadrao = "C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/SaoLourenco Bandas Spectrais/";
			int[][] blue=methods.leImagem(dirPadrao+"Banda3_BLUE_16Bits.png","Blue" );
			int[][] green=  methods.leImagem(dirPadrao+"Banda2_GREEN_16Bits.png","Green");
			int[][] red= methods.leImagem(dirPadrao+"Banda1_RED_16Bits.png","Red");
			int[][] nir =  methods.leImagem(dirPadrao+"Banda5_NIR_16Bits.png","Red");

			System.out.println("1");

			int[][] imgAmostrasRed =  methods.leImagem("C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/SaoLourenco Bandas Spectrais/amostrasPadovani_agua_vegetacao_solo.png", "Red");
			int[][] imgAmostrasGreen =  methods.leImagem("C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/SaoLourenco Bandas Spectrais/amostrasPadovani_agua_vegetacao_solo.png", "Green");
			int[][] imgAmostrasBlue =  methods.leImagem("C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/SaoLourenco Bandas Spectrais/amostrasPadovani_agua_vegetacao_solo.png", "Blue");

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
			int imgNdvi [][] = new int[nir.length][nir[0].length];
			int imgEvi [][] = new int[nir.length][nir[0].length];
			int imgNdwi [][]= new int[nir.length][nir[0].length];
			int imgNir [][]= new int[nir.length][nir[0].length];


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

			for (int i = 0; i < imgAmostrasRed.length; i++) {
				for (int j = 0; j < imgAmostrasRed[i].length; j++) {
					if(imgAmostrasRed[i][j]>=255 || imgAmostrasBlue[i][j]>=255 || imgAmostrasGreen[i][j]>=255){

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
						band5=nir[i][j]/maxBand5;

						if(ndvi<0)
							ndvi=0;
						if(evi<0)
							evi=0;
						if(ndwi<0)
							ndwi=0;

						imgNdvi[i][j]=(int) (255 *(ndvi));
						imgEvi[i][j]=(int) (254* (evi));
						imgNdwi[i][j]= (int) (255*(ndwi));
						imgNir[i][j]= (int) (255* (band5));

				

					}
				}

			}

			System.out.println("Ndvi Positivo");
			methods.gravarImagem(imgNdvi, "C:/Users/Renan Fucci/Desktop/Imagem de Indices/", "ndvi_Positivo");
			System.out.println("Evi Positivo");
			methods.gravarImagem(imgEvi, "C:/Users/Renan Fucci/Desktop/Imagem de Indices/", "evi_Positivo");
			System.out.println("Ndwi Positivo");
			methods.gravarImagem(imgNdwi, "C:/Users/Renan Fucci/Desktop/Imagem de Indices/", "ndwi_Positivo");
			System.out.println("Nir Positivo");
			methods.gravarImagem(imgNir, "C:/Users/Renan Fucci/Desktop/Imagem de Indices/", "nir_Positivo");

			int[][] imgNdvineg = new int[nir.length][nir[0].length];
			int[][] imgEvineg = new int[nir.length][nir[0].length];
			int[][] imgNdwineg= new int[nir.length][nir[0].length];
			int[][] imgNirneg = new int[nir.length][nir[0].length];

			ndvi=0;
			evi=0;
			ndwi=0;
			band5=0;
			
			for (int i = 0; i < imgAmostrasRed.length; i++) {
				for (int j = 0; j < imgAmostrasRed[i].length; j++) {

					if(imgAmostrasRed[i][j]>=255 || imgAmostrasBlue[i][j]>=255 || imgAmostrasGreen[i][j]>=255){
						
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
						band5=nir[i][j]/maxBand5;

						if(ndvi>0)
							ndvi=0;
						if(evi>0)
							evi=0;
						if(ndwi>0)
							ndwi=0;

						imgNdvineg[i][j]=(int) (254 *(Math.abs(ndvi)));
						imgEvineg[i][j]=(int) (254* (Math.abs(evi)));
						imgNdwineg[i][j]=(int) (254*(Math.abs(ndwi)));
						imgNirneg[i][j]= (int) (254* (Math.abs(band5)));



					}
				}

			}
			System.out.println("Ndvi negativo");
			methods.gravarImagem(imgNdvi, "C:/Users/Renan Fucci/Desktop/Imagem de Indices/", "ndvi_Negativo");
			System.out.println("Evi negativo");
			methods.gravarImagem(imgEvi, "C:/Users/Renan Fucci/Desktop/Imagem de Indices/", "evi_Negativo");
			System.out.println("Ndwi negativo");
			methods.gravarImagem(imgNdwi, "C:/Users/Renan Fucci/Desktop/Imagem de Indices/", "ndwi_Negativo");
			System.out.println("Nir negativo");
			methods.gravarImagem(imgNir, "C:/Users/Renan Fucci/Desktop/Imagem de Indices/", "nir_Negativo");

		}catch(Exception ex){
			System.err.println(ex.getLocalizedMessage());
			System.err.println(ex.getMessage());
			ex.printStackTrace();
		}
	}
}