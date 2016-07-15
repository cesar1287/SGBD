import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TR_Manager extends Lock_Manager{
	//criando a matriz de adjacências para representar o grafo direcionado
	static int grafo[][] = new int[6][6];
	//criando o HashMap que vai permitir criar infinitas transações sobre o grafo
	static Map<String,Integer> transaçoes = new HashMap<>();
	//timestamp, incrementado todas vez que uma transação é criada
	
	//classe para buscar no grafo
	public static boolean buscaGrafo(int verticeAtual, int verticeProx){
		try{
			if(grafo[verticeAtual][verticeProx]==1){
				return true;
			}else{
				return false;
			}
		}catch(ArrayIndexOutOfBoundsException e){
			return false;
		}
	}
	
	//classe para printar o nome da transação e o estado corrente dela
	public static void listarEstadoTransacao(){
		System.out.println("Nome da transação - Estado");
		int valor;
		for(String key: transaçoes.keySet()){
			valor = transaçoes.get(key);
			switch(valor){
			case -1:
				System.out.println(key + " - Transação ainda não iniciada");
				break;
			case 0:
				System.out.println(key + " - TR_Iniciada");
				break;
			case 1:
				System.out.println(key + " - Ativa");
				break;
			case 2:
				System.out.println(key + " - Processo Efetivação");
				break;
			case 3:
				System.out.println(key + " - Processo Cancelamento");
				break;
			case 4:
				System.out.println(key + " - Efetivada");
				break;
			case 5:
				System.out.println(key + " - TR_Finalizada");
				break;
			}
		}
		System.out.println();
	}
	
	//classe para verificar se eu posso aplicar o evento digitado pelo usuário na transação que ele escolheu
	public static void listarEventosGrafo(String transacao, String evento){
		
		int vertice = transaçoes.get(transacao);
		String item;
		@SuppressWarnings("resource")
		Scanner s = new Scanner(System.in);
		switch(evento){
			case "tr_begin":
				transaçoes.put(transacao, 0);
				System.out.println("Evento aplicado com sucesso\n");
				break;
			case "read":
				if(buscaGrafo(vertice, 1)==true){
					transaçoes.put(transacao, 1);
					System.out.println("Digite o nome do item que você gostaria de ler na transação "+transacao);
					item = s.next();
					LS(transacao, item);
					System.out.println("Evento aplicado com sucesso\n");
				}else{
					System.out.println("Passagem de evento inválida na transação\n");
				}
				break;
			case "write":
				if(buscaGrafo(vertice, 1)==true){
					transaçoes.put(transacao, 1);
					System.out.println("Digite o nome do item que você gostaria de escrever na transação "+transacao);
					item = s.next();
					LX(transacao, item);
					System.out.println("Evento aplicado com sucesso\n");
				}else{
					System.out.println("Passagem de evento inválida na transação\n");
				}
				break;
			case "tr_terminate":
				if(vertice==1){
					if(buscaGrafo(vertice, 2)==true){
						transaçoes.put(transacao, 2);
						System.out.println("Evento aplicado com sucesso\n");
					}else{
						System.out.println("Passagem de evento inválida na transação\n");
					}
				}else{
					System.out.println("Passagem de evento inválida na transação\n");
				}
				break;
			case "tr_rollback":
				if(vertice==2 || vertice==1){
					if(buscaGrafo(vertice, 3)==true){
						transaçoes.put(transacao, 3);
						System.out.println("Evento aplicado com sucesso\n");
						//operação cancelada, chama o método liberarBloqueio para avaliar a fila bloquear o item para outra transação
						liberarBloqueio(transacao);
					}else{
						System.out.println("Passagem de evento inválida na transação\n");
					}
				}else{
					System.out.println("Passagem de evento inválida na transação\n");
				}
				break;
			case "tr_commit":
				if(vertice==2){
					if(buscaGrafo(vertice, 4)==true){
						transaçoes.put(transacao, 4);
						System.out.println("Evento aplicado com sucesso\n");
						//operação cancelada, chama o método liberarBloqueio para avaliar a fila e bloquear o item para outra transação
						liberarBloqueio(transacao);
					}else{
						System.out.println("Passagem de evento inválida na transação\n");
					}
				}else{
					System.out.println("Passagem de evento inválida na transação\n");
				}
				break;
			case "tr_finish":
				if(vertice==3 || vertice==4){
					if(buscaGrafo(vertice, 5)==true){
						transaçoes.put(transacao, 5);
						System.out.println("Evento aplicado com sucesso\n");
					}else{
						System.out.println("Passagem de evento inválida na transação\n");
					}
				}else{
					System.out.println("Passagem de evento inválida na transação\n");
				}
				break;
			default:
				System.out.println("Evento inválido\n");
				break;
		}
	}
	public static void main(String[] args) {
		int opcao; 
		//timestamp = 0;
		String evento, transacao;
		
		Scanner s = new Scanner(System.in);
		
		//povoando a matriz de adjcências com as arestas do grafo
		//a lógica é simples, 1 - é aresta, 0 - não é aresta
		grafo[0][1] = 1;
		grafo[1][1] = 1;
		grafo[1][2] = 1;
		grafo[1][3] = 1;
		grafo[2][3] = 1;
		grafo[2][4] = 1;
		grafo[3][5] = 1;
		grafo[4][5] = 1;

		System.out.println("Escolha uma das opções para continuar:");
		System.out.println("1 - Criar transação");
		System.out.println("2 - Usar transação");
		System.out.println("0 - Sair");
		
		opcao = s.nextInt();
		
		while(opcao!=0){
			switch (opcao) {
			case 1:
				String nomeTransacao;
				System.out.println("\nDigite o nome da transação:");
				nomeTransacao = s.next();
				transaçoes.put(nomeTransacao, -1);
				System.out.println("Transação criada com sucesso\n");
				//timestamp++;
				break;
			case 2:
				//verifica se a lista de transações está vazia, caso afirmativo, da erro e volta para o menu
				//caso contrário continua a execução
				if(transaçoes.isEmpty()){
					System.out.println("\nNão existe nenhuma transação no sistema, antes de usar esse menu, crie\n");
					break;
				}
				//chamar as transações correntes numa lista
				System.out.println("\nEscolha a transação que gostaria de usar na lista abaixo, digite o nome dela:\n");
				for(String key: transaçoes.keySet()){
					System.out.println(key);
				}
				transacao = s.next();
				if(transaçoes.containsKey(transacao)){
					System.out.println("\nDigite o evento para aplicar na transação");
					evento = s.next();
					listarEventosGrafo(transacao, evento);
					listarEstadoTransacao();
				}else{
					System.out.println("\nTransação não encontrada ou inválida, voltando para o menu principal\n");
				}
				break;
			}
			
			System.out.println("Escolha uma das opções para continuar:");
			System.out.println("1 - Criar transação");
			System.out.println("2 - Usar transação");
			System.out.println("0 - Sair");
			
			opcao = s.nextInt();
		}
		s.close();
	}
}