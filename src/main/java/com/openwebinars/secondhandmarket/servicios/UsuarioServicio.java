package com.openwebinars.secondhandmarket.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.openwebinars.secondhandmarket.modelo.Usuario;
import com.openwebinars.secondhandmarket.repositorios.UsuarioRepository;

@Service //Anotación para la clase de servicio.
public class UsuarioServicio {
	
	@Autowired
	UsuarioRepository repositorio;

	@Autowired
	BCryptPasswordEncoder passwordEncoder; /*Para que cuando almacenemos un usuario en la bbdd
											*codifiquemos la contraseña recibida a través del formulario
											*de registro.
											*/
	
//MÉTODOS CRUD:
	
	public Usuario registrar(Usuario u) {  //REGISTRA UN NUEVO USUARIO.
		u.setPassword(passwordEncoder.encode(u.getPassword())); //Encripta la contraseña del usuario
		return repositorio.save(u); //almacena el usuario(con la contraseña encriptada)
	}
	
	public Usuario buscarId(long id) {//BUSCAR UN USUARIO POR SU ID
	return repositorio.findById(id).orElse(null); /* Si no pusiéramos el .orElse(null) nos daría el error
									*cannot convert from <Optional>Usuario to Usuario
									*/
	}
	public Usuario buscarPorEmail(String email) {//BUSCAR UN USUARIO POR SU EMAIL
		return repositorio.findFirstByEmail(email);
	}
}