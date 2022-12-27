$(async function() {
    deleteUser();
    editUser();
    newUser();
    await getAllUsersTable();
    await currentUser();
    await getUser(id);

})

const userFetchService = {
    head: {
        'Content-Type': 'application/json',
    },
    getUserTest: async (id) => await fetch('/api/users/'+id),
    getAllRoles: async () => await fetch('/api/users/roles'),
    showUser: async () => await fetch('/api/users/user'),
    findAllUsers: async () => await fetch('/api/users')

}

async function getAllUsersTable() {
    let table = $('#AllUsersTable');

    await userFetchService.findAllUsers()
        .then(res => res.json())
        .then(users => {
            table.empty();
            users.forEach(user => {
                let tableFilling = `$(
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.userName}</td>
                            <td>${user.lastName}</td>   
                            <td>${user.age}</td>                      
                            <td>${user.email}</td>
                            <td>${user.roles.map(role => role.role.substring(5)).toString().replaceAll(",", ", ")}</td>
                            <td>
                                <button type="button" data-id="${user.id}" data-action="edit" class="btn btn-info" 
                                 data-toggle="modal" id="buttonEdit" data-target="#editModal">Edit</button>
                            </td>                  
                            <td>
                                <button type="button" data-id="${user.id}" data-action="delete" class="btn btn-danger"
                                 data-toggle="modal" id="buttonDelete" data-target="#deleteModal">Delete</button>
                            </td>
                        </tr>)`;
                table.append(tableFilling);
            })
        })
}

//редактируем юзера
$('#editModal').on('show.bs.modal', event => {
    let button = $(event.relatedTarget);
    let id = button.data('id');
    showEditModal(id);
})

async function showEditModal(id) {
    let user = await getUser(id);
    let form = document.forms["formEditUser"];
    form.id.value = user.id;
    form.userName.value = user.userName;
    form.lastName.value = user.lastName;
    form.age.value = user.age;
    form.email.value = user.email;
    form.password.value = user.password;

    $('#rolesEditUser').empty();

    await userFetchService.getAllRoles()
        .then(res => res.json())
        .then(roles => {
            roles.forEach(role => {
                let selectedRole = false;
                for (let i = 0; i < user.roles.length; i++) {
                    if (user.roles[i].role === role.role) {
                        selectedRole = true;
                        break;
                    }
                }
                let el = document.createElement("option");
                el.value = role.id;
                el.text = role.role.substring(5);
                if (selectedRole) el.selected = true;
                $('#rolesEditUser')[0].appendChild(el);
            })
        })
}

function editUser() {
    const editForm = document.forms["formEditUser"];
    editForm.addEventListener("submit", ev => {
        ev.preventDefault();
        let editUserRoles = [];
        if (editForm.roles !== undefined) {
            for (let i = 0; i < editForm.roles.options.length; i++) {
                if (editForm.roles.options[i].selected) editUserRoles.push({
                    id: editForm.roles.options[i].value,
                    role: "ROLE_" + editForm.roles.options[i].text
                })
            }
        }

        fetch('api/users/' + editForm.id.value, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                id: editForm.id.value,
                userName: editForm.userName.value,
                lastName: editForm.lastName.value,
                age: editForm.age.value,
                email: editForm.email.value,
                password: editForm.password.value,
                roles: editUserRoles
            })
        }).then(() => {
            $('#editFormCloseButton').click();
            getAllUsersTable();
        })
    })
}

///удаляем юзера
$('#deleteModal').on('show.bs.modal', ev => {
    let button = $(ev.relatedTarget);
    let id = button.data('id');
    showDeleteModal(id);
})

async function showDeleteModal(id) {
    let user = await getUser(id);
    let form = document.forms["formDeleteUser"];
    form.id.value = user.id;
    form.userName.value = user.userName;
    form.lastName.value = user.lastName;
    form.age.value = user.age;
    form.email.value = user.email;

    $('#rolesDeleteUser').empty();

    await userFetchService.getAllRoles()
        .then(res => res.json())
        .then(roles => {
            roles.forEach(role => {
                let selectedRole = false;
                for (let i = 0; i < user.roles.length; i++) {
                    if (user.roles[i].role === role.role) {
                        selectedRole = true;
                        break;
                    }
                }
                let el = document.createElement("option");
                el.text = role.role.substring(5);
                el.value = role.id;
                if (selectedRole) el.selected = true;
                $('#rolesDeleteUser')[0].appendChild(el);
            })
        });
}

function deleteUser() {
    const deleteForm = document.forms["formDeleteUser"];
    deleteForm.addEventListener("submit", ev => {
        ev.preventDefault();

        fetch('/api/users/'+ deleteForm.id.value,{

                method: 'DELETE',
                headers: {'Content-Type': 'application/json'}

            })
            .then(() => {
                $('#deleteFormCloseButton').click();
                getAllUsersTable();
            })
    })
}

//добавляем нового юзера
async function newUser() {
    await userFetchService.getAllRoles()
        .then(res => res.json())
        .then(roleList => {
            roleList.forEach(role => {
                let el = document.createElement("option");
                el.text = role.role.substring(5);
                el.value = role.id;
                $('#new-roles')[0].appendChild(el);
            })
        })

    const form = document.forms["newUserForm"];
    form.addEventListener('submit', addNewUser);

    function addNewUser(e) {
        e.preventDefault();
        let newUserRoles = [];
        if (form.roles !== undefined) {
            for (let i = 0; i < form.roles.options.length; i++) {
                if (form.roles.options[i].selected) newUserRoles.push({
                    id: form.roles.options[i].value,
                    role: form.roles.options[i].role
                })
            }
        }
        fetch('/api/users', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                userName: form.userName.value,
                lastName: form.lastName.value,
                age: form.age.value,
                email: form.email.value,
                password: form.password.value,
                roles: newUserRoles
            })
        }).then(() => {
            form.reset();
            getAllUsersTable();
            $('#allUsersTableLink').click();
        })
    }
}

//панель UserPage
async function currentUser() {
    userFetchService.showUser()
        .then(res => res.json())
        .then(data => {
            $('#headerUsername').append(data.name);
            let roleList = data.roles.map(role => role.role.substring(5)).toString().replaceAll(",", ", ");
            $('#headerRoles').append(roleList);

            let user = `$(
            <tr>
                <td>${data.id}</td>
                <td>${data.userName}</td>
                <td>${data.lastName}</td>
                <td>${data.age}</td>
                <td>${data.email}</td>
                <td>${roleList}</td>)`;
            $('#userPageBody').append(user);
            $('#userPageBody1').append(user);

        })
}

//функция getUser
async function getUser(id) {
    return await userFetchService.getUserTest(id).then(res=>res.json());
}