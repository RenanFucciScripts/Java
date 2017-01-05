package puzzleGuloso;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class PuzzleGuloso{


	Queue<String> etiquetas= new LinkedList<String>();
	public int[][] solu��o;

	public int[][] getSolu��o() {
		return solu��o;
	}

	public void setSolu��o(int[][] solu��o) {
		this.solu��o= new int[solu��o.length][solu��o.length];

		for (int i = 0; i < solu��o.length; i++ ){
			for (int j = 0; j < solu��o.length ; j++ ) {
				this.solu��o[i][j] = solu��o [i][j];
			}
		}

	}


	public int[][] problema;

	public PuzzleGuloso() {
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
			fila =ordenar(fila);
			no =  removerPrimeiro(fila);
			//	imprimirFila(fila);
			if(testarObjetivo(no)==true){
				System.out.println("--------------------------Solu��o Encontrada!---------------------");
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
	  	se borda(VAZIA) ENT�O RETORNA FALHA
	  	no = remover-primeiro(borda)
	  	se TESTAR-OBJETIVO[] aplicado a ESTADO[n�] tem sucesso
	  		ent�o retornar SOLU��O(n�)
	  	borda= INSERIR-TODOS(EXPANDIR(n�,problema),borda)
	 */


	public int contarDiferen�a(Noh no){
		int[][] tmp = new int[no.estado.length][no.estado.length];
		int cont=0;
		for (int i = 0; i < tmp.length; i++) {
			for (int j = 0; j < tmp.length; j++) {
				//System.out.print((tmp[i][j]=Math.abs(this.solu��o[i][j]-no.estado[i][j]))+"");
				tmp[i][j]=Math.abs(this.solu��o[i][j]-no.estado[i][j]);
				if(tmp[i][j]!=0){
					cont+=1;
				}
			}
			System.out.println("");
		}
		
	/*	System.out.println("\n CONTADOR:"+cont);
		System.out.println("\n");
		System.out.println("diferente"+cont);
	*/
		return cont;	
	}

	public Queue<Noh> ordenar(Queue<Noh> fila){
		
		LinkedList<Noh> lista=new LinkedList<Noh>();
		lista.addAll(fila);
		Noh aux;
		
		for (int i = 0; i < fila.size(); i++) {
			for (int j = 1; j < fila.size(); j++) {
				Noh no= lista.get(i);
				Noh no1= lista.get(j);
				//System.out.println("1� "+no.getEtiqueta()+" Custo "+no.getCustoCaminho());
				//System.out.println("2� "+no1.getEtiqueta()+" Custo "+no1.getCustoCaminho());
				
				if(no.custoCaminho>no1.custoCaminho){
					 	aux=no1;
					 	no1=no;
					 	no=aux;
					 	lista.set(i, no);
					 	lista.set(j,no1);
				}	
			}	
		}
		System.out.print("\n\nFIla");
		imprimirFila(fila);
		System.out.print("Fila Ordenada");
		imprimirFila(lista);
		return lista;
	}
	


	public int contarLoop(Noh no){
		int[][] estado= no.getEstado();
	//	System.out.println("Tamanho"+estado.length);
		if(estado.length>2){
			for (int i = 0; i < estado.length; i++) {
				for (int j = 0; j < estado[i].length; j++) {
					if(((i==0 && j==0) || (i==0 && j==2) || (i==2 && j==0) || (i==2 && j==2)) && estado[i][j]==0 ){
						return 2;
					}
					else if(((i==0 && j==1) || (i==1 && j==0) || (i==1 && j==2) || (i==2 && j==1)) && estado[i][j]==0 ){
						return 3;
					}
					else if((i==1 && j==1) && estado[i][j]==0 ){
						return 4;
					}
				}
			}
		}
		return 2;
	}

	public Queue<Noh> expandir(Noh noh){
		//System.out.println("\n Expandir");
		Queue<Noh> sucessores = new LinkedList<Noh>();

		int loop= contarLoop(noh);
		System.out.println("Tamanho loop: "+loop);

		for (int i=0 ;i<loop; i++){
			Noh s= new Noh(etiquetas.poll());
			s.setEstado(a��o(i, noh.getEstado()));
			s.nohPai=noh;
			s.a��o=("filho "+(i));
			s.custoCaminho=contarDiferen�a(s);
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
		 para cada (a��o, resultado) em sucessor[problema](estado[no]) fa�a
		 	s= um novo no
		 	ESTADO[s] = resultado
		 	N�-PAI[s] = n�
		 	A��O[s]	  = a��o
		 	CUSTODOCAMINHO[s] = CUSTODOCAMINHO + CUSTODOPASSO(n�,a��o,s)
		 	PROFUNDIDADE[s] = PROFUNDIDADE[n�] + 1
		 	adicionar s a sucessores
		 retorna sucessores
	 */

	public boolean testarObjetivo(Noh no){
		//System.out.println("Teste Objetivo");
		int[][] tmp = new int[no.estado.length][no.estado.length];
		int cont=0;
		/*System.out.println("N� estado - "+(no.getEtiqueta()));
		for (int i = 0; i < tmp.length; i++) {
			for (int j = 0; j < tmp.length; j++) {
				System.out.print(no.estado[i][j]+"");
			}
			System.out.println("");
		}

		System.out.println("No Solu��o");
		for (int i = 0; i < tmp.length; i++) {
			for (int j = 0; j < tmp.length; j++) {
				System.out.print(this.solu��o[i][j]+"");
			}
			System.out.println("");
		}
		 */
		//System.out.println("No diferen�a");
		for (int i = 0; i < tmp.length; i++) {
			for (int j = 0; j < tmp.length; j++) {
				//System.out.print((tmp[i][j]=Math.abs(this.solu��o[i][j]-no.estado[i][j]))+"");
				if(Math.abs(this.solu��o[i][j]-no.estado[i][j])!=0){
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

	public int[][] a��o(int a��o, int[][] estado){
		//a��o 0 esquerda, 1 direita
		int[][] tmp = new int[estado.length][estado.length];


		//	System.out.println("Instanciando matrizes");
		for (int i = 0; i < estado.length; i++) {
			for (int j = 0; j < estado.length; j++) {
				tmp[i][j]=estado[i][j];
			}
			System.out.println("");
		}

		//Trocando as posi��es do zero
		for (int i = 0; i < estado.length; i++) {
			for (int j = 0; j < estado[i].length; j++) {
				//Testar qual posi��o da matriz � a que ser� mudada
				if(i==0 && j==0 && estado[i][j]==0){
					//Mudar para a��o 0
					if(a��o==0){
						//trocando c/ da direita
						tmp[i][j]=estado[i][j+1];
						tmp[i][j+1]=0;
						//System.out.println("Direita 1");
						return tmp;
					}
					//Mudar para a��o 1
					else{
						//trocando c/ de baixo
						tmp[i][j]=estado[i+1][j];
						tmp[i+1][j]=0;
						//System.out.println("Baixo 1");
						return tmp;
					}	
				}
				else if(i==0 && j==1 && estado[i][j]==0){
					if(a��o==0){
						//trocando c/ de baixo
						tmp[i][j]=estado[i+1][j];
						tmp[i+1][j]=0;
						//System.out.println("Baixo 2");
						return tmp;
					}
					else if(a��o==1){
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
				else if(i==0 && j==2 && estado[i][j]==0){
					if(a��o==0){
						//	trocando c/ da esquerda
						tmp[i][j]=estado[i][j-1];
						tmp[i][j-1]=0;
						return tmp;
					}
					else{
						// Trocando c/ de baixo
						tmp[i][j]=estado[i+1][j];
						tmp[i+1][j]=0;
						return tmp;
					}
				}
				else if(i==1 && j==0 && estado[i][j]==0){
					if(a��o==0){
						//Trocando c/ de cima
						tmp[i][j]=estado[i-1][j];
						tmp[i-1][j]=0;
						//System.out.println("Cima 1");
						return tmp;
					}
					else if(a��o==1){
						//Trocando c/ da direita
						tmp[i][j]=estado[i][j+1];
						tmp[i][j+1]=0;
						//System.out.println("Direita 2");
						return tmp;
					}
					else{
						//Trocando c/ de baixo
						tmp[i][j]=estado[i+1][j];
						tmp[i+1][j]=0;
						return tmp;

					}
				}
				else if(i==1 && j==1 && estado[i][j]==0){
					if(a��o==0){
						//trocando c/ da esquerda
						tmp[i][j]=estado[i][j-1];
						tmp[i][j-1]=0;
						//System.out.println("Esquerda 2");
						return tmp;
					}
					else if(a��o==1){
						//Trocando c/ de cima
						tmp[i][j]=estado[i-1][j];
						tmp[i-1][j]=0;
						//System.out.println("Cima 2");
						return tmp;
					}
					else if(a��o==2){
						//trocando c/ da direita
						tmp[i][j]=estado[i][j+1];
						tmp[i][j+1]=0;
						return tmp;
					}
					else{
						//trocando c/ de baixo
						tmp[i][j]=estado[i+1][j];
						tmp[i+1][j]=0;
						return tmp;

					}

				}
				else if(i==1 && j==2 && estado[i][j]==0){
					if(a��o==0){
						//trocando c/ de cima
						tmp[i][j]=estado[i-1][j];
						tmp[i-1][j]=0;
						return tmp;

					}
					else if(a��o==1){
						//trocando c/ da esquerda
						tmp[i][j]=estado[i][j-1];
						tmp[i][j-1]=0;
						return tmp;

					}
					else{
						//trocando c/ de baixo
						tmp[i][j]=estado[i+1][j];
						tmp[i+1][j]=0;
						return tmp;
					}
				}
				else if(i==2 && j==0 && estado[i][j]==0){
					if(a��o==0){
						//trocando c/ de cima
						tmp[i][j]=estado[i-1][j];
						tmp[i-1][j]=0;
						return tmp;
					}
					else{
						//trocando c/ da direita
						tmp[i][j]=estado[i][j+1];
						tmp[i][j+1]=0;
						return tmp;

					}
				}
				else if(i==2 && j==1 && estado[i][j]==0){
					if(a��o==0){
						//trocando c/ da esquerda
						tmp[i][j]=estado[i][j-1];
						tmp[i][j-1]=0;
						//System.out.println("Esquerda 2");
						return tmp;
					}
					else if(a��o==1){
						//Trocando c/ de cima
						tmp[i][j]=estado[i-1][j];
						tmp[i-1][j]=0;
						//System.out.println("Cima 2");
						return tmp;
					}
					else{
						//trocando c/ da direita
						tmp[i][j]=estado[i][j+1];
						tmp[i][j+1]=0;
						return tmp;
					}
				}
				else if(i==2 && j==2 && estado[i][j]==0){
					if(a��o==0){
						//Trocando c/ de cima
						tmp[i][j]=estado[i-1][j];
						tmp[i-1][j]=0;
						//System.out.println("Cima 2");
						return tmp;
					}
					else{
						//trocando c/ da esquerda
						tmp[i][j]=estado[i][j-1];
						tmp[i][j-1]=0;
						//System.out.println("Esquerda 2");
						return tmp;
					}


				}

			}
		}
		System.err.println("Nao deveria \nN�o deveria");
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
		System.out.println("A��o: "+noh.getA��o());
		System.out.println("Custo Caminho: "+noh.getCustoCaminho());
		System.out.println("Profundidade: "+noh.getProfundidade());
		if(noh.getNohPai()!=null){System.out.println("N� pai: "+noh.getNohPai().getEtiqueta());}
		else {System.out.println();}
		if(noh.getEstado()!=null){
			System.out.println("Estado");
			for (int i = 0; i < noh.getEstado().length; i++) {
				for (int j = 0; j < noh.getEstado().length; j++) {
					System.out.print(noh.getEstado()[i][j]+"");
				}
				System.out.println("");
			}
		}


	}


}
