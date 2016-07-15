import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Lock_Manager {
	
	//lista FIFO usada para guardar as transações 
	//esperando para serem feitas o bloqueio
	static Queue<String> filaItem;
	//HashMap para guardar infinitas listas para quantos
	//itens o usuário desejar
	static Map<String,Queue<String>> mapItens = new HashMap<>();
	static Map<String, Map<String,String>> lockTable = new HashMap<>();
	static Map<String, String> itemLock = new HashMap<>();
	
	public static void LockTable(String transacao, String item, String modo){
		
		Map<String, String> itemModo;
		String transacaoBloqueio;
		Queue<String> waitQ;
		
		if(itemLock.get(item)==null){
			if(mapItens.get(item)==null){
				
				filaItem = new LinkedList<>();
				mapItens.put(item, filaItem);
				
				//conceder bloqueio pedido
				if(lockTable.get(transacao)==null){
					
					itemModo = new HashMap<>();
					lockTable.put(transacao, itemModo);
					
					itemModo = lockTable.get(transacao);
					itemModo.put(item, modo);
					lockTable.put(transacao, itemModo);
					itemLock.put(item, modo);
				}else{
					
					itemModo = lockTable.get(transacao);
					itemModo.put(item, modo);
					lockTable.put(transacao, itemModo);
					itemLock.put(item, modo);
				}
			}
		}else if(itemLock.get(item).equals("X")){
			
			System.out.println("Não é possível lockar esse item, pois ele está em modo de bloqueio exclusivo, adicionando a fila...");
			//adiciono a fila de pedidos desse objeto
			waitQ = mapItens.get(item);
			transacaoBloqueio = transacao+"/"+modo;
			waitQ.add(transacaoBloqueio);
			System.out.println("Adicionado");
		}else if(itemLock.get(item).equals("S")){
			
			filaItem = new LinkedList<>();
			mapItens.put(item, filaItem);
			
			//conceder bloqueio pedido
			if(lockTable.get(transacao)==null){
				
				itemModo = new HashMap<>();
				lockTable.put(transacao, itemModo);
				
				itemModo = lockTable.get(transacao);
				itemModo.put(item, modo);
				lockTable.put(transacao, itemModo);
				itemLock.put(item, modo);
			}else{
				
				itemModo = lockTable.get(transacao);
				itemModo.put(item, modo);
				lockTable.put(transacao, itemModo);
				itemLock.put(item, modo);
			}
		}
	}
	
	public static void liberarBloqueio(String transacao){
		
		//for each pela lockTable da transação que fez commit ou rollback
		Map<String, String> itemModo = lockTable.get(transacao);
		Queue<String> itemQueue = new LinkedList<>();
		String item;
		for(Map.Entry<String, String> kv: itemModo.entrySet()){
			if(kv.getValue().equals("X")){
				itemLock.remove(kv.getKey());
			}
		}
		//efetivamente exclui da tabela de lock e verifica a Wait_Q para permitir o bloqueio
		for(Map.Entry<String, String> kv: itemModo.entrySet()){
	        itemQueue = mapItens.get(kv.getKey());
	        item = kv.getKey();
	        if(itemQueue.peek()==null){
	        	System.out.println("Não existe ninguém na fila de bloqueio do item " + item);
	        }else{
	        	System.out.println("Existe alguém na fila de bloqueio do item " + item);
	    	    String[] transacaoModo = itemQueue.peek().split("/");
	    	    if(itemLock.get(item)==null){
	    		    itemModo = lockTable.get(transacaoModo[0]);
				    itemModo.put(item, transacaoModo[1]);
				    lockTable.put(transacao, itemModo);
				    itemLock.put(item, transacaoModo[1]);
	    	    }else if(itemLock.get(item).equals("S")){
	    	    	itemModo = lockTable.get(transacaoModo[0]);
				    itemModo.put(item, transacaoModo[1]);
				    lockTable.put(transacao, itemModo);
				    itemLock.put(item, transacaoModo[1]);
	    	    }
	        }
		}
		lockTable.remove(transacao);
	}
	
	public static void LS(String transacao, String item){
		
		LockTable(transacao, item, "S");
	}
	
	public static void LX(String transacao, String item){
		
		LockTable(transacao, item, "X");
	}

	public static void U(String transacao, String item){
		
		Map<String, String> itemLockTable ;
		
		itemLockTable = lockTable.get(transacao);
		itemLockTable.remove(item);
		lockTable.put(transacao, itemLockTable);
	}
}
