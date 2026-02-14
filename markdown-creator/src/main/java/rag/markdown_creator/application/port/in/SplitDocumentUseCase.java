package rag.markdown_creator.application.port.in;

import org.springframework.ai.document.Document;

import java.util.List;

public interface SplitDocumentUseCase {
    List<Document> execute(List<Document> documents);
}
