package puzzlelargura;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;


public class PuzzleLargura{


	Queue<String> etiquetas= new LinkedList<String>();
	public int[][] solução;

	public int[][] getSolução() {
		return solução;
	}

	public void setSolução(int[][] solução) {
		this.solução= new int[solução.length][solução.length];

		for (int i = 0; i < solução.length; i++ ){
			for (int j = 0; j < solução.length ; j++ ) {
				this.solução[i][j] = solução [i][j];
			}
		}

	}


	public int[][] problema;

	public PuzzleLargura() {
		String[] et={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};


		for (String x: et) {
			etiquetas.add(x);
		}

		for (String x: et) {
			String y= (x+""+x);
			etiquetas.add(y);
		} 
		for (String x: et) {
			String z= (x+""+x+""+x);
			etiquetas.add(z);
		} 

		for (String x: et) {
			String z= (x+""+x+""+x+""+x);
			etiquetas.add(z);
		} 

		for (String x: et) {
			String z= (x+""+x+""+x+""+x+""+x);
			etiquetas.add(z);
		} 

		//System.out.println(etiquetas);
	}

	public void buscaEmLargura(int[][] problema, Queue<Noh> fila){
		fila =criarFila(etiquetas.poll(), problema, fila);
		Noh no;
		System.out.print("Fila:");
		imprimirFila(fila);
		int cont=0;
		while(true){
			if(vazia(fila)==true){
				System.err.println("Lista Vazia");
				return;
			}
			no =  removerPrimeiro(fila);
			//	imprimirFila(fila);
			if(testarObjetivo(no)==true){
				System.out.println("--------------------------Solução Encontrada!---------------------");
				System.out.println("\n");
				imprimirNoh(no);
				return;
			}
			fila=inserirTodos(expandir(no), fila);
			cont+=1;
			System.out.print("Fila:");
			imprimirFila(fila);


		}

	}





	/*
	  borda = INSERIR(CRIAR-NO[problema]),borda)
	  repita
	  	se borda(VAZIA) ENTÃO RETORNA FALHA
	  	no = remover-primeiro(borda)
	  	se TESTAR-OBJETIVO[] aplicado a ESTADO[nó] tem sucesso
	  		então retornar SOLUÇÂO(nó)
	  	borda= INSERIR-TODOS(EXPANDIR(nó,problema),borda)
	 */

	public int contarLoop(Noh no){
		int[][] estado= no.getEstado();
		//	 System.out.println("Tamanho"+estado.length);
			for (int i = 0; i < estado.length; i++) {
				for (int j = 0; j < estado[i].length; j++) {
					if(estado[i][j]==0){
						if(i==0 && j==0){
							System.out.println("loop 1");
							return 2;
						}
						else if(( (i==0) && (j== estado.length-1))){
							System.out.println("loop 2");
							return 2;
						}
						else if(((i==estado.length-1) && (j==0))){
							System.out.println("loop 3");
							return 2;
						}
						else if(((i==estado.length-1) && (j==estado.length-1))){
							System.out.println("loop 4");
							return 2;
						}
						else if((i==0 && (j>0 || j<estado.length-1))){
							System.out.println("loop 5");
							return 3;
						}
						else if(((i==estado.length-1) && (j>0 || j<estado.length-1))){
							System.out.println("loop 6");
							return 3;
						}
						else if(((j==0) && (i>0 || i< estado.length-1))){
							System.out.println("loop 7");
							return 3;
						}
						else if(((j==estado.length-1) && (i>0 || i< estado.length-1))){
							System.out.println("loop 8");
							return 3;
						}
						else{
							System.out.println("loop 9");
							return 4;
						}
					}
				}
			}
		
		return 0;
	}

