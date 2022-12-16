const usersMock = [
    {"id":1,"userName":"admin","password":"$2a$10$YP8WL7QmUTWqSXqA2Wrb4uoeKTIEr7FG4dypqAV1RVQ4TYo9eYQMu","lastName":"Zainetdinova","age":26,"email":"mail","roles":[{"id":1,"role":"ROLE_ADMIN","authority":"ROLE_ADMIN"}],"enabled":true,"authorities":[{"id":1,"role":"ROLE_ADMIN","authority":"ROLE_ADMIN"}],"username":"admin","accountNonExpired":true,"credentialsNonExpired":true,"accountNonLocked":true}
]
const fetchUser =async()=>{
    const response = await fetch("")
    return response.json()
}
//заподняем таблицу
const fillTable =(users)=> {
    const rowTemplate = document.querySelector("#user-row")

    const usersTbody = document.querySelector("#users-rows")


    users.forEach((user)=>{
        const userRowClone = rowTemplate.content.cloneNode(true)
        console.log(userRowClone)
    //    id userName lastName age mail role
        const{id, userName, lastName, age, email, roles} = user
        const tdId = document.createElement("td")
        tdId.textContent = id;
        const tdUserName = document.createElement("td")
        tdUserName.textContent = userName;
        const tdLastName = document.createElement("td")
        tdLastName.textContent = lastName;
        const tdAge = document.createElement("td")
        tdAge.textContent = age;
        const tdEmail = document.createElement("td")
        tdEmail.textContent = email;
        const tdRoles = document.createElement("td")
        tdRoles.textContent = roles.map((role)=>role.role).join(", ");
        const userTr = userRowClone.querySelector("tr")
        //"@{/admin/get/{id}(id=${user.getId()})}"
        const editButton = userTr.querySelector(".eBtn")
        editButton.setAttribute("href",`/admin/get/${id}`)
        const deleteButton = userTr.querySelector(".dBtn")
        deleteButton.setAttribute("href",`/admin/get/${id}`)
        userTr.prepend(tdId,tdUserName,tdLastName, tdAge, tdEmail, tdRoles)
        userTr.dataset.userId = id
        usersTbody.append(userTr)

    })
}

const editUser = async(event)=> {
    event.preventDefault()
    const editForm = document.querySelector("#edit-user")
    const editedUserName = editForm.nameEdit.value
    const editedLastName = editForm.lastNameEdit.value
    const editedAge = editForm.ageEdit.value
    const editedEmail = editForm.emailEdit.value
    const editedPassword = editForm.passwordEdit.value
    const userId = editForm.idEdit.value

    const roleEditOptions = editForm.roleEdit.querySelectorAll("option")
    const selectedOptions = [...roleEditOptions].filter((option)=> option.selected).map((option)=> option.value)
    console.log(selectedOptions)

    const body = {userId,userName:editedUserName, lastName:editedLastName, age:editedAge, email:editedEmail, password:editedPassword, roles:selectedOptions}

    console.log(body)
    // try {
        await fetch("/admin/updateUser", {
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(body),
            method: "POST"
        })

        const userRows = document.querySelector("#users-rows").querySelectorAll("tr")
        const currentUserRow = [...userRows].find((userRow)=>userRow.dataset.userId==userId)
        const userTdList = currentUserRow.querySelectorAll("td")
        userTdList[1].textContent=editedUserName
        userTdList[2].textContent=editedLastName
        userTdList[3].textContent=editedAge
        userTdList[4].textContent=editedEmail
        userTdList[5].textContent=selectedOptions.join(", ")


    // }catch{}



}
const addUser = async(event)=>{
    event.preventDefault()
    const addUserForm = document.querySelector("#addUser")
    const userName = addUserForm.name.value
    const lastName = addUserForm.lastname.value
    const password = addUserForm.password.value
    const age = addUserForm.age.value
    const email = addUserForm.email.value
    const rolesOptions = addUserForm.role.querySelectorAll("option")
    const roles = [...rolesOptions].filter((option)=> option.selected).map((option)=> option.value)

    const body = {userName, lastName, password, age, email, roles}
    // try {
        const response = await fetch("/admin/updateUser", {
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(body),
            method: "POST"
        })
        const user = await response.json()
    fillTable([{...body, id:111}])

    // }catch{}
}
const deleteUser = async (event)=>{
    event.preventDefault()
    const deleteForm = document.querySelector("#delete-user")
    const userId = deleteForm.idDel.value
    const body = {id:userId}

    // try {
    await fetch("/admin/updateUser", {
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(body),
        method: "POST"
    })

    const userRows = document.querySelector("#users-rows").querySelectorAll("tr")
    const currentUserRow = [...userRows].find((userRow)=>userRow.dataset.userId==userId)
    currentUserRow.remove()


    // }catch{}
}
$('document').ready( function() {
    const addUserForm = document.querySelector("#addUser")
    addUserForm.addEventListener("submit", addUser)

    // fetchUser().then((users)=>fillTable(users))

    fillTable(usersMock)
    $('.table .eBtn, .dBtn').on('click',function(event){
        event.preventDefault();
        var href = $(this).attr('href');
        var text = $(this).text();
        if(text=='Edit') {
            $.get(href, function (user, status) {
                $('.editForm #idEdit').val(user.id);
                $('.editForm #nameEdit').val(user.userName);
                $('.editForm #lastNameEdit').val(user.lastName);
                $('.editForm #ageEdit').val(user.age);
                $('.editForm #emailEdit').val(user.email);
                $('.editForm #passwordEdit').val(user.password)
               const currentRoles = user.roles.map((role)=>role.role)
                document.querySelectorAll('.editForm #roleEdit option').forEach((option)=>{
                    if (currentRoles.includes(option.value)){
                        option.selected = true
                    }
                })


            });

            $('.editForm #editModal').modal();
            document.querySelector("#edit-user").addEventListener("submit", editUser)
        }else{
            $.get(href, function (user, status) {
                $('.deleteForm #idDel').val(user.id);
                $('.deleteForm #nameDel').val(user.userName);
                $('.deleteForm #lastNameDel').val(user.lastName);
                $('.deleteForm #ageDel').val(user.age);
                $('.deleteForm #emailDel').val(user.email);
                $('.deleteForm #passwordDel').val(user.password);
                const currentRoles = user.roles.map((role)=>role.role)
                document.querySelectorAll('.deleteForm #roleDel option').forEach((option)=>{
                    if (currentRoles.includes(option.value)){
                        option.selected = true
                    }
                })

            });

            $('.deleteForm #deleteModal').modal();
            document.querySelector("#delete-user").addEventListener("submit", deleteUser)


        }

    });
});
