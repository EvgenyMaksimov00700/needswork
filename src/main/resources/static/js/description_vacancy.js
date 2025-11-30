let clientID;
let employerUserId;

// Инициализация Telegram WebApp
try {
    clientID = window.Telegram.WebApp.initDataUnsafe.user.id;
    
//    if (window.history.length > 1) {
//        window.Telegram.WebApp.BackButton.show();
//        window.Telegram.WebApp.BackButton.onClick(() => {
//            window.history.back();
//        });
//    }
    try { window.Telegram.WebApp.BackButton.hide(); } catch (e) {}

    try { window.Telegram.WebApp.closeButton.show(); } catch (e) {}
    
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

// Получаем ID вакансии из URL
const currentUrl = window.location.href;
const url = new URL(currentUrl);
const params = new URLSearchParams(url.search);
const vacancyId = params.get('id');

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

// Функция для показа уведомлений
function showNotification(message, type = 'info') {
    // Создаем уведомление
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.innerHTML = `
        <div class="notification-content">
            <i class="fas fa-${type === 'error' ? 'exclamation-circle' : type === 'success' ? 'check-circle' : 'info-circle'}"></i>
            <span>${message}</span>
        </div>
    `;
    
    // Добавляем стили
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background: var(--tg-bg-secondary);
        border: 1px solid var(--tg-bg-tertiary);
        border-radius: var(--border-radius-md);
        padding: var(--spacing-md);
        color: var(--tg-text-primary);
        z-index: 1001;
        transform: translateX(100%);
        transition: transform var(--transition-normal);
        max-width: 300px;
        box-shadow: var(--shadow-lg);
    `;
    
    document.body.appendChild(notification);
    
    // Показываем уведомление
    setTimeout(() => {
        notification.style.transform = 'translateX(0)';
    }, 100);
    
    // Скрываем через 5 секунд
    setTimeout(() => {
        notification.style.transform = 'translateX(100%)';
        setTimeout(() => {
            if (notification.parentNode) {
                notification.parentNode.removeChild(notification);
            }
        }, 300);
    }, 5000);
}

// Функция редактирования вакансии
function edit_vacancy() {
    showLoading();
    window.location.href = `/employer/vacancy/edit?id=${vacancyId}`;
}

// Функция просмотра откликов
function vacancy_responses() {
    showLoading();
    window.location.href = `/employer/responses7/show?id=${vacancyId}`;
}

// Функция удаления вакансии
function delete_vacancy() {
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
            showNotification('Вакансия успешно удалена!', 'success');
            
            // Переходим на страницу вакансий
            setTimeout(() => {
                window.location.href = "/employer/my_vacancy7/show";
            }, 1500);
        })
        .catch(error => {
            hideLoading();
            console.error('Ошибка при удалении вакансии:', error);
            showNotification('Ошибка при удалении вакансии', 'error');
        });
    }
}

// Функция форматирования даты и времени
function formatDateTime(isoString) {
    if (!isoString) return 'Не указана';
    
    try {
        const date = new Date(isoString);
        
        // Проверяем, что дата валидна
        if (isNaN(date.getTime())) {
            return 'Не указана';
        }
        
        // Форматируем дату
        const day = String(date.getDate()).padStart(2, '0');
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const year = date.getFullYear();
        
        // Форматируем время
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        
        return `${day}.${month}.${year} ${hours}:${minutes}`;
    } catch (error) {
        console.error('Ошибка форматирования даты:', error);
        return 'Не указана';
    }
}

// Функция форматирования зарплаты
function formatSalary(fromSalary, toSalary) {
    let salaryText = '';
    
    if (fromSalary != null && toSalary != null) {
        salaryText = `от ${fromSalary} до ${toSalary} руб`;
    } else if (fromSalary != null) {
        salaryText = `от ${fromSalary} руб`;
    } else if (toSalary != null) {
        salaryText = `до ${toSalary} руб`;
    } else {
        salaryText = 'Не указана';
    }
    
    return salaryText;
}

// Функция для обновления статистики откликов
async function updateResponsesCount() {
    const responsesCount = document.getElementById('responses-count');
    if (responsesCount && vacancyId) {
        try {
            const response = await fetch(`/response/vacancy/${vacancyId}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            
            if (response.ok) {
                const responses = await response.json();
                responsesCount.textContent = responses ? responses.length : 0;
            } else {
                responsesCount.textContent = '0';
            }
        } catch (error) {
            console.error('Ошибка при загрузке количества откликов:', error);
            responsesCount.textContent = '0';
        }
    }
}

// Функция для обновления просмотров
function updateViewsCount(views) {
    const viewsCount = document.getElementById('views-count');
    if (viewsCount && views !== undefined && views !== null) {
        viewsCount.textContent = views.toString();
    } else if (viewsCount) {
        viewsCount.textContent = '0';
    }
}

