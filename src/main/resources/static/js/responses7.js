const currentUrl = window.location.href;
const url = new URL(currentUrl);
const params = new URLSearchParams(url.search);
const vacancyId = params.get('id');
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

// Функция для получения инициалов
function getInitials(fullName) {
    return fullName.split(' ').map(name => name.charAt(0)).join('').toUpperCase();
}

// Функция для форматирования даты
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('ru-RU', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}

// Функция для отправки видео резюме
function sendVideo(videoCvMessage) {
    const url = "/videoCv/send";
    const data = { videoCvMessage: videoCvMessage, userId: clientID };
    console.log('Sending video data:', data);
    
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }).then(response => {
        if (response.ok) {
            showNotification('Видео резюме отправлено', 'success');
        } else {
            showNotification('Ошибка при отправке видео', 'error');
        }
    }).catch(error => {
        console.error('Error sending video:', error);
        showNotification('Ошибка при отправке видео', 'error');
    });
}

// Функция для отклонения соискателя
function rejectApplicant(responseId) {
    if (confirm('Вы уверены, что хотите отклонить этого соискателя?')) {
        showLoading();
        
        fetch(`/response/${responseId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(response => {
            hideLoading();
            if (response.ok) {
                showNotification('Соискатель отклонен', 'success');
                // Удаляем карточку из DOM
                const card = document.querySelector(`[data-response-id="${responseId}"]`);
                if (card) {
                    card.style.animation = 'fadeOut 0.3s ease forwards';
                    setTimeout(() => {
                        card.remove();
                        checkEmptyState();
                    }, 300);
                }
            } else {
                showNotification('Ошибка при отклонении соискателя', 'error');
            }
        }).catch(error => {
            hideLoading();
            console.error('Error rejecting applicant:', error);
            showNotification('Ошибка при отклонении соискателя', 'error');
        });
    }
}

// Функция для проверки пустого состояния
function checkEmptyState() {
    const responsesList = document.getElementById('responses');
    const emptyState = document.getElementById('empty-state');
    
    if (responsesList.children.length === 0) {
        emptyState.style.display = 'block';
    } else {
        emptyState.style.display = 'none';
    }
}

// Функция для открытия модального окна
function openModal(title, content) {
    const modal = document.getElementById('response-modal');
    const modalTitle = document.getElementById('modal-title');
    const modalBody = document.getElementById('modal-body');
    
    modalTitle.textContent = title;
    modalBody.innerHTML = content;
    modal.classList.add('show');
    
    // Закрытие по клику вне модала
    modal.addEventListener('click', function(e) {
        if (e.target === modal) {
            closeModal();
        }
    });
}

// Функция для закрытия модального окна
function closeModal() {
    const modal = document.getElementById('response-modal');
    modal.classList.remove('show');
}

// Функция для создания карточки отклика
function createResponseCard(response) {
    const initials = getInitials(response.job_seeker.user.fullName);
    const responseDate = formatDate(response.createdAt || new Date());
    
    const card = document.createElement('div');
    card.className = 'response-card';
    card.setAttribute('data-response-id', response.id);
    
    card.innerHTML = `
        <div class="response-header">
            <div class="applicant-info">
                <div class="applicant-avatar">
                    ${initials}
                </div>
                <div class="applicant-details">
                    <h3>${response.job_seeker.user.fullName}</h3>
                    <p>Откликнулся ${responseDate}</p>
                </div>
            </div>
            <div class="response-status">
                Новый
            </div>
        </div>
        
        <div class="response-actions">
            <button class="action-button primary" onclick="viewResume('${response.comment}', '${response.job_seeker.user.fullName}')">
                <i class="fas fa-eye"></i>
                <span>Резюме</span>
            </button>
            <button class="action-button secondary" onclick="openChat('${response.job_seeker.user.username || ''}', ${response.job_seeker.user.id})">
                <i class="fas fa-comments"></i>
                <span>Чат</span>
            </button>
            <button class="action-button danger" onclick="rejectApplicant(${response.id})">
                <i class="fas fa-times"></i>
                <span>Отклонить</span>
            </button>
        </div>
    `;
    
    return card;
}

// Функция для просмотра резюме
function viewResume(comment, applicantName) {
    const content = `
        <div class="resume-content">
            <h4>Резюме ${applicantName}</h4>
            <div class="resume-text">
                <p>${comment || 'Резюме не приложено'}</p>
            </div>
            <div class="resume-actions">
                <button class="action-button primary" onclick="sendVideo('${comment}')">
                    <i class="fas fa-video"></i>
                    <span>Отправить видео</span>
                </button>
            </div>
        </div>
    `;
    
    openModal(`Резюме ${applicantName}`, content);
}

// Функция для открытия чата
function openChat(username, userId) {
    if (username && username.trim() !== '') {
        window.Telegram.WebApp.openTelegramLink(`https://t.me/${username}`);
    } else if (userId) {
        window.Telegram.WebApp.openTelegramLink(`tg://user?id=${userId}`);
    } else {
        showNotification('Не удалось открыть чат', 'error');
    }
}

// Функция для возврата назад
function back() {
    console.log('Going back to vacancy:', vacancyId);
    window.location.href = `/employer/vacancy/description?id=${vacancyId}`;
}

// Основная функция загрузки откликов
document.addEventListener('DOMContentLoaded', function() {
    showLoading();
    
    fetch(`/response/vacancy/${vacancyId}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error: ${response.status}`);
        }
        return response.json();
    }).then(data => {
        hideLoading();
        console.log('Responses data:', data);
        
        const responsesContainer = document.getElementById('responses');
        responsesContainer.innerHTML = '';
        
        if (data && data.length > 0) {
            data.forEach(response => {
                const responseCard = createResponseCard(response);
                responsesContainer.appendChild(responseCard);
            });
        } else {
            checkEmptyState();
        }
    }).catch(error => {
        hideLoading();
        console.error('Error loading responses:', error);
        showNotification('Ошибка при загрузке откликов', 'error');
        checkEmptyState();
    });
});

// Добавляем CSS для анимации исчезновения
const style = document.createElement('style');
style.textContent = `
    @keyframes fadeOut {
        from {
            opacity: 1;
            transform: translateY(0);
        }
        to {
            opacity: 0;
            transform: translateY(-20px);
        }
    }
    
    .resume-content {
        color: var(--tg-text-primary);
    }
    
    .resume-content h4 {
        margin-bottom: var(--spacing-md);
        color: var(--tg-text-primary);
    }
    
    .resume-text {
        background: var(--tg-bg-tertiary);
        border: 1px solid var(--tg-border-light);
        border-radius: var(--radius-md);
        padding: var(--spacing-md);
        margin-bottom: var(--spacing-md);
        max-height: 200px;
        overflow-y: auto;
    }
    
    .resume-text p {
        color: var(--tg-text-secondary);
        line-height: 1.6;
        margin: 0;
    }
    
    .resume-actions {
        display: flex;
        gap: var(--spacing-sm);
    }
`;
document.head.appendChild(style);