package rag.markdown_creator.application.port.in;

import org.springframework.ai.document.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReadDocumentUseCase {
    List<Document> execute(MultipartFile file);
}
