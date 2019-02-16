$(document).ready(function () {
    $(function () {
        var tooltips = $('[data-toggle="tooltip"]');
        tooltips.each(function (index, item) {
            item.setAttribute('title', $('#' + item.getAttribute('id') + '-info').html());
        });
        tooltips.tooltip();
    })
});

const AJAX_TIMEOUT = 5000;
function onAjaxFail(xhr, textStatus, errorThrown) {
    alert('Request to the server failed');
}

function deleteCook(cookId) {
    $.ajax({
        url: '/api/cooks/' + cookId,
        type: 'DELETE',
        success: function (result) {
            $('#cook-' + cookId).remove();
        },
        timeout: AJAX_TIMEOUT,
        fail: onAjaxFail
    });
}

function setCuisineCertification(cookId, cuisineId) {
    $.ajax({
        url: '/api/cooks/' + cookId + '/certification',
        type: 'POST',
        data:
            {cuisineId: cuisineId},
        success: function (result) {
            let certificationBadge = $('#cs-' + cookId + '-' + cuisineId);
            if (result.message === 'deleted') {
                certificationBadge.removeClass('btn-success');
                certificationBadge.addClass('btn-secondary');
            } else {
                certificationBadge.addClass('btn-success');
                certificationBadge.removeClass('btn-secondary');
            }
        },
        timeout: AJAX_TIMEOUT,
        fail: onAjaxFail
    });

}