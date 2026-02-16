import { useState } from 'react'
import { downloadDocuments } from '../apis/downloadDocuments'
import type { ConvertDocumentsResponse } from '../types/document'

interface UseDownloadDocumentsReturn {
  isDownloading: boolean
  downloadError: string | null
  download: (documents: ConvertDocumentsResponse) => Promise<void>
}

export const useDownloadDocuments = (): UseDownloadDocumentsReturn => {
  const [isDownloading, setIsDownloading] = useState(false)
  const [downloadError, setDownloadError] = useState<string | null>(null)

  const download = async (documents: ConvertDocumentsResponse) => {
    if (documents.length === 0) {
      setDownloadError('다운로드할 문서가 없습니다.')
      return
    }

    setIsDownloading(true)
    setDownloadError(null)

    await new Promise(resolve => setTimeout(resolve, 3000))
    try {
      const response = await downloadDocuments(documents)

      console.log('다운로드 응답:', response)

      // Blob을 다운로드
      const url = URL.createObjectURL(response.blob)
      const link = document.createElement('a')
      link.href = url

      // 파일명 결정 (헤더에서 가져오거나 폴백)
      const fileName = response.fileName ||
        (documents.length === 1
          ? `${documents[0].fileName.replace(/\.[^/.]+$/, '')}.md`
          : `documents_${new Date().getTime()}.zip`)

      link.download = fileName

      // 다운로드 실행
      document.body.appendChild(link)
      link.click()

      // 정리
      document.body.removeChild(link)
      URL.revokeObjectURL(url)

      console.log('다운로드 성공:', fileName)
    } catch (err) {
      console.error('다운로드 에러 상세:', err)
      if (err instanceof Error) {
        setDownloadError(`다운로드 실패: ${err.message}`)
      } else {
        setDownloadError('문서 다운로드 중 알 수 없는 오류가 발생했습니다.')
      }
    } finally {
      setIsDownloading(false)
    }
  }

  return {
    isDownloading,
    downloadError,
    download,
  }
}