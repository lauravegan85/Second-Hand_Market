/* A) PREPARACIÓN DEL PROYECTO.
 * 1-Creamos nuevo proyecto:File->new->other->Spring starter project, añadiéndole las dependencias a usar:
 * web,JPA,H2,Session,Security,Redis,Thymeleaf. En POM.XML incorporamos manualmente dos dependencias que
 * no se generan por defecto, las webjars y los extras de thymeleaf.
 * 2-Añadimos algunas properties en el fichero application.properties.
 * 
 * 
 * B) CREACIÓN DE ENTIDADES.
 * Requiere crear un nuevo paquete llamado Modelo (clic new dentro del paquete de la aplicación:
 * com.openwebinars.secondhandmarket). Dentro creamos las tres entidades(clases) a manejar: Usuario,
 * Productos y Compra.Se anotan con @Entity por ser entidades. Les añadimos sus atributos(datos) y 
 * los campos de asociación entre las clases (recordar que la compra se asocia al producto/s del carrito,
 * y no al revés), luego constructores,métodos setters y getters,hashcode() and equals, y método toString().
 * 
 * Para que los campos anotados con CreatedDate funcionen tenemos que añadir una anotación en las clases
 * que la estén usando (tanto Compra,como Usuario): @EntityListeners(AuditingEntityListener.class).
 * Tendremos que crear un nuevo paquete de configuración(config) con una clase 'ConfiguracionAuditoria' 
 * con el que podamos configurar ese escuchador.
 * 
 * 
 * C)CLASE DE CONFIGURACIÓN DE AUDITORÍA, PARA LA FECHA DATE.
 * new paquete config-> Class  'ConfiguracionAuditoria' . Le ponemos la anotación @Configuration y 
 * @EnableJpaAauditing. Esto permitirá insertar la marca de tiempo (fecha y hora:TIMESTAMP) a los atributos 
 * de tipo Date; de esta manera no tenemos que hacer nada: esta configuración recoge automáticamente
 * los datos temporales del sistema.
 * 
 * 
 * D)CREAR REPOSITORIOS.
 * Creamos nuevo package llamado repositorios, con 3 repositorios que extienden a JpaRepository:
 * son las interfaces que llamaremos 'UsuarioRepository','CompraRepository' y 'ProductoRepository'.
 * Les añadimos unas primeras consultas que sabemos que usaremos:
 * -En el caso del Usuario, necesitamos buscar un usuario por su email.
 * -En el caso del producto tendremos varias búsquedas:
 *     -Buscar Todos los productos de un determinado usuario(por ej: uno mismo viendo su zona privada)
 *     -Buscar todos los productos de una determinada compra
 *     -Buscar todos los productos que aún no han sido comprados -donde la compra sea nula (para ver 
 *     cuáles sí están disponibles para poder comprarlos).
 *     -Buscar todos los productos no comprados donde el nombre del producto contenga una determinada
 *     subcadena de caracteres para poder implementar el buscador.
 *     -Todos los productos de un usuario que contengan una subcadena de caracteres para poder tener
 *     también en la zona privada del usuario un buscador de sus propios productos.
 *  -En el caso de la Compra, añadimos un método de consulta para poder buscar todas las compras de un 
 *  determinado usuario.   
 *    
 *     
 *  E) INSERTAR DATOS DE EJEMPLO EN EL REPOSITORIO DE USUARIO, USANDO UN COMMANDLINERUNNER EN 
 *  APPLICATION.JAVA. 
 *     
 *  F) SPRING SECURITY.
 *  Creamos paquete llamado Seguridad y la CLASE DE CONFIGURACIÓN DE SEGURIDAD, llamada SecuridadConfig
 *  que extiende a WebSecurityConfigurerAdapter. 
 *  Añadimos a la clase las dos anotaciones que debe llevar: @Configuration y @EnableWebSecurity.   
 *  Sobreescribimos los métodos para la autenticación y la autorización (clic dcho -> source -> 
 *  Override/Implement Methods-> y dejamos seleccionados: configure(HttpSecurity) y
 *  configure(AuthenticationManagerBuilder).
 *  En primer lugar, sobreescribimos el método de Autorización (configure(HttpSecurity)), incorporando
 *  las peticiones de autorización , el formulario de login y el logout, y añadimos la inhabilitación de
 *  los elementos de seguridad necesarios para poder usar la consola de h2.
 *  El otro método de momento lo dejamos como está.
 *  
 *  Creamos ahora la CLASE QUE IMPLEMENTA LA INTERFAZ USERDETAILSSERVICE. La llamamos UserDetailsServiceImpl
 *  (sufijo impl hace referencia a implementación). La creamos dentro del mismo paquete 'seguridad'. Esta
 *  interfaz para el servicio nos permitirá rescatar los usuarios desde un almacén de datos.*  
 *  Autocableamos/inyectamos la interfaz UserDetailsService en la clase SeguridadConfig y a continuación 
 *  completamos el método de autentificación.
 *  
 *  	   
 *  G) CREACIÓN DE LOS SERVICIOS.
 *  Creamos un paquete llamado servicios que va a contener tres servicios (uno para la clase Usuario, otro para
 *  Producto, y otro para Compra), que van a desarrollar los distintos métodos CRUD. Dichas clases se van a
 *  anotar con @Service. Y se les tiene que inyectar la dependencia de su repositorio respectivo con un @Autowired.
 *  NOTA: Es normal que en los servicios se vayan implementando nuevos métodos a medida que vayamos necesitando.
 *  
 *  7) COMENTAMOS EL COMMANDLINERUNNER QUE TENÍAMOS USANDO UN REPOSITORIO CON EJEMPLOS AÑADIDOS, PUES YA
 *  NO NECESITAMOS ESTE REPOSITORIO. Ahora usamos los servicios que acabamos de elaborar para realizar las 
 *  diferentes inserciones.
 *  
 *  8) CREAR PAQUETE LLAMADO CONTROLADORES. con una clase llamada LoginController, anotada con @Controller.
 *  Creamos los métodos de login y registro.
 *  
 *  Creamos un nuevo controlador para gestión del listado de los productos de la BBDD: lo llamamos
 *  ZonaPublicaController. Desarrollamos sus métodos: listar productos no vendidos, buscar productos por
 *  una query, buscar productos por su id.
 *  
 *  Probamos en navegador: localhost:9000-> nos lleva a localhost:9000/public.
 *  Probamos localhost:9000/auth/login. Rellenamos nuevo usuario.
 *  Vemos que se ha insertado en la consola de eclipse (pone call next value hibernate, etc etc). 
 *  Probamos en el navegador la consola de h2 para comprobar que se ha insertado: localhost:9000/h2-console.
 *  Le damos a connect , luego a run la sentencia: SELECT * FROM USUARIO, y comprobamos que se nos ha
 *  insertado.
 *  Luego comprobamos localhost:9000/public/index
 *  Luego comprobamos localhost:9000/public/   y vemos que cargan todas las fotos de los productos. Probamos
 *  el buscador en la zona privada.
 *  Clicamos en Salir y probamos el buscador en la zona pública.
 *  
 *  9) COMPRAR Y CARRITO.
 *  Crear nuevo controlador llamado CompraController. Como todo esto está en la zona privada lo ponemos con
 *  @RequestMapping("/app"). Añadimos inyección de dependencias y creamos atributo de sesión. Desarrollamos
 *  métodos para ver productos del carrito,coste total productos carrito, ver mis compras, ver el carrito,
 *  añadir producto al carrito, eliminar producto del carrito.
 *  
 *  Probamos en navegador:localhost:9000, iniciamos sesión con por ej: luismi.lopez@openwebinars.net
 *  contraseña 123456 . Si hacemos clic en carrito vemos que está vacío ("Aún no se ha seleccionado ningún
 *  producto para comprar").
 *  
 * Clic en Inicio. Vemos que en los productos subidos por el usuario no hay botón de comprar (no se va
 * a comprar sus propios productos!). En los que han subido otros, sí.
 *  
 *  10) FINALIZAR COMPRA Y FACTURA.
 * Añadimos en CompraController.java los métodos para finalizar compra, ver factura, e ir al menú de miscompras.
 * 
 *  Probamos a iniciar sesion (localhost:9000) ,comprar un producto en la web y finalizar compra. Nos enseñará
 *  la factura. Ademas, si clicamos en el apartado miscompras de la web vemos el resumen de factura y podemos
 *  acceder a verla. Si ahora vamos al apartado inicio comprobamos que los productos comprados ya no aparecen 
 *  porque el listado buscaba siempre entre los productos sin vender,por eso si usamos el buscador para
 *  buscar uno de los productos que acabamos de comprar veremos que no tendremos resultados(ya están vendidos).
 *  
 *  
 *  11) GESTIÓN DE PRODUCTOS.
 *  Creamos controlador de productos (ProductosController), con los métodos para ver productos de un propietario
 *  en su zona privada,  mostrar listado de misproductos, eliminar producto subido por el usuario para vender,
 *  crear Nuevo Producto (el usuario puede subir producto nuevo para vender).
 *  Probamos en localhost:9000 iniciando sesion con luismi.lopez@openwebinars.net ,contraseña 123456. Clicamos
 *  en el apartado web "Mis Productos" y comprobamos que salen mis productos con la etiqueta sin vender. Ya sale
 *  también la posibilidad de eliminarlos (el aspa de la imagen: x), lo cual hemos conseguido con jquery: si 
 *  clicamos en el aspa un diálogo nos pregunta si estamos seguros de querer borrar.
 * 	Ya podemos crear nuevos productos y enviarlos por formulario, aunque aún sin subir su foto: lo hacemos en
 * 	el siguiente apartado.
 * 
 * 12) SUBIDA DE IMÁGENES Y GESTIÓN DEL ALMACENAMIENTO.
 * Creamos paquete llamado upload con las mismas cinco clases utilizadas en el video de subidas de ficheros: 
 * StorageService,FileSystemStorageService (tan sólo se le añade la opción de eliminar un fichero particular),
 * StorageFileNotFoundException,StorageProperties (requiere añadir en el paquete config,en su clase de 
 * configuración de auditoría, la anotación : @EnableConfigurationProperties(StorageProperties.class)  )y
 * StorageService.
 * 
 * A raíz de esto hay que ir a servicios y controladores a implementar los cambios necesarios:
 * 
 * -En ProductoServicio, autocablear el servicio de almacenamiento StorageService, y en su método de borrar 
 * un producto habría que añadirle que si se borra el producto se borre su imagen (es decir: si imagen no
 * está vacía: delete la imagen) 
 * 
 * -En ProductosController,autocablear el servicio de almacenamiento StorageService.
 * Además, en el @PostMapping del nuevo producto (método nuevoProductoSubmit) añadimos como parámentro recibido
 * el fichero. Indicamos que si el fichero no está vacío, almacene la imagen. De modificar este método (usamos
 * en él FilesController)-> tenemos que implementar la clase FilesController dentro del paquete de controladores,
 * ya que como vamos a trabajar con ficheros en dos controladores distintos (Producto y Login usuario)el método
 * serveFile lo pondremos en un controlador independiente de uso para ambos.
 * 
 * -En LoginController, vamos a la subida del avatar para hacer algo similar al paso anterior: autoinyectamos
 * el servicio de almacenamiento StorageService,y modificamos el @PostMapping del registro de usuario (método
 * register) para incorporar como 2do parámetro el fichero.
 * 
 * Probar que funciona en navegador: localhost:9000, crear nueva cuenta de usuario, maria@openwebinars.net
 * contraseña 123456, seleccionándole un avatar(subiendole una imagen). Una vez dentro añadimos un Nuevo producto
 * subiendo tambien una imagen. Finalmente clicamos sobre el producto añadido y podremos ver su usuario
 * propietario con su avatar.
 */ 	


