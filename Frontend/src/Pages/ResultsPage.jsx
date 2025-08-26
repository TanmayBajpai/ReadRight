import { useLocation, useNavigate } from "react-router-dom"
import './ResultsPage.css'
import Dial from '/src/Components/Dial.jsx'
import { useState, useEffect } from 'react'

function ResultsPage() {

    const location = useLocation()

    const navigate = useNavigate()

    const backToHome = (error) => {
        navigate("/", {state: error})
    }

    const { response } = location.state || {}

    useEffect(() => {
        if (
            !response ||
            !response.contentAnalysis ||
            !response.headlineAnalysis ||
            !response.outletInfo
        ) {
            backToHome("There was an error fetching article contents.")
        }
    }, [response, navigate])

    if (
        !response ||
        !response.contentAnalysis ||
        !response.headlineAnalysis ||
        !response.outletInfo
    ) {
        return null
    }

    const content = response.contentAnalysis.result

    const leaningMap = {
        "Center": [90, "Centrist"],
        "Left": [10, "Left Leaning"],
        "Center-Left": [50, "Center-Left Leaning"],
        "Right": [170, "Right Leaning"],
        "Center-Right": [130, "Center-Right Leaning"]
    }

    const biasCount = content.filter(r => r.label === "biased").length

    const biasScore = ((biasCount / content.length))
    const isBiased = biasScore > 0.4



    return (
        <>
            <div>
                <div className="title-container">
                    <img src="/report.png"></img>
                    <h1>Analysis Report</h1>
                </div>

                <div className="outlet-info-container">
                    <p className="outlet-name">{response.outletInfo.name}</p>
                    <p className="outlet-domain">{response.outletInfo.domain}</p>

                    <Dial value={leaningMap[response.outletInfo.leaning][0]} />
                    <p className="outlet-leaning">{leaningMap[response.outletInfo.leaning][1]}</p>

                    <p className="outlet-notes">"{response.outletInfo.notes}"</p>

                    <div className="analysis-container">
                        <p className="analysis">Headline Analysis: <b>{response.headlineAnalysis.result[0].label === "biased" ? "Biased" : "Unbiased"}</b></p>
                        <p className="analysis">Confidence Score: <b>{Math.trunc(Number(response.headlineAnalysis.result[0].score * 100))}%</b></p>
                    </div>

                    <div className="analysis-container">
                        <p className="analysis">Content Analysis: <b>{isBiased ? "Biased" : "Unbiased"}</b></p>
                        <p className="analysis">Confidence Score: <b>{Math.trunc(Number(isBiased ? (biasScore * 100) : ((1 - biasScore) * 100)))}%</b></p>
                    </div>
                </div>

                <button className="button" onClick={() => backToHome(null)}>Back to Home Page</button>

                <div className="headline-analysis-container">
                    <p className="unbiased">"{response.headlineAnalysis.result[0].sentence}"</p>
                </div>

                <div className="content-analysis-container">
                    <p className="legend">🟨 Biased / Loaded Language / Framing</p>
                    <p className="legend">⬛ Neutral</p>
                    <p className="content">
                        "
                        {content.map((s, i) => (
                            <span key={i} className={s.label === "biased" ? "biased" : "unbiased"}>
                                {s.sentence + " "}
                            </span>
                        ))}
                        "
                    </p>
                </div>
            </div>
        </>
    )
}

export default ResultsPage