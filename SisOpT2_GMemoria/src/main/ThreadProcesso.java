package main;

import java.util.Random;

/**
 * Trabalho 2 - Gerencia de Memoria
 * 
 * @author Israel Deorce Vieira Júnior (16104279-1)
 * @date 21/11/2017
 * @class Sistemas Operacionais
 * @professor Avelino Zorzo
 * 
 */
public class ThreadProcesso implements Runnable {
	
	private Processo processo;
	//private static boolean emUso = false;
	
	private String nome;
	public ThreadProcesso(String nome, Processo processo) {
		System.out.println("Eta nome: "+nome + " Eta proc: "+ processo.getId());
		this.nome = nome;
		this.processo = processo;
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {	
		Random generator = new Random();
		int randomNum;
		while(true){
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			randomNum = generator.nextInt((10 - 1) + 1) + 1;
			if(randomNum !=10) {
				randomNum = generator.nextInt((processo.getTamProcesso() - 0) + 1);
				App.acessaMemoria(processo.getId(), randomNum);
				System.out.println("T Processo "+nome + " "+processo.getId()+" iniciou acesso a memoria " + randomNum);
			} else {
				App.alocaMemoria(processo.getId(), 4);
				System.out.println("T Processo "+nome + " "+processo.getId()+" iniciou alocação de memoria +1");
			}
			App.printa();
		}
	}

}
