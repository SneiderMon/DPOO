package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class PedidoTest {

    private Pedido pedido;

    @BeforeEach
    public void setUp() {
        pedido = new Pedido("Juan Perez", "Calle 123");
    }

    @Test
    public void testConstructorYGetDatos() {
        assertEquals("Juan Perez", pedido.getNombreCliente(), "El nombre del cliente no coincide.");
        assertTrue(pedido.getIdPedido() >= 0, "El ID del pedido debería ser válido.");
    }

    @Test
    public void testAgregarProductoYPrecios() {
        ProductoMenu prod1 = new ProductoMenu("Corral", 10000);
        ProductoMenu prod2 = new ProductoMenu("Papas", 5000);
        
        pedido.agregarProducto(prod1);
        pedido.agregarProducto(prod2);

        assertEquals(17850, pedido.getPrecioTotalPedido(), "El cálculo del IVA y total no es correcto.");
    }

    @Test
    public void testGenerarTextoFactura() {
        ProductoMenu prod1 = new ProductoMenu("Corral", 10000);
        pedido.agregarProducto(prod1);
        
        String textoEsperado = "Cliente: Juan Perez\n" +
                               "Dirección: Calle 123\n" +
                               "----------------\n" +
                               "Corral\n" +
                               "            10000\n" +
                               "----------------\n" +
                               "Precio Neto:  10000\n" +
                               "IVA:          1900\n" +
                               "Precio Total: 11900\n";
                               
        assertEquals(textoEsperado, pedido.generarTextoFactura(), "El texto de la factura del pedido no coincide.");
    }

    @Test
    public void testGuardarFactura() throws Exception {
        ProductoMenu prod1 = new ProductoMenu("Corral", 10000);
        pedido.agregarProducto(prod1);
        

        File archivoTemporal = File.createTempFile("test_factura", ".txt");
        archivoTemporal.deleteOnExit(); 
        
        pedido.guardarFactura(archivoTemporal);
        

        StringBuilder contenido = new StringBuilder();
        try (Scanner scanner = new Scanner(archivoTemporal)) {
            while (scanner.hasNextLine()) {
                contenido.append(scanner.nextLine()).append("\n");
            }
        }
        
        assertEquals(pedido.generarTextoFactura(), contenido.toString(), "El contenido del archivo guardado no es el esperado.");
    }
}