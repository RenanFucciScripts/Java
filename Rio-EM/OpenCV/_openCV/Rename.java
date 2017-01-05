package _openCV;

import java.io.File;
import java.io.IOException;
import java.util.Stack;

import org.apache.commons.io.FileUtils;

import metodos.MetodosRF;

public class Rename {

	public static void main(String[] args) {
		MetodosRF metodosRF =  new MetodosRF();
		String pathname = "C:/Users/Renan Fucci/Dropbox/Estante Magica_Renan_Fucci/Pasta_Renan_Fucci/2016-05-05/";
		File folder =  new File(pathname);
		renomearArqTraco(folder);
		Stack<File> pilhaArqs =  new Stack<>();
		metodosRF.empilharArquivosDiretorio(folder, pilhaArqs );
		
		for (File fl : pilhaArqs) {
			String str = fl.getName().substring(0, fl.getName().lastIndexOf("."));
			System.out.println(fl.getName().substring(0, fl.getName().lastIndexOf(".")));
			if(str.contentEquals("capa")){
				System.out.println("capa");
				renomearArqTraco(fl);
				
			}
		}
	}
	
	private static File renomearArqTraco(File fileEntry){
		String fileName; //nome do arquivo
		File flrenomeado;//arquivo renomeado

		fileName = fileEntry.getName().replaceAll("capa", "ilustracao_0");
		flrenomeado = new File(fileEntry.getParent()+File.separator+fileName);
		try {
			FileUtils.copyFile(fileEntry, flrenomeado);
			System.err.println("\nArquivo com espacos em branco, renomeado para: "+flrenomeado);
			fileEntry.delete();
			return flrenomeado;

		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
		return null;
	}
}

