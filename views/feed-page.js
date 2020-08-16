

$(document).ready(function() {

    $("#friend-list-anchor").click(function(e) {

        window.location.href= "friends.html";
    });

    $("#edit_profile").click(function() {

        window.location.href = "editpage.html"
    })

    $("#addEvent").click( function(e) {
        //e.preventDefault();
        window.location.href= "addEvent.html";
    });

    $.ajax({
        url: 'http://localhost:8080/connectpeople/api/user/' + localStorage.getItem("id") + '/event-set',
        async: true,
        success: successCallback,
        error: errorCallback
    });
});

function successCallback(response) {
    response.forEach(event => {


        var content = '<div id="event" class="container">' +
            '<div class="row">' +
            '<div class="[ col-xs-12 col-sm-offset-2 col-sm-8 ]">' +
            '<ul class ="event-list">' +
            '<li>' +
            '<time datetime = "">' +
                '<span class="day">' + event.day + '</span>' +
                '<span class="month">' + event.month + '</span>' +
            '</time>' +
            '<div class="info">' +
            '<h2 class="title">' + event.title + '</h2> ' +
            '<br>' +
            '<p class="desc">' + event.location + '</p>' +
            '<p class="desc">' + event.time + '</p>' +
            '</div>' +
            '</li>' + '</ul>' + '</div> </div> </div>';


        $("#event-feed").append(content);
    })
}

function errorCallback(request,status, error) {
    console.log(error);
}
