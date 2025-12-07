let clientID;
let employerId;

function resolveClientId() {
    if (!window.Telegram || !window.Telegram.WebApp) {
        throw new Error('Telegram WebApp unavailable');
    }
    try {
        window.Telegram.WebApp.ready();
    } catch (e) {
        console.warn('Telegram WebApp.ready() failed', e);
    }
    const userId = window.Telegram.WebApp.initDataUnsafe?.user?.id;
    if (!userId) {
        throw new Error('Telegram user id missing in initData');
    }
    return userId;
}

// Инициализация Telegram WebApp
try {
    clientID = resolveClientId();
    
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
    console.error('Failed to read Telegram user id', error);
    clientID = null;
    alert('Не удалось авторизироваться. Пожалуйста, обновите страницу');
}

console.log('Client ID:', clientID);

// Функция поддержки
function support() {
    window.Telegram.WebApp.openTelegramLink(`https://t.me/Cherchent`);
    window.Telegram.WebApp.close();
}

// Функция удаления работодателя
function delete_employer() {
    if (confirm("Вы действительно хотите удалить Ваш аккаунт? Это действие нельзя отменить.")) {
        showLoading();
        
        fetch(`/employer/${employerId}`, {
            method: 'DELETE',
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
            hideLoading();
            window.Telegram.WebApp.openTelegramLink(`https://t.me/tworker_ru_bot?start=start`);
            window.Telegram.WebApp.close();
        })
        .catch(error => {
            hideLoading();
            console.error('Ошибка при удалении аккаунта:', error);
            alert('Произошла ошибка при удалении аккаунта. Попробуйте позже.');
        });
    }
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
        const response = await fetch(`/employer/statistics/${employerId}`, {
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
                totalViews: 0,
                totalResponses: 0,
                activeVacancies: 0
            });
        }
    } catch (error) {
        console.log('Ошибка загрузки статистики, используем заглушки:', error);
        updateStatisticsDisplay({
            totalViews: 0,
            totalResponses: 0,
            activeVacancies: 0
        });
    }
}

// Функция обновления отображения статистики
function updateStatisticsDisplay(stats) {
    const totalViews = document.getElementById('total-views');
    const totalResponses = document.getElementById('total-responses');
    const activeVacancies = document.getElementById('active-vacancies');
    
    if (totalViews) totalViews.textContent = stats.totalViews || 0;
    if (totalResponses) totalResponses.textContent = stats.totalResponses || 0;
    if (activeVacancies) activeVacancies.textContent = stats.activeVacancies || 0;
}

// Функция анимации появления элементов
function animateElements() {
    const cards = document.querySelectorAll('.action-card');
    const statsSection = document.querySelector('.stats-section');
    
    // Анимация для карточек
    cards.forEach((card, index) => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(20px)';
        
        setTimeout(() => {
            card.style.transition = 'all 0.6s ease';
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, index * 100);
    });
    
    // Анимация для секции статистики
    if (statsSection) {
        setTimeout(() => {
            statsSection.style.transition = 'all 0.6s ease';
            statsSection.style.opacity = '1';
            statsSection.style.transform = 'translateY(0)';
        }, 600);
    }
}

// Основная инициализация при загрузке страницы
document.addEventListener('DOMContentLoaded', function() {
    // Загружаем данные работодателя
    fetch(`/employer/user/${clientID}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            console.log('Работодатель не найден, перенаправляем на регистрацию');
            window.location.href = '/employer/reg/';
            throw new Error(`Ошибка HTTP: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        employerId = data.employer_id;
        console.log('Employer ID:', employerId);
        
        // Загружаем статистику после получения ID работодателя
        loadStatistics();
    })
    .catch(error => {
        console.error('Ошибка при загрузке данных работодателя:', error);
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
    if (employerId) {
        loadStatistics();
    }
}

// Автообновление статистики каждые 5 минут (опционально)
setInterval(refreshStatistics, 5 * 60 * 1000);