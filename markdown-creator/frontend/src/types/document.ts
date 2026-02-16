/* 문서 변환 */
export interface MarkdownDocument {
  fileName: string
  fileSize: number
  content: string
}

export type ConvertDocumentsResponseDto = MarkdownDocument[]
