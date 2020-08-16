var hash = location.hash.substr(1);

$(document).ready(function() {

    $("#new-event-button").click( function(e) {
        //e.preventDefault();
        window.location.href= "newEvent.html";
    });

});