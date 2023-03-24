class User {
    constructor(firstName,lastName,age, email,password, roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age  = age;
        this.email  = email;
        this.password = password;
        this.roles = roles;
    }
}
//////////////////////////////////////////////////////
// call the function to display all users on page load
getAllUsers();
function getAllUsers() {
    // make a GET request to the server to retrieve all users
    fetch('http://localhost:8080/users/')
        .then(response => response.json())
        .then(data => {
            // loop through each user and create a new table row to display their information
            let html = '';
            data.forEach(user => {
                // create a new User object with the retrieved data
                const newUser = new User(user.firstName, user.lastName, user.age, user.email, user.password, user.roles);
                // add a new table row to display the user's information
                html += `
          <tr>
            <td>${user.id}</td>
            <td>${newUser.firstName}</td>
            <td>${newUser.lastName}</td>
            <td>${newUser.age}</td>
            <td>${newUser.email}</td>
            <td>${newUser.roles.map(role => role.roleName).join(", ")}</td>
            <td style="width: 100px"><button type="button" class="btn btn-info" data-bs-toggle="modal" data-bs-target="#editModal">
            Edit</button>
            <td style="width: 150px"><button type="button" class="btn btn-danger "data-bs-toggle="modal" data-bs-target="#deleteModal">
            Delete</button>
          </tr>
        `;
            });
            document.getElementById('usersTable').innerHTML = html;

        })
        .catch(error => console.error(error));
}
//////////////////////////////////////////////////////

    function getUser(id, callback) {
        // make a GET request to the server to retrieve the user details by ID
        fetch(`http://localhost:8080/users/${id}`)
            .then(response => response.json())
            .then(data => {
                const user = new User(data.firstName, data.lastName, data.age, data.email, data.password, data.roles);
                callback(user);
            })
            .catch(error => console.error(error));
    }
//////////////////////////////////////////////////////
document.getElementById('newUserForm').addEventListener("submit",newUser);
function newUser() {
    var form = document.getElementById('newUserForm');
    event.preventDefault();
    const newUser = new User(form.firstName.value, form.lastName.value,
        form.age.value, form.email.value,
        form.Password.value, Array.from(form.roleSelect.selectedOptions).map(option => ({
            roleName: option.value})));


    // send POST request with user data
    fetch('http://localhost:8080/users/', {
        method: 'POST',
        body: JSON.stringify(newUser),
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            // handle success response
            console.log('New user created!');
        })
        .catch(error => {
            // handle error
            console.error('Error creating user:', error);
        });

}
//////////////////////////////////////////////////////
// get a reference to the users table
const usersTable = document.getElementById('usersTable');

// add click event listener to the Edit and Delete buttons in each row
usersTable.addEventListener('click', (event) => {
    const button = event.target;
    const row = button.parentElement.parentElement;
    // if (button.classList.contains('btn-info')) { // Edit button clicked
        // get the user data from the row
    getUser(Number(row.cells[0].textContent),user =>{
        const editModal = document.getElementById('editModal');
        editModal.querySelector('#efirstName').value = user.firstName;
        editModal.querySelector('#elastName').value = user.lastName;
        editModal.querySelector('#eage').value = user.age;
        editModal.querySelector('#eemail').value = user.email;

        editModalInstance.show();
    })


})
