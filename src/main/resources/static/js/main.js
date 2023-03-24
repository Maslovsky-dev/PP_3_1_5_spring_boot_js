function newUser(form) {
    event.preventDefault();

    // get form data
        const firstName = form.firstName.value;
        const lastName = form.lastName.value;
        const age = form.age.value;
        const email = form.Email.value;
        const password = form.Password.value;
        const roles = Array.from(form.roleSelect.selectedOptions).map(option => ({
            roleName: option.value
        }));

    // create user object
    const user = {
        firstName: firstName,
        lastName: lastName,
        age: age,
        email: email,
        password: password,
        roles: roles
    };
    console.log(user);

    // send POST request with user data
    fetch('http://localhost:8080/users/', {
        method: 'POST',
        body: JSON.stringify(user),
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

    // prevent default form submission behavior
    return false;
}
