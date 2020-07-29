package com.openwebinars.secondhandmarket.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.openwebinars.secondhandmarket.modelo.Producto;
import com.openwebinars.secondhandmarket.servicios.ProductoServicio;

@Controller
@RequestMapping("/public") //Como todas las rutas del controlador,llevará el prefijo public.
						//Lo ponemos directamente aqui para no tener que ponerlo en todos y c/u de los
					//métodos de este controlador.	
public class ZonaPublicaController {

	@Autowired 
	ProductoServicio productoServicio;
	
	  						//Método anotado con ModelAttribute para que una vez invoquemos al controlador
	@ModelAttribute("productos")			//tengamos el listado de productos sin vender disponible.
	public List<Producto> productosNoVendidos(){      //LISTA LOS PRODUCTOS SIN VENDER
		return productoServicio.productosSinVender();
	}
	
	@GetMapping({"/","/index"}) //lo escuchamos en las dos rutas.
	public String index(Model model, @RequestParam(name="q", required=false) String query) { //la query no es obligatoria
		if(query != null) //Si la query es distinta de null...
			model.addAttribute("productos", productoServicio.buscar(query)); //devuelve el listado con esa query  
				//En otro caso, si query es nula, estará tirando del listado de productos sin vender de antes.
		return "index"; //pasa los productos a la vista, donde los irá dibujando.
	}
	
	@GetMapping("/producto/{id}")
	public String showProduct(Model model,@PathVariable Long id) { //PathVariable del id del producto
		Producto result= productoServicio.findById(id); //Busca el producto por su id
		if(result != null) { //Si encontramos el producto...
			model.addAttribute("producto",result); //...lo añadimos al modelo para que lo pueda visualizar.
			return "producto";
		}                              //Y si no lo encontramos...
		return "redirect:/public"; //..redirigimos a la página pública.
	}
}
