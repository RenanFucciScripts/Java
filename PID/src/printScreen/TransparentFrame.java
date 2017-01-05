package printScreen;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

import jdk.management.resource.internal.inst.ThreadRMHooks;

public class TransparentFrame extends JFrame implements MouseMotionListener, ActionListener{

	public TransparentFrame() {
		// TODO Auto-generated constructor stub
		addMouseMotionListener(this);
		/*Tira os botoes de decoracao, por exemplo: fechar, maximizar, ...*/
		setUndecorated(true);
		
		setLayout(new GridBagLayout());
		setSize(150,150);
		setLocation(200, 200);
		
		//Iniciando a Thread que ira ler a tela
		ThreadReadData t =  new ThreadReadData();
		t.windowReference = this;
		t.start();
	}
	
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		/*arrastar para onde quiser, p */
		this.setLocation(e.getLocationOnScreen().x - this.getSize().width/2, e.getLocationOnScreen().y - this.getSize().height/2);
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}
	
}
