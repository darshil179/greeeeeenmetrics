<template>
  <div class="bg-white text-gray-800 p-6 rounded-2xl shadow-lg w-80 text-center">
    <h2 class="text-2xl font-semibold mb-2">{{ weather.name }}</h2>
    <p class="text-lg capitalize">{{ weather.weather[0].description }}</p>
    <img
        :src="`https://openweathermap.org/img/wn/${weather.weather[0].icon}@2x.png`"
        alt="weather icon"
        class="mx-auto my-2"
    />
    <p class="text-5xl font-bold">{{ Math.round(weather.main.temp) }}°C</p>
    <p class="mt-2">Humidity: {{ weather.main.humidity }}%</p>
  </div>
</template>

<script setup>
defineProps({
  weather: Object
})
</script>
