# 🏋️ Fitness Agent — AI Fitness Assistant for Android

An experimental AI-powered fitness assistant built natively for Android using **Kotlin, Jetpack Compose, Google ADK for Android, and Gemini**.

The goal of this project is to explore how AI agents can be integrated directly into an Android application and used to generate personalized fitness and nutrition guidance.

> 🚧 This project is actively under development.

---

## 📱 Project Overview

Fitness Agent collects a user's fitness and dietary information through a conversational questionnaire and sends the collected context to an AI agent.

The agent then uses Gemini to generate a personalized **7-day diet plan** in a structured JSON format.

### Current flow

```text
User
  ↓
Android Chat UI
  ↓
Fixed Questionnaire
  ↓
TrainerViewModel
  ↓
Google ADK for Android
  ↓
Gemini
  ↓
Structured JSON
  ↓
7-Day Diet Plan
