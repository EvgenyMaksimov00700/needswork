
let clientID;
try {clientID = window.Telegram.WebApp.initDataUnsafe.user.id;
  function isDesktop() {
        const userAgent = navigator.userAgent.toLowerCase();
        return userAgent.includes("windows") || userAgent.includes("macintosh") || userAgent.includes("linux");
    }
    console.log(isDesktop());
    if (!isDesktop()) {
        window.Telegram.WebApp.requestFullscreen();
    }
window.Telegram.WebApp.expand();
}
catch(error) {
        clientID = 159619887
}
console.log(clientID)

document.addEventListener('touchstart', function(event) {
            const activeElement = document.activeElement;

            if (activeElement && (activeElement.tagName === 'INPUT' || activeElement.tagName === 'TEXTAREA')) {
                // Проверяем, что нажали вне поля ввода
                if (!activeElement.contains(event.target)) {
                    activeElement.blur();
                }
            }
        });

document.getElementById("next" ).addEventListener("click", async function( event ) {
  event.preventDefault();
  const inn = document.getElementById("inn").value;
  const ogrn = document.getElementById("ogrn").value;
  const name = document.getElementById("nameCompany").value;
  const logo = document.getElementById("logo").value;
  const description = document.getElementById("description").value;
  const url = "/employer/create"; // Replace with your API URL
  data = {"inn": inn, "name": name, "logo": logo, "description": description, "ogrn": ogrn, "user_id": clientID}
  try {
          const response = await fetch(url, {
              method: 'POST', // Метод запроса
              headers: {
                  'Content-Type': 'application/json' // Заголовок, указывающий на тип содержимого
              },
              body: JSON.stringify(data) // Данные, отправляемые в теле запроса, преобразованные в JSON
          });

          if (!response.ok) {
              throw new Error(`Ошибка HTTP: ${response.status}`); // Бросаем ошибку, если ответ не в порядке
          }

          const jsonData = await response.json(); // Парсим и возвращаем ответ как JSON
          //console.log(jsonData); // Обрабатываем данные ответа
          window.location.href = "/employer/lk/"; // Redirect to dashboard page after successful registration

      } catch (error) {
          console.error('Ошибка:', error); // Обрабатываем ошибку
      }})
