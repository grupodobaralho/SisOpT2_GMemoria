package main;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * obj memoria principal, obj processos???
 * @author IsraelDeorce
 *
 *A -> Acessa memória
 *C -> Criar o processo
 *M -> Aloca Memória
 */
public class App {
	
	private static String tipoEntrada;
	private static String tipoAlgTroca;
	private static int tamPagina;
	private static int tamMemFis;
	private static int tamMemVirtual;
	
	private static String idProcesso;
	//private static 
	
	public static void main(String[] args) {
		
		
	}
	
	
	//Método que chama o Sistema Operacional para fazer a leitura dos dados do arquivo .txt
	public static void load(String arquivo) {
		Path path = Paths.get(arquivo);

		try (Scanner sc = new Scanner(Files.newBufferedReader(path, Charset.forName("utf8")))) {
//			nProcessos = Integer.parseInt(sc.next());
//			tamFatiaTempo = Integer.parseInt(sc.next());
//			int contProcessos = 0;
//			while (true) {
//				Processo p = new Processo(Integer.parseInt(sc.next()), Integer.parseInt(sc.next()),
//						Integer.parseInt(sc.next()), contProcessos + 1);
//				processos.add(p);
//				contProcessos++;
//			}
		} catch (IOException e) {
			System.out.println("FALHOU");
			e.printStackTrace();
		} catch (Throwable e1) {
			System.out.println("A app apresentou o seguinte erro:");
			e1.printStackTrace();
		}

	}

}
