
let clientID;
try {clientID = window.Telegram.WebApp.initDataUnsafe.user.id;
if (window.history.length > 1) {
        window.Telegram.WebApp.BackButton.show();
        window.Telegram.WebApp.BackButton.onClick(() => {
            window.history.back();
        });
    }
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


document.getElementById('phone').addEventListener('input', function (e) {
    let digits = e.target.value.replace(/\D/g, '');

    if (digits && digits[0] !== '7') {
        digits = '7' + digits;
    }

    if (digits.startsWith('7')) {
        digits = digits.slice(1);
    }

    const part1 = digits.substring(0, 3);
    const part2 = digits.substring(3, 6);
    const part3 = digits.substring(6, 8);
    const part4 = digits.substring(8, 10);

    let formatted = '+7';
    if (part1) {
        formatted += ' (' + part1;
        if (part1.length === 3) {
            formatted += ')';
        }
    }
    if (part2) {
        formatted += ' ' + part2;
    }
    if (part3) {
        formatted += '-' + part3;
    }
    if (part4) {
        formatted += '-' + part4;
    }

    e.target.value = formatted;
});

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
  const email = document.getElementById("email").value;
  const phone = document.getElementById("phone").value;
  const url = "/employer/create"; // Replace with your API URL
  data = {"inn": inn, "name": name, "logo": logo, "description": description, "ogrn": ogrn, "user_id": clientID, "email": email, "phone": phone };
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
