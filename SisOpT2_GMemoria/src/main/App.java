package main;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
	// o tamPagina eh double para facilitar arredondamento para cima apos divisao
	// matematica
	private static int tamPagina;
	private static int tamMemFisica;
	private static int tamMemVirtual;
	private static String idProcesso;

	private static int contMemVitual = 0;
	private static int nPaginasDisp = 0;
	private static Pagina[] memVirtual;

	private static int contMemFisica = 0;
	private static int nFramesDisp = 0;
	private static Pagina[] memFisica;

	private static List<Processo> processos = new ArrayList<>();
	private static Map<String, Pagina> tabelaDePaginas = new HashMap<>();

	public static void main(String[] args) {
		load("teste2.txt");
		//System.out.println(processos);
		printa();

	}

	// Método que chama o Sistema Operacional para fazer a leitura dos dados do
	// arquivo .txt
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

	private static void alocaMemoria(String idProcesso, int tamProcesso) {

		// System.out.println(idProcesso + " " + tamProcesso);
		// System.out.println(quantPaginas);

	}

	private static void acessaMemoria(String idProcesso, int tamProcesso) {
		// System.out.println(idProcesso + " " + tamProcesso);

	}

	private static void criaProcesso(String idProcesso, int tamProcesso) {

		int tamNeces = (int) (tamProcesso / (double) tamPagina);
		// Verifica se ha espaco para o processo
		System.out.println(nFramesDisp + " " + tamNeces + " " + nPaginasDisp + " " + tamNeces);
		if (nFramesDisp < tamNeces && nPaginasDisp < tamNeces) {
			System.err.println("Nao foi possivel adicionar processo " + idProcesso + ": Memoria cheia!");
			System.exit(1);
		}

		Processo proc = new Processo(idProcesso, tamProcesso);
		int contIdPagina = 1;
		int tamProcessoAux = tamProcesso;
		//While, enquanto nao forem criadas todas paginas para o processo
		while (!(tamProcessoAux <= 0)) {
			
			//definindo quantos indices serao alocados na pagina e quantos sobrarao
			tamProcessoAux -= tamPagina;
			System.out.println("AAAAAAAAAAAA " +tamProcessoAux);
			int mAloc = tamProcessoAux;
			if (mAloc >= 0)
				mAloc = tamPagina;
			else
				mAloc = tamPagina + mAloc;	
			
			//Pagina eh criada recebendo as informacoes (incluindo qunto de seu espaço sera usado)
			Pagina pag = new Pagina(idProcesso, idProcesso + contIdPagina, tamPagina, mAloc);

			//Verifica e nao permite criar pagina que jah exista
			if (tabelaDePaginas.containsKey(pag.getIdPagina())) {
				System.err.println("Tentou adicionar uma pagina que ja existe: " + pag.getIdPagina());
				System.exit(1);
			}
			
			//Adiciona a Pagina na tabela de paginas
			tabelaDePaginas.put(pag.getIdPagina(), pag);
			
			
			//
			int indice = -1;
			for (int i = 0; i < memFisica.length; i++) {
				if (memFisica[i] == null) {
					indice = i;
					break;
				}
			}
			if (indice == -1) {				
				for (int i = 0; i < memVirtual.length; i++) {
					if (memVirtual[i] == null) {
						indice = i;
						break;
					}
				}
			}
			pag.adicionaMemoriaFisica(indice, memFisica);
			proc.addPagina(pag.getIdPagina());
			contIdPagina++;
		}
		processos.add(proc);
		//System.out.println(proc);

	}
	
	public static void printa() {
		for(int i=0; i<memFisica.length; i++) {
			if(memFisica[i]==null)
				System.out.println(i + "->" + "VAZIO");
			else
				System.out.println(i + "->" + memFisica[i]);
		}
		System.out.println();
		for(int i=0; i<memVirtual.length; i++) {
			if(memVirtual[i]==null) 
				System.out.println(i + "->" + "VAZIO");
			else				
				System.out.println(i + "->" + memVirtual[i]);			
		}
	}

}
