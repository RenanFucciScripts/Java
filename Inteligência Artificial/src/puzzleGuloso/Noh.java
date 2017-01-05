package puzzleGuloso;

public class Noh {

	public Noh esquerda;
	public Noh direita;
	public Noh nohPai;
	public String etiqueta;
	public String ação;
	public int profundidade;
	public int custoCaminho;
	public int[][] estado;
	
	
	public Noh(String etiqueta) {
		this.esquerda=null;
		this.direita=null; 
		this.nohPai=null;
		this.profundidade=0;
		this.custoCaminho=0;
		this.estado=null;
		this.etiqueta=etiqueta; 
	}
 
	public int[][] getEstado() {
		return estado;
	}

	public void setEstado(int[][] matriz) {
		this.estado= new int[matriz.length][matriz.length];
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz.length; j++) {
				this.estado[i][j]=matriz[i][j];
			}
		}
	}
	public void imprimirMatriz(){
		for (int i = 0; i < estado.length; i++) {
			for (int j = 0; j < estado.length; j++) {
				System.out.print(this.estado[i][j]+" ");
			}
			System.out.println("");
		}
	}

	public Noh getEsquerda() {
		return esquerda;
	}

	public void setEsquerda(Noh esquerda) {
		this.esquerda = esquerda;
	}

	public Noh getDireita() {
		return direita;
	}

	public void setDireita(Noh direita) {
		this.direita = direita;
	}

	public Noh getNohPai() {
		return nohPai;
	}

	public void setNohPai(Noh nohPai) {
		this.nohPai = nohPai;
	}

	public String getEtiqueta() {
		return etiqueta;
	}

	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	public String getAção() {
		return ação;
	}

	public void setAção(String ação) {
		this.ação = ação;
	}

	public int getProfundidade() {
		return profundidade;
	}

	public void setProfundidade(int profundidade) {
		this.profundidade = profundidade;
	}

	public int getCustoCaminho() {
		return custoCaminho;
	}

	public void setCustoCaminho(int custoCaminho) {
		this.custoCaminho = custoCaminho;
	}


	
	
}
