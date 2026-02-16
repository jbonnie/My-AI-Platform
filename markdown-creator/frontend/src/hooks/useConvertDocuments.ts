import { useState } from 'react'
import { convertDocuments } from '../apis/convertDocuments'
import type { ConvertDocumentsResponse } from '../types/document'

interface UseConvertDocumentsReturn {
  documents: ConvertDocumentsResponse | null
  isConverting: boolean
  convertError: string | null
  convert: () => Promise<void>
  reset: () => void
}

export const useConvertDocuments = (files: File[]): UseConvertDocumentsReturn => {
  const [documents, setDocuments] = useState<ConvertDocumentsResponse | null>(null)
  const [isConverting, setIsConverting] = useState(false)
  const [convertError, setConvertError] = useState<string | null>(null)

  const convert = async () => {
    if (files.length === 0) {
      setConvertError('파일을 선택해주세요.')
      return
    }

    setIsConverting(true)
    setConvertError(null)

    try {
      const result = await convertDocuments(files)
      setDocuments(result)
      console.log('변환 성공:', result)
    } catch (err) {
      if (err instanceof Error) {
        setConvertError(`변환 실패: ${err.message}`)
      } else {
        setConvertError('문서 변환 중 알 수 없는 오류가 발생했습니다.')
      }
      console.error('변환 에러:', err)
      setDocuments(null)
    } finally {
      setIsConverting(false)
    }
  }

  const reset = () => {
    setDocuments(null)
    setConvertError(null)
  }

  return {
    documents,
    isConverting,
    convertError,
    convert,
    reset,
  }
}