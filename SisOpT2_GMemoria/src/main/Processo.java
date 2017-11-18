package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
