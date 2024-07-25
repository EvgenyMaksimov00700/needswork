
let clientID = window.Telegram.WebApp.initDataUnsafe.user.id;
document.getElementById("next" ).addEventListener("click", async function( event ) {
  event.preventDefault();
  const inn = document.getElementById("inn").value;
  const ogrn = document.getElementById("ogrn").value;
  const name = document.getElementById("name").value;
  const logo = document.getElementById("logo").value;
  const description = document.getElementById("description").value;
  const url = "/employer"; // Replace with your API URL
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
          console.log(jsonData); // Обрабатываем данные ответа
      } catch (error) {
          console.error('Ошибка:', error); // Обрабатываем ошибку
      }})
