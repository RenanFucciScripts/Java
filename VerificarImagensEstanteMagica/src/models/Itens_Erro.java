package models;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author _
 */
public class Itens_Erro {
  
	private String fileName;
	private ArrayList<String>  msgs_erro = new ArrayList<String>();
	
	public Itens_Erro(File fl) {
		this.fileName = fl.getName();
	}
	
	public Itens_Erro(String fileName) {
		this.fileName = fileName;
	}
	
	public void addMsgErro(String msg_erro){
		msgs_erro.add(msg_erro);
	}
	
	/**
	 * @return the msgs_erro
	 */
	public ArrayList<String> getMsgs_erro() {
		return msgs_erro;
	}
	
	/**
	 * @return the fileName
	 */
	
	public String getFileName(){
		return fileName;
	}
}
