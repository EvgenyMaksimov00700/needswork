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
    document.querySelector('.container').style.marginTop="100px";
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

function getEmployerByEmail(email) {
  const response = fetch(`/employer/user/email?email=${email}`);
  if (!response.ok) return null;

  const employer = response.json();
  return employer?.user_id?.id ?? null;

}




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


function addNewVideoCV(vacancyId){
    data = {userId: clientID, message:"Перед тем как выбрать вакансию, запишите видео-резюме (в формате видео сообщения кружка telegram) с вашей презентацией. Видео будет доступно только работодателям, которым вы отправите отклик. Следуйте инструкции по записи видео - резюме: https://drive.google.com/file/d/1CZz-rHORxlP_HcacAtY5jyQ56I5UpIV5/view"}
    const response = fetch("/message/send", {
         method: 'POST', // Метод запроса
         headers: {
             'Content-Type': 'application/json' // Заголовок, указывающий на тип содержимого
         },
         body: JSON.stringify(data) // Данные, отправляемые в теле запроса, преобразованные в JSON
     });
    const body_data ={userId: clientID, vacancyId: vacancyId, urlParams: decodeURIComponent(existParams.toString())}
    console.log(body_data)
    const response1 = fetch("/state/create", {
         method: 'POST', // Метод запроса
         headers: {
             'Content-Type': 'application/json' // Заголовок, указывающий на тип содержимого
         },
         body: JSON.stringify(body_data) // Данные, отправляемые в теле запроса, преобразованные в JSON
     });
    window.Telegram.WebApp.close();
}

let employerUserId;

function vacancy_responses(vacancyName, vacancyId, from_hh, email, employerUserId) {

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
    if (data && data.length > 0) {

        data.forEach(videoCv => {
            const element = document.createElement('div');
            element.className = 'resume-button';
            element.innerHTML = `

                ${videoCv.name}
            `;
            element.onclick = () => sendVideo(videoCv.video_message, vacancyName, vacancyId, from_hh, email, employerUserId);
            resumeButtons.appendChild(element);
        });

        const element = document.createElement('div');
        element.className = 'resume-button';
        element.innerHTML = `Добавить видеорезюме`;
        element.onclick = () => addNewVideoCV(vacancyId);
        resumeButtons.appendChild(element);
    }

        else {
            addNewVideoCV(vacancyId)
        }
    })
    .catch(error => console.error(error));
}
function closeModal (){
const resumeButtons = document.getElementById('resume-buttons');
resumeModal.style.display = 'none';
}
function sendVideo(videoCvName, vacancyName, vacancyId, from_hh, email, employerUserId) {
    console.log(from_hh)
    if (!from_hh || employerUserId!=null) {
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
      }


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
              fetch(url2, {
                        method: 'POST', // Метод запроса
                        headers: {
                            'Content-Type': 'application/json' // Заголовок, указывающий на тип содержимого
                        },
                        body: JSON.stringify(data) // Данные, отправляемые в теле запроса, преобразованные в JSON
                    }).then(responseJs => {
                    if (!responseJs.ok) {
                            throw new Error(`Ошибка HTTP: ${responseJs.status}`); // Бросаем ошибку, если ответ не в порядке
                    }
                          return responseJs.json();
                    }).then(response1 => {
                          if (from_hh && employerUserId==null) {
                            const data1= {email:email, responseID: response1.id, vacancyName: vacancyName}
                            const url3 = "/api/email/send"
                            fetch(url3, {
                                method: 'POST', // Метод запроса
                                headers: {
                                    'Content-Type': 'application/json' // Заголовок, указывающий на тип содержимого
                                },
                                body: JSON.stringify(data1) // Данные, отправляемые в теле запроса, преобразованные в JSON
                            });
                          }
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

                if (data.employer.user_id!=null) {
                employerUserId=data.employer.user_id.id;}
                else {
                employerUserId=getEmployerByEmail(data.employer.email)}
                console.log (employerUserId);
                document.getElementById("position").innerHTML = data.position;
                document.getElementById("employer_name").innerHTML = "<b>Компания: </b>" + data.employer.name;
                if (data.employer.email!=null) {
                    document.getElementById("employer_phone").innerHTML = "<b>Тел: </b>" + data.employer.phone;
                }
                document.getElementById("city").innerHTML = "<b>Город: </b>" + data.city;
                if (data.address!=null) {
                document.getElementById("address").innerHTML = "<b>Адрес: </b>" + data.address;}

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
                               if (!data.from_hh) {
               paragraphs.forEach(paragraph => {
                                              if (paragraph.trim() !== "") {
                                                  const p = document.createElement('p');
                                                  p.classList.add('paragraph');
                                                  p.textContent = paragraph.trim();
                                                  outputDiv.appendChild(p);
                                              }
                                                          });

                               }
                               else {
                               outputDiv.innerHTML=data.responsibility
                               }
                document.getElementById("create_date").innerHTML = "<b>Дата публикации: </b>" + formatDateTime(data.createdDateTime);
                document.getElementById("response").onclick = () => {vacancy_responses(data.position, data.id, data.from_hh, data.employer.email, employerUserId)};
                document.getElementById("no-resume").onclick = () => {vacancy_no_resume(data.position, data.id, data.from_hh, data.employer.email, employerUserId)};
                document.getElementById("text-resume").onclick = () => {vacancy_text_resume(data.position, data.id, data.from_hh, data.employer.email, employerUserId)};
            });
setTimeout(() => {
                const loadingOverlay = document.getElementById("loading");
            loadingOverlay.style.display = "none";
            }, 1000);
});

