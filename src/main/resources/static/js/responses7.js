const currentUrl = window.location.href;
const url = new URL(currentUrl);
const params = new URLSearchParams(url.search);
const vacancyId = params.get('id');
let clientID;
try {clientID = window.Telegram.WebApp.initDataUnsafe.user.id;}
catch(error) {clientID = 159619887}
console.log(clientID)
function back () {
console.log(vacancyId);
window.location.href=`/employer/vacancy/description?id=${vacancyId}`
}

document.addEventListener('DOMContentLoaded', function () {
    fetch(`/response/vacancy/${vacancyId}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if (!response.ok) {
            throw new Error(`Ошибка HTTP: ${response.status}`); // Бросаем ошибку, если ответ не в порядке
        }
        return response.json();
    }).then(data => {
        const responsesContainer = document.getElementById('responses');
    console.log(data);
        // Очищаем контейнер, если он уже содержит элементы
        responsesContainer.innerHTML = '';

        // Перебираем отклики и создаем HTML для каждого из них
        data.forEach(response => {
            // Создаем основной контейнер для отклика
            const responseDiv = document.createElement('div');
            responseDiv.classList.add('response');

            // Создаем элемент <p> с информацией о соискателе
            const infoParagraph = document.createElement('p');
            infoParagraph.innerHTML = `Соискатель ${response.job_seeker.user.fullName}. Комментарий: ${response.comment}`;
            responseDiv.appendChild(infoParagraph);

            // Создаем группу кнопок
            const buttonGroup = document.createElement('div');
            buttonGroup.classList.add('button-group');

            // Кнопка "Посмотреть резюме"
            const viewResumeButton = document.createElement('button');
            viewResumeButton.type = 'button';
            viewResumeButton.classList.add('view-resume');
            viewResumeButton.textContent = 'посмотреть резюме';
            viewResumeButton.onclick = () => {
                window.location.href = `/employer/lk/${response.resumeId}`; // URL с ID резюме
            };
            buttonGroup.appendChild(viewResumeButton);

            // Кнопка "Открыть чат"
            const openChatButton = document.createElement('button');
            openChatButton.type = 'button';
            openChatButton.classList.add('open-chat');
            openChatButton.textContent = 'открыть чат';
            openChatButton.onclick = () => {
                window.location.href = `tg://user?id=159619887`; // URL с ID чата
            };
            buttonGroup.appendChild(openChatButton);

            // Добавляем группу кнопок в основной div
            responseDiv.appendChild(buttonGroup);

            // Добавляем весь отклик в контейнер для откликов
            responsesContainer.appendChild(responseDiv);
        });
    }).catch(error => {
        console.error('Ошибка при получении данных:', error);
    });
});