package teste;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

@SuppressWarnings("serial")

//Implementando action listener pra executar algo a partir da ação de click no botão
public class ClasseTeste extends JFrame implements ActionListener {
	

	//Criando variaveis globais pra acesso no ActionListener
	
	JButton botão1;
	JTextField texto;
	
	public static void main(String[] args) {
		new ClasseTeste();
		
	}
	
	
	//Contrutor da classe teste, que como extende uma Jframe, também é um Jframe
	public ClasseTeste() {
	
		texto=new JTextField();
		botão1= new JButton("Mostrar Texto");
	
		Container c = getContentPane();
		c.setLayout(new GridLayout(0, 1));
		c.add(texto);
		c.add(botão1);
		
		botão1.addActionListener(this);
		setVisible(true);
		setSize(500, 300);
		
	}

	@Override
	public void actionPerformed(ActionEvent ação) {
		if(ação.getSource()==botão1){
			texto.setText("Mostrando texto.......................");
		}
	}
	

}
