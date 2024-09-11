const currentUrl = window.location.href;
const url = new URL(currentUrl);
const params = new URLSearchParams(url.search);
const vacancyId = params.get('id');

document.addEventListener('DOMContentLoaded', function(){
     fetch( `/vacancy/${vacancyId}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(response => {
                if (!response.ok) {

                    throw new Error(`Ошибка HTTP: ${response.status}`);
                }
                return response.json();

            }).then(data => {
                console.log (data);
                document.getElementById("employer_name").innerHTML = "Компания: " + data.employer.name;
            });
            }).then(data => {
                console.log (data);
                document.getElementById("employer_inn").innerHTML = "ИНН: " + data.employer.inn;
            });
            }).then(data => {
                console.log (data);
                document.getElementById("employer_ogrn").innerHTML = "ОГРН: " + data.employer.ogrn
                 });
            }).then(data => {
                console.log (data);
                document.getElementById("employer_logo").innerHTML = "Логотип компании: " + data.employer.logo;
            });
            }).then(data => {
                console.log (data);
                document.getElementById("employer_name").innerHTML = "Компания: " + data.employer.name;
            });
            }).then(data => {
                console.log (data);
                document.getElementById("employer_description").innerHTML = "Описание: " + data.employer.description;
            });


});