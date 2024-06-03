package com.snakernet.registrousuarios;

import java.io.IOException;
import java.util.UUID;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servicio para almacenar archivos en un servidor FTP.
 */
@Service
public class FtpStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FtpStorageService.class);

    private final String server = "snakernet.net"; // Cambia esto por tu servidor FTP
    private final int port = 21; // Puerto estándar de FTP, cambia si es necesario
    @Value("${ftp.user}")
    String username;
    @Value("${ftp.pass}")
    String password;
    private final String uploadPath = "/web/web_porfolio/uploads"; // Ruta en el servidor FTP

    /**
     * Almacena un archivo en el servidor FTP.
     *
     * @param file el archivo a almacenar
     * @return la URL completa del archivo almacenado en el servidor FTP
     * @throws IOException si ocurre un error al almacenar el archivo
     */
    public String storeFile(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID().toString() + "." + file.getOriginalFilename();
        String fullPath = uploadPath + "/" + filename;

        FTPClient ftpClient = new FTPClient();
        try {
            logger.debug("Conectando al servidor FTP: {}:{}", server, port);
            ftpClient.connect(server, port);
            ftpClient.login(username, password);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            logger.debug("Subiendo archivo a la ruta: {}", fullPath);

            if (!ftpClient.storeFile(fullPath, file.getInputStream())) {
                logger.error("Fallo al almacenar el archivo: {}", fullPath);
                throw new RuntimeException("Failed to store file via FTP.");
            }
            logger.debug("Archivo subido exitosamente: {}", fullPath);
        } catch (IOException e) {
            logger.error("Error de IO al almacenar el archivo", e);
            throw e;
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.logout();
                    ftpClient.disconnect();
                    logger.debug("Desconectado del servidor FTP");
                } catch (IOException e) {
                    logger.warn("Error al desconectar del servidor FTP", e);
                }
            }
        }

        return "http://snakernet.net/uploads/" + filename; // Devuelve la ruta completa al archivo en el servidor FTP
    }
}
