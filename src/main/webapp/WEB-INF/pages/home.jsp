<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<html>
<head>
    <title>RnR at ThoughtWorks</title>
    <link href="../../scripts/css/datepicker.css" rel="stylesheet" type="text/css"/>
    <link href="../../scripts/css/rnr.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css"/>
    <link href='http://fonts.googleapis.com/css?family=Average+Sans' rel='stylesheet' type='text/css'>
    <script type="text/javascript" src="../../scripts/js/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="../../scripts/js/jquery-ui-1.8.18.custom.min.js"></script>
    <script type="text/javascript" src="../../scripts/js/jquery.validate.min.js"></script>
    <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
    <script type="text/javascript" src="../../scripts/js/rnr.js"></script>
</head>
<body>
<div id="page">
    <div>
        <img class="header-img" src="/images/logo.png"/>
    </div>
    <div id="midsection">
        <form name="startDate" action="/" method="POST" id="date_selector">
            <div id="main-section">
                <div id="errorContainer" class="field-label, errorContainer"></div>
                <div>
                    <label class="field-label" id="label-for-start-date-picker" for="start-date-picker">Start
                        Date:</label>
                    <input id="start-date-picker" class="banner" type="text" name="startDate"
                           value="${postedValues.startDate}" readonly>
                </div>
                <div>
                    <label class="field-label" id="label-for-rolloverdays-field" for="rolloverdays-field">Vacation
                        Balance:</label>
                    <input id="rolloverdays-field" type="text" name="rolloverdays" value="${postedValues.rollover}">
                    <span class="annotation">(as of 1/1/2014)</span>
                </div>
                <div>
                    <label class="field-label" id="label-for-accrual-rate" for="accrual-rate">Initial Accrual
                        Rate:</label>
                    <input id="accrual-rate" type="text" name="accrualRate"
                    <c:choose>
                        <c:when test="${not empty postedValues.accrualRate}">
                            <input id="accrual-rate" type="text" name="accrualRate"
                                   value="${postedValues.accrualRate}">
                        </c:when>

                        <c:otherwise>
                            <input id="accrual-rate" type="text" name="accrualRate"
                                   value="10">
                        </c:otherwise>
                    </c:choose>
                    <span class="annotation">days per year</span>
                </div>
                <div>
                    <label class="field-label" for="sales-force-text">Text from SalesForce Time Off:</label>
                    <textarea id="sales-force-text" type="text"
                              name="salesForceText">${postedValues.salesForceText}</textarea>
                    <span id="sales-force-text-help">
                        <div>
                            <button type="button" class="square-button" id="sales-force-help-button"
                                    onclick="showSalesForceHelpBox()">
                                ?
                            </button>
                            <div class="sales-force-help-box" id="sales-force-help-box-1">
                                <span>Navigate to <a href="https://our.thoughtworks.com"
                                                     target="_new">our.thoughtworks.com</a> and click the "Time Off" link in the left sidebar.</span>
                                <img class="sales-force-image" src="../../images/SFinstructions1.jpg"/>
                                <button type="button"
                                        class="square-button sales-force-button sales-force-close-window-button"
                                        onclick="showSalesForceHelpBox()">
                                    X
                                </button>
                                <button type="button" class="square-button sales-force-button sales-force-arrow-button"
                                        id="sales-force-next-button"
                                        onclick="showSecondSalesForcePage()">
                                    &#9658;
                                </button>
                            </div>
                            <div class="sales-force-help-box" id="sales-force-help-box-2">
                                <span>Copy and paste the contents of the entire page. (Cmd-A on Macs, Ctrl-A on PCs).</span>
                                <img class="sales-force-image" src="../../images/SFinstructions2.jpg"/>
                                <button type="button"
                                        class="square-button sales-force-button sales-force-close-window-button"
                                        onclick="showSalesForceHelpBox()">
                                    X
                                </button>
                                <button type="button" class="square-button sales-force-button sales-force-arrow-button"
                                        id="sales-force-back-button"
                                        onclick="showFirstSalesForcePage()">
                                    &#9668;
                                </button>
                            </div>
                        </div>
                    </span>
                </div>
            </div>
            <div id="right-bar">
                <div id="end-date-picker-div">
                    <c:set var="today" value="<%=new java.util.Date()%>"/>
                    <label class="fieldLabel" for="end-date-picker">Calculate my vacation days as of:</label>
                    <c:choose>
                        <c:when test="${not empty postedValues.endDate}">
                            <input id="end-date-picker" type="text" name="endDate" position="relative"
                                   value="${postedValues.endDate}" readonly>
                        </c:when>
                        <c:otherwise>
                            <input id="end-date-picker" type="text" name="endDate" position="relative"
                                   value="<fmt:formatDate pattern="MM/dd/yyyy" value="${today}" />" readonly>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div>
                    <input type="submit" value="Submit" class="button" id="submit-button">
                </div>
            </div>
        </form>
    </div>
    <div id="messages">
        <c:if test="${not empty postedValues.vacationDays}">
            <div id="balances">
                <span>As of ${postedValues.endDate}:</span>
                <br>
                <span class="vacation-days-container">Your vacation balance is </span>
                <span id="vacation-days">${postedValues.vacationDays}</span>
                <span class="vacation-days-container"> days.</span>
                <br>
                <span class="personal-days-container">Your personal day balance is </span>
                <span id="personal-days">${postedValues.personalDays}</span>
                <span class="personal-days-container"> days.</span>
            </div>
        </c:if>
        <c:if test="${not empty postedValues.capReachedMessage}">
            <div id="cap-message">${postedValues.capReachedMessage}</div>
        </c:if>
    </div>
</div>
</body>
</html>
