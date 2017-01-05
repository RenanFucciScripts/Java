package projCameraLID;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;

public class InterfaceCameraLID extends JFrame implements  ActionListener{

	private static final long serialVersionUID = 1L;
	/*
	 * Vari�veis globais GUI
	 */
	String usuario;
	String senha;
	JButton iniciar;
	JButton encerrar;
	JTextField fieldUsu�rio;
	JTextField fieldSenha;
	JLabel mensagem1= new JLabel();
	JLabel mensagemCam1= new JLabel();
	JLabel mensagemCam2= new JLabel();
	Thread concorrencia = new Thread(new Concorrencia());
	/*
	 * M�todo principal que vai instanciar o objeto da classe ProjetoInterface
	 * */
	public static void main(String[] args) throws IOException {
		new InterfaceCameraLID();
		//System.in.read();// Manter o programa em leitura
	}  

	/*
	 * M�todo construtor do aplicativo, bem como sua interface gr�fica.
	 */

	public InterfaceCameraLID(){
		// Tratando exce��es
		try {

			//instanciar os bot�es GUI
			iniciar= new JButton("Iniciar");
			encerrar= new JButton("Encerrar");

			fieldUsu�rio= new JTextField(15);
			fieldSenha= new JPasswordField(15);
			
			//setar a cor dos botoes
			iniciar.setBackground(Color.white);
			encerrar.setBackground(Color.white);

			// Instanciando a camada principal do JPanel
			Container c= getContentPane();

			JPanel login = new JPanel();
			login.add(new JLabel("USU�RIO:"));
			login.add(fieldUsu�rio);
			login.add(new JLabel("SENHA:"));
			login.add(fieldSenha);
			login.setLayout(new GridLayout(4,0));

			
			//Instanciando outra camada do JPanel, respons�vel pelos bot�es
			JPanel botoes = new JPanel();
			botoes.add(iniciar);
			botoes.add(encerrar);
			botoes.setLayout(new GridLayout(0,2));

			//Instanciando outra camada do Jpanel, respons�vel pela mensagem de "Sensor Ativo" ou "Sensor Inativo"
			JPanel msg =  new JPanel();
			msg.add(mensagem1);
			msg.add(mensagemCam1);
			msg.add(mensagemCam2);
			msg.setLayout(new  GridLayout(3,0));

			//Instanciando outra camada do webcamPanel, essa respons�vel por apresentar no aplicativo o video da webcam em tempo real 
			//WebcamPanel webcm = new WebcamPanel(camera);

			//Adicionando todas as camadas acimas no painel principal C 
			c.add(Box.createHorizontalStrut(100)); //Espa�os
			c.add(login);
			c.add(Box.createHorizontalStrut(100)); //Espa�os
			c.add(botoes);
			c.add(Box.createHorizontalStrut(100)); //Espa�os
			c.add(msg);
			c.setLayout(new FlowLayout());

			//Adicionando evento para a��o do bot�es
			iniciar.addActionListener(this);
			encerrar.addActionListener(this);
			//Adicionando evento para apagar os fields quando houver o clique do mouse
			
			/*
			 * Deixando a vis�vel o Frame, setando seu tamanho, localizando-o no centro da tela e 
			 * setando opera��o para finalizar a execu��o do programa junto � interface grafica
			 */
			setVisible(true);
			setSize(200,250);
			setResizable(false);
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
			if(fieldUsu�rio.getText().contentEquals("") || fieldSenha.getText().contentEquals("") ){
				//Se estiveram vazias, mostra mensagem de erro
				JOptionPane.showMessageDialog(null," \tCampos Vazios!\n\tInserir por favor.", getWarningString(),JOptionPane.ERROR_MESSAGE,null);
			}
			//else if(teste de valida��o de senha){}
			else{
			//Setando cor dos textos
			mensagem1.setText("Sensor Ativo");
			mensagem1.setForeground(Color.GREEN);
			mensagemCam1.setText("Camera Um Desativada");
			mensagemCam1.setForeground(Color.RED);
			mensagemCam2.setText("Camera Dois Desativada");
			mensagemCam2.setForeground(Color.RED);
			//Setando cor botoes
			iniciar.setBackground(Color.GREEN);
			encerrar.setBackground(Color.white);
			
			//Setando as Strinh de login
			usuario =fieldUsu�rio.getText();
			senha =fieldSenha.getText();
			/*
			try {
				concorrencia.sleep(15000);
				concorrencia.start();
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			}
		}
		//Se o evento foi do bot�o encerrar
		else if(a��o.getSource()==encerrar){
			//seta as variavel de controle como 0
			//seta a mensagem como sensor inativo e a cor de vermelho
			mensagem1.setText("Sensor Inativo");
			mensagem1.setForeground(Color.RED);

			mensagemCam1.setText("Camera Um Desativada");
			mensagemCam1.setForeground(Color.RED);

			mensagemCam2.setText("Camera Dois Desativada");
			mensagemCam2.setForeground(Color.RED);
			//seta a cor do bot�o iniciar para o padr�o(branco) e o bot�o encerrar para vermelho
			iniciar.setBackground(Color.white);
			encerrar.setBackground(Color.red);
			 if(javax.swing.JOptionPane.showConfirmDialog(null,"Deseja Fechar?","ATEN��O ",javax.swing.JOptionPane.YES_NO_OPTION )==0){     
		            fechar();     
		        }
			
		}
	}
	
	public static  void fechar() {
		System.exit(ABORT);
	}

	public static void iniciar(){
		new Concorrencia();
	}
}
