import java.io.File;


public class rar {

	public static void main(String[] args) {
		String dirEntrada= "/tmp/guest-b3W1nD/Documentos/";
		String nomeArq_Extens = "teste.rar";
		unRAR(dirEntrada, nomeArq_Extens);
	}

	public static void unRAR(String dirEntrada, String nomeArq_Extens) {
		File fl = new File(dirEntrada+nomeArq_Extens);
		String auxStr =fl.getAbsolutePath().substring((fl.getAbsolutePath().lastIndexOf("/")), fl.getAbsolutePath().lastIndexOf("."));
		auxStr =fl.getAbsolutePath().substring(0,fl.getAbsolutePath().lastIndexOf("/"))+(auxStr.replaceAll("\\s", ""));
		File pasta =new File(auxStr+"/");
		pasta.mkdir();
		String command="unrar e "+fl.getAbsolutePath()+" "+pasta.getAbsolutePath();
		try {
				Process p2 = Runtime.getRuntime().exec(new String[]{"bash","-c",command+"/"});
			} catch (Exception e) {
				System.err.println(e.getLocalizedMessage());
			}
	}
}
