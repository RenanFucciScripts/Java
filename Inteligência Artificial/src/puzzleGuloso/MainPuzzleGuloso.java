package puzzleGuloso;

import java.util.LinkedList;
import java.util.Queue;

public class MainPuzzleGuloso{

	public static void main(String[] args) {
		PuzzleGuloso puz = new PuzzleGuloso();
		/*
		int[][] problema={{1,0,2},
						  {4,5,3},
						  {7,8,6}};
		
		int[][] solu={{1,2,3},
				   	  {4,5,6},
					  {7,8,0}};
		*/
		
		int[][] problema={{0,1},
						  {3,2}};
		
		int[][] solu={{1,2},
			   	      {3,0}};
	
		puz.setSolução(solu);
		
		Queue<Noh> fila= new LinkedList<Noh>();
	
		puz.buscaEmLargura(problema, fila);
		
	/*
		int[][] esta= {{1,0},
						{3,2}};
		
		Noh b= new Noh("D");
		b.setCustoCaminho(0);
		b.setProfundidade(1);
		b.setEstado(esta);
		
		fila=puz.expandir(b);
		
		puz.imprimirFila(fila);
		
	*/	
	}
	
		
		
}
		
				
		/*
		 * Teste da lista
		 
		int[] conjunto={5,0,8};
		fila=puz.inserirTodos(conjunto, fila);
		puz.imprimirFila(fila);
		fila=puz.primeiro(fila);
		puz.imprimirFila(fila);
		fila=puz.removerPrimeiro(fila);
		puz.imprimirFila(fila);
		fila=puz.removerPrimeiro(fila);
		puz.imprimirFila(fila);
		fila=puz.removerPrimeiro(fila);
		puz.imprimirFila(fila);
		fila=puz.removerPrimeiro(fila);
		puz.imprimirFila(fila);
		
		*/
		
