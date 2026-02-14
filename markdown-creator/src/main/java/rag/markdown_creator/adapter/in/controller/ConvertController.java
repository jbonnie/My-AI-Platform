package rag.markdown_creator.adapter.in.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import rag.markdown_creator.adapter.in.dto.MarkdownDocumentResponseDto;
import rag.markdown_creator.application.port.in.ConvertDocumentUseCase;
import rag.markdown_creator.application.vo.MarkdownDocument;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ConvertController {

    private final ConvertDocumentUseCase convertDocumentUseCase;

    // 문서 변환
    @PostMapping("/api/v1/convert")
    public ResponseEntity<List<MarkdownDocumentResponseDto>> convert(List<MultipartFile> files) {
        List<MarkdownDocument> results = files.stream().map(convertDocumentUseCase::execute).toList();
        List<MarkdownDocumentResponseDto> responseDtos = results.stream()
                .map(MarkdownDocumentResponseDto::from).toList();
        return ResponseEntity.ok(responseDtos);
    }
}