package com.openwebinars.secondhandmarket;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.openwebinars.secondhandmarket.modelo.Producto;
import com.openwebinars.secondhandmarket.modelo.Usuario;
import com.openwebinars.secondhandmarket.servicios.ProductoServicio;
import com.openwebinars.secondhandmarket.servicios.UsuarioServicio;
import com.openwebinars.secondhandmarket.upload.StorageProperties;
import com.openwebinars.secondhandmarket.upload.StorageService;

@SpringBootApplication
public class SecondHandMarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecondHandMarketApplication.class, args);
	}


//AÑADIMOS COMO EJEMPLO UN USUARIO EN EL REPOSITORIO DE USUARIOS, Y VARIOS PRODUCTOS EN EL REPOSITORIO
//DE PRODUCTOS, CON EL CommandLineRunner. Asociamos todos los productos añadidos al usuario creado:
//cuando tengamos los servicios creados comentamos el commandlinerunner y usamos los servicios para insertar.	
	
	/*
	@Bean
	public CommandLineRunner initData(UsuarioRepository usuarioRepository, ProductoRepository productoRepository) {
		return args -> {

			// Instancia usuario:	
			Usuario usuario = new Usuario("Luis Miguel", "López Magaña", null, "luismi", "123456"); 
			// imagen es null. Id y fecha no llevaba el constructor porque se autogeneraban.

		
			// Guarda el usuario en el repositorio de usuarios:
			usuario = usuarioRepository.save(usuario); 

		
			// Guarda en el repositorio de productos los productos que instancia como una lista:
			 
			productoRepository.saveAll(Arrays.asList(new Producto("Bicicleta de montaña", 100.0f,
					"https://contents.mediadecathlon.com/p1638915/k$360a0335ba8ca26de23a52c8abcf8ba6/sq/BICICLETA+DE+MONTA+A+ROCKRIDER+ST+540+ALUMINIO+18V+27+5+NEGRO+ROJO.webp?f=1000x1000",
					usuario),
					new Producto("Golg GTI Serie 2", 2500.0f, "https://images.vibbo.com/635x476/397/39748014922.jpg",
							usuario),
					new Producto("Raqueta de tenis", 10.5f,
							"https://images-na.ssl-images-amazon.com/images/I/71H6CsISMEL._AC_SX569_.jpg", usuario),
					new Producto("Xbox One X", 425.0f,
							"https://d1o0zx25fn5p70.cloudfront.net/VcfYyLyY4w_qvKxoNxS7ZqvWua8=/fit-in/1280x800/noupscale/assets.rebuy.de/products/008/925/155/covers/main.jpeg?t=0",
							usuario),
					new Producto("Trípode flexible", 10.0f,
							"https://images-na.ssl-images-amazon.com/images/I/714u%2BBe2YbL._AC_SL1500_.jpg", usuario),
					new Producto("Iphone 7 128 GB", 350.0f,
							"https://static.carrefour.es/hd_510x_/crs/cdn_static/catalog/hd/075587_00_1.jpg",
							usuario)
					));

		};

	}
	
	*/
	
	
