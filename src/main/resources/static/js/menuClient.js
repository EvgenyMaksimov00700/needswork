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
const position = params.get('position');
const salary = params.get('salary');
const exp = params.get('exp');
const bus = params.get('bus');
const time = params.get('time');
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
    encodeExistParams.append('bus', encodeURIComponent(bus));}
if (time) {
    existParams.append('time', time);
    encodeExistParams.append('time', encodeURIComponent(time));
    }


document.addEventListener('DOMContentLoaded', function(){
    const filter_button=document.getElementById('filter_button');
    filter_button.href=`/vacancy/filter/page?${existParams.toString()}`;
    const url = `/vacancy/filter?${existParams.toString()}`;
    console.log(url);
     fetch( url, {
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
               const vacancy_url = `/vacancy/description?id=${vacancy.id}&${existParams.toString()}`;
               const element = `<button class="vacancy" onclick="window.location.href='${vacancy_url}'">
                                   ${vacancy.position}<br>${salary}<br>${vacancy.city}<br>${vacancy.employer.name}
                               </button>`;
               vacancies.innerHTML += element;
            })
            });
});