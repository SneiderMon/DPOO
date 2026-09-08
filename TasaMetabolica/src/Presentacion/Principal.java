package Presentacion;

import logica.Persona;

import logica.Hombre;

import logica.Mujer;

public class Principal {

	public Principal() {
		
	Persona p;
	
	p = new Hombre("Carlos", 25, 80, 180);
	if (p.Restriccion()) {
    System.out.println("TMB:" + p.CalculoTMB()); }


    
    p = new Mujer("Ana", 30, 60, 165);
    if (p.Restriccion()) {
    System.out.println("TMB : " + p.CalculoTMB()); }

}

	public static void main(String[] args) {
	    new Principal();
}	
			
}
