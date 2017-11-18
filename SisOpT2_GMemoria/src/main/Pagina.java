package main;

public class Pagina {

	// id da pagina
	private String idPagina;
	// indices para frames da mem. Fisica
	private int[] iFis;
	// indices para frames da mem Virtual
	private int[] iVirt;
	// o quanto esta pagina estah ocupada
	private int mAloc;
	// Esta na Mem. Principal ou nao?
	private boolean bitResidencia;
	// o que tiver o menor contador eh substituido
	private int contadorLRU;
	
	public Pagina() {
	}

	public Pagina( String idPagina, int tamPagina, int mAloc) {
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
		public String getIdPagina() {
			return idPagina;
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

	public void alocaMemExtra(int mAlocExtra, Pagina[] memoria) {
		if(bitResidencia) {
			for(int i=0; i<mAlocExtra; i++) {			
				memoria[iFis[mAloc+i]] = this;
				
			}
		} else{
			for(int i=0; i<mAlocExtra; i++) {
				memoria[iVirt[mAloc+i]] = this;	
			}
		}
		mAloc =+ mAlocExtra;
	}
	
	//realoca todo o processo para a outra memoria
	public void swap(Pagina[] memFisica, Pagina[] memVirtual, int novoIndice, Pagina outra) {
		
		int primeiroIndiceAtual;
		
		if(bitResidencia) {
			primeiroIndiceAtual = iFis[0];
			for (int i = 0; i < iFis.length; i++) {
				memVirtual[novoIndice+i] = memFisica[iFis[i]];
				memFisica[iFis[i]] = null;
				iVirt[i] = novoIndice + i;
			}
			bitResidencia = false;
			if(outra !=null)
				outra.adicionaMemoriaFisica(primeiroIndiceAtual, memFisica);
			
		} else {
			primeiroIndiceAtual = iVirt[0];
			for (int i = 0; i < iVirt.length; i++) {
				memFisica[novoIndice+i] = memVirtual[iVirt[i]];
				memVirtual[iVirt[i]] = null;
				iFis[i] = novoIndice + i;
			}
			bitResidencia = true;
			if(outra !=null)
				outra.adicionaMemoriaVirtual(primeiroIndiceAtual, memVirtual);
		}
	}
	
	
	public int getContadorLRU() {
		return contadorLRU;
	}

	public void setContadorLRU(int contadorLRU) {
		this.contadorLRU = contadorLRU;
	}

	public void setBitResidencia(boolean bitResidencia) {
		this.bitResidencia = bitResidencia;
	}

	public int[] getiFis() {
		return iFis;
	}

	public void setiFis(int[] iFis) {
		this.iFis = iFis;
	}

	public int[] getiVirt() {
		return iVirt;
	}

	public void setiVirt(int[] iVirt) {
		this.iVirt = iVirt;
	}

	public int getmAloc() {
		return mAloc;
	}

	public void setmAloc(int mAloc) {
		this.mAloc = mAloc;
	}

	public boolean getBitResidencia(){
		return bitResidencia;
	}

	public String getIdPagina() {
		return idPagina;
	}

	public void setIdPagina(String idPagina) {
		this.idPagina = idPagina;
	}
	
	public String toString() {
		return idPagina+ " " +bitResidencia+ " "+mAloc + " Ivirt: "+iVirt[0]+ " "+ iFis[0];
	}

}
