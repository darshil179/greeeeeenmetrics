<template>
  <div class="text-center space-y-4">
    <h2 class="text-2xl font-semibold">{{ weather.name }}</h2>

    <div class="flex flex-col items-center">
      <img
          :src="`https://openweathermap.org/img/wn/${weather.weather[0].icon}@4x.png`"
          :alt="weather.weather[0].description"
          class="drop-shadow-lg"
      />
      <p class="capitalize text-lg opacity-90">{{ weather.weather[0].description }}</p>
    </div>

    <div class="text-5xl font-bold">{{ weather.main.temp.toFixed(1) }}°C</div>

    <div class="grid grid-cols-3 gap-4 mt-6 text-sm">
      <div class="bg-white/10 p-3 rounded-lg shadow-sm">
        <p class="font-semibold">Humidity</p>
        <p>{{ weather.main.humidity }}%</p>
      </div>
      <div class="bg-white/10 p-3 rounded-lg shadow-sm">
        <p class="font-semibold">Wind</p>
        <p>{{ weather.wind.speed }} m/s</p>
      </div>
      <div class="bg-white/10 p-3 rounded-lg shadow-sm">
        <p class="font-semibold">Feels Like</p>
        <p>{{ weather.main.feels_like.toFixed(1) }}°C</p>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  weather: Object
})
</script>
