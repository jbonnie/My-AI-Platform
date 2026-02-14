package rag.markdown_creator.application.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import rag.markdown_creator.adapter.in.dto.DownloadDocumentRequestDto;
import rag.markdown_creator.application.port.in.DownloadDocumentUseCase;
import rag.markdown_creator.application.vo.DownloadFile;
import rag.markdown_creator.application.vo.MarkdownDocument;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
public class DownloadDocumentService implements DownloadDocumentUseCase {

    @Override
    public DownloadFile execute(List<MarkdownDocument> documents) {
        if(documents.isEmpty()) {
            return null;
        }

        log.info("문서 다운로드 요청: {}개", documents.size());

        // 단일 파일
        if(documents.size() < 2) {
            MarkdownDocument document = documents.get(0);
            return DownloadFile.builder()
                    .fileName(validated(document.getFileName(), "document.md"))
                    .content(document.getContent().getBytes(StandardCharsets.UTF_8))
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .build();
        }

        // 여러 파일 > 압축파일
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ZipOutputStream zos = new ZipOutputStream(baos)) {
                for (MarkdownDocument doc : documents) {
                    ZipEntry entry = new ZipEntry(doc.getFileName());
                    zos.putNextEntry(entry);
                    zos.write(doc.getContent().getBytes(StandardCharsets.UTF_8));
                    zos.closeEntry();
                }
            }
            byte[] zipBytes = baos.toByteArray();

            return DownloadFile.builder()
                    .fileName("documents_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".zip")
                    .content(zipBytes)
                    .contentType(MediaType.parseMediaType("application/zip"))
                    .build();
        } catch(IOException e) {
            throw new RuntimeException("문서 다운로드를 위한 압축파일 생성 중 문제가 발생하였습니다.");
        }
    }

    private String validated(String target, String alternative) {
        if(target != null && !target.isEmpty()) {
            return target;
        }
        return alternative;
    }
}
