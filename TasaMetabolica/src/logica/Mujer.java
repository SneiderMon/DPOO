package logica;

public class Mujer extends Persona {
	
	public Mujer(String nombre, int edad, double peso, double altura) {
		super (nombre, edad, peso,altura);
	}


	@Override
	
	public boolean Restriccion() {
	
		return (peso >= 40 && peso <= 80) && (altura >= 140 && altura <= 180) && edad > 15;
	}
	
	@Override
	
	public double CalculoTMB() {
	
		return 447.593 + (9.247 * peso) + (3.098 * altura) - (4.33 * edad);
	}

}
