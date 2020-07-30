# MERCADO DE SEGUNDA MANO

### DESCRIPCIÓN:
Aplicación web para la compraventa de productos de segunda mano entre usuarios a través de Internet. 

![Mercado de Segunda Mano](https://berlinando.net/wp-content/uploads/2017/09/Berliner-Antikmarkt-1024x540.jpeg)


### ARQUITECTURA

* Arquitectura de Software Monolítica de cuatro capas basada en el Patrón modelo-vista-controlador. 
* Niveles de abstracción que implementa:
	- Capa de Presentación (Interfaz de Usuario). Plantillas HTML.
	- Capa de Aplicación (Capa de Servicio). Mecanismos de comunicación entre capas 	(Servicios). Entre ellos, los Controladores.
	- Capa de Lógica de Negocio (Capa de Dominio). Clases e Interfaces que añaden 	funcionalidad específica a la información extraída de la Base de Datos por los Repositorios.
	- Capa de Acceso a Datos (Capa de Persistencia). Repositorios que hacen uso del patrón 	DAO (Data Access Object).


### SOFTWARE:

* Spring Boot -como motor de toda la aplicación
* Spring Web -para establecer una estructura web de tipo Modelo-Vista-Controlador
* Spring DATA JPA -para la capa de persistencia de datos
* Hibernate -herramienta de mapeo objeto-relacional para la plataforma Java
* Spring Security -para cifrar contraseñas
* Spring Session Data Redis -almacenamiento de sesiones en el servidor de Redis (motor de BBDD NoSQL en memoria)
* Thymeleaf - motor de plantillas html para tratamiento de la información entre Vista y Controlador. 
* Bootstrap -diseño y maquetación de la interfaz de usuario (formularios, botones, cuadros, menús 	de navegación y otros elementos de diseño).
* Funciones de Jquery -librería de JavaScript para interactuar con los documentos HTML y manejar eventos.
* Servidor Web Apache Tomcat -como contenedor de servlets
* Sistema Gestor de Base de datos H2 (embebido en Memoria: no permite persistencia)
* Maven – como herramienta para la gestión y construcción de proyectos Java 


### FUNCIONES DE LA APLICACIÓN:

- Usuarios: Registro (formulario de datos personales y subida de avatar/imagen de usuario), Login y Logout.
- Consulta de Catálogo de Productos público (tanto usuario registrado como no registrado) . 
- Consulta de datos de contacto del vendedor con un click sobre la imagen de producto del catálogo.
- Buscador de productos.
- Gestión de Productos del Vendedor (Alta de nuevo producto, Baja de producto publicado).
- Carrito de la compra (Añadir producto/s, Borrar Producto/s, Confirmar Compra)
- Factura de compra.
- Historial de compras.

