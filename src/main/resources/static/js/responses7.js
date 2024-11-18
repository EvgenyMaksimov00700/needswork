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
function sendVideo(videoCvId){
    const url = "/videoCv/send"
    data = {videoCvId:  videoCvId, userId: clientID}
    console.log(data)
    const response = fetch(url, {
       method: 'POST', // Метод запроса
       headers: {
       'Content-Type': 'application/json' // Заголовок, указывающий на тип содержимого
                 },
                  body: JSON.stringify(data) // Данные, отправляемые в теле запроса, преобразованные в JSON
              });
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
            infoParagraph.innerHTML = `Соискатель ${response.job_seeker.user.fullName}`;
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
            sendVideo(response.comment)
            };
            buttonGroup.appendChild(viewResumeButton);

            // Кнопка "Открыть чат"
            const openChatButton = document.createElement('button');
            openChatButton.type = 'button';
            openChatButton.classList.add('open-chat');
            openChatButton.textContent = 'открыть чат';
            openChatButton.onclick = () => {
                window.Telegram.WebApp.openTelegramLink(`https://t.me/${response.job_seeker.user.username}`); // URL с ID чата

            };

            const cancelButton = document.createElement('button');
            cancelButton.type = 'button';
            cancelButton.classList.add('cancel');
            cancelButton.textContent = 'отклонить соискателя';
            cancelButton.onclick = () => {
            fetch(`/response/${response.id}`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).then(response => {
                    if (!response.ok) {
                        throw new Error(`Ошибка HTTP: ${response.status}`); // Бросаем ошибку, если ответ не в порядке
                    }
                    return response.json();

                })
                .then(response => {location.reload();})

            };
            buttonGroup.appendChild(openChatButton);

            // Добавляем группу кнопок в основной div
            responseDiv.appendChild(buttonGroup);
            responseDiv.appendChild(cancelButton);
            // Добавляем весь отклик в контейнер для откликов
            responsesContainer.appendChild(responseDiv);
        });
    }).catch(error => {
        console.error('Ошибка при получении данных:', error);
    });
});