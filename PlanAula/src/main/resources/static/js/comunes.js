function showMsg(type, msg) {
    arrClass = [];
    arrClass['E'] = "bg-danger";
    arrClass['N'] = "bg-primary";
    arrClass['S'] = "bg-success";
    arrClass['W'] = "bg-warning";
    document.getElementById("divErrMsg") != null ? document.getElementById("divErrMsg").remove() : null;
    $('.card').first().after('<div id="divErrMsg" class="toastify on ' + arrClass[type] + ' toastify-center toastify-top" aria-live="polite" style="transform: translate(0px, 0px); top: 15px;"></div>');
    $('#divErrMsg').html(msg);
    setTimeout(() => {
        $('#divErrMsg').addClass('d-none');
    }, "4000");
}

function cambiarPagina(page) {
    $('#pageFilter').val(page);
    $('#filtrosForm').submit();
}