package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ProductoMenuTest {

    @Test
    public void testConstructorYGetNombre() {
        ProductoMenu producto = new ProductoMenu("Corral", 14000);
        assertEquals("Corral", producto.getNombre(), "El nombre del producto no es correcto.");
    }

    @Test
    public void testGetPrecio() {
        ProductoMenu producto = new ProductoMenu("Corral", 14000);
        assertEquals(14000, producto.getPrecio(), "El precio del producto no es correcto.");
    }

    @Test
    public void testGenerarTextoFactura() {
        ProductoMenu producto = new ProductoMenu("Corral", 14000);
        String textoEsperado = "Corral\n            14000\n";
        assertEquals(textoEsperado, producto.generarTextoFactura(), "El texto de la factura no coincide.");
    }
}