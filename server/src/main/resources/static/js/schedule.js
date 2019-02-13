var restaurantId = $("#restaurantId").val();
$('#calendar').fullCalendar({
    aspectRatio: 1.8,
    scrollTime: '00:00', // undo default 6am scrollTime
    header: {
        left: 'today prev,next',
        center: 'title',
        right: 'timelineDay,timelineThreeDays,month,listWeek'
    },
    defaultView: 'timelineDay',
    views: {
        timelineDay: {
            buttonText: 'Day (Timeline)',
            slotDuration: '01:00:00',
            minTime: '10:00:00'
        },
        agendaWeek: {
            buttonText: 'Week (Timeline)'
        },
        month: {
            buttonText: 'Month (Calendar)'
        },
        listWeek: {
            buttonText: 'Week (List)'
        },
        timelineThreeDays: {
            type: 'timeline',
            duration: {days: 3},
            buttonText: '3 Days (Timeline)',
            slotDuration: '01:00:00',
            minTime: '10:00:00'
        }
    },
    eventClick: function (calEvent, jsEvent, view) {
        $(this).css('border-color', 'black');
    },
    resourceLabelText: 'Cuisine List',
    resources: function (callback) {
        $.ajax({
            url: '/api/cuisines/list',
            type: 'GET',
            dataType: 'json',
            success: function (cuisines) {
                var resourceObjects = [];
                cuisines.forEach(function (cuisine) {
                    cuisine.resourceId = cuisine.id;
                    cuisine.title = cuisine.name;
                    resourceObjects.push(cuisine);
                });
                callback(resourceObjects);
            }
        });
    },
    events: function (start, end, timezone, callback) {
        $.ajax({
            url: '/api/shifts/list',
            type: 'GET',
            dataType: 'json',
            data: {
                // TODO We need to pass UTC time to server to get all things. Variable start which is parameter of this
                // function, as I understand, is wrong - it must be the beginning of the requested range in LOCAL TIME (client's local
//                            time zone) but insted of this it is the beginning of the requested range in UTC so we can skip necessary events)
                restaurantId: restaurantId,
                start: start.subtract(1, 'd').utc().format(),
                end: end.add(1, 'd').utc().format()
            },
            success: function (doc) {
                var events = [];
                doc.forEach(function (event) {
                    // event.color = getEventColor(event);
                    event.textColor = 'black';
                    event.start = moment(event.startDateTime);
                    event.end = moment(event.endDateTime);
                    event.resourceId = event.cuisine;
                    event.title = event.cook.fullName;
                    events.push(event);
                });
                callback(events);
            }
        });
    },
    schedulerLicenseKey: 'CC-Attribution-NonCommercial-NoDerivatives'
});

function buildScheduleForMonthSinceNow() {
    $.ajax({
       url: '/api/shifts/buildRoster',
        type: 'GET'
    });
    $('#calendar').fullCalendar('refetchEvents');

}
