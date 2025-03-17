function showMsg(type, msg) {
    arrClass = [];
    arrClass['E'] = "danger";
    arrClass['N'] = "primary";
    arrClass['S'] = "success";
    arrClass['W'] = "warning";
    document.getElementById("divErrMsg") != null ? document.getElementById("divErrMsg").remove() : null;
    $('.card').first().before('<div id="divErrMsg" class="alert alert-' + arrClass[type] + '" style="z-index: 10" style="transform: translate(0px, 0px); top: 15px;"></div>');
    $('#divErrMsg').html(msg);
    setTimeout(() => {
        $('#divErrMsg').addClass('d-none');
    }, "4000");
}

function cambiarPagina(page) {
    $('#pageFilter').val(page);
    $('#filtrosForm').submit();
}

function activePanel(active) {
    document.getElementById(`${active}-nav`).classList.add("active");
    document.getElementById(`${active}-tab`).classList.add("active");
}