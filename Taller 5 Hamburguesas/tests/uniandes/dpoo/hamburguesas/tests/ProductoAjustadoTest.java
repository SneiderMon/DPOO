package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ProductoAjustadoTest {

    private ProductoMenu productoBase;
    private ProductoAjustado productoAjustado;

    @BeforeEach
    public void setUp() {
        productoBase = new ProductoMenu("Corral", 14000);
        productoAjustado = new ProductoAjustado(productoBase);
    }

    @Test
    public void testGetNombre() {
        assertEquals("Corral", productoAjustado.getNombre(), "El nombre debe ser el del producto base.");
    }

    @Test
    public void testGetPrecioSinAjustes() {
        assertEquals(14000, productoAjustado.getPrecio(), "Sin ajustes, el precio debe ser el del producto base.");
    }

    @Test
    public void testGetPrecioConAgregados() {
        Ingrediente queso = new Ingrediente("Queso", 2500);
        Ingrediente tocineta = new Ingrediente("Tocineta", 2500);
        
        productoAjustado.agregarIngrediente(queso);
        productoAjustado.agregarIngrediente(tocineta);
        
        assertEquals(19000, productoAjustado.getPrecio(), "El precio debe sumar los ingredientes agregados.");
    }

    @Test
    public void testGetPrecioConEliminados() {
        Ingrediente tomate = new Ingrediente("Tomate", 1000);
        
        productoAjustado.eliminarIngrediente(tomate);
        
        assertEquals(14000, productoAjustado.getPrecio(), "Eliminar ingredientes no debe reducir el precio.");
    }

    @Test
    public void testGenerarTextoFacturaConAjustes() {
        Ingrediente queso = new Ingrediente("Queso", 2500);
        Ingrediente tomate = new Ingrediente("Tomate", 1000);
        
        productoAjustado.agregarIngrediente(queso);
        productoAjustado.eliminarIngrediente(tomate);
        
        String textoEsperado = "Corral\n" +
                               "            14000\n" +
                               "    +Queso\n" +
                               "                2500\n" +
                               "    -Tomate\n" +
                               "            16500\n";
                               
        assertEquals(textoEsperado, productoAjustado.generarTextoFactura(), "El texto de la factura con ajustes no coincide.");
    }
}