package com.web.swbotv1.Service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.web.swbotv1.Service.IUploadFileService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UploadFileServiceImpl implements IUploadFileService {

    private static final String UPLOADS_FOLDER = "uploads";
    private final Path rootPath = Paths.get(System.getProperty("user.dir")).resolve(UPLOADS_FOLDER).toAbsolutePath().normalize();


    @Override
public Resource load(String filename) throws MalformedURLException {
    try {
        Path filePath = rootPath.resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("No se pudo cargar la imagen: " + filename);
        }
        
        // Verificar que el archivo esté dentro del directorio raíz (seguridad)
        if (!filePath.getParent().equals(rootPath.toAbsolutePath())) {
            throw new RuntimeException("No se puede acceder al archivo solicitado");
        }
        
        return resource;
    } catch (MalformedURLException e) {
        throw new RuntimeException("Error al cargar la imagen: " + filename, e);
    }
}

    @Override
public String copy(MultipartFile file) throws IOException {
    // Validar que el archivo no esté vacío
    if (file == null || file.isEmpty()) {
        throw new IOException("El archivo está vacío");
    }

    // Validar que sea una imagen
    String contentType = file.getContentType();
    if (contentType == null || !contentType.startsWith("image/")) {
        throw new IOException("El archivo debe ser una imagen válida (JPEG, PNG, etc.)");
    }

    // Validar tamaño máximo (5MB)
    long maxFileSize = 5 * 1024 * 1024; // 5MB
    if (file.getSize() > maxFileSize) {
        throw new IOException("El tamaño del archivo no debe exceder los 5MB");
    }

    // Validar extensión del archivo
    String originalFilename = file.getOriginalFilename();
    if (originalFilename == null || originalFilename.isEmpty()) {
        throw new IOException("Nombre de archivo no válido");
    }

    // Obtener extensión
    String fileExtension = "";
    int lastDot = originalFilename.lastIndexOf('.');
    if (lastDot > 0) {
        fileExtension = originalFilename.substring(lastDot).toLowerCase();
    }
    
    // Validar extensiones permitidas
    List<String> allowedExtensions = Arrays.asList(".jpg", ".jpeg", ".png", ".gif");
    if (!allowedExtensions.contains(fileExtension)) {
        throw new IOException("Formato de archivo no soportado. Use JPG, JPEG, PNG o GIF");
    }

    // Crear directorio si no existe
    if (!Files.exists(rootPath)) {
        try {
            Files.createDirectories(rootPath);
        } catch (IOException e) {
            throw new IOException("No se pudo crear el directorio de destino", e);
        }
    }

    // Generar nombre único
    String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
    Path destinationFile = rootPath.resolve(uniqueFilename).normalize();

    // Validar que el archivo se guarde dentro del directorio raíz (seguridad)
    if (!destinationFile.getParent().equals(rootPath.toAbsolutePath())) {
        throw new IOException("No se puede guardar el archivo fuera del directorio de destino");
    }

    // Guardar el archivo
    try (InputStream inputStream = file.getInputStream()) {
        Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        log.info("Archivo guardado exitosamente: {}", uniqueFilename);
        return uniqueFilename;
    } catch (IOException e) {
        // Intentar limpiar en caso de error
        Files.deleteIfExists(destinationFile);
        throw new IOException("Error al guardar el archivo: " + e.getMessage(), e);
    }
}

@Override
public boolean delete(String filename) {
    if (filename == null || filename.isEmpty()) {
        log.warn("Intento de eliminar archivo con nombre vacío");
        return false;
    }

    try {
        Path filePath = rootPath.resolve(filename).normalize();
        
        // Validar que el archivo esté dentro del directorio raíz (seguridad)
        if (!filePath.getParent().equals(rootPath.toAbsolutePath())) {
            log.warn("Intento de eliminar archivo fuera del directorio permitido: {}", filename);
            return false;
        }

        boolean deleted = Files.deleteIfExists(filePath);
        if (deleted) {
            log.info("Archivo eliminado exitosamente: {}", filename);
        } else {
            log.warn("No se pudo eliminar el archivo, no existe: {}", filename);
        }
        return deleted;
    } catch (IOException e) {
        log.error("Error al intentar eliminar el archivo: " + filename, e);
        return false;
    }
}
}
