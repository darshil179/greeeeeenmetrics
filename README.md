# 🌍 GreeeeeenMetrics — Climate Data Dashboard
> Monitor CO₂, weather, and air quality with actionable eco-insights.

## 🧠 Overview(demo)
<img width="1585" height="820" alt="image" src="https://github.com/user-attachments/assets/73fd2820-4f62-45be-9c97-797c9df3e107" />

<img width="1380" height="915" alt="image" src="https://github.com/user-attachments/assets/ac95877a-7c6b-4a4a-b51c-8afc3060989e" />




## 🏗️ Architecture/High-Level Overview
                     ┌──────────────────────────┐
                     │      Vue.js Frontend     │
                     │  (Vite + Tailwind + Axios)│
                     │                          │
                     │  - Displays dashboards    │
                     │  - Calls backend APIs     │
                     └───────────┬──────────────┘
                                 │ REST API calls (HTTPS)
                                 ▼
         ┌────────────────────────────────────────────────────┐
         │               Spring Boot Backend (API Layer)       │
         │----------------------------------------------------│
         │   weather-service      → Fetches weather via        │
         │                        OpenWeatherMap API           │
         │                                                    │
         │   emissions-service    → Calculates CO₂ data via    │
         │                        Carbon Interface API         │
         │                                                    │
         │   air-quality-service  → Monitors air quality via   │
         │                        OpenAQ API                   │
         │                                                    │
         │   analytics-service    → Aggregates and analyzes     │
         │                        eco-metrics & suggestions    │
         │                                                    │
         │   user-service         → Handles users, preferences,│
         │                        and saved metrics            │
         └───────────────┬─────────────────────────────────────┘
                         │
                         ▼
                🐘 PostgreSQL Database
                  (Shared across services)




## 🧩 Tech Stack
Spring Boot, Vue.js, PostgreSQL, Redis, Docker, Azure, etc.

## 🪴 Development Phases


## 🧱 Repository Structure
Code tree

## 🐳 Run Locally
will add Commands to build and start the full stack

## 🚀 Deployment
 CI/CD & hosting

## 📈 Future Enhancements
Predictive analytics, community leaderboard, etc.

## 🧑‍💻 Author
Darshil Bhakhar 
