package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import uniandes.dpoo.hamburguesas.excepciones.*;
import uniandes.dpoo.hamburguesas.mundo.*;

public class RestauranteTest {

    private Restaurante restaurante;
    private File archivoIngredientes;
    private File archivoMenu;
    private File archivoCombos;

    // JUnit creará una carpeta temporal distinta y segura para cada prueba
    @TempDir
    Path tempDir;

    @BeforeEach
    public void setUp() throws Exception {
        restaurante = new Restaurante();
        
        // Usamos la carpeta temporal
        archivoIngredientes = tempDir.resolve("test_ingredientes.txt").toFile();
        archivoMenu = tempDir.resolve("test_menu.txt").toFile();
        archivoCombos = tempDir.resolve("test_combos.txt").toFile();
        
        // Escribimos datos válidos
        PrintWriter pwIng = new PrintWriter(archivoIngredientes);
        pwIng.println("tomate;1000");
        pwIng.println("queso;2500");
        pwIng.close();

        PrintWriter pwMenu = new PrintWriter(archivoMenu);
        pwMenu.println("corral;14000");
        pwMenu.println("papas medianas;5500");
        pwMenu.close();

        PrintWriter pwCombo = new PrintWriter(archivoCombos);
        pwCombo.println("combo corral;10%;corral;papas medianas");
        pwCombo.close();
    }

    @Test
    public void testCargarInformacionRestaurante() throws Exception {
        restaurante.cargarInformacionRestaurante(archivoIngredientes, archivoMenu, archivoCombos);
        
        assertEquals(2, restaurante.getIngredientes().size(), "Deben haber 2 ingredientes.");
        assertEquals(2, restaurante.getMenuBase().size(), "Deben haber 2 productos en el menú.");
        assertEquals(1, restaurante.getMenuCombos().size(), "Debe haber 1 combo.");
    }

    @Test
    public void testExcepcionIngredienteRepetido() throws Exception {
        PrintWriter pw = new PrintWriter(archivoIngredientes);
        pw.println("tomate;1000");
        pw.println("tomate;1500"); // Repetido
        pw.close();

        assertThrows(IngredienteRepetidoException.class, () -> {
            restaurante.cargarInformacionRestaurante(archivoIngredientes, archivoMenu, archivoCombos);
        }, "Debería lanzar IngredienteRepetidoException");
    }

    @Test
    public void testExcepcionProductoRepetido() throws Exception {
        PrintWriter pw = new PrintWriter(archivoMenu);
        pw.println("corral;14000");
        pw.println("corral;15000"); // Repetido
        pw.close();

        assertThrows(ProductoRepetidoException.class, () -> {
            restaurante.cargarInformacionRestaurante(archivoIngredientes, archivoMenu, archivoCombos);
        }, "Debería lanzar ProductoRepetidoException");
    }

    @Test
    public void testExcepcionProductoFaltanteEnCombo() throws Exception {
        PrintWriter pw = new PrintWriter(archivoCombos);
        pw.println("combo malo;10%;corral;hamburguesa_inexistente"); 
        pw.close();

        assertThrows(ProductoFaltanteException.class, () -> {
            restaurante.cargarInformacionRestaurante(archivoIngredientes, archivoMenu, archivoCombos);
        }, "Debería lanzar ProductoFaltanteException");
    }

    @Test
    public void testIniciarYConsultarPedido() throws Exception {
        assertNull(restaurante.getPedidoEnCurso(), "No debe haber pedido al inicio.");
        
        restaurante.iniciarPedido("Juan", "Calle 1");
        assertNotNull(restaurante.getPedidoEnCurso(), "Debe haber un pedido en curso.");
        assertEquals("Juan", restaurante.getPedidoEnCurso().getNombreCliente());
    }

    @Test
    public void testExcepcionYaHayPedidoEnCurso() throws Exception {
        restaurante.iniciarPedido("Juan", "Calle 1");
        
        assertThrows(YaHayUnPedidoEnCursoException.class, () -> {
            restaurante.iniciarPedido("Pedro", "Calle 2");
        }, "Debería lanzar YaHayUnPedidoEnCursoException");
    }

    @Test
    public void testCerrarYGuardarPedido() throws Exception {
        restaurante.iniciarPedido("Juan", "Calle 1");
        Pedido p = restaurante.getPedidoEnCurso();
        
        restaurante.cerrarYGuardarPedido();
        
        assertNull(restaurante.getPedidoEnCurso(), "El pedido en curso debe quedar nulo.");
        assertEquals(1, restaurante.getPedidos().size(), "El pedido cerrado debe estar en la lista.");
        assertEquals(p, restaurante.getPedidos().get(0), "El pedido guardado debe ser el mismo.");
    }

    @Test
    public void testExcepcionNoHayPedidoEnCurso() {
        assertThrows(NoHayPedidoEnCursoException.class, () -> {
            restaurante.cerrarYGuardarPedido();
        }, "Debería lanzar NoHayPedidoEnCursoException.");
    }
}