package puzzlelargura;

import java.util.LinkedList;
import java.util.Queue;

public class MainPuzzleLargura {

	public static void main(String[] args) {
		PuzzleLargura puz = new PuzzleLargura();
		
		/*int[][] problema={{1,2,3,4,5},
			   	  {6,7,8,9,10},
				  {11,12,13,14,15},
				  {16,17,18,0,20},
				  {21,22,23,19,24}};
		
		int[][] solu={{1,2,3,4,5},
				   	  {6,7,8,9,10},
					  {11,12,13,14,15},
					  {16,17,18,19,20},
					  {21,22,23,24,0}};
		*/
		/*int[][] problema={{1,2,3,4},
			   	  {5,6,7,8},
				  {9,10,0,12},
				  {13,14,11,15}};
		
		int[][] solu={{1,2,3,4},
				   	  {5,6,7,8},
					  {9,10,11,12},
					  {13,14,15,0}};*/
		
		int[][] problema={{1,2,3},
						  {4,5,6},
						  {7,0,8}};
		
		int[][] solu={{1,2,3},
				  {4,5,6},
				  {7,8,0}};
	
		
		
	/*	int[][] problema={{0,1},
						  {3,2}};
		
		int[][] solu={{1,2},
			   	      {3,0}};*/
	
		puz.setSolu��o(solu);
		
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
		
