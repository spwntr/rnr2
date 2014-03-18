$().ready(function () {
    $("#date_selector").validate({
        errorLabelContainer: '.errorContainer',
        showErrors: function (errorMap, errorList) {
            if (errorList.length) {
                $('#errorContainer').html(errorList[0]['message']);
            }
        },
        highlight: function (element, errorClass) {
            $(element).parent('td').addClass('error');
        },
        unhighlight: function (element, errorClass) {
            $(element).parent('td').removeClass('error');
        },
        onkeyup: false,
        onfocusout: false,
        focusInvalid: true,
        rules: {
            startDate: "required",
            endDate: "required",
            accrualRate: {
                required: true,
                min: 10
            },
            rolloverdays: {
                required: true,
                number: true
            }
        },

        messages: {
            startDate: "Please enter a start date.",
            accrualRate: "Please enter an accrual rate of 10 days or more.",
            rolloverdays: {
                required: "Please enter a vacation balance."
            }
        }
    });
});

$(function () {
    $("#start-date-picker").datepicker({
        changeMonth: true,
        changeYear: true,
        defaultDate: new Date(),
        maxDate: 0,
        yearRange: "1993:+0",
        onSelect: function () {
            var minDate = $(this).datepicker('getDate');
            $("#end-date-picker").datepicker("option", "minDate", minDate);
        }
    });

    $("#end-date-picker").datepicker({
        changeMonth: true,
        changeYear: true,
        defaultDate: new Date(),
        minDate: "-1Y",
        maxDate: "+3Y",
        yearRange: "+0:+3",
        onSelect: function () {
            var maxDate = $(this).datepicker('getDate');
            $("#start-date-picker").datepicker("option", "maxDate", maxDate);
        }
    });
});

function checkdate(input) {
    var dateFormat = /^\d{2}\/\d{2}\/\d{4}$/ //Basic check for format validity
    var returnVal = false;
    if (!dateFormat.test(input.value))
        alert(input);
    return returnVal;
}

function showSalesForceHelpBox() {
    $('.sales-force-help-box').toggle();

    if ($('#messages').css('display') == 'none') {
        $('#messages').css('display', 'block');
    }
    else {
        $('#messages').css('display', 'none');
    }
}

function showFirstSalesForcePage() {
    document.getElementById('sales-force-help-box-1').style.zIndex = "-1";
    document.getElementById('sales-force-help-box-2').style.zIndex = "-2";
    document.getElementById('messages').style.display = "none";
}

function showSecondSalesForcePage() {
    document.getElementById('sales-force-help-box-1').style.zIndex = "-2";
    document.getElementById('sales-force-help-box-2').style.zIndex = "-1";
    document.getElementById('messages').style.display = "none";
}
