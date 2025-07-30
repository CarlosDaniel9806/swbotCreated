package com.web.swbotv1.Service;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadFileService {
    Resource load(String filename) throws MalformedURLException; // sirve paa cargar las imagenes

    String copy(MultipartFile file) throws IOException; //sirve para copiar las imagenes en la carpeta local y deveuelve el nombre del archivo guardado

    boolean delete(String filename); // sirve para borrar las imagenes si existen
}
