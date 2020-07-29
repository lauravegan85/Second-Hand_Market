package com.openwebinars.secondhandmarket.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.openwebinars.secondhandmarket.modelo.Producto;
import com.openwebinars.secondhandmarket.modelo.Usuario;
import com.openwebinars.secondhandmarket.servicios.ProductoServicio;
import com.openwebinars.secondhandmarket.servicios.UsuarioServicio;
import com.openwebinars.secondhandmarket.upload.StorageService;

@Controller
@RequestMapping("/app") //estará dentro de la zona de app de seguridad
public class ProductosController {

//NECESITAMOS DOS SERVICIOS: USUARIO Y PRODUCTOS. Y UN ATRIBUTO DE USUARIO para tener el usuario propietario	
			//de determinados productos (los que cada usuario quiere ver en su zona privada):

@Autowired
ProductoServicio productoServicio;

@Autowired
UsuarioServicio usuarioServicio;

@Autowired
StorageService storageService; //autocableado del serv.de almacenamiento para gestión de imagen de productos.

private Usuario usuario;


//VER PRODUCTOS DE UN PROPIETARIO EN SU ZONA PRIVADA:

@ModelAttribute("misproductos")
public List<Producto> misProductos(){
	String email = SecurityContextHolder.getContext().getAuthentication().getName(); //Obtiene el email
	usuario = usuarioServicio.buscarPorEmail(email); //Busca usuario por email
	return productoServicio.productosDeUnPropietario(usuario); //Devuelve productos de ese propietario y los guarda
				//dentro del modelo para tenerlos siempre disponibles.
}

//MOSTRAR LISTADO DE MIS PRODUCTOS.
	//Este listado es idéntico al de la zona pública con la salvedad de que los productos mostrados serán los
		//del @ModelAttribute("misproductos") de la zona privada.

@GetMapping("/misproductos")
public String list(Model model,@RequestParam(name="q", required =false) String query) { //se puede hacer consultas(no obligado)
	if (query != null) //si la consulta no está vacía...
		model.addAttribute("misproductos", productoServicio.buscarMisProductos(query, usuario)); //machacamos el 
		//listado  de mis productos y buscamos entre mis productos el de la consulta.
	return 	"app/producto/lista"; //sino (si está vacía) nos lleva directamente a la plantilla y tiraremos del
										// listado de  misproductos de @ModelAttribute("misproductos")
}

//ELIMINAR PRODUCTO SUBIDO POR EL USUARIO PARA SU VENTA:

@GetMapping("/misproductos/{id}/eliminar") //de entre mis productos,le pasamos el id
public String eliminar(@PathVariable Long id) { //recoge el id
	Producto p = productoServicio.findById(id); //busca por el id pasado entre mis productos
	if(p.getCompra() == null) //si no está comprado (es decir,si la compra es nula)..
		productoServicio.borrar(p); //borra el producto
	return "redirect:/app/misproductos";	//redirige de nuevo a misproductos
}

//CREAR NUEVO PRODUCTO (el usuario puede generar producto nuevo para vender)

@GetMapping("/producto/nuevo")
public String nuevoProductoForm(Model model) {
	model.addAttribute("producto",new Producto());
	return "app/producto/form";  //redirige a plantilla form para insertar datos del producto a crear
}

//ENVIAR PRODUCTO POR FORMULARIO (para subirlo a la web).
//(El envío de producto nuevo se hace por formulario.Al enviarlo tenemos que asignarle el propietario e insertarlo):

@PostMapping("/producto/nuevo/submit") //PostMapping permite recoger producto por el formulario enviado
public String nuevoProductoSubmit(@ModelAttribute Producto producto, @RequestParam("file") MultipartFile file) { //El 2do parámetro es el fichero recibido, que añadimos para subir las imagenes de los productos.
	if(!file.isEmpty()) {
		String imagen = storageService.store(file); //almacena imagen del fichero
		producto.setImagen(MvcUriComponentsBuilder.fromMethodName(FilesController.class,"serveFile",imagen).build().toUriString());
	} //Con este FilesController como parámetro, este controlador tira del otro controlador FilesController.java a la hora de servir el fichero.
	producto.setPropietario(usuario); //asigna nuestro usuario como propietario
	productoServicio.insertar(producto); //inserta en la bbdd a través del servicio
	return "redirect:/app/misproductos"; //redirige a la plantilla de misproductos
	} /*
*fromMethodName: recibe como parámetros (elControlador,elNombreDelMétodo,losValoresDelArgumento).
*Crea un UriComponentsBuilder a partir de la asignación de un método de controlador y
* una matriz de valores de argumento de método. 
*/

}
