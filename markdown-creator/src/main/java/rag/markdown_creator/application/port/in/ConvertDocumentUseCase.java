package rag.markdown_creator.application.port.in;

import org.springframework.web.multipart.MultipartFile;
import rag.markdown_creator.application.vo.MarkdownDocument;

public interface ConvertDocumentUseCase {
    MarkdownDocument execute(MultipartFile file);
}
