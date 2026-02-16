import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import './css/Login.css'

function Login() {
  const navigate = useNavigate()
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setError('')
    setLoading(true)

    try {
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include', // 쿠키 포함
        body: JSON.stringify({
          username,
          password,
        }),
      })

      if (!response.ok) {
        const errorData = await response.json()
        throw new Error(errorData.message || '로그인에 실패했습니다.')
      }

      const data = await response.json()
      console.log('로그인 성공:', data)

      // 로그인 성공 시 메인 페이지로 이동
      navigate('/')
    } catch (err) {
      if (err instanceof Error) {
        setError(err.message)
      } else {
        setError('로그인 중 오류가 발생했습니다.')
      }
    } finally {
      setLoading(false)
    }
  }

  const handleSignupClick = () => {
    navigate('/signup')
  }

  return (
    <div className="login-container">
      <div className="login-card">
        {/* 로고/타이틀 */}
        <div className="login-header">
          <h1 className="login-title">My AI Platform</h1>
          <p className="login-subtitle">로그인하여 AI 서비스를 이용하세요</p>
        </div>

        {/* 로그인 폼 */}
        <form onSubmit={handleSubmit} className="login-form">
          {/* 에러 메시지 */}
          {error && (
            <div className="error-message">
              {error}
            </div>
          )}

          {/* 아이디 입력 */}
          <div className="form-group">
            <label htmlFor="username" className="form-label">
              아이디
            </label>
            <input
              type="text"
              id="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              className="form-input"
              placeholder="아이디를 입력하세요"
              required
              disabled={loading}
            />
          </div>

          {/* 비밀번호 입력 */}
          <div className="form-group">
            <label htmlFor="password" className="form-label">
              비밀번호
            </label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="form-input"
              placeholder="비밀번호를 입력하세요"
              required
              disabled={loading}
            />
          </div>

          {/* 로그인 버튼 */}
          <button
            type="submit"
            className="login-button"
            disabled={loading}
          >
            {loading ? '로그인 중...' : '로그인'}
          </button>
        </form>

        {/* 회원가입 링크 */}
        <div className="signup-section">
          <p className="signup-text">아직 회원이 아니신가요?</p>
          <button
            type="button"
            onClick={handleSignupClick}
            className="signup-button"
          >
            회원가입
          </button>
        </div>
      </div>

      {/* 푸터 */}
      <div className="login-footer">
        ❤️ by 장보경 (/w Claude, Chat GPT, Gemini...)
      </div>
    </div>
  )
}

export default Login