# ReadRight

ReadRight is a web application that helps users uncover political bias in online news articles.  
By simply pasting a URL, the system scrapes the article, analyzes it with NLP models, and presents the findings in a clear, intuitive dashboard.  

ReadRight evaluates the reputation of the news outlet, examines the headline for framing, analyzes the body of the text for patterns of bias, and highlights the exact sentences or phrases that contribute most strongly. This gives readers direct evidence they can interpret for themselves, rather than forcing a one-size-fits-all judgment.  

---

## 🔎 Features

- **News Outlet Leaning**  
  Retrieves the general political orientation of the source (Left, Center, Right) from an internal database.  

- **Headline Analysis**  
  Uses NLP techniques to examine the headline for framing and language choices.  

- **Article Analysis**  
  Analyzes the article text for tone, sentiment, and framing patterns.  

- **Highlighted Passages**  
  Marks words, phrases, or sentences that strongly indicate bias, shown directly in the article view.  

All results are displayed together on a **bias dashboard**.  

---

## ⚙️ System Overview

- **Spring Boot Backend**  
  - Scrapes headline and article text from the submitted URL.  
  - Looks up outlet leaning from a curated database.  
  - Runs NLP models to analyze headline and article content.  
  - Identifies and marks biased passages.  
  - Returns results as structured JSON.  

- **React Frontend (integrated into Spring Boot)**  
  - Built with React and bundled into the Spring Boot app.  
  - Provides a clean dashboard interface.  
  - Communicates with backend endpoints within the same application.  

- **Workflow**  
  1. User submits an article URL.  
  2. Backend extracts and analyzes the content.  
  3. JSON results are generated.  
  4. React frontend renders the dashboard.  

---

## 🛠 Tech Stack

- **Spring Boot (Java, Maven)**  
- **React (served from within Spring Boot)**  
- **NLP models (via Hugging Face API)**  

---

## 🚀 Getting Started

### Prerequisites
- Java ≥ 17  
- Maven  
- Hugging Face API key  

### Clone the Repository
```bash
git clone https://github.com/your-username/ReadRight.git
cd ReadRight
```

### Configure
In `src/main/resources/application.properties`, add your Hugging Face API key:
```properties
huggingface.api.key=your_api_key_here
```

### Build and Run
```bash
mvn clean install
mvn spring-boot:run
```

### Access the App
Visit:  
```
http://localhost:8080
```

### Notes
- You do not need to run the frontend/ folder separately. It exists for viewing/editing the React source. The production build is bundled and served by Spring Boot from src/main/resources/static/.

- Ensure your Hugging Face API key is set in application.properties before starting the app.

- Both the frontend and backend are served from the same server.  
---

## 📂 Project Structure

```
ReadRight/
│
├── Frontend              # Frontend folder for viewing 
|
├── src/
│   ├── main/
│   │   ├── java/         # Spring Boot backend
│   │   └── resources/
│   │       ├── static/   # React build output lives here
│   │       └── application.properties
│   └── test/             # Backend tests
│
└── pom.xml               # Maven build config
```

---

## 📷 Screenshots

### Home Page
<img width="1440" height="957" alt="Screenshot_20250920_210809" src="https://github.com/user-attachments/assets/bd1ac0fd-eee6-4852-b306-4947572ec4df" />


### Results Page
<img width="1345" height="926" alt="Screenshot_20250920_210516" src="https://github.com/user-attachments/assets/4adeceb2-9645-4a80-a6ca-261502fcf7e6" />
<img width="1899" height="779" alt="Screenshot_20250920_210545" src="https://github.com/user-attachments/assets/2617b256-037e-4570-be1e-2ebc7cc01fc5" />

