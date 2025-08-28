let clientID;
let jobSeekerId;

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

// Функция добавления нового видеорезюме
function addNewVideoCV() {
    const data = {
        userId: clientID, 
        message: "Перед тем как выбрать вакансию, запишите видео-резюме (в формате видео сообщения кружка telegram) с вашей презентацией. Видео будет доступно только работодателям, которым вы отправите отклик. Следуйте инструкцию по записи видео - резюме: https://drive.google.com/file/d/1CZz-rHORxlP_HcacAtY5jyQ56I5UpIV5/view"
    };
    
    showLoading();
    
    fetch("/message/send", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`Ошибка HTTP: ${response.status}`);
        }
        hideLoading();
        window.Telegram.WebApp.close();
    })
    .catch(error => {
        hideLoading();
        console.error('Ошибка при отправке сообщения:', error);
        alert('Произошла ошибка при отправке сообщения. Попробуйте позже.');
    });
}

// Функция поддержки
function support() {
    window.Telegram.WebApp.openTelegramLink(`https://t.me/Cherchent`);
    window.Telegram.WebApp.close();
}

// Функция отправки видео
function sendVideo(videoCvMessage) {
    const url = "/videoCv/send";
    const data = {
        videoCvMessage: videoCvMessage, 
        userId: clientID, 
        vacancyId: -1
    };
    
    showLoading();
    
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`Ошибка HTTP: ${response.status}`);
        }
        hideLoading();
        window.Telegram.WebApp.close();
    })
    .catch(error => {
        hideLoading();
        console.error('Ошибка при отправке видео:', error);
        alert('Произошла ошибка при отправке видео. Попробуйте позже.');
    });
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
        const response = await fetch(`/jobSeeker/statistics/${clientID}`, {
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
                totalVideoResumes: 0,
                totalViews: 0,
                favoriteVacancies: 0
            });
        }
    } catch (error) {
        console.log('Ошибка загрузки статистики, используем заглушки:', error);
        updateStatisticsDisplay({
            totalVideoResumes: 0,
            totalViews: 0,
            favoriteVacancies: 0
        });
    }
}

// Функция обновления отображения статистики
function updateStatisticsDisplay(stats) {
    const totalVideoResumes = document.getElementById('total-video-resumes');
    const totalViews = document.getElementById('total-views');
    const favoriteVacancies = document.getElementById('favorite-vacancies');
    
    if (totalVideoResumes) totalVideoResumes.textContent = stats.totalVideoResumes || 0;
    if (totalViews) totalViews.textContent = stats.totalViews || 0;
    if (favoriteVacancies) favoriteVacancies.textContent = stats.favoriteVacancies || 0;
}

// Функция анимации появления элементов
function animateElements() {
    const cards = document.querySelectorAll('.action-card');
    const emptyState = document.querySelector('.empty-state');
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
    
    // Анимация для пустого состояния
    if (emptyState) {
        setTimeout(() => {
            emptyState.style.transition = 'all 0.6s ease';
            emptyState.style.opacity = '1';
            emptyState.style.transform = 'scale(1)';
        }, 300);
    }
    
    // Анимация для секции статистики
    if (statsSection) {
        setTimeout(() => {
            statsSection.style.transition = 'all 0.6s ease';
            statsSection.style.opacity = '1';
            statsSection.style.transform = 'translateY(0)';
        }, 600);
    }
}

// Функция удаления видеорезюме
function delete_videoCv(videoCvId) {
    if (confirm("Вы действительно хотите удалить Ваше резюме?")) {
        showLoading();
        
        fetch(`/videoCv/${videoCvId}`, {
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
            const element = document.querySelector(`.resume-card[data-id='${videoCvId}']`);
            if (element) {
                element.remove();
                // Обновляем статистику после удаления
                loadStatistics();
            }
        })
        .catch(error => {
            hideLoading();
            console.error('Ошибка при удалении видеорезюме:', error);
            alert('Произошла ошибка при удалении видеорезюме. Попробуйте позже.');
        });
    }
}

// Функция загрузки файла
function triggerFileUpload() {
    document.getElementById('text-resume-upload').click();
}

// Основная инициализация при загрузке страницы
document.addEventListener('DOMContentLoaded', function() {
    // Загружаем видеорезюме
    fetch(`/videoCv/user/${clientID}`, {
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
        const resumeContainer = document.getElementById("resume-buttons");
        const emptyState = document.getElementById("empty-video-resume");
        
        if (data && data.length > 0) {
            // Скрываем пустое состояние
            if (emptyState) {
                emptyState.style.display = 'none';
            }
            
            // Создаем карточки для каждого видеорезюме
            data.forEach(videoCv => {
                const vacancy_url = `sendVideo('${videoCv.video_message}')`;
                
                const element = `
                    <div class="resume-card" onclick="${vacancy_url}" data-id="${videoCv.id}">
                        <div class="resume-icon">
                            <i class="fas fa-video" aria-hidden="true"></i>
                        </div>
                        <div class="resume-content">
                            <h3 class="resume-title">${videoCv.name}</h3>
                            <p class="resume-description">Видеорезюме</p>
                        </div>
                        <button class="resume-delete" onclick="event.stopPropagation(); delete_videoCv(${videoCv.id})" aria-label="Удалить резюме">
                            <i class="fas fa-trash" aria-hidden="true"></i>
                        </button>
                    </div>`;
                
                resumeContainer.innerHTML += element;
            });
        }
    })
    .catch(error => {
        console.error('Ошибка при загрузке видеорезюме:', error);
    });
    
    // Загружаем данные пользователя
    fetch(`/jobSeeker/user/${clientID}`, {
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
        jobSeekerId = data.id;
        
        if (data.textResume == null) {
            // Если резюме нет - показываем только загрузку
            document.getElementById("resume-not-uploaded").style.display = 'flex';
            document.getElementById("resume-uploaded").style.display = 'none';
            document.getElementById("resume-text").style.display = 'none';
        } else {
            // Если резюме есть - показываем обновление и просмотр, скрываем загрузку
            document.getElementById("resume-not-uploaded").style.display = 'none';
            document.getElementById("resume-uploaded").style.display = 'flex';
            document.getElementById("resume-text").style.display = 'flex';
            document.getElementById("resume-text").onclick = () => {
                window.open(data.textResumeLink, '_blank');
            };
        }
        
        // Загружаем статистику после получения данных пользователя
        loadStatistics();
    })
    .catch(error => {
        console.error('Ошибка при загрузке данных пользователя:', error);
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
    
    // Обработчик загрузки файла
    document.getElementById('text-resume-upload').addEventListener('change', async (event) => {
        const file = event.target.files[0];
        if (file) {
            showLoading();
            
            const formData = new FormData();
            formData.append('file', file);

            try {
                const response = await fetch(`/jobSeeker/resume/${clientID}`, {
                    method: 'PUT',
                    body: formData,
                });

                if (response.ok) {
                    hideLoading();
                    window.location.reload();
                } else {
                    hideLoading();
                    console.error('Ошибка при загрузке файла:', response.statusText);
                    alert('Произошла ошибка при загрузке файла. Попробуйте позже.');
                }
            } catch (error) {
                hideLoading();
                console.error('Ошибка при отправке файла:', error);
                alert('Произошла ошибка при отправке файла. Попробуйте позже.');
            }
        }
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