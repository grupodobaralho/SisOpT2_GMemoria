package main;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * Trabalho 2 - Gerencia de Memoria
 * 
 * @author Israel Deorce Vieira Júnior (16104279-1)
 * @date 21/11/2017
 * @class Sistemas Operacionais
 * @professor Avelino Zorzo
 * 
 */
public class App {

	// Tipo de entrada, algoritmo de troca e tamanho fixo das paginas
	public static String tipoEntrada;
	public static String tipoAlgTroca;
	public static int tamPagina;

	// Memoria virtual e atributos de paginas
	public static Pagina[] memVirtual;
	public static int tamMemVirtual;
	public static int nPaginasDisp = 0;

	// Memoria física e atributos de frames
	public static Pagina[] memFisica;
	public static int tamMemFisica;
	public static int nFramesDisp = 0;

	// Lista de Processos no sistema e tabela de páginas
	public static Map<String, Processo> processos = new HashMap<>();
	public static Map<String, Pagina> tabelaDePaginas = new LinkedHashMap<>();

	/**
	 * O metodo main carrega o arquivo epossui um while com um switch case que
	 * decide qual acao deverah ser tomada, levando em consideracao o COMANDO lido
	 * no arquivo. O formado de cada instrucao deve ser: COMANDO IDPROCESSO VALOR
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// Escolha o arquivo a ser executado
		 String arquivo = "SEQ-LRU.txt";
		// String arquivo = "SEQ-ALE.txt";
		// String arquivo = "ALE-LRU.txt";
		// String arquivo = "ALE-ALE.txt";

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

			while (sc.hasNext()) {
				String comando = sc.next();
				String idProcesso = sc.next();
				int tamProcesso = Integer.parseInt(sc.next());
				switch (comando) {
				case "C":
					System.out.println("Cria Processo " + idProcesso + " " + tamProcesso);
					criaProcesso(idProcesso, tamProcesso);
					// Se o tipo de entrada for aleatorio, ele cria a Thread para o processo.
					if (tipoEntrada.equals("aleatorio")) {
						ThreadProcesso threadP = new ThreadProcesso("#Thread" + idProcesso, processos.get(idProcesso));
					}
					break;
				case "A":
					if (!tipoEntrada.equals("aleatorio")) {
						System.out.println("Acesso " + idProcesso + " " + tamProcesso);
						acessaMemoria(idProcesso, tamProcesso);
					}
					break;
				case "M":
					if (!tipoEntrada.equals("aleatorio")) {
						System.out.println("MemAloc " + idProcesso + " " + tamProcesso);
						alocaMemoria(idProcesso, tamProcesso);
					}
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

		// printa();
	}

	/**
	 * Metodo chamado quando se deseja criar um novo processo
	 * 
	 * @param idProcesso
	 *            (Identifica o novo processo)
	 * @param tamProcesso
	 *            (tamanho total inicial do novo processo)
	 */
	public synchronized static void criaProcesso(String idProcesso, int tamProcesso) {

		// Verifica se existe espaco para o novo processo
		int tamNeces = (int) (tamProcesso / (double) tamPagina);
		if (nFramesDisp < tamNeces && nPaginasDisp < tamNeces) {
			System.out.println("ERRO: Nao foi possivel adicionar processo " + idProcesso + ": Memoria cheia!");
			System.exit(0);
		}

		// Havendo espaco para o novo processo: ele é criado, incluido na tabela de
		// processos e o processo de criacao das paginas referentes eh iniciado.
		Processo proc = new Processo(idProcesso, tamProcesso);
		processos.put(proc.getId(), proc);
		criaPagina(idProcesso, tamProcesso);

	}

