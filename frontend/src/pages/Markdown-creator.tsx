import { useState, useRef, useEffect } from 'react'
import FileUpload from '../components/FileUpload'
import type { FileUploadRef } from '../components/FileUpload'
import Spinner from '../components/Spinner'
import { useConvertDocuments } from '../hooks/useConvertDocuments'
import { useDownloadDocuments } from '../hooks/useDownloadDocuments'
import './css/Markdown-creator.css'

function MarkdownCreator() {
  const [selectedFiles, setSelectedFiles] = useState<File[]>([])
  const [currentPage, setCurrentPage] = useState(0) // 현재 페이지
  const fileUploadRef = useRef<FileUploadRef>(null)
  const { documents, isConverting, convertError, convert, reset } = useConvertDocuments(selectedFiles)
  const { isDownloading, downloadError, download } = useDownloadDocuments()

  const handleFilesSelect = (files: File[]) => {
    setSelectedFiles(files)
    reset()
    setCurrentPage(0) // 새 파일 선택 시 페이지 초기화
  }

  // 문서 변환
  const handleConvert = async () => {
    await convert()
    setCurrentPage(0) // 변환 후 첫 페이지로
  }

  // 새 문서 변환
  const handleConvertNewFile = () => {
    setSelectedFiles([])
    reset()
    fileUploadRef.current?.reset()
    setCurrentPage(0)
  }

  // 결과 다운로드
  const handleDownload = async () => {
    if (!documents || documents.length === 0) return
    await download(documents)
  }

  const handlePrevPage = () => {
    setCurrentPage(prev => Math.max(0, prev - 1))
  }

  const handleNextPage = () => {
    if (documents) {
      setCurrentPage(prev => Math.min(documents.length - 1, prev + 1))
    }
  }

  const currentDocument = documents && documents[currentPage]

  // 로딩 시작 시 맨 아래로 스크롤
  useEffect(() => {
    if (isConverting) {
      window.scrollTo({
        top: document.documentElement.scrollHeight,
        behavior: 'smooth'
      })
    }
  }, [isConverting])

  // 변환 완료 시 맨 위로 스크롤
  useEffect(() => {
    if (documents && documents.length > 0) {
      window.scrollTo({
        top: 0,
        behavior: 'smooth'
      })
    }
  }, [documents])

  return (
    <div className="page-container">
      <h1 className="page-title">Document Converter</h1>
      <p className="page-subtitle">Transform your documents into markdown format</p>

      {!documents && <FileUpload ref={fileUploadRef} onFilesSelect={handleFilesSelect} />}

      {(convertError || downloadError) && (
        <div className="error-message" style={{ marginTop: '20px', maxWidth: '600px' }}>
          ⚠️ {convertError ? convertError : downloadError}
        </div>
      )}

      {selectedFiles.length > 0 && !isConverting && !documents && (
        <div className="button-container">
          <button className="convert-button" onClick={handleConvert}>
            변환하기
          </button>
        </div>
      )}

      {isConverting && (
        <Spinner
          size="large"
          message="문서 변환 중..."
        />
      )}

      {currentDocument && (
        <div style={{ marginTop: '40px', color: '#e2e8f0', width: '100%', maxWidth: '800px' }}>
            {/* 변환 결과 헤더 */}
                <div style={{
                  display: 'flex',
                  alignItems: 'center',
                  gap: '12px',
                  marginBottom: '24px',
                  paddingBottom: '16px',
                  borderBottom: '2px solid rgba(102, 126, 234, 0.3)'
                }}>
                  <span style={{ fontSize: '24px' }}>✨</span>
                  <h2 style={{
                    fontSize: '24px',
                    fontWeight: '700',
                    margin: 0,
                    background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                    WebkitBackgroundClip: 'text',
                    WebkitTextFillColor: 'transparent',
                    backgroundClip: 'text'
                  }}>
                    변환 결과
                  </h2>
                </div>

          {/* 문서 내용 */}
          <div
            style={{
              marginBottom: '30px',
              background: 'rgba(30, 41, 59, 0.5)',
              padding: '20px',
              borderRadius: '12px',
              border: '1px solid rgba(148, 163, 184, 0.1)'
            }}
          >
            <h3 style={{ marginBottom: '10px', color: '#e2e8f0' }}>
              {currentDocument.fileName}
            </h3>
            <p style={{ marginBottom: '15px', color: '#94a3b8', fontSize: '14px' }}>
              크기: {(currentDocument.fileSize / 1024).toFixed(2)} KB
            </p>
            <pre style={{
              background: 'rgba(15, 23, 42, 0.5)',
              padding: '20px',
              borderRadius: '8px',
              overflow: 'auto',
              maxHeight: '500px',
              color: '#e2e8f0',
              fontSize: '14px',
              lineHeight: '1.6',
              whiteSpace: 'pre-wrap',
              wordBreak: 'break-word'
            }}>
              {currentDocument.content}
            </pre>
          </div>

          {/* 페이지 점 인디케이터 (선택사항) */}
          {documents && documents.length > 1 && (
            <div style={{
              display: 'flex',
              justifyContent: 'center',
              gap: '8px',
              marginBottom: '20px'
            }}>
              {documents.map((_, index) => (
                <button
                  key={index}
                  onClick={() => setCurrentPage(index)}
                  style={{
                    width: '10px',
                    height: '10px',
                    borderRadius: '50%',
                    border: 'none',
                    background: index === currentPage
                      ? 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
                      : 'rgba(148, 163, 184, 0.3)',
                    cursor: 'pointer',
                    padding: 0,
                    transition: 'all 0.3s'
                  }}
                  aria-label={`${index + 1}페이지로 이동`}
                />
              ))}
            </div>
          )}

          {/* 페이지 인디케이터 */}
          <div className="pagination-nav">
            <button
              className="pagination-arrow-button"
              onClick={handlePrevPage}
              disabled={currentPage === 0}
            >
              이전
            </button>

            <div className="pagination-counter">
              <span className="pagination-counter-text">
                {currentPage + 1} / {documents?.length}
              </span>
            </div>

            <button
              className="pagination-arrow-button"
              onClick={handleNextPage}
              disabled={!documents || currentPage === documents.length - 1}
            >
              다음
            </button>
          </div>

          <div className="button-container" style={{
            display: 'flex',
            gap: '16px',
            marginTop: '20px',
            justifyContent: 'center'
          }}>
            <button
              className="convert-button"
              onClick={handleDownload}
              disabled={isDownloading}
            >
              {isDownloading ? '다운로드 중...' : '결과 다운로드'}
            </button>

            <button
              className="convert-button"
              onClick={handleConvertNewFile}
            >
              새 문서 변환
            </button>
          </div>
        </div>
      )}
    </div>
  )
}

export default MarkdownCreator