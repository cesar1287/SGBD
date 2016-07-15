import java.util.Scanner;

public class Transaction{
	static int grafo[][] = new int[6][6];
	static int vertice;
	
	//classe para buscar no grafo
	public static boolean buscaGrafo(String transacao, int verticeAtual, int verticeProx){
		if(grafo[verticeAtual][verticeProx]==1){
			return true;
		}else{
			return false;
		}
	}
	
	public static void listaEventosGrafo(String evento){
		switch(evento){
			case "tr_begin":
				vertice = 0;
				break;
			case "read":
				if(buscaGrafo(null, vertice, 1)==true){
					vertice = 1;
					System.out.println("Evento aplicado com sucesso");
				}else{
					System.out.println("Passagem de evento inv�lida na transa��o");
				}
				break;
			case "write":
				if(buscaGrafo(null, vertice, 1)==true){
					vertice = 1;
					System.out.println("Evento aplicado com sucesso");
				}else{
					System.out.println("Passagem de evento inv�lida na transa��o");
				}
				break;
			case "tr_terminate":
				if(vertice==1){
					if(buscaGrafo(null, vertice, 2)==true){
						vertice = 2;
						System.out.println("Evento aplicado com sucesso");
					}else{
						System.out.println("Passagem de evento inv�lida na transa��o");
					}
				}else{
					System.out.println("Passagem de evento inv�lida na transa��o");
				}
				break;
			case "tr_rollback":
				if(vertice==2 || vertice==1){
					if(buscaGrafo(null, vertice, 3)==true){
						vertice = 3;
						System.out.println("Evento aplicado com sucesso");
					}else{
						System.out.println("Passagem de evento inv�lida na transa��o");
					}
				}else{
					System.out.println("Passagem de evento inv�lida na transa��o");
				}
				break;
			case "tr_commit":
				if(vertice==2){
					if(buscaGrafo(null, vertice, 4)==true){
						vertice = 4;
						System.out.println("Evento aplicado com sucesso");
					}else{
						System.out.println("Passagem de evento inv�lida na transa��o");
					}
				}else{
					System.out.println("Passagem de evento inv�lida na transa��o");
				}
				break;
			case "tr_finish":
				if(vertice==3 || vertice==4){
					if(buscaGrafo(null, vertice, 5)==true){
						vertice = 5;
						System.out.println("Evento aplicado com sucesso");
					}else{
						System.out.println("Passagem de evento inv�lida na transa��o");
					}
				}else{
					System.out.println("Passagem de evento inv�lida na transa��o");
				}
				break;
		}
	}
	public static void main(String[] args) {
		//criando a matriz de adjac�ncias para representar o grafo direcionado
		int opcao;
		String evento;
		
		Scanner s = new Scanner(System.in);
		
		//povoando a matriz de adjc�ncias com as arestas do grafo
		//a l�gica � simples, 1 - � aresta, 0 - n�o � aresta
		grafo[0][1] = 1;
		grafo[1][1] = 1;
		grafo[1][2] = 1;
		grafo[1][3] = 1;
		grafo[2][3] = 1;
		grafo[2][4] = 1;
		grafo[3][5] = 1;
		grafo[4][5] = 1;
		
		System.out.println("Escolha uma das op��es para continuar:");
		System.out.println("1 - Criar transa��o");
		System.out.println("2 - Usar transa��o");
		System.out.println("0 - Sair");
		
		opcao = s.nextInt();
		
		while(opcao!=0){
			switch (opcao) {
			case 1:
				System.out.println("Transa��o criada com sucesso");
				break;
			case 2:
				System.out.println("Escolha a transa��o que gostaria de usar na lista abaixo, digite o n�mero dela:");
				//chamar as transa��es correntes numa lista
				System.out.println();
				System.out.println("Digite o evento para aplicar na transa��o");
				evento = s.next();
				listaEventosGrafo(evento);
				break;
			}
			
			System.out.println("Escolha uma das op��es para continuar:");
			System.out.println("1 - Criar transa��o");
			System.out.println("2 - Usar transa��o");
			System.out.println("0 - Sair");
			
			opcao = s.nextInt();
		}
		s.close();
	}
}