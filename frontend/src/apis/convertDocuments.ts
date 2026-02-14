import axios from 'axios'
import type { ConvertDocumentsResponse } from '../types/document'

const API_BASE_URL = import.meta.env.API_BASE_URL || 'http://localhost:8080'

export const convertDocuments = async (files: File[]): Promise<ConvertDocumentsResponse> => {
  const formData = new FormData()

  files.forEach((file) => {
    formData.append('files', file)
  })

  const response = await axios.post<ConvertDocumentsResponse>(
    `${API_BASE_URL}/api/v1/convert`,
    formData,
    {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
      timeout: 60000 * 5, // 5분
    }
  )

  return response.data
}