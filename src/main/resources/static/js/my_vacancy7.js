let clientID;
let employerId;

// Инициализация Telegram WebApp
try {
    clientID = window.Telegram.WebApp.initDataUnsafe.user.id;
    
    if (window.history.length > 1) {
        window.Telegram.WebApp.BackButton.show();
        window.Telegram.WebApp.BackButton.onClick(() => {
            window.history.back();
        });
    }
    
    window.Telegram.WebApp.expand();
    
    function isDesktop() {
        const userAgent = navigator.userAgent.toLowerCase();
        return userAgent.includes("windows") || userAgent.includes("macintosh") || userAgent.includes("linux");
    }
    
    console.log('Desktop:', isDesktop());
    
    if (!isDesktop()) {
        window.Telegram.WebApp.requestFullscreen();
    }
} catch(error) {
    clientID = 159619887;
    console.log('Using fallback clientID:', clientID);
}

console.log('Client ID:', clientID);

// Функция поддержки
function support() {
    window.Telegram.WebApp.openTelegramLink(`https://t.me/Cherchent`);
    window.Telegram.WebApp.close();
}

// Функции для работы с загрузкой
function showLoading() {
    const loading = document.getElementById('loading');
    if (loading) {
        loading.classList.add('show');
    }
}

function hideLoading() {
    const loading = document.getElementById('loading');
    if (loading) {
        loading.classList.remove('show');
    }
}

