function submitForm() {
    document.getElementById("sort-form").submit();
}

setTimeout(function() {
    $('.alert').addClass("hide");
    $('.alert').removeClass("show");
}, 5000);

$('.fas').click(function () {
    $('.alert').addClass("hide");
    $('.alert').removeClass("show");
});