let clientID;
//try {
clientID = window.Telegram.WebApp.initDataUnsafe.user.id;
const tg = window.Telegram.WebApp;
tg.MainButton
  .setText('Закрыть')
  .show();
tg.MainButton.onClick(() => tg.close());
    function isDesktop() {
        const userAgent = navigator.userAgent.toLowerCase();
        return userAgent.includes("windows") || userAgent.includes("macintosh") || userAgent.includes("linux");
    }
    console.log(isDesktop());
    if (!isDesktop()) {
    document.querySelector(".container").style.marginTop = "90px";
        window.Telegram.WebApp.requestFullscreen();
    }

window.Telegram.WebApp.expand();
/*}
catch(error) {clientID = 159619887}*/
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
function getCurrentPage() {
const params = new URLSearchParams(window.location.search);
const page = parseInt(params.get("page"));
return isNaN(page) || page < 1 ? 1 : page;
}

function updatePageNumberDisplay(page) {
document.getElementById("page-number").textContent = page;
}

function goToPage(page) {
window.location.search = `?page=${page}&${existParams.toString()}`;
}

function prevPage() {
const currentPage = getCurrentPage();
if (currentPage > 1) {
  goToPage(currentPage - 1);
}
}

function nextPage() {
const currentPage = getCurrentPage();
goToPage(currentPage + 1);
}

document.addEventListener('DOMContentLoaded', function(){
    try {
        const webApp = window.Telegram.WebApp;
        const startParam = webApp.initDataUnsafe.start_param;
        const visitedKey = 'vacancyRedirectDone';

        if (!sessionStorage.getItem(visitedKey) && startParam && startParam.startsWith('vacancy_')) {
            const vacancyId = startParam.split('_')[1];
            sessionStorage.setItem(visitedKey, 'true');
            window.location.href = `/vacancy/description?id=${vacancyId}`;
            return;
        }
    } catch (e) {
         console.warn('Telegram WebApp init error', e);
    }
    const filter_button=document.getElementById('filter_button');
    filter_button.href=`/vacancy/filter/page?${existParams.toString()}`;
    const currentPage = getCurrentPage();
    updatePageNumberDisplay(currentPage);

    const url = `/vacancy/filter?page=${currentPage}&${existParams.toString()}`;
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
            console.log(data);
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
                                   <b>${vacancy.position}</b><br>${salary}<br>${vacancy.city}<br>${vacancy.employer.name}
                               </button>`;
               vacancies.innerHTML += element;
            })
            }).then(() => {
            const loadingOverlay = document.getElementById("loading");
            loadingOverlay.style.display = "none";
            });
});
