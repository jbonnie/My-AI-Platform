package rag.markdown_creator.adapter.in.dto;

import lombok.Getter;
import rag.markdown_creator.application.vo.MarkdownDocument;

@Getter
public class DownloadDocumentRequestDto {
    private String fileName;
    private String content;

    public MarkdownDocument toVo() {
        return MarkdownDocument.builder()
                .fileName(fileName)
                .content(content)
                .build();
    }
}
