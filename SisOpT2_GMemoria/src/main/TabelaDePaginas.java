package main;

import java.util.HashMap;
import java.util.Map;

public class TabelaDePaginas {
	
    private static final class Componentes {
        
        public boolean bitResidencia;	//Esta na Mem. Principal ou nao?
        public int contadorLRU;			//o que tiver o menor contador eh substituido
        public int indiceNaMemoria;

        public Componentes() {
        	this.contadorLRU = 0;
        	this.bitResidencia = false;
        	this.indiceNaMemoria = -1;
        }
    }
    
    Map<String, Componentes> tabelaDePaginas;
	
	public TabelaDePaginas() {
		tabelaDePaginas = new HashMap<>();
	}
	

}
