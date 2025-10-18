async function getWeather() {
    const city = document.getElementById("city").value.trim();
    const resultBox = document.getElementById("result");

    if (!city) {
        resultBox.innerHTML = "<p>Please enter a city name.</p>";
        return;
    }

    try {
        const response = await fetch(`http://localhost:8081/api/weather/${city}`);
        if (!response.ok) throw new Error("City not found");
        const data = await response.json();

        resultBox.innerHTML = `
      <h3>${data.city}</h3>
      <p>Temperature: ${data.temperature}°C</p>
      <p>Humidity: ${data.humidity}%</p>
      <p>Condition: ${data.condition}</p>
    `;
    } catch (error) {
        resultBox.innerHTML = `<p style="color:red;">${error.message}</p>`;
    }
}
