package main;

import java.util.HashMap;
import java.util.Map;

public class TabelaDePaginas {
	
    private static final class Componentes {

        public 
        public boolean bitResidencia;	//Esta na Mem. Principal ou nao?
        public boolean referencia; 		//foi referenciada ultimamente?
        public int contadorLRU;			//o que tiver o menor contador eh substituido

        public Componentes(Integer element, Integer level) {
        	this.contadorLRU = 0;
            this.element = element;
            this.level = level;
        }
    }
    
    Map<String, Componente> tabelaDePaginas;
	
	public TabelaDePaginas() {
		tabelaDePaginas = new HashMap<>();
	}
	

}
