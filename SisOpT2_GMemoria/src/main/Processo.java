package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
public class Processo {

	private String id;
	private int tamProcesso;
	private List<String> paginas;

	/**
	 * Um processo possui um ID, um tamanho total e uma lista para identificar as
	 * suas paginas referentes.
	 * 
	 * @param id
	 * @param tamProcesso
	 */
	public Processo(String id, int tamProcesso) {
		this.id = id;
		this.tamProcesso = tamProcesso;
		paginas = new ArrayList<>();
	}

	public void addPagina(String idPagina) {
		paginas.add(idPagina);
	}

	public int getQntPag() {
		return paginas.size();
	}

	public String getIdUltimaPag() {
		return paginas.get(paginas.size() - 1);
	}

	public String getId() {
		return id;
	}

	public int getTamProcesso() {
		return tamProcesso;
	}

	public void aumentaTamProcesso(int tamExtra) {
		this.tamProcesso += tamExtra;
	}

	@Override
	public String toString() {
		return "Processo [id=" + id + ", tamProcesso=" + tamProcesso + ", paginas=" + paginas.size() + "]";
	}
}
