package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ComboTest {

    private Combo combo;
    private ArrayList<ProductoMenu> items;

    @BeforeEach
    public void setUp() {
        items = new ArrayList<>();
        items.add(new ProductoMenu("Corral", 14000));
        items.add(new ProductoMenu("Papas medianas", 5500));
        combo = new Combo("Combo Corral", 0.10, items);
    }

    @Test
    public void testGetNombre() {
        assertEquals("Combo Corral", combo.getNombre(), "El nombre del combo no es correcto.");
    }

    @Test
    public void testGetPrecioConDescuento() {
        assertEquals(17550, combo.getPrecio(), "El precio del combo con descuento no es correcto.");
    }

    @Test
    public void testGetPrecioSinDescuento() {
        Combo comboSinDescuento = new Combo("Combo Normal", 0.0, items);
        assertEquals(19500, comboSinDescuento.getPrecio(), "El precio sin descuento debería ser el total.");
    }

    @Test
    public void testGenerarTextoFactura() {
        String textoEsperado = "Combo Combo Corral\n" +
                               " Descuento: 0.1\n" +
                               "            17550\n";
        assertEquals(textoEsperado, combo.generarTextoFactura(), "El texto de la factura del combo no coincide.");
    }
}