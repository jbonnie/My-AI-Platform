import { useRef, useState, useImperativeHandle, forwardRef } from 'react'
import './css/FileUpload.css'

export interface FileUploadRef {
  reset: () => void
}

interface FileUploadProps {
  onFilesSelect: (files: File[]) => void
  acceptedTypes?: string[]
  maxSizeMB?: number
}

const FileUpload = forwardRef<FileUploadRef, FileUploadProps>(({
  onFilesSelect,
  acceptedTypes = ['.ppt', '.pptx', '.pdf', '.doc', '.docx', '.xls', '.xlsx', '.hwp', '.hwpx', '.txt'],
  maxSizeMB = 10
}, ref) => {
  const [selectedFiles, setSelectedFiles] = useState<File[]>([])
  const [dragActive, setDragActive] = useState(false)
  const [error, setError] = useState<string>('')
  const fileInputRef = useRef<HTMLInputElement>(null)

  // 부모 컴포넌트에서 호출할 수 있는 reset 함수
  useImperativeHandle(ref, () => ({
    reset: () => {
      setSelectedFiles([])
      setError('')
      if (fileInputRef.current) {
        fileInputRef.current.value = ''
      }
    }
  }))

  const validateFile = (file: File): boolean => {
    const fileSizeMB = file.size / (1024 * 1024)
    if (fileSizeMB > maxSizeMB) {
      setError(`${file.name}는 ${maxSizeMB}MB를 초과합니다.`)
      return false
    }

    const extension = '.' + file.name.split('.').pop()?.toLowerCase()
    if (!acceptedTypes.includes(extension)) {
      setError(`${file.name}는 지원하지 않는 파일 형식입니다.`)
      return false
    }

    return true
  }

  const handleFiles = (files: FileList | null) => {
    if (!files) return

    setError('')
    const fileArray = Array.from(files)
    const validFiles = fileArray.filter(validateFile)

    if (validFiles.length > 0) {
      const newFiles = [...selectedFiles, ...validFiles]
      setSelectedFiles(newFiles)
      onFilesSelect(newFiles)
    }
  }

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    handleFiles(e.target.files)
  }

  const handleDrag = (e: React.DragEvent) => {
    e.preventDefault()
    e.stopPropagation()
    if (e.type === 'dragenter' || e.type === 'dragover') {
      setDragActive(true)
    } else if (e.type === 'dragleave') {
      setDragActive(false)
    }
  }

  const handleDrop = (e: React.DragEvent) => {
    e.preventDefault()
    e.stopPropagation()
    setDragActive(false)
    handleFiles(e.dataTransfer.files)
  }

  const handleClick = () => {
    fileInputRef.current?.click()
  }

  const removeFile = (index: number) => {
    const newFiles = selectedFiles.filter((_, i) => i !== index)
    setSelectedFiles(newFiles)
    onFilesSelect(newFiles)
  }

  const formatFileSize = (bytes: number): string => {
    if (bytes === 0) return '0 Bytes'
    const k = 1024
    const sizes = ['Bytes', 'KB', 'MB', 'GB']
    const i = Math.floor(Math.log(bytes) / Math.log(k))
    return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i]
  }

  return (
    <div className="file-upload-container">
      <div
        className={`file-upload-area ${dragActive ? 'drag-active' : ''}`}
        onDragEnter={handleDrag}
        onDragLeave={handleDrag}
        onDragOver={handleDrag}
        onDrop={handleDrop}
        onClick={handleClick}
      >
        <input
          ref={fileInputRef}
          type="file"
          multiple
          accept={acceptedTypes.join(',')}
          onChange={handleFileChange}
          style={{ display: 'none' }}
        />

        <div className="upload-icon">📁</div>
        <p className="upload-text">
          파일을 드래그하거나 클릭하여 업로드하세요
        </p>
        <p className="upload-hint">
          지원 형식: {acceptedTypes.join(', ')} (최대 {maxSizeMB}MB)
        </p>
      </div>

      {error && (
        <div className="error-message">
          ⚠️ {error}
        </div>
      )}

      {selectedFiles.length > 0 && (
        <div className="file-list">
          <h3>선택된 파일 ({selectedFiles.length}개)</h3>
          <ul>
            {selectedFiles.map((file, index) => (
              <li key={index} className="file-item">
                <div className="file-info">
                  <span className="file-name">{file.name}</span>
                  <span className="file-size">{formatFileSize(file.size)}</span>
                </div>
                <button
                  className="remove-button"
                  onClick={(e) => {
                    e.stopPropagation()
                    removeFile(index)
                  }}
                >
                  ✕
                </button>
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  )
})

FileUpload.displayName = 'FileUpload'

export default FileUpload