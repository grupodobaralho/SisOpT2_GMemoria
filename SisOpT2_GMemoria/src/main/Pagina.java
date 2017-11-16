package main;

public class Pagina {

	// Identifica o processo referente
	private String idProcesso;
	// id da pagina
	private String idPagina;
	// indices para frames da mem. Fisica
	private int[] iFis;
	// indices para frames da mem Virtual
	private int[] iVirt;
	// o quanto esta pagina estah ocupada
	private int mAloc;
	// Esta na Mem. Principal ou nao?
	public boolean bitResidencia;
	// o que tiver o menor contador eh substituido
	public int contadorLRU;
	
	public Pagina() {
	}

	public Pagina(String idProcesso, String idPagina, int tamPagina, int mAloc) {
		this.idProcesso = idProcesso;
		this.idPagina = idPagina;
		this.iFis = new int[tamPagina];
		this.iVirt = new int[tamPagina];
		this.mAloc = mAloc;
		this.bitResidencia = false;
		this.contadorLRU = 0;
	}
	
	public class Empty extends Pagina{		
		String idPagina;
		boolean bitResidencia;
		public Empty(String idPagina, boolean bitResidencia) {
			this.idPagina=idPagina;
			this.bitResidencia=bitResidencia;
		}		
		public String toString() {
			return idPagina+ " " + bitResidencia + " "+"VAZIO";
		}
		
	}

	public void adicionaMemoriaFisica(int indice, Pagina[] memoriaFisica) {
		bitResidencia = true;
		for (int i = 0; i < iFis.length; i++) {
			if (i < mAloc)
				memoriaFisica[indice + i] = this;
			else
				memoriaFisica[indice + i] = new Empty(idPagina, bitResidencia);

			iFis[i] = indice + i;
		}
	}

	public void adicionaMemoriaVirtual(int indice, Pagina[] memoriaVirtual) {
		bitResidencia = false;
		for (int i = 0; i < iVirt.length; i++) {
			if (i < mAloc)
				memoriaVirtual[indice + i] = this;
			else
				memoriaVirtual[indice + i] = new Empty(idPagina, bitResidencia);
			iVirt[i] = indice + i;
		}
	}

	public void alocaExtra(int mAlocExtra, Pagina[] memoria) {
		if(bitResidencia) {
			for(int i=mAloc; i<mAlocExtra; i++) {
				iFis[i] = iFis[mAloc-1] +1;
				memoria[iFis[i]] = this;
			}
		} else{
			for(int i=mAloc; i<mAlocExtra; i++) {
				iVirt[i] = iVirt[mAloc-1] +1;
				memoria[iVirt[i]] = this;	
			}
		}
		mAloc += mAlocExtra;		
	}

	public String getProcesso() {
		return idProcesso;
	}

	public void setProcesso(String idProcesso) {
		this.idProcesso = idProcesso;
	}

	public String getIdPagina() {
		return idPagina;
	}

	public void setIdPagina(String idPagina) {
		this.idPagina = idPagina;
	}
	
	public String toString() {
		return idPagina+ " " +bitResidencia+ " "+mAloc;
	}

}
