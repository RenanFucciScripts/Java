package faixaScanner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

public class IntercalarAmostras {
	public static void main(String[] args) {
		try {
			String strDir="C:\\Users\\Renan Fucci\\Dropbox\\Estante Magica_Renan_Fucci\\Pasta_Renan_Fucci\\2015-08-17\\"
					+"imagensFaixaScanner\\";
			
			/* Sem Janela 3x3 */
			FileReader arq = new FileReader(strDir+"AmosNaoRuidosMarg0.txt");
			BufferedReader leitura =  new BufferedReader(arq);
			FileReader arq0 = new FileReader(strDir+"AmosRuidosMarg1.txt");
			BufferedReader leitura0 =  new BufferedReader(arq0);
			FileWriter wrf =new FileWriter(strDir+"AmosRuidosMargFinal.txt");

			/* Com Janela 3x3 */
		/* 	FileReader arq = new FileReader(strDir+"arqAmosNaoRuidoMargJanela0.txt");
			BufferedReader leitura =  new BufferedReader(arq);
			FileReader arq0 = new FileReader(strDir+"arqAmosRuidosMargJanela1.txt");
			BufferedReader leitura0 =  new BufferedReader(arq0);
			FileWriter wrf =new FileWriter(strDir+"arqAmosRuidosMargJanelaFinal.txt");
*/

			int cont=0;
			String linhaFInal = "";
			while(linhaFInal!=null){
				if(cont%2==0){
					linhaFInal=leitura.readLine();
					wrf.write(linhaFInal+"\n");
					cont+=1;
				}
				else{
					linhaFInal=leitura0.readLine();
					wrf.write(linhaFInal+"\n");
					cont+=1;
				}
			}
			wrf.close();
			leitura.close();
			leitura0.close();
		}catch(FileNotFoundException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}catch(Exception e){
			System.err.println(e.getMessage());
			e.printStackTrace();

		}

	}
}
