let clientID;
try {clientID = window.Telegram.WebApp.initDataUnsafe.user.id;
window.Telegram.WebApp.expand();
  function isDesktop() {
        const userAgent = navigator.userAgent.toLowerCase();
        return userAgent.includes("windows") || userAgent.includes("macintosh") || userAgent.includes("linux");
    }
    console.log(isDesktop());
    if (!isDesktop()) {
        document.getElementById("phone").style.marginTop="120px"
        window.Telegram.WebApp.requestFullscreen();
    }
}


catch(error) {clientID = 159619887}
console.log(clientID)

let employerId;
function support(){
window.Telegram.WebApp.openTelegramLink(`https://t.me/Cherchent`); // URL с ID чата
                window.Telegram.WebApp.close();
                }
function sendVideo(videoCvMessage){
url = "/videoCv/send"
data = {videoCvMessage:  videoCvMessage, userId: clientID, vacancyId: -1}
     const response = fetch(url, {
                  method: 'POST', // Метод запроса
                  headers: {
                      'Content-Type': 'application/json' // Заголовок, указывающий на тип содержимого
                  },
                  body: JSON.stringify(data) // Данные, отправляемые в теле запроса, преобразованные в JSON
              });


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
            const vacancy_url = `sendVideo('${videoCv.video_message}')`;

            const element = `
                <div class="resume-button" onclick="${vacancy_url}" style="position: relative;" data-id="${videoCv.id}">
                    <span
                        style="position: absolute; top: 5px; right: 5px; color: white; cursor: pointer; font-weight: bold;"
                        onclick="event.stopPropagation(); delete_videoCv(${videoCv.id})">
                        &times;
                    </span>
                    ${videoCv.name}
                </div>`;
            vacancies.innerHTML += element;
        });
    });
    fetch(`/jobSeeker/user/${clientID}`, {
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
            if (data.textResume==null) {
            document.getElementById("resume-uploaded").style.display = 'none';
            }
            else {
            document.getElementById("resume-not-uploaded").style.display = 'none';
            document.getElementById("resume-text").href = '/textResume/' + data.textResume.split('/').pop();
            }
        });
});

function delete_videoCv(videoCvId) {
if (confirm("Вы действительно хотите удалить Ваше резюме?")){
fetch( `/videoCv/${videoCvId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(response => {
                if (!response.ok) {

                    throw new Error(`Ошибка HTTP: ${response.status}`);
                }
                return response;

            }).then(data => {const element = document.querySelector(`.resume-button[data-id='${videoCvId}']`);
                                     if (element) {
                                         element.remove();
                                     }})


}

}
function triggerFileUpload() {
    document.getElementById('text-resume-upload').click();
}

document.getElementById('text-resume-upload').addEventListener('change', async (event) => {
    const file = event.target.files[0];
    if (file) {
        const formData = new FormData();
        formData.append('file', file);

        try {
            const response = await fetch(`/jobSeeker/resume/${clientID}`, {
                method: 'PUT',
                body: formData,
            });

            if (response.ok) {

                window.location.reload();
            } else {
                console.error('Ошибка при загрузке файла:', response.statusText);
            }
        } catch (error) {
            console.error('Ошибка при отправке файла:', error);
        }
    }
});