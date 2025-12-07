let clientID;
let employer;

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
    alert('Не удалось определить пользователя Telegram. Пожалуйста, откройте мини-приложение из Telegram.');
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

// Функция для удаления логотипа
function removeLogo() {
    const logoPreview = document.getElementById('logo-preview');
    const logoInput = document.getElementById('logo');
    const fileUploadArea = document.getElementById('file-upload-area');
    const fileUploadContent = document.getElementById('file-upload-content');
    
    if (logoPreview) logoPreview.style.display = 'none';
    if (logoInput) logoInput.value = '';
    if (fileUploadArea) fileUploadArea.style.display = 'block';
    if (fileUploadContent) fileUploadContent.style.display = 'flex';
}

// Обработчик загрузки логотипа
document.addEventListener('DOMContentLoaded', function() {
    const logoInput = document.getElementById('logo');
    const fileUploadArea = document.getElementById('file-upload-area');
    const fileUploadContent = document.getElementById('file-upload-content');
    
    if (logoInput) {
        logoInput.addEventListener('change', e => {
            const file = e.target.files[0];
            if (!file) return;
            
            // Проверяем размер файла (5MB)
            if (file.size > 5 * 1024 * 1024) {
                alert('Файл слишком большой. Максимальный размер: 5MB');
                logoInput.value = '';
                return;
            }
            
            // Проверяем тип файла
            if (!['image/png', 'image/jpeg', 'image/jpg'].includes(file.type)) {
                alert('Поддерживаются только файлы PNG, JPG, JPEG');
                logoInput.value = '';
                return;
            }
            
            const url = URL.createObjectURL(file);
            const img = document.getElementById('logoPreview');
            const logoPreview = document.getElementById('logo-preview');
            
            if (img) {
                img.src = url;
                img.style.display = 'block';
            }
            
            if (logoPreview) {
                logoPreview.style.display = 'inline-block';
            }
            
            if (fileUploadArea) {
                fileUploadArea.style.display = 'none';
            }
        });
    }
    
    // Drag and Drop для файлов
    if (fileUploadArea) {
        fileUploadArea.addEventListener('dragover', function(e) {
            e.preventDefault();
            this.classList.add('dragover');
        });
        
        fileUploadArea.addEventListener('dragleave', function(e) {
            e.preventDefault();
            this.classList.remove('dragover');
        });
        
        fileUploadArea.addEventListener('drop', function(e) {
            e.preventDefault();
            this.classList.remove('dragover');
            
            const files = e.dataTransfer.files;
            if (files.length > 0) {
                const file = files[0];
                logoInput.files = files;
                logoInput.dispatchEvent(new Event('change'));
            }
        });
    }
});

// Обработчик ввода телефона с форматированием
document.addEventListener('DOMContentLoaded', function() {
    const phoneInput = document.getElementById('phone');
    
    if (phoneInput) {
        phoneInput.addEventListener('input', function (e) {
            let digits = e.target.value.replace(/\D/g, '');

            if (digits && digits[0] !== '7') {
                digits = '7' + digits;
            }

            if (digits.startsWith('7')) {
                digits = digits.slice(1);
            }

            const part1 = digits.substring(0, 3);
            const part2 = digits.substring(3, 6);
            const part3 = digits.substring(6, 8);
            const part4 = digits.substring(8, 10);

            let formatted = '+7';
            if (part1) {
                formatted += ' (' + part1;
                if (part1.length === 3) {
                    formatted += ')';
                }
            }
            if (part2) {
                formatted += ' ' + part2;
            }
            if (part3) {
                formatted += '-' + part3;
            }
            if (part4) {
                formatted += '-' + part4;
            }

            e.target.value = formatted;
        });
    }
});

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

