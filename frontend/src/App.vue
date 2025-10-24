<template>
  <div class="min-h-screen bg-gradient-to-br from-sky-500 to-indigo-700 flex flex-col items-center p-6 text-white">
    <h1 class="text-4xl font-bold mb-8">🌤️ Weather Metrics</h1>
    <div class="flex space-x-2 mb-6">
      <input
          v-model="city"
          @keyup.enter="fetchWeather"
          placeholder="Enter city"
          class="px-4 py-2 rounded-lg text-black focus:outline-none"
      />
      <button @click="fetchWeather" class="bg-white text-indigo-600 font-semibold px-4 py-2 rounded-lg hover:bg-gray-200">
        Search
      </button>
    </div>

    <WeatherCard v-if="weather" :weather="weather" />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import WeatherCard from './components/WeatherCard.vue'

const city = ref('Toronto')
const weather = ref(null)

async function fetchWeather() {
  const apiKey = 'YOUR_OPENWEATHERMAP_API_KEY'
  const url = `https://api.openweathermap.org/data/2.5/weather?q=${city.value}&appid=${apiKey}&units=metric`
  const res = await fetch(url)
  weather.value = await res.json()
}

// Fetch initial weather
fetchWeather()
</script>
