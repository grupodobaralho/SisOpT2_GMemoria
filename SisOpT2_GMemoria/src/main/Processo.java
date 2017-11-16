package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Processo {

	private String id;
	private int tamProcesso;
	private Set<String> paginas;

	public Processo(String id, int tamProcesso) {
		this.id = id;
		this.tamProcesso = tamProcesso;
		paginas = new HashSet<>();
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
	
	public int getTamProcesso(){
		return tamProcesso;
	}
	
	@Override
	public String toString() {
		return "Processo [id=" + id + ", tamProcesso=" + tamProcesso + ", paginas=" + paginas.size() + "]";
	}
}