// Функция для получения данных из API и отображения их на странице
async function fetchAndDisplayData() {
    const url = `/employer/user/${clientID}`;
    
    try {
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`Ошибка HTTP: ${response.status}`);
        }

        const data = await response.json();
        employer = data;
        console.log('Данные работодателя:', data);

        // Заполняем поля формы данными из API
        const innInput = document.getElementById("inn");
        const ogrnInput = document.getElementById("ogrn");
        const nameInput = document.getElementById("nameCompany");
        const descriptionInput = document.getElementById("description");
        const emailInput = document.getElementById("email");
        const phoneInput = document.getElementById("phone");
        
        if (innInput) innInput.value = data.inn || '';
        if (ogrnInput) ogrnInput.value = data.ogrn || '';
        if (nameInput) nameInput.value = data.name || '';
        if (descriptionInput) descriptionInput.value = data.description || '';
        if (emailInput) emailInput.value = data.email || '';
        if (phoneInput) phoneInput.value = data.phone || '';
        
        // Отображаем логотип если есть
        if (data.logo) {
            const logoPreview = document.getElementById('logo-preview');
            const logoImg = document.getElementById('logoPreview');
            const fileUploadArea = document.getElementById('file-upload-area');
            
            if (logoPreview && logoImg) {
                logoImg.src = `https://tworker.ru/employer/logo/${data.employer_id}`;
                logoPreview.style.display = 'inline-block';
            }
            
            if (fileUploadArea) {
                fileUploadArea.style.display = 'none';
            }
        }

    } catch (error) {
        console.error('Ошибка при загрузке данных:', error);
        showNotification('Ошибка при загрузке данных', 'error');
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

// Функция для обработки события нажатия на кнопку "next"
document.addEventListener('DOMContentLoaded', function() {
    const nextButton = document.getElementById("next");
    
    if (nextButton) {
        nextButton.addEventListener("click", async function(event) {
            event.preventDefault();
            
            // Валидация обязательных полей
            const requiredFields = ['inn', 'ogrn', 'nameCompany', 'email'];
            const missingFields = [];
            
            requiredFields.forEach(fieldId => {
                const field = document.getElementById(fieldId);
                if (field && !field.value.trim()) {
                    missingFields.push(fieldId);
                    field.style.borderColor = 'var(--tg-accent-danger)';
                } else if (field) {
                    field.style.borderColor = '';
                }
            });
            
            if (missingFields.length > 0) {
                showNotification('Пожалуйста, заполните все обязательные поля', 'error');
                return;
            }
            
            // Валидация email
            const emailInput = document.getElementById('email');
            if (emailInput && emailInput.value) {
                const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                if (!emailRegex.test(emailInput.value)) {
                    showNotification('Пожалуйста, введите корректный email', 'error');
                    emailInput.style.borderColor = 'var(--tg-accent-danger)';
                    return;
                } else {
                    emailInput.style.borderColor = '';
                }
            }
            
            showLoading();
            
            // Собираем FormData
            const logoInput = document.getElementById("logo");
            const formData = new FormData();
            formData.append("inn", document.getElementById("inn").value);
            formData.append("ogrn", document.getElementById("ogrn").value);
            formData.append("name", document.getElementById("nameCompany").value);
            formData.append("description", document.getElementById("description").value);
            formData.append("email", document.getElementById("email").value);
            formData.append("phone", document.getElementById("phone").value);
            formData.append("user_id", clientID);

            // Если файл выбран — добавляем его
            if (logoInput && logoInput.files && logoInput.files[0]) {
                formData.append("logo", logoInput.files[0]);
            }

            const url = "/employer/" + (employer ? employer.employer_id : 'new');
            console.log('Отправляем данные:', formData);

            try {
                const response = await fetch(url, {
                    method: "PUT",
                    body: formData
                });

                if (!response.ok) {
                    throw new Error(`HTTP error: ${response.status}`);
                }

                const jsonData = await response.json();
                console.log("Ответ сервера:", jsonData);
                
                hideLoading();
                showNotification('Данные успешно сохранены!', 'success');
                
                // После успешного сохранения переходим в лк
                setTimeout(() => {
                    window.location.href = "/employer/lk/";
                }, 1500);
                
            } catch (error) {
                hideLoading();
                console.error("Ошибка при обновлении:", error);
                showNotification('Ошибка при сохранении данных', 'error');
            }
        });
    }
});

// Анимация появления элементов
function animateElements() {
    const formSections = document.querySelectorAll('.form-section');
    
    formSections.forEach((section, index) => {
        section.style.opacity = '0';
        section.style.transform = 'translateY(20px)';
        
        setTimeout(() => {
            section.style.transition = 'all 0.6s ease';
            section.style.opacity = '1';
            section.style.transform = 'translateY(0)';
        }, index * 200);
    });
}

// Вызов функции для получения и отображения данных при загрузке страницы
document.addEventListener('DOMContentLoaded', function() {
    fetchAndDisplayData();
    
    // Запускаем анимации
    setTimeout(animateElements, 100);
    
    // Добавляем обработчики для кнопок
    const buttons = document.querySelectorAll('.btn-primary, .btn-secondary');
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
});