	/**
	 * Metodo chamado quando se deseja criar uma ou mais paginas para um determinado
	 * processo
	 * 
	 * @param idProcesso
	 *            (Identifica o processo que receberah as paginas)
	 * @param qntAloc
	 *            (tamanho total de bits que deverao ser distribuidos entre as
	 *            paginas)
	 */
	public synchronized static void criaPagina(String idProcesso, int qntAloc) {
		int contIdPagina = processos.get(idProcesso).getQntPag();
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
				if (indice == -1) {
					System.out.println("ERRO: Nao foi possivel alocar memoria para " + idProcesso + ": Memoria Cheia");
					System.exit(0);
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

	/**
	 * Metodo chamado quando se deseja acessar uma pagina especifica de um processo
	 * 
	 * @param idProcesso
	 *            (qual o processo que serah acessado)
	 * @param tamProcesso
	 *            (numero do bit do processo a ser acessado. O bit especifico e a
	 *            pagina serao calculados a partir deste parametro.
	 */
	public synchronized static void acessaMemoria(String idProcesso, int tamProcesso) {

		// Confere se o processo existe na memoria
		if (!processos.containsKey(idProcesso)) {
			System.out.println("ERRO: Processo " + idProcesso + " nao existe!");
			return;
		}

		// Confere se o a pagina existe na memoria
		if (processos.get(idProcesso).getTamProcesso() <= tamProcesso || tamProcesso < 0) {
			System.out.println("ERRO: Acesso a pagina inexistente: '" + tamProcesso + "' do processo: '" + idProcesso
					+ " cujo tamanho eh: " + processos.get(idProcesso).getTamProcesso());
			return;
		}

		// Constroi o id da pagina para acessa-la na tabela de paginas
		int nPagina = (int) Math.floor(tamProcesso / (double) tamPagina);
		String idPagina = idProcesso + nPagina;
		Pagina p = tabelaDePaginas.get(idPagina);

		// Se nao estiver na memoria fisica, é page fault e faz swap para memoria fisica
		if (!p.getBitResidencia()) {
			pageFault(p);
		}

		// Atualiza o contador LRU refente na Tabela, pois ela fora referenciada.
		p.setContadorLRU(p.getContadorLRU() + 1);

	}

	/**
	 * Metodo chamado quando se deseja alocar memoria extra para um determinado
	 * processo
	 * 
	 * @param idProcesso
	 *            (Identifica o processo que deverah receber memoria extra)
	 * @param qntExtra
	 *            (tamanho total de bits que deverao ser incrementados ao processo e
	 *            distribuidos entre as paginas. Se for necessario, outra pagina
	 *            serah criada.)
	 */
	public synchronized static void alocaMemoria(String idProcesso, int qntExtra) {
		// Confere se o processo existe na memoria
		if (!processos.containsKey(idProcesso)) {
			System.out.println("ERRO: Processo " + idProcesso + " nao existe!");
			return;
		}
		processos.get(idProcesso).aumentaTamProcesso(qntExtra);

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
		} else {
			criaPagina(idProcesso, qntExtra);
		}
		p.setContadorLRU(p.getContadorLRU() + 1);

	}

	/**
	 * Metodo chamado para sinalizar pageFault e realizar swap de paginas. Ele
	 * ocorre sempre que se tenta acessar uma pagina que esteja na memoria virtual e
	 * nao na memoria fisica.
	 * 
	 * @param p
	 *            (A pagina a ser transferida para a memoria fisica).
	 */
	public synchronized static void pageFault(Pagina p) {

		System.out
				.println("\n########### Page Fault da pagina " + p.getIdPagina() + "!! Estados antes do PageFault: \n");
		printa();

		Pagina pagEscolhida = null;

		// Na politica de substituicao do algoritmo LRU, ele escolherah a pagina da
		// tabela de paginas com o menor contador de acessos para ser substituida
		// (Tanenbaum - Sistemas Operacionais Modernos, pg. 162).
		if (tipoAlgTroca.equals("lru")) {
			int menorC = Integer.MAX_VALUE;
			for (Map.Entry<String, Pagina> entry : tabelaDePaginas.entrySet()) {
				Pagina entryValue = entry.getValue();
				if (entryValue.getBitResidencia()) {
					if (entryValue.getContadorLRU() < menorC) {
						menorC = entryValue.getContadorLRU();
						pagEscolhida = entryValue;
					}
				}
			}
		} else if (tipoAlgTroca.equals("aleatorio")) {
			// No aleatorio, utilizamos um gerador de numeros randomicos pra escolher uma
			// pagina da memoria fisica para ser feito swap com a pagina p.
			Random generator = new Random();
			while (pagEscolhida == null) {
				int i = generator.nextInt(memFisica.length);
				if (memFisica[i] != null)
					pagEscolhida = tabelaDePaginas.get(memFisica[i].getIdPagina());
			}
		} else
			System.out.println("ERRO: Erro na escolha do algoritmo de substituicao");

		// Se a pagina foi escolhida com sucesso, o swap eh feito passando a posicao
		// inicial na memoria da pagina escolhida como novo indice inicial da pagina p e
		// vice versa.
		if (pagEscolhida == null)
			System.out.println("ERRO: Pagina com Menor valor Cont nao encontrada! " + p.getIdPagina());
		else {
			p.swap(memFisica, memVirtual, pagEscolhida.getiFis()[0], pagEscolhida);
		}

		System.out.println("---Estados após o PageFault:\n");
		printa();
	}

	/**
	 * Metodo auxiliar para exibir o estado do programa no console. Em alguns pontos
	 * foram feitos tratamentos custosos das estruturas do programa, mas isso foi
	 * feito para permitir melhor visualização dos resultados.
	 */
	public synchronized static void printa() {
		List<Pagina> listaP = new ArrayList<>();
		tabelaDePaginas.forEach((key, value) -> {
			listaP.add(value);
		});

		String format = "%-30s%-30s%s%n";
		System.out.printf(format, "#Estado da memoria Fisica#", "\t\t#Tabela de Paginas#",
				"\t#Estado da memoria Virtual#");
		System.out.printf(
				"i-IDpag-MemF?-mAloc-ponteiro[0]  \ti-IDpag-MemF?-p[0]-cLRU         i-IDpag - MemF?-mAloc-ponteiro[0]\n");

		for (int i = 0, j = 0, k = 0; i < memFisica.length || j < listaP.size()
				|| k < memVirtual.length; i++, j++, k++) {

			String mFisica, tPaginas, mVirtual;

			if (i < memFisica.length) {
				if (memFisica[i] == null)
					mFisica = i + "->" + "VAZIO";
				else
					mFisica = i + "->" + memFisica[i];
			} else {
				mFisica = "";
			}
			if (j < listaP.size()) {
				int indiceMem = -1;
				if (listaP.get(j).getBitResidencia())
					indiceMem = listaP.get(j).getiFis()[0];
				else
					indiceMem = listaP.get(j).getiVirt()[0];
				tPaginas = j + "->" + listaP.get(j).getIdPagina() + "\t" + listaP.get(j).getBitResidencia() + "  "
						+ indiceMem + "  " + listaP.get(j).getContadorLRU();
			} else {
				tPaginas = "";
			}

			if (k < memVirtual.length) {
				if (memVirtual[i] == null)
					mVirtual = "\t" + i + "->" + "VAZIO";
				else
					mVirtual = "\t" + i + "->" + memVirtual[i];
			} else {
				mVirtual = "";
			}

			System.out.printf(format, mFisica, tPaginas, mVirtual);
		}
		System.out.println("\n");
	}

}
