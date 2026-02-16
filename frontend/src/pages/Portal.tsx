import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { FileText, Bot } from 'lucide-react'
import './css/Portal.css'

interface Service {
  id: string
  name: string
  icon: React.ReactNode
  url: string
  description: string
}

function Portal() {
  const navigate = useNavigate()
  const [username, setUsername] = useState<string>('')
  const [currentService, setCurrentService] = useState<string | null>(null)

  useEffect(() => {
    // 쿠키에서 토큰 확인
    const accessToken = getCookie('accessToken')
    const refreshToken = getCookie('refreshToken')
    const storedUsername = getCookie('username')

    // 토큰이 없으면 로그인 페이지로 리다이렉트
    if (!accessToken || !refreshToken) {
      navigate('/login')
      return
    }

    // 사용자 이름 설정
    if (storedUsername) {
      setUsername(storedUsername)
    }
  }, [navigate])

  // 쿠키에서 값 가져오기
  const getCookie = (name: string): string | null => {
    const value = `; ${document.cookie}`
    const parts = value.split(`; ${name}=`)
    if (parts.length === 2) {
      return parts.pop()?.split(';').shift() || null
    }
    return null
  }

  // 서비스 목록
  const services: Service[] = [
    {
      id: 'markdown',
      name: 'Markdown Creator',
      icon: <FileText size={64} />,
      url: 'http://localhost:3001',
      description: '문서를 마크다운으로 변환'
    },
    {
      id: 'persona',
      name: 'Persona AI',
      icon: <Bot size={64} />,
      url: 'http://localhost:3002',
      description: 'AI 캐릭터와 대화'
    }
  ]

  // 서비스 클릭 핸들러
  const handleServiceClick = (service: Service) => {
    setCurrentService(service.id)
  }

  // 홈으로 돌아가기
  const handleBackToHome = () => {
    setCurrentService(null)
  }

  // iframe 로드 완료 시 토큰 전달
  const handleIframeLoad = (serviceUrl: string) => {
    const iframe = document.getElementById('service-iframe') as HTMLIFrameElement
    if (!iframe || !iframe.contentWindow) return

    const accessToken = getCookie('accessToken')
    const refreshToken = getCookie('refreshToken')
    const username = getCookie('username')

    const message = {
      type: 'AUTH_TOKEN',
      accessToken,
      refreshToken,
      username
    }

    // postMessage로 토큰 전달
    iframe.contentWindow.postMessage(message, serviceUrl)
    console.log('토큰 전달 완료:', serviceUrl)
  }

  // 로그아웃
  const handleLogout = async () => {
    try {
      await fetch('http://localhost:8080/api/auth/logout', {
        method: 'POST',
        credentials: 'include'
      })

      // 쿠키 삭제
      document.cookie = 'accessToken=; max-age=0; path=/'
      document.cookie = 'refreshToken=; max-age=0; path=/'
      document.cookie = 'username=; max-age=0; path=/'

      navigate('/login')
    } catch (error) {
      console.error('로그아웃 실패:', error)
    }
  }

  // 서비스 화면
  if (currentService) {
    const service = services.find(s => s.id === currentService)
    if (!service) return null

    return (
      <div className="service-container">
        {/* 상단 헤더 */}
        <div className="service-header">
          <div className="service-header-left">
            <button onClick={handleBackToHome} className="back-button">
              ← 홈으로
            </button>
            <h2 className="service-title">{service.name}</h2>
          </div>
          <button onClick={handleLogout} className="logout-button">
            로그아웃
          </button>
        </div>

        {/* iframe */}
        <iframe
          id="service-iframe"
          src={service.url}
          onLoad={() => handleIframeLoad(service.url)}
          className="service-iframe"
          title={service.name}
        />
      </div>
    )
  }

  // 메인 화면
  return (
    <div className="portal-container">
      {/* 로그아웃 */}
      <button onClick={handleLogout} className="portal-logout-button">
        로그아웃
      </button>

      {/* 환영 메시지 */}
      <div className="welcome-section">
        <h1 className="welcome-title">
          안녕하세요, {username}님! 👋
        </h1>
        <p className="welcome-subtitle">
          사용하실 서비스를 선택해주세요
        </p>
      </div>

      {/* 서비스 카드들 */}
      <div className="services-grid">
        {services.map((service) => (
          <div
            key={service.id}
            onClick={() => handleServiceClick(service)}
            className="service-card"
          >
            {/* 아이콘 */}
            <div className="service-icon">
              {service.icon}
            </div>

            {/* 서비스 이름 */}
            <h2 className="service-name">
              {service.name}
            </h2>

            {/* 설명 */}
            <p className="service-description">
              {service.description}
            </p>
          </div>
        ))}
      </div>

      {/* 푸터 */}
      <div className="portal-footer">
        ❤️ by 장보경 (/w Claude, Chat GPT, Gemini...)
      </div>
    </div>
  )
}

export default Portal