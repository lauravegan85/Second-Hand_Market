package com.openwebinars.secondhandmarket.upload;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


/**
 * Implementación de un {@link StorageService} que almacena
 * los ficheros subidos dentro del servidor donde se ha desplegado
 * la apliacación.
 * 
 * ESTO SE REALIZA ASÍ PARA NO HACER MÁS COMPLEJO EL EJEMPLO.
 * EN UNA APLICACIÓN EN PRODUCCIÓN POSIBLEMENTE SE UTILICE
 * UN ALMACÉN REMOTO.
 * 
 * 
 * @author Equipo de desarrollo de Spring
 *
 */
@Service
public class FileSystemStorageService implements StorageService{

	// Directorio raiz de nuestro almacén de ficheros 
	private final Path rootLocation;

		
    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

 
    
    /**
     * Método que almacena un fichero en el almacenamiento secundario
     * desde un objeto de tipo {@link org.springframework.web.multipart#MultipartFile} MultipartFile
     * 
     * Modificamos el original del ejemplo de Spring para cambiar el nombre
     * del fichero a almacenar. Como lo asociamos al Empleado que se ha
     * dado de alta, usaremos el ID de empleado como nombre de fichero.
     * 
     * En lugar de utilizar el id del fichero correspondiente usamos el nombre del fichero subido 
     * por el usuario tomando junto con él la fecha del sistema, para evitar colisiones entre ficheros 
     * de usuarios que puedan tener igual nombre.
     */
    
    @Override
    public String store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename()); //cleanPath normaliza la ruta suprimiendo secuencias como "ruta / .." y puntos simples internos, y devuelve la ruta normalizada. getOriginalFileName()->Devuelve el nombre de archivo original en el sistema de archivos del cliente.
        String extension = StringUtils.getFilenameExtension(filename); //extrae extensión del archivo, por ej: "mypath/myfile.txt" -> "txt".
        String justFilename = filename.replace("."+extension, ""); //replace recibe como parámetros: (La secuencia de valores de caracteres a ser reemplazados , La secuencia de reemplazo de valores de caracteres) -> "le quita la extensión al nombre y la reemplaza por vacío"->o lo que es lo mismo, le quita la extensión al nombre
        String storedFilename = System.currentTimeMillis() + "_" + justFilename + "." + extension; //DatoFecha+nombreArchivo+.+extension
        try {
            if (file.isEmpty()) { //fichero vacío
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(storedFilename),
                    StandardCopyOption.REPLACE_EXISTING);
                return storedFilename;
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
        
    }

    /**
     * Método que devuelve la ruta de todos los ficheros que hay
     * en el almacenamiento secundario del proyecto.
     */
    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                .filter(path -> !path.equals(this.rootLocation))
                .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    /**
     * Método que es capaz de cargar un fichero a partir de su nombre
     * Devuelve un objeto de tipo Path
     */
    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    
    /**
     * Método que es capaz de cargar un fichero a partir de su nombre
     * Devuelve un objeto de tipo Resource
     */
    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    
    /**
     * Método que elimina todos los ficheros del almacenamiento
     * secundario del proyecto.
     */
    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    
    /**
     * Método que inicializa el almacenamiento secundario del proyecto
     */
    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }


//ELIMINAR UN FICHERO PARTICULAR
    //(Para eliminar el fichero de la imagen del producto,cuando eliminemos el producto):
	@Override
	public void delete(String filename) {
		String justFilename = StringUtils.getFilename(filename);
		try {
			Path file = load(justFilename);
			Files.deleteIfExists(file);
		} catch (IOException e) {
			throw new StorageException("Error al eliminar un fichero", e);
		}
		
	}

}
