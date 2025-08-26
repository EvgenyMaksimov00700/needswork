let clientID;
try {
clientID = window.Telegram.WebApp.initDataUnsafe.user.id;
    function isDesktop() {
        const userAgent = navigator.userAgent.toLowerCase();
        return userAgent.includes("windows") || userAgent.includes("macintosh") || userAgent.includes("linux");
    }
    console.log(isDesktop());
    if (!isDesktop()) {
        document.querySelector(".app-container").style.top = "120px";
        document.querySelector(".app-header").style.top = "120px";
        window.Telegram.WebApp.requestFullscreen();
    }

window.Telegram.WebApp.expand();
}
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

function support(){
    window.Telegram.WebApp.openTelegramLink(`https://t.me/Cherchent`); // URL с ID чата
    window.Telegram.WebApp.close();
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

// Функция для форматирования зарплаты
function formatSalary(fromSalary, toSalary) {
    let salary = "";
    if (fromSalary != null) {
        salary += `от ${fromSalary}`;
    }
    if (toSalary != null) {
        salary += ` до ${toSalary}`;
    }
    if (salary == "") {
        salary = "Не указано";
    }
    return salary;
}

// Функция для создания современной карточки вакансии
function createVacancyCard(vacancy, vacancyUrl) {
    const salary = formatSalary(vacancy.fromSalary, vacancy.toSalary);
    
    return `
        <div class="vacancy" onclick="window.location.href='${vacancyUrl}'" role="listitem" tabindex="0" onkeypress="handleVacancyKeyPress(event, '${vacancyUrl}')">
            <div class="vacancy-header">
                <h3 class="vacancy-title">${escapeHtml(vacancy.position)}</h3>
            </div>
            <div class="vacancy-details">
                <div class="vacancy-salary">
                    <i class="fas fa-money-bill-wave" aria-hidden="true"></i>
                    <span>${escapeHtml(salary)}</span>
                </div>
                <div class="vacancy-location">
                    <i class="fas fa-map-marker-alt" aria-hidden="true"></i>
                    <span>${escapeHtml(vacancy.city)}</span>
                </div>
                <div class="vacancy-company">
                    <i class="fas fa-building" aria-hidden="true"></i>
                    <span>${escapeHtml(vacancy.employer.name)}</span>
                </div>
            </div>
        </div>
    `;
}

// Функция для экранирования HTML
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Обработка нажатия клавиш для карточек вакансий
function handleVacancyKeyPress(event, url) {
    if (event.key === 'Enter' || event.key === ' ') {
        event.preventDefault();
        window.location.href = url;
    }
}

// Функция для показа состояния загрузки
function showLoading() {
    const loadingOverlay = document.getElementById("loading");
    loadingOverlay.style.display = "flex";
}

// Функция для скрытия состояния загрузки
function hideLoading() {
    const loadingOverlay = document.getElementById("loading");
    loadingOverlay.style.display = "none";
}

// Функция для показа ошибки
function showError(message) {
    const vacancies = document.getElementById("vacancies");
    vacancies.innerHTML = `
        <div class="error-state">
            <i class="fas fa-exclamation-triangle" aria-hidden="true"></i>
            <h3>Ошибка загрузки</h3>
            <p>${escapeHtml(message)}</p>
        </div>
    `;
}

// Функция для показа пустого состояния
function showEmptyState() {
    const vacancies = document.getElementById("vacancies");
    vacancies.innerHTML = `
        <div class="empty-state">
            <i class="fas fa-search" aria-hidden="true"></i>
            <h3>Вакансии не найдены</h3>
            <p>Попробуйте изменить параметры поиска</p>
        </div>
    `;
}

// Функция для загрузки вакансий с обработкой ошибок
async function loadVacancies() {
    try {
        showLoading();
        
        const currentPage = getCurrentPage();
        updatePageNumberDisplay(currentPage);
        
        const url = `/vacancy/filter?page=${currentPage}&${existParams.toString()}`;
        console.log('Loading vacancies from:', url);
        
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        
        if (!response.ok) {
            throw new Error(`HTTP ${response.status}: ${response.statusText}`);
        }
        
        const data = await response.json();
        console.log('Vacancies loaded:', data);
        
        const vacancies = document.getElementById("vacancies");
        
        if (!Array.isArray(data) || data.length === 0) {
            showEmptyState();
        } else {
            vacancies.innerHTML = data.map(vacancy => {
                const vacancy_url = `/vacancy/description?id=${vacancy.id}&${existParams.toString()}`;
                return createVacancyCard(vacancy, vacancy_url);
            }).join('');
        }
        
    } catch (error) {
        console.error('Error loading vacancies:', error);
        showError('Не удалось загрузить вакансии. Проверьте подключение к интернету и попробуйте снова.');
    } finally {
        hideLoading();
    }
}

// Обработчик для повторной попытки загрузки при ошибке сети
function retryLoad() {
    loadVacancies();
}

// Добавляем обработчик для офлайн/онлайн событий
window.addEventListener('online', () => {
    console.log('Connection restored');
    loadVacancies();
});

window.addEventListener('offline', () => {
    console.log('Connection lost');
    showError('Нет подключения к интернету. Проверьте соединение и попробуйте снова.');
});

document.addEventListener('DOMContentLoaded', function(){
    try {
        const webApp = window.Telegram.WebApp;
        const startParam = webApp.initDataUnsafe.start_param;
        const visitedKey = 'vacancyRedirectDone';

        if (!sessionStorage.getItem(visitedKey) && startParam && startParam.startsWith('vacancy_')) {

            const user = webApp.initDataUnsafe.user;
            if (!user) {
                console.warn('Нет данных о пользователе из Telegram');

            }
            else {

                // соберём DTO
                const jobSeekerDTO = {
                    user_id: user.id,
                    fullName: user.first_name + (user.last_name ? ' ' + user.last_name : ''),
                    username: user.username,
                    latitude: 0,
                    longitude: 0
                };

                // вызов вашей ручки
                const response = fetch('/jobSeeker/auth', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(jobSeekerDTO)
                });

                if (!response.ok) {
                    console.error('Ошибка при добавлении jobSeeker', response.status);
                } else {
                    const data = await response.json();
                    console.log('JobSeeker создан/найден:', data);
                }
            }
            const vacancyId = startParam.split('_')[1];
            sessionStorage.setItem(visitedKey, 'true');
            window.location.href = `/vacancy/description?id=${vacancyId}`;
            return;
        }
    } catch (e) {
         console.warn('Telegram WebApp init error', e);
    }
    
    // Настройка ссылки фильтра
    const filter_button = document.getElementById('filter_button');
    filter_button.href = `/vacancy/filter/page?${existParams.toString()}`;
    
    // Загрузка вакансий
    loadVacancies();
    
    // Добавляем обработчик для свайпа (опционально)
    let startX = 0;
    let startY = 0;
    
    document.addEventListener('touchstart', (e) => {
        startX = e.touches[0].clientX;
        startY = e.touches[0].clientY;
    });
    
    document.addEventListener('touchend', (e) => {
        if (!startX || !startY) return;
        
        const endX = e.changedTouches[0].clientX;
        const endY = e.changedTouches[0].clientY;
        
        const diffX = startX - endX;
        const diffY = startY - endY;
        
        // Минимальное расстояние для свайпа
        if (Math.abs(diffX) > Math.abs(diffY) && Math.abs(diffX) > 50) {
            if (diffX > 0) {
                // Свайп влево - следующая страница
                nextPage();
            } else {
                // Свайп вправо - предыдущая страница
                prevPage();
            }
        }
        
        startX = 0;
        startY = 0;
    });
});
