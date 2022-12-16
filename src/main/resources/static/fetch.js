$('document').ready(function() {

    $('.table .eBtn, .dBtn').on('click', function (event) {
        event.preventDefault();
        var href = $(this).attr('href');
        var text = $(this).text();
        if (text == 'Edit') {
            $.get(href, function (user, status) {
                $('.editForm #idEdit').val(user.id);
                $('.editForm #nameEdit').val(user.userName);
                $('.editForm #lastNameEdit').val(user.lastName);
                $('.editForm #ageEdit').val(user.age);
                $('.editForm #emailEdit').val(user.email);
                // $('.editForm #passwordEdit').val(user.password)
                $('.editForm #roleEdit').val(user.roles);

            });

            $('.editForm #editModal').modal();
        } else {
            $.get(href, function (user, status) {
                $('.deleteForm #idDel').val(user.id);
                $('.deleteForm #nameDel').val(user.userName);
                $('.deleteForm #lastNameDel').val(user.lastName);
                $('.deleteForm #ageDel').val(user.age);
                $('.deleteForm #emailDel').val(user.email);
                // $('.deleteForm #passwordDel').val(user.password);

            });

            $('.deleteForm #deleteModal').modal();

        }

    });
});