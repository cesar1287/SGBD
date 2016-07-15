import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TR_Manager extends Lock_Manager{
	//criando a matriz de adjac�ncias para representar o grafo direcionado
	static int grafo[][] = new int[6][6];
	//criando o HashMap que vai permitir criar infinitas transa��es sobre o grafo
	static Map<String,Integer> transa�oes = new HashMap<>();
	//timestamp, incrementado todas vez que uma transa��o � criada
	
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
	
	//classe para printar o nome da transa��o e o estado corrente dela
	public static void listarEstadoTransacao(){
		System.out.println("Nome da transa��o - Estado");
		int valor;
		for(String key: transa�oes.keySet()){
			valor = transa�oes.get(key);
			switch(valor){
			case -1:
				System.out.println(key + " - Transa��o ainda n�o iniciada");
				break;
			case 0:
				System.out.println(key + " - TR_Iniciada");
				break;
			case 1:
				System.out.println(key + " - Ativa");
				break;
			case 2:
				System.out.println(key + " - Processo Efetiva��o");
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
	
	//classe para verificar se eu posso aplicar o evento digitado pelo usu�rio na transa��o que ele escolheu
	public static void listarEventosGrafo(String transacao, String evento){
		
		int vertice = transa�oes.get(transacao);
		String item;
		@SuppressWarnings("resource")
		Scanner s = new Scanner(System.in);
		switch(evento){
			case "tr_begin":
				transa�oes.put(transacao, 0);
				System.out.println("Evento aplicado com sucesso\n");
				break;
			case "read":
				if(buscaGrafo(vertice, 1)==true){
					transa�oes.put(transacao, 1);
					System.out.println("Digite o nome do item que voc� gostaria de ler na transa��o "+transacao);
					item = s.next();
					LS(transacao, item);
					System.out.println("Evento aplicado com sucesso\n");
				}else{
					System.out.println("Passagem de evento inv�lida na transa��o\n");
				}
				break;
			case "write":
				if(buscaGrafo(vertice, 1)==true){
					transa�oes.put(transacao, 1);
					System.out.println("Digite o nome do item que voc� gostaria de escrever na transa��o "+transacao);
					item = s.next();
					LX(transacao, item);
					System.out.println("Evento aplicado com sucesso\n");
				}else{
					System.out.println("Passagem de evento inv�lida na transa��o\n");
				}
				break;
			case "tr_terminate":
				if(vertice==1){
					if(buscaGrafo(vertice, 2)==true){
						transa�oes.put(transacao, 2);
						System.out.println("Evento aplicado com sucesso\n");
					}else{
						System.out.println("Passagem de evento inv�lida na transa��o\n");
					}
				}else{
					System.out.println("Passagem de evento inv�lida na transa��o\n");
				}
				break;
			case "tr_rollback":
				if(vertice==2 || vertice==1){
					if(buscaGrafo(vertice, 3)==true){
						transa�oes.put(transacao, 3);
						System.out.println("Evento aplicado com sucesso\n");
						//opera��o cancelada, chama o m�todo liberarBloqueio para avaliar a fila bloquear o item para outra transa��o
						liberarBloqueio(transacao);
					}else{
						System.out.println("Passagem de evento inv�lida na transa��o\n");
					}
				}else{
					System.out.println("Passagem de evento inv�lida na transa��o\n");
				}
				break;
			case "tr_commit":
				if(vertice==2){
					if(buscaGrafo(vertice, 4)==true){
						transa�oes.put(transacao, 4);
						System.out.println("Evento aplicado com sucesso\n");
						//opera��o cancelada, chama o m�todo liberarBloqueio para avaliar a fila e bloquear o item para outra transa��o
						liberarBloqueio(transacao);
					}else{
						System.out.println("Passagem de evento inv�lida na transa��o\n");
					}
				}else{
					System.out.println("Passagem de evento inv�lida na transa��o\n");
				}
				break;
			case "tr_finish":
				if(vertice==3 || vertice==4){
					if(buscaGrafo(vertice, 5)==true){
						transa�oes.put(transacao, 5);
						System.out.println("Evento aplicado com sucesso\n");
					}else{
						System.out.println("Passagem de evento inv�lida na transa��o\n");
					}
				}else{
					System.out.println("Passagem de evento inv�lida na transa��o\n");
				}
				break;
			default:
				System.out.println("Evento inv�lido\n");
				break;
		}
	}
	public static void main(String[] args) {
		int opcao; 
		//timestamp = 0;
		String evento, transacao;
		
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
				String nomeTransacao;
				System.out.println("\nDigite o nome da transa��o:");
				nomeTransacao = s.next();
				transa�oes.put(nomeTransacao, -1);
				System.out.println("Transa��o criada com sucesso\n");
				//timestamp++;
				break;
			case 2:
				//verifica se a lista de transa��es est� vazia, caso afirmativo, da erro e volta para o menu
				//caso contr�rio continua a execu��o
				if(transa�oes.isEmpty()){
					System.out.println("\nN�o existe nenhuma transa��o no sistema, antes de usar esse menu, crie\n");
					break;
				}
				//chamar as transa��es correntes numa lista
				System.out.println("\nEscolha a transa��o que gostaria de usar na lista abaixo, digite o nome dela:\n");
				for(String key: transa�oes.keySet()){
					System.out.println(key);
				}
				transacao = s.next();
				if(transa�oes.containsKey(transacao)){
					System.out.println("\nDigite o evento para aplicar na transa��o");
					evento = s.next();
					listarEventosGrafo(transacao, evento);
					listarEstadoTransacao();
				}else{
					System.out.println("\nTransa��o n�o encontrada ou inv�lida, voltando para o menu principal\n");
				}
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