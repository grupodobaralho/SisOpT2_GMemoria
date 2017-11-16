package main;

import java.util.ArrayList;
import java.util.List;

public class Processo {

	private String id;
	private int tamProcesso;
	private List<String> paginas;

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
	
	public String getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return "Processo [id=" + id + ", tamProcesso=" + tamProcesso + ", paginas=" + paginas.size() + "]";
	}
}