// Функция загрузки статистики
async function loadStatistics() {
    try {
        const response = await fetch(`/employer/statistics/${clientID}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        
        if (response.ok) {
            const stats = await response.json();
            updateStatisticsDisplay(stats);
        } else {
            console.log('Статистика недоступна, используем заглушки');
            updateStatisticsDisplay({
                totalVacancies: 0,
                totalViews: 0,
                totalApplications: 0
            });
        }
    } catch (error) {
        console.log('Ошибка загрузки статистики, используем заглушки:', error);
        updateStatisticsDisplay({
            totalVacancies: 0,
            totalViews: 0,
            totalApplications: 0
        });
    }
}

// Функция обновления отображения статистики
function updateStatisticsDisplay(stats) {
    const totalVacancies = document.getElementById('total-vacancies');
    const totalViews = document.getElementById('total-views');
    const totalApplications = document.getElementById('total-applications');
    
    if (totalVacancies) totalVacancies.textContent = stats.totalVacancies || 0;
    if (totalViews) totalViews.textContent = stats.totalViews || 0;
    if (totalApplications) totalApplications.textContent = stats.totalApplications || 0;
}

// Функция анимации появления элементов
function animateElements() {
    const cards = document.querySelectorAll('.action-card');
    const vacancyCards = document.querySelectorAll('.vacancy-card');
    const statCards = document.querySelectorAll('.stat-card');
    
    // Анимация для action cards
    cards.forEach((card, index) => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(20px)';
        
        setTimeout(() => {
            card.style.transition = 'all 0.6s ease';
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, index * 100);
    });
    
    // Анимация для vacancy cards
    vacancyCards.forEach((card, index) => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(20px)';
        
        setTimeout(() => {
            card.style.transition = 'all 0.6s ease';
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, (cards.length * 100) + (index * 100));
    });
    
    // Анимация для stat cards
    statCards.forEach((card, index) => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(20px)';
        
        setTimeout(() => {
            card.style.transition = 'all 0.6s ease';
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, (cards.length * 100) + (vacancyCards.length * 100) + (index * 100));
    });
}

// Функция создания карточки вакансии
function createVacancyCard(vacancy) {
    // Формируем зарплату как в старом коде
    let salary = "";
    if (vacancy.fromSalary != null) {
        salary += `от ${vacancy.fromSalary}`;
    }
    if (vacancy.toSalary != null) {
        salary += ` до ${vacancy.toSalary}`;
    }
    if (salary == "") {
        salary = "Не указано";
    }
    
    return `
        <div class="vacancy-card" onclick="editVacancy(${vacancy.id})" data-id="${vacancy.id}">
            <div class="vacancy-header">
                <div>
                    <h3 class="vacancy-title">${vacancy.position || 'Название вакансии'}</h3>
                    <p class="vacancy-company">${vacancy.employer ? vacancy.employer.name : 'Компания'}</p>
                </div>
                <span class="vacancy-salary">${salary}</span>
            </div>
            <p class="vacancy-description">${vacancy.city || 'Город не указан'}</p>
            <div class="vacancy-footer">
                <div class="vacancy-stats">
                    <span><i class="fas fa-map-marker-alt" aria-hidden="true"></i> ${vacancy.city || 'Город не указан'}</span>
                    <span><i class="fas fa-building" aria-hidden="true"></i> ${vacancy.employer ? vacancy.employer.name : 'Компания'}</span>
                </div>
                <div class="vacancy-actions">
                    <button class="btn-edit" onclick="event.stopPropagation(); editVacancy(${vacancy.id})" aria-label="Редактировать вакансию">
                        <i class="fas fa-edit" aria-hidden="true"></i>
                    </button>
                    <button class="btn-delete" onclick="event.stopPropagation(); deleteVacancy(${vacancy.id})" aria-label="Удалить вакансию">
                        <i class="fas fa-trash" aria-hidden="true"></i>
                    </button>
                </div>
            </div>
        </div>
    `;
}

// Функция редактирования вакансии
function editVacancy(vacancyId) {
    showLoading();
    window.location.href = `/employer/vacancy/description?id=${vacancyId}`;
}

// Функция удаления вакансии
function deleteVacancy(vacancyId) {
    if (confirm("Вы действительно хотите удалить эту вакансию?")) {
        showLoading();
        
        fetch(`/vacancy/${vacancyId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Ошибка HTTP: ${response.status}`);
            }
            return response;
        })
        .then(data => {
            hideLoading();
            const element = document.querySelector(`.vacancy-card[data-id='${vacancyId}']`);
            if (element) {
                element.remove();
                // Обновляем статистику после удаления
                loadStatistics();
                
                // Проверяем, есть ли еще вакансии
                const remainingVacancies = document.querySelectorAll('.vacancy-card');
                if (remainingVacancies.length === 0) {
                    showEmptyState();
                }
            }
        })
        .catch(error => {
            hideLoading();
            console.error('Ошибка при удалении вакансии:', error);
            alert('Произошла ошибка при удалении вакансии. Попробуйте позже.');
        });
    }
}

// Функция показа пустого состояния
function showEmptyState() {
    const loadingState = document.getElementById('loading-state');
    const emptyState = document.getElementById('empty-state');
    const vacanciesList = document.getElementById('vacancies-list');
    
    if (loadingState) loadingState.style.display = 'none';
    if (emptyState) emptyState.style.display = 'flex';
    if (vacanciesList) vacanciesList.style.display = 'none';
}

// Функция показа списка вакансий
function showVacanciesList() {
    const loadingState = document.getElementById('loading-state');
    const emptyState = document.getElementById('empty-state');
    const vacanciesList = document.getElementById('vacancies-list');
    
    if (loadingState) loadingState.style.display = 'none';
    if (emptyState) emptyState.style.display = 'none';
    if (vacanciesList) vacanciesList.style.display = 'grid';
}

// Основная инициализация при загрузке страницы
document.addEventListener('DOMContentLoaded', function() {
    // Загружаем вакансии
    fetch(`/vacancy/user/${clientID}`, {
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
        const vacanciesList = document.getElementById("vacancies-list");
        
        if (data && data.length > 0) {
            // Показываем список вакансий
            showVacanciesList();
            
            // Создаем карточки для каждой вакансии
            data.forEach(vacancy => {
                const vacancyCard = createVacancyCard(vacancy);
                vacanciesList.innerHTML += vacancyCard;
            });
        } else {
            // Показываем пустое состояние
            showEmptyState();
        }
        
        // Загружаем статистику
        loadStatistics();
    })
    .catch(error => {
        console.error('Ошибка при загрузке вакансий:', error);
        showEmptyState();
        loadStatistics();
    });
    
    // Запускаем анимации
    setTimeout(animateElements, 100);
    
    // Добавляем обработчики для карточек
    const actionCards = document.querySelectorAll('.action-card');
    actionCards.forEach(card => {
        card.addEventListener('click', function() {
            // Добавляем эффект нажатия
            this.style.transform = 'scale(0.98)';
            setTimeout(() => {
                this.style.transform = '';
            }, 150);
        });
        
        // Добавляем hover эффекты для мобильных устройств
        card.addEventListener('touchstart', function() {
            this.style.transform = 'scale(0.98)';
        });
        
        card.addEventListener('touchend', function() {
            this.style.transform = '';
        });
    });
});

// Функция для обновления статистики в реальном времени (опционально)
function refreshStatistics() {
    if (clientID) {
        loadStatistics();
    }
}

// Автообновление статистики каждые 5 минут (опционально)
setInterval(refreshStatistics, 5 * 60 * 1000);