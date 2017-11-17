package main;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * obj memoria principal, obj processos???
 * 
 * @author IsraelDeorce
 *
 *         A -> Acessa memória C -> Criar o processo M -> Aloca Memória
 */
public class App {

	private static String tipoEntrada;
	private static String tipoAlgTroca;

	private static int tamPagina;
	private static int tamMemFisica;
	private static int tamMemVirtual;

	private static int contMemVitual = 0;
	private static int nPaginasDisp = 0;
	private static Pagina[] memVirtual;

	private static int contMemFisica = 0;
	private static int nFramesDisp = 0;
	private static Pagina[] memFisica;

	private static Map<String, Processo> processos = new HashMap<>();
	private static Map<String, Pagina> tabelaDePaginas = new LinkedHashMap<>();

	public static void main(String[] args) {
		load("meuteste.txt");
		// System.out.println(processos);
		printa();

	}

	public static void load(String arquivo) {
		Path path = Paths.get(arquivo);
		try (Scanner sc = new Scanner(Files.newBufferedReader(path, Charset.forName("utf8")))) {
			tipoEntrada = sc.next();
			tipoAlgTroca = sc.next();
			tamPagina = Integer.parseInt(sc.next());
			tamMemFisica = Integer.parseInt(sc.next());
			tamMemVirtual = Integer.parseInt(sc.next());

			nPaginasDisp = tamMemVirtual / tamPagina;
			nFramesDisp = tamMemFisica / tamPagina;
			memVirtual = new Pagina[tamMemVirtual];
			memFisica = new Pagina[tamMemFisica];

			// System.out.println(tipoEntrada);
			// System.out.println(tipoAlgTroca);
			// System.out.println(tamPagina);
			// System.out.println(tamMemFis);
			// System.out.println(tamMemVirtual);

			while (sc.hasNext()) {
				String comando = sc.next();
				String idProcesso = sc.next();
				int tamProcesso = Integer.parseInt(sc.next());
				switch (comando) {
				case "C":
					criaProcesso(idProcesso, tamProcesso);
					break;
				case "A":
					acessaMemoria(idProcesso, tamProcesso);
					break;
				case "M":
					alocaMemoria(idProcesso, tamProcesso);
					break;
				default:
					System.out.println("Erro na seleção do comando");
				}
			}
		} catch (IOException e) {
			System.out.println("Erro de I/O na leitura do arquivo " + arquivo + ":");
			e.printStackTrace();
		} catch (Throwable e1) {
			System.out.println("A app apresentou o seguinte erro ao ler o arquivo " + arquivo + ":");
			e1.printStackTrace();
		}

	}

	private static void alocaMemoria(String idProcesso, int qntExtra) {
		// Confere se o processo existe na memoria
		if (!processos.containsKey(idProcesso)) {
			System.err.println("ERRO: Processo " + idProcesso + " nao existe!");
			return;
		}

		// Captura o tamanho disponivel da ultima pagina de um processo
		String idUltimaPagina = processos.get(idProcesso).getIdUltimaPag();
		Pagina p = tabelaDePaginas.get(idUltimaPagina);
		int espDisponivel = tamPagina - p.getmAloc();

		// Se houver espaco disponivel, aloca ateh o maximo e cria paginas extras se for
		// necessario
		if (espDisponivel > 0) {
			if (espDisponivel >= qntExtra) {
				p.alocaMemExtra(qntExtra, memFisica);
			} else {
				p.alocaMemExtra(espDisponivel, memFisica);
				qntExtra -= espDisponivel;
				criaPagina(idProcesso, qntExtra);
			}
		}

	}

	private static void acessaMemoria(String idProcesso, int tamProcesso) {
		// Confere se o processo existe na memoria
		if (!processos.containsKey(idProcesso)) {
			System.err.println("ERRO: Processo " + idProcesso + " nao existe!");
			return;
		}
		// Confere se o a pagina existe na memoria
		if (processos.get(idProcesso).getTamProcesso() <= tamProcesso || tamProcesso < 0) {
			System.err.println("ERRO: Acesso a pagina inexistente: '" + tamProcesso + "' do processo: '" + idProcesso
					+ " cujo tamanho eh: " + processos.get(idProcesso).getTamProcesso());
			return;
		}

		// Constroi o id da pagina para acessa-la na tabela de paginas
		int nPagina = (int) Math.floor(tamProcesso / (double) tamPagina);
		String idPagina = idProcesso + nPagina;
		Pagina p = tabelaDePaginas.get(idPagina);

		// Se nao estiver na memoria fisica, page fault e faz swap para memoria fisica 
		if (!p.getBitResidencia()) {
			pageFault(p);
		}
		//Atualiza o contador LRU refente na Tabela
		p.setContadorLRU(p.getContadorLRU() + 1);

	}
	
