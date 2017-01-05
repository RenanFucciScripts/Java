package metodos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Resto {

	public static void main(String[] args) {
		try {
			String strdir="C:\\Users\\Renan Fucci\\Dropbox\\RIO 45º\\2015-07-25";
			String strArq="C:\\Users\\Renan Fucci\\Dropbox\\RIO 45º\\2015-07-25\\MenosResto.txt";
			FileReader arq = new FileReader(new File(strArq));
			BufferedReader bfArq =  new BufferedReader(arq);
			String linha = bfArq.readLine();
			while (linha!=null){
				File fl =  new File(strdir+"\\"+linha+".txt");
				System.out.println(fl.getName());
				fl.delete();
				linha=bfArq.readLine();
			}
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
