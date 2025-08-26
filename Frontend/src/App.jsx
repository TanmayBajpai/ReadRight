import HomePage from "./Pages/HomePage"
import './App.css'
import { BrowserRouter, Routes, Route } from "react-router-dom"
import ResultsPage from './Pages/ResultsPage'

function App() {
  return (
    <>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/results" element={<ResultsPage />} />
        </Routes>
      </BrowserRouter>
    </>
  )
}

export default App
