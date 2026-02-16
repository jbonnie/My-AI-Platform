/* 문서 변환 */
interface MarkdownDocument {
  fileName: string
  fileSize: number
  content: string
}

export type ConvertDocumentsResponseDto = MarkdownDocument[]

/* 문서 다운로드 */
interface DownloadResponse {
  blob: Blob
  fileName: string | null
}

export type DownloadResponseDto = DownloadResponse