function vacancy_no_resume(vacancyName, vacancyId,  from_hh, email, employerUserId) {
    if (!from_hh || employerUserId!=null) {
        const message = "На Вашу вакансию "+ vacancyName + " поступил новый отклик";
        url1 = "/videoCv/send"
        data = {videoCvMessage:  null, userId: clientID, vacancyId: vacancyId}
             const response = fetch(url1, {
                  method: 'POST', // Метод запроса
                  headers: {
                      'Content-Type': 'application/json' // Заголовок, указывающий на тип содержимого
                  },
                  body: JSON.stringify(data) // Данные, отправляемые в теле запроса, преобразованные в JSON
              });
     }
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
          data = {vacancy_id: parseInt (vacancyId), job_seeker_id: jobSeeker.id, comment: null}
          console.log (data);
           const response1 = fetch(url2, {
                method: 'POST', // Метод запроса
                headers: {
                    'Content-Type': 'application/json' // Заголовок, указывающий на тип содержимого
                },
                body: JSON.stringify(data) // Данные, отправляемые в теле запроса, преобразованные в JSON
            }).then(responseJs => {
              if (!responseJs.ok) {
                      throw new Error(`Ошибка HTTP: ${responseJs.status}`); // Бросаем ошибку, если ответ не в порядке
              }
                    return responseJs.json();
              }).then(response1 => {
                    if (from_hh && employerUserId==null) {
                      const data1= {email:email, responseID: response1.id, vacancyName: vacancyName}
                      const url3 = "/api/email/send"
                      fetch(url3, {
                          method: 'POST', // Метод запроса
                          headers: {
                              'Content-Type': 'application/json' // Заголовок, указывающий на тип содержимого
                          },
                          body: JSON.stringify(data1) // Данные, отправляемые в теле запроса, преобразованные в JSON
                      });
                    }
              });

          alert("Ваше резюме было успешно отправлено");
          const resumeButtons = document.getElementById('resume-buttons');
  })
// Логика отклика без резюме
}
function vacancy_text_resume(vacancyName, vacancyId, from_hh, email, employerUserId) {
     if (!from_hh || employerUserId!=null) {
        const message = "На Вашу вакансию "+ vacancyName + " поступил новый отклик";
        url1 = "/videoCv/send"
        data = {videoCvMessage:  null, userId: clientID, vacancyId: vacancyId, textResume: true}
         const response = fetch(url1, {
              method: 'POST', // Метод запроса
              headers: {
                  'Content-Type': 'application/json' // Заголовок, указывающий на тип содержимого
              },
              body: JSON.stringify(data) // Данные, отправляемые в теле запроса, преобразованные в JSON
          });
      }



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
                  data = {vacancy_id: parseInt (vacancyId), job_seeker_id: jobSeeker.id, comment: jobSeeker.textResume}
                  console.log (data);
                   fetch(url2, {
                        method: 'POST', // Метод запроса
                        headers: {
                            'Content-Type': 'application/json' // Заголовок, указывающий на тип содержимого
                        },
                        body: JSON.stringify(data) // Данные, отправляемые в теле запроса, преобразованные в JSON
                    }).then(responseJs => {
                        if (!responseJs.ok) {
                                throw new Error(`Ошибка HTTP: ${responseJs.status}`); // Бросаем ошибку, если ответ не в порядке
                        }
                              return responseJs.json();
                        }).then(response1 => {
                              if (from_hh && employerUserId==null) {
                                const data1= {email:email, responseID: response1.id, vacancyName: vacancyName}
                                const url3 = "/api/email/send"
                                fetch(url3, {
                                    method: 'POST', // Метод запроса
                                    headers: {
                                        'Content-Type': 'application/json' // Заголовок, указывающий на тип содержимого
                                    },
                                    body: JSON.stringify(data1) // Данные, отправляемые в теле запроса, преобразованные в JSON
                                });
                              }
                        });

                  alert("Ваше резюме было успешно отправлено");
                  const resumeButtons = document.getElementById('resume-buttons');
      })
}


