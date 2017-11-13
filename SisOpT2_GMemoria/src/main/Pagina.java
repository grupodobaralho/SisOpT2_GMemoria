package main;

public class Pagina {
	
	private String idPagina;
	private int deslocamento;
	
	public Pagina(String idPagina, int deslocamento) {
		this.idPagina = idPagina;
		this.deslocamento = deslocamento;
	}

	public String getIdPagina() {
		return idPagina;
	}

	public void setIdPagina(String idPagina) {
		this.idPagina = idPagina;
	}

	public int getDeslocamento() {
		return deslocamento;
	}

	public void setDeslocamento(int deslocamento) {
		this.deslocamento = deslocamento;
	}
	
}
