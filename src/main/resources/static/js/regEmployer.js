
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
                showNotification('Файл слишком большой. Максимальный размер: 5MB', 'error');
                logoInput.value = '';
                return;
            }
            
            // Проверяем тип файла
            if (!['image/png', 'image/jpeg', 'image/jpg'].includes(file.type)) {
                showNotification('Поддерживаются только файлы PNG, JPG, JPEG', 'error');
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
        
        // Клик по области загрузки
        fileUploadArea.addEventListener('click', function() {
            logoInput.click();
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
            
            // Валидация ИНН (10 или 12 цифр)
            const innInput = document.getElementById('inn');
            if (innInput && innInput.value) {
                const inn = innInput.value.toString();
                if (inn.length !== 10 && inn.length !== 12) {
                    showNotification('ИНН должен содержать 10 или 12 цифр', 'error');
                    innInput.style.borderColor = 'var(--tg-accent-danger)';
                    return;
                } else {
                    innInput.style.borderColor = '';
                }
            }
            
            // Валидация ОГРН (13 или 15 цифр)
            const ogrnInput = document.getElementById('ogrn');
            if (ogrnInput && ogrnInput.value) {
                const ogrn = ogrnInput.value.toString();
                if (ogrn.length !== 13 && ogrn.length !== 15) {
                    showNotification('ОГРН должен содержать 13 или 15 цифр', 'error');
                    ogrnInput.style.borderColor = 'var(--tg-accent-danger)';
                    return;
                } else {
                    ogrnInput.style.borderColor = '';
                }
            }
            
            showLoading();
            
            // Собираем данные формы
            const formData = {
                inn: document.getElementById("inn").value,
                ogrn: document.getElementById("ogrn").value,
                name: document.getElementById("nameCompany").value,
                description: document.getElementById("description").value,
                email: document.getElementById("email").value,
                phone: document.getElementById("phone").value,
                user_id: clientID
            };
            

            try {
                const response = await fetch(url, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(formData)
                });const url = "/employer/create";
            console.log('Отправляем данные:', formData);


                if (!response.ok) {
                    throw new Error(`HTTP error: ${response.status}`);
                }

                const jsonData = await response.json();
                console.log("Ответ сервера:", jsonData);
                
                hideLoading();
                showNotification('Регистрация успешно завершена!', 'success');
                
                // После успешной регистрации переходим в лк
                setTimeout(() => {
                    window.location.href = "/employer/lk/";
                }, 1500);
                
            } catch (error) {
                hideLoading();
                console.error("Ошибка при регистрации:", error);
                showNotification('Ошибка при регистрации. Попробуйте позже.', 'error');
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

// Инициализация при загрузке страницы
document.addEventListener('DOMContentLoaded', function() {
    // Запускаем анимации
    setTimeout(animateElements, 100);
    
    // Добавляем обработчики для кнопок
    const buttons = document.querySelectorAll('.btn-primary');
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
    
    // Добавляем валидацию в реальном времени
    const inputs = document.querySelectorAll('.form-input, .form-textarea');
    inputs.forEach(input => {
        input.addEventListener('blur', function() {
            validateField(this);
        });
        
        input.addEventListener('input', function() {
            if (this.style.borderColor === 'var(--tg-accent-danger)') {
                validateField(this);
            }
        });
    });
});

// Функция валидации поля
function validateField(field) {
    const value = field.value.trim();
    
    // Убираем красную рамку если поле заполнено
    if (value) {
        field.style.borderColor = '';
    }
    
    // Специальная валидация для email
    if (field.type === 'email' && value) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(value)) {
            field.style.borderColor = 'var(--tg-accent-danger)';
        } else {
            field.style.borderColor = 'var(--tg-accent-success)';
        }
    }
    
    // Специальная валидация для ИНН
    if (field.id === 'inn' && value) {
        if (value.length !== 10 && value.length !== 12) {
            field.style.borderColor = 'var(--tg-accent-danger)';
        } else {
            field.style.borderColor = 'var(--tg-accent-success)';
        }
    }
    
    // Специальная валидация для ОГРН
    if (field.id === 'ogrn' && value) {
        if (value.length !== 13 && value.length !== 15) {
            field.style.borderColor = 'var(--tg-accent-danger)';
        } else {
            field.style.borderColor = 'var(--tg-accent-success)';
        }
    }
}
