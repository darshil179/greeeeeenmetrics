<template>
  <div
      :class="[
      darkMode
        ? 'bg-gradient-to-br from-slate-900 via-gray-900 to-black text-white'
        : 'bg-gradient-to-br from-blue-100 via-sky-200 to-teal-100 text-gray-900',
      'min-h-screen flex flex-col items-center justify-center transition-colors duration-500 text-center p-4'
    ]"
  >
    <!-- Header -->
    <div class="flex justify-between items-center w-full max-w-3xl mb-8">
      <h1
          class="text-4xl font-extrabold tracking-wide text-transparent bg-clip-text bg-gradient-to-r from-teal-400 to-blue-600"
      >
        🌍 GreeeeenMetrics
      </h1>
      <button
          @click="toggleDarkMode"
          class="p-2 rounded-full bg-white/10 hover:bg-white/20 transition"
          :title="darkMode ? 'Switch to Light Mode' : 'Switch to Dark Mode'"
      >
        <span v-if="darkMode">🌞</span>
        <span v-else>🌙</span>
      </button>
    </div>

    <!-- Search Bar -->
    <div class="flex justify-center mb-8 w-full max-w-xl">
      <input
          v-model="city"
          @keyup.enter="fetchLatLon"
          placeholder="Enter a city name..."
          class="flex-1 p-3 rounded-l-2xl border-none outline-none text-lg font-medium"
          :class="
          darkMode
            ? 'bg-gray-800 text-white placeholder-gray-400'
            : 'bg-white text-gray-800 placeholder-gray-500 shadow-inner'
        "
      />
      <button
          @click="fetchLatLon"
          class="px-6 rounded-r-2xl font-semibold transition-all"
          :class="
          darkMode
            ? 'bg-gradient-to-r from-teal-600 to-blue-700 hover:scale-105'
            : 'bg-gradient-to-r from-teal-400 to-blue-500 hover:scale-105 text-white'
        "
      >
        Search
      </button>
    </div>

    <!-- Dashboard Cards -->
    <div
        v-if="weather && airQuality"
        class="grid grid-cols-1 md:grid-cols-2 gap-6 w-full max-w-3xl"
    >
      <!-- Weather Card -->
      <div
          :class="[
          'p-6 rounded-2xl border shadow-lg transition-all',
          darkMode
            ? 'bg-white/10 border-white/20 hover:shadow-teal-500/30'
            : 'bg-white/80 hover:shadow-teal-300/40'
        ]"
      >
        <h2 class="text-2xl font-semibold mb-3 flex items-center justify-center gap-2">
          🌤️ Weather in {{ cityName }}
        </h2>
        <div class="text-lg space-y-2">
          <div class="flex flex-col items-center">
            <img
                v-if="weather.weather[0].icon"
                :src="`https://openweathermap.org/img/wn/${weather.weather[0].icon}@2x.png`"
                alt="Weather Icon"
                class="w-20 h-20"
            />
            <p class="capitalize font-semibold">
              {{ weather.weather[0].description }}
            </p>
          </div>
          <!-- ✅ FIXED TEMPERATURE -->
          <p>
            <span class="font-semibold">🌡 Temperature:</span>
            {{ weather.main.temp.toFixed(1) }} °C
          </p>
          <p><span class="font-semibold">💧 Humidity:</span> {{ weather.main.humidity }}%</p>
          <p><span class="font-semibold">🌬 Wind Speed:</span> {{ weather.wind.speed }} m/s</p>

          <!-- 🌅 Added Sunrise & Sunset -->
          <p>
            <span class="font-semibold">🌅 Sunrise:</span>
            {{ formatTime(weather.sys.sunrise, weather.timezone) }}
          </p>
          <p>
            <span class="font-semibold">🌇 Sunset:</span>
            {{ formatTime(weather.sys.sunset, weather.timezone) }}
          </p>
        </div>
      </div>

      <!-- Air Quality Card -->
      <div
          :class="[
          'p-6 rounded-2xl border shadow-lg transition-all',
          darkMode
            ? 'bg-white/10 border-white/20 hover:shadow-green-500/30'
            : 'bg-white/80 hover:shadow-green-300/40'
        ]"
      >
        <h2 class="text-2xl font-semibold mb-3">💨 Air Quality</h2>
        <div class="text-lg space-y-2">
          <p>
            <span class="font-semibold">AQI:</span>
            <span :class="aqiColorClass(airQuality.list[0].main.aqi)">
              {{ airQuality.list[0].main.aqi }} ({{ getAqiLabel(airQuality.list[0].main.aqi) }})
            </span>
          </p>
          <p><span class="font-semibold">PM2.5:</span> {{ airQuality.list[0].components.pm2_5 }} µg/m³</p>
          <p><span class="font-semibold">O₃:</span> {{ airQuality.list[0].components.o3 }} µg/m³</p>
          <p><span class="font-semibold">CO:</span> {{ airQuality.list[0].components.co }} µg/m³</p>
        </div>
      </div>
    </div>

    <!-- Loading/Error -->
    <p v-if="loading" class="mt-6 text-blue-400 animate-pulse">Fetching data...</p>
    <p v-if="error" class="mt-6 text-red-400 font-medium">{{ error }}</p>
  </div>
</template>

<script setup>
import { ref } from "vue";

const city = ref("");
const cityName = ref("");
const weather = ref(null);
const airQuality = ref(null);
const loading = ref(false);
const error = ref("");
const darkMode = ref(true);

function toggleDarkMode() {
  darkMode.value = !darkMode.value;
}

async function fetchLatLon() {
  if (!city.value.trim()) return;
  loading.value = true;
  error.value = "";
  try {
    const res = await fetch(`http://localhost:8081/api/geocode/${city.value}`);
    if (!res.ok) throw new Error("Failed to fetch geocode");
    const data = await res.json();

    // Handle both array or object formats safely
    const coords = Array.isArray(data) ? data[0] : data;
    if (coords?.lat && coords?.lon) {
      cityName.value = coords.name || city.value;
      await fetchWeatherAndAirQuality(coords.lat, coords.lon);
    } else {
      error.value = "City not found.";
    }
  } catch (err) {
    console.error(err);
    error.value = "Error fetching location data.";
  } finally {
    loading.value = false;
  }
}

async function fetchWeatherAndAirQuality(lat, lon) {
  try {
    const [weatherRes, airRes] = await Promise.all([
      fetch(`http://localhost:8081/api/weather/${lat}/${lon}`),
      fetch(`http://localhost:8081/api/airquality/${lat}/${lon}`)
    ]);

    if (!weatherRes.ok || !airRes.ok) throw new Error("Failed API call");

    weather.value = await weatherRes.json();
    airQuality.value = await airRes.json();
  } catch (err) {
    console.error(err);
    error.value = "Failed to fetch weather or air quality data.";
  }
}

// 🌅 Convert UNIX timestamps to local readable times
function formatTime(timestamp, timezoneOffset) {
  const localTime = new Date((timestamp + timezoneOffset) * 1000);
  return localTime.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" });
}

function getAqiLabel(aqi) {
  const labels = ["Good", "Fair", "Moderate", "Poor", "Very Poor"];
  return labels[aqi - 1] || "Unknown";
}

function aqiColorClass(aqi) {
  const colors = [
    "text-green-400",
    "text-lime-400",
    "text-yellow-400",
    "text-orange-400",
    "text-red-500"
  ];
  return colors[aqi - 1] || "text-gray-400";
}
</script>
