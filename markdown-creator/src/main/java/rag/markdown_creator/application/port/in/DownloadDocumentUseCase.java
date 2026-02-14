package rag.markdown_creator.application.port.in;

import rag.markdown_creator.application.vo.DownloadFile;
import rag.markdown_creator.application.vo.MarkdownDocument;

import java.util.List;

public interface DownloadDocumentUseCase {
    DownloadFile execute(List<MarkdownDocument> documents);
}
