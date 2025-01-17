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

}

catch(error) {clientID = 159619887}
console.log(clientID)

const currentUrl = window.location.href;
const url = new URL(currentUrl);
const params = new URLSearchParams(url.search);
const vacancyId = params.get('id');
const city = params.get('city');
const industry = params.get('industry');
const company = params.get('company');
const position = params.get('position');
const salary = params.get('salary');
const exp = params.get('exp');
const bus = params.get('bus');
const time = params.get('time');
console.log(city);
const existParams = new URLSearchParams();
const encodeExistParams = new URLSearchParams();


if (city) {
    existParams.append('city', city);
    encodeExistParams.append('city', encodeURIComponent(city));
}
if (industry) {
    existParams.append('industry', industry);
    encodeExistParams.append('industry', encodeURIComponent(industry));
}
if (company) {
    existParams.append('company', company);
    encodeExistParams.append('company', encodeURIComponent(company));
}
if (position) {
    existParams.append('position', position);
    encodeExistParams.append('position', encodeURIComponent(position));
}
if (salary) {
    existParams.append('salary', salary);
    encodeExistParams.append('salary', encodeURIComponent(salary));
}
if (exp) {
    existParams.append('exp', exp);
    encodeExistParams.append('exp', encodeURIComponent(exp));
    }
if (bus) {
    existParams.append('bus', bus);
    encodeExistParams.append('bus', encodeURIComponent(bus));
    }
if (time) {
    existParams.append('time', time);
    encodeExistParams.append('time', encodeURIComponent(time));
    }


function edit_vacancy () {
window.location.href=`/employer/vacancy/edit?id=${vacancyId}`}
function vacancy_back() {
window.location.href=`/vacancy/menu?${existParams.toString()}`}




let employerUserId;

function vacancy_responses(vacancyName, vacancyId) {

    const resumeModal = document.getElementById('resumeModal');
    const resumeButtons = document.getElementById('resume-buttons');

    // Открыть модальное окно
    resumeModal.style.display = 'block';
    resumeButtons.innerHTML = ''; // Очищаем перед загрузкой новых данных

    fetch(`/videoCv/user/${clientID}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`Ошибка HTTP: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        data.forEach(videoCv => {
            const element = document.createElement('div');
            element.className = 'resume-button';
            element.innerHTML = `

                ${videoCv.name}
            `;
            element.onclick = () => sendVideo(videoCv.video_message, vacancyName, vacancyId);
            resumeButtons.appendChild(element);
        });
    })
    .catch(error => console.error(error));
}
function closeModal (){
const resumeButtons = document.getElementById('resume-buttons');
resumeModal.style.display = 'none';
}
function sendVideo(videoCvName, vacancyName, vacancyId){
const message = "На Вашу вакансию "+ vacancyName + " поступил новый отклик";
url1 = "/videoCv/send"
data = {videoCvMessage:  videoCvName, userId: clientID, vacancyId: vacancyId}
     const response = fetch(url1, {
          method: 'POST', // Метод запроса
          headers: {
              'Content-Type': 'application/json' // Заголовок, указывающий на тип содержимого
          },
          body: JSON.stringify(data) // Данные, отправляемые в теле запроса, преобразованные в JSON
      });
      fetch("/jobSeeker/user/"+clientID, {
          method: 'GET', // Метод запроса
          headers: {
              'Content-Type': 'application/json' // Заголовок, указывающий на тип содержимого
          },

      }).then(responseJs => {
      if (!responseJs.ok) {
              throw new Error(`Ошибка HTTP: ${responseJs.status}`); // Бросаем ошибку, если ответ не в порядке
      }
            return responseJs.json();
      }).then(jobSeeker => {
        const url2 = "/response"
              data = {vacancy_id: parseInt (vacancyId), job_seeker_id: jobSeeker.id, comment: videoCvName}
              console.log (data);
                   const response1 = fetch(url2, {
                        method: 'POST', // Метод запроса
                        headers: {
                            'Content-Type': 'application/json' // Заголовок, указывающий на тип содержимого
                        },
                        body: JSON.stringify(data) // Данные, отправляемые в теле запроса, преобразованные в JSON
                    });

              alert("Ваше резюме было успешно отправлено");
              const resumeButtons = document.getElementById('resume-buttons');
      })




          // Открыть модальное окно
      resumeModal.style.display = 'none';
}

function formatDateTime(isoString) {
    // Создаем объект Date из строки ISO
    const date = new Date(isoString);

    // Форматируем дату
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0'); // Месяцы от 0 до 11
    const year = date.getFullYear();

    // Форматируем время
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');

    // Собираем строку в нужном формате
    return `${day}.${month}.${year} ${hours}:${minutes}`;
}
document.addEventListener('DOMContentLoaded', function(){
     fetch( `/vacancy/${vacancyId}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(response => {
                if (!response.ok) {

                    throw new Error(`Ошибка HTTP: ${response.status}`);
                }
                return response.json();

            }).then(data => {
                console.log (data);
                employerUserId=data.employer.user_id.id;
                document.getElementById("position").innerHTML = data.position;
                document.getElementById("employer_name").innerHTML = "<b>Компания: </b>" + data.employer.name;
                document.getElementById("city").innerHTML = "<b>Город: </b>" + data.city;
                document.getElementById("address").innerHTML = "<b>Адрес: </b>" + data.address;
                document.getElementById("salary").innerHTML += "<b>Зарплата: </b>";
                if (data.fromSalary!=null) {
                document.getElementById("salary").innerHTML += "от " + data.fromSalary + " ";
                }
                if (data.toSalary!=null) {
                document.getElementById("salary").innerHTML += "до " + data.toSalary + " ";
                }
                if (data.fromSalary==null && data.toSalary==null) {
                document.getElementById("salary").innerHTML += "не указана";
                }
                else {
                document.getElementById("salary").innerHTML += "руб";}
                document.getElementById("exp").innerHTML = "<b>Требуемый опыт работы: </b>" + data.exp;
                document.getElementById("workSchedule").innerHTML = "<b>График работы: </b>" + data.workSchedule;
                if (data.distantWork==true) {
                document.getElementById("workSchedule").innerHTML += ", возможно удаленно";}
               const paragraphs = data.responsibility.split(/\n\s*\n/);
                               const outputDiv = document.getElementById("responsibility");
                               paragraphs.forEach(paragraph => {
                                   if (paragraph.trim() !== "") {
                                       const p = document.createElement('p');
                                       p.classList.add('paragraph');
                                       p.textContent = paragraph.trim();
                                       outputDiv.appendChild(p);
                                   }
                               });
                document.getElementById("create_date").innerHTML = "<b>Дата публикации: </b>" + formatDateTime(data.createdDateTime);
                document.getElementById("response").onclick = () => {vacancy_responses(data.position, data.id)};
            });
setTimeout(() => {
                const loadingOverlay = document.getElementById("loading");
            loadingOverlay.style.display = "none";
            }, 1000);
});

function vacancy_no_resume() {
    alert("Вы откликнулись на вакансию без резюме!");
    // Логика отклика без резюме
}


