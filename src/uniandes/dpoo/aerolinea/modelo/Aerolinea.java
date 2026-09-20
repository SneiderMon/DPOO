package uniandes.dpoo.aerolinea.modelo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uniandes.dpoo.aerolinea.exceptions.VueloSobrevendidoException;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;
import uniandes.dpoo.aerolinea.persistencia.CentralPersistencia;
import uniandes.dpoo.aerolinea.tarifas.CalculadoraTarifas;
import uniandes.dpoo.aerolinea.tarifas.CalculadoraTarifasTemporadaAlta;
import uniandes.dpoo.aerolinea.tarifas.CalculadoraTarifasTemporadaBaja;
import uniandes.dpoo.aerolinea.tiquetes.Tiquete;

public class Aerolinea {

	private List<Avion> aviones;
	private Map<String, Ruta> rutas;
	private List<Vuelo> vuelos;
	private Map<String, Cliente> clientes;

	public Aerolinea() {
		this.aviones = new ArrayList<Avion>();
		this.rutas = new HashMap<String, Ruta>();
		this.vuelos = new ArrayList<Vuelo>();
		this.clientes = new HashMap<String, Cliente>();
	}

	public void agregarRuta(Ruta ruta) {
		rutas.put(ruta.getCodigoRuta(), ruta);
	}

	public void agregarAvion(Avion avion) {
		aviones.add(avion);
	}

	public void agregarCliente(Cliente cliente) {
		clientes.put(cliente.getIdentificador(), cliente);
	}

	public boolean existeCliente(String identificadorCliente) {
		return clientes.containsKey(identificadorCliente);
	}

	public Cliente getCliente(String identificadorCliente) {
		return clientes.get(identificadorCliente);
	}

	public Collection<Avion> getAviones() {
		return aviones;
	}

	public Collection<Ruta> getRutas() {
		return rutas.values();
	}

	public Ruta getRuta(String codigoRuta) {
		return rutas.get(codigoRuta);
	}

	public Collection<Vuelo> getVuelos() {
		return vuelos;
	}

	public Vuelo getVuelo(String codigoRuta, String fechaVuelo) {
		for (Vuelo v : vuelos) {
			if (v.getRuta().getCodigoRuta().equals(codigoRuta)
					&& v.getFecha().equals(fechaVuelo)) {
				return v;
			}
		}
		return null;
	}

	public Collection<Cliente> getClientes() {
		return clientes.values();
	}

	public Collection<Tiquete> getTiquetes() {
		List<Tiquete> todos = new ArrayList<Tiquete>();
		for (Vuelo v : vuelos) {
			todos.addAll(v.getTiquetes());
		}
		return todos;
	}

	public void cargarAerolinea(String archivo, String tipoArchivo) throws Exception {
		CentralPersistencia.getPersistenciaAerolinea(tipoArchivo).cargarAerolinea(archivo, this);
	}

	public void salvarAerolinea(String archivo, String tipoArchivo) throws Exception {
		CentralPersistencia.getPersistenciaAerolinea(tipoArchivo).salvarAerolinea(archivo, this);
	}

	public void cargarTiquetes(String archivo, String tipoArchivo) throws Exception {
		CentralPersistencia.getPersistenciaTiquetes(tipoArchivo).cargarTiquetes(archivo, this);
	}

	public void salvarTiquetes(String archivo, String tipoArchivo) throws Exception {
		CentralPersistencia.getPersistenciaTiquetes(tipoArchivo).salvarTiquetes(archivo, this);
	}

	public void programarVuelo(String fecha, String codigoRuta, String nombreAvion) throws Exception {
		Ruta ruta = getRuta(codigoRuta);
		if (ruta == null) {
			throw new Exception("No existe una ruta con el código " + codigoRuta);
		}

		Avion avion = null;
		for (Avion a : aviones) {
			if (a.getNombre().equals(nombreAvion)) {
				avion = a;
				break;
			}
		}
		if (avion == null) {
			throw new Exception("No existe un avión con el nombre " + nombreAvion);
		}

		for (Vuelo v : vuelos) {
			if (v.getAvion().getNombre().equals(nombreAvion)
					&& v.getFecha().equals(fecha)
					&& hayCruceDeHorario(v.getRuta(), ruta)) {
				throw new Exception("El avión ya está ocupado en ese horario");
			}
		}

		Vuelo nuevo = new Vuelo(ruta, fecha, avion);
		vuelos.add(nuevo);
	}

	private boolean hayCruceDeHorario(Ruta r1, Ruta r2) {
		int salida1 = Ruta.getHoras(r1.getHoraSalida()) * 60 + Ruta.getMinutos(r1.getHoraSalida());
		int llegada1 = salida1 + r1.getDuracion();
		int salida2 = Ruta.getHoras(r2.getHoraSalida()) * 60 + Ruta.getMinutos(r2.getHoraSalida());
		int llegada2 = salida2 + r2.getDuracion();
		return salida1 < llegada2 && salida2 < llegada1;
	}

	public int venderTiquetes(String identificadorCliente, String fecha,
			String codigoRuta, int cantidad)
			throws VueloSobrevendidoException, Exception {

		Cliente cliente = getCliente(identificadorCliente);
		if (cliente == null) {
			throw new Exception("No existe un cliente con el identificador " + identificadorCliente);
		}

		Vuelo vuelo = getVuelo(codigoRuta, fecha);
		if (vuelo == null) {
			throw new Exception("No existe un vuelo para esa ruta y fecha");
		}

		int mes = Integer.parseInt(fecha.split("-")[1]);
		CalculadoraTarifas calculadora;
		if ((mes >= 1 && mes <= 5) || (mes >= 9 && mes <= 11)) {
			calculadora = new CalculadoraTarifasTemporadaBaja();
		} else {
			calculadora = new CalculadoraTarifasTemporadaAlta();
		}

		return vuelo.venderTiquetes(cliente, calculadora, cantidad);
	}

	public void registrarVueloRealizado(String fecha, String codigoRuta) {
		Vuelo vuelo = getVuelo(codigoRuta, fecha);
		if (vuelo != null) {
			for (Tiquete t : vuelo.getTiquetes()) {
				t.getCliente().usarTiquetes(vuelo);
			}
		}
	}

	public String consultarSaldoPendienteCliente(String identificadorCliente) {
		Cliente cliente = getCliente(identificadorCliente);
		if (cliente == null) {
			return "0";
		}
		int total = 0;
		for (Tiquete t : getTiquetes()) {
			if (t.getCliente().getIdentificador().equals(identificadorCliente)
					&& !t.esUsado()) {
				total += t.getTarifa();
			}
		}
		return "" + total;
	}
}