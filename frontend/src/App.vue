<template>
  <div class="min-h-screen bg-gradient-to-br from-green-900 via-emerald-700 to-lime-800 text-white flex flex-col items-center py-12 px-4">
    <h1 class="text-4xl font-bold mb-6">🌎 GreenMetrics Dashboard</h1>

    <div class="flex space-x-3 mb-8">
      <input
          v-model="city"
          @keyup.enter="fetchWeatherAndAirQuality"
          placeholder="Enter city name..."
          class="p-3 rounded-lg text-black w-64 focus:outline-none focus:ring-2 focus:ring-lime-400"
      />
      <button
          @click="fetchWeatherAndAirQuality"
          class="bg-lime-600 hover:bg-lime-500 text-white px-6 py-3 rounded-lg font-semibold shadow-md transition"
      >
        Search
      </button>
    </div>

    <div v-if="weather && airQuality" class="grid grid-cols-1 md:grid-cols-2 gap-6 w-full max-w-4xl">
      <div class="bg-white bg-opacity-10 backdrop-blur-lg p-6 rounded-2xl shadow-lg">
        <h2 class="text-2xl font-semibold mb-2">🌤 Weather in {{ city }}</h2>
        <p class="text-lg">Temperature: {{ weather.main.temp }}°C</p>
        <p>Condition: {{ weather.weather[0].description }}</p>
        <p>Humidity: {{ weather.main.humidity }}%</p>
        <p>Wind Speed: {{ weather.wind.speed }} m/s</p>
      </div>

      <div class="bg-white bg-opacity-10 backdrop-blur-lg p-6 rounded-2xl shadow-lg">
        <h2 class="text-2xl font-semibold mb-2">🌫 Air Quality</h2>
        <p class="text-lg">AQI: {{ airQuality.list[0].main.aqi }}</p>
        <div class="mt-2 text-sm text-gray-200">
          <p>CO: {{ airQuality.list[0].components.co }}</p>
          <p>NO₂: {{ airQuality.list[0].components.no2 }}</p>
          <p>PM2.5: {{ airQuality.list[0].components.pm2_5 }}</p>
          <p>O₃: {{ airQuality.list[0].components.o3 }}</p>
        </div>
      </div>
    </div>

    <p v-if="loading" class="mt-6 text-lg animate-pulse">Fetching data...</p>
    <p v-if="error" class="mt-6 text-red-300">{{ error }}</p>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const city = ref('')
const weather = ref(null)
const airQuality = ref(null)
const loading = ref(false)
const error = ref('')

async function fetchWeatherAndAirQuality() {
  if (!city.value.trim()) {
    error.value = 'Please enter a city name.'
    return
  }
  error.value = ''
  loading.value = true

  try {
    // 1️⃣ Get coordinates from backend
    const geoRes = await fetch(`http://localhost:8081/api/geocode/${city.value}`)
    const geoData = await geoRes.json()
    if (!geoData.length) {
      error.value = 'City not found.'
      loading.value = false
      return
    }

    const { lat, lon } = geoData[0]

    // 2️⃣ Get weather
    const weatherRes = await fetch(`http://localhost:8081/api/weather/${lat}/${lon}`)
    weather.value = await weatherRes.json()

    // 3️⃣ Get air quality
    const airRes = await fetch(`http://localhost:8081/api/airquality/${lat}/${lon}`)
    airQuality.value = await airRes.json()
  } catch (err) {
    console.error(err)
    error.value = 'Failed to fetch data.'
  } finally {
    loading.value = false
  }
}
</script>

<style>
body {
  margin: 0;
  font-family: 'Inter', sans-serif;
}
</style>
