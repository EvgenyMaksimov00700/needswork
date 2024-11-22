let clientID;
try {clientID = window.Telegram.WebApp.initDataUnsafe.user.id;
window.Telegram.WebApp.requestFullscreen();
window.Telegram.WebApp.expand();
}

catch(error) {clientID = 159619887}
console.log(clientID)

const currentUrl = window.location.href;
const url = new URL(currentUrl);
const params = new URLSearchParams(url.search);
const vacancyId = params.get('id');
const city = params.get('city');
const industry = params.get('industry');
function edit_vacancy () {
window.location.href=`/employer/vacancy/edit?id=${vacancyId}`}
function vacancy_back() {
window.location.href=`/vacancy/menu?city=${encodeURIComponent(city)}&industry=${encodeURIComponent(industry)}`}

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

function sendVideo(videoCvName, videoName, vacancyId){
const message = "На Вашу вакансию "+ videoName + " поступил новый отклик";
const url1 = "/message/send"
data = {message:  message, userId: employerUserId}
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
                document.getElementById("responsibility").innerHTML = data.responsibility;
                document.getElementById("create_date").innerHTML = "<b>Дата публикации: </b>" + data.createdDateTime;
                document.getElementById("response").onclick = () => {vacancy_responses(data.position, data.id)};
            });

});

