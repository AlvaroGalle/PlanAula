function showMsg(type, msg) {
    const arrClass = {
        'E': 'danger',
        'N': 'primary',
        'S': 'success',
        'W': 'warning'
    };
    $('#divErrMsg').remove();
    $('body').append(`
        <div id="divErrMsg"
             class="alert alert-${arrClass[type]} text-center position-fixed top-0 start-50 translate-middle-x mt-3 shadow fade show"
             style="z-index: 2000; width: auto; max-width: 90%; transition: opacity 0.5s;">
             ${msg}
        </div>
    `);
    setTimeout(() => {
        $('#divErrMsg').fadeOut(500, function() { $(this).remove(); });
    }, 4000);
}


function cambiarPagina(page) {
    $('#pageFilter').val(page);
    $('#filtrosForm').submit();
}

function activePanel(active) {
    document.getElementById(`${active}-nav`).classList.add("active");
    document.getElementById(`${active}-tab`).classList.add("active", "show");
}
