package com.snakernet.registrousuarios;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageStorageService {

	private final Path rootLocation;

	public ImageStorageService() {
		this.rootLocation = Paths.get("src/main/resources/static/uploads");

		try {
			Files.createDirectories(rootLocation);
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize storage", e);
		}
	}

	public String storeFile(MultipartFile file) {
		String filename = UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
		try {
			if (file.isEmpty()) {
				throw new RuntimeException("Failed to store empty file.");
			}
			Path destinationFile = this.rootLocation.resolve(Paths.get(filename)).normalize().toAbsolutePath();
			if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
				throw new RuntimeException("Cannot store file outside current directory.");
			}
			file.transferTo(destinationFile);
			return "/uploads/" + filename;
		} catch (IOException e) {
			throw new RuntimeException("Failed to store file.", e);
		}
	}
}
