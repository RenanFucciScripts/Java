package _openCV;

import java.io.File;

public class ArrayDirs {

	
	static String[] arrayDirs(File file){
		try{
			String regex = "\\\\";
			String[] x = file.getAbsolutePath().split(regex);
			return x;
		}catch(Exception ex){
			System.err.println(ex.getLocalizedMessage());
			ex.printStackTrace();
		}
		return null;
	}
}