// Функция для загрузки данных вакансии
function loadVacancyData() {
    showLoading();
    
    fetch(`/vacancy/${vacancyId}`, {
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
        console.log('Данные вакансии:', data);
        
        // Обновляем заголовок вакансии
        const positionElement = document.getElementById("position");
        if (positionElement) {
            positionElement.innerHTML = `<span>${data.position || 'Название не указано'}</span>`;
        }
        
        // Обновляем название компании
        const employerNameElement = document.getElementById("employer_name");
        if (employerNameElement) {
            employerNameElement.textContent = data.employer ? data.employer.name : 'Название компании не указано';
        }
        
        // Обновляем город
        const cityElement = document.getElementById("city");
        if (cityElement) {
            cityElement.textContent = data.city || 'Город не указан';
        }
        
        // Обновляем адрес
        const addressElement = document.getElementById("address");
        if (addressElement) {
            addressElement.textContent = data.address || 'Адрес не указан';
        }
        
        // Обновляем зарплату
        const salaryElement = document.getElementById("salary");
        if (salaryElement) {
            const salaryText = formatSalary(data.fromSalary, data.toSalary);
            salaryElement.innerHTML = `<span>${salaryText}</span>`;
        }
        
        // Обновляем опыт работы
        const expElement = document.getElementById("exp");
        if (expElement) {
            expElement.textContent = data.exp || 'Не указан';
        }
        
        // Обновляем график работы
        const workScheduleElement = document.getElementById("workSchedule");
        if (workScheduleElement) {
            let scheduleText = data.workSchedule || 'Не указан';
            if (data.distantWork === true) {
                scheduleText += ', возможно удаленно';
            }
            workScheduleElement.textContent = scheduleText;
        }
        
        // Обновляем описание
        const responsibilityElement = document.getElementById("responsibility");
        if (responsibilityElement && data.responsibility) {
            responsibilityElement.innerHTML = '';
            
            const paragraphs = data.responsibility.split(/\n\s*\n/);
            paragraphs.forEach(paragraph => {
                if (paragraph.trim() !== "") {
                    const p = document.createElement('p');
                    p.classList.add('paragraph');
                    p.textContent = paragraph.trim();
                    responsibilityElement.appendChild(p);
                }
            });
        } else if (responsibilityElement) {
            responsibilityElement.innerHTML = '<p class="placeholder-text">Описание вакансии не указано</p>';
        }
        
        // Обновляем дату создания
        const createDateElement = document.getElementById("create_date");
        if (createDateElement) {
            createDateElement.textContent = formatDateTime(data.createdDateTime);
        }
        
        // Обновляем статистику просмотров (берем из данных вакансии)
        if (data.views !== undefined && data.views !== null) {
            updateViewsCount(data.views);
        }
        
        // Обновляем статистику откликов
        updateResponsesCount();
        
        hideLoading();
        
        // Запускаем анимации
        setTimeout(animateElements, 100);
        
    })
    .catch(error => {
        hideLoading();
        console.error('Ошибка при загрузке данных вакансии:', error);
        showNotification('Ошибка при загрузке данных вакансии', 'error');
        
        // Показываем сообщение об ошибке
        const responsibilityElement = document.getElementById("responsibility");
        if (responsibilityElement) {
            responsibilityElement.innerHTML = '<p class="placeholder-text">Ошибка при загрузке данных</p>';
        }
    });
}

// Функция анимации появления элементов
function animateElements() {
    const sections = document.querySelectorAll('.vacancy-header-section, .vacancy-details-section, .vacancy-description-section, .vacancy-stats-section');
    
    sections.forEach((section, index) => {
        section.style.opacity = '0';
        section.style.transform = 'translateY(20px)';
        
        setTimeout(() => {
            section.style.transition = 'all 0.6s ease';
            section.style.opacity = '1';
            section.style.transform = 'translateY(0)';
        }, index * 200);
    });
}

// Инициализация при загрузке страницы
document.addEventListener('DOMContentLoaded', function() {
    // Загружаем данные вакансии
    if (vacancyId) {
        loadVacancyData();
    } else {
        showNotification('ID вакансии не найден', 'error');
    }
    
    // Добавляем обработчики для кнопок
    const buttons = document.querySelectorAll('.btn-primary, .btn-secondary, .btn-danger, .btn-back');
    buttons.forEach(button => {
        button.addEventListener('click', function() {
            // Добавляем эффект нажатия
            this.style.transform = 'scale(0.98)';
            setTimeout(() => {
                this.style.transform = '';
            }, 150);
        });
        
        // Добавляем hover эффекты для мобильных устройств
        button.addEventListener('touchstart', function() {
            this.style.transform = 'scale(0.98)';
        });
        
        button.addEventListener('touchend', function() {
            this.style.transform = '';
        });
    });
    
    // Добавляем обработчики для секций
    const sections = document.querySelectorAll('.vacancy-header-section, .vacancy-details-section, .vacancy-description-section, .vacancy-stats-section');
    sections.forEach(section => {
        section.addEventListener('touchstart', function() {
            this.style.transform = 'scale(0.98)';
        });
        
        section.addEventListener('touchend', function() {
            this.style.transform = '';
        });
    });
});

// Функция для обновления данных в реальном времени (опционально)
function refreshVacancyData() {
    if (vacancyId) {
        loadVacancyData();
    }
}

// Автообновление данных каждые 5 минут (опционально)
setInterval(refreshVacancyData, 5 * 60 * 1000);

