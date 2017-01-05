package procImg;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class MetodosProcImagens {

	private static String dir="1";
	private static int ite;


	//mtriz global para controle
	public static int[][] arrayObjeto;

	public static void contarCorCinzas(int array[][]){

		int[] cores=new int[256];
		int x=0;

		for (int i=0; i<array.length; i++){
			for(int j=0; j<array[i].length;j++){
				x=array[i][j];
				cores[x]+=1;


			}
		}

		for (int i = 0; i < cores.length; i++) {
			System.out.println(+cores[i]+"pixels de cor"+i);
		}		
	}

	public static int contarObjetos4(int array[][], int limLinha, int limColuna, int conect){
		int obj=0;
		int arrayAux[][]= new int[limLinha][limColuna];
		for (int i=0;i<limLinha;i++){
			for(int j=0;j<limColuna;j++){
				arrayAux[i][j]=array[i][j];
			}
		}

		for (int linha=0;linha<limLinha;linha++){
			for(int coluna=0;coluna<limColuna;coluna++){
				if(arrayAux[linha][coluna]==conect){
					obj+=1;
					visitar4(arrayAux,linha,coluna,limLinha,limColuna,conect);
				}
			}
		}

		return obj;
	}



	public static int contarObjetos8(int array[][], int limLinha, int limColuna, int conect){
		int obj=0;
		int arrayAux[][]= new int[limLinha][limColuna];
		for (int i=0;i<limLinha;i++){
			for(int j=0;j<limColuna;j++){
				arrayAux[i][j]=array[i][j];
			}
		}

		for (int linha=0;linha<limLinha;linha++){
			for(int coluna=0;coluna<limColuna;coluna++){
				if(arrayAux[linha][coluna]==conect){
					obj+=1;	
					visitar8(arrayAux, linha, coluna, limLinha, limColuna,conect);
				}
			}
		}
		return obj;
	}

	public static int[][] convolução(int matriz[][], double[][] elem){
		int matrizAux[][] = new int[matriz.length][matriz.length];
		int matrizResultado[][]=new int[matriz.length][matriz.length];


		for (int i=0; i<matriz.length; i++){
			for(int j=0; j<matriz.length;j++){
				matrizAux[i][j]=matriz[i][j];
			}
		}

		@SuppressWarnings("unused")
		int cont;
		double novoValor;
		for (int k=(int) Math.floor((elem.length/2)); k<matrizAux.length-(int) Math.floor((elem.length/2));k++){
			for (int l=(int) Math.floor((elem.length/2));l<matrizAux.length-(int) Math.floor((elem.length/2));l++){
				cont=0;
				novoValor=0;
				for (int m= -(int) Math.floor((elem.length/2));m<=(int) Math.floor((elem.length/2));m++){
					for (int n= -(int) Math.floor((elem.length/2));n<=(int) Math.floor((elem.length/2));n++){

						novoValor+=elem[m+(int) Math.floor((elem.length/2))][(int) Math.floor((elem.length/2))+n] * matrizAux[k+m][l+n];
						cont+=1;
						//System.out.println("k:"+k+" l:"+l+" m:"+m+" n:"+n);

						matrizResultado[k][l]=(int)novoValor;
					}

				}


			}
		}
		return matrizResultado;
	}


	public static int[][] ruido(int matriz[][]){
		int matrizAux[][] = new int[matriz.length][matriz.length];
		int matrizResultado[][]=new int[matriz.length][matriz.length];


		for (int i=0; i<matriz.length; i++){
			for(int j=0; j<matriz.length;j++){
				matrizAux[i][j]=matriz[i][j];
			}
		}

		int cont=0;

		for (int k=2; k<matrizAux.length-2;k++){
			for (int l=2;l<matrizAux.length-2;l++){
				if(cont==4){
					matrizResultado[k][l]=0;
					matrizResultado[k][l+2]=0;
					matrizResultado[k+2][l]=0;
					matrizResultado[k][l-2]=0;
					matrizResultado[k-2][l]=0;
				
					matrizResultado[k+2][l+2]=0;
					matrizResultado[k+2][l+2]=0;
					matrizResultado[k-2][l-2]=0;
					matrizResultado[k-2][l-2]=0;
				
					
					
					cont=0;
				}

				else{
					matrizResultado[k][l]=matrizAux[k][l];
					cont+=1;
				}

			}
		}
		return matrizResultado;
	}


	public static int[][] convoluçãoMediana(int matriz[][],int tamJanela, int elementoEstruturante){
		int matrizAux[][] = new int[matriz.length][matriz[0].length];
		int matrizResultado[][]=new int[matriz.length][matriz[0].length];
		int janela[][]= new int[tamJanela][tamJanela];

		for (int g=0; g<janela.length;g++){
			for (int h=0; h<janela[g].length;h++){
				janela[g][h]=elementoEstruturante;
			}
		}

		for (int i=0; i<matriz.length; i++){
			for(int j=0; j<matriz[i].length;j++){
				matrizAux[i][j]=matriz[i][j];
			}
		}

		int vetor[]=new int[tamJanela*tamJanela];
		int mediana = 0;
		int x;
		int cont;
		for (int k=(int) Math.floor((janela.length/2)); k<matrizAux.length-(int) Math.floor((janela.length/2));k++){
			for (int l=(int) Math.floor((janela.length/2));l<matrizAux[0].length-(int) Math.floor((janela.length/2));l++){
				cont=0;
				for (int m= -(int) Math.floor((janela.length/2));m<=(int) Math.floor((janela.length/2));m++){
					for (int n= -(int) Math.floor((janela.length/2));n<=(int) Math.floor((janela.length/2));n++){

						vetor[cont]=janela[m+(int) Math.floor((janela.length/2))][(int) Math.floor((janela.length/2))+n] * matrizAux[k+m][l+n];
						cont+=1;
						//System.out.println("k:"+k+" l:"+l+" m:"+m+" n:"+n);
						Arrays.sort(vetor);

						x= (int) Math.ceil(vetor.length/2);
						//System.out.println(x);
						mediana=vetor[x];
						//System.out.println(mediana);


					}
				}
				matrizResultado[k][l]=mediana;
			}
		}


		return matrizResultado;
	}

	public static int[][] convoluçãoMédiaSeletiva(int matriz[][], double[][] elem){
		int matrizAux[][] = new int[matriz.length][matriz.length];
		int matrizResultado[][]=new int[matriz.length][matriz.length];


		for (int i=0; i<matriz.length; i++){
			for(int j=0; j<matriz.length;j++){
				matrizAux[i][j]=matriz[i][j];
			}
		}

		@SuppressWarnings("unused")
		int cont;
		double novoValor;
		for (int k=(int) Math.floor((elem.length/2)); k<matrizAux.length-(int) Math.floor((elem.length/2));k++){
			for (int l=(int) Math.floor((elem.length/2));l<matrizAux.length-(int) Math.floor((elem.length/2));l++){
				cont=0;
				novoValor=0;
				if(matrizAux[k][l]==255 || matrizAux[k][l]==0){
					for (int m= -(int) Math.floor((elem.length/2));m<=(int) Math.floor((elem.length/2));m++){
						for (int n= -(int) Math.floor((elem.length/2));n<=(int) Math.floor((elem.length/2));n++){
							novoValor+=elem[m+(int) Math.floor((elem.length/2))][(int) Math.floor((elem.length/2))+n] * matrizAux[k+m][l+n];
							cont+=1;
							//System.out.println("k:"+k+" l:"+l+" m:"+m+" n:"+n);

							matrizResultado[k][l]=(int)novoValor;
						}
					}

				}
				else{
					matrizResultado[k][l]=matrizAux[k][l];

				}



			}
		}
		return matrizResultado;
	}


	public static int[][] convoluçãoMédiaSeletivaSemElemento(int matriz[][], double[][] elem){
		int matrizAux[][] = new int[matriz.length][matriz.length];
		int matrizResultado[][]=new int[matriz.length][matriz.length];


		for (int i=0; i<matriz.length; i++){
			for(int j=0; j<matriz.length;j++){
				matrizAux[i][j]=matriz[i][j];
			}
		}

		@SuppressWarnings("unused")
		int cont;
		double contMedia;
		double novoValor;
		for (int k=(int) Math.floor((elem.length/2)); k<matrizAux.length-(int) Math.floor((elem.length/2));k++){
			for (int l=(int) Math.floor((elem.length/2));l<matrizAux.length-(int) Math.floor((elem.length/2));l++){
				cont=0;
				contMedia=0;
				novoValor=0;
				for (int m= -(int) Math.floor((elem.length/2));m<=(int) Math.floor((elem.length/2));m++){
					for (int n= -(int) Math.floor((elem.length/2));n<=(int) Math.floor((elem.length/2));n++){
						if(matrizAux[m+k][n+l]!=0){
							elem[m+(int) Math.floor((elem.length/2))][(int) Math.floor((elem.length/2))+n]=1;
							contMedia+=1;
						}
						else{
							elem[m+(int) Math.floor((elem.length/2))][(int) Math.floor((elem.length/2))+n]=0;
						}
					}
				}

				for (int j = 0; j < elem.length; j++) {
					for (int j2 = 0; j2 < elem.length; j2++) {
						if(elem[j][j2]==1){
							elem[j][j2]=1.0/contMedia;
						}
					}
				}

				if(matrizAux[k][l]==255 || matrizAux[k][l]==0){
					for (int m= -(int) Math.floor((elem.length/2));m<=(int) Math.floor((elem.length/2));m++){
						for (int n= -(int) Math.floor((elem.length/2));n<=(int) Math.floor((elem.length/2));n++){
							novoValor+=elem[m+(int) Math.floor((elem.length/2))][(int) Math.floor((elem.length/2))+n] * matrizAux[k+m][l+n];
							cont+=1;
							//System.out.println("k:"+k+" l:"+l+" m:"+m+" n:"+n);

							matrizResultado[k][l]=(int)novoValor;
						}
					}

				}
				else{
					matrizResultado[k][l]=matrizAux[k][l];

				}



			}
		}
		return matrizResultado;
	}



	public static void distChess(int array[][]){

		for (int i=0; i<array.length ;i++){
			for (int j=0; j<array[i].length;j++){
				if(array[i][j]==array[5][5]){
					int x,x1,x2;
					x1=(5-i);
					x2=(5-j);
					x=Math.max((Math.abs(x1)),(Math.abs(x2)));
					array[i][j]=x;
				}
			}

			System.out.println("\n\n");
			imprimirMatrizInt(array);
		}

	}

	public static void distEuclidiana(double array[][]){

		for (int i=0; i<array.length ;i++){
			for (int j=0; j<array[i].length;j++){
				if(array[i][j]==array[5][5]){
					double x,x1,x2;
					//float y = new Float(Float.NaN);
					x1=(5-i);
					x2=(5-j);
					x=(Math.pow(x1, 2))+(Math.pow(x2,2));
					x=Math.sqrt(x);
					System.out.println("["+i+"]["+j+"]="+x);
					array[i][j]=x;
				}
			}
		}
		System.out.println("\n\n");
		imprimirMatrizDouble(array);
	}


	public static void distManhattan(int array[][]){

		for (int i=0; i<array.length ;i++){
			for (int j=0; j<array[i].length;j++){
				if(array[i][j]==array[5][5]){
					int x,x1,x2;
					x1=(5-i);
					x2=(5-j);
					x=(Math.abs(x1))+(Math.abs(x2));
					array[i][j]=x;
				}
			}
		}
		System.out.println("\n\n");
		imprimirMatrizInt(array);
	}

	public static int[][] diferença(int[][] matriz1, int[][] matriz2){
		int[][] matrizResult= new int[Math.max(matriz1.length,matriz2.length)][Math.max(matriz1.length,matriz2.length)];
	
		for (int i = 0; i < matrizResult.length; i++) {
			for (int j = 0; j < matrizResult.length; j++) {
				matrizResult[i][j]=Math.abs(matriz1[i][j]-matriz2[i][j]);
			}
		}
		
		return matrizResult;
	}


	public static int[] equalizar(int array[][]){

		int cores[]=new int[256];
		int novaCor[]= new int[256];
		int arrayAux[][] = new int[array.length][array.length];
		int x;
		@SuppressWarnings("unused")
		int qntdCores=0;

		for (int i=0; i<array.length; i++){
			for(int j=0; j<array[i].length;j++){
				arrayAux[i][j]=array[i][j];
				x=array[i][j];
				cores[x]+=1;


			}
		}

		for (int k=0; k<cores.length; k++){
			novaCor[k]= cores[k];
			if(cores[k]!=0){
				//System.out.println("Cor="+k);
				qntdCores+=1;
			}
		}

		int livre=0;

		for(int l=0; l<novaCor.length;l++){
			if(novaCor[l]<200 || novaCor[l]>500){
				novaCor[l]=0;
				livre+=1;

			}
		}
		/*		for (int i = 0; i < novaCor.length; i++) {
			if(novaCor[i]!=0){
				System.out.println(novaCor[i]+" pixels de cor "+i);	
			}
		}
		 */	
		mostrarGrafico(novaCor, "Intermediário");
		System.out.println(livre);
		int result=Math.abs(256-livre);
		int espaço=(int) Math.floor(livre/result);

		System.out.println("resultado:"+result+" espaço:"+espaço);



		int cont=0;
		int corFinal[]= new int[257];

		for (int i = 0; i < novaCor.length; i++) {
			if(novaCor[i]!=0 && cont+espaço<256){
				corFinal[cont]=novaCor[i];
				cont+=espaço;

			}
		}

		/*for (int l=0; l<corFinal.length;l++){
			if(corFinal[l]!=0){
				System.out.println(corFinal[l]+" pixels de cor "+l);
			}
		}*/
		int matriz[][]= new int[2][256];
		for (int i = 0; i <2; i++) {
			for (int j = 0; j < 256; j++) {
				if(i==0 && novaCor[j]!=0 ){
					matriz[i][j]=novaCor[j];
				}
				else if(i==1 && corFinal[j]!=0){
					matriz[i][j]=corFinal[j];
				}
			}

		}



		for (int i = 0; i <2; i++) {
			for (int j = 0; j < 256; j++) {
				System.out.print(matriz[i][j]+" ");
			}
			System.out.println("\n");
		}
		//System.out.println("Existem "+qntdCores+" cores");
		//return novaCor;
		return corFinal;

	}




	public static int[][] zoom(int[][] matriz, int tamZoom){
		int[][] matrizSaida= new int[tamZoom*matriz.length][tamZoom*matriz.length];
		int[][] matrizAux= new int[matriz.length][matriz.length];

		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz.length; j++) {
				matrizAux[i][j]=matriz[i][j];
			}
		}


		for (int i=0; i<matrizSaida.length; i++){
			for(int j=0; j<matrizSaida.length;j++){
				matrizSaida[i][j]=0;
			}
		}

		for (int i=0; i<matrizAux.length-1; i++){
			for(int j=0; j<matrizAux.length-1;j++){
				matrizSaida[i*tamZoom][j*tamZoom]=matrizAux[i][j];
			}
		}

		double[][] elem= new double[tamZoom+1][tamZoom+1];

		matrizSaida=convoluçãoMédiaSeletivaSemElemento(matrizSaida, elem);

		/*
		for (int i = 0; i < elem.length; i++) {
			for (int j = 0; j < elem.length; j++) {
				if((i==0 && j==0) || (i==0 && j==elem.length-1) || (i==elem.length-1 && j==0) || (i==elem.length-1 && j==elem.length-1)  ){
					elem[i][j]=x;
				}
				else{
					elem[i][j]=0;
				}
			}
		}

		double x1=1.0/4.0;
		double elem2[][]={{0,x1,0},
						  {x1,0,x1},	
						  {0,x1,0}};

		matrizSaida=convoluçãoMédiaSeletiva(matrizSaida, elem2);
		 */	
		return matrizSaida;
	}

	public static void gravarImagem(int matriz[][], String dir){
		try{
			BufferedImage imagem= new BufferedImage(matriz[0].length, matriz.length, 5);
			System.out.println("entra gravar imagem");
			for (int i=0; i<matriz.length; i++){
				for(int j=0; j<matriz[i].length;j++){
					imagem.setRGB(j, i, new Color(matriz[i][j],matriz[i][j],matriz[i][j]).getRGB());

				}
			}
			ImageIO.write(imagem, "PNG", new File(dir+".png"));
			System.out.println("sai gravar imagem");
		}
		catch(Exception ex){
			ex.getMessage();
		}
	}

	public static void gravarImagemColorida(int matrizRed[][], int matrizGreen[][], int matrizBlue[][], String dir){
		try{
			BufferedImage imagem= new BufferedImage(matrizRed.length, matrizRed.length, 5);
			System.out.println("entra gravar imagem");
			for (int i=0; i<matrizRed.length; i++){
				for(int j=0; j<matrizRed[i].length;j++){
					imagem.setRGB(j, i, new Color(matrizRed[i][j],matrizGreen[i][j],matrizBlue[i][j]).getRGB());

				}
			}
			ImageIO.write(imagem, "BMP", new File("C:/Users/Re/Dropbox/workspace/PDI/src/Imagens"+dir+".bmp"));
			System.out.println("sai gravar imagem");
		}
		catch(Exception ex){
			ex.getMessage();
		}
	}

	public static void  imprimirMatrizDouble(double array[][]){
		//imprimir a matriz
		for (int i=0; i<array.length; i++) {
			for (int j=0; j<array[i].length; j++) {
				System.out.printf(" %.1f ",array[i][j]);
			}
			System.out.printf("\n");
		}
		System.out.printf("\n");

	}


	public static void  imprimirMatrizInt(int array[][]){
		//imprimir a matriz
		for (int i=0; i<array.length; i++) {
			for (int j=0; j<array[i].length; j++) {
				if(array[i][j]==10){
					System.out.printf("%d ", array[i][j]);
				}
				else {
					System.out.printf(" %d ", array[i][j]);
				}

			}
			System.out.printf("\n");
		}
		System.out.printf("\n");

	}


	public static int[][] leImagem(String nome, String canalRgb){

		int array[][]=null;
		BufferedImage objeto;
		try {
			objeto = ImageIO.read(new File(nome));
			System.out.println("Tipo objeto: "+objeto.getType());
			array= new int[objeto.getHeight()][objeto.getWidth()];
			for (int i=0; i<objeto.getHeight(); i++){
				for(int j=0; j<objeto.getWidth();j++){
					if(canalRgb=="Red")
						array[i][j]=new Color(objeto.getRGB(j, i)).getRed();
					else if(canalRgb=="Green")
						array[i][j]=new Color(objeto.getRGB(j, i)).getGreen();
					else if(canalRgb=="Blue")
						array[i][j]=new Color(objeto.getRGB(j, i)).getBlue();

					//System.out.println(array[i][j]);
				}
			}
			return array;

		} catch (IOException e) {
			e.printStackTrace();
			return array;

		}



	}



	public static void mostrarGrafico(int[] cores, String nome){
		String x;
		JFrame janela = new JFrame();
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();


		for (int i = 0; i < cores.length; i++) {
			if(i==256){
				x=i+"";

			}
			else if(i==180){
				x=i+"";

			}
			else if(i==90){
				x=i+"";

			}
			else{ x=i+"";}
			dataset.addValue(cores[i], "Cor",x );

		}

		JFreeChart grafi = ChartFactory.createBarChart(nome,
				"Intensidade",
				"Frequencia de pixels ",
				dataset,
				PlotOrientation.VERTICAL,true, true, false);

		janela.add(new ChartPanel(grafi));
		janela.setVisible(true);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janela.pack();

	}

	public static int[][] segmentaçãoPorLimiarização(int matriz[][],int limiar[] ){
		int matrizAux[][] = new int[matriz.length][matriz.length];

		for (int i=0; i<matriz.length; i++){
			for(int j=0; j<matriz[i].length;j++){
				matrizAux[i][j]=matriz[i][j];

			}
		}

		for (int i=0; i<matrizAux.length; i++){
			for(int j=0; j<matrizAux[i].length;j++){
				for(int k=0;k<limiar.length;k++){
					if(k==0){
						if(matrizAux[i][j]<limiar[k] ){
							//System.out.println("=0");
							
							matrizAux[i][j]=0;
							break;
						}
						else{
							matrizAux[i][j]=255;
							break;
						}
					}
					else if(k==limiar.length-1 ){
						if(matrizAux[i][j]>=limiar[k]){
							//System.out.println(">ultimo");
							matrizAux[i][j]=255;
							
							break;
						}
					}

					else{
						if(matrizAux[i][j]>=limiar[k-1] && matrizAux[i][j]<limiar[k]){
							System.out.println("=limiar");
							matrizAux[i][j]=limiar[k];
							break;
						}
					}


				}
			}
		}
		return matrizAux;
	}




	public static void separar8(int array[][], int linha, int coluna, int limLinha , int limColuna, int conect){
		ite+=1;
		System.out.println(ite);

		if(ite==1){
			for (int i=0;i<limLinha;i++){
				for(int j=0;j<limColuna;j++){
					arrayObjeto[i][j]=255;
				}
			}
		}

		//seta o -1(visitado)
		System.out.println();
		array[linha][coluna]=-1;
		//testa a pra não dar exceção e visita o da esquerda
		if(linha-1 >=0 && array[linha-1][coluna]==conect){
			arrayObjeto[linha-1][coluna]=0;
			separar8(array, linha-1, coluna, limLinha, limColuna,conect);

		}

		//testa pra ver se não excede o limite linha da mtriz e visita o da direita
		if(linha+1 < limLinha && array[linha+1][coluna]==conect){
			arrayObjeto[linha+1][coluna]=0;
			separar8(array, linha+1, coluna, limLinha, limColuna,conect);

		}
		//testa a pra não dar exceção e visita o de cima
		if(coluna-1 >=0 && array[linha][coluna-1]==conect){
			arrayObjeto[linha][coluna-1]=0;
			separar8(array, linha, coluna-1, limLinha, limColuna,conect);
		}
		//testa a pra ver se não excede o limite coluna da matriz e visita o de baixo
		if(coluna+1 < limColuna && array[linha][coluna+1]==conect){
			arrayObjeto[linha][coluna+1]=0;
			separar8(array, linha, coluna+1, limLinha, limColuna,conect);
		}
		//diagonal direita pra baixo
		if(coluna+1 < limColuna && linha+1<limLinha && array[linha+1][coluna+1]==conect){
			arrayObjeto[linha+1][coluna+1]=0;
			separar8(array, linha+1, coluna+1, limLinha, limColuna,conect);
		}

		//diagonal esquerda pra baixo
		if(coluna-1 >=0 && linha+1<limLinha && array[linha+1][coluna-1]==conect){
			arrayObjeto[linha+1][coluna-1]=0;
			separar8(array, linha+1, coluna-1, limLinha, limColuna,conect);

		}
		//diagonal esquerda pra cima
		if(coluna-1 >=0 && linha-1 >=0 && array[linha-1][coluna-1]==conect){
			arrayObjeto[linha-1][coluna-1]=0;
			separar8(array, linha-1, coluna-1, limLinha, limColuna,conect);
		}
		//diagonal direita pra cima
		if(linha-1 >=0 && coluna+1 < limColuna && array[linha-1][coluna+1]==conect){
			arrayObjeto[linha-1][coluna+1]=0;
			separar8(array, linha-1, coluna+1, limLinha, limColuna,conect);
		}

	}

	public static int separarObjetos(int array[][], int limLinha, int limColuna, int conect){
		int obj=0;
		int arrayAux[][]= new int[limLinha][limColuna];
		for (int i=0;i<limLinha;i++){
			for(int j=0;j<limColuna;j++){
				arrayAux[i][j]=array[i][j];
			}
		}

		arrayObjeto= new int[limLinha][limColuna];

		for (int linha=0;linha<limLinha;linha++){
			for(int coluna=0;coluna<limColuna;coluna++){
				if(arrayAux[linha][coluna]==conect){
					obj+=1;
					System.out.println(obj);
					dir=(""+obj);

					ite=0;

					separar8(arrayAux, linha, coluna, limLinha, limColuna,conect);
					gravarImagem(arrayObjeto, dir);

				}
			}
		}
		return obj;
	}


	public static void visitar4(int array[][], int linha, int coluna, int limLinha , int limColuna, int conect){
		//seta o -1(visitado)
		array[linha][coluna]=-1;
		//testa a pra não dar exceção e visita o da esquerda
		if(linha-1 >=0 && array[linha-1][coluna]==conect){
			visitar4(array, linha-1, coluna, limLinha, limColuna,conect);
		}

		//testa pra ver se não excede o limite linha da mtriz e visita o da direita
		if(linha+1 < limLinha && array[linha+1][coluna]==conect){
			visitar4(array, linha+1, coluna, limLinha, limColuna,conect);
		}
		//testa a pra não dar exceção e visita o de cima
		if(coluna-1 >=0 && array[linha][coluna-1]==conect){
			visitar4(array, linha, coluna-1, limLinha, limColuna, conect);
		}
		//testa a pra ver se não excede o limite coluna da matriz e visita o de baixo
		if(coluna+1 < limColuna && array[linha][coluna+1]==conect){
			visitar4(array, linha, coluna+1, limLinha, limColuna,conect);
		}
	}


	public static void visitar8(int array[][], int linha, int coluna, int limLinha , int limColuna, int conect){
		//seta o -1(visitado)
		array[linha][coluna]=-1;
		//testa a pra não dar exceção e visita o da esquerda
		if(linha-1 >=0 && array[linha-1][coluna]==conect){
			visitar8(array, linha-1, coluna, limLinha, limColuna,conect);
		}

		//testa pra ver se não excede o limite linha da mtriz e visita o da direita
		if(linha+1 < limLinha && array[linha+1][coluna]==conect){
			visitar8(array, linha+1, coluna, limLinha, limColuna,conect);
		}
		//testa a pra não dar exceção e visita o de cima
		if(coluna-1 >=0 && array[linha][coluna-1]==conect){
			visitar8(array, linha, coluna-1, limLinha, limColuna,conect);
		}
		//testa a pra ver se não excede o limite coluna da matriz e visita o de baixo
		if(coluna+1 < limColuna && array[linha][coluna+1]==conect){
			visitar8(array, linha, coluna+1, limLinha, limColuna,conect);
		}
		//diagonal direita pra baixo
		if(coluna+1 < limColuna && linha+1<limLinha && array[linha+1][coluna+1]==conect){
			visitar8(array, linha+1, coluna+1, limLinha, limColuna,conect);
		}

		//diagonal esquerda pra baixo
		if(coluna-1 >=0 && linha+1<limLinha && array[linha+1][coluna-1]==conect){
			visitar8(array, linha+1, coluna-1, limLinha, limColuna,conect);
		}
		//diagonal esquerda pra cima
		if(coluna-1 >=0 && linha-1 >=0 && array[linha-1][coluna-1]==conect){
			visitar8(array, linha-1, coluna-1, limLinha, limColuna,conect);
		}
		//diagonal direita pra cima
		if(linha-1 >=0 && coluna+1 < limColuna && array[linha-1][coluna+1]==conect){
			visitar8(array, linha-1, coluna+1, limLinha, limColuna,conect);
		}
	}

}




