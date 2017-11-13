package main;

import java.util.ArrayList;
import java.util.List;

public class Processo {
	
	private String id;
	private int tamanho;
	private List<Pagina> paginas;
	
	public Processo(String id, int tamanho) {		
		this.id = id;
		this.tamanho = tamanho;
		this.paginas = new ArrayList<>();
	}
	
	public void inserePagina(Pagina p) {
		paginas.add(p);
	}
	
	public void removePagina(Pagina p) {
		paginas.remove(p);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Pagina> getPaginas() {
		return paginas;
	}

}
