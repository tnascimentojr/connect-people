
$(document).ready(function () {

    $("#coco").click(function (e) {
        e.preventDefault();
        
        $.ajax({
            url: 'http://localhost:8080/connectpeople/api/edit-user/' + localStorage.getItem("id"),
            type: "POST",
            data: JSON.stringify({
                name: $("#inputName").val(),
                password: $("#inputPassword").val(),
                phone: $("#inputPhone").val(),
                city: $("#inputCity").val()
            }),
            contentType: "application/json",
            async: true,
            success: successCallback,
            error: errorCallback
        });

    });
});

    function successCallback(response) {

        alert("HEYEYEYE");

        window.location.href = "/feed-page.html";
    }

    function errorCallback(request, status, error) {
        console.log(error);
    }