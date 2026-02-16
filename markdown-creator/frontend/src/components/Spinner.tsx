import './css/Spinner.css'

interface SpinnerProps {
  size?: 'small' | 'medium' | 'large'
  message?: string
}

function Spinner({ size = 'medium', message = '로딩 중...' }: SpinnerProps) {
  return (
    <div className="spinner-container">
      <div className={`spinner spinner-${size}`}></div>
      {message && <p className="spinner-message">{message}</p>}
    </div>
  )
}

export default Spinner