package rag.markdown_creator.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Service;
import rag.markdown_creator.application.port.in.SplitDocumentUseCase;

import java.util.List;

@Service
@Slf4j
public class SplitDocumentService implements SplitDocumentUseCase {

    @Override
    public List<Document> execute(List<Document> documents) {
        log.info("--------------------- 문서 청킹 진행 ---------------------");
        TokenTextSplitter splitter = new TokenTextSplitter(1000, 100, 5, 10000, true);
        return splitter.apply(documents);
    }
}
