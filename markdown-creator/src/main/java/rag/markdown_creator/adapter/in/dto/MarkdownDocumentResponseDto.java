package rag.markdown_creator.adapter.in.dto;

import lombok.Builder;
import lombok.Getter;
import rag.markdown_creator.application.vo.MarkdownDocument;

@Getter
@Builder
public class MarkdownDocumentResponseDto {
    private String fileName;
    private int fileSize;
    private String content;

    public static MarkdownDocumentResponseDto from(MarkdownDocument document) {
        return MarkdownDocumentResponseDto.builder()
                .fileName(document.getFileName())
                .fileSize(document.getFileSize())
                .content(document.getContent())
                .build();
    }
}
