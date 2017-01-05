package metodos;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.imageio.ImageIO;


/**
 * Classe com metodos de Redes Neurais Artificiais e Processamento de Imagem. 
 * 
 * @author Renan Fucci
 * */
public class MetodosRNAA {


	/**
	 * @deprecated
	 * <b>Metodo execPerceptron</b>
	 * Esse metodo vai fazer a execucao do Perceptron Linear apos sua etapa de treinamento.
	 * @param wfinal[][](double)  transposta do vetor de pesos ja treinado. 
	 * @param vetorExec[](double) vetor de caracteristica que deve ser classificado.
	 * @param limiar(double) constante limiar, neste caso da funcao degrau.
	 * @return u(int) classe correspondente ao vetor de caracteristica a ser classificado.
	 * */
	public void execPerceptron(double[][] wfinal,  double limiar){
		try{
			
			Calendar cal =  Calendar.getInstance();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
			String nomeArquivo = dateFormat.format(cal.getTime());
			/*for (int i = 0; i < wfinal.length; i++) {
				for (int j = 0; j < wfinal[i].length; j++) {
					nomeArquivo+= (wfinal[i][j]+", ");
				}
			}*/
			/**Pessoal*/
			//String caminho = "C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/dadosExecucao.txt";
			/**LADESP*/
			String caminho = "D:/Usuarios/renan.fucci/Documents/dadosExecucao.txt";
			
			FileReader file = new FileReader(caminho);
			
			BufferedReader leitura= new BufferedReader(file);
			
			int[][] img= new int[2501][2501];
			

			String linha= leitura.readLine();
			
			int i;
			int j;
			double ndvi;
			double evi;
			String[] div;
			
			while(linha!=null){
				
				div= linha.split(",");
				i=Integer.parseInt(div[0]);
				j=Integer.parseInt(div[1]);
				ndvi=Double.parseDouble(div[2]);
				evi=Double.parseDouble(div[3]);
				//System.out.println("i:"+i+" j:"+j+" ndvi:"+ndvi+" evi:"+evi);
				double[][] m={{-1},{ndvi},{evi}};
				//System.out.println("U:"+fun—É—?oU(m, wfinal, limiar));
				img[i][j]=funcaoUExec(m, wfinal, limiar);
				linha=leitura.readLine();
			}
			
			/**Pessoa*/
			//String dir= "C:/Users/Renan Fucci/Dropbox/Projeto - Embrapa/Imagens/"; 
			/**LADESP*/
			String dir= "D:/Usuarios/renan.fucci/Documents/Semana 2/imagens/"; 
			gravarImagem(img, dir, nomeArquivo);
		
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}
	}
	

	
	/**
	 * <b> Metodo somaConstMatriz<\b>
	 * Metodo que faz a soma entre uma constante e uma matriz.
	 * @param k double Constante
	 * @param m Matriz(double) Matriz a ser calculada
	 * @return tmp matriz(double) Matriz Resultado
	 * */
	public double[][] somaConstMatriz(double k, double[][] m){
		double[][] tmp= new double[m.length][m[0].length];

		/** Teste de constante igual a 0, pois se a K=0, a matriz n√£o vai mudar.*/
		if(k==0){
			return m;
		}

		/** Multiplicando cada elemento da matriz pela constante.*/

		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[i].length; j++) {
				tmp[i][j]= (k+(m[i][j]));
			}
		}

		return tmp;
	}

	
	/** 
	 * <b> Metodo multiplicacaoConstMatriz<\b>
	 * Metodo que faz a multiplicacao entre uma constante e uma matriz. 
	 * @param k double Constante
	 * @param m Matriz(double) Matriz a ser calculada
	 * @return tmp matriz(double) Matriz Resultado
	 * */
	public double[][] multiplicacaoConstMatriz(double k, double[][] m){
		double[][] tmp= new double[m.length][m[0].length];

		/* Multiplicando cada elemento da matriz pela constante.*/
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[i].length; j++) {
				tmp[i][j]= (k*(m[i][j]));
			}
		}

		return tmp;
	}


	/**
	 * <b> Metodo multiplicacaoMxM<\b>
	 * Metodo que faz a multiplicacao entre duas matrizes.
	 * @param m matriz(double) Matriz um
	 * @param m1 matriz(double) Matriz dois
	 * @return tmp matriz(double) Matriz Resultado
	 *  */
	public double[][] multiplicacaoMxM(double[][] m, double[][] m1, double[][] tmp){
		/*
		 *  Testa a regra de Produto Mtricial, na qual diz que o numero de colunas da MatrizA = numero de linha  da MatrizB.
		 */
		if(m[0].length != m1.length ){
			System.err.println("Matrizes n√£o possuem a regra de Ai =Bj ");
			System.exit(1);
		}

		/* Instancia a matriz resultado(tmp) com o a Linha da M e a Coluna da M1, seguindo as defini√ß√µes matem√°ticas*/
		tmp= new double[m.length][m1[0].length];


		/* Multiplicando cada elemento da matriz pela constante.*/
		for (int i = 0; i < tmp.length; i++) {
			for (int j = 0; j < tmp[i].length; j++) {
				/* Multiplicacao de linha por coluna, pela representacao do tabuleiro*/
				for (int k = 0; k < (m1.length); k++) {
					tmp[i][j]+= m[i][k]* m1[k][j];

				}
			}
		}
		return tmp;
	}


	/** 
	 * <b> Metodo somaMxM <\b>
	 * Metodo que faz a soma entre duas matrizes.
	 * @param m matriz(double) Matriz um
	 * @param m1 matriz(double) Matriz dois
	 * @return tmp matriz(double) Matriz Resultado
	 *  */
	public double[][] somaMxM(double[][] m, double[][] m1, double[][] tmp){
		/* Multiplicando cada elemento da matriz pela constante.*/
		for (int i = 0; i < tmp.length; i++) {
			for (int j = 0; j < tmp[i].length; j++) {
				tmp[i][j]= m[i][j]+ m1[i][j];

			}
		}
		return tmp;
	}

	/**
	 * <b> Metodo transpostaMatriz<\b>
	 * Metodo para calcular a transposta de uma matriz.
	 * @param m matriz(double) Matriz a ser calculada
	 * @return tmp matriz(double) Matriz transposta
	 * */
	public double[][] transpostaMatriz(double[][] m){
		/*
		 * A partir da do parametro ij, intancia uma nova matriz ji, isto √©,
		 * o @param com tamanho M[i][j], se torna uma matriz @return com tamanho TMP[M[j]][M[i]]  
		 * */
		double[][] tmp= new double[m[0].length][m.length];

		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[i].length; j++) {
				tmp[j][i]=m[i][j];
			}
		}

		return tmp;
	}

	/**
	 * <b>Metodo transpostaVetor</b>
	 * Metodo para calcular a transposta de um vetor
	 * @param vet[](double) vetor a ser calculado
	 * @return m[][](double) resultado, matriz transposta
	 * */
	public double[][] transpostaVetor(double[] vet){
		/*
		 * Como o parametro e um vetor, intancia-se a matriz com o i do tamanho do vetor
		 * */
		double[][] tmp= new double[vet.length][1];

		for (int i = 0; i <tmp.length; i++) {
			tmp[i][0]=vet[i];
		}
		return tmp;
	}


	/**
	 * <b>Metodo funcaoU</b>
	 * Esse metodo vai calcular o f(U(i)), que e a somatoria entre o a matriz de padroes e a de pesos.
	 * A funcao de ativacao padrao deste metodo e a funcao DEGRAU.
	 * @param m[][](double) matriz de padroes
	 * @param w[][](double) matriz de pesos
	 * @param limiar(double) constante limiar, neste caso da funcao degrau.
	 * @return x(double) resultado da somatoria dos padroes vezes os pesos, com base no limiar.
	 * */
	public double funcaoU(double[][] m, double[][] w , double limiar){
		double x=0;
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[i].length; j++) {
				x += ((m[i][j])*(w[i][j]));
				//System.out.println("x:"+x);
			}
		}
		if(x>=limiar){
			return 1;
		}
		else{
			return 0;
		}
	}
	
	/**
	 * <b>Metodo funcaoUExec</b>
	 * Metodo para aplicar a funcaoU a uma matriz de pixels pretos e brancos
	 * Esse metodo vai calcular o f(U(i)), que e a somatoria entre o a matriz de padroes e a de pesos.
	 * Diferentemente do <b>Metodo funcaoU</b>, este metodo utiliza funcao de ativacao com resultado 0(preto) e 255(branco)
	 * @param m[][](double) matriz de padroes
	 * @param w[][](double) matriz de pesos
	 * @param limiar(double) constante limiar, neste caso da funcao degrau.
	 * @return x(double) resultado da somatoria dos padroes vezes os pesos, com base no limiar.
	 * */
	public static int funcaoUExec(double[][] m, double[][] w , double limiar){
		double x=0;
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[i].length; j++) {
				x += ((m[i][j])*(w[i][j]));
				//System.out.println("x:"+x);
			}
		}
		if(x>=limiar){
			return 255;
		}
		else{
			return 0;
		}
	}
	
	/**
	 * <b>Metodo funcaoErro</b>
	 * Esse metodo vai calcular a condicao de parada com conectivo E.
	 * A condicao de parada para o conectivo E(^) quando o F(u) eh igual a zero(0) para os dois padroes(classes).
	 * @param u(double) resultado da funcaoU(u(i));
	 * @param padr√£o(int) padrao ou classe.
	 * @param parada(int)  contador para a condicao de parada
	 * @return x(int) resultado do incremento da condi√ß√£o de parada.
	 * */
	public int funcaoErro(double u, int parada){
		if(u==0){

			parada+=1;
			return parada;
		}

		return 0;
	}



	/**
	 * <b>Metodo imprimirMatriz<\b>
	 * Metodo para imprimir todos elementos de uma matriz
	 * @param matriz(double) matriz a ser impresso
	 * */
	public void imprimirMatriz(double[][] m){
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[i].length; j++) {
				System.out.printf("%.4f ",m[i][j]);
			}
			System.out.println("");
		}

	}

	/**
	 * <b>Metodo setarMatriz</b>
	 * Esse metodo seta uma matriz, pois usando somente o sinal de igual(=), 
	 * eh faz-se uma referencia de memoria, porem nao seta uma nova matriz.
	 * @param tmp[][](double) matriz que sera setada;
	 * @param m[][](double) matriz que vai ser copiada.
	 * @return tmp[][](double) matriz setada, a copia da matriz(m) para a matriz(tmp).
	 * */
	public double[][] setarMatriz(double[][] tmp,double[][] m){

		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[i].length; j++) {
				tmp[i][j]=m[i][j];
			}
		}
		return tmp;
	}


	/**
	 * <b> Metodo gravarImagem<\b>
	 * Metodo que grava imagem na extensao .BMP em um diretorio local a partir de uma matriz, no entanto so utiliza um dos tres canais RGB, ou seja, grava imagem em tons de cinza.
	 * @param matriz[][](int) matriz a ser convertida em imagem
	 * @param dir(string) diretorio que sera salvo a imagem
	 * @param nomeArquivo(string) nome da imagem a ser salva.
	 */
	public void gravarImagem(int matriz[][], String dir, String nomeArqeExtens){
		//Tratamento de exce—É—?o 
		try{
			//Instancia uma imagem em um BufferImage
			BufferedImage imagem= new BufferedImage(matriz[0].length, matriz.length, 5);
			//Imprime que comelou a gravar a imagem
			System.out.println("In");

			//Estrutura de repeti—É—?o para percorrer a linha da matriz
			for (int i=0; i<matriz.length; i++){
				//Estrutura de repeti—É—?o para percorrer a coluna da matriz
				for(int j=0; j<matriz[i].length;j++){
					//Seta no buffer os tr–ñs canais RGB na sua respectiva posi—É—?o da itera—É—?o
					imagem.setRGB(j, i, new Color(matriz[i][j],matriz[i][j],matriz[i][j]).getRGB());

				}
			}
			//Escreve a imagem em um diret–∑rio
			ImageIO.write(imagem, "JPG", new File(dir+nomeArqeExtens));
			
			System.out.println("Out");
		}
		//Captura exce—É—àes
		catch(Exception ex){
			//Imprime a exce—É—?o

			System.err.println(ex.getLocalizedMessage());
			System.err.println(ex.getMessage());
		}
	}



	/**
	 * <b> Metodo gravarImagemRed<\b>
	 * Metodo que grava imagem na extensao .BMP em um diretorio local a partir de uma matriz, no entanto so utiliza grava em vermelho as partes de interesses da imagem.
	 * @param matriz[][](int) matriz a ser convertida em imagem
	 * @param dir(string) diretorio que sera salvo a imagem
	 * @param nomeArquivo(string) nome da imagem a ser salva.
	 */
	public void gravarImagemRed(int matriz[][], String dir, String nomeArquivo){
		//Tratamento de exce—É—?o 
		try{
			//Instancia uma imagem em um BufferImage
			BufferedImage imagem= new BufferedImage(matriz[0].length, matriz.length, 5);
			//Imprime que comelou a gravar a imagem
			System.out.println("In");

			//Estrutura de repeti—É—?o para percorrer a linha da matriz
			for (int i=0; i<matriz.length; i++){
				//Estrutura de repeti—É—?o para percorrer a coluna da matriz
				for(int j=0; j<matriz[i].length;j++){
					//Seta no buffer os tr–ñs canais RGB na sua respectiva posi—É—?o da itera—É—?o
					imagem.setRGB(j, i, new Color(matriz[i][j],0,0).getRGB());

				}
			}
			//Escreve a imagem em um diret–∑rio
			ImageIO.write(imagem, "JPG", new File(dir+nomeArquivo));
			System.out.println("Out");
			
		}
		//Captura exce—É—àes
		catch(Exception ex){
			//Imprime a exce—É—?o
			System.err.println(ex.getMessage());
		}
	}

	/**
	 * <b> Metodo gravarImagemGreen<\b>
	 * Metodo que grava imagem na extensao .BMP em um diretorio local a partir de uma matriz, no entanto so grava em verde as partes de interesses da imagem.
	 * @param matriz[][](int) matriz a ser convertida em imagem
	 * @param dir(string) diretorio que sera salvo a imagem
	 * @param nomeArquivo(string) nome da imagem a ser salva.
	 */
	public void gravarImagemGreen(int matriz[][], String dir, String nomeArquivo){
		//Tratamento de exce—É—?o 
		try{
			//Instancia uma imagem em um BufferImage
			BufferedImage imagem= new BufferedImage(matriz[0].length, matriz.length, 5);
			//Imprime que comelou a gravar a imagem
			System.out.println("In");

			//Estrutura de repeti—É—?o para percorrer a linha da matriz
			for (int i=0; i<matriz.length; i++){
				//Estrutura de repeti—É—?o para percorrer a coluna da matriz
				for(int j=0; j<matriz[i].length;j++){
					//Seta no buffer os tr–ñs canais RGB na sua respectiva posi—É—?o da itera—É—?o
					imagem.setRGB(j, i, new Color(0,matriz[i][j],0).getRGB());

				}
			}
			//Escreve a imagem em um diret–∑rio
			ImageIO.write(imagem, "JPG", new File(dir+nomeArquivo));
			System.out.println("Out");
			
		}
		//Captura exce—É—àes
		catch(Exception ex){
			//Imprime a exce—É—?o
			System.err.println(ex.getMessage());
		}
	}
	
	/**
	 * <b> Metodo gravarImagemBlue<\b>
	 * Metodo que grava imagem na extensao .BMP em um diretorio local a partir de uma matriz, no entanto so grava em azul as partes de interesses da imagem.
	 * @param matriz[][](int) matriz a ser convertida em imagem
	 * @param dir(string) diretorio que sera salvo a imagem
	 * @param nomeArquivo(string) nome da imagem a ser salva.
	 */
	public void gravarImagemBlue(int matriz[][], String dir, String nomeArquivo){
		//Tratamento de exce—É—?o 
		try{
			//Instancia uma imagem em um BufferImage
			BufferedImage imagem= new BufferedImage(matriz[0].length, matriz.length, 5);
			//Imprime que comelou a gravar a imagem
			System.out.println("In");

			//Estrutura de repeti—É—?o para percorrer a linha da matriz
			for (int i=0; i<matriz.length; i++){
				//Estrutura de repeti—É—?o para percorrer a coluna da matriz
				for(int j=0; j<matriz[i].length;j++){
					//Seta no buffer os tr–ñs canais RGB na sua respectiva posi—É—?o da itera—É—?o
					imagem.setRGB(j, i, new Color(0,0,matriz[i][j]).getRGB());

				}
			}
			//Escreve a imagem em um diret–∑rio
			ImageIO.write(imagem, "JPG", new File(dir+nomeArquivo));

			System.out.println("Out");
		}
		//Captura exce—É—àes
		catch(Exception ex){
			//Imprime a exce—É—?o
			System.err.println(ex.getMessage());
		}
	}


	
	/**
	 * <b> Metodo gravarImagemColorida<\b>
	 * Metodo que grava imagem na extensao .BMP em um diretorio local a partir de uma matriz e utiliza-se das tres bandas, Red, Green e Blue.
	 * @param matrizRed[][](int) matriz na banda Red a ser convertida
	 * @param matrizGreen[][](int) matriz na banda Green a ser convertida
	 * @param matrizBlue[][](int) matriz na banda Blue a ser convertida
	 * @param dir(string) diretorio que sera salva a imagem
	 * @param nomeArquivo(string) nome da imagem a ser salva.
	 */
	public void gravarImagemColorida(int matrizRed[][], int matrizGreen[][], int matrizBlue[][], String dir, String nomeEextens){
		//Tratamento de exceÁıes
		try{
			//Instancia uma imagem em um BufferImage
			BufferedImage imagem= new BufferedImage(matrizRed[0].length, matrizRed.length, 5);
			System.out.println("entra gravar imagem");

			//Estrutura de repetiÁ„o para percorrer a linha da matriz
			for (int i=0; i<matrizRed.length; i++){
				//Estrutura de repetiÁ„o para percorrer a coluna da matriz
				for(int j=0; j<matrizRed[i].length;j++){
					//Seta no buffer os trÍs canais RGB na sua respectiva posiÁ„o da iteraÁ„o
					imagem.setRGB(j, i, new Color(matrizRed[i][j],matrizGreen[i][j],matrizBlue[i][j]).getRGB());

				}
			}
			//Escreve a imagem em um diretÛrio
			ImageIO.write(imagem, "JPG", new File(dir+nomeEextens));
			System.out.println("sai gravar imagem");
		}
		//Captura exceÁıes
		catch(Exception ex){
			//Imprime a exceÁ„o
		System.err.println(ex.getMessage());
		ex.printStackTrace();
		}
	}

	/**
	 * <b> Metodo RGBparaCinza<\b>
	 * Metodo que converte uma imagem colorida (RGB) para uma imagem em tons de Cinza (grayScale).
	 * @param matrizRed[][](int) matriz na banda Red a ser convertida
	 * @param matrizGreen[][](int) matriz na banda Green a ser convertida
	 * @param matrizBlue[][](int) matriz na banda Blue a ser convertida
	 * @return matrizRes[][](int) resultado, matriz em tons de cinza
	 *  */
	public int[][] RGBparaCinza(int matrizRed[][], int matrizGreen[][], int matrizBlue[][]){
		//Instancia matriz resultado
		int[][] matrizRes=new int[matrizRed.length][matrizRed[0].length];
		//Estrutura de repeti—É—?o para percorrer a linha da matriz
		for (int i = 0; i < matrizRes.length; i++) {
			//Estrutura de repeti—É—?o para percorrer a coluna da matriz
			for (int j = 0; j < matrizRes[i].length; j++) {
				//Matriz resultado recebe c—Älculo especifico de RBG para Tons de Cinza
				matrizRes[i][j]=((matrizRed[i][j]*(21/100))+(matrizGreen[i][j]*(72/100)+(matrizBlue[i][j]*(7/100))));
			}
		}

		//Retorna a matriz resultado
		return matrizRes;
	}



	/**
	 * <b> Metodo leImagem<\b>
	 * Metodo le uma imagem de um diretorio e converte em uma matriz de uma sÛ banda, isto È, deve-se escolher entre os tres canais: "Red" , "Green" ou "Blue".
	 * @param diretorio(String) diretorio onde se encontra a imagem a ser convertida
	 * @param canalRgb(String) um dos canais RGB a ser escolhido.
	 */
	public int[][] leImagem(String diretorio, String canalRgb){
		//Seta nulo para a matriz 
		int array[][]=null;
		//Declara uma variavel do tipo BufferImage
		BufferedImage objeto;
		//Tratamento de exce—É—àes
		try {
			//Instanciando o bufferImage
			objeto = ImageIO.read(new File(diretorio));
			//System.out.println("Tipo objeto: "+objeto.getType());
			//Instanciando a matriz
			array= new int[objeto.getHeight()][objeto.getWidth()];
			//Estrutura de repeti—É—?o para percorrer a linha da matriz
			for (int i=0; i<objeto.getHeight(); i++){
				//Estrutura de repeti—É—?o para percorrer a coluna da matriz
				for(int j=0; j<objeto.getWidth();j++){
					//Testa qual –∂ o canal passado por par–†metro
					if(canalRgb=="Red")
						array[i][j]=new Color(objeto.getRGB(j, i)).getRed();
					else if(canalRgb=="Green")
						array[i][j]=new Color(objeto.getRGB(j, i)).getGreen();
					else if(canalRgb=="Blue")
						array[i][j]=new Color(objeto.getRGB(j, i)).getBlue();

					//System.out.println(array[i][j]);
				}
			}
			//retorna a matriz
			return array;


		} //Captura a exce—É—?o
		catch (IOException e) {
			//imprime o rastro de pilha
			e.printStackTrace();
			//retorna a matriz
			return array;

		}



	}
	
	/**
	 * <b> Metodo diferenca<\b>
	 * Metodo que faz a diferenca entre duas imagens de uma so banda (CanalRBG), e retorna uma matriz correspondente a essa diferenca.
	 * @param matriz1[][](int) matriz a ser comparada
	 * @param matriz2[][](int) matriz a ser comparada
	 * @return matrizResult[][](int) resultado, matriz diferenca
	 */
	public int[][] diferenca(int[][] matriz1, int[][] matriz2){
		//instanciando a matriz resultado
		int[][] matrizResult= new int[matriz1.length][matriz1[0].length];
		//Estrutura repetiÁ„o para percorrer a linha da matriz
		for (int i = 0; i < matrizResult.length; i++) {
			//Estrutura repetiÁ„o para percorrer a coluna da matriz
			for (int j = 0; j < matrizResult[i].length; j++) {
				//Matriz resultado recebe o diferenÁa absoluta entre duas matrizes
				matrizResult[i][j]=Math.abs(matriz1[i][j]-matriz2[i][j]);
			}
		}
		//retorna a matriz resultado
		return matrizResult;
	}

	/**
	 * <b> Metodo contarCorCinzas<\b>
	 * Metodo que faz a contagen da quantidade de pixels em determinada intensidade (0-255) de uma banda da imagem.
	 * OBS.: So vao ser impressos as intensidades que possuem mais que 0 pixels.
	 * @param matriz[][](int) matriz de uma banda so que vai ser usada na contagem
	 */
	public void contarCorCinzas(int matriz[][]){

		int[] cores=new int[256];
		int x=0;

		for (int i=0; i<matriz.length; i++){
			for(int j=0; j<matriz[i].length;j++){
				x=matriz[i][j];
				cores[x]+=1;


			}
		}

		for (int i = 0; i < cores.length; i++) {
			if(cores[i]!=0)
			System.out.println(+cores[i]+"pixels de cor"+i);
		}		
	}


	public int getLBPVizinhos(int[][] img) {
		// TODO Auto-generated constructor stub
		int[][] janela= new int[3][3];
		String binary ="";
		int cent = (int) Math.floor(janela.length/2);
		int centro =  img[cent][cent];
		
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < janela.length; j++) {
				if(centro>img[i][j])
					binary+=0;
				else
					binary+=1;
			}
		}
		
		for (int i = 1; i <janela.length ; i++) {
			for (int j = janela.length-1; j <janela.length ; j++) {
				if(centro>img[i][j])
					binary+=0;
				else
					binary+=1;
			}
		}
		
		for (int i =janela.length-1 ; i < janela.length; i++) {
			for (int j = janela.length-2; j >= 0; j--) {
				if(centro>img[i][j])
					binary+=0;
				else
					binary+=1;
			}
		}
		for (int i = janela.length-2 ; i > 0; i--) {
			for (int j = 0; j >= 0; j--) {
				if(centro>img[i][j])
					binary+=0;
				else
					binary+=1;
			}
		}
		return Integer.parseInt(binary, 2);
	}
}