	public Queue<Noh> expandir(Noh noh){
		//System.out.println("\n Expandir");
		Queue<Noh> sucessores = new LinkedList<Noh>();

		int loop= contarLoop(noh);
		System.out.println("Tamanho loop: "+loop);

		for (int i=0 ;i<loop; i++){
			Noh s= new Noh(etiquetas.poll());
			s.setEstado(ação(i, noh.getEstado()));
			s.nohPai=noh;
			s.ação=("filho "+(i));
			s.custoCaminho=noh.custoCaminho+1;
			s.profundidade=noh.profundidade+1;
			sucessores.add(s);
			imprimirNoh(s);
		}
		/*System.out.print("Sucessores: ");
		imprimirFila(sucessores);
		 */
		return sucessores;
	}
	/*
		 sucessores = o conjunto vazio
		 para cada (ação, resultado) em sucessor[problema](estado[no]) faça
		 	s= um novo no
		 	ESTADO[s] = resultado
		 	NÓ-PAI[s] = nó
		 	AÇÂO[s]	  = ação
		 	CUSTODOCAMINHO[s] = CUSTODOCAMINHO + CUSTODOPASSO(nó,ação,s)
		 	PROFUNDIDADE[s] = PROFUNDIDADE[nó] + 1
		 	adicionar s a sucessores
		 retorna sucessores
	 */

	public boolean testarObjetivo(Noh no){
		//System.out.println("Teste Objetivo");
		int[][] tmp = new int[no.estado.length][no.estado.length];
		int cont=0;
		/*System.out.println("Nó estado - "+(no.getEtiqueta()));
		for (int i = 0; i < tmp.length; i++) {
			for (int j = 0; j < tmp.length; j++) {
				System.out.print(no.estado[i][j]+"");
			}
			System.out.println("");
		}

		System.out.println("No Solução");
		for (int i = 0; i < tmp.length; i++) {
			for (int j = 0; j < tmp.length; j++) {
				System.out.print(this.solução[i][j]+"");
			}
			System.out.println("");
		}
		 */
		//System.out.println("No diferença");
		for (int i = 0; i < tmp.length; i++) {
			for (int j = 0; j < tmp.length; j++) {
				//System.out.print((tmp[i][j]=Math.abs(this.solução[i][j]-no.estado[i][j]))+"");
				if(Math.abs(this.solução[i][j]-no.estado[i][j])!=0){
					return false;
				}
			}
		}
		/*
		//	System.out.println("Estado depois da sub");
		for (int i = 0; i < tmp.length; i++) {
			for (int j = 0; j < tmp.length; j++) {
				//		System.out.print(no.estado[i][j]+"");
				if(tmp[i][j]==0){
					cont+=1;
				}
			}
			System.out.println("");
		}
		/*	System.out.println("\n CONTADOR:"+cont);
		System.out.println("\n");
		 */	
		return true;

	}

