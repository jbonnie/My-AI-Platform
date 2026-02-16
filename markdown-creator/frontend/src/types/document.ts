/* 문서 변환 */
export interface MarkdownDocument {
  fileName: string
  fileSize: number
  content: string
}

export type ConvertDocumentsResponse = MarkdownDocument[]

/* 문서 다운로드 */
export interface DownloadDocument {
  fileName: string
  content: string
}

export type DownloadDocumentsRequest = DownloadDocument[]
