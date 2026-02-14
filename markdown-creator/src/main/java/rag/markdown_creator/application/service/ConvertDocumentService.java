package rag.markdown_creator.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rag.markdown_creator.application.port.in.*;
import rag.markdown_creator.application.vo.MarkdownDocument;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConvertDocumentService implements ConvertDocumentUseCase {

    private final ReadDocumentUseCase readDocumentUseCase;
    private final SplitDocumentUseCase splitDocumentUseCase;
    private final GeneratePromptUseCase generatePromptUseCase;
    private final ChatUseCase chatUseCase;

    @Override
    public MarkdownDocument execute(MultipartFile file) {
        String fileName = file.getOriginalFilename() != null && !file.getOriginalFilename().isBlank()
                ? file.getOriginalFilename()+".md" : "document.md";

        // 1. 문서 읽기
        List<Document> readResult = readDocumentUseCase.execute(file);
        // 2. 문서 청킹
        List<Document> splitResult = splitDocumentUseCase.execute(readResult);
        // 3. 문서 변환
        StringBuilder markdown = new StringBuilder();
        log.info("--------------------- 문서 변환 시작 ---------------------");
//        IntStream.range(0, splitResult.size())
//                .forEach(i -> {
//                    Document document = splitResult.get(i);
//                    Prompt prompt = generatePromptUseCase.execute(document.getText());
//                    String convertResult = chatUseCase.chat(prompt);
//                    markdown.append(convertResult);
//                    log.info("# {}/{}개 Chunk 변환 완료", i + 1, splitResult.size());
//                });
        log.info("--------------------- 문서 변환 종료 ---------------------");

//        return MarkdownDocument.builder()
//                .fileName(fileName)
//                .content(markdown.toString())
//                .fileSize(markdown.toString().getBytes(StandardCharsets.UTF_8).length)
//                .build();

        return MarkdownDocument.builder()
                .fileName(fileName)
                .content(mockData())
                .fileSize(mockData().getBytes(StandardCharsets.UTF_8).length)
                .build();
    }

    private String mockData() {
        return """
                # 장보경
                
                **Backend Developer**
                
                📧 jangbokyung@naver.com \s
                📱 010-9968-3530 \s
                🔗 [GitHub](https://github.com/jang-199)
                
                ---
                
                ## 💼 경력
                
                ### KB증권 (2023.01 ~ 현재)
                **디지털본부 디지털전략부 Application 개발팀**
                
                #### 주요 프로젝트
                
                **1. 키움증권-KB증권 이관 시스템 구축** (2024.08 ~ 2024.12)
                - **역할**: Backend 개발
                - **기술 스택**: Java 17, Spring Boot, Oracle, MyBatis
                - **주요 업무**:
                  - 키움증권 고객 이관을 위한 배치 시스템 설계 및 개발
                  - 계좌 이관 프로세스 구현 (계좌개설, 잔고이관, 주문이관)
                  - 이관 현황 모니터링 및 알림 시스템 개발
                  - 데이터 검증 로직 구현
                
                **2. 비대면 계좌개설 시스템 고도화** (2023.07 ~ 2024.07)
                - **역할**: Backend 개발
                - **기술 스택**: Java 11, Spring Boot, Oracle, MyBatis, Redis
                - **주요 업무**:
                  - 비대면 계좌개설 프로세스 개선 및 최적화
                  - OCR 연동을 통한 신분증 자동 인식 구현
                  - 본인인증 모듈 개발 (PASS, 카카오인증 등)
                  - 계좌개설 현황 관리 시스템 구축
                
                **3. KB증권 모바일 트레이딩 시스템(MTS) 유지보수** (2023.01 ~ 현재)
                - **역할**: Backend 개발 및 운영
                - **기술 스택**: Java 8/11, Spring Framework, Oracle, MyBatis
                - **주요 업무**:
                  - 주문/체결 시스템 유지보수
                  - 고객 문의 및 장애 대응
                  - 정기 배치 작업 관리
                  - 성능 개선 및 최적화
                
                ---
                
                ## 🎓 학력
                
                **성균관대학교** (2017.03 ~ 2023.02)
                - 전자전기공학부 졸업
                - 학점: 3.87 / 4.5
                
                ---
                
                ## 💻 기술 스택
                
                ### Backend
                - **Languages**: Java, Python
                - **Frameworks**: Spring Boot, Spring Framework
                - **Database**: Oracle, MySQL, Redis
                - **ORM**: MyBatis, JPA
                
                ### DevOps & Tools
                - **Version Control**: Git, GitHub, GitLab
                - **Build Tools**: Gradle, Maven
                - **IDE**: IntelliJ IDEA, Eclipse
                - **Collaboration**: Jira, Confluence
                
                ### Others
                - **Architecture**: Hexagonal Architecture, Clean Architecture
                - **Testing**: JUnit, Mockito
                - **API**: REST API
                
                ---
                
                ## 🏆 자격증
                
                - **정보처리기사** (2022.11)
                - **증권투자권유자문인력** (2023.02)
                
                ---
                
                ## 📚 교육
                
                **삼성 청년 SW 아카데미(SSAFY) 9기** (2023.01 ~ 2023.12)
                - 1년 과정 SW 교육 프로그램 수료
                - 알고리즘, 자료구조, 웹 개발 교육
                - 팀 프로젝트 2회 수행
                
                ---
                
                ## 🌟 강점
                
                - **문제 해결 능력**: 복잡한 비즈니스 로직을 효율적으로 구현
                - **커뮤니케이션**: 타 부서와의 원활한 협업 경험
                - **빠른 학습**: 새로운 기술 습득 및 적용 능력
                - **책임감**: 맡은 업무에 대한 주인의식과 완성도 추구
                
                ---
                
                ## 🎯 관심 분야
                
                - Backend Architecture
                - Performance Optimization
                - Clean Code & Refactoring
                - MSA (Microservices Architecture)
                """;
    }
}
