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
 * O objetivo dessa classe é montar uma interface gráfica para a aplicação de captura de imagens de um sensor de movimento.
 * Irá possuir 2 variáveis de entrada, uma tela que mostrará o tempo real da webcam e dois botões, um pra iniciar o sensor de movimento 
 * 	e outro para encerrar o sensor. 
 * Esta classe herda um JFrame para montar a interface gráfica sem precisar instanciar o JFrame.
 * Implementa o ActionListener para os eventos dos botões.
 */
public class ProjetoInterface extends JFrame implements  ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * Variáveis globais GUI
	 */
	JButton iniciar;
	JButton encerrar;
	static JLabel mensagem= new JLabel();
	static JComboBox<String> cBox = new JComboBox<String>();
	JTextField distanciaSuperficie;


	//Variáveis globais de controle
	static JTextField tamanhoMinObj;
	static int escalaDimen=1; 
	static String[] opCBox ={"","Sensor de Movimento", "Sensor de Movimento + Presença"};
	static Object opBox;


	// Variáveis globais para datetime
	static Calendar cal = Calendar.getInstance();
	static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	static String folder=dateFormat.format(cal.getTime()); 
	static File pasta =  new File("C:/ImagensPDI/Imagens/"+folder);


	static int[][] cabeçalho = MétodosPDI.leImagem("C:/ImagensPDI/Imagens/Cabecalho.bmp", "Red");
	static int[][] rodape = MétodosPDI.leImagem("C:/ImagensPDI/Imagens/rodapé.bmp", "Red");

	/*
	 * Instanciando a Webcam
	 */
	Webcam camera= Webcam.getWebcams().get(0);


	/*
	 * Método principal que vai instanciar o objeto da classe ProjetoInterface
	 * */
	public static void main(String[] args) throws IOException {

		pasta.mkdir();
		new ProjetoInterface();
		System.in.read();// Manter o programa em leitura
	}  

	/*
	 * Método construtor do aplicativo, bem como sua interface gráfica.
	 */

	public ProjetoInterface(){
		// Tratando exceções
		try {
			//Setar a resolução da web que será usada
			camera.setViewSize(WebcamResolution.VGA.getSize());
			//Iniciar/Abrir a webcam
			camera.open();

			//instanciar os botões GUI
			iniciar= new JButton("Iniciar");
			encerrar= new JButton("Encerrar");

			//Instanciando as opções do combo box GUI
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

			//Instanciando outra camada do JPanel, responsável pelas etiquetas e caixa de textos
			JPanel cont= new JPanel();
			cont.add(new JLabel("Opção de Sensor"));
			cont.add(cBox);
			cont.add(new JLabel("Distância em cm"));
			cont.add(distanciaSuperficie);
			cont.add(new JLabel("Tamanho Mínimo em cm²"));
			cont.add(tamanhoMinObj);
			cont.setLayout(new GridLayout(0,1));

			//Instanciando outra camada do JPanel, responsável pelos botões
			JPanel botoes = new JPanel();
			botoes.add(iniciar);
			botoes.add(encerrar);
			botoes.setLayout(new GridLayout(0,2));

			//Instanciando outra camada do Jpanel, responsável pela mensagem de "Sensor Ativo" ou "Sensor Inativo"
			JPanel msg =  new JPanel();
			msg.add(mensagem);

			//Instanciando outra camada do webcamPanel, essa responsável por apresentar no aplicativo o video da webcam em tempo real 
			WebcamPanel webcm = new WebcamPanel(camera);

			//Adicionando todas as camadas acimas no painel principal C 
			c.add(cont);
			c.add(Box.createHorizontalStrut(700)); //Espaços
			c.add(webcm);
			c.add(Box.createHorizontalStrut(700)); //Espaços
			c.add(msg);
			c.add(Box.createHorizontalStrut(700)); //Espaços
			c.add(botoes);
			c.setLayout(new FlowLayout());

			//Adicionando evento para ação do botões
			iniciar.addActionListener(this);
			encerrar.addActionListener(this);
			cBox.addActionListener(this);

			/*
			 * Deixando a visível o Frame, setando seu tamanho, localizando-o no centro da tela e 
			 * setando operação para finalizar a execução do programa junto à interface grafica
			 */
			setVisible(true);
			setSize(700,800 );
			setLocationRelativeTo(null);
			setDefaultCloseOperation(EXIT_ON_CLOSE);

			//Capturando exceções
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@Override
	/*
	 * Método que irá executar ação, quando os botoes forem pressionados,isto é, quando ocorrer um evento.
	 */
	public void actionPerformed(ActionEvent ação) {
		//Se o evento foi do botão iniciar
		if(ação.getSource()==iniciar){
			//Testa se nenhuma das caixas de texto da interface gráfica estão em branco
			if(distanciaSuperficie.getText().contentEquals("") || tamanhoMinObj.getText().contentEquals("") || cBox.getSelectedItem().equals("")== true ){
				//Se estiveram vazias, mostra mensagem de erro
				JOptionPane.showMessageDialog(null," \tCampos Vazios!\n\tInserir por favor.", getWarningString(),JOptionPane.ERROR_MESSAGE,null);
			}
			//Se não estiveram vazia, inicia o processo para o sensor de movimento
			else{

				int c=Integer.parseInt(distanciaSuperficie.getText()); //Pegando o valor inteiro da caixa de texto
				//instanciando a escala de cm por pixels, baseando-se na distancia
				escalaDimen=(753/(10*(c/27)));
				//setar variavel global da opção do comboBox GUI
				opBox = cBox.getSelectedItem();
				//Mostrando mensagem de sensor ativo
				/*mensagem.setText("Sensor Ativo");
				//setando a cor do mensagem para verde
				mensagem.setForeground(Color.GREEN);

				//setando a cor do mensagem para verde, e a de encerrar para o padrão (branco)
				iniciar.setBackground(Color.GREEN);
				encerrar.setBackground(Color.white);
				 */
				//long tempoInicio = System.currentTimeMillis();
				//int cont=0;
				//mantem em loop o método de sensor de movimento
				while(true){
					//Trata exceção
					try {

						//chama o método de sensor de movimento
						//cont+=1;
						sensorMovimento(camera);
						/*if(cont%5==0){
							System.out.println(System.currentTimeMillis() - tempoInicio);
						}*/
					} 
					//captura exceção
					catch (IOException e) {
						e.printStackTrace();
					}

				}
			}

		}
		//Se o evento foi do botão iniciar
		else if(ação.getSource()==encerrar){
			//seta as variavel de controle como 0
			System.exit(ABORT);
			x=0;
			//seta a mensagem como sensor inativo e a cor de vermelho
			mensagem.setText("Sensor Inativo");
			mensagem.setForeground(Color.RED);
			//seta a cor do botão iniciar para o padrão(branco) e o botão encerrar para vermelho
			iniciar.setBackground(Color.white);
			encerrar.setBackground(Color.red);
		}
	}

	//Variáveis globais
	static int[][] imgInicialR; 
	static int[][] imgInicialG; 
	static int[][] imgInicialB; 
	static BufferedImage image;
	static int x=0;
	static int [] limiar={21};

	/*
	 * Método que cria o sensor de movimento, tem como parâmetro a Webcam
	 */
	public static void sensorMovimento(Webcam camera) throws IOException {
		//Instancia um objeto da classe que contém os métodos de manipulação de imagens
		MétodosPDI metodos1 =new MétodosPDI();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SS");


		//Declarando três matrizes para cada canal RGB


		int[][] imgOriR;
		int[][] imgOriG;
		int[][] imgOriB;

		//Declarando matriz que fará a diferença da matriz de controle e uma variável do tipo BufferImage
		int[][] imgDiferen;
		BufferedImage image2;
		
		//Testa se é a primeira iteração, para assim gravar a imagem de controle
		if(x==0){
			//Instancia o BufferImage da imagem de controle pela captura da webcam
			image=camera.getImage();	
			//Transforma o bufferImage para uma matriz para cada canal RGB
			imgInicialR= metodos1.imagemParaMatriz(image, "Red");
			imgInicialG= metodos1.imagemParaMatriz(image, "Green");
			imgInicialB=metodos1.imagemParaMatriz(image, "Blue");

			//grava a Imagem de controle
			metodos1.gravarImagemColorida(imgInicialR, imgInicialG, imgInicialB, "imgcontrole", folder);
			//Incrementa a variável de controle
			x+=1;
		}
		//Se não for a primeira iteação
		else {
			//testa pra saber qual se a opção de sensor do comboBox GUI é a primeira, isto é, "Sensor de Movimento"
			if(opBox.equals(opCBox[1])){
				//System.out.println(opCBox[1]);
				//Incrementa a variável de controle
				x+=1;
				//Instancia o BufferImage da imagem de atual pela captura da webcam
				image2= camera.getImage();

				//Transforma o bufferImage para uma matriz para cada canal RGB
				imgOriR=metodos1.imagemParaMatriz(image2, "Red");
				imgOriG=metodos1.imagemParaMatriz(image2, "Green");
				imgOriB=metodos1.imagemParaMatriz(image2, "Blue");

				//Faz a diferença entre a imagem de controle e a imagem de atual
				imgDiferen=metodos1.diferença(imgInicialG,imgOriG);
				//Segmenta essa diferença
				imgDiferen=metodos1.segmentaçãoPorLimiarização(imgDiferen, limiar);
				//Conta a quantidade de pixels do objeto da diferença
				int qntdcores[]=metodos1.contarCorCinzasSemPrint(imgDiferen);
				/*
			System.out.println("cor"+qntdcores[255]);
			System.out.println("escala"+escalaDimen);
				 */

				//testa se o objeto de diferença é maior ou igual ao tamanho mínimo definido
				if (qntdcores[255]>=escalaDimen*Integer.parseInt(tamanhoMinObj.getText()) ){
					//Por consequência da escolha do sensor movimento, a caracteristica desta opção substitui a matriz de controle
					image=image2;	
					//Transforma o bufferImage para uma matriz para cada canal RGB
					imgInicialR= metodos1.imagemParaMatriz(image, "Red");
					imgInicialG= metodos1.imagemParaMatriz(image, "Green");
					imgInicialB=metodos1.imagemParaMatriz(image, "Blue");

					//grava a Imagem de controle
					metodos1.gravarImagemColorida(imgInicialR, imgInicialG, imgInicialB, "imgcontrole", folder);
					
					
					//adiciona o cabeçalho na imagem
					for (int i = 0; i < 80; i++) {
						for (int j = 0; j < imgOriR[i].length; j++) {
							imgOriR[i][j]=cabeçalho[i][j];
							imgOriG[i][j]=cabeçalho[i][j];
							imgOriB[i][j]=cabeçalho[i][j];
						}
					}
					//adiciona o rodapé à imagem
					for (int i = 80; i > 0 ; i--) {
						for (int j = 0; j < imgOriR[i].length; j++) {
							imgOriR[640 -i][j]=rodape[80-i][j];
							imgOriG[640-i][j]=rodape[80-i][j];
							imgOriB[640-i][j]=rodape[80-i][j];
						}
					}
					Calendar cal1 = Calendar.getInstance();
					String nomeArq=dateFormat.format(cal1.getTime());
					//grava a imagem final com rodapé
					metodos1.gravarImagemColorida(imgOriR, imgOriG, imgOriB, nomeArq, folder);

				}
				//se o tamanho do objeto não for maior que o mininmo
				else{
					//Seta NULO PARA A INTANCIA DA IMAGEM 

					image2=null;
				}
			}
			//testa pra saber qual se a opção de sensor do comboBox GUI é a segunda, isto é, "Sensor de Movimento + Presença"
			else if(opBox.equals(opCBox[2])){
					//System.out.println(opCBox[2]);
					//Incrementa a variável de controle
					x+=1;
					//Instancia o BufferImage da imagem de atual pela captura da webcam
					image2= camera.getImage();

					//Transforma o bufferImage para uma matriz para cada canal RGB
					imgOriR=metodos1.imagemParaMatriz(image2, "Red");
					imgOriG=metodos1.imagemParaMatriz(image2, "Green");
					imgOriB=metodos1.imagemParaMatriz(image2, "Blue");

					//Faz a diferença entre a imagem de controle e a imagem de atual
					imgDiferen=metodos1.diferença(imgInicialG,imgOriG);
					//Segmenta essa diferença
					imgDiferen=metodos1.segmentaçãoPorLimiarização(imgDiferen, limiar);
					//Conta a quantidade de pixels do objeto da diferença
					int qntdcores[]=metodos1.contarCorCinzasSemPrint(imgDiferen);
					/*
				System.out.println("cor"+qntdcores[255]);
				System.out.println("escala"+escalaDimen);
					 */

					//testa se o objeto de diferença é maior ou igual ao tamanho mínimo definido
					if (qntdcores[255]>=escalaDimen*Integer.parseInt(tamanhoMinObj.getText()) ){
						//adiciona o cabeçalho na imagem
						for (int i = 0; i < 80; i++) {
							for (int j = 0; j < imgOriR[i].length; j++) {
								imgOriR[i][j]=cabeçalho[i][j];
								imgOriG[i][j]=cabeçalho[i][j];
								imgOriB[i][j]=cabeçalho[i][j];
							}
						}
						//adiciona o rodapé à imagem
						for (int i = 80; i > 0 ; i--) {
							for (int j = 0; j < imgOriR[i].length; j++) {
								imgOriR[640 -i][j]=rodape[80-i][j];
								imgOriG[640-i][j]=rodape[80-i][j];
								imgOriB[640-i][j]=rodape[80-i][j];
							}
						}
						Calendar cal1 = Calendar.getInstance();
						String nomeArq=dateFormat.format(cal1.getTime());
						//grava a imagem final com rodapé
						metodos1.gravarImagemColorida(imgOriR, imgOriG, imgOriB, nomeArq, folder);

					}
					//se o tamanho do objeto não for maior que o mininmo
					else{
						//Seta NULO PARA A INTANCIA DA IMAGEM 

						image2=null;
					}
				}
			}

		//System.out.println("Sensor Detectado");

	}



}
