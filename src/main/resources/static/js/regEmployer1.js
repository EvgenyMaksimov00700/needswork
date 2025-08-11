// Инициализация Telegram Web App

let clientID;
try {
    clientID = window.Telegram.WebApp.initDataUnsafe.user.id;
if (window.history.length > 1) {
        window.Telegram.WebApp.BackButton.show();
        window.Telegram.WebApp.BackButton.onClick(() => {
            window.history.back();
        });
    }
  function isDesktop()
 {

        const userAgent = navigator.userAgent.toLowerCase();
        return userAgent.includes("windows") || userAgent.includes("macintosh") || userAgent.includes("linux");
    }
    console.log(isDesktop());
    if (!isDesktop()) {

        window.Telegram.WebApp.requestFullscreen();
    }
    window.Telegram.WebApp.expand();
}
catch(error) {clientID = 159619887}
console.log(clientID)
let employer ;
document.getElementById('logo')
  .addEventListener('change', e => {
    const file = e.target.files[0];
    if (!file) return;
    const url = URL.createObjectURL(file);
    const img = document.getElementById('logoPreview');
    img.src = url;
    img.style.display = 'block';
  });
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
// Функция для получения данных из API и отображения их на странице
async function fetchAndDisplayData() {
    const url = `/employer/user/${clientID}`; // Замените на свой URL API
    try {
        const response = await fetch(url, {
            method: 'GET', // Метод запроса
            headers: {
                'Content-Type': 'application/json' // Заголовок, указывающий на тип содержимого
            }
        });

        if (!response.ok) {
            throw new Error(`Ошибка HTTP: ${response.status}`); // Бросаем ошибку, если ответ не в порядке
        }

        const data = await response.json(); // Парсим и возвращаем ответ как JSON
        employer = data;
        console.log(data); // Логируем данные для проверки

        // Заполняем поля формы данными из API
        document.getElementById("inn").value = data.inn || '';
        document.getElementById("ogrn").value = data.ogrn || '';
        document.getElementById("nameCompany").value = data.name || '';
        document.getElementById("description").value = data.description || '';
        document.getElementById("email").value = data.email || '';
        document.getElementById("phone").value = data.phone || '';
        if (data.logo) {
          document.getElementById('logoPreview').src = `https://tworker.ru/employer/logo/${data.employer_id}`;
        }

    } catch (error) {
        console.error('Ошибка:', error); // Обрабатываем ошибку
    }
}

// Функция для обработки события нажатия на кнопку "next"
document.getElementById("next").addEventListener("click", async function(event) {
  event.preventDefault();

  // берём DOM-элемент <input type="file" id="logo">
  const logoInput = document.getElementById("logo");

  // собираем FormData
  const formData = new FormData();
  formData.append("inn", document.getElementById("inn").value);
  formData.append("ogrn", document.getElementById("ogrn").value);
  formData.append("name", document.getElementById("nameCompany").value);
  formData.append("description", document.getElementById("description").value);
  formData.append("email", document.getElementById("email").value);
  formData.append("phone", document.getElementById("phone").value);
  formData.append("user_id", clientID);

  // если файл выбран — добавляем его
  if (logoInput.files && logoInput.files[0]) {
    formData.append("logo", logoInput.files[0]);
  }

  const url = "/employer/" + employer.employer_id; // ваш API
  console.log(formData)

  try {
    const response = await fetch(url, {
      method: "PUT",
      body: formData
      // НЕ указываем headers: {'Content-Type': ...}
    });

    if (!response.ok) {
      throw new Error(`HTTP error: ${response.status}`);
    }

    const jsonData = await response.json();
    console.log("Ответ сервера:", jsonData);

    // после успешного апдейта переходим в лк
    window.location.href = "/employer/lk/";
  } catch (error) {
    console.error("Ошибка при обновлении:", error);
  }
});

// Вызов функции для получения и отображения данных при загрузке страницы
fetchAndDisplayData();