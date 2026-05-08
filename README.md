# 🚀 IntelligenceIQ

<div align="center">

### Bridging Codebase Complexity with Generative Administrative Intelligence

![Java](https://img.shields.io/badge/Java-17+-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.x-success?style=for-the-badge&logo=springboot)
![AWS](https://img.shields.io/badge/AWS-Elastic_Beanstalk-FF9900?style=for-the-badge&logo=amazon-aws&logoColor=white)
![AI](https://img.shields.io/badge/AI-Gemini_1.5_Pro-4285F4?style=for-the-badge&logo=google&logoColor=white)

</div>

---

![Hero Image](./assets/emerald-terminal.gif)

---

## 🌐 Project Access

- **Live Production:** http://springintelligeneceaihub-env.eba-guquqqc5.us-east-1.elasticbeanstalk.com/  
- **GitHub Repository:** [https://github.com/R-Venkat-Kalyan/SB_Gen_AI/edit/gemini-demo]  

---

## 📖 Overview

**IntelligenceIQ** is an enterprise-grade administrative intelligence system designed to transform static codebases into dynamic, conversational knowledge engines.

By integrating an advanced **AI Intelligence Engine**, the platform enables real-time interaction with source code, delivering contextual insights, architectural understanding, and developer-level explanations on demand.

---

## 🏗️ Role-Based Architecture

| Role        | Capabilities |
|------------|-------------|
| **End-User** | Terminal-style interaction for project summaries and logic mapping |
| **Developer** | Deep dive into method-level logic, syntax, and debugging logs |
| **Architect** | System-wide design visibility, dependency mapping, and structure analysis |

---

## ⚡ Core Features

- 🤖 AI-powered codebase intelligence across `.java`, `.html`, and `pom.xml`  
- 🧠 Classpath-aware scanning for JAR-deployed environments  
- 🔐 Secure context filtering excluding `.env`, `.properties`, `.class`  
- 🖥️ Emerald Terminal UI with real-time thinking indicators  
- ⚡ In-memory caching for sub-second response times  

---

## ⚙️ Tech Stack

**Backend**
- Java 17
- Spring Boot 3.5.14  
- Spring AI  

**AI Engine**
- Google Gemini 1.5 Pro API  

**Frontend**
- Tailwind CSS  
- Vanilla JavaScript  
- Terminal-based UI (Emerald Theme)  

**Cloud**
- AWS Elastic Beanstalk 
- Port: 5000  

---

## 🛠️ Setup & Configuration

> 🔐 Never commit `.env` or sensitive configs. Use environment variables in AWS.

### 1. Clone Repository
```bash
git clone https://github.com/R-Venkat-Kalyan/SB_Gen_AI/edit/gemini-demo
```

### 2. Environment Setup
```env
GEMINI_API_KEY=your_api_key_here
SERVER_PORT=5000
```

### 3. Build
```bash
mvn clean package
```

### 4. Run
```bash
java -jar target/gemini-demo-0.0.1-SNAPSHOT.jar
```

---

<details>
<summary><b>⚙️ Advanced Notes</b></summary>

- Ensure AWS environment variables are configured  
- Monitor logs via `web.stdout.log`  
- Validate classpath scanning on deployment  
- Enable HTTPS in production  

</details>

---

## 🖼️ Interface Modules

- **Dashboard** — System overview and entry point  
- **Chat Terminal** — AI interaction hub  
- **Code Docs** — AI-generated documentation  
- **Architect View** — Structural system analysis  
- **Developer View** — Code-level inspection  

---

## 📁 Project Structure

```
src/
 ├── controller/
 ├── service/
 ├── repository/
 ├── model/
 └── config/

resources/
 ├── templates/
 ├── static/
 └── application.yml

.env
pom.xml
```

---

## 🧠 Architecture Highlights

- Clean Layered Architecture    
- Stateless Service Design  
- High-performance caching layer  
- Secure AI prompt filtering  

---
 

## 🌟 Final Note

**IntelligenceIQ** is not just a tool —  
it is a **developer intelligence layer** that redefines how engineers interact with complex systems.
