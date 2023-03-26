class User {
    constructor(firstName, lastName, age, email, password, roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
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
            <td style="width: 150px"><button type="button" class="btn btn-danger " data-bs-toggle="modal" data-bs-target="#deleteModal">
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
document.getElementById('newUserForm').addEventListener("submit", newUser);

function newUser() {
    var form = document.getElementById('newUserForm');
    event.preventDefault();
    const newUser = new User(form.firstName.value, form.lastName.value,
        form.age.value, form.email.value,
        form.Password.value, Array.from(form.roleSelect.selectedOptions).map(option => ({
            roleName: option.value
        })));


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
            getAllUsers();
            document.getElementById('tab1-tab').click();

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
    const id = Number(row.cells[0].textContent);
    console.log("usersTable " + id);
    // get the user data from the row
    getUser(id, user => {
        if (button.classList.contains('btn-info')) { // Edit button clicked
            editProcessing(user,id);
        } else if ((button.classList.contains('btn-danger'))) {
            deleteProcessing(user,id);
        }
    });
});

const editModal = document.getElementById('editModal');
const editModalInstance = new bootstrap.Modal(editModal);
function editProcessing(user,id) {
    editModal.querySelector('#editId').value = id;
    editModal.querySelector('#editfirstName').value = user.firstName;
    editModal.querySelector('#editlastName').value = user.lastName;
    editModal.querySelector('#editage').value = user.age;
    editModal.querySelector('#editemail').value = user.email;
    // clear all selected options
    let roleSelect = editModal.querySelector('#editroleSelect');
    //Reset role selector
    roleSelect.querySelectorAll('option').forEach(option => option.selected = false);
    // set selected roles based on user's roles
    roleSelect = editModal.querySelector('#editroleSelect');
    user.roles.map(role => role.roleName).forEach(roleName => roleSelect.querySelector(`[value="${roleName}"]`).selected = true);
    editModalInstance.show();
}

// add event listener to the Edit button inside the modal
editModal.querySelector('#editButton').addEventListener('click', (event) => {
    event.preventDefault();
    // get the updated user data from the form
    const updatedUser = {
        //get Id from html hidden field
        id: editModal.querySelector('#editId').value,
        firstName: editModal.querySelector('#editfirstName').value,
        lastName: editModal.querySelector('#editlastName').value,
        age: editModal.querySelector('#editage').value,
        email: editModal.querySelector('#editemail').value,
        password: editModal.querySelector('#editpassword').value,
        roles: Array.from(editModal.querySelectorAll('#editroleSelect option:checked'), option => ({
            roleName: option.value
        }))
    };
    // send a PATCH request to update the user data
    fetch(`http://localhost:8080/users/${updatedUser.id}`, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(updatedUser)
    }).then(response => {
        if (response.ok) {
            // close the modal if the request was successful
            editModalInstance.hide();
            // reload the table to show the updated user data
            getAllUsers();
        } else {
            // display an error message if the request was not successful
            console.error('Error updating user:', response);
            alert('Error updating user');
        }
    })
        .catch(error => {
            // display an error message if the request failed
            console.error('Error updating user:', error);
            alert('Error updating user');
        });
})


const deleteModal = document.getElementById('deleteModal');
const deleteModalInstance = new bootstrap.Modal(deleteModal);

function deleteProcessing(user, id) {
    console.log("deleteProcessing" + id);
    deleteModal.querySelector('#deleteId').value = id;
    deleteModal.querySelector('#deletefirstName').value = user.firstName;
    deleteModal.querySelector('#deletelastName').value = user.lastName;
    deleteModal.querySelector('#deleteage').value = user.age;
    deleteModal.querySelector('#deleteemail').value = user.email;
    // clear all selected options
    let roleSelect = deleteModal.querySelector('#deleteroleSelect');
    //reset role selector
    roleSelect.querySelectorAll('option').forEach(option => option.selected = false);
    // set selected roles based on user's roles
    roleSelect = deleteModal.querySelector('#deleteroleSelect');
    user.roles.map(role => role.roleName).forEach(roleName => roleSelect.querySelector(`[value="${roleName}"]`).selected = true);
    deleteModalInstance.show();

}
// add event listener to the Delete button inside the modal
deleteModal.querySelector('#deleteButton').addEventListener('click', (event) => {
    event.preventDefault();
    //get Id from html hidden field
    console.log("Delete button listener" + deleteModal.querySelector('#deleteId').value,);
    // send a PATCH request to update the user data
    fetch(`http://localhost:8080/users/${deleteModal.querySelector('#deleteId').value}`, {
        method: 'DELETE'
    }).then(response => {
        if (response.ok) {
            // close the modal if the request was successful
            deleteModalInstance.hide();
            // reload the table to show the updated user data
            getAllUsers();
        } else {
            // display an error message if the request was not successful
            console.error('Error updating user:', response);
            alert('Error updating user');
        }
    })
        .catch(error => {
            // display an error message if the request failed
            console.error('Error updating user:', error);
            alert('Error updating user');
        });
});


