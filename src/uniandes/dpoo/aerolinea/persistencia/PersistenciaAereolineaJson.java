package uniandes.dpoo.aerolinea.persistencia;
 
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedHashMap;
import java.util.Map;
 
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
 
import uniandes.dpoo.aerolinea.modelo.Aeropuerto;
import uniandes.dpoo.aerolinea.modelo.Aerolinea;
import uniandes.dpoo.aerolinea.modelo.Avion;
import uniandes.dpoo.aerolinea.modelo.Ruta;
import uniandes.dpoo.aerolinea.modelo.Vuelo;
 

public class PersistenciaAereolineaJson implements IPersistenciaAerolinea
{
    private final static String AEROPUERTOS = "aeropuertos";
    private final static String AVIONES = "aviones";
    private final static String RUTAS = "rutas";
    private final static String VUELOS = "vuelos";
 
    private final static String NOMBRE = "nombre";
    private final static String CODIGO = "codigo";
    private final static String NOMBRE_CIUDAD = "nombreCiudad";
    private final static String LATITUD = "latitud";
    private final static String LONGITUD = "longitud";
 
    private final static String CAPACIDAD = "capacidad";
 
    private final static String CODIGO_RUTA = "codigoRuta";
    private final static String ORIGEN = "origen";
    private final static String DESTINO = "destino";
    private final static String HORA_SALIDA = "horaSalida";
    private final static String HORA_LLEGADA = "horaLlegada";
 
    private final static String FECHA = "fecha";
    private final static String NOMBRE_AVION = "nombreAvion";
 
    @Override
    public void cargarAerolinea( String archivo, Aerolinea aerolinea ) throws Exception
    {
        try( FileReader lector = new FileReader( archivo ) )
        {
            JSONObject jsonAerolinea = new JSONObject( new JSONTokener( lector ) );
 
            Map<String, Aeropuerto> aeropuertosPorCodigo = new LinkedHashMap<String, Aeropuerto>( );
            JSONArray jAeropuertos = jsonAerolinea.getJSONArray( AEROPUERTOS );
            for( int i = 0; i < jAeropuertos.length( ); i++ )
            {
                JSONObject jAeropuerto = jAeropuertos.getJSONObject( i );
                Aeropuerto aeropuerto = new Aeropuerto( jAeropuerto.getString( NOMBRE ), jAeropuerto.getString( CODIGO ),
                        jAeropuerto.getString( NOMBRE_CIUDAD ), jAeropuerto.getDouble( LATITUD ), jAeropuerto.getDouble( LONGITUD ) );
                aeropuertosPorCodigo.put( aeropuerto.getCodigo( ), aeropuerto );
            }
 
            JSONArray jAviones = jsonAerolinea.getJSONArray( AVIONES );
            for( int i = 0; i < jAviones.length( ); i++ )
            {
                JSONObject jAvion = jAviones.getJSONObject( i );
                aerolinea.agregarAvion( new Avion( jAvion.getString( NOMBRE ), jAvion.getInt( CAPACIDAD ) ) );
            }
 
            JSONArray jRutas = jsonAerolinea.getJSONArray( RUTAS );
            for( int i = 0; i < jRutas.length( ); i++ )
            {
                JSONObject jRuta = jRutas.getJSONObject( i );
                Aeropuerto origen = aeropuertosPorCodigo.get( jRuta.getString( ORIGEN ) );
                Aeropuerto destino = aeropuertosPorCodigo.get( jRuta.getString( DESTINO ) );
                aerolinea.agregarRuta( new Ruta( origen, destino, jRuta.getString( HORA_SALIDA ), jRuta.getString( HORA_LLEGADA ),
                        jRuta.getString( CODIGO_RUTA ) ) );
            }
 
            JSONArray jVuelos = jsonAerolinea.getJSONArray( VUELOS );
            for( int i = 0; i < jVuelos.length( ); i++ )
            {
                JSONObject jVuelo = jVuelos.getJSONObject( i );
                aerolinea.programarVuelo( jVuelo.getString( FECHA ), jVuelo.getString( CODIGO_RUTA ), jVuelo.getString( NOMBRE_AVION ) );
            }
        }
    }
 
    @Override
    public void salvarAerolinea( String archivo, Aerolinea aerolinea ) throws Exception
    {
        JSONObject jsonAerolinea = new JSONObject( );
 
        // Se recopilan los aeropuertos a partir de las rutas, ya que la aerolínea no los guarda directamente
        Map<String, Aeropuerto> aeropuertosPorCodigo = new LinkedHashMap<String, Aeropuerto>( );
        for( Ruta ruta : aerolinea.getRutas( ) )
        {
            aeropuertosPorCodigo.put( ruta.getOrigen( ).getCodigo( ), ruta.getOrigen( ) );
            aeropuertosPorCodigo.put( ruta.getDestino( ).getCodigo( ), ruta.getDestino( ) );
        }
 
        JSONArray jAeropuertos = new JSONArray( );
        for( Aeropuerto aeropuerto : aeropuertosPorCodigo.values( ) )
        {
            JSONObject jAeropuerto = new JSONObject( );
            jAeropuerto.put( NOMBRE, aeropuerto.getNombre( ) );
            jAeropuerto.put( CODIGO, aeropuerto.getCodigo( ) );
            jAeropuerto.put( NOMBRE_CIUDAD, aeropuerto.getNombreCiudad( ) );
            jAeropuerto.put( LATITUD, aeropuerto.getLatitud( ) );
            jAeropuerto.put( LONGITUD, aeropuerto.getLongitud( ) );
            jAeropuertos.put( jAeropuerto );
        }
        jsonAerolinea.put( AEROPUERTOS, jAeropuertos );
 
        JSONArray jAviones = new JSONArray( );
        for( Avion avion : aerolinea.getAviones( ) )
        {
            JSONObject jAvion = new JSONObject( );
            jAvion.put( NOMBRE, avion.getNombre( ) );
            jAvion.put( CAPACIDAD, avion.getCapacidad( ) );
            jAviones.put( jAvion );
        }
        jsonAerolinea.put( AVIONES, jAviones );
 
        JSONArray jRutas = new JSONArray( );
        for( Ruta ruta : aerolinea.getRutas( ) )
        {
            JSONObject jRuta = new JSONObject( );
            jRuta.put( CODIGO_RUTA, ruta.getCodigoRuta( ) );
            jRuta.put( ORIGEN, ruta.getOrigen( ).getCodigo( ) );
            jRuta.put( DESTINO, ruta.getDestino( ).getCodigo( ) );
            jRuta.put( HORA_SALIDA, ruta.getHoraSalida( ) );
            jRuta.put( HORA_LLEGADA, ruta.getHoraLlegada( ) );
            jRutas.put( jRuta );
        }
        jsonAerolinea.put( RUTAS, jRutas );
 
        JSONArray jVuelos = new JSONArray( );
        for( Vuelo vuelo : aerolinea.getVuelos( ) )
        {
            JSONObject jVuelo = new JSONObject( );
            jVuelo.put( FECHA, vuelo.getFecha( ) );
            jVuelo.put( CODIGO_RUTA, vuelo.getRuta( ).getCodigoRuta( ) );
            jVuelo.put( NOMBRE_AVION, vuelo.getAvion( ).getNombre( ) );
            jVuelos.put( jVuelo );
        }
        jsonAerolinea.put( VUELOS, jVuelos );
 
        try( FileWriter escritor = new FileWriter( archivo ) )
        {
            escritor.write( jsonAerolinea.toString( 4 ) );
        }
    }
}