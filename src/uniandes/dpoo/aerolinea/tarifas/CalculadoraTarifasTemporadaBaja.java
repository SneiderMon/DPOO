package uniandes.dpoo.aerolinea.tarifas;

import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;
import uniandes.dpoo.aerolinea.modelo.cliente.ClienteCorporativo;

public class CalculadoraTarifasTemporadaBaja extends CalculadoraTarifas {

	protected int COSTO_POR_KM_CORPORATIVO = 900;
	protected int COSTO_POR_KM_NATURAL = 600;
	protected double DESCUENTO_GRANDES = 0.2;
	protected double DESCUENTO_MEDIANAS = 0.1;
	protected double DESCUENTO_PEQ = 0.02;

	public CalculadoraTarifasTemporadaBaja() {
	}

	@Override
	protected int calcularCostoBase(Vuelo vuelo, Cliente cliente) {
		int distancia = calcularDistanciaVuelo(vuelo.getRuta());
		if (cliente.getTipoCliente().equalsIgnoreCase(ClienteCorporativo.CORPORATIVO)) {
			return distancia * COSTO_POR_KM_CORPORATIVO;
		} else {
			return distancia * COSTO_POR_KM_NATURAL;
		}
	}

	@Override
	protected double calcularPorcentajeDescuento(Cliente cliente) {
		if (cliente.getTipoCliente().equalsIgnoreCase(ClienteCorporativo.CORPORATIVO)) {
			ClienteCorporativo corporativo = (ClienteCorporativo) cliente;
			if (corporativo.getTamanoEmpresa() == ClienteCorporativo.GRANDE) {
				return DESCUENTO_GRANDES;
			}
			if (corporativo.getTamanoEmpresa() == ClienteCorporativo.MEDIANA) {
				return DESCUENTO_MEDIANAS;
			}
			if (corporativo.getTamanoEmpresa() == ClienteCorporativo.PEQUENA) {
				return DESCUENTO_PEQ;
			}
		}
		return 0.0;
	}
}