package uniandes.dpoo.aerolinea.tarifas;
import uniandes.dpoo.aerolinea.modelo.Aeropuerto;
import uniandes.dpoo.aerolinea.modelo.Ruta;
import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;

public abstract class CalculadoraTarifas {

    public static final double IMPUESTO = 0.28;

    public CalculadoraTarifas() {
        
    }

    public int calcularTarifa(Vuelo vuelo, Cliente cliente) {
        int costobase = calcularCostoBase(vuelo, cliente);
        double descuento = calcularPorcentajeDescuento(cliente);
        
        int costodescuento = (int) Math.round(costobase * (1.0 - descuento));
        int impuestos = calcularValorImpuestos(costodescuento);
        
        return costodescuento + impuestos;
    }

    protected abstract int calcularCostoBase(Vuelo vuelo, Cliente cliente);

    protected abstract double calcularPorcentajeDescuento(Cliente cliente);

    protected int calcularDistanciaVuelo(Ruta ruta) {
        return Aeropuerto.calcularDistancia(ruta.getOrigen(), ruta.getDestino());
    }

    protected int calcularValorImpuestos(int costobase) {
        return (int) Math.round(costobase * IMPUESTO);
    }
}