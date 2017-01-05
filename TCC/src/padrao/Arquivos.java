package padrao;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;


public class Arquivos {

	public static void main(String[] args) {
		try {
			String dirArq =  "D:/Usuarios/renan.fucci/Documents/Semana 2/amostras.txt";
			String dirArq0 =  "D:/Usuarios/renan.fucci/Documents/Semana 2/amostra0.txt";
			String dirArq00 =  "D:/Usuarios/renan.fucci/Documents/Semana 2/amostra0.txt";

			String dirArq1 =  "D:/Usuarios/renan.fucci/Documents/Semana 2/amostra1.txt";
			String dirArqFinal =  "D:/Usuarios/renan.fucci/Documents/Semana 2/amostrasFinal.txt";

			FileReader arq = new FileReader(dirArq);
			BufferedReader leitura =  new BufferedReader(arq);


			FileReader arq0 = new FileReader(dirArq0);
			BufferedReader leitura0 =  new BufferedReader(arq0);

			FileReader arq00 = new FileReader(dirArq00);
			BufferedReader leitura00 =  new BufferedReader(arq00);


			FileReader arq1 = new FileReader(dirArq1);
			BufferedReader leitura1 =  new BufferedReader(arq1);

			/*FileWriter wr0 =new FileWriter(dirArq0);
				FileWriter wr1 =new FileWriter(dirArq1);
			 */
			FileWriter wrf =  new FileWriter(dirArqFinal);

			String linha= leitura.readLine();
			int cont=2;
			String linhaFInal;
			while(true){
				System.out.println("\ncont"+cont);
				if(cont%2==0){
					System.out.println("pass P");
					linhaFInal=leitura0.readLine();
					if(linhaFInal==null){
						linhaFInal =  leitura00.readLine();
						System.out.println("resetou\n"+linhaFInal);
					}
					System.out.println("par\n"+linhaFInal);
					wrf.write(linhaFInal+"\n");
					cont+=1;

				}
				else{
					System.out.println("pass Else");
					linhaFInal=leitura1.readLine();
					if(linhaFInal==null){
						break;
					}
					System.out.println("impar\n"+linhaFInal);
					wrf.write(linhaFInal+"\n");
					cont+=1;
				}

			}
			wrf.close();
			/*	while (linha!=null){
					String str=linha.substring(linha.lastIndexOf(",")+1, linha.length());
					System.out.println(str);
					if(str.contentEquals("0")==true){
						System.out.println("arq0");
						wr0.write(linha+"\n");
					}
					else{
						System.out.println("arq1");
						wr1.write(linha+"\n");
					}
					linha= leitura.readLine();
				}
				wr0.close();
				wr1.close();
			 */} catch (FileNotFoundException e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
			 }catch(Exception e){
				 e.getMessage();
			 }

	}
}


