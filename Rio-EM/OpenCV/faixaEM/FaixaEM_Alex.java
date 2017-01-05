package faixaEM;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;

import metodos.MetodosEM;
import metodos.MetodosRF;

public class FaixaEM_Alex {
	public static void main(String[] args) throws LineUnavailableException {
		try{
			String dir="C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2015-08-17\\imagensFaixaScanner\\";		
			Stack<File> pilhaArqs =  new Stack<File>();			
			File folder =  new File(dir+"test2\\");
			pilhaArqs=MetodosEM.empilharArquivosDiretorio(folder, pilhaArqs);
			for (File file : pilhaArqs) {
				BufferedImage bf =FaixaEM(dir, file.getName());
				ImageIO.write(bf, "JPG", new File(dir+"2015-09-21\\"+file.getName()));
			}
			MetodosEM.fazerSom();
		}catch(Exception e){
			e.printStackTrace();
			MetodosEM.fazerSom();
		}
	}

	public static BufferedImage FaixaEM(String dir, String nomeImgEextns) throws Exception {

		MetodosRF methods = new MetodosRF();
		int[][] red= MetodosRF.leImagem(dir+nomeImgEextns, "Red");
		int[][] green= MetodosRF.leImagem(dir+nomeImgEextns, "Green");
		int[][] blue= MetodosRF.leImagem(dir+nomeImgEextns, "Blue");



		int[][] janela= new int[3][3];

		//Pixels da janela de convolução
		int[][] pixelR =  new int[janela.length][janela.length];
		int[][] pixelG =  new int[janela.length][janela.length];
		int[][] pixelB =  new int[janela.length][janela.length];

		//Imagem textura
		int[][] textR = new int[red.length][red[0].length];
		int[][] textG = new int[red.length][red[0].length];
		int[][] textB = new int[red.length][red[0].length];

		//Loop para formar o LBPs
		for (int i = (int) Math.floor(janela.length/2); i < textR.length - (int) Math.floor(janela.length/2); i++) {
			for (int j = (int) Math.floor(janela.length/2); j < textR[i].length - (int) Math.floor(janela.length/2); j++) {
				pixelR =  new int[janela.length][janela.length];
				pixelG =  new int[janela.length][janela.length];
				pixelB =  new int[janela.length][janela.length];
				for (int k = -(int) Math.floor(janela.length/2); k <= (int) Math.floor(janela.length/2); k++) {
					for (int l = -(int) Math.floor(janela.length/2); l <= (int) Math.floor(janela.length/2); l++) {
						pixelR[k+(int) Math.floor(janela.length/2)][l+ (int) Math.floor(janela.length/2)]= red[k+i][l+j];
						pixelG[k+(int) Math.floor(janela.length/2)][l+ (int) Math.floor(janela.length/2)]= green[k+i][l+j];
						pixelB[k+(int) Math.floor(janela.length/2)][l+ (int) Math.floor(janela.length/2)]= blue[k+i][l+j];
					}
				}
				textR[i][j]= methods.getLBPVizinhos(pixelR);
				textG[i][j]= methods.getLBPVizinhos(pixelG);
				textB[i][j]= methods.getLBPVizinhos(pixelB);
			}
		}

//		methods.gravarImagemColorida(textR, textG, textB, dir, "2015-09-21\\text_"+nomeImgEextns);


		int limiar=250; //O que sera considerado como branco
		int crop=0;// Quantidade de linhas que deve ser cortada
		int cont=0;//Variavel para controlar o limite de linhas pedido

		/* ---------- PASSO 1 ---------- */
		for (int i = red.length - (int) Math.floor(janela.length/2); i >= (int) Math.floor(janela.length/2); i--) {
			cont+=1;
			int linhaR=0;
			int linhaG=0;
			int linhaB=0;

			if(cont<200){//Limite de linhas
				//formar as linhas da imagem original
				for (int j = red[i].length -(int) Math.floor(janela.length/2); j >=(int) Math.floor(janela.length/2); j--) {
					linhaR+=red[i][j];	
					linhaG+=green[i][j];	
					linhaB+=blue[i][j];	

				}
				//formalizar o somtarorio da linha
				linhaR/=red[i].length;
				linhaG/=red[i].length;
				linhaB/=red[i].length;
				//teste para saber se corta a imagem
				if(linhaR>limiar || linhaG>limiar || linhaB>limiar)
					crop+=1;
				else
					break;
			}
		}

		//Copiando a imagem original
		int[][] newR = new int[red.length-crop][red[0].length];
		int[][] newG = new int[red.length-crop][red[0].length];
		int[][] newB = new int[red.length-crop][red[0].length];
		for (int i = 0; i < newR.length; i++) {
			for (int j = 0; j < newR[i].length; j++) {
				newR[i][j]=red[i][j];
				newG[i][j]=green[i][j];
				newB[i][j]=blue[i][j];
			}
		}
		//methods.gravarImagemColorida(newR, newG, newB, dir, "2015-09-21\\crop_"+nomeImgEextns);


		/* ---------- PASSO 2 ---------- */
		int[][] imgVarR= new int[newR.length][newR[0].length];
		int[][] imgVarG= new int[newG.length][newG[0].length];
		int[][] imgVarB= new int[newB.length][newB[0].length];

		cont=0;//Variavel para controlar o limite de linhas pedido
		for (int i = (newR.length - (int) Math.floor(janela.length/2)-1); i >= (int) Math.floor(janela.length/2); i--) {
			cont+=1;
			for (int j = (newR[i].length -(int) Math.floor(janela.length/2)-1); j >=(int) Math.floor(janela.length/2); j--) {
				double somatR=0;
				double somatG=0;
				double somatB=0;
				if(cont<=200){
					for (int k = -(int) Math.floor(janela.length/2); k <= (int)Math.floor(janela.length/2); k++) {
						for (int l = -(int) Math.floor(janela.length/2); l <=(int)Math.floor(janela.length/2); l++) {
							//	System.out.println(i+", "+j+", "+k+", "+l+", ");
							somatR+=newR[i+k][j+l];
							somatG+=newG[i+k][j+l];
							somatB+=newB[i+k][j+l];
						}
					}
					double mediaR=somatR/(janela.length*janela.length);
					double mediaG=somatG/(janela.length*janela.length);
					double mediaB=somatB/(janela.length*janela.length);
					//Montando as imagens de variancia
					imgVarR[i][j]=((((int)(somatR-mediaR))^2)/255);
					imgVarG[i][j]=((((int)(somatG-mediaG))^2)/255);
					imgVarB[i][j]=((((int)(somatB-mediaB))^2)/255);
				}
			}
		}
		//methods.gravarImagemColorida(imgVarR, imgVarG, imgVarB, dir,  "2015-09-21\\var_"+nomeImgEextns);

		//Histograma das linhas
		int[] linhaR=new int[256];
		int[] linhaG=new int[256];
		int[] linhaB=new int[256];

		int ultimaLinhaPreta = 0;//Variavel para controlar qual sera a linha que deve começar 
		cont=0;//Variavel para controlar o limite de linhas pedido

		loop://label do loop para usar o break em um lugar específico;
			for (int i = imgVarR.length-1 ; i >= (int) Math.floor(janela.length/2); i--) {
				cont+=1;
				int aux=0;
				linhaR=new int[256];
				linhaG=new int[256];
				linhaB=new int[256];
				if(cont<=200){
					//Montando o histograma para a linha
					for (int j = imgVarR[i].length-1 ; j >=(int) Math.floor(janela.length/2); j--) {
						//if(j==imgVarR[i].length-1)
						//System.out.println("fazendo linha");
						linhaR[imgVarR[i][j]]+=1;
						linhaG[imgVarG[i][j]]+=1;
						linhaB[imgVarB[i][j]]+=1;
					}
					//A partir do histograma...
					for (int k = 0; k < linhaR.length; k++) {
						//System.out.println("k: "+k);
						//System.out.println("i: "+i);
						//System.out.println("Valor: "+linhaR[k]+"\n\n");
						/**Teste para saber se é a última posição da linha, e se não tem 80% dos pixels*/
						if(aux==linhaR.length-1 &&  (linhaR[k]<imgVarR[0].length*0.8 || linhaG[k]<imgVarG[0].length*0.8 || linhaB[k]<imgVarB[0].length*0.8)){
							ultimaLinhaPreta=i-1;
							//System.out.println("break");

							break loop;//Break na label acima, para ele poder pegar a proxima linha da imagem
						}
						/**Testa se a posição do histograma tem mais de 80% dos pixels*/
						else if(linhaR[k]>=imgVarR[0].length*0.8 || linhaG[k]>=imgVarG[0].length*0.8 || linhaB[k]>=imgVarB[0].length*0.8){
							//System.out.println("if");
							//Se tiver os 80%, pinta a linha de preto
							for (int j = imgVarR[i].length-1 ; j >=(int) Math.floor(janela.length/2); j--) {
								newR[i][j]=0;
								newG[i][j]=0;
								newB[i][j]=0;
							}
						}
						else{
							aux+=1;
						}

					}
				}
			}

		// ------------- Passo 4 ------------- //

		/* Variavel para percorrer a quantidade de pixels da linha por vez, e não a linha inteita		 */
		int max= (int)Math.floor(newR[0].length/100);
		max = (int)Math.floor(newR[0].length/max);

		//Variaveis para o histograma
		linhaR=new int[256];
		linhaG=new int[256];
		linhaB=new int[256];

		cont=0;//Variavel para controlar o limite de linhas pedido

		//Variavel para controle da quantidade de vezes que ele percorreu o limite de pixels acima,
		// e não a linha inteira
		int auxCont=1;

		loopLinha://label do loop para usar o break em um lugar específico;
			//Começando o loop da ultima linha pintado de preto (passo 3)
			for (int i = ultimaLinhaPreta ; i >= (int) Math.floor(janela.length/2); i--) {
				cont+=1;
				//System.out.println("comeco cont: "+auxCont);
				int aux=0;
				linhaR=new int[256];
				linhaG=new int[256];
				linhaB=new int[256];
				//System.out.println(cont);
				//System.out.println(max);

				//limite de linhas pedido
				if(cont<=30 ){

					//Teste para saber se a última loop dos pixels, como havia pedido no email
					//Por exemplo, se percorrermos de 100 em 100 pixels,
					//este testa se é a última dessas iterações
					if(auxCont==(int)Math.floor(newR[0].length/100)){
						auxCont=1;
						break loopLinha;//Sai do loop e vai para a proxima linha
					}
					//System.out.println("\nauxcont: "+auxCont);
					//System.out.print("j: ");

					//Percorrendo a quantidade de pixels relativo a variavel auxCont, 
					//isto é, neste caso, de 100 a 100 
					for (int j = auxCont*max ; j >=(auxCont*max)-max; j--) {
						//System.out.print(j+"\t");
						//if(j==imgVarR[i].length-1)
						//System.out.println("fazendo linha");

						/**Montando histograma*/
						linhaR[imgVarR[i][j]]+=1;
						linhaG[imgVarG[i][j]]+=1;
						linhaB[imgVarB[i][j]]+=1;
					}

					loop:
						for (int k = 0; k < linhaR.length; k++) {
							/*	System.out.println("\nk: "+k);
								System.out.println("i: "+i);
								System.out.println("Valor: "+linhaR[k]);
								System.out.println("aux: "+aux);
								System.out.println("max: "+max+"\n\n");
							 */
							/**Teste para saber se é a última posição da linha, e se não tem 80% dos pixels*/
							if(aux==linhaR.length && (linhaR[k]<max*0.8 || linhaG[k]<max*0.8 || linhaB[k]<max*0.8)){
								//System.out.println("break");
								System.out.println("if cont: "+auxCont);
								auxCont+=1;
								break loop;
							}
							/**Testa se a posição do histograma tem mais de 80% dos pixels*/
							else if(linhaR[k]>=max*0.8 || linhaG[k]>=max*0.8 || linhaB[k]>=max*0.8){
								//System.out.println("if");
								//System.out.println("elseif cont: "+auxCont);
								/**Se tiver os 80%, pinta a quantidade respectiva de pixels de preto*/
								for (int j = auxCont*max; j >=(auxCont*max)-max; j--) {
									//System.out.println("paint");
									newR[i][j]=0;
									newG[i][j]=0;
									newB[i][j]=0;
								}
								auxCont+=1;
								break loop;//Sai do loop de pixels (100 a 100)
							}
							else{
								aux+=1;
								//System.out.println("else cont: "+auxCont);

							}

						}
				}
			}

		BufferedImage buff = (new MetodosRF()).matrizColorToBufferImage(newR, newG, newB);
		return buff;

		/*		
		 * methods.gravarImagemColorida(newR, newG, newB, dir, "final.jpg");
			methods.fazerSom();
		 */	
	}

}
