package com.openwebinars.secondhandmarket.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.openwebinars.secondhandmarket.modelo.Usuario;
import com.openwebinars.secondhandmarket.servicios.UsuarioServicio;
import com.openwebinars.secondhandmarket.upload.StorageService;

@Controller  //Anotación de controlador
public class LoginController {

	@Autowired  //Inyectamos servicio de usuario
	UsuarioServicio usuarioServicio;
	
	@Autowired
	StorageService storageService; //Inyectamos servicio de almacenamiento para gestión de imagen/avatar.
	
	
	@GetMapping("/") //Si alguien accede directo a la ruta raíz , redirigimos a la ruta pública , que la haremos 
	public String welcome() {					//en un controlador posterior, y que será el listado de todos.
	return "redirect:/public/";						//los productos.
	}
	
	@GetMapping("/auth/login") //MOSTRAR FORMULARIO DE LOGIN (con la seguridad incorporada en la ruta) 
	public String login(Model model) {      //Lo hacemos así porque formulario de login y de registro es el mismo.
		model.addAttribute("usuario", new Usuario()); //Command Object del usuario que queremos crear.
		return "login"; //redirige a plantilla de login
	}
	
	//Ahora hacemos el postMapping, pero no de login (que está dentro del circuito de SpringSecurity y 
	//nosotros no lo tenemos que implementar) sino del registro:
	
	@PostMapping("/auth/register") //RUTA DEL REGISTRO
	public String register(@ModelAttribute Usuario usuario, @RequestParam("file") MultipartFile file) { //inyectamos copmo parámetro el usuario que hemos creado antes; y 2do parametro es el fichero avatar de imagen del usuario.
		if(!file.isEmpty()){
			String imagen= storageService.store(file);
			usuario.setAvatar(MvcUriComponentsBuilder
					.fromMethodName(FilesController.class,"serveFile",imagen).build().toUriString());
		}
	usuarioServicio.registrar(usuario); //Registramos el usuario
	return "redirect:/auth/login"; //Una vez registrado,lo enviamos al login.
	} /*En lugar de mandar al usuario registrado al login, lo suyo sería enviarlo a su página principal pero
	* pero tendríamos que saber más de springSecurity y del servicio de autenticación, lo que no abarca el curso,
	* por lo que lo pasamos forzosamente por el formulario de login para poder acceder.
	* Con este método de registro que incorpora BCrypt ya estaríamos codificando la contraseña para poder
	* almacenarla.
	*/
}
