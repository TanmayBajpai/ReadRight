import './HomePage.css'
import FeatureCard from '/src/Components/FeatureCard.jsx'
import { useState, useEffect } from 'react'
import axios from 'axios'
import { useLocation, useNavigate } from 'react-router-dom'


function HomePage() {
  const [url, setUrl] = useState("")
  const [showError, setShowError] = useState(false)
  const [error, setError] = useState("Invalid Argument!")
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  const location = useLocation()

  const lines = [
    "Analyzing article",
    "Breaking text into sentences",
    "Running bias detection",
    "Interpreting political lean",
    "Almost there..."
  ];

  const [index, setIndex] = useState(0);

  useEffect(() => {
    const interval = setInterval(() => {
      setIndex((prev) => {
        if (prev < lines.length - 1) {
          return (prev + 1);
        }
        return prev
      })
    }, 3000);

    return () => {
      clearInterval(interval);
    };
  }, []);

  useEffect(() => {
    if (location.state) {
      setShowError(true)
      setError(location.state)

      navigate(location.pathname, { replace: true })
    }
  },[location.state, navigate])

  const analyze = async () => {
    setIndex(0)
    if (url.trim() === "") {
      setShowError(true)
      setError("URL cannot be empty")
      return
    }
    try {
      new URL(url)
    } catch {
      setShowError(true)
      setError("Invalid URL")
      return
    }
    setLoading(true)
    setShowError(false)

    try {
      const apiResponse = await axios.get(`/api/get-bias`, {
        params: { link: url.trim() }
      })

      navigate("/results", { state: { response: apiResponse.data } })
    } catch (error) {
      console.log(error)
    } finally {
      setLoading(false)
    }
  }

  return (
    <>
      <div className="title">
        <div className='name'>
          <img src="/logo.png" className="logo"></img>
          <h1>ReadRight</h1>
        </div>

        <p className='subtitle'>Uncover bias in news articles with AI-powered analysis.</p>

        <div className='input-container'>
          <p className='instruction'>Paste any article URL below to see political-lean and sentence-by-sentence editorial bias analysis</p>
          <input type="text" className='input-form' placeholder='https://example.com/new-article'
            value={url}
            onChange={(e) => {
              setUrl(e.target.value)
              setShowError(false)
            }}>
          </input>
          {showError && <p className='error'>{error}</p>}
          <button className='analyze-button' onClick={analyze}>Analyze Article</button>
        </div>

        <div className="feature-card-container">
          <FeatureCard
            title="Source Credibility"
            text="Learn about the publication's historical bias and reliability ratings."
            imgsrc="/news.png" />
          <FeatureCard
            title="Linguistic Bias Score"
            text="Get a bias score based on the language in the headline and text."
            imgsrc="/para.png" />
          <FeatureCard
            title="Bias Visualization"
            text="See biased phrases highlighted and view an overall article rating."
            imgsrc="/meter.png" />
        </div>
        <p className='footer'>Results are AI-generated and may not be fully accurate. Always verify with multiple sources.</p>
      </div>


      {loading && (
        <div className="overlay">
          <div className="loading-container">
            <div
              className="lines"
              style={{ transform: `translateY(-${index * 70}px)` }}
            >
              {lines.map((line, i) => (
                <div
                  key={i}
                  className={`line ${i === index ? "focused" : ""}`}
                >
                  {line}
                </div>
              ))}
            </div>

            <div className="focus-mask"></div>
          </div>
        </div>
      )}
    </>
  )
}

export default HomePage