package uniandes.dpoo.aerolinea.modelo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import uniandes.dpoo.aerolinea.exceptions.VueloSobrevendidoException;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;
import uniandes.dpoo.aerolinea.tarifas.CalculadoraTarifas;
import uniandes.dpoo.aerolinea.tiquetes.GeneradorTiquetes;
import uniandes.dpoo.aerolinea.tiquetes.Tiquete;

public class Vuelo {
	private String fecha;
	private Avion avion;
	private Ruta ruta;
	private Map<String, Tiquete> tiquetes;

	public Vuelo(Ruta ruta, String fecha, Avion avion) {
		this.fecha = fecha;
		this.avion = avion;
		this.ruta = ruta;
		this.tiquetes = new HashMap<String, Tiquete>();
	}

	public String getFecha() {
		return fecha;
	}

	public Avion getAvion() {
		return avion;
	}

	public Ruta getRuta() {
		return ruta;
	}

	public Collection<Tiquete> getTiquetes() {
		return tiquetes.values();
	}

	public int venderTiquetes(Cliente cliente, CalculadoraTarifas calculadora, int cantidad)
			throws VueloSobrevendidoException {
		if (tiquetes.size() + cantidad > avion.getCapacidad()) {
			throw new VueloSobrevendidoException(this);
		}

		int total = 0;
		for (int i = 0; i < cantidad; i++) {
			int tarifa = calculadora.calcularTarifa(this, cliente);
			Tiquete tiquete = GeneradorTiquetes.generarTiquete(this, cliente, tarifa);
			tiquetes.put(tiquete.getCodigo(), tiquete);
			total += tarifa;
		}
		return total;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj.getClass() != this.getClass()) {
			return false;
		}
		Vuelo otro = (Vuelo) obj;
		return fecha.equals(otro.getFecha())
				&& ruta.getCodigoRuta().equals(otro.getRuta().getCodigoRuta());
	}
}