package me.guojiang.blogbackend.Services;

import me.guojiang.blogbackend.Exceptions.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {

    @Value("${file.upload-dir}")
    private String uploadFileDir;

    @Value("${file.host}")
    private String host;

    @Value("${server.port}")
    private String port;

    public String store(MultipartFile file) {
        //TODO do refactor
        if (!Files.isDirectory(Paths.get(uploadFileDir), LinkOption.NOFOLLOW_LINKS)) {
            try {
                Files.createDirectories(Paths.get(uploadFileDir));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        var copyPath = Paths.get(uploadFileDir + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
        try {
            Files.copy(file.getInputStream(), copyPath, StandardCopyOption.REPLACE_EXISTING);
            return host + ":" + port + "/img/" + file.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileStorageException("could not save the file " + file.getOriginalFilename());
        }
    }
}
