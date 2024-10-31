let clientID;
try {clientID = window.Telegram.WebApp.initDataUnsafe.user.id;
window.Telegram.WebApp.expand();
}

catch(error) {clientID = 159619887}
console.log(clientID)

let employerId;
function support(){
window.Telegram.WebApp.openTelegramLink(`https://t.me/Cherchent`); // URL с ID чата
                window.Telegram.WebApp.close();
                }


document.addEventListener('DOMContentLoaded', function() {
    fetch(`/videoCv/user/${clientID}`, {
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
        const vacancies = document.getElementById("resume-buttons");
        data.forEach(videoCv => {
            const vacancy_url = `window.location.href='/employer/vacancy/description?id=${videoCv.video_message}'`;
            const element = `
                <div class="resume-button" onclick="${vacancy_url}" style="position: relative;" data-id="${videoCv.id}">
                    <span
                        style="position: absolute; top: 5px; right: 5px; color: red; cursor: pointer; font-weight: bold;"
                        onclick="event.stopPropagation(); delete_videoCv(${videoCv.id})">
                        &times;
                    </span>
                    ${videoCv.name}
                </div>`;
            vacancies.innerHTML += element;
        });
    });
});

function delete_videoCv(videoCvId) {
if (confirm("Вы действительно хотите удалить Ваше резюме?")){
fetch( `/employer/${videoCvId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(response => {
                if (!response.ok) {

                    throw new Error(`Ошибка HTTP: ${response.status}`);
                }
                return response.json();

            }).then(data => {const element = document.querySelector(`.resume-button[data-id='${videoCvId}']`);
                                     if (element) {
                                         element.remove();
                                     }})

}
}