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
});