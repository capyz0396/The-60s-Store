function submitForm() {
    document.getElementById("sort-form").submit();
}

setTimeout(function() {
    $('.alert').addClass("hide");
    $('.alert').removeClass("perform");
}, 5000);

$('.close-icon').click(function () {
    $('.alert').addClass("hide");
    $('.alert').removeClass("perform");
});