let clientID;

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
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.innerHTML = `
        <div class="notification-content">
            <i class="fas fa-${type === 'error' ? 'exclamation-circle' : type === 'success' ? 'check-circle' : 'info-circle'}"></i>
            <span>${message}</span>
        </div>
    `;
    
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background: var(--tg-bg-secondary);
        border: 1px solid var(--tg-bg-tertiary);
        border-radius: var(--radius-md);
        padding: var(--spacing-md);
        color: var(--tg-text-primary);
        z-index: 1001;
        transform: translateX(100%);
        transition: transform var(--transition-normal);
        max-width: 300px;
        box-shadow: var(--shadow-lg);
    `;
    
    document.body.appendChild(notification);
    
    setTimeout(() => {
        notification.style.transform = 'translateX(0)';
    }, 100);
    
    setTimeout(() => {
        notification.style.transform = 'translateX(100%)';
        setTimeout(() => {
            if (notification.parentNode) {
                notification.parentNode.removeChild(notification);
            }
        }, 300);
    }, 5000);
}

// Обработчик касания для скрытия клавиатуры на мобильных
document.addEventListener('touchstart', function(event) {
    const activeElement = document.activeElement;

    if (activeElement && (activeElement.tagName === 'INPUT' || activeElement.tagName === 'TEXTAREA')) {
        // Проверяем, что нажали вне поля ввода
        if (!activeElement.contains(event.target)) {
            activeElement.blur();
        }
    }
});

// Функция для заполнения выпадающего списка городов
function populateCitySelect() {
    fetch('/vacancy/city')
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error: ${response.status}`);
            }
            return response.json();
        })
        .then(russianCities => {
            console.log('Cities loaded:', russianCities);
            const citySelect = document.getElementById('city-select');
            russianCities.forEach(city => {
                const option = document.createElement('option');
                option.value = city.name;
                option.text = city.name;
                citySelect.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Error loading cities:', error);
            showNotification('Ошибка загрузки списка городов', 'error');
        });
}

// Функция для заполнения выпадающего списка отраслей
function industrySelect() {
    fetch('/industry/showall')
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error: ${response.status}`);
            }
            return response.json();
        })
        .then(industries => {
            console.log('Industries loaded:', industries);
            const industrySelect = document.getElementById('industry-select');
            industries.forEach(industry => {
                const option = document.createElement('option');
                option.value = industry.id;
                option.text = industry.category;
                industrySelect.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Error loading industries:', error);
            showNotification('Ошибка загрузки списка отраслей', 'error');
        });
}

// Функция для выбора опыта работы
function chooseWorkExperience(button) {
    // Убираем активный класс со всех кнопок
    const allButtons = document.querySelectorAll('.experience-button');
    allButtons.forEach(btn => {
        btn.classList.remove('active');
    });
    
    // Добавляем активный класс к выбранной кнопке
    button.classList.add('active');
}

// Функция для возврата назад
function goBack() {
    window.location.href = '/employer/lk/';
}

// Функция валидации формы
function validateForm() {
    const requiredFields = [
        { id: 'name', label: 'Название вакансии' },
        { id: 'industry-select', label: 'Отрасль' },
        { id: 'workSchedule', label: 'Тип занятости' },
        { id: 'city-name', label: 'Город' }
    ];
    
    const missingFields = [];
    
    requiredFields.forEach(field => {
        const element = document.getElementById(field.id);
        if (element && !element.value.trim()) {
            missingFields.push(field.label);
            element.style.borderColor = 'var(--tg-accent-danger)';
        } else if (element) {
            element.style.borderColor = '';
        }
    });
    
    if (missingFields.length > 0) {
        showNotification(`Пожалуйста, заполните обязательные поля: ${missingFields.join(', ')}`, 'error');
        return false;
    }
    
    // Проверяем, выбран ли опыт работы
    const activeExperience = document.querySelector('.experience-button.active');
    if (!activeExperience) {
        showNotification('Пожалуйста, выберите требуемый опыт работы', 'error');
        return false;
    }
    
    return true;
}

// Функция отправки формы
function submit() {
    if (!validateForm()) {
        return;
    }
    
    showLoading();
    
    const formData = {
        position: document.getElementById("name").value,
        industry_id: document.getElementById("industry-select").value,
        workSchedule: document.getElementById("workSchedule").value,
        distantWork: document.getElementById("remoteWork").checked,
        city: document.getElementById("city-name").value,
        address: document.getElementById("address").value,
        exp: document.querySelector('.experience-button.active span')?.innerText || 'Нет опыта',
        fromSalary: document.getElementById("salaryfrom").value || null,
        toSalary: document.getElementById("salaryto").value || null,
        responsibility: document.getElementById("textarea").value,
        employer_user_id: clientID
    };
    
    console.log('Submitting vacancy data:', formData);
    
    fetch('/vacancy', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        hideLoading();
        console.log('Vacancy created successfully:', data);
        showNotification('Вакансия успешно создана!', 'success');
        
        // Перенаправляем на страницу вакансий через 2 секунды
        setTimeout(() => {
            window.location.href = '/employer/lk/';
        }, 2000);
    })
    .catch(error => {
        hideLoading();
        console.error('Error creating vacancy:', error);
        showNotification('Ошибка при создании вакансии. Попробуйте позже.', 'error');
    });
}

// Инициализация при загрузке страницы
function initializePage() {
    industrySelect();
    populateCitySelect();
    
    // Добавляем обработчик отправки формы
    const form = document.getElementById('vacancyForm');
    if (form) {
        form.addEventListener('submit', function(e) {
            e.preventDefault();
            submit();
        });
    }
    
    // Добавляем валидацию в реальном времени
    const inputs = document.querySelectorAll('.form-input, .form-select, .form-textarea');
    inputs.forEach(input => {
        input.addEventListener('blur', function() {
            if (this.hasAttribute('required') && !this.value.trim()) {
                this.style.borderColor = 'var(--tg-accent-danger)';
            } else {
                this.style.borderColor = '';
            }
        });
        
        input.addEventListener('input', function() {
            if (this.style.borderColor === 'var(--tg-accent-danger)' && this.value.trim()) {
                this.style.borderColor = '';
            }
        });
    });
}

// Вызываем инициализацию после загрузки страницы
document.addEventListener('DOMContentLoaded', initializePage);