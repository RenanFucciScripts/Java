package printScreen;

import static java.awt.GraphicsDevice.WindowTranslucency.TRANSLUCENT;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class PrintScreen {

	// Static Variavel Global para captura de tela
	public static PrintScreen windowReader;
	
	public PrintScreen() {
		// TODO Auto-generated constructor stub
		/*Testa característica transparente*/
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		
		if(!gd.isWindowTranslucencySupported(TRANSLUCENT)){
			System.err.println("Transparencia nao eh suportada");
			System.exit(0);
		}
		
		TransparentFrame tw =  new TransparentFrame();
		
		/*Como opacidade eh 55, a transparencia eh 45%
		 * Pode-se setar a 100%, fazendo com que o usuário não veja a janela (Frame)
		 * */
		tw.setOpacity(0.55f);
		tw.setVisible(true);
		
	}
	public static void main(String[] args) {
		windowReader =  new PrintScreen();
	}
	
}
