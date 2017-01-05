package procImg;

public class FiltroMediana extends MetodosProcImagens {
	public static void main(String[] args) {
			
		String arq = "020715000009525";
		String dir = "C:\\Users\\Renan Fucci\\Dropbox\\RIO 45º\\testeQRCode\\";
		int array[][]=leImagem((dir+arq+".jpg"), "Red");
		
		array=convoluçãoMediana(array, 3, 1);
		gravarImagem(array, (dir+arq+"(MDN_C3)"));
	}
}