//COMO TENEMOS YA LOS SERVICIOS CREADOS, USAMOS ESTOS PARA INSERTAR EN EL REPOSITORIO CON EL SIG CÓDIGO:
	//(ya no necesitamos el CommandLineRunner anterior):

	@Bean
	public CommandLineRunner initData(UsuarioServicio usuarioServicio, ProductoServicio productoServicio) {
		return args -> {

			Usuario usuario = new Usuario("Luis Miguel", "López Magaña", null, "luismi.lopez@openwebinars.net",
					"123456");
			usuario = usuarioServicio.registrar(usuario); // AL REGISTRAR USUARIO,LA CONTRASEÑA YA QUEDA CIFRADA.
				//usa el método registrar de usuarioServicio.java
			
			Usuario usuario2 = new Usuario("Antonio", "García Martin", null, "antonio.garcia@hotmail.es", "123456");
			usuario2 = usuarioServicio.registrar(usuario2);

			List<Producto> listado = Arrays.asList(new Producto("Bicicleta de montaña", 100.0f,
					"https://contents.mediadecathlon.com/p1638915/k$360a0335ba8ca26de23a52c8abcf8ba6/sq/BICICLETA+DE+MONTA+A+ROCKRIDER+ST+540+ALUMINIO+18V+27+5+NEGRO+ROJO.webp?f=1000x1000",
					usuario),
					new Producto("Golg GTI Serie 2", 2500.0f, "https://images.vibbo.com/635x476/397/39748014922.jpg",
							usuario),
					new Producto("Raqueta de tenis", 10.5f,
							"https://images-na.ssl-images-amazon.com/images/I/71H6CsISMEL._AC_SX569_.jpg", usuario),
					new Producto("Xbox One X", 425.0f,
							"https://d1o0zx25fn5p70.cloudfront.net/VcfYyLyY4w_qvKxoNxS7ZqvWua8=/fit-in/1280x800/noupscale/assets.rebuy.de/products/008/925/155/covers/main.jpeg?t=0",
							usuario),
					new Producto("Trípode flexible", 10.0f,
							"https://images-na.ssl-images-amazon.com/images/I/714u%2BBe2YbL._AC_SL1500_.jpg", usuario),
					new Producto("Iphone 7 128 GB", 350.0f,
							"https://static.carrefour.es/hd_510x_/crs/cdn_static/catalog/hd/075587_00_1.jpg", usuario));
			
			listado.forEach(productoServicio::insertar); //Con el servicio de producto se insertan los diferentes productos que
																//tengamos en el listado -metodo insertar de ProductoServicio.java)

			//productoServicio::insertar : "sintaxis con referencia a método", es decir,
				//Clase::metodo
		};

	}
	
	/**
	 * Este bean se inicia al lanzar la aplicación. Nos permite inicializar el almacenamiento
	 * secundario del proyecto
	 * 
	 * @param storageService Almacenamiento secundario del proyecto
	 * @return
	 */
	@Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> { //expresión lambda
            storageService.deleteAll();
            storageService.init();
        };
    }

}
