package rag.markdown_creator.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rag.markdown_creator.application.port.in.ReadDocumentUseCase;

import java.util.List;

@Service
@Slf4j
public class ReadDocumentService implements ReadDocumentUseCase {

    @Override
    public List<Document> execute(MultipartFile file) {
        log.info("--------------------- 문서 읽기 진행: {} ---------------------", file.getOriginalFilename());
        String extension = getExtension(file);
        if(extension != null && extension.contains("pdf")) {
            return readPdf(file);
        }
        return readTika(file);
    }

    private String getExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if(fileName == null || fileName.isBlank()) {
            return null;
        }

        return fileName.substring(fileName.lastIndexOf("."));
    }

    private List<Document> readTika(MultipartFile file) {
        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(file.getResource());
        return tikaDocumentReader.read();
    }

    private List<Document> readPdf(MultipartFile file) {
        PagePdfDocumentReader pagePdfDocumentReader = new PagePdfDocumentReader(file.getResource());
        return pagePdfDocumentReader.read();
    }
}
