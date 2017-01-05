import org.opencv.core.Core;
import org.opencv.videoio.VideoCapture;

public class TesteOpenCV {

	public static void main(String[] args) {
		//Sempre usar a linha abaixo, como primeira linha de codigo, quando for utilizar a biblioteca
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		//Código teste
		VideoCapture vdcap = new VideoCapture();
		System.out.println("Biblioteca "+((vdcap.equals(null)) ? ("não ") : (""))+"foi incluida");
	}
}