	public int[][] ação(int ação, int[][] estado){
		//ação 0 esquerda, 1 direita
		int[][] tmp = new int[estado.length][estado.length];

		//	System.out.println("Instanciando matrizes");
		for (int i = 0; i < estado.length; i++) {
			for (int j = 0; j < estado.length; j++) {
				tmp[i][j]=estado[i][j];
			}
			System.out.println("");
		}

		//Trocando as posições do zero
		for (int i = 0; i < estado.length; i++) {
			for (int j = 0; j < estado[i].length; j++) {
				//Testar qual posição da matriz é a que será mudada
				if(estado[i][j]==0){
					if(i==0 && j==0){
						System.out.println("Teste 1");
						//Mudar para ação 0
						if(ação==0){
							//trocando c/ da direita
							tmp[i][j]=estado[i][j+1];
							tmp[i][j+1]=0;
							//System.out.println("Direita 1");
							return tmp;
						}
						//Mudar para ação 1
						else{
							//trocando c/ de baixo
							tmp[i][j]=estado[i+1][j];
							tmp[i+1][j]=0;
							//System.out.println("Baixo 1");
							return tmp;
						}	
					}
					else if(( (i==0) && (j== estado.length-1))){
						System.out.println("Teste 2");
						if(ação==0){
							//trocando c/ de baixo
							tmp[i][j]=estado[i+1][j];
							tmp[i+1][j]=0;
							//System.out.println("Baixo 2");
							return tmp;
						}
						else{
							//trocando c/ da esquerda
							tmp[i][j]=estado[i][j-1];
							tmp[i][j-1]=0;
							//System.out.println("Esquerda 1");
							return tmp;
						}
					}
					else if(((i==estado.length-1) && (j==0))){
						System.out.println("Teste 3");
						if(ação==0){
							//Trocando c/ de cima
							tmp[i][j]=estado[i-1][j];
							tmp[i-1][j]=0;
							//System.out.println("Cima 1");
							return tmp;
						}
						else{
							//Trocando c/ da direita
							tmp[i][j]=estado[i][j+1];
							tmp[i][j+1]=0;
							//System.out.println("Direita 2");
							return tmp;
						}
					}
					else if(((i==estado.length-1) && (j==estado.length-1))){
						System.out.println("Teste 4");
						if(ação==0){
							//Trocando c/ de cima
							tmp[i][j]=estado[i-1][j];
							tmp[i-1][j]=0;
							//System.out.println("Cima 1");
							return tmp;
						}
						else{
							//trocando c/ da esquerda
							tmp[i][j]=estado[i][j-1];
							tmp[i][j-1]=0;
							//System.out.println("Esquerda 1");
							return tmp;
						}
					}
					else if((i==0 && (j>0 || j<estado.length-1))){
						System.out.println("Teste 5");
						if(ação==0){
							//trocando c/ de baixo
							tmp[i][j]=estado[i+1][j];
							tmp[i+1][j]=0;
							//System.out.println("Baixo 2");
							return tmp;
						}
						else if(ação==1){
							//trocando c/ da esquerda
							tmp[i][j]=estado[i][j-1];
							tmp[i][j-1]=0;
							//System.out.println("Esquerda 1");
							return tmp;
						}
						else{
							//trocando c/ da direita
							tmp[i][j]=estado[i][j+1];
							tmp[i][j+1]=0;
							//System.out.println("Direita 1");
							return tmp;
						}
					}
					else if(((i==estado.length-1) && (j>0 || j<estado.length-1))){
						System.out.println("Teste 6");
						if(ação==0){
							//Trocando c/ de cima
							tmp[i][j]=estado[i-1][j];
							tmp[i-1][j]=0;
							//System.out.println("Cima 1");
							return tmp;
						}
						else if(ação==1){
							//trocando c/ da esquerda
							tmp[i][j]=estado[i][j-1];
							tmp[i][j-1]=0;
							//System.out.println("Esquerda 1");
							return tmp;
						}
						else{
							//trocando c/ da direita
							tmp[i][j]=estado[i][j+1];
							tmp[i][j+1]=0;
							//System.out.println("Direita 1");
							return tmp;
						}
					}
					else if(((j==0) && (i>0 || i< estado.length-1))){
						System.out.println("Teste 7");
						if(ação==0){
							//Trocando c/ de cima
							tmp[i][j]=estado[i-1][j];
							tmp[i-1][j]=0;
							//System.out.println("Cima 1");
							return tmp;
						}
						else if(ação==1){
							// Trocando c/ de baixo
							tmp[i][j]=estado[i+1][j];
							tmp[i+1][j]=0;
							return tmp;
						}
						else{
							//Trocando c/ da direita
							tmp[i][j]=estado[i][j+1];
							tmp[i][j+1]=0;
							//System.out.println("Direita 2");
							return tmp;
						}
					}
					else if(((j==estado.length-1) && (i>0 || i< estado.length-1))){
						System.out.println("Teste 8");
						if(ação==0){
							//Trocando c/ de cima
							tmp[i][j]=estado[i-1][j];
							tmp[i-1][j]=0;
							//System.out.println("Cima 1");
							return tmp;
						}
						else if(ação==1){
							// Trocando c/ de baixo
							tmp[i][j]=estado[i+1][j];
							tmp[i+1][j]=0;
							return tmp;
						}
						else {
							//trocando c/ da esquerda
							tmp[i][j]=estado[i][j-1];
							tmp[i][j-1]=0;
							//System.out.println("Esquerda 1");
							return tmp;
						}
					}
					else{
						System.out.println("Teste 9");
						if(ação==0){
							//	trocando c/ da esquerda
							tmp[i][j]=estado[i][j-1];
							tmp[i][j-1]=0;
							return tmp;
						}
						else if(ação==1){
							// Trocando c/ de baixo
							tmp[i][j]=estado[i+1][j];
							tmp[i+1][j]=0;
							return tmp;
						}
						else if(ação==2){
							//Trocando c/ de cima
							tmp[i][j]=estado[i-1][j];
							tmp[i-1][j]=0;
							//System.out.println("Cima 1");
							return tmp;
						}
						else {
							//Trocando c/ da direita
							tmp[i][j]=estado[i][j+1];
							tmp[i][j+1]=0;
							//System.out.println("Direita 2");
							return tmp;
						}
					}
				}
			}
		}
		System.err.println("Nao deveria \nNão deveria");
		return tmp;
	}

