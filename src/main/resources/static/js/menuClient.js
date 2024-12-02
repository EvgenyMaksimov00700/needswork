let clientID;
try {clientID = window.Telegram.WebApp.initDataUnsafe.user.id;
window.Telegram.WebApp.requestFullscreen();
window.Telegram.WebApp.expand();}
catch(error) {clientID = 159619887}
console.log(clientID)

const currentUrl = window.location.href;
const url = new URL(currentUrl);
const params = new URLSearchParams(url.search);
const city = params.get('city');
const industry = params.get('industry');
const company = params.get('company');
document.addEventListener('DOMContentLoaded', function(){
     fetch( `/vacancy/filter?city=${city}&industry=${industry}&company=${company}`, {
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
            const vacancies=document.getElementById("vacancies")
            data.forEach (vacancy => {
                let salary="";
                if (vacancy.fromSalary!=null) {
                    salary+=`от ${vacancy.fromSalary}`;
                }
                if (vacancy.toSalary!=null) {
                    salary+=` до ${vacancy.toSalary}`;
                }
                if (salary=="") {
                    salary="Не указано";
                }
               const vacancy_url = `/vacancy/description?id=${vacancy.id}&city=${encodeURIComponent(city)}&industry=${encodeURIComponent(industry)}`;
               const element = `<button class="vacancy" onclick="window.location.href='${vacancy_url}'">
                                   ${vacancy.position}<br>${salary}<br>${vacancy.city}<br>${vacancy.employer.name}
                               </button>`;
               vacancies.innerHTML += element;
            })
            });
});