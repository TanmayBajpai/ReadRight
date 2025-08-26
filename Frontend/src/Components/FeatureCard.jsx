import './FeatureCard.css'

function FeatureCard(props) {
    return(
        <>
            <div className="feature-card">
                <img src={props.imgsrc} className='feature-card-icon'></img>
                <p className='feature-card-title'>{props.title}</p>
                <p className='feature-card-text'>{props.text}</p>
            </div>
        </>
    )
}

export default FeatureCard