var userApi = Vue.resource('http://localhost:8080/users/')

Vue.component('user-row', {
    props: ['user', 'editMethod'],
    computed: {
        rolesString: function() {
                const roleNames = this.user.roles.map(role => role.roleName);
                return roleNames.join(', ');
            }
        },

    template: `
      <tr>
      <td>{{ user.id }}</td>
      <td>{{ user.firstName }}</td>
      <td>{{ user.lastName }}</td>
      <td>{{ user.age }}</td>
      <td>{{ user.email }}</td>
      <td>{{ rolesString }}</td>
      </tr>
  `
})

Vue.component('users-list', {
    props: ['users'],

    template:
        '<tbody><user-row v-for="user in users" :key="user.id" :user="user" /></tbody>',
    created: function () {
        userApi.get().then(result =>
            result.json().then(data =>
            data.forEach(user => this.users.push(user))))
    }

});

var app = new Vue({
    el: '#app',
    template: '<users-list :users="users" />',
    data: {
        users: []
    }
});