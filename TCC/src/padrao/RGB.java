package padrao;

public class RGB {

	public static void main(String[] args) {
		MetodosRNA  methods =  new MetodosRNA();
		
		String dirPadrao = "C:/Users/_/Dropbox/UFMS - CPAN/7º Semestre - 2016/TCC II/fuzzy/saolourençoespectrais/";
		String [] bands  = {"0","Banda1_RED_16Bits", "Banda2_GREEN_16Bits",
				"Banda3_BLUE_16Bits", "Banda4_RED_EDGE_16Bits", "Banda5_NIR_16Bits" }; 
		int[] band = {1,4,3};
		for (int i=1; i<= bands.length-1; i++){
			for (int j = 1; j <= bands.length-1; j++) {
				for (int j2 = 1; j2 <= bands.length-1; j2++) {
					String strexec = ("_"+i+"_"+j+"_"+j2);
					
					int[][] red= methods.leImagem(dirPadrao+bands[i]+".png","Red");
					int[][] green=  methods.leImagem(dirPadrao+bands[j]+".png","Green");	
					int[][] blue=methods.leImagem(dirPadrao+bands[j2]+".png","Blue" );

					methods.gravarImagemColorida(red, green, blue, dirPadrao, "composicao"+strexec+".jpg");
					
				}
			}
		}
		
		//3,4,5
		//periodo de seca
		
	}

}
