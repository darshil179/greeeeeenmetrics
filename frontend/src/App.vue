<template>
  <div class="min-h-screen flex flex-col items-center justify-center bg-gradient-to-br from-blue-900 via-sky-600 to-cyan-400 text-white">
    <div class="bg-white/10 backdrop-blur-md p-8 rounded-2xl shadow-2xl w-full max-w-md text-center border border-white/20">
      <h1 class="text-4xl font-bold mb-8 text-white drop-shadow-md">🌤️ Weather Metrics</h1>

      <div class="flex space-x-2 mb-8 justify-center">
        <input
            v-model="city"
            @keyup.enter="fetchWeather"
            placeholder="Enter city name..."
            class="px-4 py-2 rounded-lg text-black focus:outline-none w-2/3"
        />
        <button
            @click="fetchWeather"
            class="bg-yellow-400 text-blue-900 font-semibold px-4 py-2 rounded-lg hover:bg-yellow-300 transition"
        >
          Search
        </button>
      </div>

      <WeatherCard v-if="weather && weather.main" :weather="weather" />
    </div>

    <footer class="mt-10 text-white/70 text-sm">
      Built with 🌍 Vue + Tailwind + OpenWeatherMap
    </footer>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import WeatherCard from './components/WeatherCard.vue'

const city = ref('Toronto')
const weather = ref(null)

async function fetchWeather() {
  const apiKey = '5e5778cd98c2e8bd3bdd545cdc2da10a' // Replace with your real key
  const url = `https://api.openweathermap.org/data/2.5/weather?q=${city.value}&appid=${apiKey}&units=metric`
  const res = await fetch(url)
  if (!res.ok) {
    alert('City not found!')
    return
  }
  weather.value = await res.json()
}

// Load default city
fetchWeather()
</script>
