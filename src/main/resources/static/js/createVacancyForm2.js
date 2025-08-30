let clientID;

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

// Функция для возврата назад
function goBack() {
    window.location.href = '/employer/lk/';
}

// Функция валидации формы
function validateForm() {
    const requiredFields = [
        { id: 'name', label: 'Название вакансии' },
        { id: 'industry', label: 'Отрасль' },
        { id: 'workSchedule', label: 'Тип занятости' },
        { id: 'city', label: 'Город' },
        { id: 'description', label: 'Описание вакансии' }
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
    
    return true;
}

// Функция отправки формы
function submit() {
    if (!validateForm()) {
        return;
    }
    
    showLoading();
    
    const formData = {
        name: document.getElementById("name").value,
        industry: document.getElementById("industry").value,
        workSchedule: document.getElementById("workSchedule").value,
        remoteWork: document.getElementById("remoteWork").checked,
        city: document.getElementById("city").value,
        description: document.getElementById("description").value,
        userId: clientID
    };
    
    console.log('Submitting vacancy data:', formData);
    
    fetch('/vacancy/create', {
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