	public Queue<Noh> criarFila(String elemento, int[][] problema, Queue<Noh> fila){
		Noh x = new Noh(elemento);
		this.problema=new int[problema.length][problema.length];

		System.out.println("Problema Global");
		for (int i = 0; i < problema.length; i++) {
			for (int j = 0; j < problema.length; j++) {
				this.problema[i][j]=problema[i][j];
				System.out.print(this.problema[i][j]+" ");
			}
			System.out.println("");
		}


		/*System.out.println("Problema Parametro");
		for (int i = 0; i < problema.length; i++) {
			for (int j = 0; j < problema.length; j++) {
				System.out.print(problema[i][j]+" ");
			}
			System.out.println("");
		}
		System.out.println("");
		 */x.setEstado(this.problema);
		 fila.add(x);
		 return fila;
	}

	public boolean vazia(Queue<Noh> fila){
		if(fila.isEmpty()==true){return true;}
		else
			return false;
	}

	public Noh primeiro(Queue<Noh> fila){
		return fila.peek();
	}

	public Noh removerPrimeiro(Queue<Noh> fila){

		return fila.poll();
	}

	public Queue<Noh> inserir(Noh etiqueta,Queue<Noh> fila){
		Noh x = etiqueta;
		imprimirNoh(x);
		fila.add(x);
		return fila;
	}

	public Queue<Noh> inserirTodos(Queue<Noh> conjElementos, Queue<Noh> fila ){
		fila.addAll(conjElementos);
		return fila;
	}

	public void imprimirFila(Queue<Noh> fila){
		System.out.print("[");
		for (@SuppressWarnings("rawtypes")
		Iterator iterator = fila.iterator(); iterator.hasNext();) {
			Noh noh = (Noh) iterator.next();
			if (iterator.hasNext()==true){
				System.out.print(noh.getEtiqueta()+",");
			}
			else System.out.print(noh.getEtiqueta());
		}
		System.out.println("]");
	}


	public void imprimirNoh(Noh noh){
		System.out.println("Etiqueta: "+noh.getEtiqueta());
		System.out.println("Ação: "+noh.getAção());
		System.out.println("Custo Caminho: "+noh.getCustoCaminho());
		System.out.println("Profundidade: "+noh.getProfundidade());
		if(noh.getNohPai()!=null){System.out.println("Nó pai: "+noh.getNohPai().getEtiqueta());}
		else {System.out.println();}
		if(noh.getEstado()!=null){
			System.out.println("Estado");
			for (int i = 0; i < noh.getEstado().length; i++) {
				for (int j = 0; j < noh.getEstado().length; j++) {
					System.out.print(noh.getEstado()[i][j]+"\t");
				}
				System.out.println("");
			}
		}


	}


}
