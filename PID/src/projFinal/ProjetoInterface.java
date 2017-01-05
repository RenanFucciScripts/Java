package projFinal;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.*;

import com.github.sarxos.webcam.*;


/*
 * O objetivo dessa classe � montar uma interface gr�fica para a aplica��o de captura de imagens de um sensor de movimento.
 * Ir� possuir 2 vari�veis de entrada, uma tela que mostrar� o tempo real da webcam e dois bot�es, um pra iniciar o sensor de movimento 
 * 	e outro para encerrar o sensor. 
 * Esta classe herda um JFrame para montar a interface gr�fica sem precisar instanciar o JFrame.
 * Implementa o ActionListener para os eventos dos bot�es.
 */
public class ProjetoInterface extends JFrame implements  ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * Vari�veis globais GUI
	 */
	JButton iniciar;
	JButton encerrar;
	static JLabel mensagem= new JLabel();
	static JComboBox<String> cBox = new JComboBox<String>();
	JTextField distanciaSuperficie;


	//Vari�veis globais de controle
	static JTextField tamanhoMinObj;
	static int escalaDimen=1; 
	static String[] opCBox ={"","Sensor de Movimento", "Sensor de Movimento + Presen�a"};
	static Object opBox;


	// Vari�veis globais para datetime
	static Calendar cal = Calendar.getInstance();
	static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	static String folder=dateFormat.format(cal.getTime()); 
	static File pasta =  new File("C:/ImagensPDI/Imagens/"+folder);


	static int[][] cabe�alho = M�todosPDI.leImagem("C:/ImagensPDI/Imagens/Cabecalho.bmp", "Red");
	static int[][] rodape = M�todosPDI.leImagem("C:/ImagensPDI/Imagens/rodap�.bmp", "Red");

	/*
	 * Instanciando a Webcam
	 */
	Webcam camera= Webcam.getWebcams().get(0);


	/*
	 * M�todo principal que vai instanciar o objeto da classe ProjetoInterface
	 * */
	public static void main(String[] args) throws IOException {

		pasta.mkdir();
		new ProjetoInterface();
		System.in.read();// Manter o programa em leitura
	}  

	/*
	 * M�todo construtor do aplicativo, bem como sua interface gr�fica.
	 */

	public ProjetoInterface(){
		// Tratando exce��es
		try {
			//Setar a resolu��o da web que ser� usada
			camera.setViewSize(WebcamResolution.VGA.getSize());
			//Iniciar/Abrir a webcam
			camera.open();

			//instanciar os bot�es GUI
			iniciar= new JButton("Iniciar");
			encerrar= new JButton("Encerrar");

			//Instanciando as op��es do combo box GUI
			for (int i = 0; i < opCBox.length; i++) {
				cBox.addItem(opCBox[i]);
			}
			// Instanciando as caixas de texto GUI
			distanciaSuperficie= new JTextField();
			tamanhoMinObj = new JTextField();

			//setar a cor dos botoes
			iniciar.setBackground(Color.white);
			encerrar.setBackground(Color.white);

			// Instanciando a camada principal do JPanel
			Container c= getContentPane();

			//Instanciando outra camada do JPanel, respons�vel pelas etiquetas e caixa de textos
			JPanel cont= new JPanel();
			cont.add(new JLabel("Op��o de Sensor"));
			cont.add(cBox);
			cont.add(new JLabel("Dist�ncia em cm"));
			cont.add(distanciaSuperficie);
			cont.add(new JLabel("Tamanho M�nimo em cm�"));
			cont.add(tamanhoMinObj);
			cont.setLayout(new GridLayout(0,1));

			//Instanciando outra camada do JPanel, respons�vel pelos bot�es
			JPanel botoes = new JPanel();
			botoes.add(iniciar);
			botoes.add(encerrar);
			botoes.setLayout(new GridLayout(0,2));

			//Instanciando outra camada do Jpanel, respons�vel pela mensagem de "Sensor Ativo" ou "Sensor Inativo"
			JPanel msg =  new JPanel();
			msg.add(mensagem);

			//Instanciando outra camada do webcamPanel, essa respons�vel por apresentar no aplicativo o video da webcam em tempo real 
			WebcamPanel webcm = new WebcamPanel(camera);

			//Adicionando todas as camadas acimas no painel principal C 
			c.add(cont);
			c.add(Box.createHorizontalStrut(700)); //Espa�os
			c.add(webcm);
			c.add(Box.createHorizontalStrut(700)); //Espa�os
			c.add(msg);
			c.add(Box.createHorizontalStrut(700)); //Espa�os
			c.add(botoes);
			c.setLayout(new FlowLayout());

			//Adicionando evento para a��o do bot�es
			iniciar.addActionListener(this);
			encerrar.addActionListener(this);
			cBox.addActionListener(this);

			/*
			 * Deixando a vis�vel o Frame, setando seu tamanho, localizando-o no centro da tela e 
			 * setando opera��o para finalizar a execu��o do programa junto � interface grafica
			 */
			setVisible(true);
			setSize(700,800 );
			setLocationRelativeTo(null);
			setDefaultCloseOperation(EXIT_ON_CLOSE);

			//Capturando exce��es
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@Override
	/*
	 * M�todo que ir� executar a��o, quando os botoes forem pressionados,isto �, quando ocorrer um evento.
	 */
	public void actionPerformed(ActionEvent a��o) {
		//Se o evento foi do bot�o iniciar
		if(a��o.getSource()==iniciar){
			//Testa se nenhuma das caixas de texto da interface gr�fica est�o em branco
			if(distanciaSuperficie.getText().contentEquals("") || tamanhoMinObj.getText().contentEquals("") || cBox.getSelectedItem().equals("")== true ){
				//Se estiveram vazias, mostra mensagem de erro
				JOptionPane.showMessageDialog(null," \tCampos Vazios!\n\tInserir por favor.", getWarningString(),JOptionPane.ERROR_MESSAGE,null);
			}
			//Se n�o estiveram vazia, inicia o processo para o sensor de movimento
			else{

				int c=Integer.parseInt(distanciaSuperficie.getText()); //Pegando o valor inteiro da caixa de texto
				//instanciando a escala de cm por pixels, baseando-se na distancia
				escalaDimen=(753/(10*(c/27)));
				//setar variavel global da op��o do comboBox GUI
				opBox = cBox.getSelectedItem();
				//Mostrando mensagem de sensor ativo
				/*mensagem.setText("Sensor Ativo");
				//setando a cor do mensagem para verde
				mensagem.setForeground(Color.GREEN);

				//setando a cor do mensagem para verde, e a de encerrar para o padr�o (branco)
				iniciar.setBackground(Color.GREEN);
				encerrar.setBackground(Color.white);
				 */
				//long tempoInicio = System.currentTimeMillis();
				//int cont=0;
				//mantem em loop o m�todo de sensor de movimento
				while(true){
					//Trata exce��o
					try {

						//chama o m�todo de sensor de movimento
						//cont+=1;
						sensorMovimento(camera);
						/*if(cont%5==0){
							System.out.println(System.currentTimeMillis() - tempoInicio);
						}*/
					} 
					//captura exce��o
					catch (IOException e) {
						e.printStackTrace();
					}

				}
			}

		}
		//Se o evento foi do bot�o iniciar
		else if(a��o.getSource()==encerrar){
			//seta as variavel de controle como 0
			System.exit(ABORT);
			x=0;
			//seta a mensagem como sensor inativo e a cor de vermelho
			mensagem.setText("Sensor Inativo");
			mensagem.setForeground(Color.RED);
			//seta a cor do bot�o iniciar para o padr�o(branco) e o bot�o encerrar para vermelho
			iniciar.setBackground(Color.white);
			encerrar.setBackground(Color.red);
		}
	}

	//Vari�veis globais
	static int[][] imgInicialR; 
	static int[][] imgInicialG; 
	static int[][] imgInicialB; 
	static BufferedImage image;
	static int x=0;
	static int [] limiar={21};

	/*
	 * M�todo que cria o sensor de movimento, tem como par�metro a Webcam
	 */
	public static void sensorMovimento(Webcam camera) throws IOException {
		//Instancia um objeto da classe que cont�m os m�todos de manipula��o de imagens
		M�todosPDI metodos1 =new M�todosPDI();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SS");


		//Declarando tr�s matrizes para cada canal RGB


		int[][] imgOriR;
		int[][] imgOriG;
		int[][] imgOriB;

		//Declarando matriz que far� a diferen�a da matriz de controle e uma vari�vel do tipo BufferImage
		int[][] imgDiferen;
		BufferedImage image2;
		
		//Testa se � a primeira itera��o, para assim gravar a imagem de controle
		if(x==0){
			//Instancia o BufferImage da imagem de controle pela captura da webcam
			image=camera.getImage();	
			//Transforma o bufferImage para uma matriz para cada canal RGB
			imgInicialR= metodos1.imagemParaMatriz(image, "Red");
			imgInicialG= metodos1.imagemParaMatriz(image, "Green");
			imgInicialB=metodos1.imagemParaMatriz(image, "Blue");

			//grava a Imagem de controle
			metodos1.gravarImagemColorida(imgInicialR, imgInicialG, imgInicialB, "imgcontrole", folder);
			//Incrementa a vari�vel de controle
			x+=1;
		}
		//Se n�o for a primeira itea��o
		else {
			//testa pra saber qual se a op��o de sensor do comboBox GUI � a primeira, isto �, "Sensor de Movimento"
			if(opBox.equals(opCBox[1])){
				//System.out.println(opCBox[1]);
				//Incrementa a vari�vel de controle
				x+=1;
				//Instancia o BufferImage da imagem de atual pela captura da webcam
				image2= camera.getImage();

				//Transforma o bufferImage para uma matriz para cada canal RGB
				imgOriR=metodos1.imagemParaMatriz(image2, "Red");
				imgOriG=metodos1.imagemParaMatriz(image2, "Green");
				imgOriB=metodos1.imagemParaMatriz(image2, "Blue");

				//Faz a diferen�a entre a imagem de controle e a imagem de atual
				imgDiferen=metodos1.diferen�a(imgInicialG,imgOriG);
				//Segmenta essa diferen�a
				imgDiferen=metodos1.segmenta��oPorLimiariza��o(imgDiferen, limiar);
				//Conta a quantidade de pixels do objeto da diferen�a
				int qntdcores[]=metodos1.contarCorCinzasSemPrint(imgDiferen);
				/*
			System.out.println("cor"+qntdcores[255]);
			System.out.println("escala"+escalaDimen);
				 */

				//testa se o objeto de diferen�a � maior ou igual ao tamanho m�nimo definido
				if (qntdcores[255]>=escalaDimen*Integer.parseInt(tamanhoMinObj.getText()) ){
					//Por consequ�ncia da escolha do sensor movimento, a caracteristica desta op��o substitui a matriz de controle
					image=image2;	
					//Transforma o bufferImage para uma matriz para cada canal RGB
					imgInicialR= metodos1.imagemParaMatriz(image, "Red");
					imgInicialG= metodos1.imagemParaMatriz(image, "Green");
					imgInicialB=metodos1.imagemParaMatriz(image, "Blue");

					//grava a Imagem de controle
					metodos1.gravarImagemColorida(imgInicialR, imgInicialG, imgInicialB, "imgcontrole", folder);
					
					
					//adiciona o cabe�alho na imagem
					for (int i = 0; i < 80; i++) {
						for (int j = 0; j < imgOriR[i].length; j++) {
							imgOriR[i][j]=cabe�alho[i][j];
							imgOriG[i][j]=cabe�alho[i][j];
							imgOriB[i][j]=cabe�alho[i][j];
						}
					}
					//adiciona o rodap� � imagem
					for (int i = 80; i > 0 ; i--) {
						for (int j = 0; j < imgOriR[i].length; j++) {
							imgOriR[640 -i][j]=rodape[80-i][j];
							imgOriG[640-i][j]=rodape[80-i][j];
							imgOriB[640-i][j]=rodape[80-i][j];
						}
					}
					Calendar cal1 = Calendar.getInstance();
					String nomeArq=dateFormat.format(cal1.getTime());
					//grava a imagem final com rodap�
					metodos1.gravarImagemColorida(imgOriR, imgOriG, imgOriB, nomeArq, folder);

				}
				//se o tamanho do objeto n�o for maior que o mininmo
				else{
					//Seta NULO PARA A INTANCIA DA IMAGEM 

					image2=null;
				}
			}
			//testa pra saber qual se a op��o de sensor do comboBox GUI � a segunda, isto �, "Sensor de Movimento + Presen�a"
			else if(opBox.equals(opCBox[2])){
					//System.out.println(opCBox[2]);
					//Incrementa a vari�vel de controle
					x+=1;
					//Instancia o BufferImage da imagem de atual pela captura da webcam
					image2= camera.getImage();

					//Transforma o bufferImage para uma matriz para cada canal RGB
					imgOriR=metodos1.imagemParaMatriz(image2, "Red");
					imgOriG=metodos1.imagemParaMatriz(image2, "Green");
					imgOriB=metodos1.imagemParaMatriz(image2, "Blue");

					//Faz a diferen�a entre a imagem de controle e a imagem de atual
					imgDiferen=metodos1.diferen�a(imgInicialG,imgOriG);
					//Segmenta essa diferen�a
					imgDiferen=metodos1.segmenta��oPorLimiariza��o(imgDiferen, limiar);
					//Conta a quantidade de pixels do objeto da diferen�a
					int qntdcores[]=metodos1.contarCorCinzasSemPrint(imgDiferen);
					/*
				System.out.println("cor"+qntdcores[255]);
				System.out.println("escala"+escalaDimen);
					 */

					//testa se o objeto de diferen�a � maior ou igual ao tamanho m�nimo definido
					if (qntdcores[255]>=escalaDimen*Integer.parseInt(tamanhoMinObj.getText()) ){
						//adiciona o cabe�alho na imagem
						for (int i = 0; i < 80; i++) {
							for (int j = 0; j < imgOriR[i].length; j++) {
								imgOriR[i][j]=cabe�alho[i][j];
								imgOriG[i][j]=cabe�alho[i][j];
								imgOriB[i][j]=cabe�alho[i][j];
							}
						}
						//adiciona o rodap� � imagem
						for (int i = 80; i > 0 ; i--) {
							for (int j = 0; j < imgOriR[i].length; j++) {
								imgOriR[640 -i][j]=rodape[80-i][j];
								imgOriG[640-i][j]=rodape[80-i][j];
								imgOriB[640-i][j]=rodape[80-i][j];
							}
						}
						Calendar cal1 = Calendar.getInstance();
						String nomeArq=dateFormat.format(cal1.getTime());
						//grava a imagem final com rodap�
						metodos1.gravarImagemColorida(imgOriR, imgOriG, imgOriB, nomeArq, folder);

					}
					//se o tamanho do objeto n�o for maior que o mininmo
					else{
						//Seta NULO PARA A INTANCIA DA IMAGEM 

						image2=null;
					}
				}
			}

		//System.out.println("Sensor Detectado");

	}



}
