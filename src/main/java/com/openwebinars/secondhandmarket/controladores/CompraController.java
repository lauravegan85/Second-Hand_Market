package com.openwebinars.secondhandmarket.controladores;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.openwebinars.secondhandmarket.modelo.Compra;
import com.openwebinars.secondhandmarket.modelo.Producto;
import com.openwebinars.secondhandmarket.modelo.Usuario;
import com.openwebinars.secondhandmarket.servicios.CompraServicio;
import com.openwebinars.secondhandmarket.servicios.ProductoServicio;
import com.openwebinars.secondhandmarket.servicios.UsuarioServicio;

@Controller
@RequestMapping("/app")  //Como todo está en la zona privada, colocamos este inicio de ruta para todos los métodos
public class CompraController {

//Necesitamos de los 3 SERVICIOS,porque la compra es donde coinciden los tres:
	
	@Autowired
	CompraServicio compraServicio;  //Si confirmamos carrito acabamos comprando
	
	@Autowired
	ProductoServicio productoServicio; //Productos que insertamos dentro del carrito
	
	@Autowired
	UsuarioServicio usuarioServicio; //Permite saber el usuario que termina haciendo la compra
	
//AÑADIMOS LA SESIÓN para poder implementar el carrito:
	
	@Autowired
	HttpSession session;
	
//AÑADIMOS UN ATRIBUTO PRIVADO DE LA CLASE USUARIO,donde almacenaremos el usuario que está logueado,
	//(ya que al no meternos de lleno con el uso de SpringSecurity, al rescatar los datos del usuario
		//sólo tenemos una parte,el principal,donde tiene algunos datos como el email, pero NO es una
			//instancia de nuestra propia clase. Y como la vamos a necesitar para vincular la compra con
				//el usuario, lo rescataremos de este modo.
	
	private Usuario usuario;
	
//COMO MODELATTRIBUTE siempre vamos a necesitar tener EL CARRITO. 
	//Estos métodos en un controlador anotados con @ModelAttribute, van a poner dentro del modelo el resultado
		//de ejecutar este método.
	
	@ModelAttribute("carrito")
	public List<Producto> productosCarrito(){
		List<Long> contenido = (List<Long>)session.getAttribute("carrito");
		return (contenido==null) ? null : productoServicio.variosPorId(contenido);
	} /*
	*El carrito se obtiene de la sesión. Va a ser un listado de los IDs de los productos que queremos comprar.
	*Para poder ver los productos del carrito,buscaremos varios productos por sus IDs.
	*Si el carrito estuviese vacío, se devuelve vacío. Es decir:
	*return null si no encuentra el id de carrito almacenado en la variable contenido; en caso contrario;
	*devuelve una lista de los productos del carrito encontrados por sus id para el id de carrito recibido
	*como parámetro (contenido). 
	**/
	
//AÑADIMOS EL ATRIBUTO DEL COSTE TOTAL DE LOS PRODUCTOS DEL CARRITO,para poder visualizarlo:
	
	@ModelAttribute("total_carrito")
	public Double totalCarrito() {
		List<Producto> productosCarrito = productosCarrito();
		if(productosCarrito != null) 
			return productosCarrito.stream() //Devuelve un flujo secuencial de los elementos de esta colección
				.mapToDouble(p -> p.getPrecio()) //Devuelve un DoubleStream que consta de los resultados de aplicar la función dada a los elementos de esta secuencia.
				.sum(); //Devuelve la suma de elementos en esta secuencia.
			return 0.0;
			
		}/* Obtenemos todos los productos del carrito.
	 * Si no está vacío,mediante un stream(), vamos sacando el precio (mapeándolo a un double),los sumamos,
	 * y lo devolvemos.
	 * En caso contrario, Si productosCarrito estuviese nulo devuelve cero como coste total de no tener productos
	 * en el carrito.
	 */
	
	
//AÑADIMOS EL ATRIBUTO "MIS COMPRAS"	
	
