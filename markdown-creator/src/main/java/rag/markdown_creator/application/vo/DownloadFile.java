package rag.markdown_creator.application.vo;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.MediaType;

@Getter
@Builder
public class DownloadFile {
    private String fileName;
    private byte[] content;
    private MediaType contentType;
}
