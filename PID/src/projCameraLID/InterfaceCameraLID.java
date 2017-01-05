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
	 * Variáveis globais GUI
	 */
	String usuario;
	String senha;
	JButton iniciar;
	JButton encerrar;
	JTextField fieldUsuário;
	JTextField fieldSenha;
	JLabel mensagem1= new JLabel();
	JLabel mensagemCam1= new JLabel();
	JLabel mensagemCam2= new JLabel();
	Thread concorrencia = new Thread(new Concorrencia());
	/*
	 * Método principal que vai instanciar o objeto da classe ProjetoInterface
	 * */
	public static void main(String[] args) throws IOException {
		new InterfaceCameraLID();
		//System.in.read();// Manter o programa em leitura
	}  

	/*
	 * Método construtor do aplicativo, bem como sua interface gráfica.
	 */

	public InterfaceCameraLID(){
		// Tratando exceções
		try {

			//instanciar os botões GUI
			iniciar= new JButton("Iniciar");
			encerrar= new JButton("Encerrar");

			fieldUsuário= new JTextField(15);
			fieldSenha= new JPasswordField(15);
			
			//setar a cor dos botoes
			iniciar.setBackground(Color.white);
			encerrar.setBackground(Color.white);

			// Instanciando a camada principal do JPanel
			Container c= getContentPane();

			JPanel login = new JPanel();
			login.add(new JLabel("USUÁRIO:"));
			login.add(fieldUsuário);
			login.add(new JLabel("SENHA:"));
			login.add(fieldSenha);
			login.setLayout(new GridLayout(4,0));

			
			//Instanciando outra camada do JPanel, responsável pelos botões
			JPanel botoes = new JPanel();
			botoes.add(iniciar);
			botoes.add(encerrar);
			botoes.setLayout(new GridLayout(0,2));

			//Instanciando outra camada do Jpanel, responsável pela mensagem de "Sensor Ativo" ou "Sensor Inativo"
			JPanel msg =  new JPanel();
			msg.add(mensagem1);
			msg.add(mensagemCam1);
			msg.add(mensagemCam2);
			msg.setLayout(new  GridLayout(3,0));

			//Instanciando outra camada do webcamPanel, essa responsável por apresentar no aplicativo o video da webcam em tempo real 
			//WebcamPanel webcm = new WebcamPanel(camera);

			//Adicionando todas as camadas acimas no painel principal C 
			c.add(Box.createHorizontalStrut(100)); //Espaços
			c.add(login);
			c.add(Box.createHorizontalStrut(100)); //Espaços
			c.add(botoes);
			c.add(Box.createHorizontalStrut(100)); //Espaços
			c.add(msg);
			c.setLayout(new FlowLayout());

			//Adicionando evento para ação do botões
			iniciar.addActionListener(this);
			encerrar.addActionListener(this);
			//Adicionando evento para apagar os fields quando houver o clique do mouse
			
			/*
			 * Deixando a visível o Frame, setando seu tamanho, localizando-o no centro da tela e 
			 * setando operação para finalizar a execução do programa junto à interface grafica
			 */
			setVisible(true);
			setSize(200,250);
			setResizable(false);
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
			if(fieldUsuário.getText().contentEquals("") || fieldSenha.getText().contentEquals("") ){
				//Se estiveram vazias, mostra mensagem de erro
				JOptionPane.showMessageDialog(null," \tCampos Vazios!\n\tInserir por favor.", getWarningString(),JOptionPane.ERROR_MESSAGE,null);
			}
			//else if(teste de validação de senha){}
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
			usuario =fieldUsuário.getText();
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
		//Se o evento foi do botão encerrar
		else if(ação.getSource()==encerrar){
			//seta as variavel de controle como 0
			//seta a mensagem como sensor inativo e a cor de vermelho
			mensagem1.setText("Sensor Inativo");
			mensagem1.setForeground(Color.RED);

			mensagemCam1.setText("Camera Um Desativada");
			mensagemCam1.setForeground(Color.RED);

			mensagemCam2.setText("Camera Dois Desativada");
			mensagemCam2.setForeground(Color.RED);
			//seta a cor do botão iniciar para o padrão(branco) e o botão encerrar para vermelho
			iniciar.setBackground(Color.white);
			encerrar.setBackground(Color.red);
			 if(javax.swing.JOptionPane.showConfirmDialog(null,"Deseja Fechar?","ATENÇÂO ",javax.swing.JOptionPane.YES_NO_OPTION )==0){     
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