	@ModelAttribute("mis_compras")
	public List<Compra> misCompras(){
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		usuario = usuarioServicio.buscarPorEmail(email); //Busca usuario por su email
		return compraServicio.porPropietario(usuario); //Busca compras por propietario (tb habríamos podido 
		    // implementar como método alternativo de búsqueda, el buscar por el email del propietario en
				//lugar de por el objeto propietario).
	}/*
	 * Obtenemos el listado de todas las compras de un usuario. 
	 * Para saber quién es ese usuario, a través del objeto SecurityContextHolder vamos a obtener el contexto
	 * de seguridad,y de ahí el elemento de autenticación, y el nombre (nosotros estamos usando el email como
	 * username).
	 * 
	 */
	

// MÉTODO PARA VER EL CARRITO:
	@GetMapping("/carrito")
	public String verCarrito(Model model) {
		return "app/compra/carrito"; //Sólo necesita ir a la plantilla correspondiente (carrito.html)ya que el 
	}			// resto de atributos necesarios están siempre disponibles con @ModelAttribute("carrito") y 
			//	@ModelAttribute("total_carrito") , los dos primeros métodos.
	
//AÑADIR UN PRODUCTO AL CARRITO:
	@GetMapping("/carrito/add/{id}") //en base al id del producto
	public String addCarrito(Model model,@PathVariable Long id) { //recoge id de producto
		List<Long> contenido=(List<Long>)session.getAttribute("carrito"); //Si existe carrito en la sesión,lo devuelve
				if(contenido == null) //si carrito es nulo...
					contenido=new ArrayList<>(); //crea de nuevo el contenido
				if(!contenido.contains(id))  //si el id de producto no está contenido
					contenido.add(id); //lo añadimos
				session.setAttribute("carrito", contenido); //almacena el carrito en la sesión
				return "redirect:/app/carrito"; //redirigimos a visualizar carrito
	}
	
//ELIMINAR	UN PRODUCTO DEL CARRITO:
	
	@GetMapping("/carrito/eliminar/{id}") //en base al id del producto
	public String borrarDeCarrito(Model model,@PathVariable Long id) {
		List<Long> contenido=(List<Long>)session.getAttribute("carrito"); //Si existe carrito en la sesión,lo devuelve
		if(contenido == null) //si carrito es nulo...
			return "redirect:/public"; //te envía al listado de productos
		contenido.remove(id); //en otro caso, eliminamos el id de producto
		if(contenido.isEmpty()) //si el carrito se quedó vacío tras eliminar el producto
			session.removeAttribute("carrito"); //eliminamos el carrito de la sesión
		else //sino
			session.setAttribute("carrito",contenido); //almacenamos de nuevo el carrito en la sesión
		return "redirect:/app/carrito"; // y nos redirigimos a la página del carrito
	}
	
//FINALIZAR COMPRA:
	
	@GetMapping("/carrito/finalizar") //El proceso de finalización de compra se mapea en esta ruta
	public String checkout() {
		List<Long> contenido = (List<Long>)session.getAttribute("carrito"); //Obtiene el carrito
		if(contenido == null) //Si carrito fuese nulo..
			return "redirect:/public"; //redirige a la página pública
		
		List<Producto> productos = productosCarrito();//en otro caso, obtenemos los productos del carrito
		
		Compra c = compraServicio.insertar(new Compra(),usuario); //Insertamos nueva compra asociada al usuario con el servicio de compra
	
		productos.forEach(p -> compraServicio.addProducto(p, c)); //Añadimos a la compra los diferentes productos,
			//lo cuál se realiza asignando a cada producto p la compra c a la que están relacionados.
		
		session.removeAttribute("carrito"); //elimina carrito desde la sesión
	
		return "redirect:/app/compra/factura/"+c.getId(); //Nos redirigimos hacia la factura, que es otro controlador
									//que tenemos que implementar.
	}
	
	
//VER FACTURA:
	
	@GetMapping("/compra/factura/{id}") //una factura en base al id de esa factura
	public String factura(Model model, @PathVariable Long id) {
		Compra c = compraServicio.buscarPorId(id); //Busca la compra por su id
		List<Producto> productos = productoServicio.productosDeUnaCompra(c); //Saca todos los productos de esa compra
		model.addAttribute("productos", productos); //muestra en la estructura de factura.html el listado de productos...
		model.addAttribute("compra",c); //junto con el precio...
		model.addAttribute("total_compra", productos.stream().mapToDouble(p -> p.getPrecio()).sum()); //junto con el total en euros
		return "/app/compra/factura"; //nos redirige a la vista
	}
	
//OPCIÓN DE MENÚ DE MIS COMPRAS:
		//Arriba ya teníamos el atributo mis_compras en el modelo,donde la habíamos buscado, así que
			//sólo tenemos que ir hacia la plantilla correspondiente.
	
	@GetMapping("/miscompras")
	public String verMisCompras(Model model) {
		return "/app/compra/listado"; //Nos redirige a la plantilla correspondiente
	}
	
}
