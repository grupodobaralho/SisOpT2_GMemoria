package main;

public class MemoriaFisica {
	
	private int[] RAM;
	
	public MemoriaFisica(int tamanho) {
		RAM = new int[tamanho];
	}

	public int[] getRAM() {
		return RAM;
	}
	
	public int getPos(int i) {
		return RAM[i];
	}
	
	public void inserePos(int pos, int n) {
		RAM[pos] = n;
	}

}
