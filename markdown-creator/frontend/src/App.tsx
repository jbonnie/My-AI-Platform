import { BrowserRouter, Routes, Route } from 'react-router-dom'
import MarkdownCreator from './pages/Markdown-creator'

function App() {

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<MarkdownCreator />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App