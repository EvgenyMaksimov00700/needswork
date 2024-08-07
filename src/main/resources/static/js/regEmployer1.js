// Инициализация Telegram Web App
window.Telegram.WebApp.expand();
let clientID = window.Telegram.WebApp.initDataUnsafe.user.id;
let employer ;
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
        document.getElementById("logo").value = data.logo || '';
        document.getElementById("description").value = data.description || '';
    } catch (error) {
        console.error('Ошибка:', error); // Обрабатываем ошибку
    }
}

// Функция для обработки события нажатия на кнопку "next"
document.getElementById("next").addEventListener("click", async function(event) {
    event.preventDefault();
    
    // Сбор данных из формы
    const inn = document.getElementById("inn").value;
    const ogrn = document.getElementById("ogrn").value;
    const name = document.getElementById("nameCompany").value;
    const logo = document.getElementById("logo").value;
    const description = document.getElementById("description").value;

    // Данные для отправки на сервер
    const url ="/employer/" + employer.id; // Замените на свой URL API
    const data = {
        inn: inn,
        ogrn: ogrn,
        name: name,
        logo: logo,
        description: description,
        user_id: clientID
    };

    try {
        const response = await fetch(url, {
            method: 'PUT', // Метод запроса
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

        // Закрываем Telegram Web App
        window.location.href='/employer/lk/';
    } catch (error) {
        console.error('Ошибка:', error); // Обрабатываем ошибку
    }
});

// Вызов функции для получения и отображения данных при загрузке страницы
fetchAndDisplayData();