	private static void pageFault(Pagina p) {
		System.out.println("Page Fault");
		Pagina pComMenorValor = null;
		int menorC = Integer.MAX_VALUE;
		for (Map.Entry<String, Pagina> entry : tabelaDePaginas.entrySet()) {
			Pagina entryValue = entry.getValue();
			if (entryValue.getBitResidencia()) {
				if (entryValue.getContadorLRU() < menorC) {
					menorC = entryValue.getContadorLRU();
					pComMenorValor = entryValue;
				}
			}
		}
		if (pComMenorValor == null)
			System.err.println("ERRO: Pagina com Menor valor Cont nao encontrada! " + p.getIdPagina());
		else {
			p.swap(memFisica, memVirtual, pComMenorValor.getiFis()[0], pComMenorValor);
		}
	}

	private static void criaProcesso(String idProcesso, int tamProcesso) {

		// Verifica se ha espaco para o processo
		int tamNeces = (int) (tamProcesso / (double) tamPagina);
		if (nFramesDisp < tamNeces && nPaginasDisp < tamNeces) {
			System.err.println("Nao foi possivel adicionar processo " + idProcesso + ": Memoria cheia!");
			return;
		}
		Processo proc = new Processo(idProcesso, tamProcesso);
		processos.put(proc.getId(), proc);
		criaPagina(idProcesso, tamProcesso);

	}

	private static void criaPagina(String idProcesso, int qntAloc) {
		int contIdPagina = 0;
		int tamProcessoAux = qntAloc;
		// While, enquanto nao forem criadas todas paginas para o processo
		while (!(tamProcessoAux <= 0)) {

			// definindo quantos indices serao alocados na pagina e quantos
			// sobrarao
			tamProcessoAux -= tamPagina;
			int mAloc = tamProcessoAux;
			if (mAloc >= 0)
				mAloc = tamPagina;
			else
				mAloc = tamPagina + mAloc;

			// Pagina eh criada recebendo idPagina, tamanho e qnt memoria a ser alocada
			Pagina pag = new Pagina(idProcesso + contIdPagina, tamPagina, mAloc);

			// Verifica e nao permite criar pagina que jah exista
			if (tabelaDePaginas.containsKey(pag.getIdPagina())) {
				System.err.println("Tentou adicionar uma pagina que ja existe: " + pag.getIdPagina());
				System.exit(1);
			}

			// Adiciona a Pagina na tabela de paginas
			tabelaDePaginas.put(pag.getIdPagina(), pag);

			// Procura espaço na memoria fisica e virtual e aloca pagina
			// atualizando os parametros que informam espaco de memoria
			// disponivel
			int indice = -1;
			for (int i = 0; i < memFisica.length; i++) {
				if (memFisica[i] == null) {
					indice = i;
					break;
				}
			}
			// System.out.println("Indice: "+indice);
			if (indice == -1) {
				for (int i = 0; i < memVirtual.length; i++) {
					if (memVirtual[i] == null) {
						indice = i;
						break;
					}
				}
				pag.adicionaMemoriaVirtual(indice, memVirtual);
				pageFault(pag);
				nPaginasDisp--;
			} else {
				pag.adicionaMemoriaFisica(indice, memFisica);
				nFramesDisp--;
			}

			processos.get(idProcesso).addPagina(pag.getIdPagina());
			contIdPagina++;
		}
	}

	public static void printa() {
		System.out.println("### Estado da memoria Fisica ###");
		System.out.println("Indice - idFrame - naMemFisica? - memEmUsoDoFrame");
		for (int i = 0; i < memFisica.length; i++) {
			if (memFisica[i] == null)
				System.out.println(i + "->" + "VAZIO");
			else
				System.out.println(i + "->" + memFisica[i]);
		}
		System.out.println("\n### Estado da memoria Virtual ###");
		System.out.println("Indice - idPagina - naMemFisica? - memEmUsoDaPagina");
		for (int i = 0; i < memVirtual.length; i++) {
			if (memVirtual[i] == null)
				System.out.println(i + "->" + "VAZIO");
			else
				System.out.println(i + "->" + memVirtual[i]);
		}
		System.out.println("\n### Tabela de Paginas ###");
		System.out.println("idPagina - naMemFisica? - indice - cLRU");
		tabelaDePaginas.forEach((key, value) -> {
			int indiceMem = -1;
			if (value.getBitResidencia())
				indiceMem = value.getiFis()[0];
			else
				indiceMem = value.getiVirt()[0];
			System.out.println(value.getIdPagina() + " " + value.getBitResidencia() + " " + indiceMem + " "
					+ value.getContadorLRU());
		});
	}

}
