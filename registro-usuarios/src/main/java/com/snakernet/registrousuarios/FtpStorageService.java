package com.snakernet.registrousuarios;

import java.io.IOException;
import java.util.UUID;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FtpStorageService {
    private final String server = "snakernet.net"; // Cambia esto por tu servidor FTP
    private final int port = 21; // Puerto estándar de FTP, cambia si es necesario
    @Value("${ftp.user}")
    String username;
    @Value("${ftp.pass}")
    String password;
    private final String uploadPath = "/web/web_porfolio/uploads"; // Ruta en el servidor FTP
    
    public String storeFile(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID().toString() + "." + file.getOriginalFilename();
        String fullPath = uploadPath + "/" + filename;

        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            ftpClient.login(username, password);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();

            if (!ftpClient.storeFile(fullPath, file.getInputStream())) {
                throw new RuntimeException("Failed to store file via FTP.");
            }
        } finally {
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        }

        return "http://snakernet.net/uploads/" + filename; // Devuelve la ruta completa al archivo en el servidor FTP
    }
}
