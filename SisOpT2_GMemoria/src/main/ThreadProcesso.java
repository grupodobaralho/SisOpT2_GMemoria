package main;

import java.util.Random;

/**
 * Trabalho 2 - Gerencia de Memoria
 * 
 * @author Israel Deorce Vieira J�nior (16104279-1)
 * @email israel.deorce@acad.pucrs.br
 * @date 21/11/2017
 * @class Sistemas Operacionais
 * @professor Avelino Zorzo
 * 
 *            #Resumo do Programa:
 * 
 *            Este programa � uma alternativa de implementa��o para o exerc�cio
 *            proposto no segundo trabalho da disciplina de Sistemas
 *            Operacionais. O exerc�cio envolve o desenvolvimento de um programa
 *            que simule um gerenciador de mem�ria utilizando t�cnica de
 *            pagina��o, com algoritmos LRU (Menos Recentemente Utilizado) e
 *            aleat�rio como algoritmos de substitui��o de p�ginas.
 *            Desenvolvemos a solu��o proposta utilizando a linguagem de
 *            programa��o Java orientada a objetos, com auxilio de uma estrutura
 *            de tabela de p�ginas. O enunciado do trabalho, os arquivos de
 *            teste e o artigo que descreve melhor o problema e a solu��o, est�o
 *            todos disponiveis neste projeto.
 *            
 * Os resultados do programa foram anexados em arquivos nomeanos "SAIDA-X-Y.txt".
 * 
 */
public class ThreadProcesso implements Runnable {

	private String nome;
	private Processo processo;

	/**
	 * Esta classe representa uma Thread de um processo. Ela recebe um nome e o
	 * processo em si, e execita
	 * 
	 * @param nome
	 * @param processo
	 */
	public ThreadProcesso(String nome, Processo processo) {
		this.nome = nome;
		this.processo = processo;
		Thread t = new Thread(this);
		t.start();
		try {
			t.join(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * O Metodo Run eh o metodo que indica a a��o da Thread no programa. No nosso
	 * caso, ele faz a Thread entrar em um loop que fica sorteando um valor de 1 a
	 * 10. Se o valor sorteado for 10 (10% de chance), ele irah alocar mais 1 de
	 * memoria para o processo, se for outro numero, ele irah acessar uma pagina
	 * randomica do processo na memoria (90% de chance).
	 */
	@Override
	public void run() {
		Random generator = new Random();
		int randomNum;
		while (true) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			randomNum = generator.nextInt((10 - 1) + 1) + 1;
			if (randomNum != 10) {
				randomNum = generator.nextInt((processo.getTamProcesso() - 0));
				App.acessaMemoria(processo.getId(), randomNum);
				System.out.println(
						"T Processo " + nome + " " + processo.getId() + " iniciou acesso a memoria " + randomNum);
			} else {
				App.alocaMemoria(processo.getId(), 1);
				System.out.println("T Processo " + nome + " " + processo.getId() + " iniciou aloca��o de memoria +1");
			}
			// App.printa();
		}
	}

}
