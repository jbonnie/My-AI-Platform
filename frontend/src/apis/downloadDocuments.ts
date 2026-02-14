import axios from 'axios'
import type { ConvertDocumentsResponse } from '../types/document'

interface DownloadResponse {
  blob: Blob
  fileName: string | null
}

const API_BASE_URL = import.meta.env.API_BASE_URL || 'http://localhost:8080'

export const downloadDocuments = async (documents: ConvertDocumentsResponse): Promise<DownloadResponse> => {
  const requestBody = documents.map(doc => ({
    fileName: doc.fileName,
    content: doc.content
  }))

  const response = await axios.post(`${API_BASE_URL}/api/v1/download`, requestBody, {
    responseType: 'blob',
    headers: {
      'Content-Type': 'application/json',
    },
  })

  // Content-Disposition 헤더에서 파일명 추출
  const contentDisposition = response.headers['content-disposition']
  let fileName: string | null = null

  if (contentDisposition) {
    // filename*=UTF-8'' 형식 처리
    const filenameStarMatch = /filename\*=UTF-8''([^;\n]*)/.exec(contentDisposition)
    if (filenameStarMatch && filenameStarMatch[1]) {
      fileName = decodeURIComponent(filenameStarMatch[1])
      console.log('추출된 파일명:', fileName)
    } else {
      // 일반 filename 형식도 시도
      const matches = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/.exec(contentDisposition)
      if (matches && matches[1]) {
        fileName = matches[1].replace(/['"]/g, '')
      }
    }
  } else {
    console.log('추출된 파일명 없음')
  }

  return {
    blob: response.data,
    fileName
  }
}