package main;

/**
 * Trabalho 2 - Gerencia de Memoria
 * 
 * @author Israel Deorce Vieira Júnior (16104279-1)
 * @date 21/11/2017
 * @class Sistemas Operacionais
 * @professor Avelino Zorzo
 * 
 */
public class Pagina {

	// id da pagina
	private String idPagina;
	// indices para frames da mem. Fisica
	private int[] iFis;
	// indices para frames da mem Virtual
	private int[] iVirt;
	// O quanto esta pagina estah ocupada
	private int mAloc;
	// Esta na Mem. Principal ou nao?
	private boolean bitResidencia;
	// No LRU, o que tiver o menor contador serah substituido
	private int contadorLRU;

	/**
	 * Uma pagina pertence a um processo e compoe uma tabela de paginas que, para
	 * cada processo, reconhece se o mesmo estah na memoria fisica ou secundaria e
	 * qual eh o endereco correspondente. Alem disso, A pagina tambem guarda um
	 * contador que aumenta a cada referencia da pagina, para auxiliar na execucao
	 * do algoritmo de substituicao LRU.
	 */
	public Pagina() {
	}

	public Pagina(String idPagina, int tamPagina, int mAloc) {
		this.idPagina = idPagina;
		this.iFis = new int[tamPagina];
		this.iVirt = new int[tamPagina];
		this.mAloc = mAloc;
		this.bitResidencia = false;
		this.contadorLRU = 0;
	}

	/**
	 * Classe criada para auxiliar a identificar posicoes na memoria que pertencem a
	 * uma pagina, mas estao disponiveis (vazias).
	 * 
	 * @author Israelmp
	 *
	 */
	public class Empty extends Pagina {
		String idPagina;
		boolean bitResidencia;

		public Empty(String idPagina, boolean bitResidencia) {
			this.idPagina = idPagina;
			this.bitResidencia = bitResidencia;
		}

		public String getIdPagina() {
			return idPagina;
		}

		public String toString() {
			return idPagina + "\tVAZIO\t\t ";
		}

	}

	/**
	 * Metodo que recebe a memoria fisica e o indice inicial para alocar esta
	 * pagina.
	 * 
	 * @param indice
	 * @param memoriaVirtual
	 */
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

	/**
	 * Metodo que recebe a memoria virtual e o indice inicial para alocar esta
	 * pagina.
	 * 
	 * @param indice
	 * @param memoriaVirtual
	 */
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

	/**
	 * Metodo que recebe a memoria onde esta memoria esta alocada e uma quantidade
	 * inteira para alocar quantidade extra. Este metodo soh eh chamado se houver
	 * espaco livre para alocacao de memoria extra.
	 * 
	 * @param indice
	 * @param memoriaVirtual
	 */
	public void alocaMemExtra(int mAlocExtra, Pagina[] memoria) {
		if (bitResidencia) {
			for (int i = 0; i < mAlocExtra; i++) {
				memoria[iFis[mAloc + i]] = this;

			}
		} else {
			for (int i = 0; i < mAlocExtra; i++) {
				memoria[iVirt[mAloc + i]] = this;
			}
		}
		mAloc += mAlocExtra;
		contadorLRU++;
		
	}

	/**
	 * Realoca toda a pagina para a outra memoria
	 * 
	 * @param memFisica
	 *            (Referencia para a memoria Fisica)
	 * @param memVirtual
	 *            (Referencia para a memoria Virtual)
	 * @param novoIndice
	 *            (Novo indice para onde a pagina comecarah a ser realocada)
	 * @param outra
	 *            (A pagina que deverah assumir a posicao da memoria da atual)
	 */
	public void swap(Pagina[] memFisica, Pagina[] memVirtual, int novoIndice, Pagina outra) {

		int primeiroIndiceAtual;

		if (bitResidencia) {
			primeiroIndiceAtual = iFis[0];
			for (int i = 0; i < iFis.length; i++) {
				memVirtual[novoIndice + i] = memFisica[iFis[i]];
				memFisica[iFis[i]] = null;
				iVirt[i] = novoIndice + i;
			}
			bitResidencia = false;
			if (outra != null)
				outra.adicionaMemoriaFisica(primeiroIndiceAtual, memFisica);

		} else {
			primeiroIndiceAtual = iVirt[0];
			for (int i = 0; i < iVirt.length; i++) {
				memFisica[novoIndice + i] = memVirtual[iVirt[i]];
				memVirtual[iVirt[i]] = null;
				iFis[i] = novoIndice + i;
			}
			bitResidencia = true;
			if (outra != null)
				outra.adicionaMemoriaVirtual(primeiroIndiceAtual, memVirtual);
			contadorLRU++;
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

	public boolean getBitResidencia() {
		return bitResidencia;
	}

	public String getIdPagina() {
		return idPagina;
	}

	public void setIdPagina(String idPagina) {
		this.idPagina = idPagina;
	}

	public String toString() {
		String str = "";
		if (bitResidencia)
			str = idPagina + "\t" + bitResidencia + "\t" + mAloc + "\t " + iFis[0];
		else
			str = idPagina + "\t" + bitResidencia + "\t" + mAloc + "\t " + iVirt[0];
		return str;
	}

}
